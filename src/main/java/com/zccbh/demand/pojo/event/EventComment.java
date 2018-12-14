package com.zccbh.demand.pojo.event;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              互助事件评价详情
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class EventComment extends BaseModel {
    private static final long serialVersionUID = 2799935852466283115L;

    /**
     * 车主用户id
     */
    private Integer customerId;
    /**
     * 修理厂id
     */
    private Integer maintenanceshopId;
    /**
     * 互助事件号
     */
    private String eventNo;
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
    private Date createAt;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getMaintenanceshopId() {
        return maintenanceshopId;
    }

    public void setMaintenanceshopId(Integer maintenanceshopId) {
        this.maintenanceshopId = maintenanceshopId;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "EventComment{" +
                "customerId=" + customerId +
                ", maintenanceshopId=" + maintenanceshopId +
                ", eventNo='" + eventNo + '\'' +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", createAt=" + createAt +
                '}';
    }
}
