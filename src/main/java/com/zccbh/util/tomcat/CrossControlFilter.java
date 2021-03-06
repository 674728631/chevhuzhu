package com.zccbh.util.tomcat;

/**
 * Created by Administrator on 2017/8/2.
 */
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DataSourceContextHolder;
import com.zccbh.util.base.RedisUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class CrossControlFilter implements Filter{

    private boolean isCross = false;

    @Override
    public void destroy() {
        isCross = false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(isCross){
            DataSourceContextHolder.setDbType("dataSource");
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            if(httpServletRequest.getServletPath().indexOf("wxResult") != -1||httpServletRequest.getServletPath().indexOf("wxPayCarResult") != -1){
                System.out.println("wxResult: "+httpServletRequest.getServletPath());
            }else{
                httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
                httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                httpServletResponse.setHeader("Access-Control-Max-Age", "0");
                httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,rqSide");
                httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpServletResponse.setHeader("XDomainRequestAllowed","1");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String isCrossStr = filterConfig.getInitParameter("IsCross");
        isCross = isCrossStr.equals("true")?true:false;
        SAXReader reader = new SAXReader();
        String path1 = CommonField.class.getClassLoader().getResource("").getPath();
        String target = path1.replaceAll("%20"," ");
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new File(target+"/com/zccbh/config/spring.xml"));
            // 通过document对象获取根节点bookstore
            Element bookStore = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = bookStore.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                Element book = (Element) it.next();
                // 获取book的属性名以及 属性值
                List<Attribute> bookAttrs = book.attributes();
                for (Attribute attr : bookAttrs) {
                    if(attr.getName().equals("location")) {
                        String properties = attr.getValue().substring(attr.getValue().indexOf(":") + 1);
                        SpringContextHolder.getBean(RedisUtil.class).put("properties",properties);
                        System.out.println(properties);
                    }

                }

            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(isCrossStr);
        System.out.println("target = " + target);
    }

}
