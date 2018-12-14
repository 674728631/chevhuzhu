package com.zccbh.demand.pojo.common;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              车V互助信息表
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class News extends BaseModel {
    private static final long serialVersionUID = -8648648992045575555L;

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 图片链接
     */
    private String linkImg;
    /**
     * 创建时间
     */
    private Date createAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", linkImg='" + linkImg + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
