package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cbh_message")
public class CbhMessage {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 车主用户id
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 维修厂用户id
     */
    @Column(name = "businessId")
    private Integer businessId;

    /**
     * 车牌号
     */
    @Column(name = "licensePlateNumber")
    private String licensePlateNumber;

    /**
     * 互助事件编号
     */
    @Column(name = "eventNo")
    private String eventNo;

    /**
     * 互助事件编号
     */
    private String model;

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
    @Column(name = "isRead")
    private Integer isRead=1;

    /**
     * 倒计时
     */
    @Column(insertable = false)
    private Integer countdown;

    /**
     * 服务分
     */
    private Double score;

    /**
     * 创建时间
     */
    @Column(name = "createAt")
    private Date createAt= DateUtils.getDateTime();

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取车主用户id
     *
     * @return customerId - 车主用户id
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * 设置车主用户id
     *
     * @param customerId 车主用户id
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取互助事件编号
     *
     * @return eventNo - 互助事件编号
     */
    public String getEventNo() {
        return eventNo;
    }

    /**
     * 设置互助事件编号
     *
     * @param eventNo 互助事件编号
     */
    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    /**
     * 获取消息类型: 1系统消息 3余额变动  5我的互助
     *
     * @return type - 消息类型: 1系统消息 3余额变动  5我的互助
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置消息类型: 1系统消息 3余额变动  5我的互助
     *
     * @param type 消息类型: 1系统消息 3余额变动  5我的互助
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取图片
     *
     * @return img - 图片
     */
    public String getImg() {
        return img;
    }

    /**
     * 设置图片
     *
     * @param img 图片
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * 获取链接
     *
     * @return chaining - 链接
     */
    public String getChaining() {
        return chaining;
    }

    /**
     * 设置链接
     *
     * @param chaining 链接
     */
    public void setChaining(String chaining) {
        this.chaining = chaining;
    }

    /**
     * 获取状态 1.未读 3已读
     *
     * @return isRead - 状态 1.未读 3已读
     */
    public Integer getIsRead() {
        return isRead;
    }

    /**
     * 设置状态 1.未读 3已读
     *
     * @param isRead 状态 1.未读 3已读
     */
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    /**
     * 获取创建时间
     *
     * @return createAt - 创建时间
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置创建时间
     *
     * @param createAt 创建时间
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getCountdown() {
        return countdown;
    }

    public void setCountdown(Integer countdown) {
        this.countdown = countdown;
    }
}