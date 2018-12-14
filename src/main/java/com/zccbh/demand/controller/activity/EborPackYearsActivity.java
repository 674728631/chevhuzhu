package com.zccbh.demand.controller.activity;


import com.zccbh.demand.service.activities.EborPackYearsActivityService;
import com.zccbh.test.exception.CustomException;
import com.zccbh.util.collect.Constant;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/eboActivity")
public class EborPackYearsActivity {

    private Logger logger = LoggerFactory.getLogger(EborPackYearsActivity.class);

    @Autowired
    private EborPackYearsActivityService eborPackYearsActivityService;


    @RequestMapping(value = "/submitInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String submitInfo(@RequestBody Map<String,Object> param, HttpServletRequest request) {
        String openId = String.valueOf(request.getSession().getAttribute("fromUserName"));
        logger.debug("提交信息接口{}-{}", param,openId);
        param.put("openId",openId);
        try {
            eborPackYearsActivityService.submitInfo(param);
        } catch (CustomException e) {
            logger.error("", e);
            return Constant.toReModel(e.getCode(), e.getMessage(), null);
        }

        return null;
    }

    @RequestMapping(value = "/validateCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String validateCode(@RequestBody Map<String,Object> param, HttpServletRequest request) {
        logger.debug("提交信息接口{}", param);
        try {
            eborPackYearsActivityService.validateCode(param);
        } catch (CustomException e) {
            logger.error("", e);
            return Constant.toReModel(e.getCode(), e.getMessage(), null);
        }

        return null;
    }
}
