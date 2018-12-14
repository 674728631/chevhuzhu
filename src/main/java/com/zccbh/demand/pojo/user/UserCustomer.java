package com.zccbh.demand.pojo.user;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              车主用户信息
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class UserCustomer extends BaseModel {
    private static final long serialVersionUID = -8382225401106791017L;

    /**
     * 用户名
     */
    private String customerUN;
    /**
     * 电话号码
     */
    private String customerPN;
    /**
     * 密码
     */
    private String customerPW;
    /**
     * 微信号码
     */
    private String wechat;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别 0.女 1.男
     */
    private Integer sex;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 地区
     */
    private String area;
    /**
     * 登录时间
     */
    private Date timeLogin;
    /**
     * 登录错误次数
     */
    private Integer loginErrorNum;
    /**
     * 锁定结束时间
     */
    private Date timeLockover;
    /**
     * 锁定类型
     */
    private Integer typeLock;
    /**
     * 用户类型
     */
    private Integer typeUser;
    /**
     * 数据状态 1.正常 2.冻结
     */
    private Integer status;
    /**
     * 加入时间
     */
    private Date timeJoin;

    public String getCustomerUN() {
        return customerUN;
    }

    public void setCustomerUN(String customerUN) {
        this.customerUN = customerUN;
    }

    public String getCustomerPN() {
        return customerPN;
    }

    public void setCustomerPN(String customerPN) {
        this.customerPN = customerPN;
    }

    public String getCustomerPW() {
        return customerPW;
    }

    public void setCustomerPW(String customerPW) {
        this.customerPW = customerPW;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getTimeLogin() {
        return timeLogin;
    }

    public void setTimeLogin(Date timeLogin) {
        this.timeLogin = timeLogin;
    }

    public Integer getLoginErrorNum() {
        return loginErrorNum;
    }

    public void setLoginErrorNum(Integer loginErrorNum) {
        this.loginErrorNum = loginErrorNum;
    }

    public Date getTimeLockover() {
        return timeLockover;
    }

    public void setTimeLockover(Date timeLockover) {
        this.timeLockover = timeLockover;
    }

    public Integer getTypeLock() {
        return typeLock;
    }

    public void setTypeLock(Integer typeLock) {
        this.typeLock = typeLock;
    }

    public Integer getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(Integer typeUser) {
        this.typeUser = typeUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTimeJoin() {
        return timeJoin;
    }
    public void setTimeJoin(Date timeJoin) {
        this.timeJoin = timeJoin;
    }

    @Override
    public String toString() {
        return "UserCustomer{" +
                "customerUN='" + customerUN + '\'' +
                ", customerPN='" + customerPN + '\'' +
                ", customerPW='" + customerPW + '\'' +
                ", wechat='" + wechat + '\'' +
                ", portrait='" + portrait + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", area='" + area + '\'' +
                ", timeLogin=" + timeLogin +
                ", loginErrorNum=" + loginErrorNum +
                ", timeLockover=" + timeLockover +
                ", typeLock=" + typeLock +
                ", typeUser=" + typeUser +
                ", status=" + status +
                ", timeJoin=" + timeJoin +
                '}';
    }
}
