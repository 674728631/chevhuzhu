package com.zccbh.test.exception;

/**
 * @Author:                     luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-06-14 16:10
 **/
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.zccbh.util.collect.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/**
 * 全局异常处理器
 * @author CatalpaFlat
 */
public class  CustomExceptionResolver implements HandlerExceptionResolver{
    /**日志log*/
    private static Logger log = LoggerFactory.getLogger(CustomExceptionResolver.class);

    //系统抛出的异常
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {

        ex.printStackTrace();
        ModelAndView mv = new ModelAndView();
        /*  使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常   */
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<>();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        attributes.put("data", "");
        String exceptionInfo = ex.toString();
        System.err.println("exceptionInfo = " + exceptionInfo);
        if (ex instanceof CustomException) {
            CustomException customException = (CustomException) ex;
            attributes.put("code", customException.getCode());
            attributes.put("message", exceptionInfo.substring(42));
        } else {
//            attributes.put("code", "500");
//            if (Constant.toEmpty(exceptionInfo) && Arrays.asList(exceptionInfo.split(":")).contains("org.springframework.jdbc.BadSqlGrammarException")) {
//                attributes.put("message", "SQL出错了,原因是: "+exceptionInfo.split(":")[exceptionInfo.split(":").length-1]+" ,出错类名是: " + stackTrace[0].getFileName() + " 出错方法是: " + stackTrace[0].getMethodName() + ",出错的行数是: " + stackTrace[0].getLineNumber());
//            } else {
//                attributes.put("message", exceptionInfo + " ,出错类名是: " + stackTrace[0].getFileName() + " 出错方法是: " + stackTrace[0].getMethodName() + ",出错的行数是: " + stackTrace[0].getLineNumber());
//            }
            attributes.put("code", "500");
            attributes.put("message", "服务器出现异常");
        }

        view.setAttributesMap(attributes);
        mv.setView(view);
        log.debug("异常:" + ex.getMessage(), ex);
        return mv;
        }
}
