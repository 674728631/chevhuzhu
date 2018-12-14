package com.zccbh.demand.mapper.business;

import com.zccbh.demand.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface MaintenanceshopMapper extends BaseMapper{
	Map<String,Object> findCount(Map<String, Object> map);
	Map<String,Object> findDetail(Map<String, Object> map);

	List<Map<String, Object>> findShopList(Map<String, Object> map);
	List<Map<String, Object>> findCanDistribution(Map<String, Object> map);
	List<Map<String, Object>> findActivityShop(Map<String, Object> map);
	List<Map<String, Object>> countAttentionAndRegister(Map<String, Object> map);
	List<Map<String, Object>> chartShop(Map<String, Object> map);
	List<Map<String, Object>> chartChannel(Map<String, Object> map);
}