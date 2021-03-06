package com.zccbh.demand.service.activities;

import com.alibaba.fastjson.JSONObject;
import com.zccbh.demand.mapper.activities.CarWashActivityMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.pojo.model.ExcelFieldEntity;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.HttpUtils;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.WxUtil;
import com.zccbh.util.export.ExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarWashActivityService {

    private Logger logger = LoggerFactory.getLogger(CarWashActivityService.class);

    @Autowired
    private UserCustomerMapper userCustomerMapper;

    @Autowired
    private CarWashActivityMapper carWashActivityMapper;

    private final static int COUPON_MAX_NUM = 2;
    private final static int COUPON_FEE = 1;
    private final static int WHILE_COUNT = 3;

    /**
     * 判断用户参与资格
     *
     * @param userInfo
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Map<String, Object> qualification(Map<String, String> userInfo) throws Exception {
        logger.info("判断用户= {} 参与资格>>>>", userInfo);
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("result", 200);
        rsMap.put("message", "OK");
        rsMap.put("number", 0);
        // 根据用户id 查询邀请记录
        Map<String, Object> rs = userCustomerMapper.getInvitationSuccessTempParam(userInfo.get("id"));
        int num = 0;
        if (!CollectionUtils.isEmpty(rs))
            num = Integer.valueOf(rs.get("num").toString());
        if (num < 2) {
            rsMap.put("result", 201);
            rsMap.put("message", "邀请次数不足，当前邀请次数为" + num + ",邀请满2人即可参与");
            return rsMap;
        }
        // 购买次数
        num = 0;
        List<Map<String, Object>> couponList = getUserCouponList(userInfo);
        if (!CollectionUtils.isEmpty(couponList))
            num = couponList.size();
        if (num >= COUPON_MAX_NUM) {
            rsMap.put("result", 202);
            rsMap.put("message", "已到最大购买次数，当前购买次数为" + num + "次");
            return rsMap;
        } else if (num == 0) {
            // 调用熊猫车服接口判断是否注册
            Map<String, String> paramBogy = new HashMap<>();
            paramBogy.put("tel", userInfo.get(CommonField.MOBILE_NUMBER));
//            String str = HttpUtils.doPost("https://carwash.pandacarlife.com/washceshi/Demo/check_reg", paramBogy); // 测试接口
            String str = HttpUtils.doPost("https://carwash.pandacarlife.com/wash/Demo/check_reg", paramBogy); // 正式接口
            JSONObject returnBody = JSONObject.parseObject(str);
            logger.info("熊猫车服判断用户注册接口调用结果>>>>>{}", returnBody);
            if (null == returnBody || !"200".equals(returnBody.get("status").toString())) {
                rsMap.put("result", 203);
                rsMap.put("message", returnBody.get("msg"));
                return rsMap;
            }
        }
        rsMap.put("number", (COUPON_MAX_NUM - num) < 0 ? 0 : (COUPON_MAX_NUM - num));
        return rsMap;
    }

    /**
     * 一元洗车预支付接口
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Transactional
    public String couponPay(Map<String, Object> param) throws Exception {

        //
        int number = Integer.valueOf(param.get("number").toString());
        Map<String, String> temp = new HashMap<>();
        temp.put("id", param.get("customerId").toString());
        List<Map<String, Object>> couponList = getUserCouponList(temp);
        int num = 0;
        if (!CollectionUtils.isEmpty(couponList))
            num = couponList.size();
        if (num >= COUPON_MAX_NUM) {
            throw new RuntimeException("已到最大购买次数，当前购买次数为" + num + "次");
        }
        if (num + number > 2)
            throw new RuntimeException("您还可以购买" + (COUPON_MAX_NUM - num) + "次");

        if (CollectionUtils.isEmpty(param))
            throw new RuntimeException("参数异常");

        int payMoney_int = number * COUPON_FEE * 100;
        // 生成随机订单
        String outTradeNo;
        param.put("payStatus", 0);
        param.put("couponStatus", 0);
        param.put("couponNo", "");
        String orderNo = "";
        for (int i = 1; i <= number; i++) {
            param.put("totalFee", COUPON_FEE);
            logger.info("保存购买记录>>>>>>>>>>>>>>");
            carWashActivityMapper.saveSingle(param);
            orderNo += "".equals(orderNo) ? String.valueOf(param.get("id")) : "|" + String.valueOf(param.get("id"));
        }
        String toCurrTime = Constant.toCurrTime();
        if (toCurrTime.length() > 10) {
            outTradeNo = toCurrTime.substring(toCurrTime.length() - 10, toCurrTime.length()) + "_" + orderNo;
        } else {
            outTradeNo = Constant.toCurrTime() + "_" + orderNo;
        }
        // 获取用户openid
        Map<String, Object> userInfo = userCustomerMapper.getUserInfoById(Integer.valueOf(param.get("customerId").toString()));
        if (CollectionUtils.isEmpty(userInfo))
            throw new RuntimeException("查询用户失败！");

        String result = WxUtil.sendPrepay(Constant.toNowTimeDate().getTime(),
                Constant.toReadPro("chevhuzhuUrl") + "/activities/wxPayResult",
                "127.0.0.1",
                "1元洗车",
                userInfo.get("openId").toString(), //用户openid
                outTradeNo, // 订单
                payMoney_int, //金额
                WxUtil.SuJiShu(),
                "1元洗车");
        Map<String, Object> wxMap = WxUtil.resolveXml(result);//解析xml
        logger.info("统一下单返回xml=", wxMap);
        String json = null;
        if (wxMap.get("prepay_id") != null)//获取prepay_id 封装返回,调H5支付所需参数
            json = String.valueOf(WxUtil.coverH5Pay(wxMap.get("prepay_id")));
        return json;
       /* // 测试
        Map<String, Object> wxReturnMap = new HashMap<>();
        wxReturnMap.put("out_trade_no", outTradeNo);
        ex(wxReturnMap);
        return null;
        //*/
    }

    /**
     * 微信支付回调接口
     *
     * @param wxReturnMap
     * @return
     */
    @Transactional
    public Map<String, Object> wxPayResult(Map<String, Object> wxReturnMap) {
        Map<String, Object> outMap = new HashMap<>();
        List<Map<String, Object>> couponList = new ArrayList<>();
        outMap.put("couponList", couponList);

        String noticeStr;
        try {
            noticeStr = WxUtil.setXML("SUCCESS", "ok");
            if (!CollectionUtils.isEmpty(wxReturnMap) && null != wxReturnMap.get("return_code")) {
                String return_code = wxReturnMap.get("return_code").toString(); // 返回状态码
                logger.info("微信支付回调结果>>>>>>>>>{}", return_code);
                if ("SUCCESS".equals(return_code)) {
                    Map<String, Object> orderInfo = new HashMap<>();
                    String outTradeNo = wxReturnMap.get("out_trade_no").toString().split("_")[1];// 商户订单号
                    String[] orderNos = outTradeNo.split("\\|");
                    for (String orderNo : orderNos) {
                        orderInfo.put("id", orderNo);
                        Map<String, Object> rs = carWashActivityMapper.findSingle(orderInfo);
                        if (!CollectionUtils.isEmpty(rs)) {
                            couponList.add(rs);
                            orderInfo.put("transactionId", wxReturnMap.get("transaction_id").toString());// 微信支付订单号
                            orderInfo.put("timeEnd", wxReturnMap.get("time_end").toString()); // 支付完成时间
                            String result_code = wxReturnMap.get("result_code").toString();// 业务结果
                            logger.info("支付结果={}>>>>", result_code);
                            if ("SUCCESS".equals(result_code)) {
                                orderInfo.put("payStatus", 1);
                            } else {
                                orderInfo.put("payStatus", 2);
                            }
                            orderInfo.put("version", rs.get("version"));
                            int count = carWashActivityMapper.updateModel(orderInfo);
                            if (count == 0) {
                                logger.info("微信支付回调订单信息更新失败>>>>");
                                noticeStr = WxUtil.setXML("FAIL", "FAILURE");
                                outMap.put("noticeStr", noticeStr);
                                return outMap;
                            }
                        }
                    }
                }
            }
            outMap.put("noticeStr", noticeStr);
            return outMap;
        } catch (Exception e) {
            logger.error("支付回调异常", e);
            noticeStr = WxUtil.setXML("FAIL", "FAILURE");
            outMap.put("noticeStr", noticeStr);
            return outMap;
        }
    }

    /**
     * 优惠券使用状态核销接口
     *
     * @param param
     */
    @Transactional
    public int couponUseResult(Map<String, Object> param) {
        logger.info("优惠券使用状态核销========>{}", param);
        if (CollectionUtils.isEmpty(param))
            return -1;
        Map<String, Object> coupon = new HashMap<>();
        coupon.put("customerPN", param.get("tel"));
        String couponStatus = param.get("ev_status").toString();
        if ("已使用".equals(couponStatus))
            coupon.put("couponStatus", 2);
        else if ("未使用".equals(couponStatus))
            coupon.put("couponStatus", 0);
        else if ("预约待使用".equals(couponStatus))
            coupon.put("couponStatus", 1);
        coupon.put("couponNo", param.get("guid"));
        coupon.put("useTime", param.get("use_time").toString());
        Map<String, Object> couponInfo = carWashActivityMapper.getCouponByNo(param.get("guid").toString());
        if (CollectionUtils.isEmpty(couponInfo))
            return -1;
        coupon.put("version", couponInfo.get("version"));
        return carWashActivityMapper.updateByNo(coupon);
    }

    /**
     * 熊猫车服注册接口
     *
     * @param param
     */
    @Transactional
    public void xiongMaoRegister(Map<String, Object> param) {
        logger.info("进入熊猫车服注册接口======>{}", param);
        List<Map<String, Object>> couponList = (List<Map<String, Object>>) param.get("couponList");
        Map<String, String> paramBogy = new HashMap<>();
        paramBogy.put("shopId", "10101");
        if (!CollectionUtils.isEmpty(couponList)) {
            String userTel = couponList.get(0).get("customerPN").toString();
            paramBogy.put("tel", userTel);
            paramBogy.put("num", couponList.size() + "");
            try {

//                String str = HttpUtils.doPost("https://carwash.pandacarlife.com/washceshi/Demo/register", paramBogy); // 测试接口
                String str = HttpUtils.doPost("https://carwash.pandacarlife.com/wash/Demo/register", paramBogy); // 正式接口
                JSONObject returnBody = JSONObject.parseObject(str);

                logger.info("熊猫车服注册接口调用结果>>>>>{}", returnBody);
                if (null != returnBody && "200".equals(returnBody.get("status").toString())) {
                    List<List<Map<String, Object>>> couponListTemp = (List<List<Map<String, Object>>>) returnBody.get("coupon");
                    Map<String, Object> couponInfo = new HashMap<>();
                    for (int i = 1; i <= couponListTemp.size(); i++) {
                        int whileCount = 0;
                        int upCount = 0;
                        // 更新失败重试
                        while (upCount == 0 && whileCount < WHILE_COUNT) {
                            couponInfo.clear();
                            List<Map<String, Object>> data = couponListTemp.get(i - 1);
                            couponInfo.put("id", couponList.get(i - 1).get("id"));
                            Map<String, Object> rs = carWashActivityMapper.findSingle(couponInfo);
                            couponInfo.put("couponNo", data.get(0).get("guid"));
                            couponInfo.put("validTime", data.get(0).get("valid_time"));
                            couponInfo.put("version", rs.get("version"));
                            upCount = carWashActivityMapper.updateModel(couponInfo);
                            whileCount++;
                        }
                        // 重试失败记录
                        if (upCount == 0 && whileCount == WHILE_COUNT)
                            logger.error("用户购买优惠券信息{} 更新失败！！！", couponInfo);
                    }
                } else {
                    throw new RuntimeException(returnBody.get("msg").toString());
                }
            } catch (Exception e) {
                logger.error("调用小猫车服注册接口失败", e);
                throw new RuntimeException("注册！");
            }
        }
    }

    public void ex(Map<String, Object> wxReturnMap) {

        wxReturnMap.put("return_code", "SUCCESS");
        wxReturnMap.put("transaction_id", "asafsfasfsadfasf");
        wxReturnMap.put("time_end", "20181112180000");
        wxReturnMap.put("result_code", "SUCCESS");

        Map<String, Object> outData = wxPayResult(wxReturnMap);
        xiongMaoRegister(outData);
    }

    /**
     * 获取用户优惠券信息
     *
     * @param tokenMap
     * @return
     */
    public List<Map<String, Object>> getUserCouponList(Map<String, String> tokenMap) {
        return carWashActivityMapper.getUserCouponList(tokenMap.get("id"));
    }


    public Map<String, Object> statCarWashActivity(Map<String, Object> inParam) {

        Map<String, Object> outData = new HashMap<>();
        //总数
        Map<String, Object> total = null;
        if (0 == (int) inParam.get("type"))
            total = carWashActivityMapper.carWashTotalBuyNumber(inParam);
        else if (1 == (int) inParam.get("type"))
            total = carWashActivityMapper.carWashTotalBuyNumber1(inParam);
        else if (2 == (int) inParam.get("type"))
            total = carWashActivityMapper.carWashTotalBuyNumber2(inParam);

        int allRow = Integer.valueOf(total.get("count").toString());
        // 总页数
        int totalPage = allRow % 10 == 0 ? allRow / 10 : allRow / 10 + 1;
        // 当前页码
        int currentPage = Integer.valueOf(inParam.get("currentPage").toString()) == 0 ? 1 : Integer.valueOf(inParam.get("currentPage").toString());
        if (currentPage > totalPage)
            currentPage = totalPage;
        if (currentPage == 0)
            currentPage = 1;
        // 当前开始数
        int startRow = (currentPage - 1) * 10;
        // 分页数据
        inParam.put("startNo", startRow);
        inParam.put("pageSize", 10);

        List<Map<String, Object>> list = null;
        if (0 == (int) inParam.get("type"))
            list = carWashActivityMapper.statCarWashActivity(inParam);
        else if (1 == (int) inParam.get("type"))
            list = carWashActivityMapper.statCarWashActivity1(inParam);
        else if (2 == (int) inParam.get("type"))
            list = carWashActivityMapper.statCarWashActivity2(inParam);
        outData.put("totalPage", totalPage);
        outData.put("allRow", allRow);
        outData.put("currentPage", currentPage);
        outData.put("pageList", list);
        return outData;
    }

    public List<ExcelFieldEntity> exportExcel(Map<String, Object> param) {
        List<Map<String, Object>> list = carWashActivityMapper.statCarWashActivity(param);
        List<ExcelFieldEntity> rowList = new ArrayList<>();
        for (Map<String, Object> rowMap : list) {
            String customerPN = rowMap.get("customerPN").toString();
            String buyNum = rowMap.get("buyNum").toString();
            String totalFee = rowMap.get("totalFee").toString();
            String timeEnd = rowMap.get("timeEnd").toString();
            ExcelFieldEntity rowData = new ExcelFieldEntity(customerPN, buyNum, totalFee, timeEnd, "一元洗车");
            rowList.add(rowData);
        }
        return rowList;
    }

    public Map<String, Object> statCarWashTotal() {
        Map<String, Object> inParam = new HashMap<>();
        return carWashActivityMapper.carWashTotalBuyNumber(inParam);
    }
}
