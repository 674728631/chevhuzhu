package com.zccbh.demand.pojo.user;

import com.zccbh.demand.pojo.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Comments:              账户明细
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class AccountDetail extends BaseModel {
    private static final long serialVersionUID = -7010201547813306848L;

    /**
     * 账户id
     */
    private Integer accountId;
    /**
     * 明细类型
     */
    private Integer type;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否已读（1.未读 3.已读）
     */
    private Integer isRead;
    /**
     * 创建时间
     */
    private Date createAt;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "AccountDetail{" +
                "accountId=" + accountId +
                ", type=" + type +
                ", amt=" + amt +
                ", content='" + content + '\'' +
                ", isRead=" + isRead +
                ", createAt=" + createAt +
                '}';
    }
}
