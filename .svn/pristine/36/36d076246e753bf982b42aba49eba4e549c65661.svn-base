package com.zccbh.util.base;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class JSONPost {

    //读取前段post方法传递过来的信息
    public static JSONObject  readJSONStringFromRequestBody(HttpServletRequest request) {
        StringBuffer json = new StringBuffer();
        String line = null ;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null ) {
                json.append(line);
            }
        }
        catch (Exception e) {
            System.out.println( "Error reading JSON string: " + e.toString());
        }
        System.err.println("json = " + json);
        return JSONObject.fromObject(json.toString());
    }
}
