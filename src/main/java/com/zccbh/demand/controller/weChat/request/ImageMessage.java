package com.zccbh.demand.controller.weChat.request;

/** 
 * 图片消息 
 *
 * @author luoyuangang
 * @date 2017/12/13
 */  
public class ImageMessage extends BaseMessage {  
    // 图片链接  
    private String PicUrl;  
  
    public String getPicUrl() {  
        return PicUrl;  
    }  
  
    public void setPicUrl(String picUrl) {  
        PicUrl = picUrl;  
    }  
} 
