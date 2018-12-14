package com.zccbh.demand.pojo.merchants;

import com.zccbh.util.base.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cbh_suggestions")
public class CbhSuggestions {
    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * name
     */
    private String name;

    /**
     * 联系方式
     */
    private String tel;

    /**
     *  意见建议反馈的B端或C端用户ID
     */
    @Column(name = "suggestionsId")
    private Integer suggestionsId;

    /**
     *  反馈端（1.车主端 2.商家端）
     */
    @Column(name = "typeClient")
    private Integer typeClien=2;

    /**
     *  类型 10 功能异常 20 体验问题 30新功能建议 40其他
     */
    @Column(name = "typeQuestion")
    private Integer typeQuestion;
    /**
     * 图片
     */
    private String img;


    /**
     * 状态
     */
    private Integer status=1;

    /**
     * 创建时间
     */
    @Column(name = "createAt")
    private Date createAt= DateUtils.getDateTime();

    /**
     * 建议内容
     */
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取联系方式
     *
     * @return tel - 联系方式
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置联系方式
     *
     * @param tel 联系方式
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取图片
     *
     * @return img - 图片
     */
    public String getImg() {
        return img;
    }

    /**
     * 设置图片
     *
     * @param img 图片
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取建议内容
     *
     * @return content - 建议内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置建议内容
     *
     * @param content 建议内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeClien() {
        return typeClien;
    }

    public void setTypeClien(Integer typeClien) {
        this.typeClien = typeClien;
    }

    public Integer getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(Integer typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public Integer getSuggestionsId() {
        return suggestionsId;
    }

    public void setSuggestionsId(Integer suggestionsId) {
        this.suggestionsId = suggestionsId;
    }
}