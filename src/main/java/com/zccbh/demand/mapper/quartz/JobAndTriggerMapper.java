package com.zccbh.demand.mapper.quartz;


import com.zccbh.demand.pojo.quartz.JobAndTrigger;

import java.util.List;

public interface JobAndTriggerMapper {
    List<JobAndTrigger> getJobAndTriggerDetails();
}
