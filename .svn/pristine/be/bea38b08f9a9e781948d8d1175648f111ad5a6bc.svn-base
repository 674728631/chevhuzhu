package com.zccbh.demand.service.basic;

import com.zccbh.demand.mapper.basic.DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DictionaryService {
    @Autowired
    private DictionaryMapper dictionaryMapper;

    /**
     * 查询单条码表记录
     * @param paramModelMap 查询条件
     * @return 码表记录
     * @throws Exception
     */
    public Map<String, Object> findSingle(Map<String, Object> paramModelMap) throws Exception {
        return dictionaryMapper.findSingle(paramModelMap);
    }
}


