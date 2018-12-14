package com.zccbh.demand.service.system;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zccbh.demand.mapper.system.CbhUserCustomerLogMapper;
import com.zccbh.util.base.DateUtils;

/** 
 * @ClassName: CbhUserCustomerLogService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年11月21日 上午10:58:29 
 *  
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserCustomerLogService {
	
	@Autowired
	private CbhUserCustomerLogMapper userCustomerLogMapper;
	
	/**
	 * 加入用户日志
	 * @author xiaowuge  
	 * @date 2018年12月3日  
	 * @version 1.0
	 */
	public void saveUserCustomerLog(Map<String, Object> map) throws Exception{
		userCustomerLogMapper.saveSingle(map);
	}

}
