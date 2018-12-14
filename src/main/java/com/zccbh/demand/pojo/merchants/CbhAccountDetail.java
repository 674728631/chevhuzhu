package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cbh_account_detail")
public class CbhAccountDetail {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 账户id
     */
    @Column(name = "accountId")
    private Integer accountId;

    /**
     * 明细类型
     */
    private Integer type;

    /**
     * 互助事件号
     */
    @Column(name = "eventNo")
    private String eventNo;

    /**
     * 互助事件号
     */
    private String img;

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
    @Column(name = "isRead")
    private Integer isRead=1;

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
     * 获取账户id
     *
     * @return accountId - 账户id
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * 设置账户id
     *
     * @param accountId 账户id
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取明细类型
     *
     * @return type - 明细类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置明细类型
     *
     * @param type 明细类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取金额
     *
     * @return amt - 金额
     */
    public BigDecimal getAmt() {
        return amt;
    }

    /**
     * 设置金额
     *
     * @param amt 金额
     */
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取是否已读（1.未读 3.已读）
     *
     * @return isRead - 是否已读（1.未读 3.已读）
     */
    public Integer getIsRead() {
        return isRead;
    }

    /**
     * 设置是否已读（1.未读 3.已读）
     *
     * @param isRead 是否已读（1.未读 3.已读）
     */
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
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

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}