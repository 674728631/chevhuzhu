package com.zccbh.demand.controller.quartz;

import com.zccbh.demand.mapper.merchants.CbhEventMapper;
import com.zccbh.demand.mapper.merchants.CbhMessageMapper;
import com.zccbh.demand.mapper.user.MessageBackstageMapper;
import com.zccbh.demand.pojo.merchants.CbhEvent;
import com.zccbh.demand.service.user.UserAdminService;
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.SmsDemo;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 处理定时调度任务的统一入口
 * @author nixianhua
 *
 */
public class MainJob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("【定时任务】开始......");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Map<String, Object> params = (Map<String, Object>) jobDataMap.get("params");
		SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
		Scheduler sche = schedulerFactory.getScheduler();
		String eventNo = params.get("jobName").toString();
		CbhEventMapper eventMapper = SpringContextHolder.getBean(CbhEventMapper.class);
		CbhMessageMapper messageMapper = SpringContextHolder.getBean(CbhMessageMapper.class);
		MessageBackstageMapper messageBackstageMapper = SpringContextHolder.getBean(MessageBackstageMapper.class);
		UserAdminService userAdminService = SpringContextHolder.getBean(UserAdminService.class);
		CbhEvent event = eventMapper.selectByEventNo(eventNo);
		if (event != null) {
			event.setCancellationsReason("超时自动放弃接单");
			event.setCancellationsInstructions("超时自动放弃接单");
			event.setStatusEvent(12);
			// 设置放弃接单时间
			event.setFailReceiveOrderTime(new Date());
			//event.setMaintenanceshopId(null);
			eventMapper.updateByPrimaryKey(event);
			messageMapper.deleteByOrderNo(eventNo);
			QuartzUtils.removeJob(sche, eventNo);
			try {
				// 保存后台消息
				Map<String, Object> backMsgParam = new HashMap<>();
				backMsgParam.put("type",1);
				backMsgParam.put("orderNo",eventNo);
				backMsgParam.put("title","放弃接单");
				backMsgParam.put("content",params.get("licensePlateNumber") + "的救助订单被放弃接单，等待您重新分单！");
				backMsgParam.put("isSolve",1);
				backMsgParam.put("orderStatus",12);
				backMsgParam.put("createTime", DateUtils.formatDate(new Date()));
				messageBackstageMapper.saveSingle(backMsgParam);
				// 发送推送消息
				Map<String, Object> pushMsgMap = new HashMap<>();
				pushMsgMap.put("orderNo", backMsgParam.get("orderNo"));
				pushMsgMap.put("type", backMsgParam.get("type"));
				pushMsgMap.put("orderStatus", backMsgParam.get("orderStatus"));
				userAdminService.pushMessageToManager("event",(String) backMsgParam.get("title"),(String) backMsgParam.get("content"),pushMsgMap);
			}catch (Exception e){
				e.printStackTrace();
			}
			System.out.println("【定时任务】结束......");
		}
		try {
			//短信通知
			SmsDemo.sendSms(81,params.get("adminPN").toString(),params.get("licensePlateNumber").toString());
		} catch (Exception e){

		}
	}
}