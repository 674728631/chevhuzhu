package com.zccbh.demand.controller.weChat;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.activities.MiddleCouponCustomerMapper;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.InvitationCustomerMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.event.EventApplyMapper;
import com.zccbh.demand.mapper.event.EventCommentMapper;
import com.zccbh.demand.mapper.event.EventComplaintMapper;
import com.zccbh.demand.mapper.event.EventReceivecarMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.service.business.MaintenanceshopService;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.customer.MessageBackstageService;
import com.zccbh.demand.service.customer.MessageService;
import com.zccbh.demand.service.customer.UserCustomerService;
import com.zccbh.demand.service.event.DistributionOrder;
import com.zccbh.demand.service.event.EventCommentService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.order.OrderService;
import com.zccbh.demand.service.order.RecordPaymentService;
import com.zccbh.demand.service.user.UserAdminService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/order")
@Controller
public class InsuranceIndexController {
	@Autowired EventService eventService;
	@Autowired CarService carService;
	@Autowired CarMapper carMapper;
	@Autowired UserCustomerService userCustomerService;
	@Autowired EventComplaintMapper eventComplaintMapper;
	@Autowired EventCommentMapper eventCommentMapper;
	@Autowired EventReceivecarMapper eventReceivecarMapper;
	@Autowired RecordRechargeMapper recordRechargeMapper;
	@Autowired MessageService messageService;
	@Autowired MaintenanceshopService maintenanceshopService;
	@Autowired EventCommentService eventCommentService;
	@Autowired FoundationMapper foundationMapper;
	@Autowired AccountMapper accountMapper;
	@Autowired EventApplyMapper eventApplyMapper;
	@Autowired MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;
	@Autowired DistributionOrder distributionOrder;
	@Autowired WeiXinUtils weiXinUtils;
	@Autowired MiddleCouponCustomerMapper middleCouponCustomerMapper;
	@Autowired
	private InvitationCustomerMapper invitationCustomerMapper;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RecordPaymentService recordPaymentService;
	@Autowired
	private MessageBackstageService messageBackstageService;
	@Autowired
    private UserAdminService userAdminService;

