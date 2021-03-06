package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhMaintenanceshop;
import com.zccbh.util.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CbhMaintenanceshopMapper extends BaseMapper<CbhMaintenanceshop> {
    List<Map<String,Object>> selectByToken(@Param("toKen") String toKen);

    List<CbhMaintenanceshop> getByToken(@Param("toKen") String toKen);
    
    Map<String, Object> getMaintenanceShopDetail(Map<String, Object> requestMap);
    
    List<String> getShopQrcode();
}