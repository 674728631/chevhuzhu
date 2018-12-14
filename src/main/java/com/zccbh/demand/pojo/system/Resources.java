package com.zccbh.demand.pojo.system;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              系统_资源
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Resources extends BaseModel {
    private static final long serialVersionUID = 4257211114948387644L;

    /**
     * URL描述
     */
    private String description;
    /**
     * URL名称
     */
    private String name;
    /**
     * URL
     */
    private String url;
    /**
     * URL类型（车主端=1 商家端=2 管理员端=3）
     */
    private Integer type;
    /**
     * 数据状态 1.正常
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createAt;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Resources{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createAt=" + createAt +
                '}';
    }
}
