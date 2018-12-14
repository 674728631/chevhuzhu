package com.zccbh.demand.service.event;

import com.zccbh.demand.controller.quartz.DistributionJob;
import com.zccbh.demand.controller.quartz.MainJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.mapper.business.UserBusinessMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.event.EventReceivecarMapper;
import com.zccbh.demand.mapper.user.UserAdminMapper;
import com.zccbh.demand.service.customer.MessageService;
import com.zccbh.demand.service.order.OrderService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动分单
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DistributionOrder {
    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventReceivecarMapper eventReceivecarMapper;

    @Autowired
    private UserBusinessMapper businessMapper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @Autowired
    private WeiXinUtils weiXinUtils;

    @Autowired
    private UserAdminMapper adminMapper;

    /**
     * 根据是否关注进行分单
     */
    @Transactional
    public String autoDistribution(Map<String, Object> map) throws Exception {
        Map result = eventMapper.findAttention(map);
        Integer maintenanceshopId;
        if(Constant.toEmpty( result) && Constant.toEmpty( result.get("maintenanceshopId"))){
            maintenanceshopId = (Integer)result.get("maintenanceshopId");
            if(maintenanceshopId != -1){
                distributionOfEvent(result);
                return "0";
            }
        }
        return "4000";
    }

    /**
     * 互助理赔分单
     * @param paramModelMap 订单维修厂等信息
     * @return 是否成功
     * @throws Exception
     */
    @Transactional
    public String distributionOfEvent(Map<String, Object> paramModelMap) throws Exception{
        Map map = new HashMap();
        //修改互助单
        map.clear();
        map.put("eventNo",paramModelMap.get("eventNo"));
        map.put("statusEvent",11);
        map.put("maintenanceshopId",paramModelMap.get("maintenanceshopId"));
        eventMapper.updateModel(map);
        //修改互助单接车详情
        map.clear();
        map.put("eventNo",paramModelMap.get("eventNo"));
        
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String date = time.format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 10);
		String date1 = time.format(calendar.getTime());
        map.put("createAt",date);
        map.put("timeReceiveCar", date1);
        eventReceivecarMapper.updateModel(map);
        //获取商家用户
        map.clear();
        map.put("maintenanceshopId",paramModelMap.get("maintenanceshopId"));
        map.put("typeUser",1);
        List<Map<String, Object>> businessList = businessMapper.findMore(map);
        if(businessList.size()>0){
            //推送消息给商家
            map.clear();
            map.put("eventNo",paramModelMap.get("eventNo"));
            map.put("businessId",(Integer)businessList.get(0).get("id"));
            map.put("licensePlateNumber",paramModelMap.get("licensePlateNumber"));
            map.put("model",paramModelMap.get("model"));
            map.put("type",21);
            map.put("title","接单消息");
            map.put("content","您有1个"+ paramModelMap.get("licensePlateNumber") +"的维修订单,接单时间还剩10分钟,请及时接单");
            messageService.saveMessage(map);
            //发送短信给商家
            SmsDemo.sendSms(41,(String) businessList.get(0).get("businessPN"),(String) paramModelMap.get("licensePlateNumber"));
            //推送消息给商家app
            map.clear();
            map.put("orderNo", paramModelMap.get("eventNo"));
            map.put("orderStatus", 11);
            map.put("licensePlateNumber", paramModelMap.get("licensePlateNumber"));
            map.put("type",21);
            if(Constant.toEmpty(businessList.get(0).get("iosDeviceId"))){
                SmsDemo.mobilePushMessage(10, 21,businessList.get(0).get("iosDeviceId").toString(), paramModelMap.get("licensePlateNumber") + "的订单，等待您接单", map);
            }
            if(Constant.toEmpty(businessList.get(0).get("androidDeviceId"))){
                SmsDemo.mobilePushMessage(20, 21,businessList.get(0).get("androidDeviceId").toString(), paramModelMap.get("licensePlateNumber") + "的订单，等待您接单", map);
            }
            //微信推送给用户
            map.clear();
            map.put("eventNo",paramModelMap.get("eventNo"));
            Map<String,Object> eventInfo = eventMapper.findSingle(map);
            if (Constant.toEmpty(eventInfo.get("openId"))) {
                map.clear();
                map.put("openid", eventInfo.get("openId"));
                map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
                map.put("model",eventInfo.get("model"));
                map.put("keyword1", paramModelMap.get("eventNo"));
                map.put("keyword2", "待接单");
                weiXinUtils.sendTemplate(10, map);
            }
            //分单后开启定时任务，20分钟后重新分单
            Scheduler sche = schedulerFactory.getScheduler();
            map.clear();
            map.put("jobName", paramModelMap.get("eventNo"));
            if(Constant.toEmpty(adminMapper.findAdminPN())){
                map.put("adminPN", adminMapper.findAdminPN().get("adminPN"));
            }else {
                map.put("adminPN", "15196296025");
            }
            map.put("licensePlateNumber", eventInfo.get("licensePlateNumber"));
            QuartzUtils.removeJob(sche,(String) paramModelMap.get("eventNo"));
            Map<String, String> dateMap = DateUtils.getDateMap(20 * 60 * 1000);
            String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
            QuartzUtils.addJob(sche,(String) paramModelMap.get("eventNo"), MainJob.class, map, cron);
        }
        return "0";
    }

    /**
     * 保险理赔分单
     */
    @Transactional
    public String distributionOfOrder(Map<String, Object> paramModelMap) throws Exception{
        Map map = new HashMap();
        //查询车辆详情信息
        map.put("carId",paramModelMap.get("carId"));
        Map<String,Object> carInfo = carMapper.findCarInfo(map);
        //修改互助单
        map.clear();
        map.put("orderNo",paramModelMap.get("orderNo"));
        map.put("status",11);
        map.put("maintenanceshopId",paramModelMap.get("maintenanceshopId"));
        map.put("distributionTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        orderService.updateOrder(map);
        //获取商家用户
        map.clear();
        map.put("maintenanceshopId",paramModelMap.get("maintenanceshopId"));
        map.put("typeUser",1);
        List<Map<String, Object>> businessList = businessMapper.findMore(map);
        if(businessList.size()>0){
            //推送消息给商家
            map.clear();
            map.put("eventNo",paramModelMap.get("orderNo"));
            map.put("businessId",(Integer)businessList.get(0).get("id"));
            map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
            map.put("model",carInfo.get("model"));
            map.put("type",121);
            map.put("title","保险理赔|接单消息");
            map.put("content","您有1个"+ carInfo.get("licensePlateNumber") +"的维修订单,接单时间还剩10分钟,请及时接单");
            messageService.saveMessage(map);
            //发送短信给商家
            SmsDemo.sendSms(41,(String) businessList.get(0).get("businessPN"),(String) carInfo.get("licensePlateNumber"));
            //推送消息给商家app
            map.clear();
            map.put("orderNo", paramModelMap.get("orderNo"));
            map.put("orderStatus", 11);
            map.put("licensePlateNumber", carInfo.get("licensePlateNumber"));
            map.put("type",121);
            if(Constant.toEmpty(businessList.get(0).get("iosDeviceId"))){
                SmsDemo.mobilePushMessage(10, 21,businessList.get(0).get("iosDeviceId").toString(), carInfo.get("licensePlateNumber") + "的订单，等待您接单", map);
            }
            if(Constant.toEmpty(businessList.get(0).get("androidDeviceId"))){
                SmsDemo.mobilePushMessage(20, 21,businessList.get(0).get("androidDeviceId").toString(), carInfo.get("licensePlateNumber") + "的订单，等待您接单", map);
            }
        }
        //微信推送给用户
        if (Constant.toEmpty(carInfo.get("openId"))) {
            map.clear();
            map.put("openid", carInfo.get("openId"));
            map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
            map.put("keyword1", paramModelMap.get("orderNo"));
            map.put("keyword2", "待接单");
            map.put("orderType", "order");
            weiXinUtils.sendTemplate(10, map);
        }
        //分单后开启定时任务，20分钟后重新分单
        Scheduler sche = schedulerFactory.getScheduler();
        map.clear();
        map.put("jobName", paramModelMap.get("orderNo"));
        if(Constant.toEmpty(adminMapper.findAdminPN())){
            map.put("adminPN", adminMapper.findAdminPN().get("adminPN"));
        }else {
            map.put("adminPN", "15196296025");
        }
        map.put("licensePlateNumber", carInfo.get("licensePlateNumber"));
        QuartzUtils.removeJob(sche,(String) paramModelMap.get("orderNo"));
        Map<String, String> dateMap = DateUtils.getDateMap(20 * 60 * 1000);
        String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
        QuartzUtils.addJob(sche,(String) paramModelMap.get("orderNo"), DistributionJob.class, map, cron);
        return "0";
    }
}
