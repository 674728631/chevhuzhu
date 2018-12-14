package com.zccbh.util.base;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-03-21 11:01
 **/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;
import com.zccbh.util.collect.Constant;

/**
 * 获取经纬度
 *
 * @author Sunny
 * 密钥:f247cdb592eb43ebac6ccd27f796e2d2
 */
public class GetLatAndLngByGaoDe {
    /**
     * @param addr
     * 百度查询的地址
     * @return
     * @throws IOException
     */
    public Object[] getCoordinate(String addr) throws IOException {
        String lng = null;//经度
        String lat = null;//纬度
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        }catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String key = "f247cdb592eb43ebac6ccd27f796e2d2";
        String url = String .format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                int count = 1;
                while((data= br.readLine())!=null){
                    if(count==5){
                        lng = (String)data.subSequence(data.indexOf(":")+1, data.indexOf(","));//经度
                        count++;
                    }else if(count==6){
                        lat = data.substring(data.indexOf(":")+1);//纬度
                        count++;
                    }else{
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new Object[]{lng,lat};
    }

    /**
     * @param address
     * 高德查询的地址
     * @return
     * @throws IOException
     */
    public static Map<String,Object> getLatitudeAndLongitude(String address) throws IOException {
        //1、通过java api从高德地图获取经纬度
        String url = "http://restapi.amap.com/v3/geocode/geo?address="+address+"&output=JSON&key="+ Constant.toReadPro("gaoDeKey");
        Map<String,Object> coordinate = new HashMap<>();
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setCompressionEnabled(true).setAllowPoolingConnection(true);
        builder.setRequestTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        builder.setIdleConnectionTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        AsyncHttpClient client = new AsyncHttpClient(builder.build());
        try {
            ListenableFuture<Response> future = client.prepareGet(url).execute();
//            String result = future.get().getResponseBody();
//            System.out.println(result);
            JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(future.get().getResponseBody());
            if(jsonNode.findValue("status").textValue().equals("1")) {
                JsonNode listSource = jsonNode.findValue("location");
                System.out.println(listSource);//经度
                String[] split = listSource.textValue().split(",");
                String longitude = new BigDecimal(split[0]).divide(new BigDecimal("0.000001"), 0, RoundingMode.HALF_UP).toString();
                String latitude = new BigDecimal(split[1]).divide(new BigDecimal("0.000001"), 0, RoundingMode.HALF_UP).toString();
                coordinate.put("longitude",longitude);
                coordinate.put("latitude",latitude);
                return coordinate;
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if(client != null){
                client.close();
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
//        GetLatAndLngByBaidu getLatAndLngByBaidu = new GetLatAndLngByBaidu();
//        Object[] o = getLatAndLngByBaidu.getCoordinate("成都市益州大道北段333号");
//        System.out.println(o[0]);//经度
//        System.out.println(o[1]);//纬度
        Map<String, Object> stringObjectMap = getLatitudeAndLongitude("成都市益州大道北段333号");
        System.out.println("stringObjectMap = " + stringObjectMap);

    }
}
