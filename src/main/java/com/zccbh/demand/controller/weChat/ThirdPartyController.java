package com.zccbh.demand.controller.weChat;


import com.alibaba.fastjson.JSONObject;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.customer.UserCustomerService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.order.OrderService;
import com.zccbh.demand.service.order.RecordPaymentService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Controller
public class ThirdPartyController {
	@Autowired UserCustomerService userCustomerService;
	@Autowired UserCustomerMapper userCustomerMapper;
	@Autowired EventMapper eventMapper;
	@Autowired CarService carService;
	@Autowired RecordRechargeMapper recordRechargeMapper;
	@Autowired WeiXinUtils weixinUtils;
	@Autowired EventService eventService;
	@Autowired OrderService orderService;
	@Autowired RecordPaymentService recordPaymentService;
	
        @RequestMapping(value = "/jsInfo",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
        @ResponseBody
        public String wxInfo(HttpServletRequest request,String url){
            try{
                String url_1 = URLDecoder.decode(url, "UTF-8");
                return Constant.toReModel("200","RETRUE SUCCESSFUL", WxUtil.toWxJsInfo(url_1, weixinUtils.getAccessToken()));
            }catch (Exception e) {
            	e.printStackTrace();
                return Constant.toReModel("2222","parameter not is null",null);
            }
        }
        @RequestMapping(value = "/weChatPay",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
        @ResponseBody
        public String wxPayCar(HttpServletRequest request){
            try{
            	Map<String,String> tokenMap = userCustomerService.validationToken(request);
 				if(tokenMap!=null){
 					System.out.println("支付进来了--------------------------------:");
 					String json = "";
 					String customerId = tokenMap.get("id");
 					String[] carIds = request.getParameterValues("CarId");  //选择的车辆id
 					String amountId = request.getParameter("amountId");  //金额的Id
 					BigDecimal a = new BigDecimal("0");
 					String orderNo = "";
 					String outTradeNo = "";
 					 
 					String str = "";
 					for(String carId:carIds){
 						if(amountId.equals("1")){
 							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
 							Date date = new Date();
// 							a = "2018-05-31".equals(df.format(date))||"2018-06-01".equals(df.format(date))||"2018-06-02".equals(df.format(date))||"2018-06-03".equals(df.format(date))||"2018-06-04".equals(df.format(date))?new BigDecimal("6.1"):new BigDecimal("9");
							a = new BigDecimal("9");
 	 					}else{
 	 						Map<String,Object> rMap = carService.getPayAmount(carId);
			        		a =  new BigDecimal("99").subtract(new BigDecimal(String.valueOf(rMap.get("amount"))));
 	 					}
 						Map<String,Object> cmap = new HashMap<String, Object>();
 						cmap.put("id", carId);
 						Map<String,Object> carMap = carService.findCarDetail(cmap);
 						String timeEnd = String.valueOf(carMap.get("timeEnd"));
 						String typeGuarantee = String.valueOf(carMap.get("typeGuarantee"));
 						String licensePlateNumber = String.valueOf(carMap.get("licensePlateNumber"));
 						
 						SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
 	            		Date now = new Date();
 						if(typeGuarantee.equals("2")){
 							if(now.getTime()<time.parse(timeEnd).getTime()){
 								str += str==""?licensePlateNumber+"正在包年中，无法充值":"_"+licensePlateNumber+"正在包年中，无法充值";
 								continue;
 							}
 						}
 						Map<String,Object> pMap = new HashMap<String, Object>();
 						pMap.put("customerId", customerId);
 						pMap.put("carId", carId);
 						pMap.put("amt", a);
 						pMap.put("type", 1);
 						pMap.put("status", 2);
 						pMap.put("eventType", carMap.get("timeBegin")==null?"1":"2");
 	                    recordRechargeMapper.saveSingle(pMap);
 	                    orderNo += orderNo==""?String.valueOf(pMap.get("id")):"|"+String.valueOf(pMap.get("id"));
 	                }
 					if(str!=""){
 						return Constant.toReModel("4444",str,"");
 					}
 					System.out.println("orderNo--------------------------------:"+orderNo);
 					String toCurrTime = Constant.toCurrTime();
 					if(toCurrTime.length()>10){
	                        outTradeNo = toCurrTime.substring(toCurrTime.length()-10,toCurrTime.length())+"_"+orderNo;
	                    }else{
	                        outTradeNo = Constant.toCurrTime()+"_"+ orderNo;
	                    }

	                    Map<String,Object> paramMap = new HashMap<String,Object>();
	                    paramMap.put("id",customerId);
	                    Map<String,Object> associatorInfoModelMap = userCustomerMapper.findUser(paramMap);//查询用户信息 获取用户openid
	                    int payMoney_int = Integer.valueOf(String.valueOf(a.multiply(new BigDecimal(100))).split("\\.")[0]);
	                  // int payMoney_int = 1;
	                    //支付时间,回调(通知地址),ip,商品内容,openid,商户订单号(订单id_随机值),支付金额,随机字符,设备号
	                    System.out.println("地址-----------------------------"+Constant.toReadPro("chevhuzhuUrl")+"/wxPayCarResult");
	                    String result = WxUtil.sendPrepay(Constant.toNowTimeDate().getTime(),
	                    		Constant.toReadPro("chevhuzhuUrl")+"/wxPayCarResult",
	                         "127.0.0.1","车险",
	                         associatorInfoModelMap.get("openId").toString(),
	                         outTradeNo,payMoney_int,
	                         WxUtil.SuJiShu(),"车险");

	                    Map<String, Object> wxMap = WxUtil.resolveXml(result);//解析xml
	                    System.out.println("统一下单返回xml:" + JSONObject.toJSON(wxMap));

	                    
	                    if(wxMap.get("prepay_id") != null)//获取prepay_id 封装返回吊起H5支付所需参数
	                    {
	                    	json = String.valueOf(WxUtil.coverH5Pay(wxMap.get("prepay_id")));

	                    }
	                    return Constant.toReModel("200","返回成功",json);
 				}
 				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }catch (Exception e){
            	e.printStackTrace();
                return Constant.toReModel("4444","ThirdParty",e);
            }
        }
       @RequestMapping(value = "/paymentRepair",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
        @ResponseBody
        public String paymentRepair(HttpServletRequest request){
            try{
            	Map<String,String> tokenMap = userCustomerService.validationToken(request);
    			if(tokenMap!=null){
    				Map<String,Object> peMap = new HashMap<String, Object>();
    				String eventNo = request.getParameter("eventNo");
    				String customerId = tokenMap.get("id");
    	        	peMap.put("eventNo", eventNo);
    				Map<String,Object> eventMap = eventService.findOrderDetail(peMap);
    				BigDecimal amt = new BigDecimal(String.valueOf(eventMap.get("amtPay")).equals("null")?"0":String.valueOf(eventMap.get("amtPay")));
    				Map<String,Object> pMap = new HashMap<String, Object>();
					pMap.put("customerId", customerId);
					pMap.put("eventNo", eventNo);
					pMap.put("carId", eventMap.get("carId"));
					pMap.put("amt", amt);
					pMap.put("type", 1);
					pMap.put("status", 2);
					pMap.put("eventType", 3);
	                recordRechargeMapper.saveSingle(pMap);
	                String orderNo = String.valueOf(pMap.get("id"));
	                System.out.println("理赔支付进来了-----------------------------"+orderNo);
    				String outTradeNo = "";
	                String toCurrTime = Constant.toCurrTime();
 					if(toCurrTime.length()>10){
	                        outTradeNo = toCurrTime.substring(toCurrTime.length()-10,toCurrTime.length())+"_"+orderNo;
	                    }else{
	                        outTradeNo = Constant.toCurrTime()+"_"+ orderNo;
	                    }

	                    Map<String,Object> paramMap = new HashMap<String,Object>();
	                    paramMap.put("id",customerId);
	                    Map<String,Object> associatorInfoModelMap = userCustomerMapper.findUser(paramMap);//查询用户信息 获取用户openid
	                    int payMoney_int = Integer.valueOf(String.valueOf(amt.multiply(new BigDecimal(100))).split("\\.")[0]);
	                   //int payMoney_int = 1;
	                    //支付时间,回调(通知地址),ip,商品内容,openid,商户订单号(订单id_随机值),支付金额,随机字符,设备号
	                    System.out.println("地址-----------------------------"+Constant.toReadPro("chevhuzhuUrl")+"/wxPayCarResult");
	                    String result = WxUtil.sendPrepay(Constant.toNowTimeDate().getTime(),
	                    		Constant.toReadPro("chevhuzhuUrl")+"/paymentRepairResult",
	                         "127.0.0.1","车险",
	                         associatorInfoModelMap.get("openId").toString(),
	                         outTradeNo,payMoney_int,
	                         WxUtil.SuJiShu(),"车险");

	                    Map<String, Object> wxMap = WxUtil.resolveXml(result);//解析xml
	                    System.out.println("统一下单返回xml:" + JSONObject.toJSON(wxMap));
	                    String json = "";
	                    
	                    if(wxMap.get("prepay_id") != null)//获取prepay_id 封装返回吊起H5支付所需参数
	                    {
	                    	json = String.valueOf(WxUtil.coverH5Pay(wxMap.get("prepay_id")));

	                    }
	                    return Constant.toReModel("200","返回成功",json);
                }else{
                	return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
                }
            }catch (Exception e){
            	e.printStackTrace();
                return Constant.toReModel("4444","ThirdParty",e);
            }
        }
       @RequestMapping(value = "/paymentRepairResult",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
       public void paymentRepairResult(HttpServletRequest request,HttpServletResponse response){
           try{
               System.out.println("支付回调进来了----paymentRepairResult-start_____________________________________________");
               request.setCharacterEncoding("UTF-8");
               BufferedReader in=new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
               StringBuilder builder = new StringBuilder();
               String line = null;
               while ((line = in.readLine()) != null)
                   builder.append(line);
               Map<String, Object> WxReturnMap = WxUtil.resolveXml(builder.toString());
               if(Constant.toEmpty(WxReturnMap) && Constant.toEmpty(WxReturnMap.get("out_trade_no"))) {
                   String outTradeNo = WxReturnMap.get("out_trade_no").toString();
                   String orderNo = outTradeNo.split("_")[1];
                   synchronized (orderNo) {
                   	int isZ = carService.paymentRepairResult(orderNo);
                       try{
                           if(isZ == 1){
                            System.out.println("success--------------------");
                           }else{
                               System.out.println("wxResult failure!");
                           }
                       }catch (Exception e){
                       	e.printStackTrace();
                           System.out.println("wx rePay sms or template failure!");
                       }
                   }
               }
               System.out.println("wxResult-ok________________________________________________");
           }catch (Exception e){
           	e.printStackTrace();
               System.out.println(Constant.toReModel("4444","ThirdParty",e));
           }
       }
        /**
         * 微信支付回调
         * @param request
         * @param response
         */
        @RequestMapping(value = "/wxPayCarResult",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
        public void wxRePayOrder(HttpServletRequest request,HttpServletResponse response){
            try{
                System.out.println("支付回调进来了----wxPayCarResult-start_____________________________________________");
                request.setCharacterEncoding("UTF-8");
                BufferedReader in=new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = in.readLine()) != null)
                    builder.append(line);
                Map<String, Object> WxReturnMap = WxUtil.resolveXml(builder.toString());
                if(Constant.toEmpty(WxReturnMap) && Constant.toEmpty(WxReturnMap.get("out_trade_no"))) {
                    String outTradeNo = WxReturnMap.get("out_trade_no").toString();
                    String orderNo = outTradeNo.split("_")[1];
                    synchronized (orderNo) {
                    	PrintWriter writer = response.getWriter();
                    	int isZ = carService.PayCarResult(orderNo);
                        try{
                            if(isZ == 1){
                             System.out.println("success--------------------");
                             String noticeStr = WxUtil.setXML("SUCCESS", "ok");
                             writer.write(noticeStr);
                             writer.flush();
                            }else{
                                System.out.println("wxResult failure!");
                                String noticeStr = WxUtil.setXML("FAIL", "FAILURE");
                                writer.write(noticeStr);
                                writer.flush();
                            }
                        }catch (Exception e){
                        	e.printStackTrace();
                            System.out.println("wx rePay sms or template failure!");
                        }
                    }
                }
                System.out.println("wxResult-ok________________________________________________");
            }catch (Exception e){
            	e.printStackTrace();
                System.out.println(Constant.toReModel("4444","ThirdParty",e));
            }
        }
	@RequestMapping(value = "/orderPay",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
	@ResponseBody
	public String orderPay(HttpServletRequest request){
		try{
			Map<String,String> tokenMap = userCustomerService.validationToken(request);
			if(tokenMap!=null){
				Map<String,Object> peMap = new HashMap<String, Object>();
				String orderNo = request.getParameter("orderNo");
				String customerId = tokenMap.get("id");
				peMap.put("orderNo", orderNo);
				Map<String,Object> orderMap = orderService.weChatGetOrderDetail(peMap);
				BigDecimal amt = new BigDecimal(String.valueOf(orderMap.get("amtAssert")).equals("null")?"0":String.valueOf(orderMap.get("amtAssert")));
				Map<String,Object> pMap = new HashMap<String, Object>();
				pMap.put("customerId", customerId);
				pMap.put("orderNo", orderNo);
				pMap.put("carId", orderMap.get("carId"));
				pMap.put("amt", amt);
				pMap.put("status", 2);
				recordPaymentService.saveRecordPayment(pMap);
				String payId = String.valueOf(pMap.get("id"));
				System.out.println("理赔支付进来了-----------------------------"+payId);
				String outTradeNo = "";
				String toCurrTime = Constant.toCurrTime();
				if(toCurrTime.length()>10){
					outTradeNo = toCurrTime.substring(toCurrTime.length()-10,toCurrTime.length())+"_"+payId;
				}else{
					outTradeNo = Constant.toCurrTime()+"_"+ payId;
				}

				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("id",customerId);
				Map<String,Object> associatorInfoModelMap = userCustomerMapper.findUser(paramMap);//查询用户信息 获取用户openid
				int payMoney_int = Integer.valueOf(String.valueOf(amt.multiply(new BigDecimal(100))).split("\\.")[0]);
				//int payMoney_int = 1;
				//支付时间,回调(通知地址),ip,商品内容,openid,商户订单号(订单id_随机值),支付金额,随机字符,设备号
				System.out.println("地址-----------------------------"+Constant.toReadPro("chevhuzhuUrl")+"/orderPayResult");
				String result = WxUtil.sendPrepay(Constant.toNowTimeDate().getTime(),
						Constant.toReadPro("chevhuzhuUrl")+"/orderPayResult",
						"127.0.0.1","车险",
						associatorInfoModelMap.get("openId").toString(),
						outTradeNo,payMoney_int,
						WxUtil.SuJiShu(),"车险");

				Map<String, Object> wxMap = WxUtil.resolveXml(result);//解析xml
				System.out.println("统一下单返回xml:" + JSONObject.toJSON(wxMap));
				String json = "";

				if(wxMap.get("prepay_id") != null)//获取prepay_id 封装返回吊起H5支付所需参数
				{
					json = String.valueOf(WxUtil.coverH5Pay(wxMap.get("prepay_id")));

				}
				return Constant.toReModel("200","返回成功",json);
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
		}catch (Exception e){
			e.printStackTrace();
			return Constant.toReModel("4444","ThirdParty",e);
		}
	}
	@RequestMapping(value = "/orderPayResult",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public void orderPayResult(HttpServletRequest request,HttpServletResponse response){
		try{
			System.out.println("支付回调进来了----paymentRepairResult-start_____________________________________________");
			request.setCharacterEncoding("UTF-8");
			BufferedReader in=new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = in.readLine()) != null)
				builder.append(line);
			Map<String, Object> WxReturnMap = WxUtil.resolveXml(builder.toString());
			if(Constant.toEmpty(WxReturnMap) && Constant.toEmpty(WxReturnMap.get("out_trade_no"))) {
				String outTradeNo = WxReturnMap.get("out_trade_no").toString();
				String orderNo = outTradeNo.split("_")[1];
				synchronized (orderNo) {
					int isZ = orderService.orderPayResult(orderNo);
					try{
						if(isZ == 1){
							System.out.println("success--------------------");
						}else{
							System.out.println("wxResult failure!");
						}
					}catch (Exception e){
						e.printStackTrace();
						System.out.println("wx rePay sms or template failure!");
					}
				}
			}
			System.out.println("wxResult-ok________________________________________________");
		}catch (Exception e){
			e.printStackTrace();
			System.out.println(Constant.toReModel("4444","ThirdParty",e));
		}
	}
       /* *//**
         * 提现到银行卡
         * @return
         *//*
        @RequestMapping(value = "/wxTx",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
        @ResponseBody
        public String wxTx(HttpServletRequest request){
            try{
                Map<String,Object> parameterModelMap = ConstantNetwork.toBrowserValueI(request);
                Map<String,Object> accountModelMapInfo = new HashMap<String,Object>();
                if(Constant.toEmpty(parameterModelMap.get("associatorId"))){
                    accountModelMapInfo.put("customerId",parameterModelMap.get("associatorId"));
                    accountModelMapInfo = usersInfoService.findIsCustomer(accountModelMapInfo);
                }else{
                    accountModelMapInfo.put("accountId",parameterModelMap.get("accountId"));
                }
                String txPw = Constant.toEmpty(parameterModelMap.get("txPW"))?parameterModelMap.get("txPW").toString():"";
                    accountModelMapInfo.put("accountPW",MD5Util.getMD5Code(txPw));
                accountModelMapInfo.put("accountId",accountModelMapInfo.get("accountId"));
                accountModelMapInfo = usersInfoService.findAccountInfo(accountModelMapInfo);
                if(Constant.toEmpty(accountModelMapInfo)){
                    parameterModelMap.put("accountId",accountModelMapInfo.get("id"));
                    List<Map<String,Object>> bankListModel = usersInfoService.findBankListModel(parameterModelMap);
                    if(Constant.toEmpty(bankListModel) && bankListModel.size() > 0){
                        parameterModelMap.remove("id");
                        String amt = Constant.toEmpty(parameterModelMap.get("amt"))?parameterModelMap.get("amt").toString():"0";
                        String associatorId = Constant.toEmpty(parameterModelMap.get("associatorId"))?parameterModelMap.get("associatorId").toString():"";
                        String bankCardBing = Constant.toEmpty(bankListModel.get(0).get("cardNumber"))?bankListModel.get(0).get("cardNumber").toString():"";
                        String usersName = Constant.toEmpty(bankListModel.get(0).get("cardName"))?bankListModel.get(0).get("cardName").toString():"";
                        String bakCode = Constant.toEmpty(bankListModel.get(0).get("nanksId"))?bankListModel.get(0).get("nanksId").toString():"";

                        System.out.println("wxTx-start_____________________________________________ pc");
                        double doubleAmt = Double.parseDouble(amt);
                        if(doubleAmt > 0 && Constant.toEmpty(bankCardBing) && Constant.toEmpty(usersName) && Constant.toEmpty(bakCode)){
                            String reTx  = thirdPartyService.wxTx(bankCardBing,usersName,bakCode,parameterModelMap);
                            if(reTx.equals("SUCCESS")){
                                System.out.println("wxTx-ok_____________________________________________ pc");
                                try{
                                    SmsDemo.sendSms(41,Constant.toReadPro("financeMobileNumber"),amt);
                                    int i1 = usersInfoService.sendMessage(parameterModelMap);
                                }catch (Exception e){
                                    System.out.println("pc wxTx message push failure!");
                                }
                                return Constant.toReModel("200","SUCCESS",null);
                            }else if(reTx.equals("-1")){
                                return Constant.toReModel("4740","用户账户金额","用户账户金额");
                            }else{
                                return Constant.toReModel("4740","未知错误","未知错误");
                            }
                        }
                    }
                    return Constant.toReModel("4747","非法操作","非法操作");
                }else{
                    return Constant.toReModel("4746","密码错误","密码错误");
                }
            }catch (Exception e){
                  System.out.println("wxTx-error_____________________________________________ pc");
                  Constant.toReModel("4444","ThirdParty",e);
                  String wxReXxml = e.getMessage();
                  if(wxReXxml.indexOf("result_code") != -1){
                      Map<String, Object> WxReturnMap = WxUtil.resolveXml(wxReXxml);
                      if(Constant.toEmpty(WxReturnMap.get("err_code_des"))){
                          wxReXxml = WxReturnMap.get("err_code_des").toString();
                          if(wxReXxml.indexOf("出款账户余额不足") != -1){
                              wxReXxml = "请联系管理员!";
                          }
                          return Constant.toReModel("4404",wxReXxml,wxReXxml);
                      }
                  }
                return Constant.toReModel("4404","未知错误","未知错误");
            }
        }*/



}
