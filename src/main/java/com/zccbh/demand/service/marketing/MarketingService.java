package com.zccbh.demand.service.marketing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.controller.quartz.DBODataJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.controller.weChat.WeixinConstants;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.mapper.system.ChartMapper;
import com.zccbh.demand.mapper.system.DBODataMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.HttpUtils;
import com.zccbh.util.base.LocalThreadPool;
import com.zccbh.util.base.NormalUtils;
import com.zccbh.util.base.RedisUtil;
import com.zccbh.util.collect.Constant;
import org.omg.CORBA.ObjectHelper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.customer.WechatLoginMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import org.springframework.util.CollectionUtils;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author xiaowuge
 * @ClassName: MarketingService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月25日 下午4:32:27
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MarketingService {

    @Autowired
    private WechatLoginMapper wechatLoginMapper;
    @Autowired
    private UserCustomerMapper userCustomerMapper;
    @Autowired
    private RecordRechargeMapper recordRechargeMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private ChartMapper chartMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    WeiXinUtils weiXinUtils;
    @Autowired
    CarMapper carMapper;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DBODataMapper dboDataMapper;
    @Autowired
    private FoundationMapper foundationMapper;

    private Logger logger = LoggerFactory.getLogger(MarketingService.class);

    @Transactional
    public Map<String, Object> localShow(Map<String, Object> parameMap) throws Exception {
    	 if (Constant.toEmpty(parameMap.get("endTime"))) {
             String endTime = LocalDate.parse((String) parameMap.get("endTime")).plusDays(1).toString();
             parameMap.put("endTime", endTime);
         }
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> concernWeChat = wechatLoginMapper.concernWeChat(parameMap);
        resultMap.put("weChatConcernNum", concernWeChat.get("concernWeChat"));
        Map<String, Object> userRegister = userCustomerMapper.getRegisterNum(parameMap);
        resultMap.put("registerNum", userRegister.get("registerNum"));
        Map<String, Object> carInfo = wechatLoginMapper.selectObservationAndGuarantee(parameMap);
        resultMap.put("observationNum", carInfo.get("observationNum"));
        resultMap.put("guaranteeNum", carInfo.get("guaranteeNum"));
        Map<String, Object> rechargeInfo = recordRechargeMapper.marketingRecharge(parameMap);
//		resultMap.put("amtRcharge", rechargeInfo.get("amtRcharge"));
        resultMap.put("rechargeNum", rechargeInfo.get("rechargeNum"));
        Map<String, Object> eventResultInfo = eventMapper.eventResult(parameMap);
        resultMap.put("eventMoney", eventResultInfo.get("eventMoney"));
        resultMap.put("eventNum", eventResultInfo.get("eventNum"));
        return resultMap;
    }


    /**
     * 全局数据
     *
     * @return
     */
    public Map<String, Object> globalDate() {
        Map<String, Object> out = new HashMap<>();
        Collection<Callable<Map<String, Object>>> taskList = new ArrayList<>();
        taskList.add(this::weiXinSubscribeCount);
        taskList.add(this::getAllUserCount);
        taskList.add(this::getCarCountByStatus);
        taskList.add(this::userCost);
        taskList.add(this::avgRecharge);
//        taskList.add(this::avgGuaranteeNum);
        taskList.add(this::avgEventAmt);
//        taskList.add(this::toPay);
        taskList.add(this::noBanlance);
        taskList.add(this::eventNum);
        taskList.add(this::successEventPer);
        try {
            List<Future<Map<String, Object>>> futureList = LocalThreadPool.executorService.invokeAll(taskList);
            for (Future<Map<String, Object>> future : futureList) {
                out.putAll(future.get());
            }
//            executorService.shutdown();
        } catch (InterruptedException e) {
            logger.error("", e);
        } catch (ExecutionException e) {
            logger.error("", e);
        }
        return calculateUserCost(out);
    }

    // 用户成本
    public Map<String, Object> userCost() {
        Map<String, Object> rs = chartMapper.userCost();
        double userCost = NormalUtils.decimalToDouble((BigDecimal) rs.get("userCost"), true);
        // 正数说明没有用户成本
        if (userCost >= 0)
            rs.put("userCost", 0);
        else
            rs.put("userCost", -userCost);
        return rs;
    }

    // 平均充值金额
    public Map<String, Object> avgRecharge() {
        Map<String, Object> rs = chartMapper.avgRecharge();
        rs.put("avgRecharge", NormalUtils.decimalToDouble((BigDecimal) rs.get("avgRecharge"), true));
        return rs;
    }

    // 平均保障中天数
    public Map<String, Object> avgGuaranteeNum() {
        Map<String, Object> rs = chartMapper.avgGuaranteeNum();
        rs.put("avgGuaranteeNum", NormalUtils.decimalToInt((BigDecimal) rs.get("avgGuaranteeNum"), NormalUtils.ADD_ONE));
        return rs;
    }

    // 平均理赔金额
    public Map<String, Object> avgEventAmt() {
        Map<String, Object> rs = chartMapper.avgEventAmt();
        rs.put("avgEventAmt", NormalUtils.decimalToDouble((BigDecimal) rs.get("avgEventAmt"), true));
        return rs;
    }

    // 待支付,占比
    public Map<String, Object> toPay() {
        Map<String, Object> rs = chartMapper.toPay();
        BigDecimal toPayPer = (BigDecimal) rs.get("toPayPer");
        toPayPer = toPayPer.multiply(new BigDecimal(100));
        rs.put("toPayPer", NormalUtils.decimalToDouble(toPayPer, true) + "%");
        return rs;
    }

    // 余额不足数,占比
    public Map<String, Object> noBanlance() {
        Map<String, Object> rs = chartMapper.noBanlance();
        BigDecimal noBanlancePer = (BigDecimal) rs.get("noBanlancePer");
        noBanlancePer = noBanlancePer.multiply(new BigDecimal(100));
        rs.put("noBanlancePer", NormalUtils.decimalToDouble(noBanlancePer, true));
        return rs;
    }

    // 申请理赔次数
    public Map<String, Object> eventNum() {
        Map<String, Object> rs = chartMapper.eventNum();
        rs.put("eventPer", NormalUtils.decimalToDouble(((BigDecimal) rs.get("eventPer")).multiply(new BigDecimal(100)), true));
        return rs;
    }

    // 申请理赔通过次数
    public Map<String, Object> successEventPer() {
        Map<String, Object> rs = new HashMap<>();
        Foundation foundation;
        try {
            foundation = foundationMapper.findEntitySingle(new HashMap<>());
            rs.put("successEventPer", foundation.getAmtPaid().divide(foundation.getAmtTotal().add(foundation.getAllowance()),4,RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        } catch (Exception e) {
            logger.error("", e);
        }
        return rs;
    }

    /**
     * 全局理赔概况
     *
     * @return
     */
    public Map<String, Object> globalClaimsAnalysis() {
        Map<String, Object> out = new HashMap<>();
        // 截止每个月保障中车辆
        Map<String, Object> monthRs = chartMapper.getGuaranteeNumByMonth();
        String str = (String) monthRs.get("guaranteeNum");
        String[] guaranteeNums = str.split(",");
        out.put("guaranteeNum", guaranteeNums);
        // 截止每个月理赔车辆 ,定损通过的
        String rs = chartMapper.eventNumByMonth();
        List<Object> months = new ArrayList<>();
        List<Object> eventNos = new ArrayList<>();
        String[] temp = rs.split(",");
        for (String event : temp) {
            String[] tempp = event.split("-");
            months.add(tempp[0]);
            eventNos.add(tempp[1]);
        }
        out.put("month", months);
        out.put("eventNum", eventNos);
        // 理赔率
        Double[] eventPer = new Double[guaranteeNums.length];
        for (int i = 0; i < guaranteeNums.length; i++) {
            double d = Long.valueOf(eventNos.get(i).toString()) / NormalUtils.stringToDouble(guaranteeNums[i], true) * 100;
            eventPer[i] = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        out.put("eventPer", eventPer);
        return out;
    }

    /**
     * 理赔金额比例
     *
     * @return
     */
    public List<Map<String, Object>> eventAmt() {
        List<Map<String, Object>> rs = chartMapper.eventAmt();
//		LOGGER.debug("理赔金额比例:{}", rs);
        rs.forEach(map -> map.replace("name", map.get("name").toString().substring(2)));
//		LOGGER.debug("理赔金额比例:{}", rs);
        return rs;
    }

    /**
     * 虚拟补贴
     *
     * @return
     */
    public List<Map<String, Object>> rechargeNum() {
        List<Map<String, Object>> rs = chartMapper.rechargeNum();
//		LOGGER.debug(" 虚拟补贴比例debug:{}", rs);
        return rs;
    }


    /**
     * 维修厂结算数据
     *
     * @return
     */
    public List<Map<String, Object>> maintenanceshopAmt(Integer index) {
        List<Map<String, Object>> accountList = null;
        try {
            accountList = accountMapper.findMore(new HashMap<>());
            amtDecoding(accountList);
            Collections.sort(accountList, (o1, o2) -> {
                Double amtUnfreeze2 = (Double) o2.get("amtUnfreeze");
                Double amtUnfreeze1 = (Double) o1.get("amtUnfreeze");
                return amtUnfreeze2.compareTo(amtUnfreeze1);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        PageInfo<Map<String, Object>> accountInfo = new PageInfo<>(accountList.subList(0,9));
//		LOGGER.debug("返回结果：{}", index);
        if (1 == index)
            return accountList.subList(0, 10);
        else if (2 == index)
            return accountList;
        else
            throw new RuntimeException("参数错误!");
    }

    /**
     * 维修厂结算数据
     *
     * @return
     */
    public PageInfo<Map<String, Object>> maintenanceshopBill(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> accountList = accountMapper.findMore(paramModelMap);
        amtDecoding(accountList);
//        Collections.sort(accountList, (o1, o2) -> {
//            Double amtUnfreeze2 = (Double) o2.get("amtUnfreeze");
//            Double amtUnfreeze1 = (Double) o1.get("amtUnfreeze");
//            return amtUnfreeze2.compareTo(amtUnfreeze1);
//        });
        return new PageInfo<>(accountList);
    }

    /**
     * 受损部位统计
     *
     * @return
     */
    public List<Map<String, Object>> getDamagePosition() {
        List<Map<String, Object>> out = new ArrayList<>();
        String rs = chartMapper.getDamagePosition();
//		LOGGER.debug(" 受损部位统计:{}", rs);
        String[] position_count = rs.split(",");
        String[] name = position_count[0].split("-");
        String[] value = position_count[1].split("-");
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name[i]);
            map.put("value", value[i]);
            out.add(map);
        }
        return out;
    }

    /**
     * 微信关注数（微信数据）
     *
     * @return
     */
    private Map<String, Object> weiXinSubscribeCount() {
        Map<String, Object> outMap = new HashMap<>();
        int total = 0;
        try {
            String access_token = weiXinUtils.getAccessToken();
            String userList = HttpUtils.doGet(WeixinConstants.GET_USERS.replace("ACCESS_TOKEN", access_token).replace("NEXT_OPENID", ""));
            JSONObject userJson = JSONObject.parseObject(userList);
            if (null != userJson.get("total"))
                total = Integer.valueOf(userJson.get("total").toString());
        } catch (Exception e) {
            logger.error("获取微信关注数==>", e);
        }
        outMap.put("subscribeCount", total);
        return outMap;
    }

    /**
     * 总会员  ,观察期,  保障中, 退出计划, 退出再加入
     *
     * @return
     */
    private Map<String, Object> getCarCountByStatus() {
        Map<String, Object> count = chartMapper.getCarCountByStatus();
        count.put("reJoinNum", carMapper.findCarList3(new HashMap<>()).size());
        return count;
    }


    /**
     * 注册用户数，渠道用户，
     * 自然用户， 邀请用户
     *
     * @return
     */
    private Map<String, Object> getAllUserCount() {
        Map<String, Object> map = userCustomerMapper.getAllUserCount();
        Long signupCount = (Long) map.get("signupCount");
        Long natureUserCount = (Long) map.get("natureUserCount");
        Long invitationUserCount = (Long) map.get("invitationUserCount");
        Long channelUserCount = (Long) map.get("channelUserCount");
        double channel = NormalUtils.decimalToDouble(new BigDecimal((double) channelUserCount / signupCount * 100), true);
        double nature = NormalUtils.decimalToDouble(new BigDecimal((double) natureUserCount / signupCount * 100), true);
        double invite = NormalUtils.decimalToDouble(new BigDecimal((double) invitationUserCount / signupCount * 100), true);
        map.put("channelPer", channel);
        map.put("naturePer", nature);
        map.put("invitePer", invite);
        return map;
    }

    // 金钱解密
    private void amtDecoding(List<Map<String, Object>> accountList) throws Exception {
        for (Map<String, Object> account : accountList) {
            String amtUnfreeze = Constant.toOr((String) account.get("amtUnfreeze"), Constant.toReadPro("orKey"), "decrypt");
            Double amtUnfreezeT = NormalUtils.stringToDouble(amtUnfreeze, true);
            String amtFreeze = Constant.toOr((String) account.get("amtFreeze"), Constant.toReadPro("orKey"), "decrypt");
            Double amtFreezeT = NormalUtils.stringToDouble(amtFreeze, true);
            account.put("amtTotal", new BigDecimal(amtUnfreeze).add(new BigDecimal(amtFreeze)));
            account.put("amtUnfreeze", amtUnfreezeT);
            account.put("amtFreeze", amtFreezeT);
        }
    }

    /**
     * 不同用户的复购次数
     *
     * @return
     */
    public Map<String, Object> getReEnter() {
        // 获取1.2 3 9 元用户数
        Map<String, Object> out1 = chartMapper.getUserCountByAmt();
        // 获取1.2 3 9 元用户复购次数
//        Map<String, Object> out2 = chartMapper.getRechargeCountByAmt();

        Collection<Callable<Map<String, Object>>> taskList = new ArrayList<>();
        taskList.add(() -> chartMapper.getRechargeCountByAmt12());
        taskList.add(() -> chartMapper.getRechargeCountByAmt3());
        taskList.add(() -> chartMapper.getRechargeCountByAmt9x());
        taskList.add(() -> chartMapper.getRechargeCountByAmt9z());
        taskList.add(() -> chartMapper.getTotalUserCount());

        List<Future<Map<String, Object>>> futureList;
        try {
            futureList = LocalThreadPool.executorService.invokeAll(taskList);
            for (Future<Map<String, Object>> future : futureList) {
                out1.putAll(future.get());
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        // 计算复购率
        double per12 = NormalUtils.decimalToDouble(new BigDecimal((double) (long) out1.get("rechargeNum12") / (long) out1.get("userNum12") * 100), true);
        double per3 = NormalUtils.decimalToDouble(new BigDecimal((double) (long) out1.get("rechargeNum3") / (long) out1.get("userNum3") * 100), true);
        double per9x = NormalUtils.decimalToDouble(new BigDecimal((double) (long) out1.get("rechargeNum9x") / (long) out1.get("userNum9x") * 100), true);
        double per9z = NormalUtils.decimalToDouble(new BigDecimal((double) (long) out1.get("rechargeNum9z") / (long) out1.get("userNum9z") * 100), true);
        out1.put("per12", per12);
        out1.put("per3", per3);
        out1.put("per9x", per9x);
        out1.put("per9z", per9z);

        long total = (long) out1.get("rechargeNum12") + (long) out1.get("rechargeNum3") + (long) out1.get("rechargeNum9x") + (long) out1.get("rechargeNum9z");
        double totalPer = NormalUtils.decimalToDouble(new BigDecimal((double) total / (long) out1.get("num") * 100), true);
        out1.put("totalPer", totalPer);
        return out1;
    }

    /**
     * 计算环比
     *
     * @param out
     * @return
     */
    private Map<String, Object> calculateUserCost(Map<String, Object> out) {
        createDBODataJob();
        Map dbodata = redisUtil.get("dbodata", Map.class);
        if (CollectionUtils.isEmpty(dbodata)) {
            try {
                dbodata = dboDataMapper.findSingle(new HashMap<>());
            } catch (Exception e) {
                logger.error("", e);
                return out;
            }
            redisUtil.putAndTime("dbodata", dbodata, 7, TimeUnit.DAYS);
        }
        double new_userCost = Double.valueOf(out.get("userCost").toString());
        double new_avgRecharge = Double.valueOf(out.get("avgRecharge").toString());
        double new_avgEventAmt = Double.valueOf(out.get("avgEventAmt").toString());
        double old_userCost = Double.valueOf(dbodata.get("userCost").toString());
        double old_avgRecharge = Double.valueOf(dbodata.get("avgRecharge").toString());
        double old_avgEventAmt = Double.valueOf(dbodata.get("avgEventAmt").toString());
        out.put("userCost$", NormalUtils.decimalToDouble(new BigDecimal((new_userCost - old_userCost) / old_userCost * 100), true));
        out.put("avgRecharge$", NormalUtils.decimalToDouble(new BigDecimal((new_avgRecharge - old_avgRecharge) / old_avgRecharge * 100), true));
        out.put("avgEventAmt$", NormalUtils.decimalToDouble(new BigDecimal((new_avgEventAmt - old_avgEventAmt) / old_avgEventAmt * 100), true));
        return out;
    }

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    private void createDBODataJob() {
        String jobName = "DBODataJob_ervery_sunday_1";
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobKey = new JobKey(jobName, "JOB_GROUP_DBO");
        try {
            boolean exists = scheduler.checkExists(jobKey);
            if (exists)
                return;
            JobDetail jobDetail = newJob(DBODataJob.class).withIdentity(jobKey).build();
            TriggerKey triggerKey = new TriggerKey(jobName, "TRIGGER_GROUP_DBO");// 触发器
            // 0 55 23 ? * SUN
            Trigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule("0 55 23 ? * SUN")).build();// 触发器时间设定
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();// 启动
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
