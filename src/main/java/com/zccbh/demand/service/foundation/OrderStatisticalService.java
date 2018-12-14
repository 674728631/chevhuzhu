package com.zccbh.demand.service.foundation;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.mapper.foundation.OrderStatisticalMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrderStatisticalService {
    @Autowired
    private OrderStatisticalMapper orderStatisticalMapper;

    @Autowired
    private CarMapper carMapper;

    /**
     * 创建保险订单统计
     */
    @Transactional
    public void saveOrderStatistical(Map<String, Object> paramModelMap) throws Exception{
        orderStatisticalMapper.saveSingle(paramModelMap);
    }

    /**
     * 修改保险订单统计
     */
    @Transactional
    public void updateOrderStatistical(Map<String, Object> paramModelMap) throws Exception {
        orderStatisticalMapper.updateModel(paramModelMap);
    }

    /**
     * 查询统计数据
     */
    public Map<String, Object> findSumData() throws Exception {
        Map param = new HashMap();
        Map<String, Object> carCount = carMapper.findCount(param);
        Integer carNum = Integer.parseInt(carCount.get("observationNum").toString())+Integer.parseInt(carCount.get("guaranteeNum").toString());

        Map result = orderStatisticalMapper.findSumData();
        BigDecimal orderRadio = new BigDecimal(result.get("totalOrder").toString()).multiply(new BigDecimal(100)).divide(new BigDecimal(carNum), 1, RoundingMode.HALF_UP);
        result.put("orderRadio",orderRadio);
        BigDecimal profit = new BigDecimal(result.get("totalOrderAmt").toString()).subtract(new BigDecimal(result.get("totalBusinessAmt").toString())).subtract(new BigDecimal(result.get("totalChannelAmt").toString()));
        result.put("profit",profit);
        return result;
    }

    /**
     * 查询保险理赔收支明细数据
     */
    public PageInfo<Map<String, Object>> findOrderList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> rechargeList = orderStatisticalMapper.findOrderStatisticalList(paramModelMap);
        return new PageInfo<>(rechargeList);
    }
}


