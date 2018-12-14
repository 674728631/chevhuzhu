package com.zccbh.demand.controller.weChat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zccbh.demand.service.activities.GiftRecordServcie;
import com.zccbh.demand.service.activities.GiftService;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DrawLotteryUtil;
import com.zccbh.util.collect.Constant;

import net.sf.jsqlparser.statement.create.table.Index;

/** 
 * @ClassName: WeixinDrawController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年9月28日 下午3:34:50 
 *  
 */
@Controller
@RequestMapping(value = "/draw")
public class WeixinDrawController {
	  
	@Autowired
	private GiftService giftService;
	
	@Autowired
	private GiftRecordServcie giftRecordServcie;
	
	@RequestMapping(value = "/startDraw",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
	@ResponseBody
	public String startDraw(HttpServletRequest request){
		String openid = String.valueOf(request.getSession().getAttribute("fromUserName"));
		if (openid.equals("null") || openid.equals("")) {
			return Constant.toReModel(CommonField.FAIL_OPENID, "没有获取到OPENID,请刷新后再试", null);
		}
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("drawNum", request.getParameter("drawNum"));
			map.put("openId", openid);
			List<Map<String, Object>> recordList = giftRecordServcie.getRecordByOpenId(map);
			if(recordList.size() > 0){
				return Constant.toReModel(CommonField.FAIL, "每人只有一次抽奖机会", null);
			}
			List<Map<String, Object>> giftList = giftService.getGiftByDrawNum(map);
			int index = DrawLotteryUtil.drawGift(giftList);
			Map<String, Object> gift = giftList.get(index);
			
			Map<String, Object> recordMap = new HashMap<>();
			recordMap.put("openId", openid);
			recordMap.put("giftId", gift.get("id"));
			SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String date = time.format(new Date());
			recordMap.put("createAt", date);
			giftRecordServcie.saveRecord(recordMap);
			return Constant.toReModel(CommonField.SUCCESS, "抽奖成功", gift);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL, "抽奖失败", null);
		}
	}
	
	@RequestMapping(value = "/saveRecord",method = RequestMethod.POST,produces="application/json;charset=utf-8")
	@ResponseBody
	public String saveRecord(HttpServletRequest request){
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("openId", request.getParameter("openId"));
			map.put("giftId", request.getParameter("giftId"));
			SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String date = time.format(new Date());
			map.put("createAt", date);
			giftRecordServcie.saveRecord(map);
			return Constant.toReModel(CommonField.SUCCESS, "参与成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL, "参与失败", null);
		}
	}
	
	@RequestMapping(value = "updateRecord",method = RequestMethod.POST,produces="application/json;charset=utf-8")
	@ResponseBody
	public String updateRecord(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		map.put("id", request.getParameter("id"));
		map.put("licensePlateNumber", request.getParameter("licensePlateNumber"));
		try {
			giftRecordServcie.updateRecord(map);
			return Constant.toReModel(CommonField.SUCCESS, "绑定成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL, "绑定成功", null);
		}
	}

}
