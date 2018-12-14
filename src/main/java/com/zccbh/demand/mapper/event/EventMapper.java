package com.zccbh.demand.mapper.event;

import com.zccbh.demand.mapper.BaseMapper;

import java.util.List;
import java.util.Map;
public interface EventMapper extends BaseMapper{
	int findApplyCount(Map<String,Object> map);

	Map<String, Object> selectFoundation();
	Map<String, Object> findPublicityEvent();
	Map<String,Object> findCompleteEvent();
	Map<String, Object> findPublicityAmount();
	Map<String, Object> findRandomCar();
	Map<String,Object> findCount(Map<String, Object> paramModelMap);
	Map<String,Object> findAttention(Map<String, Object> map);
	Map<String,Object> findShopEvent(Map<String, Object> map);
	Map<String,Object> findEventQuotation(String eventNo);
	Map<String, Object> findOrderDetail(Map<String,Object> pMap);

	List<Map<String, Object>> findJionNew();
	List<Map<String, Object>> findCompensateList(Map<String, Object> map);
	List<Map<String, Object>> findMessageNew(Map<String,Object> map);
	List<Map<String, Object>> findApplyId(Map<String,Object> map);
	List<Map<String, Object>> chartEvent(Map<String, Object> map);

	/**
	 * 查询互助的订单详情，app接口调用
	 * @param over
	 * @return
	 */
	Map<String, Object> findHzOrderDetail(Map<String, Object> over);

	/**
	 * 互助理赔,审核不通过统计
	 * @param parameModelMap
	 * @return
	 */
    List<Map<String, Object>> eventApplyFail(Map<String, Object> parameModelMap);

	/**
	 * 互助理赔,审核通过的统计
	 * @param parameModelMap
	 * @return
	 */
	List<Map<String, Object>> eventApplySuccess(Map<String, Object> parameModelMap);

	/**
	 * 保险理赔, 查询商家报价
	 * @param eventNo
	 * @return
	 */
	Map<String, Object> findOrderQuotation(String eventNo);
	
	/**
	 * 理赔统计
	 * @author xiaowuge  
	 * @date 2018年11月8日  
	 * @version 1.0
	 */
	Map<String, Object> eventResult(Map<String, Object> parameModelMap);
	
	/**
	 * 获取订单状态
	 * @author xiaowuge  
	 * @date 2018年11月30日  
	 * @version 1.0
	 */
	int getEventStatus(String eventNo);
}
