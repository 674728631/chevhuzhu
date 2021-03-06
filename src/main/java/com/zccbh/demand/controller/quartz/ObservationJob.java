package com.zccbh.demand.controller.quartz;

import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.service.basic.DictionaryService;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.collect.Constant;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 处理定时调度任务的统一入口
 * @author nixianhua
 *
 */
public class ObservationJob implements Job{

	private Logger logger = LoggerFactory.getLogger(ObservationJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		logger.info("转入观察期【定时任务】开始......{}",jobDataMap);
		Map<String, Object> params = (Map<String, Object>) jobDataMap.get("params");
		SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
		Scheduler sche = schedulerFactory.getScheduler();
		String jobName = params.get("jobName").toString();
		WeiXinUtils weiXinUtils = SpringContextHolder.getBean(WeiXinUtils.class);
		CarService carService = SpringContextHolder.getBean(CarService.class);
		EventService eventService = SpringContextHolder.getBean(EventService.class);
		DictionaryService dictionaryService = SpringContextHolder.getBean(DictionaryService.class);
		UserCustomerMapper userCustomerMapper = SpringContextHolder.getBean(UserCustomerMapper.class);
		UserCustomerLogService userCustomerLogService = SpringContextHolder.getBean(UserCustomerLogService.class);
		try {
			Map map = new HashMap();
			//修改车辆保障时间
			Map<String, Object> carMap = carService.findCarById(new Integer(params.get("carId").toString()));
			
			//获取用户信息
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("id", carMap.get("customerId"));
            Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
            //进入保障中
            reqMap.clear();
            reqMap.put("customerId", customer.get("id"));
            reqMap.put("customerPN", customer.get("customerPN"));
            reqMap.put("source", customer.get("source"));
            reqMap.put("createAt", customer.get("timeJoin"));
            reqMap.put("currentStatus", customer.get("status"));
            reqMap.put("optTime", DateUtils.formatDate(new Date()));
            reqMap.put("optType", 6);
            reqMap.put("optDesc", "进入保障中");
            reqMap.put("recordTime", DateUtils.formatDate(new Date()));
        	userCustomerLogService.saveUserCustomerLog(reqMap);

        	String typeGuarantee;
        	if(null == params.get("typeGuarantee") || "".equals(params.get("typeGuarantee")))
				typeGuarantee = "1";
			else
				typeGuarantee = params.get("typeGuarantee").toString();

			typeGuarantee = Constant.toEmpty(carMap.get("typeGuarantee"))?carMap.get("typeGuarantee").toString():typeGuarantee;
			if (Constant.toEmpty(carMap.get("messageFlag"))) {
				Integer messageFlag = (Integer)carMap.get("messageFlag");
				if(Constant.toEmpty(params.get("openid"))){
					if(messageFlag==5 || messageFlag==7){
						map.put("messageFlag", 2);
					}
				}else {
					if(messageFlag==5){
						map.put("messageFlag", 7);
					}
				}
			}

			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (carMap.get("timeBegin") != null && DateUtils.getYearDate((Date) carMap.get("timeBegin")).compareTo(new Date()) == 1) {
				map.put("timeEnd", "2".equals(typeGuarantee) ? time.format(DateUtils.getYearDate((Date) carMap.get("timeBegin"))) : "");
			} else {
				map.put("timeBegin", time.format(new Date()));
				map.put("timeEnd", "2".equals(typeGuarantee) ? time.format(DateUtils.getYearDate(new Date())) : "");
			}
			map.put("id", params.get("carId").toString());
			map.put("status",20);
			carService.updateCar(map);
			eventService.updateDayNumber("guaranteeNum",1);
			//删除定时任务
			QuartzUtils.removeJob(sche, jobName);
			//微信推送
			map.clear();
			logger.info("观察期结束=====:  {}" , params);
			if (Constant.toEmpty(params.get("openid"))) {
//				map.put("openid", params.get("openid"));
//				map.put("content",params.get("content"));
//				map.put("keyword1", params.get("keyword1"));
//				map.put("keyword2", params.get("keyword2"));
//				map.put("url",params.get("url"));
//				weiXinUtils.sendTemplate(1, map);

				// 发送车辆进入保障的通知
				Map dictionary = dictionaryService.findSingle(MapUtil.build().put("type","observationTime").over());
				Map<String, String> param = new HashMap<>();
				param.put("openid", (String)params.get("openid"));
				param.put("licensePlateNumber", (String)carMap.get("licensePlateNumber"));
				param.put("observationTime",dictionary.get("value").toString());
				weiXinUtils.sendTemplate(13, param);
			}

			logger.info("转入观察期【定时任务】结束......");
		} catch (Exception e){
			logger.error("",e);
		}
	}

}