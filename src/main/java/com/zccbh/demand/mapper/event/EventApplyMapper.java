package com.zccbh.demand.mapper.event;

import com.zccbh.demand.mapper.BaseMapper;

import java.util.Map;
public interface EventApplyMapper extends BaseMapper{
	
	Map<String, Object> getEventApply(Map<String, Object> map);
}
