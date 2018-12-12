package com.finace.miscroservice.borrow.po;

import com.finace.miscroservice.commons.entity.BasePage;

public class UserJiangPinPO extends BasePage{

    private Integer id;

    private String userId;

    private String jiangPinName;

    private String addTime;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJiangPinName() {
        return jiangPinName;
    }

    public void setJiangPinName(String jiangPinName) {
        this.jiangPinName = jiangPinName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
