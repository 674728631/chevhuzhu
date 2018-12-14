package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cbh_event_repair")
public class cbhEventRepair {
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
     * 维修人员id
     */
    @Column(name = "repairmanId")
    private Integer repairmanId;

    /**
     * 维修说明
     */
    private String description;

    /**
     * 维修后照片
     */
    private String img;

    /**
     * 开始维修时间
     */
    @Column(name = "timeBegin")
    private Date timeBegin;

    /**
     * 预计结束时间
     */
    @Column(name = "timeEnd")
    private Date timeEnd;

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
     * 获取维修人员id
     *
     * @return repairmanId - 维修人员id
     */
    public Integer getRepairmanId() {
        return repairmanId;
    }

    /**
     * 设置维修人员id
     *
     * @param repairmanId 维修人员id
     */
    public void setRepairmanId(Integer repairmanId) {
        this.repairmanId = repairmanId;
    }

    /**
     * 获取维修说明
     *
     * @return description - 维修说明
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置维修说明
     *
     * @param description 维修说明
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取维修后照片
     *
     * @return img - 维修后照片
     */
    public String getImg() {
        return img;
    }

    /**
     * 设置维修后照片
     *
     * @param img 维修后照片
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * 获取开始维修时间
     *
     * @return timeBegin - 开始维修时间
     */
    public Date getTimeBegin() {
        return timeBegin;
    }

    /**
     * 设置开始维修时间
     *
     * @param timeBegin 开始维修时间
     */
    public void setTimeBegin(Date timeBegin) {
        this.timeBegin = timeBegin;
    }

    /**
     * 获取预计结束时间
     *
     * @return timeEnd - 预计结束时间
     */
    public Date getTimeEnd() {
        return timeEnd;
    }

    /**
     * 设置预计结束时间
     *
     * @param timeEnd 预计结束时间
     */
    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
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