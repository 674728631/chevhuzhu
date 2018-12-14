package com.zccbh.test.exception;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-06-14 16:17
 **/

import org.apache.poi.ss.formula.functions.T;

/**
 * http请求返回success以及error方法封装
 * Created by CatalpaFlat
 * on 2017/4/12.
 */
public class ResultUtils {
    private static final String SUCCESS_MSG = "成功";
    /**
     * http回调成功
     * @param data
     * @return
     */
    public  static Result success(T data){
        Result result = new Result();
        result.setCode(200);
        result.setMessage(SUCCESS_MSG);
        result.setData(data);
        return result;
    }
    /**
     * 无object返回
     * @return
     */
    public  static Result success(){
        return success(null);
    }
    /**
     * http回调错误
     * @param code
     * @param msg
     * @return
     */
    public static Result error(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setData("出错了");
        result.setMessage(msg);
        return  result;
    }

}
