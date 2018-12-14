package com.zccbh.demand.service.event;

import com.zccbh.demand.mapper.business.UserBusinessMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.MessageMapper;
import com.zccbh.demand.mapper.event.EventAssertMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EventAssertService {
	@Autowired
	private EventAssertMapper eventAssertMapper;

	@Autowired
	private EventMapper eventMapper;

	@Autowired
	private CarMapper carMapper;

	@Autowired
	private UserBusinessMapper businessMapper;

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private WeiXinUtils weiXinUtils;

	/**
	 * 修改定损详情
	 * @param paramModelMap 定损详情信息
	 * @return 修改结果信息
	 * @throws Exception
	 */
	@Transactional
	public String updateEventAssert(Map<String, Object> paramModelMap) throws Exception {
		eventAssertMapper.updateModel(paramModelMap);
		return "0";
	}

	/**
	 * 定损审核通过
	 * @param paramModelMap 定损详情信息
	 * @return 操作是否成功
	 * @throws Exception
	 */
	@Transactional
	public String assertSuccess(Map<String, Object> paramModelMap) throws Exception {
		Map map = new HashMap();
		//定损费用
		BigDecimal amtAssert = new BigDecimal((String) paramModelMap.get("amtAssert")); 
		//获取剩余理赔额度
		Map<String, Object> carDetail = carMapper.findCarByEventNo((String) paramModelMap.get("eventNo"));
		BigDecimal amtCompensation = (BigDecimal) carDetail.get("amtCompensation");
		//比较剩余理赔额度和定损金额，得出车主应支付金额
		BigDecimal amtPay = amtAssert.divide(new BigDecimal(10), 2, RoundingMode.HALF_UP); //车主支付10%金额
		BigDecimal amtCooperation = amtAssert;//互助金额
		int answer = amtCompensation.compareTo(amtAssert);
		if(answer < 1){
			amtPay = amtCompensation.divide(new BigDecimal(10), 2, RoundingMode.HALF_UP).add(amtAssert.subtract(amtCompensation));
			amtCooperation = amtCompensation;
		}
		//获取保障中的车辆数量
		map.put("status",20);
		int carCount = carMapper.findCarCountByStatus(map);
		//计算分摊金额
		BigDecimal amtShare = amtCooperation.divide(new BigDecimal(carCount),2,RoundingMode.HALF_UP);
		BigDecimal amtCBH = new BigDecimal(0).setScale(2,RoundingMode.HALF_UP);
		int answer2 = amtShare.compareTo(new BigDecimal(0.1));
		if(answer2==1){
			amtShare = new BigDecimal(0.1).setScale(2,RoundingMode.HALF_UP);
			amtCBH = amtCooperation.subtract(amtShare.multiply(new BigDecimal(carCount))).setScale(2,RoundingMode.HALF_UP);
			amtCooperation = amtShare.multiply(new BigDecimal(carCount)).setScale(2,RoundingMode.HALF_UP);
		}
		int answer3 = amtShare.compareTo(new BigDecimal(0.01));
		if(answer3!=1){
			amtShare = new BigDecimal(0.01).setScale(2,RoundingMode.HALF_UP);
			amtCBH = new BigDecimal(0).setScale(2,RoundingMode.HALF_UP);
		}
		//修改互助单
		map.clear();
		map.put("eventNo",paramModelMap.get("eventNo"));
		map.put("statusEvent",31);
		map.put("amtBusiness",new BigDecimal((String) paramModelMap.get("amtBusiness")));
		map.put("amtCooperation",amtCooperation);
		map.put("amtCBH",amtCBH);
		map.put("amtShare",amtShare);
		map.put("amtPay",amtPay);
		eventMapper.updateModel(map);
		//修改定损详情
		map.clear();
		map.put("eventNo",paramModelMap.get("eventNo"));
		map.put("amtAssert",amtAssert);
		map.put("reasonAssert",paramModelMap.get("reasonAssert"));
		// app接口需要增加描述字段
		map.put("description",paramModelMap.get("description"));
		map.put("comfirmAssertTime", DateUtils.formatDate(new Date()));
		updateEventAssert(map);
		//推送消息给用户告知他定损审核通过
		map.clear();
		map.put("eventNo",paramModelMap.get("eventNo"));
		Map<String,Object> eventInfo = eventMapper.findSingle(map);
		map.clear();
		map.put("customerId",(Integer)eventInfo.get("customerId"));
		map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
		map.put("eventNo",paramModelMap.get("eventNo"));
		map.put("type",41);
		map.put("title","救助消息");
		map.put("content",eventInfo.get("licensePlateNumber") + "的救助订单定损通过，点击去支付。");
		messageMapper.saveSingle(map);
		//发送短信给用户
		SmsDemo.sendSms(71,(String) eventInfo.get("customerPN"),(String) eventInfo.get("licensePlateNumber"));
		//微信推送给用户
		if (Constant.toEmpty(eventInfo.get("openId"))) {
			map.clear();
			map.put("openid", eventInfo.get("openId"));
			map.put("eventNo", paramModelMap.get("eventNo"));
			map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
			map.put("model",eventInfo.get("model"));
			map.put("money", amtPay.toString());
			map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
			map.put("type", "2");
			weiXinUtils.sendTemplate(2, map);
		}
		//获取商家用户
		map.clear();
		map.put("maintenanceshopId",eventInfo.get("maintenanceshopId"));
		map.put("typeUser",1);
		List<Map<String, Object>> businessList = businessMapper.findMore(map);
		if(businessList.size()>0){
			//推送消息给商家app
			map.clear();
			map.put("orderNo", paramModelMap.get("eventNo"));
			map.put("orderStatus",31);
			map.put("licensePlateNumber", eventInfo.get("licensePlateNumber"));
			map.put("type",5);
			if(Constant.toEmpty(businessList.get(0).get("iosDeviceId"))){
				SmsDemo.mobilePushMessage(10, 5,businessList.get(0).get("iosDeviceId").toString(), eventInfo.get("licensePlateNumber") + "的订单定损已经通过", map);
			}
			if(Constant.toEmpty(businessList.get(0).get("androidDeviceId"))){
				SmsDemo.mobilePushMessage(20, 5,businessList.get(0).get("androidDeviceId").toString(), eventInfo.get("licensePlateNumber") + "的订单定损已经通过", map);
			}
		}
		return "0";
	}

	/**
	 * 查询定损详情
	 * @param paramModelMap 查询条件
	 * @return 定损详情
	 * @throws Exception
	 */
	public Map<String, Object> findEventAssert(Map<String, Object> paramModelMap) throws Exception {
		Map<String, Object> eventAssert = eventAssertMapper.findSingle(paramModelMap);
		if(Constant.toEmpty(eventAssert.get("drivingLicense"))){
			eventAssert.put("drivingLicense",CommonField.getCarDrivingUrl((String) eventAssert.get("drivingLicense")));
		}
		if(Constant.toEmpty(eventAssert.get("carPhotos"))){
			eventAssert.put("carPhotos",CommonField.getCarUrl((String) eventAssert.get("carPhotos")));
		}
		if(Constant.toEmpty(eventAssert.get("accidentImg"))){
			List<String> accidentImg = CommonField.getImgList(0,(String) eventAssert.get("accidentImg"));
			eventAssert.put("accidentImg",accidentImg);
		}
		if(Constant.toEmpty(eventAssert.get("assertImg"))){
			List<String> assertImg = CommonField.getImgList(1,(String) eventAssert.get("assertImg"));
			eventAssert.put("assertImg",assertImg);
		}
		return eventAssert;
	}
}
