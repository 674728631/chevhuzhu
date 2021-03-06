package com.zccbh.demand.mapper.customer;

import com.zccbh.demand.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface InvitationCustomerMapper extends BaseMapper{
	int selectInvitationCount1(Map<String, Object> map);
	int selectInvitationCount2(@Param("customerId") String customerId);

	Map<String, Object> selectFirstCar(@Param("id") int id);

	List<Map<String, Object>> selectInvitationList(@Param("id") int id);
	List<Map<String, Object>> selectInvitationList1(Map<String, Object> map);
	List<Map<String, Object>> selectInvitationList2(Map<String, Object> map);
	
	List<Map<String, Object>> selectByInvitedCustomerId(Map<String, Object> map);
	/**
	 * 获取成功邀请列表
	 * @author xiaowuge  
	 * @date 2018年9月19日  
	 * @version 1.0
	 */
	List<Map<String, Object>> getInvitedList(Map<String, Object> map);

	/**
	 * 统计商家或者渠道的拉新数据
	 * @param paramModelMap
	 * @return
	 */
    List<Map<String, Object>> selectInvitationListFromBusinessOrChannel(Map<String, Object> paramModelMap);

	/**
	 * 统计商家或者渠道的拉新数据的总和
	 * @param paramModelMap
	 * @return
	 */
	Map<String, Object> countInvitationListFromBusinessOrChannel(Map<String, Object> paramModelMap);

	/**
	 * 统计用户的拉新数据总和
	 * @param paramModelMap
	 * @return
	 */
	Map<String, Object> countSelectInvitationList1(Map<String, Object> paramModelMap);

    /**
     * 查询用户的统计详情
     * @param paramModelMap
     * @return
     */
    List<Map<String, Object>> selectUserDetail(Map<String, Object> paramModelMap);

    /**
     * 查询商家或者其他渠道的统计详情
     * @param paramModelMap
     * @return
     */
    List<Map<String, Object>> selectBusinessOrChannalDetail(Map<String, Object> paramModelMap);
    
    /**
     * 统计总邀请数
     * @author xiaowuge  
     * @date 2018年11月23日  
     * @version 1.0
     */
    Map<String, Object> totalCustomer();
    
    /**
     * 总被邀请数
     * @author xiaowuge  
     * @date 2018年11月23日  
     * @version 1.0
     */
    Map<String, Object> totalInvitationCustomer();
    
    /**
     * 拉新数据
     * @author xiaowuge  
     * @date 2018年12月5日  
     * @version 1.0
     */
    List<Map<String, Object>> selectInvitationDate(Map<String, Object> map);
    
    /**
     * 渠道拉新总数
     * @author xiaowuge  
     * @date 2018年12月11日  
     * @version 1.0
     */
    Map<String, Object> totalInvitation(Map<String, Object> map);
    
    /**
     * 渠道拉新列表
     * @author xiaowuge  
     * @date 2018年12月11日  
     * @version 1.0
     */
    List<Map<String, Object>> invitationList(Map<String, Object> map);
    
}
