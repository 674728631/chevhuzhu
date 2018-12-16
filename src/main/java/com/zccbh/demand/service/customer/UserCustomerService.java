package com.zccbh.demand.service.customer;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.util.base.Base64;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.collect.Constant;

import com.zccbh.util.uploadImg.UploadFileUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserCustomerService {
    @Autowired
    private UserCustomerMapper customerMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private MiddleCustomerQrcodeService middleCustomerQrcodeService;
    @Autowired
    private UserCustomerMapper userCustomerMapper;
    @Autowired
    private UserCustomerLogService userCustomerLogService;

    private Logger logger = LoggerFactory.getLogger(UserCustomerService.class);

    /**
     * 添加会员
     *
     * @param paramModelMap 会员信息
     * @return 添加结果信息
     * @throws Exception
     */
    @Transactional
    public String saveCustomer(Map<String, Object> paramModelMap) throws Exception {
        if (Constant.toEmpty(paramModelMap.get("customerPN"))) {
            Map<String, Object> map = new HashMap<>();
            map.put("customerPN", paramModelMap.get("customerPN"));
            List<Map<String, Object>> customerList = customerMapper.findMore(map);
            if (customerList.size() > 0) {
                return "4003";
            } else {
                if (Constant.toEmpty(paramModelMap.get("nickname"))) {
                    paramModelMap.put("nickname", Base64.getBase64((String) paramModelMap.get("nickname")));
                }
                customerMapper.saveSingle(paramModelMap);
                return "0";
            }
        } else {
            return "4001";
        }
    }

    @Transactional
    public String saveAndGetCustomer(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("customerPN", paramModelMap.get("customerPN"));
        List<Map<String, Object>> customerList = customerMapper.findMore(map);
        if (customerList.size() > 0) {
            return String.valueOf(customerList.get(0).get("id"));
        } else {
            if (Constant.toEmpty(paramModelMap.get("nickname"))) {
                paramModelMap.put("nickname", Base64.getBase64((String) paramModelMap.get("nickname")));
            }
            customerMapper.saveSingle(paramModelMap);
            return String.valueOf(paramModelMap.get("id"));
        }
    }

    /**
     * 查询会员数据
     *
     * @param paramModelMap 查询条件
     * @return 会员信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findCustomerList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> customerList = customerMapper.findMore(paramModelMap);
        for (Map<String, Object> customer : customerList) {
            if (customer.get("nickname") != null) {
                customer.put("nickname", Base64.getFromBase64((String) customer.get("nickname")));
            }
        }
        PageInfo<Map<String, Object>> customerInfo = new PageInfo<>(customerList);
        return customerInfo;
    }

    /**
     * 根据会员手机和车牌号查询对应会员及车辆
     *
     * @param paramModelMap 查询条件
     * @return 对应详情
     * @throws Exception
     */
    public Map<String, Object> findCustomerAndCar(Map<String, Object> paramModelMap) throws Exception {
        return customerMapper.findCustomerAndCar(paramModelMap);
    }

    /**
     * 查询关注了维修厂的会员数据
     *
     * @param paramModelMap 查询条件
     * @return 会员信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findShopCustomer(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> customerList = customerMapper.findShopCustomer(paramModelMap);
        for (Map<String, Object> customer : customerList) {
            if (customer.get("nickname") != null) {
                customer.put("nickname", Base64.getFromBase64((String) customer.get("nickname")));
            }
        }
        PageInfo<Map<String, Object>> customerInfo = new PageInfo<>(customerList);
        return customerInfo;
    }

    /**
     * @param request 验证token是否有效
     * @return
     * @throws ParseException
     */
    public Map<String, String> validationToken(HttpServletRequest request) throws ParseException {
        String token = request.getParameter(CommonField.TOKEN);
        logger.info("验证token={}>>>>>>", token);
        String mobileNumber = request.getParameter(CommonField.MOBILE_NUMBER);
        if (token != null && !"".equals(token)) {
            // 根据token获取用户信息
            Map<String, Object> customer = customerMapper.selectByTokenAndMobileNo(mobileNumber, token);
            if (customer != null) {
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tg = String.valueOf(customer.get("tokenaging"));
                Date tokenAging = time.parse(tg);
                token = String.valueOf(customer.get("token"));
                String status = String.valueOf(customer.get("status"));
                logger.info("用户状态={}，token失效时间={}", status, tg);
                // 用户状态正常
                if (status.equals("1")) {
                    if (tokenAging != null && token != null) {
                        if (DateUtils.booleanToken(tokenAging, new Date())) {
                            logger.info("token验证通过>>>>>>");
                            Map<String, String> hashMap = new HashMap<>();
                            hashMap.put(CommonField.TOKEN, token);
                            hashMap.put(CommonField.MOBILE_NUMBER, String.valueOf(customer.get("customerPN")));
                            String userId =  String.valueOf(customer.get("id"));
                            hashMap.put("id", userId);
                            hashMap.put("flag", "1");
                            try {
                                Map<String,Object> tokenUp = new HashMap<>();
                                Date tokenTime = DateUtils.getTokenTime();
                                tokenUp.put("tokenaging", DateUtils.formatDate(tokenTime));
                                tokenUp.put("id", userId);
                                userCustomerMapper.updateModel(tokenUp);
                            } catch (Exception e) {
                                logger.error("更新token时间失败",e);
                            }
                            return hashMap;
                        }
                        logger.info("token已失效>>>>>>");
                        return null;
                    }
                }
                return null;
            }
        }
        return null;
    }

    public Map<String, String> validationToken(String token) throws ParseException {
        if (token != null && !"".equals(token)) {
            Map<String, Object> customer = customerMapper.selectByTokenAndMobileNo("", token);
            if (customer != null) {
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tg = String.valueOf(customer.get("tokenaging"));
                Date tokenAging = time.parse(tg);
                token = String.valueOf(customer.get("token"));
                if (tokenAging != null && token != null) {
                    if (DateUtils.booleanToken(tokenAging, new Date())) {
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put(CommonField.TOKEN, token);
                        hashMap.put(CommonField.MOBILE_NUMBER, String.valueOf(customer.get("customerPN")));
                        hashMap.put("id", String.valueOf(customer.get("id")));
                        hashMap.put("flag", "1");
                        return hashMap;
                    }
                    return null;
                }
                return null;
            }
        }
        return null;
    }

    public Map<String, Object> findUser(Map<String, Object> map) throws Exception {
        return customerMapper.findUser(map);
    }

    public Map<String, Object> findUserInfo(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> pmap = new HashMap<String, Object>();
        pmap.put("customerId", paramModelMap.get("id"));
        List<Map<String, Object>> list = carMapper.findMore(pmap);
        String a = "";
        for (Map<String, Object> m : list) {
            pmap.put("time1", m.get("timeBegin"));
            pmap.put("time2", m.get("timeEnd"));
            Map<String, Object> amap = carMapper.findHelpCount(pmap);
            if (amap == null) {
                continue;
            }
            String str = String.valueOf(amap.get("str")).equals("null") ? "" : String.valueOf(amap.get("str"));
            a = a.equals("") ? str : getNewStr(a, str);
        }
        Map<String, Object> map = customerMapper.findUserInfo(paramModelMap);
        if (map.get("userName") != null) {
            map.put("userName", Base64.getFromBase64(String.valueOf(map.get("userName"))));
        }
        Map<String, Object> countMap = customerMapper.selectCount(String.valueOf(paramModelMap.get("id")));
        if (map != null) {
            map.put("helpCount", a.equals("") || a.equals("null") ? 0 : a.split(",").length);
            if (countMap != null) {
                map.putAll(countMap);
            }
        }
        Map<String, Object> cmap = carMapper.findCarNumByUserId(Integer.valueOf(String.valueOf(paramModelMap.get("id"))));
        if (cmap != null) {
            map.putAll(cmap);
        } else {
            map.put("guaranteeCarCount", 0);
            map.put("amtCooperation", 0);
        }
        return map;
    }

    @Transactional
    public void updateModel(Map<String, Object> map) throws Exception {
    	
        //获取用户信息
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("id", map.get("id"));
        Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
        //用户冻结
    	reqMap.clear();
    	reqMap.put("customerId", customer.get("id"));
    	reqMap.put("customerPN", customer.get("customerPN"));
    	reqMap.put("source", customer.get("source"));
    	reqMap.put("createAt", customer.get("timeJoin"));
    	reqMap.put("currentStatus", customer.get("status"));
    	reqMap.put("optTime", DateUtils.formatDate(new Date()));
    	reqMap.put("optType", 13);
    	reqMap.put("optDesc", "用户冻结");
    	reqMap.put("recordTime", DateUtils.formatDate(new Date()));
    	userCustomerLogService.saveUserCustomerLog(reqMap);
    	
        customerMapper.updateModel(map);
    }

    public List<Map<String, Object>> selectRechargeList(Map<String, Object> rmap) throws Exception {
        List<Map<String, Object>> aList = getAmtShare(rmap);//用户保障期内所有的互助分摊
        List<String> month1 = aList.stream().map(a -> String.valueOf(a.get("month"))).collect(Collectors.toList());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list1 = customerMapper.selectRechargeList(rmap); // 查询支付记录（每月）
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list1 != null && list1.size() > 0) {
            // 循环每月
            for (Map<String, Object> m : list1) {
                String[] gc = String.valueOf(m.get("gc")).split(",");
                String[] gt = String.valueOf(m.get("gt")).split(",");
                String[] ga = String.valueOf(m.get("ga")).split(",");
                String[] gtype = String.valueOf(m.get("gtype")).split(",");
                String ft = String.valueOf(m.get("ft"));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("yearMonth", ft.split("-")[0] + "年" + ft.split("-")[1] + "月");
                List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
                BigDecimal b1 = new BigDecimal(0);
                BigDecimal b2 = new BigDecimal(0);
                for (int i = 0; i < gc.length; i++) {
                    Map<String, Object> mm = new HashMap<String, Object>();
                    b1 = b1.add(gtype[i].equals("1") || gtype[i].equals("2") ? new BigDecimal(ga[i]) : new BigDecimal(0));
                    b2 = b2.add(gtype[i].equals("1") || gtype[i].equals("2") ? new BigDecimal(0) : new BigDecimal(ga[i]));
                    mm.put("content", gc[i].replaceAll("\\|", ","));
                    mm.put("time", time.parse(gt[i]).getTime());
                    mm.put("amt", gtype[i].equals("1") || gtype[i].equals("2") ? "+" + ga[i] : "-" + ga[i]);
                    mm.put("type", gtype[i]);
                    mList.add(mm);
                }

                // 按时间排序
                for (int i = 0; i < mList.size(); i++) {
                    for (int j = i + 1; j < mList.size(); j++) {
                        Map<String, Object> m1 = mList.get(i);
                        Map<String, Object> m2 = mList.get(j);
                        Double t1 = Double.valueOf(String.valueOf(m1.get("time")));
                        Double t2 = Double.valueOf(String.valueOf(m2.get("time")));
                        if (t2 > t1) {
                            mList.set(i, m2);
                            mList.set(j, m1);
                        }
                    }
                }
                String zhichu = "";
                if (month1.contains(ft)) {
                    month1.remove(ft);
                }
                for (Map<String, Object> mm : aList) {
                    String month = String.valueOf(mm.get("month"));
                    String str = String.valueOf(mm.get("str"));
                    String amtShare = String.valueOf(mm.get("amtShare"));
                    BigDecimal amt = new BigDecimal(amtShare);
                    try {
                        if (ft.equals(month)) {
                            b2 = b2.add(amt);
                            zhichu = amtShare;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (month.equals(ft)) {
                        Map<String, Object> m1 = new HashMap<String, Object>();
                        m1.put("content", "本月完成" + str.split(",").length + "起救助事件,您互助金支出" + amtShare + "元");
                        m1.put("time", "");
                        m1.put("amt", "-" + amtShare);
                        m1.put("type", 4);
                        mList.add(0, m1);
                    }

                }
                map.put("income", b1);
                map.put("outcome", zhichu);
                map.put("dataList", mList);
                list.add(map);
            }
            //没有充值记录单有发现互助理赔的情况
            if (month1 != null && month1.size() > 0) {
                month1.forEach(a -> {
                    final String[] amtSharea = {""};
                    List<Map<String, Object>> mList = new ArrayList<>();
                    aList.forEach(b -> {
                        String month = String.valueOf(b.get("month"));
                        String str = String.valueOf(b.get("str"));
                        String amtShare = String.valueOf(b.get("amtShare"));
                        if (a.equals(month)) {
                            amtSharea[0] = amtShare;
                            Map<String, Object> m1 = new HashMap<>();
                            m1.put("content", "本月完成" + str.split(",").length + "起救助事件,您互助金支出" + amtShare + "元");
                            m1.put("time", "");
                            m1.put("amt", "-" + amtShare);
                            m1.put("type", 4);
                            mList.add(0, m1);
                        }
                    });
                    list.add(MapUtil.build().put("yearMonth", a.split("-")[0] + "年" + a.split("-")[1] + "月").
                            put("income", 0).put("outcome", amtSharea[0]).put("dataList", mList).over());
                });
                List<Map<String, Object>> collect = list.stream().sorted((a, b) -> a.get("yearMonth").toString().compareTo(b.get("yearMonth").toString())).collect(Collectors.toList());
                Collections.reverse(collect);
                return collect;
            }
        }
        return list;
    }

    public List<Map<String, Object>> getAmtShare(Map<String, Object> rmap) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> l = carMapper.findMore(rmap); //用户所有车辆信息以及最后一次互助及充值情况
        BigDecimal total = new BigDecimal(0);
        int count = 0;
        // 循环车辆
        for (Map<String, Object> m : l) {
            rmap.put("time1", m.get("timeBegin"));
            rmap.put("time2", null);
            if (null != m.get("timeSignout")
                    && !"1111-11-11 11:11:11".equals(m.get("timeSignout").toString())
                    && !"1111-11-11 11:11:11.0".equals(m.get("timeSignout").toString()))
                rmap.put("time2", m.get("timeSignout"));
            List<Map<String, Object>> amap = carMapper.findCarAmtShareGroupByMonth(rmap);//用户保障期内所有的互助分摊
            if (amap != null && amap.size() > 0) {
                String licensePlateNumber = m.get("licensePlateNumber").toString();
                for (Map<String, Object> mm : amap) {
                    String month = String.valueOf(mm.get("month"));//互助事件完成月
                    String str = String.valueOf(mm.get("str"));//互助事件号
                    BigDecimal a = new BigDecimal(String.valueOf(mm.get("amtShare")).equals("null") ? "0" : String.valueOf(mm.get("amtShare")));// 互助分摊
                    int flag = 0;
                    if (list.size() > 0) {
                        for (Map<String, Object> mmm : list) {
                            String month1 = String.valueOf(mmm.get("month"));
                            if (month1.equals(month)) {
                                String str2 = String.valueOf(mmm.get("str"));
                                BigDecimal aa = new BigDecimal(String.valueOf(mmm.get("amtShare")).equals("null") ? "0" : String.valueOf(mmm.get("amtShare")));
                                mmm.put("info", String.valueOf(mmm.get("info")) + "," + licensePlateNumber + "互助分摊" + a);
                                mmm.put("str", getNewStr(str2, str));
                                mmm.put("amtShare", aa.add(a));
                                flag = 1;
                                break;
                            }
                        }
                    }
                    if (flag == 0) {
                        Map<String, Object> m4 = new HashMap<String, Object>();
                        m4.put("str", str);
                        m4.put("month", month);
                        m4.put("amtShare", a);
                        m4.put("info", licensePlateNumber + "互助分摊" + a);
                        list.add(m4);
                    }
                }
            }
        }
        return list;
    }

    public String getNewStr(String a, String b) {
        String c = "";
        List<String> list11 = Arrays.asList(a.split(","));
        List<String> list22 = Arrays.asList(b.split(","));
        List<String> list1 = new ArrayList(list11);
        List<String> list2 = new ArrayList(list22);
        for (String s : list2) {
            if (list1.indexOf(s) == -1) {
                list1.add(s);
            }
        }
        for (String s : list1) {
            c += c.equals("") ? s : "," + s;
        }
        return c;
    }

    /**
     * 查询用户数量
     *
     * @throws Exception
     */
    public Map<String, Object> findCustomerCount(Map<String, Object> paramModelMap) throws Exception {
        return customerMapper.findCount(paramModelMap);
    }

    /**
     * 查询某个用户详情
     */
    public Map<String, Object> findCustomerDetail(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> customerDetail = customerMapper.findSingle(paramModelMap);
        //将昵称进行解密
        if (customerDetail.get("nickname") != null) {
            customerDetail.put("nickname", Base64.getFromBase64((String) customerDetail.get("nickname")));
        }
        //获取用户添加的全部车辆
        List<Map<String, Object>> totalCarList = carMapper.findCarOfCustomer(paramModelMap);
        StringBuffer carSb = new StringBuffer();
        if (totalCarList.size() > 0) {
            for (Map car : totalCarList) {
                carSb.append(car.get("licensePlateNumber")).append(",");
            }
            customerDetail.put("totalCar", carSb.toString().substring(0, carSb.toString().length() - 1));
        } else {
            customerDetail.put("totalCar", "");
        }
        //获取用户保障中的车辆
        paramModelMap.put("status", 20);
        List<Map<String, Object>> guaranteeCarList = carMapper.findCarOfCustomer(paramModelMap);
        if (guaranteeCarList.size() > 0) {
            carSb = new StringBuffer();
            for (Map car : guaranteeCarList) {
                carSb.append(car.get("licensePlateNumber")).append(",");
            }
            customerDetail.put("guaranteeCar", carSb.toString().substring(0, carSb.toString().length() - 1));
        } else {
            customerDetail.put("guaranteeCar", "");
        }
        return customerDetail;
    }

    /**
     * 根据用户的id，获取头像和昵称
     *
     * @param customerId
     * @return
     */
    @Transactional
    public Map<String, Object> getUserInfoById(Integer customerId) {
        Map<String, Object> result = customerMapper.getUserInfoById(customerId);
        if (result != null && Constant.toEmpty(result.get("nickname"))) {
            result.put("nickname", Base64.getFromBase64((String) result.get("nickname")));
        }
        String newFileName = null;
        if (result != null && Constant.toEmpty(result.get("headPortrait"))) {
            newFileName = (String) result.get("headPortrait");
        } else {
            if (result != null && Constant.toEmpty(result.get("portrait"))) {
                String portrait = (String) result.get("portrait");
                if (portrait.contains("thirdwx.qlogo.cn")) {
                    // 从微信下载放入阿里云，然后保存进数据库；查询阿里云返回链接返回
                    newFileName = downloadPortrait(portrait, customerId);
                } else {
                    newFileName = portrait;
                }
            }
        }

        if (newFileName != null) {
            try {
                String imgURL = UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, newFileName);
                result.put("imgURL", imgURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 返回用户邀请二维码
        try {
            String qrcode = middleCustomerQrcodeService.getQrcodeByCustomerId(customerId);
            if (null == qrcode)
                throw new RuntimeException("获取永久专属二维码失败！");
            String qrcodeURL = UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, qrcode);
            result.put("qrcode", qrcodeURL);
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException("获取永久专属二维码失败！");
        }

        return result;
    }

    /**
     * 从微信下载放入阿里云，然后保存进数据库；查询阿里云返回链接返回
     *
     * @param portrait
     */
    private String downloadPortrait(String portrait, Integer customerId) {
        try {
            HttpClient hClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(portrait);
            HttpResponse response = hClient.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();

                String uuid = UUID.randomUUID().toString().replace("-", "");
                String newFileName = uuid + ".jpg";
                // 上传至阿里云
                Boolean uploadResult = UploadFileUtil.saveImg(CommonField.POSTER_IMG_URL, newFileName, IOUtils.toByteArray(in));
                System.out.println("上传到阿里云的图片(" + (CommonField.POSTER_IMG_URL + newFileName) + ")结果为: " + uploadResult);
                if (uploadResult) {
                    // 更新数据库
                    customerMapper.updateHeadPortrait(newFileName, customerId);
                    return newFileName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


