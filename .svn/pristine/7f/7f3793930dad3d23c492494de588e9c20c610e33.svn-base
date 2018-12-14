package com.zccbh.demand.mapper.activities;

import com.zccbh.demand.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface CarWashActivityMapper extends BaseMapper {

    /**
     * 获取用户购买数量
     *
     * @param customerId
     * @return
     */
    List<Map<String, Object>> getUserCouponList(String customerId);

    /**
     * 根据优惠券号获取优惠券信息
     *
     * @param couponNo
     * @return
     */
    Map<String, Object> getCouponByNo(String couponNo);

    int updateByNo(Map<String, Object> param);

    Map<String, Object> carWashTotalBuyNumber(Map<String, Object> param);

    List<Map<String, Object>> statCarWashActivity(Map<String, Object> param);

    List<Map<String, Object>> statCarWashActivity1(Map<String, Object> inParam);

    List<Map<String, Object>> statCarWashActivity2(Map<String, Object> inParam);

    Map<String, Object> carWashTotalBuyNumber1(Map<String, Object> inParam);

    Map<String, Object> carWashTotalBuyNumber2(Map<String, Object> inParam);
}
