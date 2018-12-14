package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhMaintenanceshopEmployee;
import com.zccbh.util.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CbhMaintenanceshopEmployeeMapper extends BaseMapper<CbhMaintenanceshopEmployee> {
    List<Map<String,Object>> getAssertEmployee(Map<String, Object> assertEmployee);

    List<Map<String,Object>> getEmployeeList(@Param("maintenanceshopId") Integer maintenanceshopId,@Param("id") String id);
}