package com.zccbh.test.exception;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-06-14 16:12
 **/


/**
 * 自定义异常类型
 *
 * @author CatalpaFlat
 */
public class CustomException extends RuntimeException {

    private String code = "500";
    private String message;

    public CustomException() {
    }

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}