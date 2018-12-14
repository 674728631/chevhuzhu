package com.zccbh.demand.pojo.merchants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cbh_order")
public class CbhOrder {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 保险理赔单号
     */
    @Column(name = "orderNo")
    private String orderNo;

    /**
     * 事件状态（1.申请审核 2 申请审核未通过 3.申请审核通过 10.待分单 11.待接单 12.放弃接单 21.待定损  22 .定损审核 23 .定损审核未通过 31.待支付 41.待交车 51.待维修 52维修中 61.待取车 71.待评价 81.投诉中 100.已完成）
     */
    private Integer status;

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
     * 定损人员id
     */
    @Column(name = "assertmanId")
    private Integer assertmanId;

    /**
     * 维修人员id
     */
    @Column(name = "repairmanId")
    private Integer repairmanId;

    /**
     * 定损金额
     */
    @Column(name = "amtAssert")
    private BigDecimal amtAssert;

    /**
     * 商家实际得到金额
     */
    @Column(name = "amtBusiness")
    private BigDecimal amtBusiness;

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
     * 车主姓名
     */
    @Column(name = "carOwnerName")
    private String carOwnerName;

    /**
     * 车主电话
     */
    @Column(name = "carOwnerTel")
    private String carOwnerTel;

    /**
     * 交车地点
     */
    @Column(name = "deliverPlace")
    private String deliverPlace;

    /**
     * 交车经度
     */
    @Column(name = "deliverLongitude")
    private String deliverLongitude;

    /**
     * 交车纬度
     */
    @Column(name = "deliverLatitude")
    private String deliverLatitude;

    /**
     * 取车地点
     */
    @Column(name = "takePlace")
    private String takePlace;

    /**
     * 取车经度
     */
    @Column(name = "takeLongitude")
    private String takeLongitude;

    /**
     * 取车纬度
     */
    @Column(name = "takeLatitude")
    private String takeLatitude;

    /**
     * 撤单原因
     */
    @Column(name = "reasonCancellations")
    private String reasonCancellations;

    /**
     * 撤单说明
     */
    @Column(name = "explanationCancellations")
    private String explanationCancellations;

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
    @Column(name = "assertDescription")
    private String assertDescription;

    /**
     * 定损说明
     */
    @Column(name = "explanationAssert")
    private String explanationAssert;

    /**
     * 申请理赔审核说明
     */
    @Column(name = "examineExplanation")
    private String examineExplanation;

    /**
     * 维修说明
     */
    @Column(name = "explanationRepair")
    private String explanationRepair;

    /**
     * 维修后照片
     */
    @Column(name = "repairImg")
    private String repairImg;

    /**
     * labelContent 标签内容  格式 服务号_技术很棒_很专业
     */
    @Column(name = "labelContent")
    private String labelContent;

    /**
     * 评价内容
     */
    @Column(name = "commentContent")
    private String commentContent;

    /**
     * 评价星级
     */
    @Column(name = "commentScore")
    private Integer commentScore;

    /**
     * 投诉内容
     */
    @Column(name = "complaintContent")
    private String complaintContent;

    /**
     * 投诉照片
     */
    @Column(name = "complaintImg")
    private String complaintImg;

    /**
     * 申请理赔时间
     */
    @Column(name = "applyTime")
    private Date applyTime;

    /**
     * 用户选择交车时间
     */
    @Column(name = "reciveCarTime")
    private Date reciveCarTime;

    /**
     * 申请分单时间
     */
    @Column(name = "applyDistributionTime")
    private Date applyDistributionTime;

    /**
     * 交车完成时间
     */
    @Column(name = "deliverCarTime")
    private Date deliverCarTime;

    /**
     * 分单时间
     */
    @Column(name = "distributionTime")
    private Date distributionTime;

    /**
     * 接单时间
     */
    @Column(name = "receiveOrderTime")
    private Date receiveOrderTime;

    /**
     * 定损时间
     */
    @Column(name = "assertTime")
    private Date assertTime;

    /**
     * 定损确认时间
     */
    @Column(name = "comfirmAssertTime")
    private Date comfirmAssertTime;

    /**
     * 开始维修时间
     */
    @Column(name = "beginRepairTime")
    private Date beginRepairTime;

