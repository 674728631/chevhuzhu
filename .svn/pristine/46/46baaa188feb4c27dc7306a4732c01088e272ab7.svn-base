package com.zccbh.demand.mapper.business;

import com.zccbh.demand.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MiddleCustomerMaintenanceshopMapper extends BaseMapper {
    int updateModelByCustomerTel(Map<String, Object> map) throws Exception;

    int updateModelById(Map<String, Object> map) throws Exception;

    Map<String, Object> selectByMobileNumber(@Param(value = "mobileNumber") String mobileNumber);

    /**
     * 根据手机号码，查询邀请的信息，专门为车妈妈
     *
     * @param mobileNumber
     * @return
     */
    Map<String, Object> selectByMobileNumberForChemama(@Param(value = "mobileNumber") String mobileNumber);

    int countUserAndShop(Map<String, Object> parameMap);

    int updateSubscribeStatus(Map<String, Object> map);
}