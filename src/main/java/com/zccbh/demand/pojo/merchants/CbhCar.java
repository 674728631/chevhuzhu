package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cbh_car")
public class CbhCar {
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
     * 车主姓名
     */
    @Column(name = "nameCarOwner")
    private String nameCarOwner;

    /**
     * 车主电话
     */
    @Column(name = "telCarOwner")
    private String telCarOwner;

    /**
     * 车牌号
     */
    @Column(name = "licensePlateNumber")
    private String licensePlateNumber;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 品牌
     */
    private String model;

    /**
     * 车型
     */
    @Column(name = "carType")
    private String carType;

    /**
     * 行驶城市
     */
    @Column(name = "drvingCity")
    private String drvingCity;

    /**
     * 行驶证照片
     */
    @Column(name = "drivingLicense")
    private String drivingLicense;

    /**
     * 车辆照片
     */
    @Column(name = "carPhotos")
    private String carPhotos;

    /**
     * 车架号
     */
    @Column(name = "VIN")
    private String VIN;

    /**
     * 剩余理赔额度
     */
    @Column(name = "amtCompensation")
    private BigDecimal amtCompensation;

    /**
     * 互助金余额
     */
    @Column(name = "amtCooperation")
    private BigDecimal amtCooperation;

    /**
     * 保障类型（扣款型，包年型）
     */
    @Column(name = "typeGuarantee")
    private Integer typeGuarantee;

    /**
     * 保障开始时间
     */
    @Column(name = "timeBegin")
    private Date timeBegin;

    /**
     * 保障结束时间
     */
    @Column(name = "timeEnd")
    private Date timeEnd;

    /**
     * 退出时间
     */
    @Column(name = "timeSignout")
    private Date timeSignout;

    /**
     * 失败原因
     */
    @Column(name = "reasonFailure")
    private String reasonFailure;

    /**
     * 失败次数
     */
    @Column(name = "failureNum")
    private Integer failureNum;

    /**
     * 数据状态 1.为审核中 2.审核通过 3.为保障中 4.已退出
     */
    private Integer status;

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
     * 获取车主姓名
     *
     * @return nameCarOwner - 车主姓名
     */
    public String getNameCarOwner() {
        return nameCarOwner;
    }

    /**
     * 设置车主姓名
     *
     * @param nameCarOwner 车主姓名
     */
    public void setNameCarOwner(String nameCarOwner) {
        this.nameCarOwner = nameCarOwner;
    }

    /**
     * 获取车主电话
     *
     * @return telCarOwner - 车主电话
     */
    public String getTelCarOwner() {
        return telCarOwner;
    }

    /**
     * 设置车主电话
     *
     * @param telCarOwner 车主电话
     */
    public void setTelCarOwner(String telCarOwner) {
        this.telCarOwner = telCarOwner;
    }

    /**
     * 获取车牌号
     *
     * @return licensePlateNumber - 车牌号
     */
    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    /**
     * 设置车牌号
     *
     * @param licensePlateNumber 车牌号
     */
    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    /**
     * 获取品牌
     *
     * @return brand - 品牌
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 设置品牌
     *
     * @param brand 品牌
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 获取车型
     *
     * @return carType - 车型
     */
    public String getCarType() {
        return carType;
    }

    /**
     * 设置车型
     *
     * @param carType 车型
     */
    public void setCarType(String carType) {
        this.carType = carType;
    }

    /**
     * 获取行驶城市
     *
     * @return drvingCity - 行驶城市
     */
    public String getDrvingCity() {
        return drvingCity;
    }

    /**
     * 设置行驶城市
     *
     * @param drvingCity 行驶城市
     */
    public void setDrvingCity(String drvingCity) {
        this.drvingCity = drvingCity;
    }

    /**
     * 获取行驶证照片
     *
     * @return drivingLicense - 行驶证照片
     */
    public String getDrivingLicense() {
        return drivingLicense;
    }