    /**
     * 维修完成时间
     */
    @Column(name = "endRepairTime")
    private Date endRepairTime;

    /**
     * 取车完成时间
     */
    @Column(name = "takeCarTime")
    private Date takeCarTime;

    /**
     * 支付时间
     */
    @Column(name = "payTime")
    private Date payTime;

    /**
     * 完成时间
     */
    @Column(name = "completeTime")
    private Date completeTime;

    /**
     * 投诉时间
     */
    @Column(name = "complaintTime")
    private Date complaintTime;

    /**
     * 撤销投诉时间
     */
    @Column(name = "unComplaintTime")
    private Date unComplaintTime;

    /**
     *放弃接单时间
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
     * 获取保险理赔单号
     *
     * @return orderNo - 保险理赔单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置保险理赔单号
     *
     * @param orderNo 保险理赔单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取事件状态（1.申请审核 2 申请审核未通过 3.申请审核通过 10.待分单 11.待接单 12.放弃接单 21.待定损  22 .定损审核 23 .定损审核未通过 31.待支付 41.待交车 51.待维修 52维修中 61.待取车 71.待评价 81.投诉中 100.已完成）
     *
     * @return status - 事件状态（1.申请审核 2 申请审核未通过 3.申请审核通过 10.待分单 11.待接单 12.放弃接单 21.待定损  22 .定损审核 23 .定损审核未通过 31.待支付 41.待交车 51.待维修 52维修中 61.待取车 71.待评价 81.投诉中 100.已完成）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置事件状态（1.申请审核 2 申请审核未通过 3.申请审核通过 10.待分单 11.待接单 12.放弃接单 21.待定损  22 .定损审核 23 .定损审核未通过 31.待支付 41.待交车 51.待维修 52维修中 61.待取车 71.待评价 81.投诉中 100.已完成）
     *
     * @param status 事件状态（1.申请审核 2 申请审核未通过 3.申请审核通过 10.待分单 11.待接单 12.放弃接单 21.待定损  22 .定损审核 23 .定损审核未通过 31.待支付 41.待交车 51.待维修 52维修中 61.待取车 71.待评价 81.投诉中 100.已完成）
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取定损人员id
     *
     * @return assertmanId - 定损人员id
     */
    public Integer getAssertmanId() {
        return assertmanId;
    }

    /**
     * 设置定损人员id
     *
     * @param assertmanId 定损人员id
     */
    public void setAssertmanId(Integer assertmanId) {
        this.assertmanId = assertmanId;
    }

    /**
     * 获取维修人员id
     *
     * @return repairmanId - 维修人员id
     */
    public Integer getRepairmanId() {
        return repairmanId;
    }

    /**
     * 设置维修人员id
     *
     * @param repairmanId 维修人员id
     */
    public void setRepairmanId(Integer repairmanId) {
        this.repairmanId = repairmanId;
    }

    /**
     * 获取定损金额
     *
     * @return amtAssert - 定损金额
     */
    public BigDecimal getAmtAssert() {
        return amtAssert;
    }

    /**
     * 设置定损金额
     *
     * @param amtAssert 定损金额
     */
    public void setAmtAssert(BigDecimal amtAssert) {
        this.amtAssert = amtAssert;
    }

    /**
     * 获取商家实际得到金额
     *
     * @return amtBusiness - 商家实际得到金额
     */
    public BigDecimal getAmtBusiness() {
        return amtBusiness;
    }

    /**
     * 设置商家实际得到金额
     *
     * @param amtBusiness 商家实际得到金额
     */
    public void setAmtBusiness(BigDecimal amtBusiness) {
        this.amtBusiness = amtBusiness;
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
     * 获取车主姓名
     *
     * @return carOwnerName - 车主姓名
     */
    public String getCarOwnerName() {
        return carOwnerName;
    }

    /**
     * 设置车主姓名
     *
     * @param carOwnerName 车主姓名
     */
    public void setCarOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
    }

    /**
     * 获取车主电话
     *
     * @return carOwnerTel - 车主电话
     */
    public String getCarOwnerTel() {
        return carOwnerTel;
    }

    /**
     * 设置车主电话
     *
     * @param carOwnerTel 车主电话
     */
    public void setCarOwnerTel(String carOwnerTel) {
        this.carOwnerTel = carOwnerTel;
    }

