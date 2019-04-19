package com.jiuyuan.entity;

import java.io.Serializable;

public class UserSign implements Serializable {

    private static final long serialVersionUID = -684707871324140601L;

    private long id;

    private long userId;

    private int dayTime;

    private int mondayTime;

    private int weekDay;

    private int grantCoins;

    private long createTime;

    private long updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public int getMondayTime() {
        return mondayTime;
    }

    public void setMondayTime(int mondayTime) {
        this.mondayTime = mondayTime;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getGrantCoins() {
        return grantCoins;
    }

    public void setGrantCoins(int grantCoins) {
        this.grantCoins = grantCoins;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

}
