package com.zccbh.demand.mapper.customer;

import com.zccbh.demand.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserCustomerMapper extends BaseMapper {
    int findEventCount();

    Map<String, Object> getHomePageData();

    Map<String, Object> getCustomerinfo(String mobileNumber);

    Map<String, Object> selectCount(@Param(value = "customerId") String customerId);

    Map<String, Object> selectByMobileNumber(@Param(value = "customerPN") String mobileNumber);

    Map<String, Object> selectByTokenAndMobileNo(@Param(value = "customerpn") String customerpn, @Param(value = "token") String token);

    Map<String, Object> findAmtBycar(Map<String, Object> map);

    Map<String, Object> findCount(Map<String, Object> paramModelMap);

    Map<String, Object> findFirstCarByOpenId(@Param(value = "customerId") String openId);

    Map<String, Object> findCustomerAndCar(Map<String, Object> map);

    Map<String, Object> findUser(Map<String, Object> map);

    Map<String, Object> findUserInfo(Map<String, Object> map);

    List<Map<String, Object>> findUserList();

    List<Map<String, Object>> findShopCustomer(Map<String, Object> map);

    List<Map<String, Object>> selectRechargeList(Map<String, Object> map);

    List<Map<String, Object>> chartRegister(Map<String, Object> map);

    /**
     * 查询注册用户数据, 渠道拉新数据统计
     *
     * @param paramModelMap
     * @return
     */
    List<Map<String, Object>> chartRegisterForChannel(Map<String, Object> paramModelMap);

    /**
     * 根据用户的id，获取头像和昵称
     *
     * @param customerId
     * @return
     */
    Map<String, Object> getUserInfoById(@Param("customerId") Integer customerId);

    /**
     * 更新头像；存在阿里云的
     *
     * @param newFileName
     */
    void updateHeadPortrait(@Param("newFileName") String newFileName, @Param("customerId") Integer customerId);

    Map<String, Object> getRegisterNum(Map<String, Object> parameMap);

    /**
     * 根据openid获取用户
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> getCustomerByOpenId(Map<String, Object> param);

    Map<String, Object> getInvitationSuccessTempParam(String customerId);

    Map<String, Object> getAllUserCount();

    List<Map<String, Object>> getUserInfoAndCarInfoByPhoneNo(String customerPN);
}