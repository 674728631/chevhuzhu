/**   
 * @author xiaowuge  
 * @date 2018年9月27日  
 * @version 1.0  
 */ 
package com.zccbh.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.event.EventApplyFailService;
import com.zccbh.demand.service.system.CarLogService;
import com.zccbh.demand.service.system.UserCustomerLogService;

/** 
 * @ClassName: CarTest 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年9月27日 上午10:18:14 
 *  
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/zccbh/config/spring.xml")
public class CarTest {
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private CarLogService carLogService;
	
	@Autowired
	private UserCustomerLogService userCustomerLogService;
	
	@Autowired
	private EventApplyFailService eventApplyFailService;
	
//	@Test
//	public void carTest(){
//		
//		Map<String, Object> carMap = new HashMap<>();
//		carMap.put("id","578");
//		carMap.put("typeGuarantee", 1);
//		carMap.put("licensePlateNumber", "川AFF888");
//		carMap.put("openid", "ob8Yy0t17brlBelm6GLR-Mp0dWLI");			
//		carMap.put("customerId", "652");
//		try {
//			carService.observation(carMap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void carTest(){
//		try {
//			carService.editObservation();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void carLogTest(){
//		Map<String, Object> map = new HashMap<>();
//		map.put("customerId", 111);
//		try {
//			carLogService.saveCarLog(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void userLogTest(){
//		Map<String, Object> map = new HashMap<>();
//		map.put("customerId", 111);
//		try {
//			userCustomerLogService.saveUserCustomerLog(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void eventApplyFailTest(){
		Map<String, Object> map = new HashMap<>();
		map.put("eventNo", 111);
		try {
//			eventApplyFailService.saveEventApplyFail(map);
			eventApplyFailService.getEventApplyFail(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateCompensateNum(){
		Map<String, Object> map = new HashMap<>();
		map.put("carId", 717);
		carService.updateCompensateNum(map);
	}

}
