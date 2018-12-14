package com.zccbh.demand.mapper.activities;

import java.util.List;
import java.util.Map;

import com.zccbh.demand.mapper.BaseMapper;

public interface MiddleCouponCustomerMapper extends BaseMapper{
	List<Map<String, Object>> findPayCoupon(Map<String, Object> map);
}