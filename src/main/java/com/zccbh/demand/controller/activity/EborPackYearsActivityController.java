package com.zccbh.demand.controller.activity;


import com.zccbh.demand.service.activities.EborPackYearsActivityService;
import com.zccbh.util.collect.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/eboActivity")
public class EborPackYearsActivityController {

    private Logger logger = LoggerFactory.getLogger(EborPackYearsActivityController.class);

    @Autowired
    private EborPackYearsActivityService eborPackYearsActivityService;

    /**
     * eBo包年用户注册
     * @param param
     * @param request
     * @return
     */
    @RequestMapping(value = "/submitInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String submitInfo(@RequestBody Map<String, Object> param, HttpServletRequest request) {
        Object openId = request.getSession().getAttribute("fromUserName");
        logger.info("提交信息接口{}-{}", param, openId);

        if (null != openId && !"".equals(openId))
            param.put("openId", openId);
        try {
            Map<String, Object> outData = eborPackYearsActivityService.submitInfo(param);
            return Constant.toReModel("200", "SUCCESS", outData);
        } catch (Exception e) {
            logger.error("", e);
            return Constant.toReModel("500", e.getMessage(), null);
        }
    }

    /**
     * eBo包年用户填写验证码
     * @param param
     * @return
     */
    @RequestMapping(value = "/validateCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String validateCode(@RequestBody Map<String, Object> param) {
        logger.info("保存验证码接口{}", param);
        try {
            eborPackYearsActivityService.validateCode(param);
            return Constant.toReModel("200", "SUCCESS", null);
        } catch (Exception e) {
            logger.error("", e);
            return Constant.toReModel("500", e.getMessage(), null);
        }
    }
}
