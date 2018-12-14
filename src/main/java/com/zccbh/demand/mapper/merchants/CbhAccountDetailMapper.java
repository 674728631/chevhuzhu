package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhAccountDetail;
import com.zccbh.util.base.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CbhAccountDetailMapper extends BaseMapper<CbhAccountDetail> {
    List<Map<String,Object>> selectByToken(@Param("toKen") String toKen);
    int saveSingle(Map<String, Object> map) throws Exception;
    List<Map<String,Object>> selectByYearAndMonth(@Param("toKen") String toKen);
}