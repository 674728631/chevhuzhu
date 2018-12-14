package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhCar;
import com.zccbh.util.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CbhCarMapper extends BaseMapper<CbhCar> {
    List<Map<String,Object>> selectByCarId();

    void updateBycarId(@Param(value = "id") Integer id,@Param(value = "messageFlag")Integer status);
}