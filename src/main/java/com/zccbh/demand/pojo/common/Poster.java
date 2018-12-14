package com.zccbh.demand.pojo.common;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              首页轮播图（活动海报）
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Poster extends BaseModel {
    private static final long serialVersionUID = 4907663294571669430L;

    /**
     * 标题
     */
    private String title;
    /**
     * 点击时跳转的链接
     */
    private String linkTitle;
    /**
     * 图片链接
     */
    private String linkImg;
    /**
     * 活动内容
     */
    private String content;
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

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Poster{" +
                "title='" + title + '\'' +
                ", linkTitle='" + linkTitle + '\'' +
                ", linkImg='" + linkImg + '\'' +
                ", content='" + content + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
