package com.zccbh.demand.controller.weChat;

import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;


@WebFilter("/*")
public class WeixinAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpServletResponse hResponse = (HttpServletResponse) response;
//        String agent = hRequest.getHeader("User-Agent");
//        //如果session中已经存在微信号了，就不用获取了，否则要获取，获取到以后要存放sesion
//        String fromUserName = (String) hRequest.getSession().getAttribute("fromUserName");
//        // 请求的url
//        String requestUrl = hRequest.getRequestURL().toString();
//        System.out.println(new Date().toString() + "进入doFilter方法的URL: " + requestUrl);
//        if (fromUserName == null && requestUrl != null && requestUrl.endsWith(".html"))
//        {
//            //只有在微信端才做里面的操作
//            if (agent != null && agent.toLowerCase().indexOf("micromessenger") >= 0)
//            {
//                String code = request.getParameter("code");
//                String state = request.getParameter("state");
//                //如果code不为空，scope为base,scope为userInfo代表用户已经同意
//                if (code != null) // && state != null && state.equals("1")
//                {
//                    System.out.println("++++++++++++通过code获取openid++++++++++++++++");
//                    // 通过Code获取openid来进行授权
//                    String url =  WeixinConstants.AUTH_GET_OID.replace("APPID", WeixinConstants.APPID)
//                            .replace("SECRET", WeixinConstants.APPSECRET)
//                            .replace("CODE", code);
//                    String json = HttpClientUtil.httpGet(url);
//                    if (Constant.toEmpty(json)) {
//                        JSONObject jsonObject = JSONObject.fromObject(json);
//                        if (jsonObject.containsKey("openid")) {
//                            hRequest.getSession().setAttribute("fromUserName", jsonObject.getString("openid"));
//                        }
//                    }
//                }
//                else
//                {
//                    System.out.println("进入用户授权的逻辑!!!");
//                    //发送用户同意的请求
//                    String path = hRequest.getRequestURL().toString();
//                    String query = hRequest.getQueryString();
//                    if (query != null) {
//                        path = path + "?" + query;
//                    }
//                    System.out.println("回调地址为: " + path);
//                    String uri = WeixinConstants.AUTH_URL.replace("APPID", WeixinConstants.APPID)
//                            .replace("REDIRECT_URI", URLEncoder.encode(path, "UTF-8"))
//                            .replace("SCOPE", "snsapi_userinfo").replace("STATE", "1");
//                    hResponse.sendRedirect(uri);
//                   return;
//                }
//            }
//        }
        chain.doFilter(hRequest, hResponse);
    }

    @Override
    public void destroy() {

    }

}

