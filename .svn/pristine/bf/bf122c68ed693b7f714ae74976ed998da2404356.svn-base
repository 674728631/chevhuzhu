package com.zccbh.demand.controller.quartz;

import com.zccbh.demand.mapper.merchants.CbhCarMapper;
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import org.quartz.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.Calendar;


/**
 * 处理定时调度任务的统一入口 支付和添加照片的定时器
 *
 */
public class PayOrAddJob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("【定时任务】开始......");
		try {
			CbhCarMapper carMapper = SpringContextHolder.getBean(CbhCarMapper.class);
			WeiXinUtils weiXinUtils = SpringContextHolder.getBean(WeiXinUtils.class);
			List<Map<String,Object>> carMapLsit =carMapper.selectByCarId();
			if (carMapLsit != null&& carMapLsit.size()>0) {
				carMapLsit.stream().forEach(carMap -> {
					Map<String, String> map = new HashMap<>();
					Date createAt = (Date) carMap.get("createAt");
					BigDecimal money = (BigDecimal) carMap.get("money");
					String carId = carMap.get("id").toString();
					String licensePlateNumber = carMap.get("licensePlateNumber").toString();
					map.put("model", Constant.toEmpty(carMap.get("model")) ? carMap.get("model").toString() : null);
					map.put("openid", carMap.get("openId").toString());
					map.put("licensePlateNumber", licensePlateNumber);
					map.put("carId", carId);
					map.put("money", Constant.toEmpty(money) ? String.valueOf(money.intValue()) : "9");
					map.put("keyword2", DateUtils.getStringDateTime(new Date()));
					try {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(DateUtils.getDateTime());
						calendar.set(Calendar.HOUR_OF_DAY,0);
						calendar.set(Calendar.MINUTE,0);
						calendar.set(Calendar.SECOND,0);
						if(calendar.getTimeInMillis() >createAt.getTime()) {
							Integer status = (Integer) carMap.get("status");
							String customerPN = String.valueOf(carMap.get("customerPN"));
							carMapper.updateBycarId(Integer.valueOf(carId),status);
							if(status==1){
								SmsDemo.sendSms(72, customerPN, licensePlateNumber);
							}
							weiXinUtils.sendTemplate(status == 1 ? 7 : 8, map);
						}
					}catch (Exception e){
						e.printStackTrace();
					}
				});
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("【定时任务】结束......");
	}


}