package com.zccbh.demand.pojo.user;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              消息通知
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Message extends BaseModel {
    private static final long serialVersionUID = 3765713975366709014L;

    /**
     * 车主用户id
     */
    private Integer customerId;
    /**
     * 互助事件编号
     */
    private String eventNo;
    /**
     * 消息类型: 1系统消息 3余额变动  5我的互助
     */
    private Integer type;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 图片
     */
    private String img;
    /**
     * 链接
     */
    private String chaining;
    /**
     * 状态 1.未读 3已读
     */
    private Integer isRead;
    /**
     * 创建时间
     */
    private Date createAt;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getChaining() {
        return chaining;
    }

    public void setChaining(String chaining) {
        this.chaining = chaining;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "customerId=" + customerId +
                ", eventNo='" + eventNo + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", chaining='" + chaining + '\'' +
                ", isRead=" + isRead +
                ", createAt=" + createAt +
                '}';
    }
}
