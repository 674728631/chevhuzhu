package com.zccbh.demand.pojo.user;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              银行卡
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Bankcard extends BaseModel {
    private static final long serialVersionUID = -2684890694350195852L;

    /**
     * 账户id
     */
    private Integer accountId;
    /**
     * 开户行id
     */
    private Integer bankId;
    /**
     * 开户人姓名
     */
    private String nameCardOwner;
    /**
     * 开户行名称
     */
    private String nameBank;
    /**
     * 具体支行名称
     */
    private String nameBankDetail;
    /**
     * 银行卡号
     */
    private String cardNumber;
    /**
     * 是否设为默认卡 1.是
     */
    private Integer isAcquiescence;
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

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getNameCardOwner() {
        return nameCardOwner;
    }

    public void setNameCardOwner(String nameCardOwner) {
        this.nameCardOwner = nameCardOwner;
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }

    public String getNameBankDetail() {
        return nameBankDetail;
    }

    public void setNameBankDetail(String nameBankDetail) {
        this.nameBankDetail = nameBankDetail;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getIsAcquiescence() {
        return isAcquiescence;
    }

    public void setIsAcquiescence(Integer isAcquiescence) {
        this.isAcquiescence = isAcquiescence;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Bankcard{" +
                "accountId=" + accountId +
                ", bankId=" + bankId +
                ", nameCardOwner='" + nameCardOwner + '\'' +
                ", nameBank='" + nameBank + '\'' +
                ", nameBankDetail='" + nameBankDetail + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", isAcquiescence=" + isAcquiescence +
                ", createAt=" + createAt +
                '}';
    }
}
