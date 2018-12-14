package com.zccbh.demand.controller.quartz;

import com.zccbh.demand.mapper.system.DBODataMapper;
import com.zccbh.demand.service.marketing.MarketingService;
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.util.base.LocalThreadPool;
import com.zccbh.util.base.RedisUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 每周天定时查出 用户成本，平均充值，平均理赔 保存
 */
public class DBODataJob implements Job {

    private Logger logger = LoggerFactory.getLogger(DBODataJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        MarketingService marketingService = SpringContextHolder.getBean(MarketingService.class);
        DBODataMapper dboDataMapper = SpringContextHolder.getBean(DBODataMapper.class);
        RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);
        logger.info("查询用户成本等定时任务开始========================================>");
        Map<String, Object> out = new HashMap<>();
        Collection<Callable<Map<String, Object>>> taskList = new ArrayList<>();
        taskList.add(() -> marketingService.userCost());
        taskList.add(() -> marketingService.avgRecharge());
        taskList.add(() -> marketingService.avgEventAmt());
        try {
            List<Future<Map<String, Object>>> outList = LocalThreadPool.executorService.invokeAll(taskList);
            for (Future<Map<String, Object>> future : outList) {
                out.putAll(future.get());
            }
            out.put("timeNodes", getSqlDate());
            dboDataMapper.saveSingle(out);
            redisUtil.putAndTime("dbodata", out, 7, TimeUnit.DAYS);
            logger.info("查询用户成本等定时任务结束========================================>");
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private java.sql.Date getSqlDate() {
        java.util.Date nowDate = new java.util.Date();
        return new java.sql.Date(nowDate.getTime());
    }

}
