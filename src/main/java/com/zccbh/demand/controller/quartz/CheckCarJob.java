package com.zccbh.demand.controller.quartz;

import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.test.CheVHuZhuTest;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import java.util.*;


/**
 * 处理定时调度任务的统一入口 支付和添加照片的定时器
 *
 */
public class CheckCarJob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("【定时任务】开始......");
		try {
			SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
			Scheduler scheduler = schedulerFactory.getScheduler();
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			if (jobKeys != null&&jobKeys.size()>0) {
				jobKeys.forEach(a ->{
					try {
						List<? extends Trigger> triggers = scheduler.getTriggersOfJob(a);
						triggers.forEach(b->{
							String[] jobName = a.getName().split("_");
							Date previousFireTime = b.getPreviousFireTime();
							if (jobName[0].equals("observationJob")&&previousFireTime !=null){
								String carId = jobName[1];
								System.err.print(carId+",");
								CheVHuZhuTest.quartz(Integer.valueOf(carId));
							}
						});
					}catch (Exception e){
						e.printStackTrace();
					}
				});
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("【定时任务】结束......");
	}


}