    /**
     * 获取交车地点
     *
     * @return deliverPlace - 交车地点
     */
    public String getDeliverPlace() {
        return deliverPlace;
    }

    /**
     * 设置交车地点
     *
     * @param deliverPlace 交车地点
     */
    public void setDeliverPlace(String deliverPlace) {
        this.deliverPlace = deliverPlace;
    }

    /**
     * 获取交车经度
     *
     * @return deliverLongitude - 交车经度
     */
    public String getDeliverLongitude() {
        return deliverLongitude;
    }

    /**
     * 设置交车经度
     *
     * @param deliverLongitude 交车经度
     */
    public void setDeliverLongitude(String deliverLongitude) {
        this.deliverLongitude = deliverLongitude;
    }

    /**
     * 获取交车纬度
     *
     * @return deliverLatitude - 交车纬度
     */
    public String getDeliverLatitude() {
        return deliverLatitude;
    }

    /**
     * 设置交车纬度
     *
     * @param deliverLatitude 交车纬度
     */
    public void setDeliverLatitude(String deliverLatitude) {
        this.deliverLatitude = deliverLatitude;
    }

    /**
     * 获取取车地点
     *
     * @return takePlace - 取车地点
     */
    public String getTakePlace() {
        return takePlace;
    }

    /**
     * 设置取车地点
     *
     * @param takePlace 取车地点
     */
    public void setTakePlace(String takePlace) {
        this.takePlace = takePlace;
    }

    /**
     * 获取取车经度
     *
     * @return takeLongitude - 取车经度
     */
    public String getTakeLongitude() {
        return takeLongitude;
    }

    /**
     * 设置取车经度
     *
     * @param takeLongitude 取车经度
     */
    public void setTakeLongitude(String takeLongitude) {
        this.takeLongitude = takeLongitude;
    }

    /**
     * 获取取车纬度
     *
     * @return takeLatitude - 取车纬度
     */
    public String getTakeLatitude() {
        return takeLatitude;
    }

    /**
     * 设置取车纬度
     *
     * @param takeLatitude 取车纬度
     */
    public void setTakeLatitude(String takeLatitude) {
        this.takeLatitude = takeLatitude;
    }

    /**
     * 获取撤单原因
     *
     * @return reasonCancellations - 撤单原因
     */
    public String getReasonCancellations() {
        return reasonCancellations;
    }

    /**
     * 设置撤单原因
     *
     * @param reasonCancellations 撤单原因
     */
    public void setReasonCancellations(String reasonCancellations) {
        this.reasonCancellations = reasonCancellations;
    }

    /**
     * 获取撤单说明
     *
     * @return explanationCancellations - 撤单说明
     */
    public String getExplanationCancellations() {
        return explanationCancellations;
    }

    /**
     * 设置撤单说明
     *
     * @param explanationCancellations 撤单说明
     */
    public void setExplanationCancellations(String explanationCancellations) {
        this.explanationCancellations = explanationCancellations;
    }

    /**
     * 获取受损程度
     *
     * @return damageExtent - 受损程度
     */
    public String getDamageExtent() {
        return damageExtent;
    }

    /**
     * 设置受损程度
     *
     * @param damageExtent 受损程度
     */
    public void setDamageExtent(String damageExtent) {
        this.damageExtent = damageExtent;
    }

    /**
     * 获取受损部位
     *
     * @return damagePosition - 受损部位
     */
    public String getDamagePosition() {
        return damagePosition;
    }

    /**
     * 设置受损部位
     *
     * @param damagePosition 受损部位
     */
    public void setDamagePosition(String damagePosition) {
        this.damagePosition = damagePosition;
    }

    /**
     * 获取定损照片
     *
     * @return assertImg - 定损照片
     */
    public String getAssertImg() {
        return assertImg;
    }

    /**
     * 设置定损照片
     *
     * @param assertImg 定损照片
     */
    public void setAssertImg(String assertImg) {
        this.assertImg = assertImg;
    }

    /**
     * 获取定损描述
     *
     * @return assertDescription - 定损描述
     */
    public String getAssertDescription() {
        return assertDescription;
    }

    /**
     * 设置定损描述
     *
     * @param assertDescription 定损描述
     */
    public void setAssertDescription(String assertDescription) {
        this.assertDescription = assertDescription;
    }