	/**
	 * 保险理赔发起
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/compensation",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String compensation(HttpServletRequest request) throws Exception{
		try {
			Map<String,String> tokenMap = userCustomerService.validationToken(request);
			if(tokenMap!=null){
				String result = orderService.weChatCompensation(request,tokenMap);
				if(!result.equals("")&&result.indexOf("理赔")!=-1){
					return Constant.toReModel(CommonField.FAIL, result, CommonField.PARAMETER_ERROR_PROMPT);
				}
				return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", result);
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
    } catch (Exception e) {
    	e.printStackTrace();
    	return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }
	}

	@RequestMapping(value = "/compensateList",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String compensateList(HttpServletRequest request){
        try{
        	Map<String,String> tokenMap = userCustomerService.validationToken(request);
			if(tokenMap!=null){
				Map<String,Object> paramModelMap = new HashMap<String, Object>();
				paramModelMap.put("pageNo", request.getParameter("pageNo"));
				paramModelMap.put("pageSize", request.getParameter("pageSize"));
				paramModelMap.put("status", request.getParameter("status"));
				paramModelMap.put("customerId", tokenMap.get("id"));
				PageInfo<Map<String, Object>> carList = orderService.weChatFindOrderList(paramModelMap);
				return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL",carList);
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
        }catch (Exception e){
        	e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,e);
        }
    }

	@RequestMapping(value = "/goOrderDetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String goOrderDetail(HttpServletRequest request){
        try{
        	Map<String,String> tokenMap = userCustomerService.validationToken(request);
			if(tokenMap!=null){
				Map<String,Object> pMap = new HashMap<String, Object>();
	        	pMap.put("orderNo", request.getParameter("orderNo"));
				Map<String, Object> detail = orderService.weChatGetOrderDetail(pMap);
				return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL",detail);
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,e);
        }
    }
	@RequestMapping(value = "/orderPay1",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
	@ResponseBody
	public String orderPay1(HttpServletRequest request){
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
				int isZ = orderService.orderPayResult(payId);
				if(isZ==1){
					return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL","success");
				}else{
					return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,"");
				}
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
		}catch (Exception e){
			e.printStackTrace();
			return Constant.toReModel("4444","ThirdParty",e);
		}
	}
	@RequestMapping(value = "/comfirmAssert",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
	@ResponseBody
	public String comfirmAssert(HttpServletRequest request){
		try{
			Map<String,String> tokenMap = userCustomerService.validationToken(request);
			if(tokenMap!=null){
				Map<String,Object> pMap = new HashMap<String, Object>();
				String orderNo = request.getParameter("orderNo");
				SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = time.format(new Date());
				pMap.put("deliverCarTime",date);
				pMap.put("status", 31);
				pMap.put("orderNo",orderNo);
				orderService.updateOrder(pMap);
				return Constant.toReModel("200","返回成功","success");
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
		}catch (Exception e){
			e.printStackTrace();
			return Constant.toReModel("4444","ThirdParty",e);
		}
	}
	@RequestMapping(value = "/publicity",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
	@ResponseBody
	public String publicity(HttpServletRequest request){
		try{
			Map<String,Object> paramModelMap = new HashMap<String, Object>();
			paramModelMap.put("pageNo", request.getParameter("pageNo"));
			paramModelMap.put("pageSize", request.getParameter("pageSize"));
			PageInfo<Map<String, Object>> list = orderService.findPublicityList(paramModelMap);
			return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL",list);
		}catch (Exception e){
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,e);
		}
	}
	@RequestMapping(value = "/publicityDetail",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String publicityDetail(HttpServletRequest request){
        try{
        	Map<String,Object> pMap = new HashMap<String, Object>();
			pMap.put("orderNo", request.getParameter("orderNo"));
			Map<String, Object> detail = orderService.weChatGetOrderDetail(pMap);
			String model = String.valueOf(detail.get("model"));
			detail.put("model", model.substring(0,model.length()-3)+"***");
//			String licensePlateNumber = String.valueOf(detail.get("licensePlateNumber"));
//			detail.put("licensePlateNumber", carService.hideStr(licensePlateNumber));
			detail.put("nameCarOwner", eventService.hideName(String.valueOf(detail.get("nameCarOwner"))));
			return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL",detail);
        }catch (Exception e){
        	e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,e);
        }
    }
	@RequestMapping(value = "/insertReceivecar",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String insertReceivecar(HttpServletRequest request){
        try{
        	Map<String,String> tokenMap = userCustomerService.validationToken(request);
			if(tokenMap!=null){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("orderNo", request.getParameter("orderNo")); //订单号
				map.put("carOwnerName", request.getParameter("nameCarOwner"));  //联系人
				map.put("carOwnerTel", request.getParameter("telCarOwner")); //电话
				map.put("deliverPlace", request.getParameter("place"));  //地址
				Map<String,Object> detail = eventService.findOrderDetail(map);
				map.put("licensePlateNumber", String.valueOf(detail.get("licensePlateNumber")));  
				map.put("deliverLongitude", request.getParameter("longitude")); //经度
				map.put("deliverLatitude", request.getParameter("latitude"));  //维度
				map.put("reciveCarTime", request.getParameter("reciveCarTime"));  //用户填写接车时间
				SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = time.format(new Date());
				map.put("applyDistributionTime",date);

				map.put("status", 10);
				orderService.updateOrder(map);
				// 保存后台消息
				Map<String, Object> backMsgParam = new HashMap<>();
				backMsgParam.put("type",2);
				backMsgParam.put("orderNo",request.getParameter("orderNo"));
				backMsgParam.put("title","待分单");
				backMsgParam.put("content",String.valueOf(detail.get("licensePlateNumber")) + "的保险理赔订单等待您分单！");
				backMsgParam.put("isSolve",1);
				backMsgParam.put("orderStatus",10);
				backMsgParam.put("createTime",DateUtils.formatDate(new Date()));
				messageBackstageService.save(backMsgParam);
                // 发送推送消息
                Map<String, Object> pushMsgMap = new HashMap<>();
                pushMsgMap.put("orderNo", request.getParameter("orderNo"));
                pushMsgMap.put("type", 2);
                pushMsgMap.put("orderStatus", 10);
                userAdminService.pushMessageToManager("order","待分单",String.valueOf(detail.get("licensePlateNumber")) + "的保险理赔订单等待您分单！",pushMsgMap);
				return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL","success");
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
        }catch (Exception e){
        	e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,e);
        }
    }
	@RequestMapping(value = "/updateComplaint",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String updateComplaint(HttpServletRequest request){
        try{
        	Map<String,String> tokenMap = userCustomerService.validationToken(request);
			if(tokenMap!=null){
				Map<String,Object> map = new HashMap<String, Object>();
				String content = request.getParameter("content");
				map.put("orderNo", request.getParameter("orderNo")); //订单号
				SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				String date = time.format(new Date());
				if(content!=null&&!"".equals(content)){
					map.put("complaintTime", date);
					map.put("createAt", date);
					map.put("complaintContent", content);
					map.put("status", 71);
					orderService.updateOrder(map);
					// 保存后台消息
					Map<String, Object> orderDetail = orderService.findDetail(MapUtil.build().put("orderNo", request.getParameter("orderNo")).over());
					Map<String, Object> backMsgParam = new HashMap<>();
					backMsgParam.put("type",2);
					backMsgParam.put("orderNo",request.getParameter("orderNo"));
					backMsgParam.put("title","投诉中");
					if (orderDetail != null){
						backMsgParam.put("content",orderDetail.get("licensePlateNumber") + "的保险理赔订单在投诉中，等待您处理！");
					}
					backMsgParam.put("isSolve",1);
					backMsgParam.put("orderStatus",71);
					backMsgParam.put("createTime", DateUtils.formatDate(new Date()));
					messageBackstageService.save(backMsgParam);
					// 发送推送消息
					Map<String, Object> pushMsgMap = new HashMap<>();
					pushMsgMap.put("orderNo", backMsgParam.get("orderNo"));
					pushMsgMap.put("type", backMsgParam.get("type"));
					pushMsgMap.put("orderStatus", backMsgParam.get("orderStatus"));
					userAdminService.pushMessageToManager("order",(String) backMsgParam.get("title"),(String) backMsgParam.get("content"),pushMsgMap);
				}else{   //撤销投诉
					/*if(String.valueOf(detail.get("statusEvent")).equals("71")||String.valueOf(detail.get("statusEvent")).equals("100")){
						return Constant.toReModel(CommonField.TOKEN_FAILURE, "状态错误，请勿重复提交", CommonField.PARAMETER_ERROR_PROMPT);
					}*/
					map.clear();
					map.put("orderNo", request.getParameter("orderNo")); //订单号
					map.put("unComplaintTime", date);
					map.put("status", 51);
					orderService.updateOrder(map);
				}
				return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL","success");
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
        }catch (Exception e){
        	e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,e);
        }
    }
	@RequestMapping(value = "/insertComment",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String insertComment(HttpServletRequest request){
        try{
        	Map<String,String> tokenMap = userCustomerService.validationToken(request);
			if(tokenMap!=null){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("orderNo", request.getParameter("orderNo")); //订单号
				map.put("labelContent", request.getParameter("labelContent")); //
				map.put("commentContent", request.getParameter("content")); //
				map.put("commentScore", request.getParameter("score")); //
				SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				String date = time.format(new Date());
				map.put("completeTime", date);
				map.put("status",100);
				orderService.updateOrder(map);
				return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL","success");
			}else{
				return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
			}
        }catch (Exception e){
        	e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,e);
        }
    }



}
