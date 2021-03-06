package com.zccbh.demand.controller.system;

import com.zccbh.demand.service.system.MenuService;
import com.zccbh.demand.service.user.UserAdminService;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.base.RedisUtil;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.MD5Util;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisUtil redisUtil;

    //登录页面
    @RequestMapping(value = "/boss",method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
//    	Cookie[] cookies = request.getCookies();
//    	if (cookies != null ) {
//    		for(Cookie cookie2 : cookies){
//        		if (cookie2.getName().equals("userInfo")) {
//        			System.out.println(cookie2.getValue());
//    				Map<String, Object> usersInfo = JSONObject.fromObject(cookie2.getValue());
//    				System.out.println(usersInfo);
//    				HttpSession session = request.getSession();
//    				if (session.getAttribute("userInfo") == null) {
//    					request.getSession().setAttribute("userInfo",usersInfo);
//    				}
//    				
//    				// 账号密码都正确
//    	            String rightsMenu = usersInfo.get("rightsMenu").toString();
//    	            String[] menus = rightsMenu.split(",");
//    	            for (String menu:menus) {
//    	                if ("1".equals(menu)){
//    	                    usersInfo.put("hasMainHtml",true);
//    	                }
//    	            }
//    				return "main";
//    			}
//        	}
//		}
    	Cookie[] cookies = request.getCookies();
    	if (cookies != null ) {
    		for(Cookie cookie2 : cookies){
        		if (cookie2.getName().equals("userInfo")) {
        			System.out.println(cookie2.getValue());
    				String cookieValue = cookie2.getValue();
    				String value[] = cookieValue.split("-");
    				String username = value[0];
    				String password = value[1];
    				Map<String,Object> parameterModelMap = new HashMap<>();
    		        parameterModelMap.put("adminUN", username);
    		        parameterModelMap.put("adminPN",username);
    		        Map<String,Object> usersInfo = userAdminService.selectByUsername(parameterModelMap);
    		        
    				System.out.println(usersInfo);
    				HttpSession session = request.getSession();
    				if (session.getAttribute("userInfo") == null) {
    					request.getSession().setAttribute("userInfo",usersInfo);
    				}
    				
    				// 账号密码都正确
    	            String rightsMenu = usersInfo.get("rightsMenu").toString();
    	            String[] menus = rightsMenu.split(",");
    	            for (String menu:menus) {
    	                if ("1".equals(menu)){
    	                    usersInfo.put("hasMainHtml",true);
    	                }
    	            }
    				return "main";
    			}
        	}
		}

    	return "login";
    }

    // 管理员用户登录
    @RequestMapping(value = "/boss",method = RequestMethod.POST)
    public String loginApplication(HttpServletRequest request,HttpServletResponse response, String username, String password, ModelMap modelMap){
        try {
            if(Constant.toEmpty(username,password)){
                Map<String,Object> parameterModelMap = new HashMap<String,Object>(){{
                    put("adminUN", username);
                    put("adminPN",username);
                    put("adminPW", MD5Util.getMD5Code(password));
                }};
                Map<String,Object> usersInfo = userAdminService.findLoginAdmin(parameterModelMap);
                if(usersInfo != null){
                    String rightsMenu = usersInfo.get("rightsMenu").toString();
                    String[] menus = rightsMenu.split(",");
                    for (String menu:menus) {
                        if ("1".equals(menu)){
                            usersInfo.put("hasMainHtml",true);
                        }
                    }
                   
                    //将用户信息存入cookie
                    Cookie cookie = new Cookie("userInfo",usersInfo.toString());
                    cookie.setMaxAge(360*24*60); //设置为一年有效期
//                    cookie.setPath("/"); //可在同一应用服务器内共享方法
                    response.addCookie(cookie);
                    
                    request.getSession().setAttribute("userInfo",usersInfo);
                    return "main";
                }else{
                    modelMap.put("rl",Constant.toModelMap("4002","",null));
                }
            }else{
                modelMap.put("rl",Constant.toModelMap("4001","",null));
            }
            return "login";
        }catch (Exception e){
            modelMap.put("rl",Constant.toModelMap("4000","",e));
            return "login";
        }
    }

    // 加载左侧菜单
    @RequestMapping(value = "/loadLeftMenu",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String loadLeftMenu(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> reModelMap = new HashMap<String,Object>();
            Map<String,Object> userInfo = (Map<String, Object>) request.getSession().getAttribute("userInfo");
            if(Constant.toEmpty(userInfo.get("rightsMenu"))) {
                String idArr = userInfo.get("rightsMenu").toString();
                Map map = new HashMap();
                map.put("idArr",idArr);
                reModelMap.put("leftMenu", menuService.findMenuList(map));
            }
            return Constant.toReModel("0","SUCCESSFUL",reModelMap);
        }catch (Exception e){
            return Constant.toReModel("4000","LoadLeftMenu",e);
        }
    }

    //首页页面
    @RequestMapping(value = "/main.html",method = RequestMethod.GET)
    public String mainPage(ModelMap modelMap){
    	redisUtil.delect("carIndex");
        return "main";
    }
    
    //微信支付
    @RequestMapping(value = "/verify_9f9a4affb8af882a04053877878e0a1a.html",method = RequestMethod.GET)
	public String checkPage(){
		return "wxcheck";
	}

    // 检验验证码
    @RequestMapping(value = "/phoneVerification",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String phoneVerification(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String mobileNumber = paramModelMap.get("adminPN").toString();
            String verifyCode = paramModelMap.get("verifyCode").toString();
            if (Constant.toEmpty(mobileNumber)&&Constant.toEmpty(verifyCode)) {
                if (verifyCode.equals(redisUtil.getStr(mobileNumber))) {
                    redisUtil.delect(mobileNumber);
                    return Constant.toReModel("0","SUCCESSFUL!",null);
                }
                return Constant.toReModel("4000","验证码错误!",null);
            }
            return Constant.toReModel("4000","手机号和验证码不能为空",null);
        }catch (Exception e){
            return Constant.toReModel("4000","验证失败",e);
        }
    }

    // 重置密码
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String resetPassword(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map resultMap =  userAdminService.resetPassword(paramModelMap);
            return Constant.toReModel(resultMap.get("code").toString(),resultMap.get("message").toString(),null);
        }catch (Exception e){
            return Constant.toReModel("4000","验证失败",e);
        }
    }

    /**
     * 后台系统登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/boss/login",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response, String username, String password){
        try{
            if (!Constant.toEmpty(username) || !Constant.toEmpty(password)){
                return Constant.toReModel("501","用户名或者密码不能为空",null);
            }
            Map<String,Object> parameterModelMap = new HashMap<>();
            parameterModelMap.put("adminUN", username);
            parameterModelMap.put("adminPN",username);
            Map<String,Object> usersInfo = userAdminService.selectByUsername(parameterModelMap);
            if (usersInfo == null){
                return Constant.toReModel("502","您输入的账号不存在，请核对后重新输入",null);
            }
            if (!((String)usersInfo.get("adminPW")).equals(MD5Util.getMD5Code(password))){
                return Constant.toReModel("503","您输入的密码有误，请核对后重新输入",null);
            }
            // 账号密码都正确
            String rightsMenu = usersInfo.get("rightsMenu").toString();
            String[] menus = rightsMenu.split(",");
            for (String menu:menus) {
                if ("1".equals(menu)){
                    usersInfo.put("hasMainHtml",true);
                }
            }
            
            //将用户信息存入cookie
            Cookie cookie = new Cookie("userInfo",username + "-" + password);
            cookie.setMaxAge(360*24*60); //设置为一年有效期
            cookie.setPath("/"); //可在同一应用服务器内共享方法
            response.addCookie(cookie);
            
            request.getSession().setAttribute("userInfo",usersInfo);
            return Constant.toReModel("200","登录成功",null);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("500","服务器出现异常,请稍后再试",null);
        }
    }
}
