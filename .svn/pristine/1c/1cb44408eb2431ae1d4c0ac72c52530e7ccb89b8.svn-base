package com.zccbh.demand.controller.marketing;

import com.alipay.api.domain.Data;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.mapper.activities.CouponModelMapper;
import com.zccbh.demand.pojo.model.ExcelFieldEntity;
import com.zccbh.demand.service.activities.CarWashActivityService;
import com.zccbh.demand.service.activities.CouponService;
import com.zccbh.demand.service.customer.UserCustomerService;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.WxUtil;
import com.zccbh.util.export.ExcelExport;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/activities")
public class ActivityController {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponModelMapper couponModelMapper;

    @Autowired
    private UserCustomerService userCustomerService;

    /**
     * 营销活动模板页面
     */
    @RequestMapping(value = "/model.html", method = RequestMethod.GET)
    public String maintenanceshopDetailPage(ModelMap modelMap) {
        return "marketing/activity-model";
    }

    /**
     * 查询所有营销活动模板数据
     */
    @RequestMapping(value = "/modelList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String maintenanceshopDetail(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> couponModelList = couponService.findCouponModelList(paramModelMap);
            return Constant.toReModel("0", "SUCCESSFUL", couponModelList);
        } catch (Exception e) {
            return Constant.toReModel("4000", "加载营销活动模板失败", e);
        }
    }

    /**
     * 添加活动模板
     */
    @RequestMapping(value = "/saveCouponModel", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("活动模板>添加模板")
    public String saveCouponModel(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> userInfo = (Map<String, Object>) request.getSession().getAttribute("userInfo");
            if (Constant.toEmpty(userInfo.get("id"))) {
                paramModelMap.put("createBy", userInfo.get("id"));
            }
            String result = couponService.saveCouponModel(paramModelMap);
            switch (result) {
                case "0":
                    return Constant.toReModel(result, "SUCCESSFUL", null);
                case "4001":
                    return Constant.toReModel(result, "请输入合法的面值或使用条件", null);
            }
            return Constant.toReModel("4000", "添加模板失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel("4000", "添加模板失败", null);
        }
    }

    /**
     * 修改活动模板
     */
    @RequestMapping(value = "/updateCouponModel", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("活动模板>修改模板")
    public String updateCouponModel(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = couponService.updateCouponModel(paramModelMap);
            switch (result) {
                case "0":
                    return Constant.toReModel(result, "SUCCESSFUL", null);
                case "4001":
                    return Constant.toReModel(result, "请输入合法的面值或使用条件", null);
            }
            return Constant.toReModel("4000", "添加模板失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel("4000", "添加模板失败", null);
        }
    }

    /**
     * 删除活动模板
     */
    @RequestMapping(value = "/deleteCouponModel", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("活动模板>删除模板")
    public String deleteCouponModel(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("isDel", 1);
            String result = couponService.deleteCouponModel(paramModelMap);
            return Constant.toReModel(result, "SUCCESSFUL", null);
        } catch (Exception e) {
            return Constant.toReModel("4000", "删除模板出错", null);
        }
    }

    /**
     * 加载模板数据
     */
    @RequestMapping(value = "/loadModel", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String loadModel(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> eventAssert = couponService.findModelDetail(paramModelMap);
            return Constant.toReModel("0", "SUCCESSFUL", eventAssert);
        } catch (Exception e) {
            return Constant.toReModel("4000", "加载模板出错", e);
        }
    }

    /**
     * 发起活动页面
     */
    @RequestMapping(value = "/launchCoupon.html", method = RequestMethod.GET)
    public String launchCouponPage(Integer modelId, ModelMap modelMap) {
        modelMap.put("modelId", modelId);
        return "marketing/activity-launchCoupon";
    }

    /**
     * 创建一次活动
     */
    @RequestMapping(value = "/saveCoupon", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("活动模板>发起活动")
    public String saveCoupon(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("logoPath", request.getSession().getServletContext().getRealPath("/") + "/cite/images/qrcodeLogo.png");
            Map<String, Object> userInfo = (Map<String, Object>) request.getSession().getAttribute("userInfo");
            if (Constant.toEmpty(userInfo.get("id"))) {
                paramModelMap.put("createBy", userInfo.get("id"));
            }
            String result = couponService.saveCoupon(paramModelMap);
            switch (result) {
                case "0":
                    return Constant.toReModel(result, "SUCCESSFUL", null);
                case "4001":
                    return Constant.toReModel(result, "请输入合法的面值或使用条件", null);
            }
            return Constant.toReModel("4000", "发起活动失败", null);
        } catch (Exception e) {
            return Constant.toReModel("4000", "发起活动失败", e);
        }
    }

    /**
     * 启用活动
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("活动列表>启用活动")
    public String start(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("status", 1);
            String result = couponService.startCoupon(paramModelMap);
            return Constant.toReModel(result, "SUCCESSFUL", null);
        } catch (Exception e) {
            return Constant.toReModel("4000", "启用活动出错", e);
        }
    }

    /**
     * 停用活动
     */
    @RequestMapping(value = "/end", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("活动列表>停用活动")
    public String end(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("status", 2);
            String result = couponService.endCoupon(paramModelMap);
            return Constant.toReModel(result, "SUCCESSFUL", null);
        } catch (Exception e) {
            return Constant.toReModel("4000", "停用活动出错", e);
        }
    }

    /**
     * 编辑活动
     */
    @RequestMapping(value = "/updateCoupon", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("活动列表>编辑活动")
    public String updateCoupon(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = couponService.updateCoupon(paramModelMap);
            switch (result) {
                case "0":
                    return Constant.toReModel(result, "SUCCESSFUL", null);
                case "4001":
                    return Constant.toReModel(result, "请输入合法的面值或使用条件", null);
            }
            return Constant.toReModel("4000", "编辑活动出错", null);
        } catch (Exception e) {
            return Constant.toReModel("4000", "编辑活动出错", null);
        }
    }

    /**
     * 删除活动
     */
    @RequestMapping(value = "/deleteCoupon", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("活动列表>删除活动")
    public String deleteCoupon(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("isDel", 1);
            String result = couponService.deleteCoupon(paramModelMap);
            return Constant.toReModel(result, "SUCCESSFUL", null);
        } catch (Exception e) {
            return Constant.toReModel("4000", "删除活动出错", null);
        }
    }

    /**
     * 加载活动数据
     */
    @RequestMapping(value = "/loadCoupon", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String loadCoupon(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> eventAssert = couponService.findCouponDetail(paramModelMap);
            return Constant.toReModel("0", "SUCCESSFUL", eventAssert);
        } catch (Exception e) {
            return Constant.toReModel("4000", "加载活动出错", e);
        }
    }

    /**
     * 营销活动页面
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String listPage(ModelMap modelMap) {
        return "marketing/activity-list";
    }

    /**
     * 查询所有营销活动数据
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> couponList = couponService.findCouponList(paramModelMap);
            return Constant.toReModel("0", "SUCCESSFUL", couponList);
        } catch (Exception e) {
            return Constant.toReModel("4000", "加载营销活动失败", e);
        }
    }

    /**
     * 开启平台活动
     */
    @RequestMapping(value = "/startActivity", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String startActivity(HttpServletRequest request, @RequestBody String strJson) {

        Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);

        try {
            Map<String, Object> coupon = couponService.selectByModelId(paramModelMap);
            if (Constant.toEmpty(coupon)) {
                return Constant.toReModel("4000", "活动已开启，请不要重复开启", null);
            }

            Map<String, Object> couponModel = couponModelMapper.findSingle(paramModelMap);
            Map<String, Object> map = new HashMap<>();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("name", couponModel.get("name"));
            map.put("description", couponModel.get("description"));
            map.put("num", couponModel.get("num"));
            map.put("amount", couponModel.get("amount"));
            map.put("meetPrice", couponModel.get("meetPrice"));
            map.put("type", couponModel.get("type"));
            map.put("createBy", couponModel.get("createBy"));
            map.put("createAt", time.format(new Date()));
            map.put("verifyBy", couponModel.get("verifyBy"));
            map.put("isDel", 0);
            map.put("modelId", couponModel.get("id"));
            map.put("couponNo", Constant.createCouponNo());
            map.put("status", 1);

            couponService.insertCoupon(map);
            return Constant.toReModel("0", "SUCCESSFUL", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel("4000", "开启活动失败", e);
        }

    }

    @Autowired
    CarWashActivityService carWashActivityService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 一元洗车-判断用户参与资格
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/carwash1RMB", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String carwashActivity(HttpServletRequest request) {

        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request.getParameter("token"));
            if (CollectionUtils.isEmpty(tokenMap))
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            Map<String, Object> outData = carWashActivityService.qualification(tokenMap);
            return Constant.toReModel("200", "SUCCESSFUL", outData);

        } catch (Exception e) {
            logger.error("判断用户参与资格异常====>", e);
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 一元洗车-预支付接口
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/buyCarWashCoupon", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String buyCarWashCoupon(HttpServletRequest request) {
        logger.info("1元洗车微信支付>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request.getParameter("token"));
            if (CollectionUtils.isEmpty(tokenMap))
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            Map<String, Object> param = new HashMap<>();
            param.put("number", request.getParameter("number"));
            param.put("customerId", tokenMap.get("id"));
            param.put("customerPN", tokenMap.get(CommonField.MOBILE_NUMBER));
            String json = carWashActivityService.couponPay(param);
            return Constant.toReModel("200", "返回成功", json);
        } catch (Exception e) {
            logger.error("微信支付异常", e);
            return Constant.toReModel("500", e.getMessage(), e);
        }
    }

    /**
     * 一元洗车-微信支付回调接口
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/wxPayResult", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public void wxPayResult(HttpServletRequest request, HttpServletResponse response) {
        logger.info("微信支付回调>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        PrintWriter writer = null;
        try {
            request.setCharacterEncoding("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            Map<String, Object> wxReturnMap = WxUtil.resolveXml(builder.toString());

            Map<String, Object> outData = carWashActivityService.wxPayResult(wxReturnMap);
            writer = response.getWriter();
            writer.write(outData.get("noticeStr").toString());
            writer.flush();
            carWashActivityService.xiongMaoRegister(outData);
        } catch (Exception e) {
            logger.error("支付回调异常", e);
        } finally {
            if (null != writer)
                writer.close();
        }
    }

    /**
     * 一元洗车-优惠券使用状态核销接口
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/couponUseResult", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String couponUseResult(@RequestBody Map<String, Object> param) {
        int i = carWashActivityService.couponUseResult(param);
        if (i > 0) {
            return Constant.toReModel("200", "SUCCESS", null);
        } else if (-1 == i) {
            return Constant.toReModel("201", "参数错误", null);
        } else {
            return Constant.toReModel("500", "系统异常", null);
        }
    }

    /**
     * 一元洗车-获取用户优惠券
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUserCouponList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getUserCouponList(HttpServletRequest request) {
        logger.info("查询用户一元洗车优惠券======>");
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request.getParameter("token"));
            if (CollectionUtils.isEmpty(tokenMap))
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            List<Map<String, Object>> couponList = carWashActivityService.getUserCouponList(tokenMap);
            return Constant.toReModel("200", "SUCCESS", couponList);
        } catch (Exception e) {
            logger.error("查询用户一元洗车优惠券异常", e);
        }
        return Constant.toReModel("500", "系统异常，请稍后再试。", null);
    }

    /**
     * 一元洗车伙同-财务统计
     *
     * @return
     */
    @RequestMapping(value = "/statCarWashActivity", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String statCarWashActivity(@RequestBody Map<String, Object> param) {
        Map<String, Object> list = carWashActivityService.statCarWashActivity(param);
        return Constant.toReModel("200", "SUCCESS", list);
    }

    /**
     * 财务统计页面
     */
    @RequestMapping(value = "/carWash.html", method = RequestMethod.GET)
    public String eventPage(ModelMap modelMap) {
        return "finance/carWash";
    }

    /**
     * Excel导出
     * @param response
     * @param beginTime
     * @param endTime
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public void exportExcel(HttpServletResponse response, String beginTime, String endTime) {
        Map<String, Object> in = new HashMap<>();
        in.put("beginTime", beginTime);
        in.put("endTime", endTime);
        List<ExcelFieldEntity> rowList = carWashActivityService.exportExcel(in);
        OutputStream out = null;
        try {
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("一元洗车活动.xls".getBytes("UTF-8"), "iso-8859-1")); //excel Name
            out = response.getOutputStream();
        } catch (IOException e) {
            logger.error("", e);
        }
        String[] headers = new String[]{"用户手机", "购买数量", "支付金额", "支付时间", "活动名称"};
        ExcelExport.exportExcel("一元洗车活动", headers, rowList, out, "yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 数量统计
     * @return
     */
    @RequestMapping(value = "/statCarWashTotal", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String statCarWashTotal() {
        return Constant.toReModel("200", "SUCCESS", carWashActivityService.statCarWashTotal());
    }
}
