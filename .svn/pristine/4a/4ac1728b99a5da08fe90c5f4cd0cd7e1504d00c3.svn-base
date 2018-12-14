/**   
 * @author xiaowuge  
 * @date 2018年9月28日  
 * @version 1.0  
 */ 
package com.zccbh.demand.service.activities;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zccbh.demand.mapper.activities.GiftMapper;

/** 
 * @ClassName: GiftService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年9月28日 下午3:22:05 
 *  
 */
@Service
@Transactional(propagation=Propagation.SUPPORTS, readOnly = true)
public class GiftService {
	
	@Autowired
	private GiftMapper giftMapper;
	
	/**
	 * 根据抽奖活动编号查找奖品列
	 * @author xiaowuge  
	 * @date 2018年9月28日  
	 * @version 1.0
	 */
	@Transactional
	public List<Map<String, Object>> getGiftByDrawNum(Map<String, Object> drawNumMap) throws Exception{
		return giftMapper.selectByDrawNum(drawNumMap);
	}
	
	/**
	 * 查询奖品详情
	 * @author xiaowuge  
	 * @date 2018年9月28日  
	 * 
	 * @version 1.0
	 */
	@Transactional
	public Map<String, Object> getGift(Map<String, Object> giftMap) throws Exception{
		return giftMapper.findSingle(giftMap);
	}
	

}
