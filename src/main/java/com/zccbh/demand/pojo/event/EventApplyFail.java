package com.zccbh.demand.pojo.event;

import java.util.Date;

import com.zccbh.demand.pojo.BaseModel;

public class EventApplyFail extends BaseModel{
	
	private static final long serialVersionUID = 5729747308649713755L;

	private Integer id;

	/**
	 * 事件编号
	 */
    private String eventno; 

    /**
     * 事件描述
     */
    private String accidentdescription;

    /**
     * 时间图片
     */
    private String accidentimg;

    /**
     * 理赔时间
     */
    private Date timeapply;

    /**
     * 审核时间
     */
    private Date timeexamine;

    private String reasonfailure;

    private String reasonsuccess;

    /**
     * 记录创建时间
     */
    private Date createat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventno() {
        return eventno;
    }

    public void setEventno(String eventno) {
        this.eventno = eventno == null ? null : eventno.trim();
    }

    public String getAccidentdescription() {
        return accidentdescription;
    }

    public void setAccidentdescription(String accidentdescription) {
        this.accidentdescription = accidentdescription == null ? null : accidentdescription.trim();
    }

    public String getAccidentimg() {
        return accidentimg;
    }

    public void setAccidentimg(String accidentimg) {
        this.accidentimg = accidentimg == null ? null : accidentimg.trim();
    }

    public Date getTimeapply() {
        return timeapply;
    }

    public void setTimeapply(Date timeapply) {
        this.timeapply = timeapply;
    }

    public Date getTimeexamine() {
        return timeexamine;
    }

    public void setTimeexamine(Date timeexamine) {
        this.timeexamine = timeexamine;
    }

    public String getReasonfailure() {
        return reasonfailure;
    }

    public void setReasonfailure(String reasonfailure) {
        this.reasonfailure = reasonfailure == null ? null : reasonfailure.trim();
    }

    public String getReasonsuccess() {
        return reasonsuccess;
    }

    public void setReasonsuccess(String reasonsuccess) {
        this.reasonsuccess = reasonsuccess == null ? null : reasonsuccess.trim();
    }

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
    }
}