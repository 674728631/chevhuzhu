package com.zccbh.demand.controller.weChat;  
  
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.SmsDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
  
@Component 
public class PaymentTask {  
      
	@Autowired
	private CarMapper carMapper;
	@Autowired
	private WeiXinUtils weiXinUtils;
	
    int i=0;  

    @Scheduled(cron = "0 0 10 * * ?") //"0/5 * *  * * ? " //"0 0 12 ? * SAT"
    public void job1() throws Exception {  
    	System.out.println((i++)+" 每天上午10点的定时任务--------：：："+new Date());
    	try {
    		send(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	/*try {
    		send(2);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    	
        
    }   
    public void send(int status) throws Exception{
    	SimpleDateFormat time =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		List<Map<String,Object>> unPaymentList = carMapper.findUnPaymentList(status);
		if(unPaymentList!=null&&unPaymentList.size()>0){
			for(Map<String,Object> map:unPaymentList){
				String model = String.valueOf(map.get("model"));
				String licensePlateNumber = String.valueOf(map.get("licensePlateNumber"));
				String openId = String.valueOf(map.get("openId"));
				Map<String,String> rmap = new HashMap<String, String>();
				rmap.put("model", model);
				rmap.put("openid", openId);
				rmap.put("licensePlateNumber", licensePlateNumber);
				rmap.put("carId", String.valueOf(map.get("carId")));
				rmap.put("money", status==1?"9":String.valueOf(map.get("amtCooperation")));
				rmap.put("keyword2", time.format(new Date()));
				weiXinUtils.sendTemplate(status==1?7:8, rmap);
				rmap.clear();
				/*rmap.put("messageFlag", String.valueOf(status));
				rmap.put("id", String.valueOf(map.get("carId")));
				carMapper.updateModel(rmap);*/
				
				if(status==1){
					String customerPN = String.valueOf(map.get("customerPN"));
					SmsDemo.sendSms(72, customerPN, licensePlateNumber);
				}
			}
		}
    }
} 