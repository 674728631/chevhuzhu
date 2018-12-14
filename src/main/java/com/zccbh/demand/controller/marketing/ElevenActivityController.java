package com.zccbh.demand.controller.marketing;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zccbh.demand.service.activities.MiddleCustomerMaintenanceshopService;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;

import net.sf.json.JSONObject;

/** 
 * @ClassName: ElevenActivityController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年11月2日 下午3:48:45 
 *  
 */

@Controller
@RequestMapping("/activity")
public class ElevenActivityController {
	
	@Autowired
	private MiddleCustomerMaintenanceshopService middleCustomerMaintenanceshopService;
	
	/**
	 * 统计参与数
	 * @author xiaowuge  
	 * @date 2018年11月2日  
	 * @version 1.0
	 */
	@RequestMapping(value = "/number", method = RequestMethod.POST)
	@ResponseBody
	public String number(HttpServletRequest request, @RequestBody String strJson){
		Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
		int count = middleCustomerMaintenanceshopService.countUserAndShop(paramModelMap);
		return Constant.toReModel(CommonField.SUCCESS, "统计成功", count);				
	}

}