    /**
     * 设置行驶证照片
     *
     * @param drivingLicense 行驶证照片
     */
    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    /**
     * 获取车辆照片
     *
     * @return carPhotos - 车辆照片
     */
    public String getCarPhotos() {
        return carPhotos;
    }

    /**
     * 设置车辆照片
     *
     * @param carPhotos 车辆照片
     */
    public void setCarPhotos(String carPhotos) {
        this.carPhotos = carPhotos;
    }

    /**
     * 获取车架号
     *
     * @return VIN - 车架号
     */
    public String getVIN() {
        return VIN;
    }

    /**
     * 设置车架号
     *
     * @param VIN 车架号
     */
    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    /**
     * 获取剩余理赔额度
     *
     * @return amtCompensation - 剩余理赔额度
     */
    public BigDecimal getAmtCompensation() {
        return amtCompensation;
    }

    /**
     * 设置剩余理赔额度
     *
     * @param amtCompensation 剩余理赔额度
     */
    public void setAmtCompensation(BigDecimal amtCompensation) {
        this.amtCompensation = amtCompensation;
    }

    /**
     * 获取互助金余额
     *
     * @return amtCooperation - 互助金余额
     */
    public BigDecimal getAmtCooperation() {
        return amtCooperation;
    }

    /**
     * 设置互助金余额
     *
     * @param amtCooperation 互助金余额
     */
    public void setAmtCooperation(BigDecimal amtCooperation) {
        this.amtCooperation = amtCooperation;
    }

    /**
     * 获取保障类型（扣款型，包年型）
     *
     * @return typeGuarantee - 保障类型（扣款型，包年型）
     */
    public Integer getTypeGuarantee() {
        return typeGuarantee;
    }

    /**
     * 设置保障类型（扣款型，包年型）
     *
     * @param typeGuarantee 保障类型（扣款型，包年型）
     */
    public void setTypeGuarantee(Integer typeGuarantee) {
        this.typeGuarantee = typeGuarantee;
    }

    /**
     * 获取保障开始时间
     *
     * @return timeBegin - 保障开始时间
     */
    public Date getTimeBegin() {
        return timeBegin;
    }

    /**
     * 设置保障开始时间
     *
     * @param timeBegin 保障开始时间
     */
    public void setTimeBegin(Date timeBegin) {
        this.timeBegin = timeBegin;
    }

    /**
     * 获取保障结束时间
     *
     * @return timeEnd - 保障结束时间
     */
    public Date getTimeEnd() {
        return timeEnd;
    }

    /**
     * 设置保障结束时间
     *
     * @param timeEnd 保障结束时间
     */
    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * 获取退出时间
     *
     * @return timeSignout - 退出时间
     */
    public Date getTimeSignout() {
        return timeSignout;
    }

    /**
     * 设置退出时间
     *
     * @param timeSignout 退出时间
     */
    public void setTimeSignout(Date timeSignout) {
        this.timeSignout = timeSignout;
    }

    /**
     * 获取失败原因
     *
     * @return reasonFailure - 失败原因
     */
    public String getReasonFailure() {
        return reasonFailure;
    }

    /**
     * 设置失败原因
     *
     * @param reasonFailure 失败原因
     */
    public void setReasonFailure(String reasonFailure) {
        this.reasonFailure = reasonFailure;
    }

    /**
     * 获取失败次数
     *
     * @return failureNum - 失败次数
     */
    public Integer getFailureNum() {
        return failureNum;
    }

    /**
     * 设置失败次数
     *
     * @param failureNum 失败次数
     */
    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }

    /**
     * 获取数据状态 1.为审核中 2.审核通过 3.为保障中 4.已退出
     *
     * @return status - 数据状态 1.为审核中 2.审核通过 3.为保障中 4.已退出
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置数据状态 1.为审核中 2.审核通过 3.为保障中 4.已退出
     *
     * @param status 数据状态 1.为审核中 2.审核通过 3.为保障中 4.已退出
     */
    public void setStatus(Integer status) {
        this.status = status;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}