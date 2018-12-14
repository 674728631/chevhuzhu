package com.zccbh.demand.controller.quartz;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.pojo.quartz.JobAndTrigger;
import com.zccbh.demand.service.quartz.JobAndTriggerImpl;
import com.zccbh.util.base.DataSourceContextHolder;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job")
public class QuartzController {

    @Autowired
    JobAndTriggerImpl jobAndTriggerImpl;

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @RequestMapping(value = "/queryjob", method=RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String queryjob(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
        DataSourceContextHolder.setDbType("qzDataSource");
        PageInfo<JobAndTrigger> jobAndTrigger = jobAndTriggerImpl.getJobAndTriggerDetails(pageNum, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("JobAndTrigger", jobAndTrigger);
        map.put("number", jobAndTrigger.getTotal());
        String str = JSONObject.toJSON(map).toString();
        return str;
    }

    @PostMapping(value = "/deletejob")
    public void deletejob(String jobClassName, String jobGroupName, String triggerGroupName) throws Exception {

        Scheduler scheduler2 = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, triggerGroupName);
        scheduler2.pauseTrigger(triggerKey); // 停止触发器
        boolean b = scheduler2.unscheduleJob(triggerKey); // 移除触发器
        b = scheduler2.deleteJob(JobKey.jobKey(jobClassName, jobGroupName)); // 删除任务
    }
}
