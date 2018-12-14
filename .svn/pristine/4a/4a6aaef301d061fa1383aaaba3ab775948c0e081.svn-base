package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cbh_event_assert")
public class CbhEventAssert {
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
     * 定损人(修理厂员工id)
     */
    @Column(name = "maintenanceshopEmployeeId")
    private Integer maintenanceshopEmployeeId;

    /**
     * 受损程度
     */
    @Column(name = "damageExtent")
    private String damageExtent;

    /**
     * 受损部位
     */
    @Column(name = "damagePosition")
    private String damagePosition;

    /**
     * 定损照片
     */
    @Column(name = "assertImg")
    private String assertImg;

    /**
     * 定损描述
     */
    private String description;

    /**
     * 定损费用
     */
    @Column(name = "amtAssert")
    private BigDecimal amtAssert;

    /**
     * 定损时间
     */
    @Column(name = "timeAssert")
    private Date timeAssert;

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
     * 获取定损人(修理厂员工id)
     *
     * @return maintenanceshopEmployeeId - 定损人(修理厂员工id)
     */
    public Integer getMaintenanceshopEmployeeId() {
        return maintenanceshopEmployeeId;
    }

    /**
     * 设置定损人(修理厂员工id)
     *
     * @param maintenanceshopEmployeeId 定损人(修理厂员工id)
     */
    public void setMaintenanceshopEmployeeId(Integer maintenanceshopEmployeeId) {
        this.maintenanceshopEmployeeId = maintenanceshopEmployeeId;
    }

    /**
     * 获取定损描述
     *
     * @return description - 定损描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置定损描述
     *
     * @param description 定损描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取定损费用
     *
     * @return amtAssert - 定损费用
     */
    public BigDecimal getAmtAssert() {
        return amtAssert;
    }

    /**
     * 设置定损费用
     *
     * @param amtAssert 定损费用
     */
    public void setAmtAssert(BigDecimal amtAssert) {
        this.amtAssert = amtAssert;
    }

    /**
     * 获取定损时间
     *
     * @return timeAssert - 定损时间
     */
    public Date getTimeAssert() {
        return timeAssert;
    }

    /**
     * 设置定损时间
     *
     * @param timeAssert 定损时间
     */
    public void setTimeAssert(Date timeAssert) {
        this.timeAssert = timeAssert;
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

    public String getDamageExtent() {
        return damageExtent;
    }

    public void setDamageExtent(String damageExtent) {
        this.damageExtent = damageExtent;
    }

    public String getDamagePosition() {
        return damagePosition;
    }

    public void setDamagePosition(String damagePosition) {
        this.damagePosition = damagePosition;
    }

    public String getAssertImg() {
        return assertImg;
    }

    public void setAssertImg(String assertImg) {
        this.assertImg = assertImg;
    }

}