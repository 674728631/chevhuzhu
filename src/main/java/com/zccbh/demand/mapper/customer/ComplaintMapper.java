package com.zccbh.demand.mapper.customer;

import com.zccbh.demand.mapper.BaseMapper;

import java.util.Map;

public interface ComplaintMapper extends BaseMapper{
    Map<String,Object> findCount(Map<String, Object> paramModelMap);
}
