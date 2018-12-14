package com.zccbh.demand.mapper.user;

import com.zccbh.demand.mapper.BaseMapper;
import com.zccbh.demand.pojo.user.MessageBackstage;

import java.util.List;
import java.util.Map;

public interface MessageBackstageMapper extends BaseMapper{
	
    List<Map<String,Object>> getMessageList(String token);
    
    List<Map<String, Object>> getMessageListByType(Map<String, String> map);

    MessageBackstage selectByIdAndToken(Map<String, Object> id);
    
    MessageBackstage selectById(Map<String, Object> map);
    
    int countBackMsg(Map<String, Object> map);
    
    void deleteBackMsg(Map<String, Object> map);
}