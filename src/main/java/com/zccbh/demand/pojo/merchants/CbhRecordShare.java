package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cbh_record_share")
public class CbhRecordShare {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 互助事件号
     */
    @Column(name = "eventNo")
    private String eventNo;

    /**
     * 互助金额
     */
    @Column(name = "amtCooperation")
    private BigDecimal amtCooperation;

    /**
     * 分摊金额
     */
    @Column(name = "amtShare")
    private BigDecimal amtShare;

    /**
     * 描述
     */
    private String description;

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
     * 获取互助金额
     *
     * @return amtCooperation - 互助金额
     */
    public BigDecimal getAmtCooperation() {
        return amtCooperation;
    }

    /**
     * 设置互助金额
     *
     * @param amtCooperation 互助金额
     */
    public void setAmtCooperation(BigDecimal amtCooperation) {
        this.amtCooperation = amtCooperation;
    }

    /**
     * 获取分摊金额
     *
     * @return amtShare - 分摊金额
     */
    public BigDecimal getAmtShare() {
        return amtShare;
    }

    /**
     * 设置分摊金额
     *
     * @param amtShare 分摊金额
     */
    public void setAmtShare(BigDecimal amtShare) {
        this.amtShare = amtShare;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
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
}