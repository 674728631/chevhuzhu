package com.zccbh.util.base;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;

import java.util.*;

public class JsonUtils {
    /**
     * 类描述：复杂json字符串转换为Map，包含数组时value为List
     * @param json
     * @return
     */
    public static Map json2Map(String json) {
        LinkedMap map = new LinkedMap();
        JSONObject js = JSONObject.fromObject(json);
        populate(js, map);
        return map;
    }

    /**
     *<p>类描述：json中的键值对解析成map。</p>
     */
    private static Map populate(JSONObject jsonObject, Map map) {
        for (Iterator iterator = jsonObject.entrySet().iterator(); iterator
                .hasNext();) {
            String entryStr = String.valueOf(iterator.next());
            String key = entryStr.substring(0, entryStr.indexOf("="));
            String value = entryStr.substring(entryStr.indexOf("=") + 1,
                    entryStr.length());
            if (jsonObject.get(key).getClass().equals(JSONObject.class)) {
                HashMap _map = new HashMap();
                map.put(key, _map);
                populate(jsonObject.getJSONObject(key), ((Map) (_map)));
            } else if (jsonObject.get(key).getClass().equals(JSONArray.class)) {
                ArrayList list = new ArrayList();
                map.put(key, list);
                populateArray(jsonObject.getJSONArray(key), list);
            } else {
                map.put(key, jsonObject.get(key));
            }
        }

        return map;
    }

    /**
     *<p>类描述：如果是键对应数组,则返回一个list到上级的map里。</p>
     */
    private static void populateArray(JSONArray jsonArray, List list) {
        for (int i = 0; i < jsonArray.size(); i++)
            if (jsonArray.get(i).getClass().equals(JSONArray.class)) {
                ArrayList _list = new ArrayList();
                list.add(_list);
                populateArray(jsonArray.getJSONArray(i), _list);
            } else if (jsonArray.get(i).getClass().equals(JSONObject.class)) {
                HashMap _map = new HashMap();
                list.add(_map);
                populate(jsonArray.getJSONObject(i), _map);
            } else {
                list.add(jsonArray.get(i));
            }
    }

