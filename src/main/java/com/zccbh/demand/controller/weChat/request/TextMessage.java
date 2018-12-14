package com.zccbh.demand.controller.weChat.request;

/** 
 * 文本消息 
 *
 * @author luoyuangang
 * @date 2017/12/13
 */  
public class TextMessage extends BaseMessage {  
    // 消息内容  
    private String Content;  
  
    public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }  
} 