    /**
     * 获取定损说明
     *
     * @return explanationAssert - 定损说明
     */
    public String getExplanationAssert() {
        return explanationAssert;
    }

    /**
     * 设置定损说明
     *
     * @param explanationAssert 定损说明
     */
    public void setExplanationAssert(String explanationAssert) {
        this.explanationAssert = explanationAssert;
    }

    /**
     * 获取维修说明
     *
     * @return explanationRepair - 维修说明
     */
    public String getExplanationRepair() {
        return explanationRepair;
    }

    /**
     * 设置维修说明
     *
     * @param explanationRepair 维修说明
     */
    public void setExplanationRepair(String explanationRepair) {
        this.explanationRepair = explanationRepair;
    }

    /**
     * 获取维修后照片
     *
     * @return repairImg - 维修后照片
     */
    public String getRepairImg() {
        return repairImg;
    }

    /**
     * 设置维修后照片
     *
     * @param repairImg 维修后照片
     */
    public void setRepairImg(String repairImg) {
        this.repairImg = repairImg;
    }

    /**
     * 获取labelContent 标签内容  格式 服务号_技术很棒_很专业
     *
     * @return labelContent - labelContent 标签内容  格式 服务号_技术很棒_很专业
     */
    public String getLabelContent() {
        return labelContent;
    }

    /**
     * 设置labelContent 标签内容  格式 服务号_技术很棒_很专业
     *
     * @param labelContent labelContent 标签内容  格式 服务号_技术很棒_很专业
     */
    public void setLabelContent(String labelContent) {
        this.labelContent = labelContent;
    }

    /**
     * 获取评价内容
     *
     * @return commentContent - 评价内容
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * 设置评价内容
     *
     * @param commentContent 评价内容
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    /**
     * 获取评价星级
     *
     * @return commentScore - 评价星级
     */
    public Integer getCommentScore() {
        return commentScore;
    }

    /**
     * 设置评价星级
     *
     * @param commentScore 评价星级
     */
    public void setCommentScore(Integer commentScore) {
        this.commentScore = commentScore;
    }

    /**
     * 获取投诉内容
     *
     * @return complaintContent - 投诉内容
     */
    public String getComplaintContent() {
        return complaintContent;
    }

    /**
     * 设置投诉内容
     *
     * @param complaintContent 投诉内容
     */
    public void setComplaintContent(String complaintContent) {
        this.complaintContent = complaintContent;
    }

    /**
     * 获取投诉照片
     *
     * @return complaintImg - 投诉照片
     */
    public String getComplaintImg() {
        return complaintImg;
    }

    /**
     * 设置投诉照片
     *
     * @param complaintImg 投诉照片
     */
    public void setComplaintImg(String complaintImg) {
        this.complaintImg = complaintImg;
    }

    /**
     * 获取申请理赔时间
     *
     * @return applyTime - 申请理赔时间
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * 设置申请理赔时间
     *
     * @param applyTime 申请理赔时间
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取用户选择交车时间
     *
     * @return reciveCarTime - 用户选择交车时间
     */
    public Date getReciveCarTime() {
        return reciveCarTime;
    }

    /**
     * 设置用户选择交车时间
     *
     * @param reciveCarTime 用户选择交车时间
     */
    public void setReciveCarTime(Date reciveCarTime) {
        this.reciveCarTime = reciveCarTime;
    }

    /**
     * 获取申请分单时间
     *
     * @return applyDistributionTime - 申请分单时间
     */
    public Date getApplyDistributionTime() {
        return applyDistributionTime;
    }

    /**
     * 设置申请分单时间
     *
     * @param applyDistributionTime 申请分单时间
     */
    public void setApplyDistributionTime(Date applyDistributionTime) {
        this.applyDistributionTime = applyDistributionTime;
    }

    /**
     * 获取交车完成时间
     *
     * @return deliverCarTime - 交车完成时间
     */
    public Date getDeliverCarTime() {
        return deliverCarTime;
    }

    /**
     * 设置交车完成时间
     *
     * @param deliverCarTime 交车完成时间
     */
    public void setDeliverCarTime(Date deliverCarTime) {
        this.deliverCarTime = deliverCarTime;
    }

