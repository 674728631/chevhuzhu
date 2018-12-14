package com.zccbh.demand.controller.weChat;


import com.zccbh.demand.service.customer.CarService;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/chemama/")
@RestController
public class ChemamaController {
	@Autowired CarService carService;


	/**
	 * 车妈妈那边购买9元互助后吊的接口
	 * @param request
	 * @return
	 */
	@PostMapping(value = "saveChevhuzhu", produces = "application/json;charset=utf-8")
	public String saveChevhuzhu(HttpServletRequest request){
		try {
			Map<String,Object> resultMap = carService.saveChevhuzhu(request);
			Boolean status = (Boolean) resultMap.get(CommonField.STATUS);
			String resultMessage = (String) resultMap.get(CommonField.RESULTMESSAGE);
			if (Constant.toEmpty(resultMap.get(CommonField.VALIDATION_CODE))) {
				return Constant.toReModel(resultMap.get(CommonField.VALIDATION_CODE).toString(), resultMessage, CommonField.PARAMETER_ERROR_PROMPT);
			}
			return status? Constant.toReModel(CommonField.SUCCESS, "", resultMap)
					: Constant.toReModel(CommonField.FAIL, resultMessage,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL, e.getMessage(), CommonField.PARAMETER_ERROR_PROMPT);
		}
	}

//	/**
//	 * 非自然用户查询车辆
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "findChemamaCar",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=utf-8")
//	public String findChemamaCar(HttpServletRequest request){
//		try {
//			Map<String,Object> carMap = carService.findChemamaCar(request);
//			if(carMap==null){
//				return Constant.toReModel(CommonField.FAIL, "手机号和车牌号不匹配", CommonField.PARAMETER_ERROR_PROMPT);
//			}
//			return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", carMap);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
//		}
//	}

//	/**
//	 * 非自然用户保存图片
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping(value = "saveCarImg",produces = "application/json;charset=utf-8")
//	public String saveCarImg(HttpServletRequest request) throws Exception{
//		try {
//				int flag = carService.saveCarImg(request);
//				if(flag==0){
//					return Constant.toReModel(CommonField.FAIL, "手机号和车牌号不匹配", CommonField.PARAMETER_ERROR_PROMPT);
//				}
//				return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", "SUCCESS");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
//		}
//	}

	/**
	 * 查询是否激活
	 * @param request
	 * @return
	 */
	@PostMapping(value = "queryActivation",produces = "application/json;charset=utf-8")
	public String queryActivation(HttpServletRequest request){
		try {
//			Map<String,Object> resultMap = carService.findChemamaCar(request);
//			Boolean status = (Boolean) resultMap.get(CommonField.STATUS);
//			String resultMessage = (String) resultMap.get(CommonField.RESULTMESSAGE);
//			if (Constant.toEmpty(resultMap.get(CommonField.VALIDATION_CODE))) {
//				String string = resultMap.get(CommonField.VALIDATION_CODE).toString();
//				resultMap.remove(CommonField.VALIDATION_CODE);
//					return Constant.toReModel(string, resultMessage, resultMap);
//			}
//			return status? Constant.toReModel(CommonField.SUCCESS, "", resultMap)
//					: Constant.toReModel(CommonField.FAIL, resultMessage,resultMap);
			Map<String, Object> resultMap = carService.findChemamaCar(request);
			String status = (String) resultMap.get(CommonField.STATUS);
			String resultMessage = (String) resultMap.get(CommonField.RESULTMESSAGE);
			if(Constant.toEmpty(resultMap.get(CommonField.VALIDATION_CODE))){
				String string = resultMap.get(CommonField.VALIDATION_CODE).toString();
				resultMap.remove(CommonField.VALIDATION_CODE);
				return Constant.toReModel(string, resultMessage, resultMap);
			}else{
				if( status.equals("1")){
					return Constant.toReModel(CommonField.FAIL, resultMessage, resultMap);
				}else if(status.equals("2")){
					return Constant.toReModel(CommonField.SUCCESS, "", resultMap);
				}else{
					return Constant.toReModel(CommonField.FAIL, resultMessage, resultMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL, e.getMessage(), CommonField.PARAMETER_ERROR_PROMPT);
		}
	}
	/**
	 * 车妈妈支付后调的接口
	 * @param request
	 * @return
	 */
	@PostMapping(value = "payEnd",produces = "application/json;charset=utf-8")
	public String payEnd(HttpServletRequest request){
		try {
			Map<String,Object> resultMap = carService.payEnd(request);
			Boolean status = (Boolean) resultMap.get(CommonField.STATUS);
			String resultMessage = (String) resultMap.get(CommonField.RESULTMESSAGE);
			if (Constant.toEmpty(resultMap.get(CommonField.VALIDATION_CODE))) {
				return Constant.toReModel(resultMap.get(CommonField.VALIDATION_CODE).toString(), resultMessage, CommonField.PARAMETER_ERROR_PROMPT);
			}
			return status? Constant.toReModel(CommonField.SUCCESS, "", resultMap)
					: Constant.toReModel(CommonField.FAIL, resultMessage,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL, e.getMessage(), CommonField.PARAMETER_ERROR_PROMPT);
		}

	}
	/**
	 * 车妈妈首页数据的接口
	 * @return
	 */
	@GetMapping(value = "homePageData",produces = "application/json;charset=utf-8")
	public String homePageData(){
		try {
			Map<String,Object> resultMap = carService.homePageData();
			Boolean status = (Boolean) resultMap.get(CommonField.STATUS);
			String resultMessage = (String) resultMap.get(CommonField.RESULTMESSAGE);
			return status? Constant.toReModel(CommonField.SUCCESS, "", resultMap)
					: Constant.toReModel(CommonField.FAIL, resultMessage,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL, e.getMessage(), CommonField.PARAMETER_ERROR_PROMPT);
		}

	}
}
