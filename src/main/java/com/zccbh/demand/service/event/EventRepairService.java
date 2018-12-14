package com.zccbh.demand.service.event;

import com.zccbh.demand.mapper.event.EventRepairMapper;
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
public class EventRepairService {
	@Autowired
	private EventRepairMapper eventRepairMapper;

	/**
	 * 查询维修详情
	 * @param paramModelMap 查询条件
	 * @return 维修详情
	 * @throws Exception
	 */
	public Map<String, Object> findEventRepair(Map<String, Object> paramModelMap) throws Exception {
		Map<String, Object> eventRepair = eventRepairMapper.findSingle(paramModelMap);
		if(Constant.toEmpty(eventRepair.get("drivingLicense"))){
			eventRepair.put("drivingLicense",CommonField.getCarDrivingUrl((String) eventRepair.get("drivingLicense")));
		}
		if(Constant.toEmpty(eventRepair.get("carPhotos"))){
			eventRepair.put("carPhotos",CommonField.getCarUrl((String) eventRepair.get("carPhotos")));
		}
		if(Constant.toEmpty(eventRepair.get("accidentImg"))){
			List<String> accidentImg = CommonField.getImgList(0,(String) eventRepair.get("accidentImg"));
			eventRepair.put("accidentImg",accidentImg);
		}
		if(Constant.toEmpty(eventRepair.get("assertImg"))){
			List<String> assertImg = CommonField.getImgList(1,(String) eventRepair.get("assertImg"));
			eventRepair.put("assertImg",assertImg);
		}
		if(Constant.toEmpty(eventRepair.get("img"))){
			List<String> img = CommonField.getImgList(2,(String) eventRepair.get("img"));
			eventRepair.put("img",img);
		}
		return eventRepair;
	}
}
