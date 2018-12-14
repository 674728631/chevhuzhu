package com.zccbh.demand.pojo.system;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              系统_权限
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Rights extends BaseModel {
    private static final long serialVersionUID = 7048045497104636878L;

    /**
     * 角色id
     */
    private Integer roleId;
    /**
     * 菜单权限
     */
    private String rightsMenu;
    /**
     * URL权限
     */
    private String rightsResource;
    /**
     * 数据状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createAt;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRightsMenu() {
        return rightsMenu;
    }

    public void setRightsMenu(String rightsMenu) {
        this.rightsMenu = rightsMenu;
    }

    public String getRightsResource() {
        return rightsResource;
    }

    public void setRightsResource(String rightsResource) {
        this.rightsResource = rightsResource;
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
        return "Rights{" +
                "roleId=" + roleId +
                ", rightsMenu='" + rightsMenu + '\'' +
                ", rightsResource='" + rightsResource + '\'' +
                ", status=" + status +
                ", createAt=" + createAt +
                '}';
    }
}
