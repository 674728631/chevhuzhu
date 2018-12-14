package com.zccbh.test.exception;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-06-14 16:18
 **/
/**
 * 枚举定义异常类型以及相对于的错误信息
 * 有助于返回码的唯一性
 * Created by CatalpaFlat
 * on 2017/4/12.
 */
public enum ResultEnum {

    to(-1,"未知错误"),
    UNKONW_ERROR(-1,"未知错误"),
    SUCCESS(200,"成功"),
    TEST_ERRORR(500,"测试异常")
    ;

    private Integer code;
    private String msg;
    ResultEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
    public Integer getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
