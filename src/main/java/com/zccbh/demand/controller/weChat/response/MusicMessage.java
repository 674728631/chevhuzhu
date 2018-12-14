package com.zccbh.demand.controller.weChat.response;

/** 
* 音乐消息 
*
 * @author luoyuangang
 * @date 2017/12/13
*/  
public class MusicMessage extends ResponseBaseMessage {
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
    // 音乐
    private com.zccbh.demand.controller.weChat.response.Music Music;

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

    public com.zccbh.demand.controller.weChat.response.Music getMusic() {
      return Music;
    }

    public void setMusic(com.zccbh.demand.controller.weChat.response.Music music) {
      Music = music;
    }
} 
