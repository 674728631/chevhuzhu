package com.zccbh.demand.service.activities;

import com.zccbh.demand.mapper.activities.PackYearsCodeMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.system.CbhCarLogMapper;
import com.zccbh.demand.mapper.system.CbhUserCustomerLogMapper;
import com.zccbh.demand.pojo.user.Car;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.test.exception.CustomException;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.collect.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EborPackYearsActivityService {

    private Logger logger = LoggerFactory.getLogger(EborPackYearsActivityService.class);

    private final static int EBO_SHOPID = 163;
    private final static String USER_SIGN_UP = "用户注册";
    private final static String USER_ADD_CAR = "用户添加车辆";
    private final static String USER_CAR_GUANCHA = "用户车辆进入观察期";

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

    @Autowired
    CbhUserCustomerLogMapper cbhUserCustomerLogMapper;

    @Autowired
    CbhCarLogMapper cbhCarLogMapper;

    /**
     * 包年信息处理
     *
     * @param param
     * @return
     * @throws CustomException
     */
    @Transactional
    public Map<String, Object> submitInfo(Map<String, Object> param) throws Exception {
//        String code = (String) param.get("code");
        String phoneNo = (String) param.get("phoneNo");
        String carLPN = (String) param.get("carNo");
        String openId = null;
        if (param.containsKey("openId"))
            openId = param.get("openId").toString();
        if (!Constant.toEmpty(phoneNo) || !Constant.toEmpty(carLPN))
            throw new CustomException("500", "参数错误");
//        validateCode(code);
        Map<String, Object> out = new HashMap<>();
        Map<String, Object> rs = validatePhoneNoAndCarLPN(phoneNo, carLPN);
        int customerId = 0;
        int carId = 0;
        if (rs.containsKey("0") || rs.containsKey("1")) {
            if (rs.containsKey("0")) {
                customerId = signIn(phoneNo, openId);
            } else {
                List<Map<String, Object>> cars = (List<Map<String, Object>>) rs.get("1");
                customerId = (int) cars.get(0).get("userId");
                openId = (String) cars.get(0).get("openId");
            }
            Map<String, Object> car;
            try {
                car = registerCar(customerId, phoneNo, carLPN,openId);
            } catch (Exception e) {
                logger.error("保存车辆异常", e);
                throw new CustomException("500", "保存车辆失败");
            }
            carId = ((Long) car.get("id")).intValue();
            try {
                // 异常不需要回滚
                if (rs.containsKey("0"))
                    saveMiddel(openId, customerId, phoneNo);
//                saveRechargeLog(customerId, carId, phoneNo, carLPN);
                car.put("openId", openId);
                // 转入保障中定时器并发送微信通知
                carService.observation(car);
            } catch (Exception e) {
                logger.error("保存关系/插入充值记录/定时器异常", e);
            }
        } else if (rs.containsKey("2")) {
            List<Map<String, Object>> cars = (List<Map<String, Object>>) rs.get("2");
            openId = (String) cars.get(0).get("openId");
            customerId = (int) cars.get(0).get("userId");
            String customerPN = (String) cars.get(0).get("customerPN");
            for (Map<String, Object> car : cars) {
                if (carLPN.equals(car.get("licensePlateNumber"))) {
                    carId = (int) car.get("carId");
                    break;
                }
            }
            if (carId == 0)
                throw new CustomException("500", "车辆错误");
            updateCar4Year(customerId, carId, openId, customerPN);
        } else if (rs.containsKey("3")) {
            customerId = (int) rs.get("3");
            Map<String, Object> user = userCustomerMapper.getUserInfoById(customerId);
            String customerPN = phoneEncryption((String) user.get("customerPN"));
            throw new CustomException("501", "对不起，该车牌号已被手机号" + customerPN + "添加，请检查您的车牌号或手机号是否正确。");
        }
        if (0 == customerId || 0 == carId)
            throw new CustomException("500", "注册失败");
        out.put("customerId", customerId);
        out.put("carId", carId);
        packYearsCodeMapper.saveUserAndCar(out);
        return out;
    }

    /**
     * 验证码保存
     *
     * @param param
     * @return
     * @throws CustomException
     */
    @Transactional
    public int validateCode(Map<String, Object> param) throws CustomException {
        String code = String.valueOf(param.get("code"));
        int customerId = Integer.valueOf(param.get("customerId").toString());
        int carId = Integer.valueOf(String.valueOf(param.get("carId")));
        int codeId = Integer.valueOf(String.valueOf(param.get("id")));
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
            codeInfo.put("code", code);
            codeInfo.put("carId", carId);
            codeInfo.put("customerId", customerId);
            codeInfo.put("use_status", 0);
            codeInfo.put("id", codeId);
            try {
                packYearsCodeMapper.saveCode(codeInfo);
            } catch (Exception e) {
                logger.error("", e);
                throw new CustomException("500", "保存验证码失败！");
            }
        } else {
            throw new CustomException("500", "验证码已使用！");
        }
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
        Car car = carMapper.getCarByLicensePlateNumber(carLPN);
        List<Map<String, Object>> cars = userCustomerMapper.getUserInfoAndCarInfoByPhoneNo(phoneNo);
        // 新用户,新车
        if (CollectionUtils.isEmpty(cars) && null == car) {
            out.put("0", null);
            return out;
        }
        // 老用户，新车
        if(!CollectionUtils.isEmpty(cars) && null == car) {
            out.put("1", cars);
            return out;
        }

        // 老用户，老车
        if(!CollectionUtils.isEmpty(cars) && null != car) {
            int customerId = (int) cars.get(0).get("userId");
            // 车人匹配
            if(customerId == car.getCustomerId()) {
                out.put("2", cars);
                return out;
            }
        }
        // 用户车辆不匹配
        out.put("3", car.getCustomerId());
        return out;
    }

    /**
     * 新用户注册
     *
     * @param phoneNo
     * @param openId
     * @return
     * @throws CustomException
     */
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
//        Date tokenTime = DateUtils.getTokenTime();
//        String toKen = SecurityUtil.getToKen();
//        userInfo.put("toKen", toKen);
//        userInfo.put("tokenaging", tokenTime);
        userInfo.put("customerPN", phoneNo);
        userInfo.put("openId", openId);
        userInfo.put("portrait", headimgurl);
        userInfo.put("nickname", nickname);
        userInfo.put("source", "e泊车");
        userInfo.put("timeJoin", date);
        userInfo.put("status", 1);
        try {
            userCustomerMapper.saveSingle(userInfo);
            int customerId = ((Long) userInfo.get("id")).intValue();
            logger.info("{} 注册成功，id={}。", phoneNo, customerId);
            try {
                userLog(customerId, 1, USER_SIGN_UP);
            } catch (Exception e) {
                logger.error("", e);
            }
            return customerId;
        } catch (Exception e) {
            logger.error("", e);
            throw new CustomException("500", "注册失败！");
        }
    }

    /**
     * 添加新车
     *
     * @param customerId
     * @param mobileNumber
     * @param carLPN
     * @return
     * @throws Exception
     */
    private Map<String, Object> registerCar(int customerId, String mobileNumber, String carLPN,String openId) throws Exception {
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
        car.put("timeSignout", "1111-11-11 11:11:11");
        if (null == openId || "".equals(openId))
            car.put("messageFlag", "50");
        carMapper.saveSingle(car);
        try {
            // 日志异常不回滚
            carLog(customerId, ((Long) car.get("id")).intValue(), 1, USER_ADD_CAR);
            userLog(customerId, 3, USER_ADD_CAR);
        } catch (Exception e) {
            logger.error("", e);
        }
        return car;
    }

    /**
     * 写渠道关系表
     *
     * @param openId
     * @param customerId
     * @param mobileNumber
     * @throws Exception
     */
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
    private void updateCar4Year(int customerId, int carId, String openId, String customerPN) throws CustomException {
        Map<String, Object> car = new HashMap<>();
        car.put("id", carId);
        Map<String, Object> carInfo = carMapper.findCar(car);
        BigDecimal amtCooperation = ((BigDecimal) carInfo.get("amtCooperation")).add(new BigDecimal(99));
        int status = (int) carInfo.get("status");
        if (20 == status) {
            carMapper.updateCar4Year(carId, amtCooperation, (BigDecimal) carInfo.get("amtCooperation"), customerPN, 20);
        } else if (13 == status) {
            carMapper.updateCar4Year(carId, amtCooperation, (BigDecimal) carInfo.get("amtCooperation"), customerPN, 13);
        } else {
            car.put("amtCooperation", amtCooperation);
            car.put("typeGuarantee", 2);
            car.put("status", 13);
            car.put("telCarOwner", customerPN);
            car.put("payTime", DateUtils.formatDate(new Date()));
            car.put("timeSignout", "1111-11-11 11:11:11");
            if(30 == status)
                car.put("reJoinNum", "a");
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
            carLog(customerId, carId, 3, USER_CAR_GUANCHA);
            userLog(customerId, 5, "进入观察期");
        }
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(time.format(new Date()));
        Date date = DateUtils.getYearDate(new Date());
        System.out.println(date);

        System.out.println( DateUtils.getTokenTime());
        Map<String, String> dateMap=DateUtils.getDateMap(300000l);
        String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
        System.out.println(cron);

    }

    // 用户日志
    private void userLog(int customerId, int optType, String optDesc) {
        try {
            Map<String, Object> customer = userCustomerMapper.getUserInfoById(customerId);
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("customerId", customer.get("id"));
            reqMap.put("customerPN", customer.get("customerPN"));
            reqMap.put("source", customer.get("source"));
            reqMap.put("createAt", customer.get("timeJoin"));
            reqMap.put("currentStatus", customer.get("status"));
            reqMap.put("optTime", DateUtils.formatDate(new Date()));
            reqMap.put("optType", optType);
            reqMap.put("optDesc", optDesc);
            reqMap.put("recordTime", DateUtils.formatDate(new Date()));
            cbhUserCustomerLogMapper.saveSingle(reqMap);
        } catch (Exception e) {
            logger.error("用户注册日志失败", e);
        }
    }

    // 车辆日志
    private void carLog(int customerId, int carId, int optType, String optDesc) {
        try {
            Map<String, Object> carLogMap2 = new HashMap<>();
            carLogMap2.put("customerId", customerId);
            carLogMap2.put("carId", carId);
            carLogMap2.put("optTime", DateUtils.formatDate(new Date()));
            carLogMap2.put("optType", optType);
            carLogMap2.put("optDesc", optDesc);
            carLogMap2.put("recordeTime", DateUtils.formatDate(new Date()));
            cbhCarLogMapper.saveSingle(carLogMap2);
        } catch (Exception e) {
            logger.error("用户添车失败", e);
        }
    }
}
