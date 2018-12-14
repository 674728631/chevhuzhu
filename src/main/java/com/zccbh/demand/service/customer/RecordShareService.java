package com.zccbh.demand.service.customer;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.customer.RecordShareMapper;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RecordShareService {
    @Autowired
    private RecordShareMapper recordShareMapper;

    /**
     * 查询支出数据
     * @param paramModelMap 查询条件
     * @return 支出信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findExpensesList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> shareList = recordShareMapper.findMore(paramModelMap);
        PageInfo<Map<String, Object>> shareInfo = new PageInfo<>(shareList);
        return shareInfo;
    }
}


