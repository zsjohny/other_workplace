package com.jiuy.core.meta.account;

import java.io.Serializable;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.BaseMeta;

public class UserCoinLog extends BaseMeta<Long> implements Serializable {

    private static final long serialVersionUID = -6718137924763381737L;

    private long id;

    private long userId;

    private UserCoinOperation operation;

    private int oldAvalCoins;

    private int newAvalCoins;

    private int oldUnavalCoins;

    private int newUnavalCoins;

    private String relatedId;

    private long createTime;

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

    public UserCoinOperation getOperation() {
        return operation;
    }

    public void setOperation(UserCoinOperation operation) {
        this.operation = operation;
    }

    public int getOldAvalCoins() {
        return oldAvalCoins;
    }

    public void setOldAvalCoins(int oldAvalCoins) {
        this.oldAvalCoins = oldAvalCoins;
    }

    public int getNewAvalCoins() {
        return newAvalCoins;
    }

    public void setNewAvalCoins(int newAvalCoins) {
        this.newAvalCoins = newAvalCoins;
    }

    public int getOldUnavalCoins() {
        return oldUnavalCoins;
    }

    public void setOldUnavalCoins(int oldUnavalCoins) {
        this.oldUnavalCoins = oldUnavalCoins;
    }

    public int getNewUnavalCoins() {
        return newUnavalCoins;
    }

    public void setNewUnavalCoins(int newUnavalCoins) {
        this.newUnavalCoins = newUnavalCoins;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public Long getCacheId() {
        return this.id;
    }

}
