package com.zccbh.demand.controller;

import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.pojo.system.Log;
import com.zccbh.demand.service.system.LogService;
import com.zccbh.util.collect.Constant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

@Aspect
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private LogService logService;

    @Pointcut("execution(* com.zccbh.demand.controller..*(..))")
    public void controllerAspect() {
    }

    @After("controllerAspect()")
    public void doAfter(JoinPoint point) throws Exception {
        //获取请求访问的方法
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        //判断方法上有没有日志注解
        if (targetMethod.isAnnotationPresent(LogAnnotation.class)) {
            //获取http请求
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //获取当前登录用户信息
            Map<String, Object> userInfo = Constant.toEmpty(request.getSession().getAttribute("userInfo")) ? (Map) request.getSession().getAttribute("userInfo") : null;

            //获取操作人
            String practitioner = null;
            if (Constant.toEmpty(userInfo)) {
                practitioner = (String) userInfo.get("adminUN");
            }
            //获取操作端ip
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            //获取操作描述
            String perform = ((LogAnnotation) targetMethod.getAnnotation(LogAnnotation.class)).value();
            //获取请求URL
            String url = request.getRequestURI().substring(request.getContextPath().length());
            //获取请求参数
            Object[] args = point.getArgs();
            StringBuffer sb = new StringBuffer();
            for (Object o : args) {
                if (o != null) {
                    sb.append(o.toString()).append("|");
                }
            }
            String parm = sb.toString();

            Log log = new Log();
            log.setPractitioner(practitioner);
            log.setIp(ip);
            log.setPerform(perform);
            log.setUrl(url);
            log.setPram(parm);

            logService.saveLog(log);
        }
    }


    @Around(value = "controllerAspect()")
    public String around(ProceedingJoinPoint joinPoint) {
        String result = null;
        String uri = null;
        long start = System.currentTimeMillis();
        try {
            ServletRequestAttributes server = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null == server) {
                result = (String) joinPoint.proceed();
                return result;
            } else {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                uri = request.getRequestURI();
                logger.info("================={}/{}.{}开始执行=====================", uri, joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
                result = (String) joinPoint.proceed();
                long end = System.currentTimeMillis();
                logger.info("===={}耗时{} ms====", uri, (end - start));
                logger.info("================={}执行完成=====================", uri);
                logger.info(result);
            }
        } catch (Throwable throwable) {
            long end = System.currentTimeMillis();
            logger.error("===={}耗时{} ms====", uri, (end - start));
            logger.error("================={}执行异常=====================", uri);
            logger.error("{}", throwable);
        }
        return result;
    }

}