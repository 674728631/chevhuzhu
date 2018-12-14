package com.zccbh.demand.pojo.event;

import com.zccbh.demand.pojo.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Comments:              互助事件定损详情
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class EventAssert extends BaseModel {
    private static final long serialVersionUID = -7264897916079443539L;

    /**
     * 互助事件号
     */
    private String eventNo;
    /**
     * 定损人(修理厂员工id)
     */
    private Integer maintenanceshopEmployeeId;
    /**
     * 定损描述
     */
    private String description;
    /**
     * 定损费用
     */
    private BigDecimal amtAssert;
    /**
     * 定损时间
     */
    private Date timeAssert;

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public Integer getMaintenanceshopEmployeeId() {
        return maintenanceshopEmployeeId;
    }

    public void setMaintenanceshopEmployeeId(Integer maintenanceshopEmployeeId) {
        this.maintenanceshopEmployeeId = maintenanceshopEmployeeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public BigDecimal getAmtAssert() {
        return amtAssert;
    }

    public void setAmtAssert(BigDecimal amtAssert) {
        this.amtAssert = amtAssert;
    }

    public Date getTimeAssert() {
        return timeAssert;
    }

    public void setTimeAssert(Date timeAssert) {
        this.timeAssert = timeAssert;
    }

    @Override
    public String toString() {
        return "EventAssert{" +
                "eventNo='" + eventNo + '\'' +
                ", maintenanceshopEmployeeId=" + maintenanceshopEmployeeId +
                ", description='" + description + '\'' +
                ", amtAssert=" + amtAssert +
                ", timeAssert=" + timeAssert +
                '}';
    }
}
