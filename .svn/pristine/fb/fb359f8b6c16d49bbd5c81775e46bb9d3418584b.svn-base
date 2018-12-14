package com.zccbh.demand.controller.weChat.response;

/** 
 * 文本消息 
 * @author luoyuangang
 * @date 2017/12/13
 */  
public class TextMessage extends ResponseBaseMessage {
    // 接收方帐号（收到的OpenID）
    private String ToUserName;
    // 开发者微信号
    private String FromUserName;
    // 消息创建时间 （整型）
    private long CreateTime;
    // 消息类型（text/music/news）
    private String MsgType;
    // 位0x0001被标志时，星标刚收到的消息
    private int FuncFlag;
    // 回复的消息内容  
    private String Content;  
  
    public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }

    @Override
    public String getToUserName() {
        return ToUserName;
    }

    @Override
    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    @Override
    public String getFromUserName() {
        return FromUserName;
    }

    @Override
    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    @Override
    public long getCreateTime() {
        return CreateTime;
    }

    @Override
    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    @Override
    public String getMsgType() {
        return MsgType;
    }

    @Override
    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    @Override
    public int getFuncFlag() {
        return FuncFlag;
    }

    @Override
    public void setFuncFlag(int funcFlag) {
        FuncFlag = funcFlag;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "Content='" + Content + '\'' +
                '}';
    }
}
