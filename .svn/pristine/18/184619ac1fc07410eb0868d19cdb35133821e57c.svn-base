package com.zccbh.demand.pojo.merchants;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * @ClassName: CbhGiftRecord 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年9月28日 下午2:13:26 
 *  
 */
@Table(name = "cbh_gift_record")
public class CbhGiftRecord {
	
	/**
	 * 获奖记录id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * 奖品id
	 */
	@Column(name = "giftId")
	private Integer giftId;
	
	/**
	 * openId
	 */
	@Column(name = "openId")
	private String openId;
	
	/**
	 * 车牌号
	 */
	@Column(name = "licensePlateNumber")
	private String licensePlateNumber;
	
	/**
	 * 抽奖时间
	 */
	@Column(name = "createAt")
	private Date createAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGiftId() {
		return giftId;
	}

	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
