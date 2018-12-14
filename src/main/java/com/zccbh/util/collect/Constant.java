package com.zccbh.util.collect;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.zccbh.demand.service.weChat.SpringContextHolder;
import com.zccbh.util.base.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Project:
 * @Comments: 常量
 * @JDK version used:        1.8
 * @Author: DengJian
 * @Create Date:            2017年11月17日
 * @Modified By:            <修改人中文名或拼音缩写>
 * @Modified Date:          <修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: <修改原因描述>
 */
public  class Constant {

    private static final Logger logger = LoggerFactory.getLogger(Constant.class);

    /*formatting*/
    public final static SimpleDateFormat SF_SIZE19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//初始化时间格式
    public final static SimpleDateFormat SF_YMDHMS = new SimpleDateFormat("yyyyMMddHHmmss");

    public final static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i|windows (phone|ce)|blackberry|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp|laystation portable)|nokia|fennec|htc[-_]|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    public final static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    //移动设备正则匹配:手机端、平板
    public final static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    public final static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);
    public final static BigDecimal oneQuota = new BigDecimal("125");

    /**
     * 检测是否是移动设备访问
     * @param userAgent 浏览器标识
     * @return true:移动设备接入，false:pc端接入
     */
    public static boolean toBrowserCheck(String userAgent){
        if(null == userAgent)
            userAgent = "";
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if(matcherPhone.find() || matcherTable.find()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建互助事件编号
     * @return
     */
    public static String createEventNo(){
        return toCurrTime()+""+(int)(new Random().nextDouble()*(1000000 - 10000) + 10000);
    }

    /**
     * 创建优惠券编号
     * @return
     */
    public static String createCouponNo(){
        String val = toCurrTime();
        Random random = new Random();
        for ( int i = 0; i < 6; i++ )
        {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) )
            { // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                val += (char) ( nextInt + random.nextInt( 26 ) );
            }
            else if ( "num".equalsIgnoreCase( str ) )
            { // 产生数字
                val += String.valueOf( random.nextInt( 10 ) );
            }
        }
        return val;
    }

    /**
     * @Comments: 是否为空(字符串|集合|数组:!=null or size>0, 数字 > 0, boolean:true)
     * @param: 不定参数(不定类型)
     * @return: boolean true不为空，false为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean toEmpty(Object... obj) {
        if (obj != null) {
            for (Object o : obj) {
                if (o != null) {
                    if (o instanceof String && !o.equals("")) {//传值为字符串    不能为""
                        continue;
                    } else if (o instanceof Number) {//传值为数字  && ((Number) o).doubleValue() > 0
                        continue;
                    } else if (o instanceof Boolean && (Boolean) o) {//传值为 boolean 不能为false
                        continue;
                    } else if (o instanceof Collection && !((Collection) o).isEmpty()) {//传值为Collection 长度 > 0
                        continue;
                    } else if (o instanceof Map && !((Map) o).isEmpty()) {//传值为map 长度 > 0
                        continue;
                    } else if (o instanceof Object[] && ((Object[]) o).length > 0) {//传值为数组 长度 > 0
                        continue;
                    } else {//否则  为没有值
                        return false;
                    }
                } else {//否则  为没有值
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Comments: 抛出异常 (时间 路径 错误类型 错误行数)
     * @param: exception, className
     */
    public static void toThExCla(Exception e, String[] includeArr) {
        String method = "";
        StackTraceElement ste = null;
        JSONObject strJson = new JSONObject();
        for (int i = 0; i < e.getStackTrace().length; i++) {
            //e.getMessage();
            ste = e.getStackTrace()[i];
            if (i == 0)//obtain throws exception className and methodName
                method = ste.getClassName() + "." + ste.getMethodName() + ":";
            String _method = ste.getClassName() + "." + ste.getMethodName() + ":";//获取每一个数据的类名和方法名
            for(int j = 0;j< includeArr.length;j++){
                if (_method.indexOf(includeArr[j]) > -1 && ste.getLineNumber() != -1) {//系统如果抛出 include(类名) error则加入行号
                    String str = Constant.toEmpty(strJson.get(includeArr[j]))?strJson.getString(includeArr[j]):"";
                    strJson.put(includeArr[j],str+","+ste.getLineNumber());
                }
            }
        }
        //e.fillInStackTrace() e.getMessage()
        System.err.println(toStrTime() + " → " + method + strJson.toString());
        System.err.println(" message:"+ e.getMessage());
    }

    /**
     * @Comments: 获取当前系统时间 yyyy-MM-dd HH:mm:ss
     * @return: String
     */
    public static String toStrTime() {
        return SF_SIZE19.format(new Date());
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     * @return String
     */
    public static String toCurrTime() {
        return SF_YMDHMS.format(new Date());
    }

    /**
     * 获取当前时间(Date)
     * @return
     */
    public static Date toNowTimeDate() {
        Date time = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        try {
            time = df.parse(df.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     * @param length int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int toRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * @Comments:读取文件
     * @param:
     * @return:
     */
    public static String toReadPro(String name) {
        Properties prop = new Properties();
        try {
//            RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);
//            String s = redisUtil.get("properties");
//            prop.load(Constant.class.getResourceAsStream(s));
            prop.load(Constant.class.getResourceAsStream("/"+SpringContextHolder.getBean(RedisUtil.class).get("properties")));
//            prop.load(Constant.class.getResourceAsStream("/com/zccbh/config/applicationDev.properties"));
        } catch (IOException e) {
            toThExCla(e, new String[]{"Constant"});
        }
        return prop.getProperty(name);
    }

    /**
     * hash
     * @param str
     * @return
     */
    public static String toHash(String str) {
        long hash = 5381;
        String c = "";
        for (int i = 0; i < str.length(); i++) {
            hash = str.charAt(i);
            c += "&#" + hash + ";";
        }
        return c;
    }

    /**
     * @param code,message,includeArr,obj
     * @comments 返回给前端的模型数据
     */
    public static String toReModel(String code,String message, String[] includeArr, Object obj) {
        JSONObject returnJson = new JSONObject();
        if (code.equals("4444")) {
            Constant.toThExCla((Exception) obj, includeArr);
        }
        returnJson.put("code", code);
        returnJson.put("message", message);
        returnJson.put("data", null);
        return returnJson.toString();
    }
    public static String toReModel(String code,String message, Object obj) {
        JSONObject returnJson = new JSONObject();
            returnJson.put("code", code);
            returnJson.put("message", message);
            returnJson.put("data", obj);
        return returnJson.toString();
    }
    public static String toSuccessReModel(Object obj) {
        JSONObject returnJson = new JSONObject();
            returnJson.put("code", "200");
            returnJson.put("message", "");
            returnJson.put("data", obj);
        return returnJson.toString();
    }
    public static ModelMap toModelMap(String code, String message, Object obj) {
        ModelMap modelMap = new ModelMap();
            modelMap.put("code", code);
            modelMap.put("message", message);
            modelMap.put("data", obj);
        return modelMap;
    }

    /**
     * @Comments:default:ISO-8859-1 -> UTF-8 乱码则转码
     * @param: str(乱码字符), coding(default:ISO-8859-1), coding1(default:UTF-8)
     * @return: str
     */
    public static String toUTF8(String str, String coding, String coding1) {
        if (str == null)
            return null; //为空直接返回
        String retStr = str;
        try {
            byte b[] = str.getBytes(null != coding && "".equals(coding) ? coding : "ISO-8859-1");
            for (int i = 0; i < b.length; i++) {
                byte b1 = b[i];
                if (b1 == 63) {//如果b[i]有63,不用转码;
                    break;
                } else if (b1 > 0) {//如果b[i]全大于0,那么为英文字符串,不用转码;
                    continue;
                } else if (b1 < 0) {//如果b[i]有小于0的,那么已经乱码,要转码.
                    retStr = new String(b, null != coding1 && "".equals(coding1) ? coding1 : "UTF-8");
                    break;
                }
            }
        } catch (Exception e) {
            toThExCla(e, new String[]{"Constant"});
        }
        return retStr;
    }

    /**
     * jwts
     * @param rqSide, str
     */
    public static String toJWTS(String rqSide, String str) {
        String reVal = null;
        if (str.equals("+")) {
            reVal = Jwts.builder().setSubject(rqSide)
                 .compressWith(CompressionCodecs.DEFLATE)
                 .signWith(SignatureAlgorithm.HS256, toReadPro("jwtSK"))
                 .setIssuedAt(new Date())
                 .claim("rqSide", rqSide)
                 .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS))) // 有效期一个星期
                 .compact();
        } else {
            Claims c = Jwts.parser().setSigningKey(toReadPro("jwtSK")).parseClaimsJws(rqSide).getBody();
            JSONObject jsonObject = JSONObject.fromObject(c);
            System.out.println();
            long localTime = Date.from(Instant.now()).getTime() / 1000;
            long exp = jsonObject.getLong("exp");
            reVal = localTime - exp < 0 ? jsonObject.getString("rqSide") : null;//有效时间
        }
        return reVal;
    }

    public static String toB64(String rqSide, String str) {
        if (str.equals("+")) {
            rqSide = rqSide.equals("H5") ? "CBH-H5" :
                 rqSide.equals("PC") ? "CBH-PC" :
                      rqSide.equals("ios") ? "CBH-IOS" :
                           rqSide.equals("android") ? "CBH-Android" : rqSide;
            return Base64.encodeBase64String(rqSide.getBytes());
        } else {
            return new String(Base64.decodeBase64(rqSide));
        }
    }

    /**
     * @Comments: 根据键值进行加密
     * @param: data, key加密键byte数组
     * @return: boolean
     */
    public static String toOr(String data, String key,String str) throws Exception {
        SecureRandom sr = new SecureRandom();//生成一个可信任的随机数源
        DESKeySpec dks = new DESKeySpec(key.getBytes());//从原始密钥数据创建DESKeySpec对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Constant.toReadPro("OFTEN_DES"));//创建一个密钥工厂,然后用它把DESKeySpec转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(Constant.toReadPro("OFTEN_DES"));//Cipher对象实际完成加密操作
        String strs = null;
        if(str == "encrypt"){
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);//用密钥初始化Cipher对象
            strs = new BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
        }else if(str == "decrypt"){
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);//用密钥初始化Cipher对象
            strs = new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(data)));
        }
        return strs;
    }

    /**
     * @param sr   操作
     * @param code
     * @return
     * @comments 阿里短信模板
     */
    private static String toSMSText(String sr, String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        if (sr.equals("registry")) {
            map.put("product", "注册");
        } else if (sr.equals("login")) {
            map.put("product", "登录");
        } else if (sr.equals("rePassWord")) {
            map.put("product", "找回密码");
        }
        return JSONObject.fromObject(map).toString();
    }

    /**
     * @param rn 手机号
     * @return
     * @comments 阿里短信发送短信
     */
    public static boolean toALSMS(String rn, String vCode, String smsUse) throws Exception {
        String orKey = toReadPro("orKey");
        TaobaoClient client = new DefaultTaobaoClient(toOr(toReadPro("alSMSUrl"), orKey,"encrypt"),
             toOr(toReadPro("alSMSAppkey"), orKey,"encrypt"), toOr(toReadPro("alSMSSecret"), orKey,"encrypt"));
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend(toReadPro("alExt"));                                    //回传值
        req.setSmsType("normal");                                                    //类型
        req.setSmsFreeSignName(toOr(toReadPro("alSMSFSN"), orKey,"encrypt"));            //签名
        req.setRecNum(rn);                                                            //收信人phone
        req.setSmsParamString(toSMSText(smsUse, vCode));                                //内容
        req.setSmsTemplateCode(toOr(toReadPro("alSMSTC_1"), orKey,"encrypt"));        //模板id
        AlibabaAliqinFcSmsNumSendResponse res = client.execute(req);
        //System.out.println("-------------------------------------------------");
        //System.out.println(toOr(toReadPro("alSMSFSN"),orKey));
        //System.out.println(res.getBody());;
        //System.out.println("-------------------------------------------------");
        return true;
    }

    /**
     * @Comments:计算
     * @param: d1, d2, type("-","+","*")
     * @return: double
     */
    public static double toComput(double d1, double d2, String type) {
        long L1 = (long) (d1 * 10000);
        long L2 = (long) (d2 * 10000);
        long L3 = 0;
        if (null != type && type.equals("-"))
            L3 = L1 - L2;
        else if (null != type && type.equals("+"))
            L3 = L1 + L2;
        else if (null != type && type.equals("*"))
            L3 = L1 * L2;
        //else if(null != type && type.equals("/"))
        //L3 = L1 / L2;
        if (L3 % 10 > 0) {
            return (L3 - L3 % 10 + 10 * 1.0) / 10000.0;
        } else {
            return L3 * 1.0 / 10000.0;
        }
    }

    /**
     * @Comments:计算
     * @param: n1, n2, type("-","+","*","/")
     * @return: double
     */
    public static double toDecimal(double n1, double n2, String type) {
        double bear = 0;
        try {
            BigDecimal bd_1 = new BigDecimal(n1);
            BigDecimal bd_2 = new BigDecimal(n2);
            //ign.ex.methods.dome.BinarySystem.java
            if (type.equals("+")) {//add(BigDecimal)BigDecimal对象中的值相加，然后返回这个对象。
                bear = bd_1.add(bd_2).setScale(2, RoundingMode.HALF_UP).doubleValue();
            } else if (type.equals("-")) {//subtract(BigDecimal)BigDecimal对象中的值相减，然后返回这个对象。
                bear = bd_1.subtract(bd_2).setScale(2, RoundingMode.HALF_UP).doubleValue();
            } else if (type.equals("*")) {//multiply(BigDecimal)BigDecimal对象中的值相乘，然后返回这个对象。
                bear = bd_1.multiply(bd_2).setScale(2, RoundingMode.HALF_UP).doubleValue();
            } else if (type.equals("/")) {//divide(BigDecimal)BigDecimal对象中的值相除，然后返回这个对象。
                bear = bd_1.divide(bd_2).setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
        } catch (Exception e) {
            toThExCla(e, new String[]{"Constant"});
        }
        return bear;
    }

    /**
     * 利用MD5进行加密
     * @param str 待加密字符
     * @return 加密后字符
     * @throws Exception
     */
    public static String toByMd5(String str) throws Exception{
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        return base64en.encode(md5.digest(str.getBytes("utf-8")));//加密后的字符串
    }

    /**
     * 加*
     * @param str
     * @return
     */
    public static String toPart(String str){
        if(str.length() > 9){
            return str.substring(0,3)+"****"+str.substring(str.length()-4,str.length());
        }else{
            return str;
        }
    }

    /**
     * 获取checkCode
     * @param params
     * @return
     */
    public static String toCheckCode(Map params){
        List<String> list = new ArrayList<String>();
        Set keySet = params.keySet();//获取所有的参数
        for (Iterator<String> it=keySet.iterator();it.hasNext();) {
            String keyName =  it.next();
            if (keyName.equals("checkCode"))
                continue;
            list.add(keyName + "="+ params.get(keyName));
        }
        Collections.sort(list);//排序
        String source = "";
        for (String i : list)
            source += i + "&";
        source = source.substring(0, source.length() - 1);
        source +=  "cbh_s";
        String md5CheckCode = MD5Util.encrypt(source, "UTF-8");
        return md5CheckCode;
    }

    public static String toKL(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 'd');
        }
        return new String(a);
    }

    /**
     * @param jsonObject,key
     * @comments 根据key获取不为空的JSONObject value
     */
    public static String toJsonValue(JSONObject jsonObject, String key) {
        return Constant.toEmpty(jsonObject.get(key)) ? jsonObject.getString(key) : "";
    }

    /**
     * 生成随机数字和字母
     * @param length
     * @return
     */
    public static String toStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if( "char".equalsIgnoreCase(charOrNum) ) {//输出字母还是数字
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
    public static String toInviteCode(String id,String type){
        String inviteCodeTop = "" + id;
        if(inviteCodeTop.length() <= 4){
            if(type.equals("company")) {
                inviteCodeTop = ((char) (int) (Math.random() * 26 + 97)) + "" + ((char) (int) (Math.random() * 26 + 97)) + "" + id + toStringRandom(4 - inviteCodeTop.length());
            }else{
                inviteCodeTop = ((char) (int) (Math.random() * 26 + 97)) + "" + id + toStringRandom(5 - inviteCodeTop.length());
            }
        }else if((inviteCodeTop.length()-4)%3 >= 0){
            char[] ictChild = inviteCodeTop.toCharArray();
            if(type.equals("company")){
                inviteCodeTop = "" + ((char)(int)(Math.random()*26+97)) + "" + ((char)(int)(Math.random()*26+97));
            }else{
                inviteCodeTop = "" + ((char)(int)(Math.random()*26+97));
            }
            for(int i=0;i < ictChild.length;i++){
                inviteCodeTop += ictChild[i] + "" + ((char)(int)(Math.random()*26+97));
            }
        }
        return inviteCodeTop.toUpperCase();
    }

    public static void main(String[] args) {
         System.out.println(toHash("创建订单"));
         try{
             JSONObject aa = new JSONObject();
             aa.put("bs",742.22);
             aa.put("cwa",0);
             aa.put("fa",0);
             Map<String,Object> cc = new HashMap<String,Object>();
             cc.put("bs", com.alibaba.fastjson.JSON.toJSONString(aa));

             System.out.println(toOr(cc.get("bs").toString(),toReadPro("orKey"),"encrypt"));

             System.out.println(toOr(toOr(cc.get("bs").toString(),toReadPro("orKey"),"encrypt"),toReadPro("orKey"),"decrypt"));

             System.out.println(toOr("+aDUJ+GOHHEyzHcLbZq4YhWoDNPWx0u2UzwLkHA+6BU=",toReadPro("orKey"),"decrypt"));

             System.out.println(toOr("diJFz+X+RzBbcouZx0n7fBWoDNPWx0u2UzwLkHA+6BU=",toReadPro("orKey"),"decrypt"));


         }catch (Exception e){

         }

         // JSONObject j = JSONObject.fromObject("{\\\"name\":\\\"小明\\\",\\\"phone\\\":\\\"13547968547\\\",\\\"ct\\\":\\\"1\\\",\\\"cn\\\":\\\"511304199104206219\\\"}");

         // System.out.println(j);

           /* System.out.println(280/2%10-1);

            //System.out.println(toB64("H5","+"));
            System.out.println(MD5Util.getMD5Code("chevdian"));*/

     }
}
