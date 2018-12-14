package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Table(name = "middle_business_maintenanceshop")
public class MiddleBusinessMaintenanceshop {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 维修厂用户id
     */
    @Column(name = "businessId")
    private Integer businessId;

    /**
     * 维修厂id
     */
    @Column(name = "maintenanceshopId")
    private Integer maintenanceshopId;

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
     * 获取维修厂用户id
     *
     * @return businessId - 维修厂用户id
     */
    public Integer getBusinessId() {
        return businessId;
    }

    /**
     * 设置维修厂用户id
     *
     * @param businessId 维修厂用户id
     */
    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
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