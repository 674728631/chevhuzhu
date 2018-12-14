package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cbh_event_complaint")
public class CbhEventComplaint {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 互助事件号
     */
    @Column(name = "eventNo")
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
    @Column(name = "timeComplaint")
    private Date timeComplaint;

    /**
     * 撤销投诉时间
     */
    @Column(name = "timeUnComplaint")
    private Date timeUnComplaint;

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
     * 获取互助事件号
     *
     * @return eventNo - 互助事件号
     */
    public String getEventNo() {
        return eventNo;
    }

    /**
     * 设置互助事件号
     *
     * @param eventNo 互助事件号
     */
    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    /**
     * 获取投诉内容
     *
     * @return content - 投诉内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置投诉内容
     *
     * @param content 投诉内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取投诉照片
     *
     * @return img - 投诉照片
     */
    public String getImg() {
        return img;
    }

    /**
     * 设置投诉照片
     *
     * @param img 投诉照片
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * 获取投诉时间
     *
     * @return timeComplaint - 投诉时间
     */
    public Date getTimeComplaint() {
        return timeComplaint;
    }

    /**
     * 设置投诉时间
     *
     * @param timeComplaint 投诉时间
     */
    public void setTimeComplaint(Date timeComplaint) {
        this.timeComplaint = timeComplaint;
    }

    /**
     * 获取撤销投诉时间
     *
     * @return timeUnComplaint - 撤销投诉时间
     */
    public Date getTimeUnComplaint() {
        return timeUnComplaint;
    }

    /**
     * 设置撤销投诉时间
     *
     * @param timeUnComplaint 撤销投诉时间
     */
    public void setTimeUnComplaint(Date timeUnComplaint) {
        this.timeUnComplaint = timeUnComplaint;
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
}