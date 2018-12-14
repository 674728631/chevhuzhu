package com.zccbh.demand.mapper.customer;

import com.zccbh.demand.mapper.BaseMapper;
import com.zccbh.demand.pojo.user.Car;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MiddleCustomerQrcodeMapper extends BaseMapper {
    Map<String, Object> findByCustomerId(Map<String, Object> param);
}