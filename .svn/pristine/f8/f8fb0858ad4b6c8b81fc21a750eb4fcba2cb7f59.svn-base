package com.zccbh.util.base;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.uploadImg.UploadFileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommonField {
    //返回状态
    public static String STATUS="status";
    //返回错误信息
    public static String RESULTMESSAGE="resultMessage";

    public static final String ORDER_NUMBER_CANNOT_EMPTY ="订单号不能为空";
    public static final String MERCHANT_CODE_CANNOT_EMPTY ="商户编码不能为空";
    public static final String MERCHANT_CODE_INEXISTENCE ="商户编码不存在,请仔细核对一下!";
    public static final String TOKEY_LOSE_EFFICACY ="token失效";
    public static final String SORRY_THERE_IS_NO_ORDER ="对不起,没有这个订单!";
    public static final String MUST_NOT_BE_BLANK ="必填项不能为空";
    public static final String PARAMETER_NOT_BE_BLANK ="参数不能为空";
    public static final String STRING_SUCCESS ="成功";

    //JQ_INSURANCE 交强险
    public static final String SERVER_FAILURE ="服务器繁忙，请右上角刷新，稍后再试";

    //ASSOCIATORIMG 用户头像
    public static final String ASSOCIATORIMG ="associatorimg";

    //LICENSE_PLATE_NUMBER 车牌号
    public static final String LICENSE_PLATE_NUMBER ="licensePlateNumber";

    //MERCHANT_CODE 商户编码
    public static final String MERCHANT_CODE ="merchantCode";
    public static final String PORTRAIT ="portrait";
    public static final String MESSAGE_NUMBER ="messageNumber";

    //COMMON_FIELD 公共字段
    public static final String COMMON_FIELD ="commonField";
    //客户电话
    public static final String CUSTOMER_TEL ="customerTel";
    public static final String PASS_WORD ="passWord";
    //openId
    public static final String OPEN_ID ="openId";
    public static final String TO_KEN ="toKen";
    public static final String TOKEN_AGING ="tokenAging";
    public static final String ANDROID_DEVICEID ="androidDeviceId";
    public static final String IOS_DEVICEID ="iosDeviceId";
    //维修厂ID
    public static final String MAINTENANCESHOP_ID ="maintenanceshopId";

    //CAR_DETAIL 车妈妈车辆明细
    public static final String CAR_DETAIL ="carDetail";

    //TOKEN token
    public static final String TOKEN ="token";
    //HOME_PAGE_URL 首页图片路径
    public static final String HOME_PAGE_URL ="homePageImg/";
    //AGENCY_URL 分公司图片路径
    public static final String AGENCY_URL ="agency/";
    //ORDER_NO 订单编号
    public static final String ORDER_NO ="orderNo";
    //VALIDATION_CODE  验证token
    public static final String VALIDATION_CODE ="validationCode";

    //ORDER_STATUS 订单状态
    public static final String ORDER_STATUS ="orderStatus";

    //sy_INSURANCE 商业险
    public static final String SY_INSURANCE ="syInsurance";

    //MOBILE_NUMBER 手机号码
    public static final String MOBILE_NUMBER ="mobileNumber";
    //验证码
    public static final String VERIFICATION_CODE ="verificationCode";

    //PHOTO_UPLOAD_SUCCESS 图片上传成功
    public static final String PHOTO_UPLOAD_SUCCESS ="图片上传成功";

    //PHOTO_UPLOAD_FAIL 图片上传失败
    public static final String PHOTO_UPLOAD_FAIL ="图片上传失败";

    //PARAMETER_ERROR_PROMPT 参数错误提示
    public static final String PARAMETER_ERROR_PROMPT =" ";

    //ERROR 参数错误
    public static final String ERROR ="参数错误";

    //PASSENGERS 乘客险
    public static final String PASSENGERS ="乘客险";

    //permissions 权限
    public static final String PERMISSIONS ="对不起,您不是店铺管理员!不能执行此操作!";

    //REGISTRY 注册
    public static final String REGISTRY ="registry";

    //REPASSWORD 找回密码
    public static final String REPASSWORD ="rePassWord";

    //EMBODY 体现
    public static final String EMBODY ="embody";

    //SUCCESS 操作成功
    public static final String SUCCESS ="200";

    //FAIL 操作失败
    public static final String FAIL ="500";
    
    //FAIL_OPENID 获取openid失败
    public static final String FAIL_OPENID = "1000";
    
    //FAIL_AUTH 授权失败
    public static final String FAIL_AUTH = "1050";
    
  //already_received 已领取过优惠券
    public static final String ALREADY_RECEIVED ="501";

    //REPETITION 重复投保
    public static final String REPETITION ="488";

    //repeatCar 重复车辆
    public static final String REPEATCAR ="489";

    //FAIL 操作失败
    public static final String TOKEN_FAILURE ="388";

    //MERCHANTCODE_FAILURE 商户编码不存在
    public static final String MERCHANTCODE_FAILURE ="300";

    //ID_CARD_FRONT 身份证正面
    public static final String ID_CARD_FRONT ="idCardFront";

    //DRIVING_LICENSE 行驶证
    public static final String DRIVING_LICENSE ="drivingLicense";

    //IDCARD_REVERSE 身份证反面
    public static final String IDCARD_REVERSE ="idCardReverse";

    //COPY_DRIVING_LICENSE 行驶证副本
    public static final String COPY_DRIVING_LICENSE ="copyDrivingLicense";

    //PARAMETER_ERROR 参数错误
    public static final String PARAMETER_ERROR ="400";

    //TOKEN_VALIDITY_PERIOD token有效期
    public static final int TOKEN_VALIDITY_PERIOD =30;


    //INSURED_PER 被保人
    public static final String INSURED_PER="insuredPer/";

    /**
     *ASSOCIATOR_IMG_URL 用户头像信息
     */
    public static final String  ASSOCIATOR_IMG_URL ="associator/associatorimg/";

    /**
     *PHOTOGRAPHS2 		行驶证照片-->身份证照片正面
     */
    public static final String  MAINTENANCESHOP ="maintenanceshop/";
    public static final String  EVENT ="event/";
    public static final String  ORDER ="order/";

    public static final String  DRIVING_IMG_URL ="car/driving/";
    
    public static final String  POSTER_IMG_URL ="poster/";
    
    public static final String  CAR_IMG_URL ="car/car/";

    public static final String  COMPENSATE_IMG_URL ="event/compensate/";

    public final static String ALIPAY_CASH_WITHDRAWAL(String money) {
        return  "您提现"+money+"元到支付宝账户";
    }
    /**
     *PHOTOGRAPHS 		身份证照片正面-->身份证照片反面-->行驶证照片-->行驶证副本
     */
    public static final String[]  PHOTOGRAPHS4 ={ID_CARD_FRONT,IDCARD_REVERSE,DRIVING_LICENSE,COPY_DRIVING_LICENSE};

    /**
     *商铺 		logo-->商铺照片-->员工-->海报->营业执照-->二维码照片
     */
    public static final String[]  MAINTENANCESHOP_URL ={MAINTENANCESHOP+"logo/",MAINTENANCESHOP+"maintenanceshopImg/",MAINTENANCESHOP+"employeeimg/",MAINTENANCESHOP+"poster/",MAINTENANCESHOP+"businessLicense/",MAINTENANCESHOP+"qrcode/"};
    /**
     *商铺(车V互助) 		互助事件  申请理赔--定损-维修后 投诉
     */
    public static final String[]  EVENT_URL ={EVENT+"compensate/",EVENT+"assert/",EVENT+"maintenance/",EVENT+"complaint/",EVENT+"qrcode/"};
    /**
     *商铺(保险理赔) 		理赔事件  申请理赔--定损-维修后 投诉
     */
    public static final String[]  ORDER_URL ={ORDER+"accident/",ORDER+"assert/",ORDER+"repair/",ORDER+"complaint/"};

    public static final String[]  PHOTOGRAPHS4_URL ={INSURED_PER+ID_CARD_FRONT+"/",INSURED_PER+IDCARD_REVERSE+"/",INSURED_PER+DRIVING_LICENSE+"/",INSURED_PER+COPY_DRIVING_LICENSE+"/"};

    public static String getCustomerImgUrl(String imgName)throws Exception{
        return UploadFileUtil.getImgURL("poster/",imgName);
    }

    public static String getHomePageUrl(String imgName)throws Exception{
        return UploadFileUtil.getImgURL(HOME_PAGE_URL,imgName);
    }

    //获得车辆照片
    public static String getCarUrl(String imgName)throws Exception{
    	JSONObject carPhotos = JSON.parseObject(imgName);
        String cjh = UploadFileUtil.getImgURL(CAR_IMG_URL,(String) carPhotos.get("cjh"));
        String yq = UploadFileUtil.getImgURL(CAR_IMG_URL,(String) carPhotos.get("yq"));
        String zq = UploadFileUtil.getImgURL(CAR_IMG_URL,(String) carPhotos.get("zq"));
        String yh = UploadFileUtil.getImgURL(CAR_IMG_URL,(String) carPhotos.get("yh"));
        String zh = UploadFileUtil.getImgURL(CAR_IMG_URL,(String) carPhotos.get("zh"));
        String qd = UploadFileUtil.getImgURL(CAR_IMG_URL,(String) carPhotos.get("qd"));
        String zc = UploadFileUtil.getImgURL(CAR_IMG_URL,(String) carPhotos.get("zc"));
        String yc = UploadFileUtil.getImgURL(CAR_IMG_URL,(String) carPhotos.get("yc"));
        String carUrl = "";
        if(String.valueOf(carPhotos.get("qd")).equals("null")||String.valueOf(carPhotos.get("qd")).equals("")){
        	carUrl = "{\"yq\":\""+ yq +"\",\"zq\":\""+ zq +"\",\"yh\":\""+ yh +"\",\"zh\":\""+ zh +"\"}";
        }else{
        	carUrl = "{\"yq\":\""+ yq +"\",\"zq\":\""+ zq +"\",\"yh\":\""+ yh +"\",\"zh\":\""+ zh +"\",\"qd\":\""+ qd +"\",\"zc\":\""+ zc +"\",\"yc\":\""+ yc +"\"}";
        }
        
        return carUrl;
    }

    //获得行驶证照片
    public static String getCarDrivingUrl(String imgName)throws Exception{
        return UploadFileUtil.getImgURL(DRIVING_IMG_URL,imgName);
    }

    /**
     *PHOTOGRAPHS 		logo-->商铺照片-->员工-->海报-->营业执照-->二维码照片
     */
    public static String getMaintenanceShopImg(int i,String imgName)throws Exception{
        return UploadFileUtil.getImgURL(MAINTENANCESHOP_URL[i],imgName);
    }

    /**
     *
     * @param type 维修厂  0. logo  1.商铺照片 2.员工  3.海报  4.营业执照 5二维码照片
     * @param imgNames
     * @return
     * @throws Exception
     */
    public static List<String> getMaintenanceShopImgList( int type,String imgNames)throws Exception{
        List<String> imgList = new ArrayList<>();
        if (Constant.toEmpty(imgNames)) {
            String[] strings = imgNames.split("_");
            for (int i = 0; i <strings.length ; i++) {
                String imgName = strings[i];
                String maintenanceShopImg =getMaintenanceShopImg(type, imgName);
                imgList.add(i,maintenanceShopImg);
            }
            return imgList;
        }
        return imgList;
    }

    /**
     *PHOTOGRAPHS 		车V互助事件 0 申请理赔--1 定损- 2 维修后  3 投诉
     */
    public static String getEventImg(int i,String imgName)throws Exception{
        return UploadFileUtil.getImgURL(EVENT_URL[i],imgName);
    }

    /**
     *PHOTOGRAPHS 		保险理赔事件 0 申请理赔--1 定损- 2 维修后  3 投诉
     */
    public static String getOrderImg(int i,String imgName)throws Exception{
        return UploadFileUtil.getImgURL(ORDER_URL[i],imgName);
    }
    /**
     *PHOTOGRAPHS 		账户明细logo
     */
    public static String getMoneyDetailsLogoImg(String imgName)throws Exception{
        return UploadFileUtil.getImgURL("moneyDetailsLogo/",imgName);
    }

    /**
     *
     * @param type 车V互助事件  0 申请理赔   1 定损   2 维修后 3 投诉
     * @param imgNames
     * @return
     * @throws Exception
     */
    public static List<String> getImgList( int type,String imgNames)throws Exception{
        return getImgLists(0,type,imgNames);
    }

    /**
     * @param type 保险理赔事件  0 申请理赔   1 定损   2 维修后 3 投诉
     * @param imgNames
     * @return
     * @throws Exception
     */
    public static List<String> getOrderImgList(int type,String imgNames)throws Exception{
        return getImgLists(1,type,imgNames);
    }
    public static List<String> getImgLists(int status,int type,String imgNames)throws Exception{
        List<String> imgList = new ArrayList<>();
        if (Constant.toEmpty(imgNames)) {
            String[] strings = imgNames.split("_");
            for (int i = 0; i <strings.length ; i++) {
                String imgName = strings[i];
                String eventImg=null;
                if(status==1){
                    eventImg =getOrderImg(type, imgName);
                }else{
                    eventImg =getEventImg(type, imgName);
                }
                imgList.add(i,eventImg);
            }
            return imgList;
        }
        return imgList;
    }


    //获得车辆照片
    public static Map<String,String> getCarUrlMap(String imgName)throws Exception{
        Map<String,Object> map = JsonUtils.json2Map(imgName);
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("yq","");
        parameterMap.put("zq","");
        parameterMap.put("yh","");
        parameterMap.put("zh","");
        parameterMap.put("qd","");
        parameterMap.put("zc","");
        parameterMap.put("yc","");
        parameterMap.put("cjh","");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (Constant.toEmpty(value)) {
                parameterMap.put(entry.getKey(),UploadFileUtil.getImgURL(CAR_IMG_URL,value.toString()));
            }
        }
        return parameterMap;
    }
    //获得车辆照片
    public static List<String> getCarUrlList(String imgName)throws Exception{
        List<String> carImgList=new ArrayList<>();
        if (!Constant.toEmpty(imgName)) {
            return carImgList;
        }
        Map<String,Object> map = JsonUtils.json2Map(imgName);
        Object qd = map.get("qd");
        Object yq = map.get("yq");
        Object yc = map.get("yc");
        Object yh = map.get("yh");
        Object zq = map.get("zq");
        Object zc = map.get("zc");
        Object zh = map.get("zh");
//        Object cjh = map.get("cjh");
        Object[] carImgs ={qd,yq,yc,yh,zh,zc,zq};
        for (int i = 0; i <carImgs.length ; i++) {
            Object carImg = carImgs[i];
            if (Constant.toEmpty(carImg)) {
                carImgList.add(UploadFileUtil.getImgURL(CAR_IMG_URL,carImg.toString()));
            }
        }
        return carImgList;
    }

}
