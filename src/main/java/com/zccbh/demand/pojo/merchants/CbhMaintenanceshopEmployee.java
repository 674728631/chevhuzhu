package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cbh_maintenanceshop_employee")
public class CbhMaintenanceshopEmployee {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 维修厂id
     */
    @Column(name = "maintenanceshopId")
    private Integer maintenanceshopId;

    /**
     * 工种类型
     */
    private Integer type;

    /**
     * 维修技师名字
     */
    private String name;

    /**
     * 从业年限
     */
    @Column(name = "workYear")
    private Integer workYear;

    /**
     * 职位名称
     */
    @Column(name = "jobTitle")
    private String jobTitle;

    /**
     * 电话号码
     */
    private String tel;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 简介
     */
    private String introduction;

    /**
     * 维修技师照片
     */
    private String img;

    /**
     * 评价
     */
    private String comment;

    /**
     * 加入时间
     */
    @Column(name = "timeJoin")
    private Date timeJoin= DateUtils.getDateTime();

    /**
     * 状态 1代表正常 2代表删除
     */
    private Integer status=1;
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
     * 获取工种类型
     *
     * @return type - 工种类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置工种类型
     *
     * @param type 工种类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取维修技师名字
     *
     * @return name - 维修技师名字
     */
    public String getName() {
        return name;
    }

    /**
     * 设置维修技师名字
     *
     * @param name 维修技师名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取从业年限
     *
     * @return workYear - 从业年限
     */
    public Integer getWorkYear() {
        return workYear;
    }

    /**
     * 设置从业年限
     *
     * @param workYear 从业年限
     */
    public void setWorkYear(Integer workYear) {
        this.workYear = workYear;
    }

    /**
     * 获取简介
     *
     * @return introduction - 简介
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置简介
     *
     * @param introduction 简介
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    /**
     * 获取维修技师照片
     *
     * @return img - 维修技师照片
     */
    public String getImg() {
        return img;
    }

    /**
     * 设置维修技师照片
     *
     * @param img 维修技师照片
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * 获取评价
     *
     * @return comment - 评价
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置评价
     *
     * @param comment 评价
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取加入时间
     *
     * @return timeJoin - 加入时间
     */
    public Date getTimeJoin() {
        return timeJoin;
    }

    /**
     * 设置加入时间
     *
     * @param timeJoin 加入时间
     */
    public void setTimeJoin(Date timeJoin) {
        this.timeJoin = timeJoin;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}