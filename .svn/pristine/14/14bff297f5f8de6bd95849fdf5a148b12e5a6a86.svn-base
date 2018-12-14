package com.zccbh.demand.pojo;

import com.google.gson.Gson;

import java.io.Serializable;

public class BaseModel implements Serializable{

    private static final long serialVersionUID = 4267072974485825627L;

    /**
     * 主键
     */
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
