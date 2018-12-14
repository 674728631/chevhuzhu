package com.zccbh.demand.service.event;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.event.EventCommentMapper;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.uploadImg.UploadFileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EventCommentService {
	@Autowired
	private EventCommentMapper eventCommentMapper;

	/**
	 * 查询评论详情
	 * @param paramModelMap 查询条件
	 * @return 评论详情
	 * @throws Exception
	 */
	public Map<String, Object> findEventComment(Map<String, Object> paramModelMap) throws Exception {
		Map<String, Object> eventComment = eventCommentMapper.findSingle(paramModelMap);
		if(Constant.toEmpty(eventComment.get("drivingLicense"))){
			eventComment.put("drivingLicense",CommonField.getCarDrivingUrl((String) eventComment.get("drivingLicense")));
		}
		if(Constant.toEmpty(eventComment.get("carPhotos"))){
			eventComment.put("carPhotos",CommonField.getCarUrl((String) eventComment.get("carPhotos")));
		}
		if(Constant.toEmpty(eventComment.get("accidentImg"))){
			List<String> accidentImg = CommonField.getImgList(0,(String) eventComment.get("accidentImg"));
			eventComment.put("accidentImg",accidentImg);
		}
		if(Constant.toEmpty(eventComment.get("assertImg"))){
			List<String> assertImg = CommonField.getImgList(1,(String) eventComment.get("assertImg"));
			eventComment.put("assertImg",assertImg);
		}
		if(Constant.toEmpty(eventComment.get("img"))){
			List<String> img = CommonField.getImgList(2,(String) eventComment.get("img"));
			eventComment.put("img",img);
		}
		return eventComment;
	}

	public PageInfo<Map<String, Object>> findCommentList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> carList = eventCommentMapper.findMore(paramModelMap);
        if(carList!=null&&carList.size()>0){
        	for(Map<String,Object> m : carList){
        		m.put("portrait", String.valueOf(m.get("portrait")).indexOf("thirdwx.qlogo.cn")==-1?UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL,String.valueOf(m.get("portrait"))):String.valueOf(m.get("portrait")));
        		String up = String.valueOf(m.get("userphone"));
        		m.put("userphone", up.substring(0, 4)+"****"+up.substring(up.length()-4));
        	}
        }
        PageInfo<Map<String, Object>> carInfo = new PageInfo<>(carList);
        return carInfo;
    }
}
