package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhOrder;
import com.zccbh.util.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CbhOrderMapper extends BaseMapper<CbhOrder> {
    Map<String,Object> selectOrderDetailByOrderNo(@Param(value = "token") String token, @Param(value = "orderNo") String orderNo);

    CbhOrder selectByEventNo(String orderNo);
}