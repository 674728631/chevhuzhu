/**   
 * @author xiaowuge  
 * @date 2018年9月28日  
 * @version 1.0  
 */ 
package com.zccbh.demand.pojo.merchants;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * @ClassName: CbhGift 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年9月28日 下午1:54:43 
 *  
 */
@Table(name = "cbh_gift")
public class CbhGift {
	
	/**
	 * 奖品id
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    /**
     * 抽奖活动编号
     */
   @Column(name = "drawNum")
	private String drawNum;
	
   /**
    * 奖品名称
    */
   @Column(name = "name")
	private String name;
	
   /**
    * 获奖概率
    */
   @Column(name = "prob")
	private BigDecimal prob;

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public String getDrawNum() {
	return drawNum;
}

public void setDrawNum(String drawNum) {
	this.drawNum = drawNum;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public BigDecimal getProb() {
	return prob;
}

public void setProb(BigDecimal prob) {
	this.prob = prob;
}
   

}
