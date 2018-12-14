package com.zccbh.demand.service.event;

import com.zccbh.demand.mapper.event.EventReceivecarMapper;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EventReceiveService {
	@Autowired
	private EventReceivecarMapper eventReceivecarMapper;

	/**
	 * 查询接单详情
	 * @param paramModelMap 查询条件
	 * @return 接单详情
	 * @throws Exception
	 */
	public Map<String, Object> findReceiveDetail(Map<String, Object> paramModelMap) throws Exception {
		Map<String, Object> eventReceive = eventReceivecarMapper.findSingle(paramModelMap);
		if(Constant.toEmpty(eventReceive.get("accidentImg"))){
			List<String> accidentImg = CommonField.getImgList(0,(String) eventReceive.get("accidentImg"));
			eventReceive.put("accidentImg",accidentImg);
		}
		return eventReceive;
	}

	/**
	 * 查询待分单详情
	 * @param paramModelMap 查询条件
	 * @return 分单详情
	 * @throws Exception
	 */
	public Map<String, Object> findDistributionDetail(Map<String, Object> paramModelMap) throws Exception {
		Map<String, Object> receivecar = eventReceivecarMapper.findReceivecar(paramModelMap);
		if(Constant.toEmpty(receivecar.get("drivingLicense"))){
			receivecar.put("drivingLicense",CommonField.getCarDrivingUrl((String) receivecar.get("drivingLicense")));
		}
		if(Constant.toEmpty(receivecar.get("carPhotos"))){
			receivecar.put("carPhotos",CommonField.getCarUrl((String) receivecar.get("carPhotos")));
		}
		if(Constant.toEmpty(receivecar.get("accidentImg"))){
			List<String> accidentImg = CommonField.getImgList(0,(String) receivecar.get("accidentImg"));
			receivecar.put("accidentImg",accidentImg);
		}
		if(Constant.toEmpty(receivecar.get("eventQrcode"))){
			List<String> eventQrcode = CommonField.getImgList(4,(String) receivecar.get("eventQrcode"));
			receivecar.put("eventQrcode",eventQrcode);
		}
		return receivecar;
	}
}
