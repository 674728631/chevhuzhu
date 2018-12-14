package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhEvent;
import com.zccbh.util.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CbhEventMapper extends BaseMapper<CbhEvent> {
    CbhEvent selectByEventNo(String eventNo);

    Map<String,Object> getOrderDetailByEventNo(@Param("eventNo") String eventNo, @Param("toKen") String toKen);

    Map<String,Object> getOrderDetailByStatus21(@Param("eventNo") String eventNo, @Param("toKen") String toKen);

    Map<String,Object> getOrderDetailByStatus52(@Param("eventNo") String eventNo, @Param("toKen") String toKen);

    Map<String,Object> getOrderDetailByStatus81(@Param("eventNo") String eventNo, @Param("toKen") String toKen,@Param("type") String type);

    Map<String,Object> getOrderDetailByStatus100(@Param("eventNo") String eventNo, @Param("toKen") String toKen);
}