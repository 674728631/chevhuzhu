package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cbh_event_comment")
public class CbhEventComment {
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
     * 修理厂id
     */
    @Column(name = "maintenanceshopId")
    private Integer maintenanceshopId;

    /**
     * 互助事件号
     */
    @Column(name = "eventNo")
    private String eventNo;

    /**
     * 互助事件号
     */
    @Column(name = "labelContent")
    private String labelContent;

    /**
     * 内容
     */
    private String content;

    /**
     * 星级
     */
    private Integer score;

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
     * 获取修理厂id
     *
     * @return maintenanceshopId - 修理厂id
     */
    public Integer getMaintenanceshopId() {
        return maintenanceshopId;
    }

    /**
     * 设置修理厂id
     *
     * @param maintenanceshopId 修理厂id
     */
    public void setMaintenanceshopId(Integer maintenanceshopId) {
        this.maintenanceshopId = maintenanceshopId;
    }

    /**
     * 获取互助事件号
     *
     * @return eventNo - 互助事件号
     */
    public String getEventNo() {
        return eventNo;
    }

    /**
     * 设置互助事件号
     *
     * @param eventNo 互助事件号
     */
    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
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
     * 获取星级
     *
     * @return score - 星级
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 设置星级
     *
     * @param score 星级
     */
    public void setScore(Integer score) {
        this.score = score;
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

    public String getLabelContent() {
        return labelContent;
    }

    public void setLabelContent(String labelContent) {
        this.labelContent = labelContent;
    }
}