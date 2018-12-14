package com.zccbh.demand.controller.quartz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:/com/zccbh/config/spring-quartz1.xml")
@ContextConfiguration(locations = { "classpath:/com/zccbh/config/spring-quartz.xml", "classpath:/com/zccbh/config/spring.xml"})
public class JobTest {
	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	@Test
	public void testName() throws Exception {
		//schedulerFactory通过spring注入
		Scheduler sche = schedulerFactory.getScheduler();
		String job_name = "20180326111141533646";
		String cron = "0/10 * * * * ?";
//		String cron = "0 0 0/1 * * ? ";
//		String cron = "0 0/30 * * * ?";
		Map<String, Object> params = new HashMap<>();
		params.put("jobName", job_name);
//		params.put("orderInfoService", orderInfoService);MainJob
		QuartzUtils.addJob(sche, job_name, MainJob.class, params, cron);
//		System.in.read();
//		Thread.sleep(1000*60);
//		System.out.println("【移除定时】开始...");
//		QuartzUtils.removeJob(sche, job_name);
//		System.out.println("【移除定时】成功");
	}
}
