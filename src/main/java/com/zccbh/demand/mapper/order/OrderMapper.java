package com.zccbh.demand.mapper.order;

import com.zccbh.demand.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface OrderMapper extends BaseMapper{
    Map<String,Object> findCount(Map<String, Object> paramModelMap);
    Map<String,Object> findDetail(Map<String, Object> map);
    Map<String, Object> findOrderByRecordRechargeId(@Param("id") String id);

    List<Map<String,Object>> findUnfinishedOrder(Map<String, Object> map);
    List<Map<String, Object>> findPublicityList(Map<String, Object> map);
    List<Map<String, Object>> findOrderList(Map<String, Object> map);

    /**
     * 申请保险理赔的统计
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> orderCount(Map<String, Object> parameModelMap);

    /**
     * 保险理赔，审核不通过统计
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> orderApplyFail(Map<String, Object> parameModelMap);

    /**
     * 保险理赔，审核通过
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> orderApplySuccess(Map<String, Object> parameModelMap);
}
