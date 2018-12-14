package com.zccbh.demand.pojo.datadic;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              基础数据_城市信息
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class City extends BaseModel {
    private static final long serialVersionUID = -9019834358937648097L;

    /**
     * 具体区县uuid
     */
    private String uuid;
    /**
     * 具体区县名称
     */
    private String name;
    /**
     * 城市uuid
     */
    private String cityUuid;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 省份uuid
     */
    private String provinceUuid;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 修改时间
     */
    private Date timeUpdate;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityUuid() {
        return cityUuid;
    }

    public void setCityUuid(String cityUuid) {
        this.cityUuid = cityUuid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceUuid() {
        return provinceUuid;
    }

    public void setProvinceUuid(String provinceUuid) {
        this.provinceUuid = provinceUuid;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(Date timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    @Override
    public String toString() {
        return "City{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", cityUuid='" + cityUuid + '\'' +
                ", cityName='" + cityName + '\'' +
                ", provinceUuid='" + provinceUuid + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", createAt=" + createAt +
                ", timeUpdate=" + timeUpdate +
                '}';
    }
}
