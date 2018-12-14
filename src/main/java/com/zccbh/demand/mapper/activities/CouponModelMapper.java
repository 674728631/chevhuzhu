package com.zccbh.demand.mapper.activities;

import java.util.List;
import java.util.Map;

import com.zccbh.demand.mapper.BaseMapper;

public interface CouponModelMapper extends BaseMapper {

    Map<String, Object> selectByModelId(Map<String, Object> paramModelMap);

    List<Map<String, Object>> selectUserCoupon();
}