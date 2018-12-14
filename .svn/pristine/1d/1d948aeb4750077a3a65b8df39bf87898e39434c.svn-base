package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhMessage;
import com.zccbh.util.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CbhMessageMapper extends BaseMapper<CbhMessage> {
    int saveMessage(Map<String, Object> map) throws Exception;

    List<CbhMessage> selectAllByToken(@Param(value = "toKen")String toKen);

    List<Map<String,Object>> selectByToken(String toKen);

    List<Map<String,Object>> selectServicePointsByToken(String toKen);

    CbhMessage selectByEventNoAndContent(@Param(value = "eventNo")String orderNo, @Param(value = "content")String content);

    void deleteByOrderNo(@Param(value = "eventNo")String orderNo);
}