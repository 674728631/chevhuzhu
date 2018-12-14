package com.zccbh.demand.controller.system;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.RedisUtil;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;

/**
 * @Project:
 * @Comments:               短信
 * @JDK version used:       1.8
 * @Author:                 liuhuan
 * @Create Date:            2018年3月9日
 * @Modified By:            <修改人中文名或拼音缩写>
 * @Modified Date:          <修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: <修改原因描述>
 */
@RequestMapping("/msm")
@Controller
public class MSMController {
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/verificationCode",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String quotedPrice(HttpServletRequest request,String mobileNumber){
        String[] mobileNos = mobileNumber.split("_");
        String mobileNo = mobileNos[0];
        String msmType = mobileNos[1];
        String verificationCode = verificationCode();
        System.out.println("verificationCode = " + verificationCode);
        System.out.println(msmType);
        //0是注册1是登录2是修改密码
        switch (msmType){
            case "0":
                try {
                    //Constant.toALSMS(mobileNo,verificationCode,CommonField.REGISTRY);
                    SendSmsResponse sendSmsResponse = SmsDemo.sendSms(1,mobileNo, verificationCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "1":
                try {
                    //Constant.toALSMS(mobileNo,verificationCode,CommonField.LOGIN);
                    SendSmsResponse sendSmsResponse = SmsDemo.sendSms(1,mobileNo, verificationCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "2":
                try {
                    //Constant.toALSMS(mobileNo,verificationCode,CommonField.REPASSWORD);
                    SendSmsResponse sendSmsResponse = SmsDemo.sendSms(1,mobileNo, verificationCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "3":
                try {
                    //Constant.toALSMS(mobileNo,verificationCode,CommonField.LOGIN);
                    SendSmsResponse sendSmsResponse = SmsDemo.sendSms(1,mobileNo, verificationCode);
                    redisUtil.setIntForPhone(mobileNo+ CommonField.EMBODY,verificationCode,1000*60*5
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL",null);
              default:
                  return Constant.toReModel(CommonField.FAIL,"参数错误",null);
        }
        //放入缓存并设置有效期
        System.out.println("verificationCode = " + verificationCode);
        System.out.println("mobileNo = " + mobileNo);
        redisUtil.setIntForPhone(mobileNo,verificationCode,1000*60*15);
        return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL",null);
    }

    //生成随机的短信验证码
    public  String verificationCode() {
        String[] beforeShuffle = new String[]{"1", "2", "3", "4", "5", "6", "7",
                "8", "9", "0"};
        List list = Arrays.asList(beforeShuffle); //将数组转成List
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        return afterShuffle.substring(5, 9);
    }
}
