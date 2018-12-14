package com.zccbh.demand.service.activities;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;

/** 
 * @ClassName: MiddleCustomerMaintenanceshopService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年11月2日 下午3:43:44 
 *  
 */
@Service
@Transactional(propagation=Propagation.SUPPORTS, readOnly = true)
public class MiddleCustomerMaintenanceshopService {
	
	@Autowired
	private MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;
	
	@Transactional
	public int countUserAndShop(Map<String, Object> map){
		return middleCustomerMaintenanceshopMapper.countUserAndShop(map);
	}

}
