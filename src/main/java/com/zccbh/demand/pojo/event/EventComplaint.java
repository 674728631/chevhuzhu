package com.zccbh.demand.pojo.event;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              互助事件投诉详情
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class EventComplaint extends BaseModel {
    private static final long serialVersionUID = -3504091332690002034L;

    /**
     * 互助事件号
     */
    private String eventNo;
    /**
     * 投诉内容
     */
    private String content;
    /**
     * 投诉照片
     */
    private String img;
    /**
     * 投诉时间
     */
    private Date timeComplaint;
    /**
     * 撤销投诉时间
     */
    private Date timeUnComplaint;

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Date getTimeComplaint() {
        return timeComplaint;
    }

    public void setTimeComplaint(Date timeComplaint) {
        this.timeComplaint = timeComplaint;
    }

    public Date getTimeUnComplaint() {
        return timeUnComplaint;
    }

    public void setTimeUnComplaint(Date timeUnComplaint) {
        this.timeUnComplaint = timeUnComplaint;
    }

    @Override
    public String toString() {
        return "EventComplaint{" +
                "eventNo='" + eventNo + '\'' +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", timeComplaint=" + timeComplaint +
                ", timeUnComplaint=" + timeUnComplaint +
                '}';
    }
}
