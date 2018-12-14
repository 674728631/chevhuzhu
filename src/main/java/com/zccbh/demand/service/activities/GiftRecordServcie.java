package com.zccbh.demand.service.activities;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zccbh.demand.mapper.activities.GiftMapper;
import com.zccbh.demand.mapper.activities.GiftRecordMapper;

/** 
 * @ClassName: GiftRecordServcie 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年9月28日 下午3:32:15 
 *  
 */
@Service
@Transactional(propagation=Propagation.SUPPORTS, readOnly = true)
public class GiftRecordServcie {
	
	@Autowired
	private GiftMapper giftMapper;
	
	@Autowired
	private GiftRecordMapper giftRecordMapper;
	
	/**
	 * 保存抽奖记录
	 * @author xiaowuge  
	 * @date 2018年9月28日  
	 * @version 1.0
	 */
	@Transactional
	public String saveRecord(Map<String, Object> recordMap) throws Exception{
		int i = giftRecordMapper.saveSingle(recordMap);
		return "0";
	}
	
	@Transactional
	public List<Map<String, Object>> getRecordByOpenId(Map<String, Object> map)throws Exception{
		return giftRecordMapper.selectByOpenId(map);
	}
	
	@Transactional
	public String updateRecord(Map<String, Object> map)throws Exception{
		giftRecordMapper.updateModel(map);
		return "0";
	}

}
