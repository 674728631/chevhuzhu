package com.zccbh.demand.mapper.customer;

import com.zccbh.demand.mapper.BaseMapper;
import com.zccbh.demand.pojo.merchants.CbhRecordShare;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RecordRechargeMapper extends BaseMapper{
	BigDecimal findRechargeByCarId(Map<String, Object> over);

	Map<String, Object> selectCreatTime(Map<String, Object> map);
	Map<String, Object> selectAmt(Map<String, Object> map);

	List<Map<String, Object>> findRecharge();
	List<Map<String, Object>> findRechargeNum();
	List<Map<String, Object>> customerRecord(Map<String, Object> map);
	List<Map<String, Object>> chartFoundation(Map<String, Object> map);
	List<Map<String, Object>> chartTwiceRecharge(Map<String, Object> map);

	/**
	 * 查询出该车牌号的所有，充值记录
	 * @param paramModelMap
	 * @return
	 */
    List<Map<String, Object>> findAllRecordByLicensePlateNumber(Map<String, Object> paramModelMap);
    
    
    Map<String, Object> findRechargeDate(Map<String, Object> paramModelMap);
    
    List<Map<String, Object>> findRechargeList(Map<String, Object> paramModelMap);
    
    /**
     * 充值记录
     * @author xiaowuge  
     * @date 2018年10月26日  
     * @version 1.0
     */
    Map<String, Object> marketingRecharge(Map<String, Object> parameMap);
    
    /**
     * 查询充值9元
     * @author xiaowuge  
     * @date 2018年11月14日  
     * @version 1.0
     */
    Map<String, Object> selectNine(Map<String, Object> map);
    /**
     * 查询充值99元
     * @author xiaowuge  
     * @date 2018年11月14日  
     * @version 1.0
     */
    Map<String, Object> selectTwoNine(Map<String, Object> map);
    
    /**
     * 统计复购总数
     * @author xiaowuge  
     * @date 2018年11月16日  
     * @version 1.0
     */
    Map<String, Object> selectTotalReEnter();
    
    /**
     * 自然用户复购
     * @author xiaowuge  
     * @date 2018年11月16日  
     * @version 1.0
     */
    Map<String, Object> selectNaturalReEnter();
    
    /**
     * 总补贴
     * @author xiaowuge  
     * @date 2018年11月19日  
     * @version 1.0
     */
    Map<String, Object> selectSubsidy();
    
    /**
     * 总互助金额
     * @author xiaowuge  
     * @date 2018年11月19日  
     * @version 1.0
     */
    Map<String, Object> selectEventMoney();
    
    /**
     * 总理赔次数
     * @author xiaowuge  
     * @date 2018年11月19日  
     * @version 1.0
     */
    Map<String, Object> selectEventTotal();
    
    /**
     * 补贴最多的前10
     * @author xiaowuge  
     * @date 2018年11月19日  
     * @version 1.0
     */
    List<Map<String, Object>> listShop();

    /**
     * 充值数据
     * @return
     */
    List<Map<String, Object>> getRechargeInfo();

    /**
     * 根据渠道id获取充值金额
     * @author xiaowuge  
     * @date 2018年12月13日  
     * @version 1.0
     */
    Map<String, Object> getRechargeNum(Map<String, Object> map);
    
}