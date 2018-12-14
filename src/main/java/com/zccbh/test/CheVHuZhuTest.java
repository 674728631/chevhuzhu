package com.zccbh.test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.zccbh.demand.controller.quartz.ObservationJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.controller.weChat.WeixinConstants;
import com.zccbh.demand.mapper.business.MaintenanceshopMapper;
import com.zccbh.demand.mapper.business.MiddleBusinessMaintenanceshopMapper;
import com.zccbh.demand.mapper.business.UserBusinessMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.merchants.CbhCarMapper;
import com.zccbh.demand.mapper.merchants.CbhEventReceivecarMapper;
import com.zccbh.demand.mapper.merchants.CbhUserBusinessMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.pojo.merchants.CbhCar;
import com.zccbh.demand.pojo.merchants.CbhUserBusiness;
import com.zccbh.demand.service.basic.DictionaryService;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.*;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.MD5Util;
import com.zccbh.util.qrcode.QRCodeUtil;
import com.zccbh.util.uploadImg.UploadFileUtil;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/zccbh/config/spring.xml")
public class CheVHuZhuTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Autowired
    private CbhEventReceivecarMapper applyMapper;

    @Autowired
    private CbhUserBusinessMapper businessMapper;
    @Autowired
    private WeiXinUtils weiXinUtils;
    @Autowired
    private CbhCarMapper cbhCarMapper;
    @Autowired
    private FoundationMapper foundationMapper;

    @Test
    public void test1() throws Exception {
        Integer[] integer = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        WebMvcConfig bean = SpringContextHolder.getBean(WebMvcConfig.class);
////        BaseInterceptor bean = SpringContextHolder.getBean(BaseInterceptor.class);
//        System.out.println("bean = " + bean.getClass());
//        Map paramModelMap = new HashMap();
//        Map<String, String> result1 = new HashMap<>();
//        result1.put("openid", "1");
//        result1.put("licensePlateNumber", "2");
//        result1.put("model", "3");
//        result1.put("eventNo", "4");
//        result1.put("address", "5");
//        result1.put("tel", "13881352548");
//        weiXinUtils.sendTemplate(3, result1);

        //paramModelMap.put("idArr","1,2,3,4,5,6,7,8,9");
//        List<Map<String, Object>> list = menuService.findMenuList(paramModelMap);
//        for (Map map: list) {
//            System.out.println(map);
//        }
//        String poster = UploadFileUtil.getImgURL("poster/", "20180309170002.jpg");
//        System.out.println("poster = " + poster);
//        String maintenanceShopImg = CommonField.getMaintenanceShopImg(2, "22.jpg");
//        System.out.println("maintenanceShopImg = " + maintenanceShopImg);
//        String imgs="11.jpg_22.jpg_33.jpg_44.jpg_55.jpg";
//        List<String> carImgList = CommonField.getCarImgList(imgs);
//        for(int i = 0; i < carImgList.size(); i++) {
//            String s = carImgList.get(i);
//            System.out.println("s = " + s);
//
//        }
//        String s="maintenanceshopImg0";
//        StringBuffer buffer = new StringBuffer();
//        if (Constant.toEmpty(buffer)) {
//            System.out.println("s = " + s.contains("maintenanceshopImg"));
//        }
//        System.out.println("s1 = " + Constant.toEmpty(buffer));
//        String assert1 = CommonField.getEventImg(1, "84fb5172eea748278c105d08372c6b08.jpg");
//        String maintenance = CommonField.getEventImg(2, "b588c2d987c146f1aeccdc41f5058349.jpg");
//
//        System.out.println("assert = " + assert1);
//        System.out.println("maintenance = " + maintenance);
//        String substring = s.substring(0, (s.length()-1));
//        System.out.println("s = " + substring);
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(s);
//        System.out.println("buffer = " + buffer.toString());
//        businessService.servicePoints("20180316182226877583",0.5,"+");
//        Map<String, String> result1 = new HashMap<>();
//        result1.put("openid", "oydTd0tYJO49DVY1mzSBT_aJ2_II");
//        result1.put("number", "11");
//        result1.put("names", "小花");
//        result1.put("totalAmount", "1000");
//        result1.put("money", "1");
//        weiXinUtils.sendTemplate(5, result1);
//        String md5Code = MD5Util.getMD5Code("123456");
//        System.out.println("md5Code = " + md5Code);
//        Map<String, String> stringStringMap = weiXinUtils.getNickname("oydTd0tYJO49DVY1mzSBT_aJ2_II");
//        System.out.println("stringStringMap = " + stringStringMap);
//        Student student = Student.getStudent();
//        System.out.println("student = " + student.getClass());


    }

    @Test
    public void test2() throws Exception {
        String str1 = "hello";
        String str2 = "he" + new String("llo");
        System.out.println(str1 == str2);
        System.out.println(str1.equals(str2));
        String a = Constant.toOr("0.1", Constant.toReadPro("orKey"), "encrypt");
        System.out.println(a);

    }

    @Test
    public void test3() throws Exception {
        String a = CommonField.getCarUrl("{\"zh\":\"86d1b126d09c4b05bbee95f839c65bb8.jpg\",\"yh\":\"7bc218e0e9ae4cb89ade8d0aa7d7cc84.jpg\",\"zq\":\"394404f242994256bd58d9e7676ae739.jpg\",\"yq\":\"62632f46895045f4b329f01cd681a3cb.jpg\"}");
        System.out.println(a);
    }

    @Autowired
    private EventService eventService;

    @Test
    public void test4() throws Exception {
        String accessToken = weiXinUtils.getAccessToken();
        String url = WeixinConstants.QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
        String bodys = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + 123 + "}}}";
        HttpResponse response = HttpUtils.sendPost(url, bodys);
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(content);
    }

    @Test
    public void test5() throws Exception {
        //new一个URL对象
//        URL url = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQH68DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyQ3lBV1pmOWtmdGkxMDAwMHcwM3oAAgQUxL1aAwQAAAAA");
//        //打开链接
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        //设置请求方式为"GET"
//        conn.setRequestMethod("GET");
//        //超时响应时间为5秒
//        conn.setConnectTimeout(5 * 1000);
//        //通过输入流获取图片数据
//        InputStream inStream = conn.getInputStream();
//        BufferedImage image = ImageIO.read(inStream);
        File imagePic = new File("C:/Users/lenovo/Desktop/111.png");
//        File imagePic = new File("C:/Users/lenovo/Desktop/222.jpg");
        BufferedImage image = ImageIO.read(imagePic);
        Graphics2D gs = image.createGraphics();
        File logoPic = new File("C:/Users/lenovo/Desktop/333.jpg");
        BufferedImage logo = ImageIO.read(logoPic);
        /**
         * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
         */
        int widthLogo = logo.getWidth(null) > image.getWidth() * 3 / 10 ? (image.getWidth() * 3 / 10) : logo.getWidth(null);
        int heightLogo = logo.getHeight(null) > image.getHeight() * 3 / 10 ? (image.getHeight() * 3 / 10) : logo.getWidth(null);
        /**
         * logo放在中心
         */
        int x = (image.getWidth() - widthLogo) / 2;
        int y = (image.getHeight() - heightLogo) / 2;
        //开始绘制图片
        gs.drawImage(logo, x, y, widthLogo, heightLogo, null);
        gs.dispose();
        image.flush();
        ImageIO.write(image, "png", new FileOutputStream(new File("E:/test.png")));
    }

    @Test
    public void test6() throws Exception {
        Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //1、生成二维码
        BitMatrix encode = new MultiFormatWriter().encode("http://weixin.qq.com/q/02C6nKZI9kfti10000w03A", BarcodeFormat.QR_CODE, 430, 430, his);

        //2、获取二维码宽高
        int codeWidth = encode.getWidth();
        int codeHeight = encode.getHeight();

        //3、将二维码放入缓冲流
        BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < codeWidth; i++) {
            for (int j = 0; j < codeHeight; j++) {
                //4、循环将二维码内容定入图片
                image.setRGB(i, j, encode.get(i, j) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        Graphics2D gs = image.createGraphics();
        //String path = request.getContextPath();
        //File logoPic = new File(path+"/cite/images/qrcodeLogo.png");
        File logoPic = new File("C:/Users/lenovo/Desktop/qrcodeLogo.png");
        BufferedImage logo = ImageIO.read(logoPic);
        /**
         * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
         */
        int widthLogo = logo.getWidth(null) > image.getWidth() * 15 / 100 ? (image.getWidth() * 15 / 100) : logo.getWidth(null);
        int heightLogo = logo.getHeight(null) > image.getHeight() * 15 / 100 ? (image.getHeight() * 15 / 100) : logo.getWidth(null);
        /**
         * logo放在中心
         */
        int x = (image.getWidth() - widthLogo) / 2;
        int y = (image.getHeight() - heightLogo) / 2;
        //开始绘制图片
        gs.drawImage(logo, x, y, widthLogo, heightLogo, null);
        gs.dispose();
        image.flush();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ImageIO.write(image,"png",out);
//        byte[] content = out.toByteArray();
//        String uuid = UUID.randomUUID().toString().replace("-","");
//
//        //新的图片文件名 = 获取时间戳+"."+图片扩展名
//        String newFileName = uuid + ".png";
//        UploadFileUtil.saveImg("maintenanceshop/qrcode/",newFileName,content);
        ImageIO.write(image, "png", new File("E:/test.png"));
        //QRCodeUtil.QRCodeCreate("http://weixin.qq.com/q/022CnwYu9kfti100000031","E:/test.png",18,"C:/Users/lenovo/Desktop/333.jpg");
    }

    @Autowired
    private CarMapper carMapper;

    @Test
    public void test7() throws Exception {
        Map map = new HashMap();
        BigDecimal amtAssert = new BigDecimal("1000");
        //获取剩余理赔额度
        BigDecimal amtCompensation = new BigDecimal(100);

        BigDecimal amtPay = amtAssert.divide(new BigDecimal(10), 2, RoundingMode.HALF_UP);
        BigDecimal amtCooperation = amtAssert;//互助金额
        int answer = amtCompensation.compareTo(amtAssert);
        if (answer < 1) {
            amtPay = amtCompensation.divide(new BigDecimal(10), 2, RoundingMode.HALF_UP).add(amtAssert.subtract(amtCompensation));
            amtCooperation = amtCompensation;
        }
        //获取保障中的车辆数量
        map.put("status", 20);
        int carCount = 1000000000;
        //计算分摊金额
        BigDecimal amtShare = amtCooperation.divide(new BigDecimal(carCount), 2, RoundingMode.HALF_UP);
        BigDecimal amtCBH = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        int answer2 = amtShare.compareTo(new BigDecimal(0.1));
        if (answer2 == 1) {
            amtShare = new BigDecimal(0.1).setScale(2, RoundingMode.HALF_UP);
            amtCBH = amtCooperation.subtract(amtShare.multiply(new BigDecimal(carCount))).setScale(2, RoundingMode.HALF_UP);
            amtCooperation = amtShare.multiply(new BigDecimal(carCount)).setScale(2, RoundingMode.HALF_UP);
        }
        int answer3 = amtShare.compareTo(new BigDecimal(0.01));
        if (answer3 != 1) {
            amtShare = new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP);
            amtCBH = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        }
        System.out.println("amtPay           " + amtPay);
        System.out.println("amtCooperation        " + amtCooperation);
        System.out.println("amtCBH       " + amtCBH);
        System.out.println("amtShare              " + amtShare);
    }

    @Autowired
    private UserCustomerMapper customerMapper;

    @Test
    public void test8() throws Exception {
        List<Map<String, Object>> list = customerMapper.findMore(new HashMap<>());
        System.out.println("111111111111111111111111111111111111111111111111111111111");
        for (Map a : list){
            Map map = new HashMap();
            map.put("id",a.get("id"));
            if(a.get("shopName")!=null){
                map.put("source",a.get("shopName"));
            }else {
                map.put("source","自然用户");
            }
            customerMapper.updateModel(map);
        }
    }

    @Autowired
    private MaintenanceshopMapper maintenanceshopMapper;

    @Test
    public void test9() throws Exception {
        Map map = new HashMap();
        List<Map<String, Object>> shopList = maintenanceshopMapper.countAttentionAndRegister(map);
        System.out.println(shopList);
    }

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test10() throws Exception {
        Map paramModelMap = new HashMap<>();
        paramModelMap.put("name", "杨峰");
        paramModelMap.put("tel", "18908044150");
        paramModelMap.put("address", "成都东方希望中心");
        paramModelMap.put("type", 20);
        //保存商家
        maintenanceshopMapper.saveSingle(paramModelMap);
        //创建商家二维码
        redisUtil.delect("accessToken2");
        String accessToken = weiXinUtils.getAccessToken();
        String url = WeixinConstants.QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
        String bodys = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + paramModelMap.get("id") + "}}}";
        HttpResponse response = HttpUtils.sendPost(url, bodys);
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        String qrcode_url = JSONObject.fromObject(content).getString("url");
        String qrcodeName = QRCodeUtil.saveQrcode(qrcode_url, "D:/IdeaProjects/chevhuzhu/trunk/04 code/chevhuzhu_a/src/main/webapp/cite/images/qrcodeLogo.png", "maintenanceshop/qrcode/");
        //保存商家二维码
        Map map = new HashMap();
        map.put("id", paramModelMap.get("id"));
        map.put("qrcode", qrcodeName);
        maintenanceshopMapper.updateModel(map);
    }

    @Test
    public void test11() throws Exception {
        Integer carId = 284;
        CarService carService = SpringContextHolder.getBean(CarService.class);
        Map<String, Object> carInfo = carService.findCarById(carId);
        String jobName = "observationJob_" + carId;
        Map payAmount = carService.getPayAmount(carId.toString());
        //修改车辆保障时间
        BigDecimal a = new BigDecimal(payAmount.get("amount").toString());
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = time.format(new Date());
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Map map = new HashMap();
        map.put("id", carId);
        map.put("timeBegin", carInfo.get("timeBegin") == null ? date : null);
        map.put("timeEnd", a.compareTo(new BigDecimal("99")) == 0 ? time.format(calendar.getTime()) : "");
        map.put("status", 20);
        carService.updateCar(map);
        //删除定时任务
        SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
        Scheduler sche = schedulerFactory.getScheduler();
        QuartzUtils.removeJob(sche, jobName);
    }

    @Test
    public void test12() throws Exception {
//        List<CbhUserBusiness> cbhUserBusinesses = businessMapper.selectAll();
        CbhUserBusiness a = businessMapper.selectByPrimaryKey(2);
//        cbhUserBusinesses.stream().forEach(a-> {
        String androidDeviceId = a.getAndroidDeviceId();
        String iosDeviceId = a.getIosDeviceId();
        String content = "渠道拉新分单活动已经结束了，平台加推保险销售“百万计划”利器。详询15828053709 （宋经理），有你的支持我们会更强大！";
//            CbhMessage message = new CbhMessage();
//            message.setBusinessId(a.getId());
//            message.setType(1);
//            message.setTitle("系统消息");
//            message.setContent(content);
//            messageMapper.insert(message);
//            移动推送
        if (Constant.toEmpty(androidDeviceId) || Constant.toEmpty(iosDeviceId)) {
            Map<String, Object> parameterHashMap = new HashMap<>();
            parameterHashMap.put("orderNo", "https://www.baidu.com/");
            parameterHashMap.put("orderStatus", "");
            parameterHashMap.put("type", 100);
            try {
                if (Constant.toEmpty(androidDeviceId)) {
                    SmsDemo.mobilePushMessage(20, 1, androidDeviceId, content, parameterHashMap);
                }
                if (Constant.toEmpty(iosDeviceId)) {
                    SmsDemo.mobilePushMessage(10, 1, iosDeviceId, content, parameterHashMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        });
    }

    @Test
    public void test13() throws Exception {
//        SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
//        Scheduler scheduler = schedulerFactory.getScheduler();
//        boolean started = scheduler.isStarted();
//        System.out.println("started = " + started);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(new Date(1527577740000L));
        System.out.println(date);
//        scheduler.isStarted()
//        String string=null;
//        for(int i = 0; i < 6; i++) {
//            String uuid = UUIDCreator.getUUID()+".jpg";
//            System.out.println("uuid = " + uuid);
//            string+=uuid+"_";
//            System.out.println("string = " + string);
//        }
//        "1264A5D775A04521947028ABB2BC7A58.jpg_209E87174EB34FA491714F6C021F2B34.jpg_C6BAB4A9BE7841CBB59A4F7521232D5C.jpg_866ABD2345984A7B9E3D7D1C90E59D64.jpg_3F5CE5BD0AD84DFFB6EACE0AC58AD116.jpg_FA51823DAAD54F07A5E2A75424257777.jpg"
    }

    @Test
    public void test14() throws Exception {
        SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
        Scheduler scheduler = schedulerFactory.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        if (jobKeys != null && jobKeys.size() > 0) {
            jobKeys.forEach(a -> {
                try {
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(a);
                    triggers.forEach(b -> {
                        String[] jobName = a.getName().split("_");
                        Date previousFireTime = b.getPreviousFireTime();
                        if (jobName[0].equals("observationJob") && previousFireTime != null) {
                            String carId = jobName[1];
                            quartz(Integer.valueOf(carId));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Test
    public void test151() throws Exception {
        List<String> stringList = Arrays.asList("oydTd0knxuFBrJyLBeE5EcVm2eOc", "oydTd0v3qkCnPYLRPSfG2UbE7puQ", "oydTd0jb9BEnAM6BMnwJAHuJl0Bg",
                "oydTd0rQ0gKY5FB7C6AcSVFPyGsg", "oydTd0nzIpp7Rt6G_GjVx3kOAPn4", "oydTd0tobVhJfGKWhk8nzS-SESd4",
                "oydTd0iA-ifLfEvQCROwXUb0l6Ww", "oydTd0rsmOu07PDw31xTdK9GsYiI", "oydTd0hWpbW3yvwWB4s9OWX-YVTY",
                "oydTd0tJE2_U25EU8GnKHXyYKRzs", "oydTd0ow8x1_1B2U7F-sssfdbn5U", "oydTd0swsf60t7v8zr9A-7H96UQs",
                "oydTd0h-6oU1HD7wadrIl8H8u6AQ", "oydTd0u73mySCkM2AVkjxiHbdrm8", "oydTd0rhkowJS2mR_YSNOp2Lyxsc",
                "oydTd0ibpWV_hGbyG-cmStXTiJdw", "oydTd0oHUNXlNOHIjWkRmv8nDfsU", "oydTd0oncPSmganGAEEdtqYyoPNM");
        stringList.forEach(a -> {
            try {
                Map<String, String> nickname = weiXinUtils.getNickname(a);
                System.out.println("nickname = " + nickname);
            } catch (Exception e) {

            }
        });

    }

    public static void quartz(Integer carId) {
        try {
            CarService carService = SpringContextHolder.getBean(CarService.class);
            Map<String, Object> carInfo = carService.findCarById(carId);
            String jobName = "observationJob_" + carId;
            if (carInfo != null && !carInfo.isEmpty()) {
                Map map = new HashMap();
                //修改车辆保障时间
                Map<String, Object> carMap = carService.findCarById(carId);
                if (carMap.get("timeBegin") != null && DateUtils.getYearDate((Date) carMap.get("timeBegin")).compareTo(new Date()) == 1) {
                    map.put("timeEnd", "2".equals(carMap.get("typeGuarantee").toString()) ? DateUtils.getYearDate((Date) carMap.get("timeBegin")) : "");
                } else {
                    map.put("timeBegin", DateUtils.getDateTime());
                    map.put("timeEnd", "2".equals(carMap.get("typeGuarantee").toString()) ? DateUtils.getYearDate(DateUtils.getDateTime()) : "");
                }
                map.put("id", carId);
                map.put("status", 20);
                carService.updateCar(map);
            }
            //删除定时任务
            SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
            Scheduler sche = schedulerFactory.getScheduler();
            QuartzUtils.removeJob(sche, jobName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test16() throws Exception {
        Integer[] objects = new Integer[]{6, 18, 15, 10, 11, 13, 5, 8, 1, 20, 4, 2, 14, 12, 7, 9, 19, 17, 3, 16};
        int low = 0;
        int high = objects.length - 1;
        Calendar instance = Calendar.getInstance();
        long timeInMillis = instance.getTimeInMillis();
        System.out.println("timeInMillis = " + timeInMillis);
        while (low < high) {
            for (int i = low; i < high; i++) {
                for (int j = low; j < high - 1; j++) {
                    if (objects[j] > objects[j + 1]) {
                        int temp = objects[j];
                        objects[j] = objects[j + 1];
                        objects[j + 1] = temp;
                    }
                }
            }
            high--;
            for (int i = high + 1; i > low; i--) {
                for (int j = high + 1; j > low + 1; j--) {
                    if (objects[j] < objects[j - 1]) {
                        int temp = objects[j];
                        objects[j] = objects[j - 1];
                        objects[j - 1] = temp;
                    }
                }
                low++;
            }
        }
        Calendar instance1 = Calendar.getInstance();
        long timeInMillis1 = instance1.getTimeInMillis();
        System.out.println("timeInMillis1 = " + timeInMillis1);
        System.out.println(timeInMillis1 - timeInMillis);
        System.out.println("objects = " + Arrays.toString(objects));

    }

    @Test
    public void test171() throws Exception {
        Integer[] objects = new Integer[]{6, 18, 15, 10, 11, 13, 5, 8, 1, 20, 4, 2, 14, 12, 7, 9, 19, 17, 3, 16};

        Calendar instance = Calendar.getInstance();
        long timeInMillis = instance.getTimeInMillis();
        System.out.println("timeInMillis = " + timeInMillis);
        for (int i = 0; i < objects.length - 1; i++) {
            for (int j = 0; j < objects.length - 1 - i; j++) {
                if (objects[j] > objects[j + 1]) {
                    int temp = objects[j];
                    objects[j] = objects[j + 1];
                    objects[j + 1] = temp;
                }
            }
        }
        Calendar instance1 = Calendar.getInstance();
        long timeInMillis1 = instance1.getTimeInMillis();
        System.out.println("timeInMillis1 = " + timeInMillis1);
        System.out.println(timeInMillis1 - timeInMillis);
        System.out.println("objects = " + Arrays.toString(objects));

    }

    @Autowired
    private UserBusinessMapper businessMapper2;
    @Autowired
    private MiddleBusinessMaintenanceshopMapper middleBusinessMaintenanceshopMapper;

    @Test
    public void test15() throws Exception {
        Map<String, Object> map2 = new HashMap<>();
        map2.put("businessUN", "13018212356");
        map2.put("businessPN", "13018212356");
        map2.put("typeUser", 1);
        map2.put("businessPW", MD5Util.getMD5Code("212356"));
        businessMapper2.saveSingle(map2);
        Map<String, Object> map = new HashMap<>();
        map.put("businessId", map2.get("id"));
        map.put("maintenanceshopId", 137);
        middleBusinessMaintenanceshopMapper.saveSingle(map);
    }

    @Autowired
    private CarService carService;

    @Test
    public void test17() throws Exception {
        Map map = new HashMap();
        map.put("addAmt", 7.8);
        map.put("type", 1);
        map.put("licensePlateNumber", "川A19GF7");
        map.put("practitioner", "梁杰");
        carService.updateAmt(map);
    }

    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    private DictionaryService dictionaryService;

    @Test
    public void test18() throws Exception {
        //创建定时任务，指定时间后进入保障期
        Integer carId = 1123;
        Map<String, Object> carInfo = carService.findCarById(carId);

        Map<String, Object> map = new HashMap<>();
        map.put("jobName", "observationJob_" + carId);
        map.put("carId", carId);
        map.put("typeGuarantee", carInfo.get("typeGuarantee"));
        map.put("openid", carInfo.get("openId"));
        map.put("content", "爱车" + carInfo.get("licensePlateNumber") + "加入车V互助成功，您爱车正享受互助保障中。");
        map.put("keyword1", "审核通过");
        map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
        map.put("url", "http://www.chevhuzhu.com/hfive/view/rule_photo2.html");
        Scheduler sche = schedulerFactory.getScheduler();
        QuartzUtils.removeJob(sche, map.get("jobName").toString());
        //从数据库查询定时时间
        Map dicMap = new HashMap();
        dicMap.put("type", "observationTime");
        Map dictionary = dictionaryService.findSingle(dicMap);
        //开启定时器
        Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));
        String cron = dateMap.get("second") + " " + dateMap.get("minute") + " " + dateMap.get("hour") + " " + dateMap.get("day") + " " + dateMap.get("month") + " ? *";
        QuartzUtils.addJob(sche, map.get("jobName").toString(), ObservationJob.class, map, cron);

        //计算观察期结束时间
        Long imestamp = new Date().getTime();
        imestamp = imestamp + Long.valueOf(dictionary.get("value").toString());
        Date date1 = new Date(imestamp);
        String observationEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
        //更新车辆状态
        map.clear();
        map.put("id", carId);
        map.put("status", 13);
        map.put("observationEndTime", observationEndTime);
        map.put("payTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        carService.updateCar(map);
        //统计中观察期车数量加一
        eventService.updateDayNumber("observationNum", 1);
        //微信通知用户 车辆已进入观察期
        try {
            BigDecimal day = new BigDecimal(dictionary.get("value").toString()).divide(new BigDecimal("86400000"), 0, RoundingMode.HALF_UP);
            Map<String, String> rmap = new HashMap<String, String>();
            rmap.put("openid", String.valueOf(carInfo.get("openId")));
            rmap.put("licensePlateNumber", String.valueOf(carInfo.get("licensePlateNumber")));
            rmap.put("day", String.valueOf(day));
            weiXinUtils.sendTemplate(11, rmap);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test19() throws Exception {
        redisUtil.delect("accessToken2");
    }
    @Test
    public void test20 () throws Exception {
        SchedulerFactoryBean schedulerFactory = SpringContextHolder.getBean(SchedulerFactoryBean.class);
        Scheduler scheduler = schedulerFactory.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        if (jobKeys != null && jobKeys.size() > 0) {
            jobKeys.forEach(a -> {
                try {
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(a);
                    triggers.forEach(b -> {
                        String[] jobName = a.getName().split("_");
                        if (jobName[0].equals("observationJob")) {
                            String carId = jobName[1];
                            CbhCar cbhCar = cbhCarMapper.selectByPrimaryKey(Integer.valueOf(carId));
                            if (null == cbhCar) {
                                System.err.println("cbhCar = " + "observationJob_" + carId);
                                QuartzUtils.removeJob(scheduler, "observationJob_" + carId);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Test
    public void test21() throws Exception {
        Foundation foundation =  foundationMapper.findEntitySingle(new HashMap<>());
        Map<String,Object> upfMap = new HashMap<>();
        upfMap.put("payNum", foundation.getPayNum()+1);
        upfMap.put("versions", foundation.getVersions());
        Thread.sleep(1000*5);
        Integer integer = foundationMapper.updateData(upfMap);
        if (integer==0)
            test21();
        else
        System.out.println("integer = 修改成功");
        return;

    }
      
}