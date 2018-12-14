package com.zccbh.demand.service.activities;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.zccbh.demand.mapper.activities.PackYearsCodeMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.pojo.user.Car;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.test.exception.CustomException;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.SecurityUtil;
import com.zccbh.util.collect.Constant;
import org.omg.CORBA.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EborPackYearsActivityService {

    private Logger logger = LoggerFactory.getLogger(EborPackYearsActivityService.class);

    private final static int EBO_SHOPID = 163;

    @Autowired
    PackYearsCodeMapper packYearsCodeMapper;

    @Autowired
    CarMapper carMapper;

    @Autowired
    UserCustomerMapper userCustomerMapper;

    @Autowired
    WeiXinUtils weiXinUtils;

    @Autowired
    MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;

    @Autowired
    RecordRechargeMapper recordRechargeMapper;

    @Autowired
    CarService carService;

    @Transactional(rollbackFor = {CustomException.class, Exception.class})
    public void submitInfo(Map<String, Object> param) throws CustomException {
//        String code = (String) param.get("code");
        String phoneNo = (String) param.get("phoneNo");
        String carLPN = (String) param.get("carNo");
        String openId = (String) param.get("openId");
        if (!Constant.toEmpty(phoneNo) || !Constant.toEmpty(carLPN))
            throw new CustomException("500", "参数错误");
//        validateCode(code);
        Map<String, Object> rs = validatePhoneNoAndCarLPN(phoneNo, carLPN);
        if (rs.containsKey("0") || rs.containsKey("1")) {
            int customerId;
            if (rs.containsKey("0")) {
                customerId = signIn(phoneNo, openId);
            } else {
                List<Map<String, Object>> cars = (List<Map<String, Object>>) rs.get("1");
                customerId = (int) cars.get(0).get("id");
                openId = (String) cars.get(0).get("openId");
            }
            Map<String, Object> car;
            try {
                car = registerCar(customerId, phoneNo, carLPN);
            } catch (Exception e) {
                logger.error("保存车辆异常", e);
                throw new CustomException("500", "保存车辆失败");
            }
            int carId = (int) car.get("id");
            try {
                // 异常不需要回滚
                saveMiddel(openId, customerId, phoneNo);
                saveRechargeLog(customerId, carId, phoneNo, carLPN);
                car.put("openId", openId);
                // 转入保障中定时器并发送微信通知
                carService.observation(car);
            } catch (Exception e) {
                logger.error("保存关系/插入充值记录/定时器异常", e);
            }
        } else if (rs.containsKey("2")) {
            List<Map<String, Object>> cars = (List<Map<String, Object>>) rs.get("1");
            openId = (String) cars.get(0).get("openId");
            int carId = 0;
            for (Map<String, Object> car : cars) {
                if (carLPN.equals(car.get("licensePlateNumber"))) {
                    carId = (int) car.get("id");
                    break;
                }
            }
            if (carId == 0)
                throw new CustomException("500", "车辆错误");
            updateCar4Year(carId, openId);
        } else if (rs.containsKey("3")) {
            int customerId = (int) rs.get("3");
            Map<String, Object> user = userCustomerMapper.getUserInfoById(customerId);
            String customerPN = phoneEncryption((String) user.get("customerPN"));
            throw new CustomException("501", "对不起，该车牌号已被手机号" + customerPN + "添加，请检查您的车牌号或手机号是否正确。");
        }
    }


    public int validateCode(Map<String, Object> param) throws CustomException {
        String code = (String) param.get("code");
        String customerId = (String) param.get("customerId");
        String carId = (String) param.get("carId");
        if (!Constant.toEmpty(code) || !Constant.toEmpty(customerId) || !Constant.toEmpty(carId))
            throw new CustomException("500", "参数错误");
        Map<String, Object> codeInfo;
        try {
            codeInfo = packYearsCodeMapper.queryByCode(code);
        } catch (Exception e) {
            logger.error("", e);
            throw new CustomException("500", "验证码错误！");
        }
        if (CollectionUtils.isEmpty(codeInfo)) {
            // 保存验证码
            codeInfo = new HashMap<>();
            codeInfo.put("uuid", code);
            codeInfo.put("carId", carId);
            codeInfo.put("customerId", customerId);
            codeInfo.put("use_status", 0);
            try {
                packYearsCodeMapper.saveSingle(codeInfo);
            } catch (Exception e) {
                logger.error("", e);
                throw new CustomException("500", "保存验证码失败！");
            }
        }
        if (null != codeInfo.get("use_status") && "1".equals(codeInfo.get("use_status").toString()))
            throw new CustomException("500", "验证码已使用！");
        return 0;
    }

    /**
     * 验证车辆及用户
     *
     * @param phoneNo 手机号
     * @param carLPN  车牌号
     * @return 0-新用户 1-老用户，新车  2-老用户,老车  3-用户车辆不匹配
     */
    private Map<String, Object> validatePhoneNoAndCarLPN(String phoneNo, String carLPN) {
        Map<String, Object> out = new HashMap<>();
        List<Map<String, Object>> cars = userCustomerMapper.getUserInfoAndCarInfoByPhoneNo(phoneNo);
        if (!CollectionUtils.isEmpty(cars)) {
            List<String> carLPNs = new ArrayList<>();
            for (Map<String, Object> car : cars) {
                if (null != car.get("licensePlateNumber"))
                    carLPNs.add(car.get("licensePlateNumber").toString());
            }
            if (carLPNs.contains(carLPN)) {
                out.put("2", cars);
                return out;
            }
            out.put("1", cars);
            return out;
        }
        Car car = carMapper.getCarByLicensePlateNumber(carLPN);
        if (null == car) {
            out.put("0", null);
            return out;
        }
        out.put("3", car.getCustomerId());
        return out;
    }

    // 注册
    private int signIn(String phoneNo, String openId) throws CustomException {
        String headimgurl = null;
        String nickname = null;
        if (Constant.toEmpty(openId)) {
            Map<String, String> nicknames;
            try {
                nicknames = weiXinUtils.getNickname(openId);
                headimgurl = nicknames.get("headimgurl");
                nickname = nicknames.get("nickname");
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        Map<String, Object> userInfo = new HashMap<>();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = time.format(new Date());
        Date tokenTime = DateUtils.getTokenTime();
        String toKen = SecurityUtil.getToKen();
        userInfo.put("timeLogin", date);
        userInfo.put("customerPN", phoneNo);
        userInfo.put("toKen", toKen);
        userInfo.put("openId", openId);
        userInfo.put("portrait", headimgurl);
        userInfo.put("nickname", nickname);
        userInfo.put("tokenaging", tokenTime);
        userInfo.put("source", "e泊车");
        try {
            userCustomerMapper.saveSingle(userInfo);
            int customerId = (int) userInfo.get("id");
            logger.info("{} 注册成功，id={}。", phoneNo, customerId);
            return customerId;
        } catch (Exception e) {
            logger.error("", e);
            throw new CustomException("500", "注册失败！");
        }
    }

    // 加车
    private Map<String, Object> registerCar(int customerId, String mobileNumber, String carLPN) throws Exception {
        Map<String, Object> car = new HashMap<>();
        car.put("customerId", customerId);
        car.put("telCarOwner", mobileNumber);
        car.put("licensePlateNumber", carLPN); //车牌
        car.put("amtCompensation", 1000);
        car.put("amtCooperation", 99);
        car.put("typeGuarantee", 2);
        car.put("status", 13);
        car.put("createAt", DateUtils.formatDate(new Date()));
        car.put("payTime", DateUtils.formatDate(new Date()));
        car.put("level", 1);
        car.put("timeEnd", "date_add(c.timeBegin,interval 1 year)");
        carMapper.saveSingle(car);
        return car;
    }

    // 写关系表
    private void saveMiddel(String openId, int customerId, String mobileNumber) throws Exception {
        Map<String, Object> rMap = new HashMap<>();
        rMap.put("openId", openId);
        rMap.put("customerId", customerId);
        rMap.put("maintenanceshopId", EBO_SHOPID);
        rMap.put("customerTel", mobileNumber);
        rMap.put("responseNumber", 0);
        rMap.put("status", 0);
        logger.info("{} 新用户,未关注，保存关注关系。", mobileNumber);
        middleCustomerMaintenanceshopMapper.saveSingle(rMap);
    }

    // 插入充值记录
    private void saveRechargeLog(int customerId, int carId, String customerPN, String licensePlateNumber) throws Exception {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("customerId", customerId);
        pMap.put("carId", carId);
        pMap.put("amt", 99);
        pMap.put("type", 3);
        pMap.put("status", 1);
        pMap.put("eventType", "1");
        pMap.put("timeRecharge", DateUtils.formatDate(new Date()));
        pMap.put("description", "手机号为" + customerPN + "的会员为" + licensePlateNumber + "充值了99元");
        recordRechargeMapper.saveSingle(pMap);
    }

    // 手机号加密
    private String phoneEncryption(String phone) {
        if (null == phone || "".equals(phone) || phone.trim().length() < 11)
            return "*";
        char[] numbers = phone.toCharArray();
        for (int i = 0; i < numbers.length; i++) {
            if (i < 3 || i > 6)
                continue;
            numbers[i] = '*';
        }
        return new String(numbers);
    }

    // 更新车
    private void updateCar4Year(int carId, String openId) throws CustomException {
        Map<String, Object> car = new HashMap<>();
        car.put("id", carId);
        Map<String, Object> carInfo = carMapper.findCar(car);
        BigDecimal amtCooperation = ((BigDecimal) carInfo.get("amtCooperation")).add(new BigDecimal(99));
        int status = (int) carInfo.get("status");
        if (!"20".equals(carInfo.get("status").toString())) {
            car.put("status", 13);
            if ("30".equals(carInfo.get("status").toString())) {
                car.put("rereJoinNum", ((int) carInfo.get("rereJoinNum")) + 1);
            }
            car.put("payTime", DateUtils.formatDate(new Date()));
            car.put("observationEndTime", DateUtils.formatDate(new Date()));
        }
        if (20 == status || 13 == status) {
            carMapper.updateCar4Year(carId, amtCooperation, (BigDecimal) carInfo.get("amtCooperation"));
        } else {
            car.put("amtCooperation", amtCooperation);
            car.put("typeGuarantee", 2);
            car.put("status", 13);
            car.put("payTime", DateUtils.formatDate(new Date()));
            car.put("timeEnd", "date_add(c.timeBegin,interval 1 year)");
            try {
                carMapper.updateModel(car);
            } catch (Exception e) {
                logger.error("更新车辆", e);
                throw new CustomException("500", "更新车辆失败");
            }
            car.put("licensePlateNumber", carInfo.get("licensePlateNumber"));
            car.put("openId", openId);
            car.put("amtCompensation", carInfo.get("amtCompensation"));
            try {
                carService.observation(car);
            } catch (Exception e) {
                logger.error("进入观察期定时器", e);
            }
        }
    }
}
