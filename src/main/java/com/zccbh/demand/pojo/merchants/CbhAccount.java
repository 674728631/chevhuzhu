package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cbh_account")
public class CbhAccount {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 提现密码
     */
    @Column(name = "accountPW")
    private String accountPW;

    /**
     * 第三方账号(json):{zfb:支付宝,wx:微信}
     */
    @Column(name = "thirdPartyAccount")
    private String thirdPartyAccount;

    /**
     * 可用金额
     */
    @Column(name = "amtUnfreeze")
    private String amtUnfreeze;

    /**
     * 冻结金额
     */
    @Column(name = "amtFreeze")
    private String amtFreeze;

    /**
     * 已提现金额
     */
    @Column(name = "amtPaid")
    private BigDecimal amtPaid=new BigDecimal(0.00);


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
     * 获取提现密码
     *
     * @return accountPW - 提现密码
     */
    public String getAccountPW() {
        return accountPW;
    }

    /**
     * 设置提现密码
     *
     * @param accountPW 提现密码
     */
    public void setAccountPW(String accountPW) {
        this.accountPW = accountPW;
    }

    /**
     * 获取第三方账号(json):{zfb:支付宝,wx:微信}
     *
     * @return thirdPartyAccount - 第三方账号(json):{zfb:支付宝,wx:微信}
     */
    public String getThirdPartyAccount() {
        return thirdPartyAccount;
    }

    /**
     * 设置第三方账号(json):{zfb:支付宝,wx:微信}
     *
     * @param thirdPartyAccount 第三方账号(json):{zfb:支付宝,wx:微信}
     */
    public void setThirdPartyAccount(String thirdPartyAccount) {
        this.thirdPartyAccount = thirdPartyAccount;
    }

    /**
     * 获取已提现金额
     *
     * @return amtPaid - 已提现金额
     */
    public BigDecimal getAmtPaid() {
        return amtPaid;
    }

    /**
     * 设置已提现金额
     *
     * @param amtPaid 已提现金额
     */
    public void setAmtPaid(BigDecimal amtPaid) {
        this.amtPaid = amtPaid;
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

    public String getAmtUnfreeze() {
        return amtUnfreeze;
    }

    public void setAmtUnfreeze(String amtUnfreeze) {
        this.amtUnfreeze = amtUnfreeze;
    }

    public String getAmtFreeze() {
        return amtFreeze;
    }

    public void setAmtFreeze(String amtFreeze) {
        this.amtFreeze = amtFreeze;
    }
}