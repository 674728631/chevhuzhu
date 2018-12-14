package com.zccbh.demand.controller.weChat;

import com.zccbh.demand.mapper.activities.CouponMapper;
import com.zccbh.demand.mapper.activities.MiddleCouponCustomerMapper;
import com.zccbh.demand.mapper.business.MaintenanceshopMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.merchants.CbhMaintenanceshopMapper;
import com.zccbh.demand.service.weChat.PromotionService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaowuge
 * @ClassName: PromotionController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年9月17日 上午9:56:30
 * 渠道拉新接口
 */
@Controller
public class PromotionController {

    private Logger logger = LoggerFactory.getLogger(PromotionController.class);

    @Autowired
    MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;
    @Autowired
    UserCustomerMapper userCustomerMapper;
    @Autowired
    WeiXinUtils weiXinUtils;
    @Autowired
    MiddleCouponCustomerMapper middleCouponCustomerMapper;
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    MaintenanceshopMapper maintenanceshopMapper;
    @Autowired
    CbhMaintenanceshopMapper cbhMaintenanceshopMapper;


    @Autowired
    PromotionService promotionService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/JoinActivities", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String JoinActivities(HttpServletRequest request) {
        try {
            String openId = String.valueOf(request.getSession().getAttribute("fromUserName"));
            if (StringUtils.isBlank(openId) || "null".equals(openId)) {
                return Constant.toReModel(CommonField.FAIL_OPENID, "没有获取到OPENID,请刷新后再试", null);
            }
            Map<String, Object> param = new HashMap<>();
            param.put("shopId", request.getParameter("shopId"));
            param.put("couponNo", request.getParameter("couponNo"));
            param.put("mobileNumber", request.getParameter("mobileNumber"));
            param.put("openId", openId);
            Map<String, Object> rs = promotionService.promotion(param);
            int returnCode = (int) rs.get("returnCode");
            if (500 == returnCode)
                return Constant.toReModel(CommonField.FAIL, "对不起,您已注册登录车V互助", CommonField.PARAMETER_ERROR_PROMPT);
            else if (501 == returnCode)
                return Constant.toReModel(CommonField.ALREADY_RECEIVED, "你已通过其他渠道关注过车V互助", CommonField.PARAMETER_ERROR_PROMPT);
            else if (502 == returnCode)
                return Constant.toReModel(CommonField.ALREADY_RECEIVED, "已经领取过优惠券", CommonField.PARAMETER_ERROR_PROMPT);
            else if (503 == returnCode)
                return Constant.toReModel(CommonField.FAIL, rs.get("flag").toString(), CommonField.PARAMETER_ERROR_PROMPT);
            else if (504 == returnCode)
                return Constant.toReModel(CommonField.FAIL, "没有找到此优惠活动", CommonField.PARAMETER_ERROR_PROMPT);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
        }
        return Constant.toReModel(CommonField.SUCCESS, "", CommonField.PARAMETER_ERROR_PROMPT);
    }
}
