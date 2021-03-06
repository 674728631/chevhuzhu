package com.zccbh.demand.service.event;

import com.zccbh.demand.mapper.event.EventApplyMapper;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EventApplyService {
	@Autowired
	private EventApplyMapper eventApplyMapper;
	
	@Autowired
    private EventApplyFailService eventApplyFailService;

	/**
	 * 修改理赔申请
	 * @param paramModelMap 理赔申请信息
	 * @return 修改结果信息
	 * @throws Exception
	 */
	@Transactional
	public String updateEventApply(Map<String, Object> paramModelMap) throws Exception {
		eventApplyMapper.updateModel(paramModelMap);
		return "0";
	}

	/**
	 * 查询理赔申请详情
	 * @param paramModelMap 查询条件
	 * @return 申请详情
	 * @throws Exception
	 */
	public Map<String, Object> findEventApply(Map<String, Object> paramModelMap) throws Exception {
		
		Map<String, Object> eventApply = eventApplyMapper.findSingle(paramModelMap);
		
        //判断是否有不通过记录
      Map<String, Object> eventApplyFail = eventApplyFailService.getEventApplyFail(MapUtil.build().put("eventNo", eventApply.get("eventNo")).over());
      if (Constant.toEmpty(eventApplyFail.get("accidentImg"))) {
			eventApply.put("enentFailFlag", true);
		}else{
			eventApply.put("eventFailFlag", false);
		}
		if(Constant.toEmpty(eventApply.get("drivingLicense"))){
			eventApply.put("drivingLicense",CommonField.getCarDrivingUrl((String) eventApply.get("drivingLicense")));
		}
		if(Constant.toEmpty(eventApply.get("carPhotos"))){
			eventApply.put("carPhotos",CommonField.getCarUrl((String) eventApply.get("carPhotos")));
		}
		if(Constant.toEmpty(eventApply.get("accidentImg"))){
			List<String> accidentImg = CommonField.getImgList(0,(String) eventApply.get("accidentImg"));
			eventApply.put("accidentImg",accidentImg);
		}
		if(Constant.toEmpty(eventApply.get("eventQrcode"))){
			List<String> eventQrcode = CommonField.getImgList(4,(String) eventApply.get("eventQrcode"));
			eventApply.put("eventQrcode",eventQrcode);
		}
		return eventApply;
	}
	
	/**
	 * 查询历史不通过
	 * @author xiaowuge  
	 * @date 2018年11月22日  
	 * @version 1.0
	 */
	public Map<String, Object> getHistoryEventApply(Map<String, Object> map)throws Exception{
		
		Map<String, Object> resultMap = new HashMap<>();
		   //判断是否有不通过记录
        Map<String, Object> eventApplyFail = eventApplyFailService.getEventApplyFail(MapUtil.build().put("eventNo", map.get("eventNo")).over());
        if (null == eventApplyFail) {
        	resultMap.put("enentFailFlag", false);
		}else{
			resultMap.put("eventFailFlag", true);
			if (Constant.toEmpty(eventApplyFail.get("accidentImg"))) {
				List<String> accidentImg = CommonField.getImgList(0,(String) eventApplyFail.get("accidentImg"));
				eventApplyFail.put("accidentImg", accidentImg);
			}
			resultMap.put("eventApplyFail", eventApplyFail);
		}
        return resultMap;
	}
}
