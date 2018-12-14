package com.zccbh.demand.pojo.datadic;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              基础数据_银行信息
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Bank extends BaseModel {
    private static final long serialVersionUID = -8899245763536669645L;

    /**
     * 银行名称
     */
    private String name;
    /**
     * 数据状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Bank{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", createAt=" + createAt +
                '}';
    }
}
