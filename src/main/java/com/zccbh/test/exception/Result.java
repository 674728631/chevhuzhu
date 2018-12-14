package com.zccbh.test.exception;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-06-14 16:13
 **/
/**
 * http请求返回的最外层
 * Created by CatalpaFlat
 * on 2017/4/12.
 */
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "{\"code\": "+code+",\"message\": \""+message+"\",\"data\": "+data+"}";
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result( T data) {
        this.code = 200;
        this.message = "";
        this.data = data;
    }
    public Result() {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
