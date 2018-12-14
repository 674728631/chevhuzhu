package com.zccbh.util.base;

import java.time.LocalDateTime;
import java.util.*;

import com.zccbh.util.collect.MD5Util;

public class SecurityUtil {
    /**
     *
     *  @Description    : 身份验证token值算法：
     *                              算法是：将特定的某几个参数一map的数据结构传入，
     *                              进行字典序排序以后进行md5加密,32位小写加密；
     *  @Method_Name    : authentication
     *  @param
     *  @param srcData   约定用来计算token的参数
     *  @return
     */
    public static String authentication(Map<String , Object > srcData){
        //排序，根据keyde 字典序排序
        List<Map.Entry<String,Object>> list = new ArrayList<Map.Entry<String,Object>>(srcData.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Object>>(){
            //升序排序
            public int compare(Map.Entry<String,Object> o1, Map.Entry<String,Object> o2){
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        StringBuffer srcSb = new StringBuffer();
        for(Map.Entry<String , Object>srcAtom : list){
            srcSb.append(String.valueOf(srcAtom.getValue()));
        }
        System.out.println("身份验证加密前字符串："+srcSb.toString());
        //计算token
        String token = MD5Util.md5(srcSb.toString());
//      System.out.println(cToken);//for test
        return token;
    }
    //获得token
    public static String getToKen(){
//        Map<String,Object> haspMap = new HashMap<>();
//        haspMap.put(String.valueOf(Status.SUCCESS),UUIDCreator.getUUID());
//        haspMap.put(String.valueOf(Status.NO_REMOVE),UUIDCreator.getUUID());
//        haspMap.put(String.valueOf(Status.NO_DELETE),UUIDCreator.getUUID());
//        //String authentication =authentication(haspMap);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusDays(CommonField.TOKEN_VALIDITY_PERIOD);
        String string1 = localDateTime.toString().replace("T", "").replace("-","").replace(":","").replace(".","");
        String toKen =string1+UUIDCreator.getUUID()+UUIDCreator.getUUID();
        return toKen;
    }
    
    public static void main(String[] args) {
    	System.out.println(SecurityUtil.getToKen());
	}
}
