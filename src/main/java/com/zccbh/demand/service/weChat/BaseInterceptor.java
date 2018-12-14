package com.zccbh.demand.service.weChat;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-06-22 9:30
 **/

import com.zccbh.demand.mapper.user.UserAdminMapper;
import com.zccbh.demand.pojo.user.UserAdmin;
import com.zccbh.util.base.*;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

import static org.jboss.netty.handler.codec.rtsp.RtspHeaders.Names.USER_AGENT;

/**
 * 实现HandlerInterceptor接口，自定义拦截器
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {
    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);

    @Autowired
    private UserAdminMapper userAdminMapper;
    //实现前置方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        System.out.println("uri = " + uri);
        response.setContentType("text/javascript; charset=utf-8");
        LOGGE.info("UserAgent: {}", request.getHeader(USER_AGENT));
        LOGGE.info("用户访问地址: {}, 来路地址: {}", uri);

//        拦截器处理用户权限
        if (!"/appBackstage/appLogin".equals(uri)&&!"/appBackstage/phoneVerification".equals(uri)&&!"/appBackstage/updadePassWord".equals(uri)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            String token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            if (!Constant.toEmpty(token)) {
                response.getWriter().write("{\n" +
                        "    \"code\": \"500\",\n" +
                        "    \"message\": \"对不起,toKen不能为空!\",\n" +
                        "    \"data\": \" \"\n" +
                        "}");
                return false;
            }
            UserAdmin userAdmin = userAdminMapper.selectByToken(MapUtil.build().put("toKen",token).over());
            if (userAdmin == null) {
                response.getWriter().write("{\n" +
                        "    \"code\": \"388\",\n" +
                        "    \"message\": \"您的登录已失效,请重新登录!\",\n" +
                        "    \"data\": \" \"\n" +
                        "}");
                return false;
            }else{
                Date tokenAging = userAdmin.getTokenAging();
                token = userAdmin.getToKen();
                Integer status = userAdmin.getStatus();
                if (tokenAging != null && token != null) {
                    if (!DateUtils.booleanToken(tokenAging, new Date())) {
                        response.getWriter().write("{\n" +
                                "    \"code\": \"388\",\n" +
                                "    \"message\": \"您的登录已失效,请重新登录!\",\n" +
                                "    \"data\": \" \"\n" +
                                "}");
                        return false;
                    }
                    if ( null !=status && 2 == status) {
                        response.getWriter().write("{\n" +
                                "    \"code\": \"500\",\n" +
                                "    \"message\": \"尊敬的管理员您的账户由于违规操作、被举报或其他原因，已经被冻结，如有疑问请联系客服（400-0812-868）\",\n" +
                                "    \"data\": \" \"\n" +
                                "}");
                        return false;
                    }
                }
            }
            ThreadCache.setPostRequestParams(jsonObject.toString());
            LOGGE.info("filer-post请求参数:[params={}]", jsonObject.toString());
            return true;
        }
        byte[] bytes = IOUtils.toByteArray(request.getInputStream());
        String params = new String(bytes, request.getCharacterEncoding());
        ThreadCache.setPostRequestParams(params);
        LOGGE.info("filer-post请求参数:[params={}]", params);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //这个方法可以往request中添加一些公共的工具类给前端页面进行调用

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //当请求处理完成调用
    }
}
