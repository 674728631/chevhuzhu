package com.zccbh.demand.service.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zccbh.demand.mapper.event.EventApplyFailMapper;

/** 
 * @ClassName: EventApplyFailService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年11月21日 下午2:00:49 
 *  
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EventApplyFailService {
	
	@Autowired
	private EventApplyFailMapper eventApplyFailMapper;
	
	/**
	 * 插入不通过记录
	 * @author xiaowuge  
	 * @date 2018年11月21日  
	 * @version 1.0
	 */
	public void saveEventApplyFail(Map<String, Object> map)throws Exception{
		eventApplyFailMapper.saveSingle(map);
	}
	
	/**
	 * 获取最新不通过记录
	 */
	public Map<String, Object> getEventApplyFail(Map<String, Object> map) throws Exception{
		return eventApplyFailMapper.findSingle(map);
	}

}
