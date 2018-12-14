package com.zccbh.demand.service.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.system.LogMapper;
import com.zccbh.demand.pojo.system.Log;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LogService {
    @Autowired
    private LogMapper logMapper;

    /**
     * 记录操作日志
     * @param log 日志对象
     * @return
     * @throws Exception
     */
    @Transactional
    public int saveLog(Log log) throws Exception{
        return logMapper.saveEntitySingle(log);
    }

    /**
     * 查询操作日志
     * @param paramModelMap 查询条件
     * @return 日志集合
     * @throws Exception
     */
    public PageInfo<Log>  findLogList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Log> logList = logMapper.findEntityMore(paramModelMap);
        PageInfo<Log> logInfo = new PageInfo<>(logList);
        return logInfo;
    }

}


