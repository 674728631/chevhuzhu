package com.zccbh.demand.mapper.activities;

import java.util.Map;


import com.zccbh.demand.mapper.BaseMapper;

public interface CouponMapper extends BaseMapper{
	
	Map<String, Object> selectByModelId(Map<String, Object> paramModelMap);
}