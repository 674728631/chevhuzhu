package com.zccbh.demand.controller.quartz;

import com.zccbh.demand.mapper.merchants.CbhEventMapper;
import com.zccbh.demand.mapper.merchants.CbhUserBusinessMapper;
import com.zccbh.demand.pojo.merchants.CbhEvent;
import com.zccbh.demand.pojo.merchants.CbhUserBusiness;
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 处理定时调度任务的统一入口
 * @author nixianhua
 *
 */
public class ReceiveCarJob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("【定时任务】开始......");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Map<String, Object> params = (Map<String, Object>) jobDataMap.get("params");
		SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
		Scheduler sche = schedulerFactory.getScheduler();
		String jobName = params.get("jobName").toString();
		String content = params.get("content").toString();
		CbhEventMapper eventMapper = SpringContextHolder.getBean(CbhEventMapper.class);
		CbhUserBusinessMapper businessMapper = SpringContextHolder.getBean(CbhUserBusinessMapper.class);
		String[] split = jobName.split("_");
		CbhEvent event = eventMapper.selectByEventNo(split[1]);
		if (event != null) {
			Integer maintenanceshopId = event.getMaintenanceshopId();
			List<CbhUserBusiness> CbhUserBusinessListis = businessMapper.getUserBusinessByMaintenanceshopId(maintenanceshopId);
			if (CbhUserBusinessListis != null&&CbhUserBusinessListis.size()>0) {
				for(int i = 0; i < CbhUserBusinessListis.size(); i++) {
					CbhUserBusiness business = CbhUserBusinessListis.get(i);
					String iosDeviceId = business.getIosDeviceId();
					String androidDeviceId = business.getAndroidDeviceId();
					//移动推送
					if (Constant.toEmpty(androidDeviceId) || Constant.toEmpty(iosDeviceId)) {
						Map<String, Object> parameterHashMap = new HashMap<>();
						parameterHashMap.put("orderNo", event.getEventNo());
						parameterHashMap.put("orderStatus", event.getStatusEvent());
						parameterHashMap.put("type", 5);
						try {
							if (Constant.toEmpty(androidDeviceId)) {
								SmsDemo.mobilePushMessage(20, 5, androidDeviceId, content, parameterHashMap);
							}
							if (Constant.toEmpty(iosDeviceId)) {
								SmsDemo.mobilePushMessage(10, 5, iosDeviceId, content, parameterHashMap);
							}
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			QuartzUtils.removeJob(sche, jobName);
			System.out.println("【定时任务】结束......");
		}
	}
}