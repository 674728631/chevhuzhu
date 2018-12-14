package com.zccbh.util.collect;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ConstantNetwork {

    /**
     * @param request
     * @return 浏览器
     */
    public static String toBrowserName(HttpServletRequest request){
        String agent = request.getHeader("User-Agent").toLowerCase();
        if(Constant.toBrowserCheck(agent)){
            Enumeration names = request.getHeaderNames();
            String mobile = "";
            while (names.hasMoreElements()) {
                if (request.getHeader((String) names.nextElement()).contains("iPhone")) {
                    mobile = "iPhone";
                    break;
                }
            }
            if ("".equals(mobile))
                mobile = "android";
            return  mobile;
        }else{
            if(agent.indexOf("msie 7")>0){
                return "ie7";
            }else if(agent.indexOf("msie 8")>0){
                return "ie8";
            }else if(agent.indexOf("msie 9")>0){
                return "ie9";
            }else if(agent.indexOf("msie 10")>0){
                return "ie10";
            }else if(agent.indexOf("msie")>0){
                return "ie";
            }else if(agent.indexOf("opera")>0){
                return "opera";
            }else if(agent.indexOf("opera")>0){
                return "opera";
            }else if(agent.indexOf("firefox")>0){
                return "firefox";
            }else if(agent.indexOf("webkit")>0){
                return "webkit";
            }else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
                return "ie11";
            }else{
                return "Others";
            }
        }
    }

    public static Map<String,Object> toBrowserValueI(HttpServletRequest request){
        Map<String,Object> parameterModelMap = new HashMap<String,Object>();
        Map map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry entry : set) {
            parameterModelMap.put(entry.getKey().toString(),entry.getValue());
        }
        return parameterModelMap;
    }

    /**
     * @param request
     * @return 客户端ip
     */
    public static String toClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }


}
