package com.zccbh.demand.pojo.system;

import java.util.Date;

import com.zccbh.demand.pojo.BaseModel;

/** 
 * @ClassName: UserCustomerLog 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年11月21日 上午9:53:18 
 *  
 */
public class UserCustomerLog extends BaseModel{
	
	private static final long serialVersionUID = 4941944333241858574L;

	private Integer id;

	/**
	 * 用户id
	 */
    private Integer customerId; 

    /**
     * 用户电话
     */
    private String customerPN;

    /**
     * 用户来源
     */
    private String source;

    /**
     * 用户创建时间
     */
    private Date createAt;

    /**
     * 用户当前状态
     */
    private Integer currentStatus;

    /**
     * 用户操作时间
     */
    private Date optTime;

    /**
     * 用户操作类型
     */
    private Integer optType;

    /**
     * 用户操作描述
     */
    private String optDesc;

    /**
     * 记录时间
     */
    private Date recordTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerPN() {
		return customerPN;
	}

	public void setCustomerPN(String customerPN) {
		this.customerPN = customerPN;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Integer currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Date getOptTime() {
		return optTime;
	}

	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public String getOptDesc() {
		return optDesc;
	}

	public void setOptDesc(String optDesc) {
		this.optDesc = optDesc;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
    

}
