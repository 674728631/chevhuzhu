package com.zccbh.demand.service.customer;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.tools.corba.se.idl.constExpr.And;
import com.zccbh.demand.mapper.activities.MiddleCouponCustomerMapper;
import com.zccbh.demand.mapper.customer.InvitationCustomerMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.service.activities.CouponService;
import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.util.base.Base64;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.uploadImg.UploadFileUtil;

import org.aspectj.weaver.GeneratedReferenceTypeDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InvitationService {
    @Autowired
    private InvitationCustomerMapper invitationCustomerMapper;
    @Autowired
    private CouponService couponService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private UserCustomerMapper userCustomerMapper;
    @Autowired
    private UserCustomerService userCustomerService;
    @Autowired
    private MiddleCouponCustomerMapper middleCouponCustomerMapper;
    @Autowired
    private UserCustomerLogService userCustomerLogService;
    

    private Logger logger = LoggerFactory.getLogger(InvitationService.class);


    public PageInfo<Map<String, Object>> findInvitationList1(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;

        // 处理结束时间，多加一天，sql用的是between and
        if (Constant.toEmpty(paramModelMap.get("endTime"))) {
            String endTime = LocalDate.parse((String) paramModelMap.get("endTime")).plusDays(1).toString();
            paramModelMap.put("endTime", endTime);
        }

        // 判断是否有昵称查询条件
        String nickName = "";
        if (Constant.toEmpty(paramModelMap.get("customerPN"))) {
            nickName = Base64.getBase64(paramModelMap.get("customerPN").toString());
        }
        paramModelMap.put("nickName", nickName);
        // 统计总数的时候,计算百分比,分母只是传入时间的查询条件
        Map<String, Object> countParamMap = MapUtil.build().put("beginTime", paramModelMap.get("beginTime")).put("endTime", paramModelMap.get("endTime")).over();
        Map<String, Object> total = invitationCustomerMapper.countSelectInvitationList1(countParamMap);
        BigDecimal count = null;
        if (total == null) {
            count = new BigDecimal(0);
        } else {
            count = (BigDecimal) total.get("total");
        }
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectInvitationList1(paramModelMap);
        for (Map<String, Object> invitation : invitationList) {
            if (invitation.get("nickname") != null) {
                String portrait = String.valueOf(invitation.get("portrait"));
                invitation.put("nickname", Base64.getFromBase64((String) invitation.get("nickname")));
                invitation.put("portrait", portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait);
            }
            // 计算比例
            BigDecimal numbers = (BigDecimal) invitation.get("total");
            if (count.compareTo(new BigDecimal(0)) == 0) {
                invitation.put("ratio", 0);
            } else {
                invitation.put("ratio", numbers.divide(count, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            }
        }

        PageInfo<Map<String, Object>> invitationInfo = new PageInfo<>(invitationList);
        return invitationInfo;
    }

    public PageInfo<Map<String, Object>> findInvitationList2(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectInvitationList2(paramModelMap);
        for (Map<String, Object> invitation : invitationList) {
            if (invitation.get("nickname") != null) {
                String portrait = String.valueOf(invitation.get("portrait"));
                invitation.put("nickname", Base64.getFromBase64((String) invitation.get("nickname")));
                invitation.put("portrait", portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait);
            }
        }
        PageInfo<Map<String, Object>> invitationInfo = new PageInfo<>(invitationList);
        return invitationInfo;
    }

    public int selectInvitationCount1() throws Exception {
        return invitationCustomerMapper.selectInvitationCount1(new HashMap<String, Object>());
    }

    public int selectInvitationCount2(String customerId) throws Exception {
        return invitationCustomerMapper.selectInvitationCount2(customerId);
    }

    /**
     * 老用户邀请新用户 邀请记录
     *
     * @author xiaowuge
     * @date 2018年9月19日
     * @version 1.0
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String saveInvition(Map<String, Object> paramModelMap) throws Exception {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<String, Object>();

        logger.info("查询活动模板详情>>>");
        map.clear();
        map.put("modelId", paramModelMap.get("modelId"));
        Map<String, Object> couponModel = couponService.findModelDetail(map);
        if (couponModel == null) {
            return "4001";
        }

        map.clear();
//        Date tokenTime = DateUtils.getTokenTime();
//        String toKen = SecurityUtil.getToKen();
//        map.put("customerPN", paramModelMap.get("customerPN"));
//        map.put("toKen", toKen);
//        map.put("tokenaging", tokenTime);
//        map.put("openId", paramModelMap.get("openId"));
        map.put("source", "邀请活动");
        map.putAll(paramModelMap);
        logger.info("保存新用户>>>{}", map);
        userCustomerMapper.saveSingle(map);

        //获取新用户信息
//        List<Map<String, Object>> customerList = userCustomerMapper.findMore(map);
        String newCustomerId = map.get("id").toString();
        paramModelMap.put("newCustomerId", newCustomerId);

        logger.info("插入邀请记录>>>");
        map.clear();
        map.put("customerId", paramModelMap.get("customerId"));
//        map.put("invitedCustomerId", customerList.get(0).get("id"));
        map.put("invitedCustomerId", newCustomerId);
        map.put("status", "0");
        map.put("modelId", paramModelMap.get("modelId"));
        map.put("creatAt", time.format(new Date()));
        invitationCustomerMapper.saveSingle(map);

        logger.info("添加优惠券领取记录>>>");
        map.clear();
        map.put("modelId", paramModelMap.get("modelId"));
        Map<String, Object> coupon = couponService.selectByModelId(map);
        
        map.clear();
//        map.put("customerId", customerList.get(0).get("id"));
        map.put("customerId", newCustomerId);
        map.put("couponNo", coupon.get("couponNo"));
        map.put("status", "1");
        map.put("receiveTime", time.format(new Date()));
        middleCouponCustomerMapper.saveSingle(map);
        
        //获取用户信息
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("id", paramModelMap.get("customerId"));
        Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
        //用户邀请记录
    	reqMap.clear();
    	reqMap.put("customerId", customer.get("id"));
    	reqMap.put("customerPN", customer.get("customerPN"));
    	reqMap.put("source", customer.get("source"));
    	reqMap.put("createAt", customer.get("timeJoin"));
    	reqMap.put("currentStatus", customer.get("status"));
    	reqMap.put("optTime", DateUtils.formatDate(new Date()));
    	reqMap.put("optType", 11);
    	reqMap.put("optDesc", "用户邀请操作");
    	reqMap.put("recordTime", DateUtils.formatDate(new Date()));
    	userCustomerLogService.saveUserCustomerLog(reqMap);
    	
    	 //获取用户信息
        Map<String, Object> reqMap1 = new HashMap<>();
        reqMap1.put("id", newCustomerId);
        Map<String, Object> customer1 = userCustomerMapper.findUser(reqMap1);
        //用户领券
        reqMap1.clear();
        reqMap1.put("customerId", customer1.get("id"));
        reqMap1.put("customerPN", customer1.get("customerPN"));
        reqMap1.put("source", customer1.get("source"));
        reqMap1.put("createAt", customer1.get("timeJoin"));
        reqMap1.put("currentStatus", customer1.get("status"));
        reqMap1.put("optTime", DateUtils.formatDate(new Date()));
        reqMap1.put("optType", 14);
        reqMap1.put("optDesc", "用户领券");
        reqMap1.put("recordTime", DateUtils.formatDate(new Date()));
    	userCustomerLogService.saveUserCustomerLog(reqMap1);
    	
        return "0";
    }

    @Transactional
    public List<Map<String, Object>> selectByInvitedCustomerId(Map<String, Object> map) {
        return invitationCustomerMapper.selectByInvitedCustomerId(map);
    }

    /**
     * 修改关系状态
     *
     * @author xiaowuge
     * @date 2018年9月19日
     * @version 1.0
     */
    @Transactional
    public void updateInvitation(Map<String, Object> map) {
        try {
            invitationCustomerMapper.updateModel(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询通过商家邀请的的列表数据
     *
     * @param paramModelMap
     * @return
     */
    public PageInfo<Map<String, Object>> findInvitationListFromBusiness(Map<String, Object> paramModelMap) {
        paramModelMap.put("type", 10);
        // 处理结束时间，多加一天，sql用的是between and
        if (Constant.toEmpty(paramModelMap.get("endTime"))) {
            String endTime = LocalDate.parse((String) paramModelMap.get("endTime")).plusDays(1).toString();
            paramModelMap.put("endTime", endTime);
        }
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        // 统计总数的时候,计算百分比,分母只是传入时间的查询条件
        Map<String, Object> countParamMap = MapUtil.build().put("beginTime", paramModelMap.get("beginTime"))
                .put("endTime", paramModelMap.get("endTime"))
                .put("type", paramModelMap.get("type"))
                .over();
        Map<String, Object> total = invitationCustomerMapper.countInvitationListFromBusinessOrChannel(countParamMap);
        BigDecimal count = null;
        if (total == null) {
            count = new BigDecimal(0);
        } else {
            count = (BigDecimal) total.get("total");
        }

        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectInvitationListFromBusinessOrChannel(paramModelMap);

        // 计算比例
        if (invitationList != null && invitationList.size() > 0) {
            for (Map<String, Object> item : invitationList) {
                BigDecimal numbers = (BigDecimal) item.get("totalNum");
                if (count.compareTo(new BigDecimal(0)) == 0) {
                    item.put("ratio", 0);
                } else {
                    item.put("ratio", numbers.divide(count, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                }
                if (item.get("createAt") != null) {
                    item.put("createAt", getStringDate((Date) item.get("createAt")));
                }
            }
        }
        return new PageInfo<>(invitationList);
    }

    //获得string类型的时间
    private String getStringDate(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtils.getStringDateTime(date);
    }

    /**
     * 查询通过其他渠道邀请的的列表数据
     *
     * @param paramModelMap
     * @return
     */
    public PageInfo<Map<String, Object>> findInvitationListFromChannel(Map<String, Object> paramModelMap) {
        paramModelMap.put("type", 20);
        // 处理结束时间，多加一天，sql用的是between and
        if (Constant.toEmpty(paramModelMap.get("endTime"))) {
            String endTime = LocalDate.parse((String) paramModelMap.get("endTime")).plusDays(1).toString();
            paramModelMap.put("endTime", endTime);
        }
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        // 统计总数的时候,计算百分比,分母只是传入时间的查询条件
        Map<String, Object> countParamMap = MapUtil.build().put("beginTime", paramModelMap.get("beginTime"))
                .put("endTime", paramModelMap.get("endTime"))
                .put("type", paramModelMap.get("type"))
                .over();
        Map<String, Object> total = invitationCustomerMapper.countInvitationListFromBusinessOrChannel(countParamMap);
        BigDecimal count = null;
        if (total == null) {
            count = new BigDecimal(0);
        } else {
            count = (BigDecimal) total.get("total");
        }

        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectInvitationListFromBusinessOrChannel(paramModelMap);


        // 计算比例
        if (invitationList != null && invitationList.size() > 0) {
            for (Map<String, Object> item : invitationList) {
                BigDecimal numbers = (BigDecimal) item.get("totalNum");
                if (count.compareTo(new BigDecimal(0)) == 0) {
                    item.put("ratio", 0);
                } else {
                    item.put("ratio", numbers.divide(count, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                }
                if (item.get("createAt") != null) {
                    item.put("createAt", getStringDate((Date) item.get("createAt")));
                }
            }
        }
        return new PageInfo<>(invitationList);
    }

    /**
     * 查询用户邀请的详情
     *
     * @param paramModelMap
     * @return
     */
    public PageInfo<Map<String, Object>> listUserDetail(Map<String, Object> paramModelMap) throws Exception {
        // 处理结束时间，多加一天，sql用的是between and
        if (Constant.toEmpty(paramModelMap.get("endTime"))) {
            String endTime = LocalDate.parse((String) paramModelMap.get("endTime")).plusDays(1).toString();
            paramModelMap.put("endTime", endTime);
        }
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectUserDetail(paramModelMap);
        for (Map<String, Object> invitation : invitationList) {
            if (invitation.get("nickname") != null) {
                invitation.put("nickname", Base64.getFromBase64((String) invitation.get("nickname")));
            }
            if (invitation.get("portrait") != null) {
                String portrait = String.valueOf(invitation.get("portrait"));
                invitation.put("portrait", portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait);
            }
        }
        return new PageInfo<>(invitationList);
    }

    /**
     * 查询商家邀请的详情
     *
     * @param paramModelMap
     * @return
     */
    public PageInfo<Map<String, Object>> listBusinessDetail(Map<String, Object> paramModelMap) throws Exception {
        paramModelMap.put("type", 10);
        // 处理结束时间，多加一天，sql用的是between and
        if (Constant.toEmpty(paramModelMap.get("endTime"))) {
            String endTime = LocalDate.parse((String) paramModelMap.get("endTime")).plusDays(1).toString();
            paramModelMap.put("endTime", endTime);
        }
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectBusinessOrChannalDetail(paramModelMap);
        for (Map<String, Object> invitation : invitationList) {
            if (invitation.get("nickname") != null) {
                invitation.put("nickname", Base64.getFromBase64((String) invitation.get("nickname")));
            }
            if (invitation.get("portrait") != null) {
                String portrait = String.valueOf(invitation.get("portrait"));
                invitation.put("portrait", portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait);
            }
        }
        return new PageInfo<>(invitationList);
    }

    /**
     * 查询商家邀请的详情
     *
     * @param paramModelMap
     * @return
     */
    public PageInfo<Map<String, Object>> listChannelDetail(Map<String, Object> paramModelMap) throws Exception {
        paramModelMap.put("type", 20);
        // 处理结束时间，多加一天，sql用的是between and
        if (Constant.toEmpty(paramModelMap.get("endTime"))) {
            String endTime = LocalDate.parse((String) paramModelMap.get("endTime")).plusDays(1).toString();
            paramModelMap.put("endTime", endTime);
        }
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectBusinessOrChannalDetail(paramModelMap);
        for (Map<String, Object> invitation : invitationList) {
            if (invitation.get("nickname") != null) {
                invitation.put("nickname", Base64.getFromBase64((String) invitation.get("nickname")));
            }
            if (invitation.get("portrait") != null) {
                String portrait = String.valueOf(invitation.get("portrait"));
                invitation.put("portrait", portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait);
            }
        }
        return new PageInfo<>(invitationList);
    }

    /**
     * 用户拉新数据
     *
     * @author xiaowuge
     * @date 2018年11月9日
     * @version 1.0
     */
    public PageInfo<Map<String, Object>> findUserInvitationList(Map<String, Object> paramModelMap) throws Exception {

        int pageNo = 1;
        int pageSize = 10;
        // 统计总数的时候,计算百分比,分母只是传入时间的查询条件
        Map<String, Object> countParamMap = MapUtil.build().put("beginTime", paramModelMap.get("beginTime")).put("endTime", paramModelMap.get("endTime")).over();
        Map<String, Object> total = invitationCustomerMapper.countSelectInvitationList1(countParamMap);
        BigDecimal count = null;
        if (total == null) {
            count = new BigDecimal(0);
        } else {
            count = (BigDecimal) total.get("totalNum");
        }
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectInvitationList1(paramModelMap);
        for (Map<String, Object> invitation : invitationList) {
            if (invitation.get("nickname") != null) {
                String portrait = String.valueOf(invitation.get("portrait"));
                invitation.put("nickname", Base64.getFromBase64((String) invitation.get("nickname")));
                invitation.put("portrait", portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait);
            }
            // 计算比例
            BigDecimal numbers = (BigDecimal) invitation.get("total");
            if (count.compareTo(new BigDecimal(0)) == 0) {
                invitation.put("ratio", 0);
            } else {
                invitation.put("ratio", numbers.divide(count, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            }
        }

        PageInfo<Map<String, Object>> invitationInfo = new PageInfo<>(invitationList);
        return invitationInfo;
    }

    /**
     * 查询通过商家邀请的的列表数据
     *
     * @author xiaowuge
     * @date 2018年11月9日
     * @version 1.0
     */
    public PageInfo<Map<String, Object>> findBusinessInvitationList(Map<String, Object> paramModelMap) {
        paramModelMap.put("type", 10);

        int pageNo = 1;
        int pageSize = 10;
        // 统计总数的时候,计算百分比,分母只是传入时间的查询条件
        Map<String, Object> countParamMap = MapUtil.build().put("beginTime", paramModelMap.get("beginTime"))
                .put("endTime", paramModelMap.get("endTime"))
                .put("type", paramModelMap.get("type"))
                .over();
        Map<String, Object> total = invitationCustomerMapper.countInvitationListFromBusinessOrChannel(countParamMap);
        BigDecimal count = null;
        if (total == null) {
            count = new BigDecimal(0);
        } else {
            count = (BigDecimal) total.get("total");
        }

        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectInvitationListFromBusinessOrChannel(paramModelMap);

        // 计算比例
        if (invitationList != null && invitationList.size() > 0) {
            for (Map<String, Object> item : invitationList) {
                BigDecimal numbers = (BigDecimal) item.get("totalNum");
                if (count.compareTo(new BigDecimal(0)) == 0) {
                    item.put("ratio", 0);
                } else {
                    item.put("ratio", numbers.divide(count, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                }
                if (item.get("createAt") != null) {
                    item.put("createAt", getStringDate((Date) item.get("createAt")));
                }
            }
        }
        return new PageInfo<>(invitationList);
    }

    /**
     * 其他渠道拉新数据
     *
     * @author xiaowuge
     * @date 2018年11月9日
     * @version 1.0
     */
    public PageInfo<Map<String, Object>> findChannelInvitationList(Map<String, Object> paramModelMap) {
        paramModelMap.put("type", 20);
        int pageNo = 1;
        int pageSize = 10;
        // 统计总数的时候,计算百分比,分母只是传入时间的查询条件
        Map<String, Object> countParamMap = MapUtil.build().put("beginTime", paramModelMap.get("beginTime"))
                .put("endTime", paramModelMap.get("endTime"))
                .put("type", paramModelMap.get("type"))
                .over();
        Map<String, Object> total = invitationCustomerMapper.countInvitationListFromBusinessOrChannel(countParamMap);
        BigDecimal count = null;
        if (total == null) {
            count = new BigDecimal(0);
        } else {
            count = (BigDecimal) total.get("total");
        }

        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectInvitationListFromBusinessOrChannel(paramModelMap);


        // 计算比例
        if (invitationList != null && invitationList.size() > 0) {
            for (Map<String, Object> item : invitationList) {
                BigDecimal numbers = (BigDecimal) item.get("totalNum");
                if (count.compareTo(new BigDecimal(0)) == 0) {
                    item.put("ratio", 0);
                } else {
                    item.put("ratio", numbers.divide(count, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                }
                if (item.get("createAt") != null) {
                    item.put("createAt", getStringDate((Date) item.get("createAt")));
                }
            }
        }
        return new PageInfo<>(invitationList);
    }
    
    /**
     * 获取邀请数据
     * @author xiaowuge  
     * @date 2018年11月23日  
     * @version 1.0
     */
    public Map<String, Object> getInvitationInfo() throws Exception{
    	Map<String, Object> resultMap = new HashMap<>();
    	Map<String, Object> customerInfo = invitationCustomerMapper.totalCustomer();
    	resultMap.put("customerNum", customerInfo.get("customerNum"));
    	Map<String, Object> invitedCustomerInfo = invitationCustomerMapper.totalInvitationCustomer();
    	resultMap.put("invitedCustomerNum", invitedCustomerInfo.get("invitedCustomerNum"));
    	return resultMap;
    }
    
    /**
     * 拉新数据
     * @author xiaowuge  
     * @date 2018年12月5日  
     * @version 1.0
     */
    public PageInfo<Map<String, Object>> findInviationDate(Map<String, Object> paramModelMap) {
    	
    	 if (Constant.toEmpty(paramModelMap.get("endTime"))) {
             String endTime = LocalDate.parse((String) paramModelMap.get("endTime")).plusDays(1).toString();
             paramModelMap.put("endTime", endTime);
         }
        int pageNo = 1;
        int pageSize = 10;
        // 统计总数的时候,计算百分比,分母只是传入时间的查询条件
        Map<String, Object> countParamMap = MapUtil.build().put("beginTime", paramModelMap.get("beginTime"))
                .put("endTime", paramModelMap.get("endTime"))
                .put("type", paramModelMap.get("type"))
                .over();
//        Map<String, Object> total1 = invitationCustomerMapper.countSelectInvitationList1(countParamMap);
//        Map<String, Object> total2 = invitationCustomerMapper.countInvitationListFromBusinessOrChannel(countParamMap);
//        BigDecimal count = null;
//        if (total1 == null && total2 == null) {
//            count = new BigDecimal(0);
//        }else if(total1 == null && total2 != null){
//            count = (BigDecimal) total2.get("total");
//        }else if(total1 != null && total2 == null){
//        	count = (BigDecimal) total1.get("total");
//        }else{
//        	count = ((BigDecimal)total1.get("total")).add(((BigDecimal)total2.get("total")));
//        }
        Map<String, Object> total = invitationCustomerMapper.totalInvitation(countParamMap);
        BigDecimal count = null;
        if (total == null) {
			count = new BigDecimal(0);
		}else{
			count = (BigDecimal)total.get("total");
		}

        PageHelper.startPage(pageNo, pageSize);
//        List<Map<String, Object>> invitationList = invitationCustomerMapper.selectInvitationDate(paramModelMap);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.invitationList(paramModelMap);
        
        // 计算比例
        if (invitationList != null && invitationList.size() > 0) {
            for (Map<String, Object> item : invitationList) {
            	
            	 if (item.get("source") != null) {
            		 item.put("name", item.get("source"));
                 }
            	 System.out.println( item.get("numbers").getClass().toString());
                BigDecimal numbers = new BigDecimal( item.get("numbers").toString());
                if (count.compareTo(new BigDecimal(0)) == 0) {
                    item.put("ratio", 0);
                } else {
                    item.put("ratio", numbers.divide(count, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                }
                if (item.get("createAt") != null) {
                    item.put("createAt", item.get("createAt").toString());
                }
            }
        }
        return new PageInfo<>(invitationList);
    }
}


