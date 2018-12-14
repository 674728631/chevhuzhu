package com.zccbh.demand.service.weChat;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-06-22 10:48
 **/
public class ThreadCache {
    // ThreadLocal里只存储了简单的String对象，也可以自己定义对象，存储更加复杂的参数
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static String getPostRequestParams(){
        return threadLocal.get();
    }

    public static void setPostRequestParams(String postRequestParams){
        threadLocal.set(postRequestParams);
    }

    public static void removePostRequestParams(){
        threadLocal.remove();
    }
}
