package com.zccbh.demand.controller.weChat;

import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.Base64;

import sun.tools.jar.resources.jar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component("taskJob")
public class CheckAMTtask {  
	@Autowired
	private CarService carService;
	@Autowired
	private UserCustomerMapper userCustomerMapper;
	@Autowired
	private WeiXinUtils weiXinUtils;
	@Autowired
	private FoundationMapper foundationMapper;
	
    int i=0;  

    @Scheduled(cron = "0 0 11 1 * ?") //0 0 12 ? * SAT
    public void job1() throws Exception {    
    	try {
    		System.out.println("++++++++++++++++定时任务开始++++++++++++++++++");
			List<Map<String,Object>> userList = userCustomerMapper.findUserList();
			if(userList!=null&&userList.size()>0){
				int count = userCustomerMapper.findEventCount();
				Foundation foundation =  foundationMapper.findEntitySingle(new HashMap<String, Object>());
				BigDecimal totalAmount = foundation.getShowTotal().add(foundation.getAmtPaid());
				String showTotal = totalAmount.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
				
				ExecutorService fixedThread = Executors.newFixedThreadPool(100);
				long begin = System.nanoTime();
				System.out.println(new Date());
				for(Map<String,Object> m:userList){
					Runnable run = new Runnable() {					
						@Override
						public void run() {
							String nickname =String.valueOf(m.get("nickname")).equals("null")?String.valueOf(m.get("nickname")):Base64.getFromBase64(String.valueOf(m.get("nickname")));
							String openId = String.valueOf(m.get("openId"));
							String customerPN = String.valueOf(m.get("customerPN"));
							String[] carIds = String.valueOf(m.get("carId")).split(",");
							String[] timeBegins = String.valueOf(m.get("timeBegin")).split(",");
							String[] timeSignouts = String.valueOf(m.get("timeSignout")).split(",");

							String names = "";
							String ids = "";
							BigDecimal totalAmounts = new BigDecimal("0");
							BigDecimal moneys = new BigDecimal("0");
							for(int i=0;i<carIds.length;i++){
								if(!carIds[i].equals("-1")){

									Map<String,Object> rmap = new HashMap<String, Object>();
									rmap.put("timeBegin", timeBegins[i]);
									rmap.put("timeSignout", timeSignouts[i].equals("-1")||timeSignouts[i].equals("1111-11-11 11:11:11.0")||timeSignouts[i].equals("1111-11-11 11:11:11")?"":timeSignouts[i]);
									Map<String,Object> map = userCustomerMapper.findAmtBycar(rmap);
									if(map!=null&&map.get("number")!=null&&!"0".equals(String.valueOf(map.get("number")))){
										int number = Integer.valueOf(String.valueOf(map.get("number")));
										String name = String.valueOf(map.get("names"));
										String id = String.valueOf(map.get("ids"));
										//BigDecimal totalAmount = new BigDecimal("0");
										BigDecimal money = new BigDecimal("0");
										try {
											//totalAmount = new BigDecimal(String.valueOf(map.get("totalAmount")).equals("null")?"0":String.valueOf(map.get("totalAmount")));
											money = new BigDecimal(String.valueOf(map.get("money")).equals("null")?"0":String.valueOf(map.get("money")));
										} catch (Exception e) {
											e.printStackTrace();
										}
										if(!name.equals("")){
											String[] nn = name.split(",");
											String aa = "";
											for(String s:nn){
												s = Base64.getFromBase64(s);
												aa += aa.equals("")?s:","+s;
											}
											names += names.equals("")?aa:","+aa;
										}
										if(!id.equals("")){
											ids += ids.equals("")?id:","+id;
										}
										//totalAmounts = totalAmounts.add(totalAmount);
										moneys = moneys.add(money);
									}
								}
							}
							String[] na = names.split(",");
							List<String> l = new ArrayList<String>();
							if(na.length>1){
								for(String s:na){
									if(!l.contains(s)){
										l.add(s);
									}
								}
							}
							names = "";
							for(String s:l){
								names += names.equals("")?s:","+s;
							}
							String[] ia = ids.split(",");
							List<String> il = new ArrayList<String>();
							if(ia.length>1){
								for(String s:ia){
									if(!il.contains(s)){
										il.add(s);
									}
								}
							}
							Map<String,String> mm = new HashMap<String, String>();
							mm.put("number", String.valueOf(count));
							mm.put("number1",String.valueOf(l.size()));
							mm.put("names", names);
							mm.put("totalAmount", showTotal);
							mm.put("money", String.valueOf(moneys));
							mm.put("nickname", nickname);
							mm.put("openid", openId);
							mm.put("customerPN",customerPN);
							try {
								weiXinUtils.sendTemplate(18, mm);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					};
					fixedThread.submit(run);
					Thread.sleep(100);
				}
				fixedThread.shutdown();
				fixedThread.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				System.out.println("----------定时任务结束-------------耗时："+(System.nanoTime()-begin)/1000_000d+"-----------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        System.out.println((i++)+" taskJob：：："+new Date());    
    }    
} 