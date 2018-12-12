package com.finace.miscroservice.activity.po;

/**
 * 红包日志实体类
 */
public class HongbaoLogPO {

    private int id;

    private int userId;

    private Double hongbaoMoney;

    private String remark;

    private String addtime;

    private int activeId;

    private Integer status;

    private String userTotal;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Double getHongbaoMoney() {
        return hongbaoMoney;
    }

    public void setHongbaoMoney(Double hongbaoMoney) {
        this.hongbaoMoney = hongbaoMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserTotal() {
        return userTotal;
    }

    public void setUserTotal(String userTotal) {
        this.userTotal = userTotal;
    }
}
