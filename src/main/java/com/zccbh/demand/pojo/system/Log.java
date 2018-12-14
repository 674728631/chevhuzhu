package com.zccbh.demand.pojo.system;

import com.zccbh.demand.pojo.BaseModel;

import java.util.Date;

/**
 * @Comments:              系统_日志
 * @Author:                LiangJie
 * @Create Date:           2018年2月28日
 */
public class Log extends BaseModel {
    private static final long serialVersionUID = -5575293369371520606L;

    /**
     * 操作人
     */
    private String practitioner;
    /**
     * 操作端ip
     */
    private String ip;
    /**
     * 操作描述
     */
    private String perform;
    /**
     * 请求URL
     */
    private String url;
    /**
     * 请求参数
     */
    private String pram;
    /**
     * 创建时间
     */
    private Date createAt;

    public String getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPerform() {
        return perform;
    }

    public void setPerform(String perform) {
        this.perform = perform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPram() {
        return pram;
    }

    public void setPram(String pram) {
        this.pram = pram;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Log{" +
                "practitioner='" + practitioner + '\'' +
                "ip='" + ip + '\'' +
                ", perform='" + perform + '\'' +
                ", url='" + url + '\'' +
                ", pram='" + pram + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
