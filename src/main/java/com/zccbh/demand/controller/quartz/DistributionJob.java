package com.zccbh.demand.controller.quartz;

import com.zccbh.demand.mapper.merchants.CbhEventMapper;
import com.zccbh.demand.mapper.merchants.CbhMessageMapper;
import com.zccbh.demand.mapper.user.MessageBackstageMapper;
import com.zccbh.demand.pojo.merchants.CbhEvent;
import com.zccbh.demand.service.order.OrderService;
import com.zccbh.demand.service.user.UserAdminService;
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.MapUtil;
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
public class DistributionJob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("【定时任务】开始......");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Map<String, Object> params = (Map<String, Object>) jobDataMap.get("params");
		SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
		Scheduler sche = schedulerFactory.getScheduler();
		OrderService orderService = SpringContextHolder.getBean(OrderService.class);
		CbhMessageMapper messageMapper = SpringContextHolder.getBean(CbhMessageMapper.class);
		MessageBackstageMapper messageBackstageMapper = SpringContextHolder.getBean(MessageBackstageMapper.class);
		UserAdminService userAdminService = SpringContextHolder.getBean(UserAdminService.class);
		String orderNo = params.get("jobName").toString();
		Map map = MapUtil.build().put("orderNo",orderNo).put("status",12).put("reasonCancellations","超时自动放弃接单").put("explanationCancellations","超时自动放弃接单").put("failReceiveOrderTime", DateUtils.formatDate(new Date())).over();
		try {
			orderService.updateOrder(map);
			//短信通知
			SmsDemo.sendSms(81,params.get("adminPN").toString(),params.get("licensePlateNumber").toString());
		} catch (Exception e){
			e.printStackTrace();
		}
		try {
			// 保存后台消息
			Map<String, Object> backMsgParam = new HashMap<>();
			backMsgParam.put("type",2);
			backMsgParam.put("orderNo",orderNo);
			backMsgParam.put("title","放弃接单");
			backMsgParam.put("content",params.get("licensePlateNumber").toString() + "的保险理赔订单被放弃接单，等待您重新分单！");
			backMsgParam.put("isSolve",1);
			backMsgParam.put("orderStatus",12);
			backMsgParam.put("createTime", DateUtils.formatDate(new Date()));
			messageBackstageMapper.saveSingle(backMsgParam);
			// 发送推送消息
			Map<String, Object> pushMsgMap = new HashMap<>();
			pushMsgMap.put("orderNo", backMsgParam.get("orderNo"));
			pushMsgMap.put("type", backMsgParam.get("type"));
			pushMsgMap.put("orderStatus", backMsgParam.get("orderStatus"));
			userAdminService.pushMessageToManager("order",(String) backMsgParam.get("title"),(String) backMsgParam.get("content"),pushMsgMap);
		}catch (Exception e){
			e.printStackTrace();
		}
		messageMapper.deleteByOrderNo(orderNo);
		QuartzUtils.removeJob(sche, orderNo);
		System.out.println("【定时任务】结束......");
	}
}