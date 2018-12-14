package com.zccbh.demand.service.event;

import com.zccbh.demand.mapper.event.EventComplaintMapper;
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
public class EventComplaintService {
	@Autowired
	private EventComplaintMapper eventComplaintMapper;

	/**
	 * 查询投诉详情
	 * @param paramModelMap 查询条件
	 * @return 投诉详情
	 * @throws Exception
	 */
	public Map<String, Object> findEventComplaint(Map<String, Object> paramModelMap) throws Exception {
		Map<String, Object> eventComplaint = eventComplaintMapper.findSingle(paramModelMap);
		if(Constant.toEmpty(eventComplaint.get("drivingLicense"))){
			eventComplaint.put("drivingLicense",CommonField.getCarDrivingUrl((String) eventComplaint.get("drivingLicense")));
		}
		if(Constant.toEmpty(eventComplaint.get("carPhotos"))){
			eventComplaint.put("carPhotos",CommonField.getCarUrl((String) eventComplaint.get("carPhotos")));
		}
		if(Constant.toEmpty(eventComplaint.get("accidentImg"))){
			List<String> accidentImg = CommonField.getImgList(0,(String) eventComplaint.get("accidentImg"));
			eventComplaint.put("accidentImg",accidentImg);
		}
		if(Constant.toEmpty(eventComplaint.get("assertImg"))){
			List<String> assertImg = CommonField.getImgList(1,(String) eventComplaint.get("assertImg"));
			eventComplaint.put("assertImg",assertImg);
		}
		if(Constant.toEmpty(eventComplaint.get("img"))){
			List<String> img = CommonField.getImgList(2,(String) eventComplaint.get("img"));
			eventComplaint.put("img",img);
		}
		return eventComplaint;
	}
}
