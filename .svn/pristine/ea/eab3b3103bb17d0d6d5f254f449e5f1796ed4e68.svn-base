package com.zccbh.util.base;

import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.ConstantNetwork;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CommonInterceptor extends HandlerInterceptorAdapter{

    private final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());
        String method = request.getMethod();

        String strBackUrl = "http://" + request.getServerName() + ":" + request.getServerPort()+ contextPath;

        String browser = "",rqSide="";
        Map<String,Object> userInfo = Constant.toEmpty(request.getSession().getAttribute("userInfo"))?(Map)request.getSession().getAttribute("userInfo"):null;

        int logInfoType = 0;
        try{
            rqSide = Constant.toEmpty(request.getHeader("rqSide"))?request.getHeader("rqSide"):Constant.toEmpty(request.getParameter("rqSide"))?request.getParameter("rqSide"):"";
            browser = ConstantNetwork.toBrowserName(request);
            String logUrl = Constant.toStrTime() + " "+ browser + " " + ConstantNetwork.toClientIp(request) + " " + url + " " + method;
            String parameterJson = !url.equals("/boss/")?JSONObject.fromObject(ConstantNetwork.toBrowserValueI(request)).toString():"";
            System.out.println("----------------------------------------------------------------");
            System.out.println(logUrl);
            logInfoType = rqSide.equals(Constant.toB64("PC","+"))?1:
                                rqSide.equals(Constant.toB64("H5","+"))?21:
                                    rqSide.equals(Constant.toB64("ios","+"))?22:
                                        rqSide.equals(Constant.toB64("android","+"))?23:0;
            if(((logInfoType == 1 && userInfo != null) || (logInfoType == 21 || logInfoType == 22 || logInfoType == 23)) && parameterJson.length() < 255){
                Map<String,Object> paramLogModelMap = new HashMap<String,Object>();
                    paramLogModelMap.put("type",logInfoType);
                    paramLogModelMap.put("url",logUrl);
                    paramLogModelMap.put("pram",Constant.toKL(parameterJson));
            }
            System.out.println("----------------------------------------------------------------");
        }catch (Exception e){
            System.out.println("获取请求信息失败!");
        }

        if(userInfo != null || url.indexOf("index") != -1 || url.equals("/boss/") || url.indexOf("/exBypass/") != -1 || url.indexOf("notify") != -1 || url.indexOf("wxResult") != -1)
            return true;

        if(Constant.toEmpty(rqSide)){
            switch (logInfoType){
                case 1:
                    System.out.println("cs ------ pc ");
                    if(Constant.toEmpty(userInfo)){
                        return true;
                    }else{
                        request.getRequestDispatcher(strBackUrl).forward(request, response);
                        return false;
                    }
                case 21:
                    System.out.println("cs ------ h5 ");
                    return true;
                case 22:
                    System.out.println("cs ------ iphone " + (browser.equals("iPhone")?2:1));
                    return true;
                case 23:
                    System.out.println("cs ------ android " + (browser.equals("android")?2:1));
                    return true;
                default:
                    return false;
            }
        }else{
            request.getRequestDispatcher(strBackUrl).forward(request, response);
            return false;
        }
    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {}

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}

}
