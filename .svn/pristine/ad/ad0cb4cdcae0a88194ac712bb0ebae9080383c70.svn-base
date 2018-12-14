package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cbh_event")
public class CbhEvent {
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
     * 车主用户id
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 车辆id
     */
    @Column(name = "carId")
    private Integer carId;

    /**
     * 维修厂id
     */
    @Column(name = "maintenanceshopId")
    private Integer maintenanceshopId;

    /**
     * 支付时间
     */
    @Column(name = "timePay")
    private Date timePay;

    /**
     * 支付金额
     */
    @Column(name = "amtPay")
    private BigDecimal amtPay;

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
     * 接单时间
     */
    @Column(name = "timeReceiveOrder")
    private Date timeReceiveOrder;

    /**
     * 互助事件完成时间
     */
    @Column(name = "timeComplete")
    private Date timeComplete;

    /**
     * 事件状态（1.待审核 11.待接单 21.待定损 31.待支付 41.待交车 51.待维修 61.待接车 71.待评价 81.投诉中 100.已完成）
     */
    @Column(name = "statusEvent")
    private Integer statusEvent;
    /**
     * 撤单原因
     */
    @Column(name = "cancellationsReason")
    private String cancellationsReason;
    /**
     * 撤单说明
     */
    @Column(name = "cancellationsInstructions")
    private String cancellationsInstructions;
    /**
     * 创建时间
     */
    @Column(name = "createAt")
    private Date createAt= DateUtils.getDateTime();

    /**
     * 放弃接单时间
     */
    @Column(name = "failReceiveOrderTime")
    private Date failReceiveOrderTime;

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
     * 获取车辆id
     *
     * @return carId - 车辆id
     */
    public Integer getCarId() {
        return carId;
    }

    /**
     * 设置车辆id
     *
     * @param carId 车辆id
     */
    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    /**
     * 获取维修厂id
     *
     * @return maintenanceshopId - 维修厂id
     */
    public Integer getMaintenanceshopId() {
        return maintenanceshopId;
    }

    /**
     * 设置维修厂id
     *
     * @param maintenanceshopId 维修厂id
     */
    public void setMaintenanceshopId(Integer maintenanceshopId) {
        this.maintenanceshopId = maintenanceshopId;
    }

    /**
     * 获取支付时间
     *
     * @return timePay - 支付时间
     */
    public Date getTimePay() {
        return timePay;
    }

    /**
     * 设置支付时间
     *
     * @param timePay 支付时间
     */
    public void setTimePay(Date timePay) {
        this.timePay = timePay;
    }

    /**
     * 获取支付金额
     *
     * @return amtPay - 支付金额
     */
    public BigDecimal getAmtPay() {
        return amtPay;
    }

    /**
     * 设置支付金额
     *
     * @param amtPay 支付金额
     */
    public void setAmtPay(BigDecimal amtPay) {
        this.amtPay = amtPay;
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
     * 获取接单时间
     *
     * @return timeReceiveOrder - 接单时间
     */
    public Date getTimeReceiveOrder() {
        return timeReceiveOrder;
    }

    /**
     * 设置接单时间
     *
     * @param timeReceiveOrder 接单时间
     */
    public void setTimeReceiveOrder(Date timeReceiveOrder) {
        this.timeReceiveOrder = timeReceiveOrder;
    }

    /**
     * 获取互助事件完成时间
     *
     * @return timeComplete - 互助事件完成时间
     */
    public Date getTimeComplete() {
        return timeComplete;
    }

    /**
     * 设置互助事件完成时间
     *
     * @param timeComplete 互助事件完成时间
     */
    public void setTimeComplete(Date timeComplete) {
        this.timeComplete = timeComplete;
    }

    /**
     * 获取事件状态（1.待审核 11.待接单 21.待定损 31.待支付 41.待交车 51.待维修 61.待接车 71.待评价 81.投诉中 100.已完成）
     *
     * @return statusEvent - 事件状态（1.待审核 11.待接单 21.待定损 31.待支付 41.待交车 51.待维修 61.待接车 71.待评价 81.投诉中 100.已完成）
     */
    public Integer getStatusEvent() {
        return statusEvent;
    }

    /**
     * 设置事件状态（1.待审核 11.待接单 21.待定损 31.待支付 41.待交车 51.待维修 61.待接车 71.待评价 81.投诉中 100.已完成）
     *
     * @param statusEvent 事件状态（1.待审核 11.待接单 21.待定损 31.待支付 41.待交车 51.待维修 61.待接车 71.待评价 81.投诉中 100.已完成）
     */
    public void setStatusEvent(Integer statusEvent) {
        this.statusEvent = statusEvent;
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

    public String getCancellationsReason() {
        return cancellationsReason;
    }

    public void setCancellationsReason(String cancellationsReason) {
        this.cancellationsReason = cancellationsReason;
    }

    public String getCancellationsInstructions() {
        return cancellationsInstructions;
    }

    public void setCancellationsInstructions(String cancellationsInstructions) {
        this.cancellationsInstructions = cancellationsInstructions;
    }

    public Date getFailReceiveOrderTime() {
        return failReceiveOrderTime;
    }

    public void setFailReceiveOrderTime(Date failReceiveOrderTime) {
        this.failReceiveOrderTime = failReceiveOrderTime;
    }
}