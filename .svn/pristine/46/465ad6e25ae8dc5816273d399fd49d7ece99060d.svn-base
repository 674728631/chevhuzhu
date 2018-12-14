package com.zccbh.demand.mapper.customer;

import com.zccbh.demand.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface WechatLoginMapper extends BaseMapper{
    List<Map<String, Object>> chartView(Map<String, Object> map);

    /**
     * 获取微信关注数
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> weChatConcerns(Map<String, Object> parameModelMap);

    /**
     * 获取微信关注数, 渠道拉新的数据
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> weChatConcernsForChannel(Map<String, Object> parameModelMap);
    
    /**
     * 微信关注数量（通过时间查询）
     * @author xiaowuge  
     * @date 2018年10月26日  
     * @version 1.0
     */
    Map<String, Object> concernWeChat(Map<String, Object> parameMap);
    /**
     * 根据时间查询观察期和保障中
     * @author xiaowuge  
     * @date 2018年10月26日  
     * @version 1.0
     */
    Map<String, Object> selectObservationAndGuarantee(Map<String, Object> parameMap);
}