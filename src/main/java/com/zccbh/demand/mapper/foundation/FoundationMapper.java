package com.zccbh.demand.mapper.foundation;

import com.zccbh.demand.mapper.BaseMapper;
import com.zccbh.demand.pojo.common.Foundation;

import java.util.Map;

public interface FoundationMapper extends BaseMapper<Foundation>{
    Integer updateData(Map<String,Object> map) throws Exception;

    /**
     * 查询本月的实际支出
     * @return
     */
    Map<String, Object> findCurrentMonthRealPaid();
    
    /**
     * 补贴
     */
    Map<String, Object> getSubsidy();
}