package com.zccbh.util.base;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map工具类，链式调用生成map -->  MapUtil.build.put("", "").put("", "").putAll(map).over();
 */
public class MapUtil {

    private Map<String, Object> map;

    private MapUtil() {

    }

    public static MapUtil build() {
        MapUtil instance = new MapUtil();
        instance.map = new HashMap<>();
        return instance;
    }

    public static MapUtil build(Map<String, Object> map) {
        MapUtil instance = new MapUtil();
        instance.map = map;
        return instance;
    }

    public static MapUtil buildLink() {
        MapUtil instance = new MapUtil();
        instance.map = new LinkedHashMap<>();
        return instance;
    }

    public MapUtil put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public MapUtil putAll(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    public Map<String, Object> over() {
        return this.map;
    }

    public static void main(String[] args) {
        Map<String, Object> map = MapUtil.build().put("a", "11").put("b", "11").over();
        System.out.println(map);
    }
    
    /**
     * 使用 Map按key进行排序得到key=value的字符串
     * @param plain
     * @param eqaulsType K与V之间的拼接字符串 = 或者其他...
     * @param spliceType K-V与K-V之间的拼接字符串  & 或者|...
     * @return
     */
    public static Map<String, Object> stringToMap(String plain, String eqaulsType,
                                                  String spliceType) {
        if (plain == null || plain.isEmpty()) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        String[] split = plain.split(spliceType);
        for (String kv : split) {
            if ("|".equals(kv)) {
                continue;
            }
            String[] kvArr = kv.split(eqaulsType);
            if (kvArr.length == 2) {
                map.put(kvArr[0], kvArr[1]);
            } else {
                map.put(kvArr[0], "");
            }
        }

        return map;
    }

}
