package com.jiuyuan.entity.account;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class UserCoin extends BaseMeta<Long> implements Serializable {

    private static final long serialVersionUID = -2960266889540167523L;

    private long id;

    private long userId;

    private int avalCoins;

    private int unavalCoins;

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

    public int getAvalCoins() {
        return avalCoins;
    }

    public void setAvalCoins(int avalCoins) {
        this.avalCoins = avalCoins;
    }

    public int getUnavalCoins() {
        return unavalCoins;
    }

    public void setUnavalCoins(int unavalCoins) {
        this.unavalCoins = unavalCoins;
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

    public int getAllCoins() {
        return getAvalCoins() + getUnavalCoins();
    }

    @Override
    public Long getCacheId() {
        return this.id;
    }

}
