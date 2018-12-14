package com.zccbh.demand.service.system;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zccbh.demand.mapper.system.CbhCarLogMapper;

/** 
 * @ClassName: CarLogService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年11月21日 上午10:04:00 
 *  
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CarLogService {
	
	@Autowired
	private CbhCarLogMapper carLogMapper;
	
	/**
	 * 保存车辆操作记录
	 * @author xiaowuge  
	 * @date 2018年11月21日  
	 * @version 1.0
	 */
	public void saveCarLog(Map<String, Object> map) throws Exception{
		carLogMapper.saveSingle(map);
	}

}
