package com.zccbh.demand.pojo.common;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              意见反馈
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Suggestions extends BaseModel {
    private static final long serialVersionUID = -3447007192444311051L;

    /**
     * 反馈人名字
     */
    private String name;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 反馈端（1.车主端 2.商家端）
     */
    private Integer typeClient;
    /**
     * 反馈问题类型
     */
    private Integer typeQuestion;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 状态(是否知晓，是否解决)
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(Integer typeClient) {
        this.typeClient = typeClient;
    }

    public Integer getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(Integer typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Suggestions{" +
                "name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", typeClient=" + typeClient +
                ", typeQuestion=" + typeQuestion +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createAt=" + createAt +
                '}';
    }
}
