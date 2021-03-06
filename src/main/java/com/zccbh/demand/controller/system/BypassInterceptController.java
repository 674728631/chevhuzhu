package com.zccbh.demand.controller.system;

import com.zccbh.util.collect.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Project:
 * @Comments:               绕开拦截
 * @JDK version used:       1.8
 * @Author:                 DengJian
 * @Create Date:            2017年11月17日
 * @Modified By:            <修改人中文名或拼音缩写>
 * @Modified Date:          <修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: <修改原因描述>
 */
@RequestMapping("/exBypass")
@Controller
public class BypassInterceptController {
//
//    @Autowired
//    private ExOrderInfoService orderInfoService;
//    @Autowired
//    private ThirdPartyService thirdPartyService;
//    @Autowired
//    private OrderTotalService orderTotalService;
//    @Autowired
//    private MessageService messageService;
//    @Autowired
//    private UsersInfoService usersInfoService;
//
    //pc session
    @RequestMapping(value = "/isSession",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String isSession(HttpServletRequest request){
        return Constant.toEmpty(request.getSession().getAttribute("userInfo"))?"1":"0";
    }
//
    // pc 用户退出
    @RequestMapping(value = "/exitLogin",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public void exitLogin(HttpServletRequest request,HttpServletResponse response){
        request.getSession().invalidate();
        Cookie cookie = new Cookie("userInfo", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
//
//    @RequestMapping(value = "/cs",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
//    public void cs(HttpServletRequest request){
//        try{
//
//        }catch (Exception e){
//             Constant.toReModel("4444","ThirdParty",e);
//        }
//    }
//
//    //模拟支付
//    @RequestMapping(value = "/zfCS",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public String zfCS(HttpServletRequest request,@RequestBody String strJson){
//        try{
//            Map<String,Object> parameterModelMap = JSONObject.fromObject(strJson);
//            thirdPartyService.wxRePayOrder(parameterModelMap.get("orderNo").toString());
//
//                Map<String,Object> orderDataInfo = orderInfoService.findOneOrder(parameterModelMap.get("orderNo").toString());
//                JSONObject orderChildJson = JSONObject.fromObject(orderDataInfo.get("reAmt"));//订单返佣金额详情
//                JSONObject payAmtDetail = JSONObject.fromObject(orderDataInfo.get("payAmtDetail"));//支付金额
//                Map<String,Object> paraUserInfo = new HashMap<String,Object>();
//                    paraUserInfo.put("customerId",orderDataInfo.get("customerId"));
//                Map<String,Object> userInfoDataMap = usersInfoService.findIsCustomer(paraUserInfo);
//                String rc = orderChildJson.getString("rc");
//                Map<String,Object> paramModelMap = new HashMap<String,Object>();
//                    paramModelMap.put("orderNo",parameterModelMap.get("orderNo").toString());
//                    paramModelMap.put("customerId",orderChildJson.get("customerId"));
//                    paramModelMap.put("reTg",(int)(Double.parseDouble(orderChildJson.getString("rt"))*100));
//                    paramModelMap.put("reHy",(int)(Double.parseDouble(rc)*100));
//                    paramModelMap.put("zfAmt",(int)(Double.parseDouble(payAmtDetail.getString("amt"))*100));
//                    paramModelMap.put("syAmt",(int)(Double.parseDouble(payAmtDetail.getString("sy"))*100));
//                orderTotalService.saveOrderTotal(paramModelMap);
//
//                Map<String,Object>  paramMessageModel = new HashMap<String,Object>();
//                    paramMessageModel.put("customerId",orderDataInfo.get("customerId"));
//                    paramMessageModel.put("orderNo",parameterModelMap.get("orderNo").toString());
//                    paramMessageModel.put("content", CommonField.pay(rc));
//                    paramMessageModel.put("isRead",1);
//                messageService.saveSingle(paramMessageModel);
//            return Constant.toReModel("8888","",null);
//        }catch (Exception e){
//            return Constant.toReModel("4444","perform error,please contact the administrator.",
//                 new String[]{"BypassInterceptController","ThirdPartyService","UsersInfoService","ExOrderInfoService","OrderTotalService","MessageService"},e);
//        }
//    }
//
//    //提现测试
//    @RequestMapping(value = "/txCS",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public String txCS(HttpServletRequest request,@RequestBody String strJson){
//        try{
//            //String strJson_D = URLDecoder.decode(strJson, "UTF-8");
//            Map<String,Object> parameterModelMap = JSONObject.fromObject(strJson);
//            double doubleAmt = Double.parseDouble(parameterModelMap.get("amt").toString());
//            parameterModelMap.put("associatorId",6);
//            String reTx  = thirdPartyService.wxTx(parameterModelMap.get("bankCardBing").toString(),parameterModelMap.get("usersName").toString(),parameterModelMap.get("bakCode").toString(),parameterModelMap);
//            if(reTx.equals("SUCCESS")){
//                return Constant.toReModel("8888","SUCCESS",null);
//            }else if(reTx.equals("-1")){
//                return Constant.toReModel("4740","用户账户金额","用户账户金额");
//            }else{
//                return Constant.toReModel("4740","未知错误","未知错误");
//            }
//        }catch (Exception e){
//            return Constant.toReModel("4444","ThirdParty",e);
//        }
//    }



}
