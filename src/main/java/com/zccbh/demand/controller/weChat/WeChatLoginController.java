package com.zccbh.demand.controller.weChat;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.demand.service.user.UsersService;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;

/**
 * @Project:
 * @Comments:               微信登录
 * @JDK version used:       1.8
 * @Author:                 liuhuan
 * @Create Date:            2018年3月9日
 * @Modified By:            <修改人中文名或拼音缩写>
 * @Modified Date:          <修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: <修改原因描述>
 */
@Controller
public class WeChatLoginController {
	
	@Autowired UsersService usersService;
	
	@Autowired
    private UserCustomerLogService userCustomerLogService;
	 /**
    *
    * @param request       请求
    * @param phoneAndCode  手机号码_验证码
    * @return  method = RequestMethod.POST,
    * @throws UnsupportedEncodingException
    */
   @RequestMapping(value = "/appLogin",produces = "application/json;charset=utf-8")
   @ResponseBody
   public String appLogin(HttpServletRequest request,String phoneAndCode) {
	   String openid = (String) request.getSession().getAttribute("fromUserName");
	   HttpServletRequest hRequest = (HttpServletRequest) request;
	   String agent = hRequest.getHeader("User-Agent");
	   if (agent != null && agent.toLowerCase().indexOf("micromessenger") >= 0 && !Constant.toEmpty(openid)) {
		   return Constant.toReModel(CommonField.FAIL_OPENID, "获取openid失败", null);
	   }
       try {
           Map<String,Object> hashMap = usersService.appLogin(request);
           if(hashMap ==null){
        	   return Constant.toReModel(CommonField.PARAMETER_ERROR,"验证码错误或过期",null);
           }else{
        	   if("2".equals(String.valueOf(hashMap.get("flag")))){
        		   return Constant.toReModel(CommonField.PARAMETER_ERROR,"尊敬的用户您的账户由于违规操作、被举报或其他原因，已经被冻结，如有疑问请联系客服（"+
                           "<a href=\"tel:4000812868\">400-0812-868</a>）",null);
        	   }else{
        		   return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", hashMap);
        	   }
        	   
           }
       } catch (Exception e) {
           e.printStackTrace();
           return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
       }
   }
}
