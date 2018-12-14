package com.zccbh.demand.service.foundation;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.foundation.FoundationDetailMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
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
public class FoundationDetailService {
    @Autowired
    private FoundationDetailMapper foundationDetailMapper;

    /**
     * 保存互助金修改操作
     */
    @Transactional
    public void save(Map<String, Object> paramModelMap) throws Exception{
        foundationDetailMapper.saveSingle(paramModelMap);
    }

    /**
     * 查询互助金修改记录
     */
    public PageInfo<Map<String, Object>> findList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = foundationDetailMapper.findMore(paramModelMap);
        return new PageInfo<>(list);
    }
}