    /**
     * 获取分单时间
     *
     * @return distributionTime - 分单时间
     */
    public Date getDistributionTime() {
        return distributionTime;
    }

    /**
     * 设置分单时间
     *
     * @param distributionTime 分单时间
     */
    public void setDistributionTime(Date distributionTime) {
        this.distributionTime = distributionTime;
    }

    /**
     * 获取接单时间
     *
     * @return receiveOrderTime - 接单时间
     */
    public Date getReceiveOrderTime() {
        return receiveOrderTime;
    }

    /**
     * 设置接单时间
     *
     * @param receiveOrderTime 接单时间
     */
    public void setReceiveOrderTime(Date receiveOrderTime) {
        this.receiveOrderTime = receiveOrderTime;
    }

    /**
     * 获取定损时间
     *
     * @return assertTime - 定损时间
     */
    public Date getAssertTime() {
        return assertTime;
    }

    /**
     * 设置定损时间
     *
     * @param assertTime 定损时间
     */
    public void setAssertTime(Date assertTime) {
        this.assertTime = assertTime;
    }

    /**
     * 获取定损确认时间
     *
     * @return comfirmAssertTime - 定损确认时间
     */
    public Date getComfirmAssertTime() {
        return comfirmAssertTime;
    }

    /**
     * 设置定损确认时间
     *
     * @param comfirmAssertTime 定损确认时间
     */
    public void setComfirmAssertTime(Date comfirmAssertTime) {
        this.comfirmAssertTime = comfirmAssertTime;
    }

    /**
     * 获取开始维修时间
     *
     * @return beginRepairTime - 开始维修时间
     */
    public Date getBeginRepairTime() {
        return beginRepairTime;
    }

    /**
     * 设置开始维修时间
     *
     * @param beginRepairTime 开始维修时间
     */
    public void setBeginRepairTime(Date beginRepairTime) {
        this.beginRepairTime = beginRepairTime;
    }

    /**
     * 获取维修完成时间
     *
     * @return endRepairTime - 维修完成时间
     */
    public Date getEndRepairTime() {
        return endRepairTime;
    }

    /**
     * 设置维修完成时间
     *
     * @param endRepairTime 维修完成时间
     */
    public void setEndRepairTime(Date endRepairTime) {
        this.endRepairTime = endRepairTime;
    }

    /**
     * 获取取车完成时间
     *
     * @return takeCarTime - 取车完成时间
     */
    public Date getTakeCarTime() {
        return takeCarTime;
    }

    /**
     * 设置取车完成时间
     *
     * @param takeCarTime 取车完成时间
     */
    public void setTakeCarTime(Date takeCarTime) {
        this.takeCarTime = takeCarTime;
    }

    /**
     * 获取支付时间
     *
     * @return payTime - 支付时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 设置支付时间
     *
     * @param payTime 支付时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取完成时间
     *
     * @return completeTime - 完成时间
     */
    public Date getCompleteTime() {
        return completeTime;
    }

    /**
     * 设置完成时间
     *
     * @param completeTime 完成时间
     */
    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    /**
     * 获取投诉时间
     *
     * @return complaintTime - 投诉时间
     */
    public Date getComplaintTime() {
        return complaintTime;
    }

    /**
     * 设置投诉时间
     *
     * @param complaintTime 投诉时间
     */
    public void setComplaintTime(Date complaintTime) {
        this.complaintTime = complaintTime;
    }

    /**
     * 获取撤销投诉时间
     *
     * @return unComplaintTime - 撤销投诉时间
     */
    public Date getUnComplaintTime() {
        return unComplaintTime;
    }

    /**
     * 设置撤销投诉时间
     *
     * @param unComplaintTime 撤销投诉时间
     */
    public void setUnComplaintTime(Date unComplaintTime) {
        this.unComplaintTime = unComplaintTime;
    }

    public Date getFailReceiveOrderTime() {
        return failReceiveOrderTime;
    }

    public void setFailReceiveOrderTime(Date failReceiveOrderTime) {
        this.failReceiveOrderTime = failReceiveOrderTime;
    }

    public String getExamineExplanation() {
        return examineExplanation;
    }

    public void setExamineExplanation(String examineExplanation) {
        this.examineExplanation = examineExplanation;
    }
}