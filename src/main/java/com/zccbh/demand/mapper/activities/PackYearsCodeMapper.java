package com.zccbh.demand.mapper.activities;

import com.zccbh.demand.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface PackYearsCodeMapper extends BaseMapper {

    Map<String, Object> queryByCode(@Param("uuid") String uuid) throws Exception;

    int updateCode4Use(@Param("customerId") String customerId, @Param("carId") String carId, @Param("use_status") int useStatus, @Param("uuid") String uuid);
}
