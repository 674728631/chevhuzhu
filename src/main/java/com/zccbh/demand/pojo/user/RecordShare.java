package com.zccbh.demand.pojo.user;

import com.zccbh.demand.pojo.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Comments:              分摊记录表
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class RecordShare extends BaseModel {
    private static final long serialVersionUID = -1687425533072015074L;

    /**
     * 互助事件号
     */
    private String eventNo;
    /**
     * 互助金额
     */
    private BigDecimal amtCooperation;
    /**
     * 分摊金额
     */
    private BigDecimal amtShare;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createAt;

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public BigDecimal getAmtCooperation() {
        return amtCooperation;
    }

    public void setAmtCooperation(BigDecimal amtCooperation) {
        this.amtCooperation = amtCooperation;
    }

    public BigDecimal getAmtShare() {
        return amtShare;
    }

    public void setAmtShare(BigDecimal amtShare) {
        this.amtShare = amtShare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "RecordShare{" +
                "eventNo='" + eventNo + '\'' +
                ", amtCooperation=" + amtCooperation +
                ", amtShare=" + amtShare +
                ", description='" + description + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
