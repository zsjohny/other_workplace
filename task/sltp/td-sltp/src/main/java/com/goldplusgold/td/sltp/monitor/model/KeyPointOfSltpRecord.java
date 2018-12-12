package com.goldplusgold.td.sltp.monitor.model;

/**
 * 止盈止损关键点记录
 * Created by Administrator on 2017/5/12.
 */
public class KeyPointOfSltpRecord {
    /** 止盈止损记录ID */
    private String uuid;
    /** 用户ID */
    private String userId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "KeyPointOfSltpRecord{" +
                "uuid='" + uuid + '\'' +
                ", userId=" + userId +
                '}';
    }
}
