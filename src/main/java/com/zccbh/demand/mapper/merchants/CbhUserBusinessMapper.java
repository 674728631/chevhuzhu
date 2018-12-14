package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhUserBusiness;
import com.zccbh.util.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CbhUserBusinessMapper extends BaseMapper<CbhUserBusiness> {
    CbhUserBusiness selectByToken(@Param(value = "toKen")String toKen,@Param(value = "mobileNumber")String mobileNumber);

    CbhUserBusiness selectByMobileNumber(@Param(value = "userName") String userName,@Param(value = "passWord") String passWord);

    List<Map<String,Object>> getOrderList(Map<String, Object> parameterMap);

    CbhUserBusiness selectByPhoneNumber(String mobileNumber);

    Map<String,Object> getHomePageParameter(@Param(value = "toKen")String toKen);

    Map<String,Object> getTotalAmount(String toKen);

    Map<String,Object> getMyDetails(@Param(value = "toKen")String toKen);

    Map<String,String> getCustomerPhone(Integer id);

    List<String> getadministratorPhone();

    List<CbhUserBusiness> getUserBusinessByMaintenanceshopId(Integer id);

    List<Map<String,Object>> helpEachOtherList(Map<String, Object> parameterMap);
}