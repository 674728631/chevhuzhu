package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cbh_event_apply")
public class CbhEventApply {
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
     * 事故描述
     */
    @Column(name = "accidentDescription")
    private String accidentDescription;

    /**
     * 事故照片
     */
    @Column(name = "accidentImg")
    private String accidentImg;

    /**
     * 申请理赔时间
     */
    @Column(name = "timeApply")
    private Date timeApply;

    /**
     * 审核时间
     */
    @Column(name = "timeExamine")
    private Date timeExamine;

    /**
     * 审核失败原因
     */
    @Column(name = "reasonFailure")
    private String reasonFailure;

    /**
     * 创建时间
     */
    @Column(name = "createAt")
    private Date createAt;

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
     * 获取事故描述
     *
     * @return accidentDescription - 事故描述
     */
    public String getAccidentDescription() {
        return accidentDescription;
    }

    /**
     * 设置事故描述
     *
     * @param accidentDescription 事故描述
     */
    public void setAccidentDescription(String accidentDescription) {
        this.accidentDescription = accidentDescription;
    }

    /**
     * 获取事故照片
     *
     * @return accidentImg - 事故照片
     */
    public String getAccidentImg() {
        return accidentImg;
    }

    /**
     * 设置事故照片
     *
     * @param accidentImg 事故照片
     */
    public void setAccidentImg(String accidentImg) {
        this.accidentImg = accidentImg;
    }

    /**
     * 获取申请理赔时间
     *
     * @return timeApply - 申请理赔时间
     */
    public Date getTimeApply() {
        return timeApply;
    }

    /**
     * 设置申请理赔时间
     *
     * @param timeApply 申请理赔时间
     */
    public void setTimeApply(Date timeApply) {
        this.timeApply = timeApply;
    }

    /**
     * 获取审核时间
     *
     * @return timeExamine - 审核时间
     */
    public Date getTimeExamine() {
        return timeExamine;
    }

    /**
     * 设置审核时间
     *
     * @param timeExamine 审核时间
     */
    public void setTimeExamine(Date timeExamine) {
        this.timeExamine = timeExamine;
    }

    /**
     * 获取审核失败原因
     *
     * @return reasonFailure - 审核失败原因
     */
    public String getReasonFailure() {
        return reasonFailure;
    }

    /**
     * 设置审核失败原因
     *
     * @param reasonFailure 审核失败原因
     */
    public void setReasonFailure(String reasonFailure) {
        this.reasonFailure = reasonFailure;
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