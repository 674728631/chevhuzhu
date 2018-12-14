package com.zccbh.demand.pojo.user;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              维修厂信息
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Maintenanceshop extends BaseModel {
    private static final long serialVersionUID = -3817058440355007165L;

    /**
     * 账户id
     */
    private Integer accountId;
    /**
     * 维修厂名称
     */
    private String name;
    /**
     * 维修厂照片
     */
    private String img;
    /**
     * 联系人
     */
    private String linkman;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 联系微信
     */
    private String wechat;
    /**
     * 地址
     */
    private String address;
    /**
     * 营业时间
     */
    private String businessHours;
    /**
     * 企业logo
     */
    private String logo;

    /**
     * 累计维修次数
     */
    private Integer repairNum;
    /**
     * 累计评论次数
     */
    private Integer commentNum;
    /**
     * 评分
     */
    private Integer score;
    /**
     * 加入时间
     */
    private Date timeJoin;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getRepairNum() {
        return repairNum;
    }

    public void setRepairNum(Integer repairNum) {
        this.repairNum = repairNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getTimeJoin() {
        return timeJoin;
    }

    public void setTimeJoin(Date timeJoin) {
        this.timeJoin = timeJoin;
    }

    @Override
    public String toString() {
        return "Maintenanceshop{" +
                "accountId=" + accountId +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", linkman='" + linkman + '\'' +
                ", tel='" + tel + '\'' +
                ", wechat='" + wechat + '\'' +
                ", address='" + address + '\'' +
                ", businessHours='" + businessHours + '\'' +
                ", logo='" + logo + '\'' +
                ", repairNum=" + repairNum +
                ", commentNum=" + commentNum +
                ", score=" + score +
                ", timeJoin=" + timeJoin +
                '}';
    }
}
