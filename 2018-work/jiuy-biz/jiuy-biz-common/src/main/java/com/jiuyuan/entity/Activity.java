package com.jiuyuan.entity;

import java.io.Serializable;

public class Activity implements Serializable {

    private static final long serialVersionUID = 8415376689327404379L;

    private long activityId;

    private String activityCode;
    
    private String memo;

    private int status;

    private int limitCount;

    private int grantAmountMin;

    private int grantAmountMax;

    private long startTime;

    private long endTime;

    private long createTime;

    private long updateTime;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
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

    public boolean isEffective() {
        long time = System.currentTimeMillis();
        return startTime <= time && time <= endTime;
    }

    public boolean isUnlimit() {
        return limitCount <= 0;
    }

	public int getGrantAmountMin() {
		return grantAmountMin;
	}

	public void setGrantAmountMin(int grantAmountMin) {
		this.grantAmountMin = grantAmountMin;
	}

	public int getGrantAmountMax() {
		return grantAmountMax;
	}
	
	public int getGrantAmountRandom() {
		return (int)(grantAmountMin + Math.random()*(grantAmountMax-grantAmountMin+1));
	}

	public void setGrantAmountMax(int grantAmountMax) {
		this.grantAmountMax = grantAmountMax;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "Activity [activityId=" + activityId + ", activityCode=" + activityCode + ", memo=" + memo + ", status="
				+ status + ", limitCount=" + limitCount + ", grantAmountMin=" + grantAmountMin + ", grantAmountMax="
				+ grantAmountMax + ", startTime=" + startTime + ", endTime=" + endTime + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

	
}
