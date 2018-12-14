package com.zccbh.demand.controller.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.zccbh.demand.mapper.event.EventCommentMapper;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.weChat.SpringContextHolder;


/**
 * 处理定时调度任务的统一入口
 * @author nixianhua
 *
 */
public class CommentJob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("【定时任务】开始......");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Map<String, Object> params = (Map<String, Object>) jobDataMap.get("params");
		SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
		Scheduler sche = schedulerFactory.getScheduler();
		String jobName = params.get("jobName").toString();
		EventService eventService = SpringContextHolder.getBean(EventService.class);
		EventCommentMapper eventCommentMapper = SpringContextHolder.getBean(EventCommentMapper.class);
		CarService carService = SpringContextHolder.getBean(CarService.class);
		try {
			Map map = new HashMap();
			
			SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = time.format(new Date());
			map.put("eventNo", params.get("eventNo"));
			List<Map<String,Object>> rlist = eventCommentMapper.findListByNo(map);
			if(rlist!=null&&rlist.size()>0){
				System.out.println("已经评论过-------");
			}else{
				map.put("statusEvent", 100);
				eventService.updateEvent(map);
				
				map.clear();
				map.put("maintenanceshopId", params.get("maintenanceshopId")); 
				map.put("eventNo", params.get("eventNo")); 
				map.put("labelContent", "服务好_技术很棒_很专业_人很nice"); 
				map.put("content", "好评"); 
				map.put("score", 5); 
				map.put("customerId", params.get("customerId"));
				map.put("createAt", date);
				eventCommentMapper.saveSingle(map);
			}
			//删除定时任务
			QuartzUtils.removeJob(sche, jobName);
			System.out.println("【定时任务】结束......");
		} catch (Exception e){
			e.printStackTrace();
			QuartzUtils.removeJob(sche, jobName);
			System.out.println("【定时任务】结束......");
		}
	}
}