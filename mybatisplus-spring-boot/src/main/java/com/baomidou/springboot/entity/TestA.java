package com.baomidou.springboot.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

public class TestA extends Model {

    private Integer id;
    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
