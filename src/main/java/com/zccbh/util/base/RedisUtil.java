package com.zccbh.util.base;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;//redis操作模板

    /** 订单单号的Redis Key */
    private static final String ORDER_NO_GENERATOR = "ORDER_NO_GENERATOR";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 获取订单单号
     * @return
     */
    public  String  getOrderNo() {
        String time = LocalDateTime.now().format(FORMATTER);
        String autoIncrement = String.format("%05d", this.incrementAndExpire(ORDER_NO_GENERATOR));
        return time + autoIncrement;
    }

    private int incrementAndExpire(String key) {
        //每次从1开始自增
        int i = redisTemplate.opsForValue().increment(key, 1).intValue();
        //获取第二天的0点
        Instant instant = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        //如果每次都是每天第一次获取到的自增1，就设置过期时间是第二天零点
        if (i == 1) {
            redisTemplate.expireAt(key, date);
        }
        return i;
    }

    public void put(String key, String value) {
        if (key==null || "".equals(key))
            return;
        redisTemplate.opsForHash().put(key, key, value);
    }

    public void put(String key, Object value) {
        if (key==null || "".equals(key))
            return;
        redisTemplate.opsForHash().put(key, key, new Gson().toJson(value));
    }

    public <T> T get(String key, Class<T> className) {
        Object obj = redisTemplate.opsForHash().get(key, key);
        if(obj == null)
            return null;
        return new Gson().fromJson(""+obj, className);
    }

    public String get(String key) {
        Object obj = redisTemplate.opsForHash().get(key, key);
        if(obj == null)
            return null;
        else
            return String.valueOf(obj);
    }

    /**
     * 短信缓存
     * @author fxl
     * @date 2016年9月11日
     * @param key
     * @param value
     * @param time
     */
    public  void setIntForPhone(String key,Object value,int time){
        redisTemplate.opsForValue().set(key, JsonMapper.toJsonString(value));
        if(time > 0){
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }
    /**
     * 取得缓存（字符串类型）
     * @param key
     * @return
     */
    public  String getStr(String key){
        String string = redisTemplate.boundValueOps(key).get();
            return StringUtils.isNotBlank(string)?string.replaceAll("\"",""):null;

    }

    /**
     * 取得缓存（字符串类型）没有就删除key
     * @param key
     * @return
     */
    public  String getStr(String key, boolean retain){
        String value = redisTemplate.boundValueOps(key).get();
        if(!retain){
            redisTemplate.delete(key);
        }
        return value;
    }

    /**
     * 获取缓存json对象<br>
     * @param key   key
     * @param clazz 类型
     * @return
     */
    public  <T> T getJson(String key, Class<T> clazz) {
        return JsonMapper.fromJsonString(redisTemplate.boundValueOps(key).get(), clazz);
    }

    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public  void set(String key,Object value,int time){
        if(value.getClass().equals(String.class)){
            redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Integer.class)){
            redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Double.class)){
            redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Float.class)){
            redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Short.class)){
            redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Long.class)){
            redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Boolean.class)){
            redisTemplate.opsForValue().set(key, value.toString());
        }
        if(time > 0){
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }
    /**
     * 获取double类型值
     * @param key
     * @return
     */
    public  double getDouble(String key) {
        String value = redisTemplate.boundValueOps(key).get();
        if(StringUtils.isNotBlank(value)){
            return Double.valueOf(value);
        }
        return 0d;
    }

    /**
     * 设置double类型值
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public  void setDouble(String key, double value, int time) {
        redisTemplate.opsForValue().set(key, String.valueOf(value));
        if(time > 0){
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置double类型值
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public  void setInt(String key, int value, int time) {
        redisTemplate.opsForValue().set(key, String.valueOf(value));
        if(time > 0){
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    public void putAndTime(String key, Object value,int time) {
        if (key==null || "".equals(key))
            return;
        redisTemplate.opsForHash().put(key, key, new Gson().toJson(value));
        if(time > 0){
            redisTemplate.expire(key, time, TimeUnit.SECONDS);

        }
    }

    public void putAndTime(String key, Object value,int time,TimeUnit unit) {
        if (key==null || "".equals(key))
            return;
        redisTemplate.opsForHash().put(key, key, new Gson().toJson(value));
        if(time > 0){
            redisTemplate.expire(key, time, unit);
        }
    }

    /**
     *
     * @param key  删除key
     */
    public  void delect(String key) {
        redisTemplate.delete(key);
    }
    /**
     *
     * @param key  获取key的有效期
     */
    public  void getExpire(String key) {
        Long expire = redisTemplate.getExpire(key);
        System.out.println("expire = " + expire);
    }

}