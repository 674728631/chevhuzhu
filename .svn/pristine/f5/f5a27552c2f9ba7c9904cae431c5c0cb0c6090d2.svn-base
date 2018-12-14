package com.zccbh.demand.service.customer;

import com.zccbh.demand.mapper.user.MessageBackstageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MessageBackstageService {
    @Autowired
    private MessageBackstageMapper messageBackstageMapper;

    /**
     * 新增一条后台消息
     * @param param
     */
    @Transactional
    public void save(Map<String, Object> param) throws Exception {
        messageBackstageMapper.saveSingle(param);
    }
    
    /**
     * 根据订单号和当前状态查询消息
     * @author xiaowuge  
     * @date 2018年11月12日  
     * @version 1.0
     */
    @Transactional
    public int countBackMsg(Map<String, Object> map){
    	return messageBackstageMapper.countBackMsg(map);
    }   
    
    
    public void deleteBackMsg(Map<String, Object> map) throws Exception{
    	messageBackstageMapper.deleteBackMsg(map);
    }

}


