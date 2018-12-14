package com.zccbh.demand.pojo.user;

import com.zccbh.demand.pojo.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Comments:              充值记录表
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class RecordRecharge extends BaseModel {
    private static final long serialVersionUID = -7294448884019723715L;

    /**
     * 车主用户id
     */
    private Integer customerId;
    /**
     * 车辆id
     */
    private Integer carId;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 描述
     */
    private String description;
    /**
     * 充值方式（微信，支付宝）
     */
    private Integer type;
    /**
     * 充值时间
     */
    private Date timeRecharge;
    /**
     * 状态(是否到账) 1.到账 2.没有到账
     */
    private Integer status;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getTimeRecharge() {
        return timeRecharge;
    }

    public void setTimeRecharge(Date timeRecharge) {
        this.timeRecharge = timeRecharge;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RecordRecharge{" +
                "customerId=" + customerId +
                ", carId=" + carId +
                ", amt=" + amt +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", timeRecharge=" + timeRecharge +
                ", status=" + status +
                '}';
    }
}
