package com.zccbh.util.collect;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.crypto.Cipher;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class WxUtil {

    //接口返回地址
    public final static String wxPayBackpath = "http://www.chevdian.com/thirdParty/wxResult";
    //获取预支付id的接口url
    public final static String gateurl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //转账到银行卡
    public final static String bankurl = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";
    //获取微信公钥
    public final static String publicKey = "https://fraud.mch.weixin.qq.com/risk/getpublickey";

    /**
     * js微信参数
     * @return
     */
    public static SortedMap<String, String> toWxJsInfo(String url, String accessToken) {
        String currTime = Constant.toCurrTime();
        String jsapi_ticket = getTicket(accessToken);//获取Ticket
        String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//时间戳和随机字符串
        //System.out.println("accessToken:"+accessToken+"\njsapi_ticket:"+jsapi_ticket+"\n时间戳："+currTime+"\n随机字符串："+noncestr);
        String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + currTime + "&url=" + url;//将参数排序并拼接字符串
        //生成签名
        SortedMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("appId", Constant.toReadPro("appId"));
        treeMap.put("timestamp", currTime);
        treeMap.put("nonceStr", noncestr + "");//currTime.substring(8, currTime.length())+toRandom(4)
        treeMap.put("jsApiList", "[onMenuShareTimeline,onMenuShareAppMessage,onMenuShareQQ,onMenuShareWeibo,onMenuShareQZone]");
        treeMap.put("signature", SHA1(str));//将字符串进行sha1加密
        return treeMap;
    }

    private static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2)
                    hexString.append(0);
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Constant.toThExCla(e,new String[]{"WxUtil"});
        }
        return "";
    }

    private static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";//这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            Constant.toThExCla(e,new String[]{"WxUtil"});
        }
        return ticket;
    }

    /**
     * 解析xml
     * @param value
     * @return
     */
    public static Map<String, Object> resolveXml(String value) throws Exception{
    	/*2018.10.9 修改*/
   /*     Map<String, Object> resultMap = new TreeMap<String, Object>();
        try {
            Document document = DocumentHelper.parseText(value);
            Element rootElt = document.getRootElement(); //获取根节点
            Iterator iterator = rootElt.elementIterator();//获取所有子节点
            while(iterator.hasNext()){
                Element secondElement = (Element) iterator.next();
                String name = secondElement.getName();
                String text = secondElement.getText();
                resultMap.put(name, text);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return resultMap;*/
    	
	    	try {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
				InputStream stream = new ByteArrayInputStream(value.getBytes("UTF-8"));
				org.w3c.dom.Document doc = documentBuilder.parse(stream);
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getDocumentElement().getChildNodes();
				for(int idx = 0; idx < nodeList.getLength(); ++idx){
					Node node = nodeList.item(idx);
					if(node.getNodeType() == Node.ELEMENT_NODE){
						org.w3c.dom.Element element = (org.w3c.dom.Element) node;
						resultMap.put(element.getNodeName(), element.getTextContent());
					}
				}
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return resultMap;
			} catch (Exception e) {
				throw e;
			}
		} 

    /**
     * @Description：将请求参数转换为xml格式的string
     * @param parameters  请求参数
     * @return
     */
    public static String getRequestXml(SortedMap<String,String> parameters) throws Exception{
    	/*修改2018.10.9*/
     /*   StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();*/
    	org.w3c.dom.Document document = WXPayXmlUtil.newDocument();
    	org.w3c.dom.Element root = document.createElement("xml");
    	document.appendChild(root);
    	for(String key: parameters.keySet()){
    		String value = parameters.get(key);
    		if(value == null){
    			value = "";
    		}
    		value = value.trim();
    		org.w3c.dom.Element field = document.createElement(key);
    		field.appendChild(document.createTextNode(value));
    		root.appendChild(field);
    	}
    	TransformerFactory tf = TransformerFactory.newInstance();
    	Transformer transformer = tf.newTransformer();
    	DOMSource source = new DOMSource(document);
    	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    	StringWriter writer = new StringWriter();
    	StreamResult result = new StreamResult(writer);
    	transformer.transform(source, result);
    	String output = writer.getBuffer().toString();
    	try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return output;
    	
    }

    /**
     * h5起调支付界面
     * @param prepayId
     * @return
     */
    public static com.alibaba.fastjson.JSONObject coverH5Pay(Object prepayId){
        SortedMap<String, String> treeMap = new TreeMap<String, String>();
        String currTime = Constant.toCurrTime();
        treeMap.put("appId", Constant.toReadPro("appId"));
        treeMap.put("timeStamp", currTime);
        treeMap.put("nonceStr",  currTime.substring(8, currTime.length())+Constant.toRandom(4) + "");
        treeMap.put("package", "prepay_id="+prepayId);
        treeMap.put("signType", "MD5");
        treeMap.put("paySign", createSHA1Sign(treeMap));//签名
        //JSONObject json = JSONObject.fromObject(treeMap);
        return (com.alibaba.fastjson.JSONObject) JSON.toJSON(treeMap);
    }

    /**
     *  商户转账到银行卡
     * @param orderNo  订单号
     * @param userBankCard  银行卡号
     * @param userName 用户姓名
     * @param bankCode 银行代码
     * @param amt  提现金额
     * @param desc 描述
     * @return
     */
    public static String reUsePrepay(String orderNo,String userBankCard,String userName,String bankCode,int amt,String desc) throws Exception{
        String reWxTxContents = "";
        if(orderNo.length() > 32)
            orderNo = orderNo.substring(orderNo.length()-31,orderNo.length());
        SortedMap<String,String> es = new TreeMap<String,String>();
        es.put("amount",amt+"");//付款金额
        es.put("bank_code",bankCode);//收款方开户行
        es.put("mch_id",Constant.toReadPro("partner"));//商户号
        es.put("nonce_str",SuJiShu());   //随机字符串
        es.put("partner_trade_no",orderNo); //商户企业付款单号
        es.put("enc_bank_no",RSACipher(userBankCard.getBytes()));//收款方银行卡号
        es.put("enc_true_name",RSACipher(userName.getBytes("UTF-8")));//收款方用户名
        es.put("desc",desc);//付款说明
        es.put("sign",createSHA1Sign(es));//签名
        String xmlStr = getRequestXml(es);
        reWxTxContents = httpClientPostSSL(bankurl, xmlStr);
        return reWxTxContents;
    }

    /**
     * RSA  RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING填充
     * @param value
     * @return
     * @throws Exception
     */
    private static String RSACipher(byte[] value) throws Exception{
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, loadPublicKeyByStr(loadPublicKeyByFile()));
        return new String(base64.encode(cipher.doFinal(value)));
    }
    /**
     * 公钥 pkcs#8
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    private static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            org.apache.commons.codec.binary.Base64 base64 = new Base64();
            byte[] buffer = base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }
    private static String loadPublicKeyByFile() throws Exception {
        try {
            InputStream is = WxUtil.class.getResourceAsStream("/com/zccbh/config/prove/publicKey.pem");
            int count = 0;
            while (count == 0)
                count = is.available();
            byte[] bytes = new byte[count];
            int readCount = 0; // 已经成功读取的字节的个数
            while (readCount <= count) {
                if (readCount == count)
                    break;
                readCount += is.read(bytes, readCount, count - readCount);
            }
            String readContent = new String(bytes, 0, readCount, "UTF-8");
            is.close();

            String[] a = readContent.split("-----BEGIN PUBLIC KEY-----");
            String[] b = a[1].split("-----END PUBLIC KEY-----");
            readContent = b[0].replace(" ", "");
            readContent = readContent.replace("\r", "");
            readContent = readContent.replaceAll("\n", "");
            return readContent;
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 创建签名SHA1
     * @param esMd5
     * @return
     */
    public static String createSHA1Sign(SortedMap<String,String> esMd5) {
        //排序
        SortedMap<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(esMd5);
        StringBuffer sb = new StringBuffer();
        Set es = sortMap.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        String params = sb.toString()+"key="+ Constant.toReadPro("partnerKey");
        String appsign = MD5Util.getMD5Code(params).toUpperCase();
        return appsign;
    }
    //比较器类
    public static class MapKeyComparator implements Comparator<String>{
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /**
     * 生成随机字符串
     * @return
     */
    public static String SuJiShu(){
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return temp;
    }

    /**
     * @Title: 微信预支付
     * @param time_start
     * @param basePath 通知地址
     * @param userIp 用户ip地址
     * @param describe 商品描述
     * @param openId 用户openId
     * @param orderId 订单id
     * @param taotal 需要支付的金额
     * SuJiShu 随机字符窜
     * deviceNo 设备号
     */
    public static String sendPrepay(long time_start,String basePath,String userIp,String describe,String openId,String orderId,int taotal,String SuJiShu,String deviceNo) throws Exception{
        String prepayid = "";
        SortedMap<String,String> es = new TreeMap<String,String>();
        es.put("appid", Constant.toReadPro("appId"));
        es.put("mch_id", Constant.toReadPro("partner"));  //商户号
        es.put("device_info", deviceNo);        //设备号
        es.put("nonce_str", SuJiShu);           //随机字符
        es.put("body", describe);                //商品描述
        es.put("attach", "车V店");              //附加数据
        es.put("out_trade_no", orderId);        //商户订单号
        es.put("total_fee", taotal+"");         //总金额 订单总金额，单位为分，详见支付金额
        es.put("spbill_create_ip", userIp);
        es.put("notify_url", basePath);          //通知地址
        es.put("trade_type", "JSAPI");
        es.put("time_start", time_start+"");
        es.put("openid", openId);
        es.put("sign", createSHA1Sign(es)); //签名
        String xmlStr = getRequestXml(es);
        prepayid = HttpPost(gateurl, xmlStr);
        //System.out.println(prepayid);		//et-life annotation
        return prepayid;
    }

    /**
     * 向指定URL发送POST方法的请求
     * @param url 发送请求的URL
     * @param param  请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String HttpPost(String url,String param){
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();// 打开和URL之间的连接
            conn.setRequestProperty("accept", "*/*");// 设置通用的请求属性
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Accept-Charset", "UTF-8");// 设置 HttpURLConnection的字符编码
            // 发送POST请求必须设置如下两行
            //conn.setConnectTimeout(60);
            conn.setConnectTimeout(3000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));// 获取URLConnection对象对应的输出流
            if(param!=null&&!param.equals("")){// 发送请求参数
                out.print(param);
            }
            out.flush();// flush输出流的缓冲
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));// 定义BufferedReader输入流来读取URL的响应
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            result="链接超时";
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 获取微信 RSA 公钥
     * @param praurl
     * @param data
     * @return
     * @throws Exception
     */
    public static String he(String praurl,String data) throws Exception{
        // 证书文件(微信商户平台-账户设置-API安全-API证书-下载证书)
        KeyStore keyStore = KeyStore.getInstance("PKCS12"); //指定读取证书格式为PKCS12
        //FileInputStream instream = new FileInputStream(new File("/com/zccbh/config/prove/apiclient_cert.p12"));//读取本机存放的PKCS12证书文件
        FileInputStream instream = (FileInputStream)WxUtil.class.getResourceAsStream("/com/zccbh/config/prove/publicKey.pem");
        try {
            keyStore.load(instream, "1489209962".toCharArray());//指定PKCS12的密码(商户ID)
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1489209962".toCharArray()).build();
        //指定TLS版本
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        //设置httpclient的SSLSocketFactory
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        HttpPost httpost = new HttpPost(praurl);
        httpost.setEntity(new StringEntity(data, "UTF-8"));
        CloseableHttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        EntityUtils.consume(entity);
        return jsonStr;
    }
    /**
     * 微信商户转账到个人银行双向验证
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static String httpClientPostSSL(String url,String data) throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        //FileInputStream instream = new FileInputStream(new File("/com/zccbh/config/prove/apiclient_cert.p12"));
        InputStream is = WxUtil.class.getResourceAsStream("/com/zccbh/config/prove/apiclient_cert.p12");
        keyStore.load(is, Constant.toReadPro("partner").toCharArray());
        is.close();
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, Constant.toReadPro("partner").toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom() .setSSLSocketFactory(sslsf) .build();
        HttpPost httpost = new HttpPost(url);
        httpost.addHeader("Connection", "keep-alive");
        httpost.addHeader("Accept", "*/*");
        httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpost.addHeader("Host", "api.mch.weixin.qq.com");
        httpost.addHeader("X-Requested-With", "XMLHttpRequest");
        httpost.addHeader("Cache-Control", "max-age=0");
        httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        httpost.setEntity(new StringEntity(data, "UTF-8"));
        CloseableHttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        EntityUtils.consume(entity);
        return jsonStr;
    }

    /**
     * 微信订单查询
     * @param outTradeNo
     * @return
     */
    public String wxOrderCheck(String outTradeNo) throws Exception{
        String result = "";
        SortedMap<String,String> es = new TreeMap<String,String>();
        es.put("appid", Constant.toReadPro("appId"));
        es.put("mch_id",  Constant.toReadPro("partner"));  //商户号
        es.put("nonce_str", SuJiShu()); //随机字符
        es.put("out_trade_no", outTradeNo); //商户订单号
        es.put("sign", createSHA1Sign(es)); //签名
        String xmlStr = getRequestXml(es);
        result = HttpPost( Constant.toReadPro("gateurl"), xmlStr);
        return result;
    }




    /**
     * 生成商户公钥
     * @return
     * @throws Exception
     */
    private String reEncTrueName() throws Exception{
        SortedMap<String,String> es = new TreeMap<String,String>();
        es.put("mch_id",Constant.toReadPro("partner"));//商户号 ,Constant.toReadPro("partner")
        es.put("nonce_str",SuJiShu());                         //随机字符串
        es.put("sign_type","MD5");
        es.put("sign",createSHA1Sign(es));
        String xmlStr = getRequestXml(es);
        String reEncTrueName = he(publicKey, xmlStr);
        return reEncTrueName;
    }
    /**
     * 获取微信端 公钥 存入文件
     * @throws Exception
     */
    private void WxTxPubKey() throws Exception{
        Map<String, Object> WxReturnMap = WxUtil.resolveXml(reEncTrueName());
        String pubKey = null;
        if(WxReturnMap.get("result_code").equals("SUCCESS"))
            pubKey = WxReturnMap.get("pub_key").toString();
        //存入文件
        FileWriter pubfw = new FileWriter( "/com/zccbh/config/prove/publicKey.pem");
        BufferedWriter pubbw = new BufferedWriter(pubfw);
        pubbw.write(pubKey);
        pubbw.flush();
        pubbw.close();
        pubfw.close();

        //读取文件
        BufferedReader br = new BufferedReader(new FileReader("/com/zccbh/config/prove/publicKey.pem"));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            sb.append(readLine);
        }
        br.close();
    }
    
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }


}