    /**
     * 将json字符串转为Map结构
     * 如果json复杂，结果可能是map嵌套map
     * @param jsonStr 入参，json格式字符串
     * @return 返回一个map
     */
    public static Map<String, Object> json2Maps(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        if(jsonStr != null && !"".equals(jsonStr)){
            //最外层解析
            JSONObject json = JSONObject.fromObject(jsonStr);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                //如果内层还是数组的话，继续解析
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Iterator<JSONObject> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject json2 = it.next();
                        list.add(json2Map(json2.toString()));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }
            return map;
        }else{
            return null;
        }
    }

    public static void main(String[] args) {
        String string="{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"error_code\":0,\"result\":[{\"citynow\":{\"city\":\"成都\",\"AQI\":\"149\",\"quality\":\"轻度污染\",\"date\":\"2017-12-28 11:00\"},\"lastTwoWeeks\":{\"1\":{\"city\":\"成都\",\"AQI\":\"60\",\"quality\":\"良\",\"date\":\"2017-09-21\"},\"2\":{\"city\":\"成都\",\"AQI\":\"46\",\"quality\":\"优\",\"date\":\"2017-09-22\"},\"3\":{\"city\":\"成都\",\"AQI\":\"70\",\"quality\":\"良\",\"date\":\"2017-09-23\"},\"4\":{\"city\":\"成都\",\"AQI\":\"84\",\"quality\":\"良\",\"date\":\"2017-09-24\"},\"5\":{\"city\":\"成都\",\"AQI\":\"87\",\"quality\":\"良\",\"date\":\"2017-09-25\"},\"6\":{\"city\":\"成都\",\"AQI\":\"32\",\"quality\":\"优\",\"date\":\"2017-09-26\"},\"7\":{\"city\":\"成都\",\"AQI\":\"37\",\"quality\":\"优\",\"date\":\"2017-09-27\"},\"8\":{\"city\":\"成都\",\"AQI\":\"43\",\"quality\":\"优\",\"date\":\"2017-09-28\"},\"9\":{\"city\":\"成都\",\"AQI\":\"63\",\"quality\":\"良\",\"date\":\"2017-09-29\"},\"10\":{\"city\":\"成都\",\"AQI\":\"66\",\"quality\":\"良\",\"date\":\"2017-09-30\"},\"11\":{\"city\":\"成都\",\"AQI\":\"60\",\"quality\":\"良\",\"date\":\"2017-10-01\"},\"12\":{\"city\":\"成都\",\"AQI\":\"38\",\"quality\":\"优\",\"date\":\"2017-10-02\"},\"13\":{\"city\":\"成都\",\"AQI\":\"18\",\"quality\":\"优\",\"date\":\"2017-10-03\"},\"14\":{\"city\":\"成都\",\"AQI\":\"32\",\"quality\":\"优\",\"date\":\"2017-10-04\"},\"15\":{\"city\":\"成都\",\"AQI\":\"45\",\"quality\":\"优\",\"date\":\"2017-10-05\"},\"16\":{\"city\":\"成都\",\"AQI\":\"60\",\"quality\":\"良\",\"date\":\"2017-10-06\"},\"17\":{\"city\":\"成都\",\"AQI\":\"60\",\"quality\":\"良\",\"date\":\"2017-10-07\"},\"18\":{\"city\":\"成都\",\"AQI\":\"88\",\"quality\":\"良\",\"date\":\"2017-10-08\"},\"19\":{\"city\":\"成都\",\"AQI\":\"15\",\"quality\":\"优\",\"date\":\"2017-10-09\"},\"20\":{\"city\":\"成都\",\"AQI\":\"31\",\"quality\":\"优\",\"date\":\"2017-10-10\"},\"21\":{\"city\":\"成都\",\"AQI\":\"60\",\"quality\":\"良\",\"date\":\"2017-10-11\"},\"22\":{\"city\":\"成都\",\"AQI\":\"46\",\"quality\":\"优\",\"date\":\"2017-10-12\"},\"23\":{\"city\":\"成都\",\"AQI\":\"55\",\"quality\":\"良\",\"date\":\"2017-10-13\"},\"24\":{\"city\":\"成都\",\"AQI\":\"26\",\"quality\":\"优\",\"date\":\"2017-10-14\"},\"25\":{\"city\":\"成都\",\"AQI\":\"22\",\"quality\":\"优\",\"date\":\"2017-10-15\"},\"26\":{\"city\":\"成都\",\"AQI\":\"25\",\"quality\":\"优\",\"date\":\"2017-10-16\"},\"27\":{\"city\":\"成都\",\"AQI\":\"49\",\"quality\":\"优\",\"date\":\"2017-10-17\"},\"28\":{\"city\":\"成都\",\"AQI\":\"71\",\"quality\":\"良\",\"date\":\"2017-10-18\"}},\"lastMoniData\":{\"1\":{\"city\":\"金泉两河\",\"AQI\":\"143\",\"America_AQI\":\"175\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"109\",\"PM2.5Day\":\"109\",\"PM10Hour\":\"143\",\"lat\":\"30.713\",\"lon\":\"103.972\"},\"2\":{\"city\":\"十里店\",\"AQI\":\"147\",\"America_AQI\":\"177\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"112\",\"PM2.5Day\":\"112\",\"PM10Hour\":\"163\",\"lat\":\"30.6775\",\"lon\":\"104.140833\"},\"3\":{\"city\":\"三瓦窑\",\"AQI\":\"204\",\"America_AQI\":\"204\",\"quality\":\"重度污染\",\"PM2.5Hour\":\"154\",\"PM2.5Day\":\"154\",\"PM10Hour\":\"207\",\"lat\":\"30.576389\",\"lon\":\"104.059444\"},\"4\":{\"city\":\"沙河铺\",\"AQI\":\"188\",\"America_AQI\":\"194\",\"quality\":\"中度污染\",\"PM2.5Hour\":\"141\",\"PM2.5Day\":\"141\",\"PM10Hour\":\"197\",\"lat\":\"30.636111\",\"lon\":\"104.117778\"},\"5\":{\"city\":\"君平街\",\"AQI\":\"172\",\"America_AQI\":\"188\",\"quality\":\"中度污染\",\"PM2.5Hour\":\"130\",\"PM2.5Day\":\"130\",\"PM10Hour\":\"195\",\"lat\":\"\",\"lon\":\"\"},\"6\":{\"city\":\"灵岩寺\",\"AQI\":\"139\",\"America_AQI\":\"174\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"106\",\"PM2.5Day\":\"106\",\"PM10Hour\":\"145\",\"lat\":\"\",\"lon\":\"\"},\"7\":{\"city\":\"大石西路\",\"AQI\":\"199\",\"America_AQI\":\"199\",\"quality\":\"中度污染\",\"PM2.5Hour\":\"149\",\"PM2.5Day\":\"149\",\"PM10Hour\":\"205\",\"lat\":\"\",\"lon\":\"\"},\"8\":{\"city\":\"美国领事馆\",\"AQI\":\"147\",\"America_AQI\":\"--\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"112\",\"PM2.5Day\":\"112\",\"PM10Hour\":\"163\",\"lat\":\"30.6249965160\",\"lon\":\"104.0687606677\"}}}]}\n";
        Map map = json2Map(string);
        List<String> result = ( List<String> ) map.get("result");
        System.out.println("result = " + result);

        System.out.println(result);
        String jsonp ="{\n" +
                "\t\"insureAreaCode\": \"投保地区代码(市级代码)\",\n" +
                "\t\"channelUserId\": \"渠道用户ID\",\n" +
                "\t\"remark\": \"备注\",\n" +
                "\t\"carInfo\": {  //车辆信息\n" +
                "\t\t\"isNew\": \"是否未上牌\",\n" +
                "\t\t\"price\": \"新车发票价\", \n" +
                "\t\t\"carLicenseNo\": \"车牌号\",\n" +
                "\t\t\"vinCode\": \"车架号\",\n" +
                "\t\t\"engineNo\": \"发动机号\",\n" +
                "\t\t\"carProperty\": \"车辆使用性质\",\n" +
                "\t\t\"property\": \"车辆所属性质\",\n" +
                "\t\t\"registDate\": \"初登日期\",\n" +
                "\t\t\"isTransferCar\": \"是否过户车\",\n" +
                "\t\t\"transferDate\": \"过户日期\",\n" +
                "\t\t\"vehicleName\": \"车型名称\",\n" +
                "\t\t\"vehicleId\": \"车型id\",\n" +
                "\t\t\"modelLoads\": \"核定载重量(货车必填)\",\n" +
                "\t\t\"purpose\": \"车辆用途\",\n" +
                "\t\t\"seat\": \"核定载客人数\",\n" +
                "\t\t\"fullWeight\": \"整备质量\",\n" +
                "\t\t\"drivingArea\": \"行驶区域\"\n" +
                "\t},\n" +
                "\t\"carOwner\": { \n" +
                "\t\t\"name\": \"姓名\",\n" +
                "\t\t\"idcardType\": \"证件类型\",\n" +
                "\t\t\"idcardNo\": \"证件号\",\n" +
                "\t\t\"phone\": \"手机号码\",\n" +
                "        \"email\": \"电子邮箱\"\n" +
                "\t},\n" +
                "\"applicant\": {\n" +
                "        \"name\": \"姓名\",\n" +
                "        \"idcardType\": \"证件类型\",\n" +
                "        \"idcardNo\": \"证件号\",\n" +
                "        \"phone\": \"手机号码\",\n" +
                "        \"email\": \"电子邮箱\"\n" +
                "    },\n" +
                "\"insured\": {\n" +
                "\"name\": \"姓名\",\n" +
                "\"idcardType\": \"证件类型\",\n" +
                "\"idcardNo\": \"证件号\",\n" +
                "\"phone\": \"手机号码\",\n" +
                "\"email\": \"电子邮箱\"\n" +
                "},\n" +
                "\t\"insureInfo\": {\n" +
                "\t\t\"efcInsureInfo\": {  // 交强险\n" +
                "\t\t\t\"startDate\": \"2016-07-02\", \n" +
                "\t\t\t\"endDate\": \"2016-07-01\"\n" +
                "\t\t},\n" +
                "\t\t\"taxInsureInfo\": {  // 车船税\n" +
                "\t\t\t\"isPaymentTax\": \"是否代缴车船税\"\n" +
                "\t\t},\n" +
                "\t\t\"bizInsureInfo\": {  // 商业险\n" +
                "\t\t\t\"startDate\": \"2016-07-02\",\n" +
                "\t\t\t\"endDate\": \"2016-07-01\",\n" +
                "\t\t\t\"riskKinds\": [{  // 险种信息\n" +
                "\t\t\t\t\"riskCode\": \"险种代码\",\n" +
                "\t\t\t\t\"amount\": \"险种保额\",\n" +
                "\t\t\t\t\"notDeductible\": \"是否投保不计免赔\"\n" +
                "\t\t\t}]\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"providers\": [{\n" +
                "\t\t\"prvId\": \"供应商id\"\n" +
                "\t}]\n" +
                "}";
        Map<String, Object> stringObjectMap = json2Maps(string);
        String resultcode = (String) stringObjectMap.get("resultcode");
        if (resultcode.equals("200")) {
            List<Map<String, Object>> result1 = (List<Map<String, Object>>) stringObjectMap.get("result");
            Map<String, Object> citynow = (Map<String, Object>) result1.get(0).get("citynow");
            Object aqi = citynow.get("AQI");
            System.out.println("aqi = " + aqi);
            System.out.println("citynow = " + citynow);
        }
        Map<String, Object> stringObjectMap1 = json2Maps(jsonp);
        Map<String, Object> insured= (Map<String, Object>) stringObjectMap1.get("insured");
        System.out.println("insured = " + insured);
    }
}
