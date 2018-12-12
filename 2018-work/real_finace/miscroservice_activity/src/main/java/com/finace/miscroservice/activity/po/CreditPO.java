package com.finace.miscroservice.activity.po;


import java.io.Serializable;

public class CreditPO implements Serializable {

	/**
     * credit.user_id
     * 会员ID
     */
    private long userId;

    /**
     * credit.value
     * 金豆数值
     */
    private int value;

    /**
     * credit.op_user
     * 操作者
     */
    private int opUser;

    /**
     * credit.addtime
     * 添加时间
     */
    private long addtime;

    /**
     * credit.addip
     * 添加IP
     */
    private String addip;

    /**
     * credit.updatetime
     * 最后更新时间
     */
    private String updatetime;

    /**
     * credit.updateip
     * 最后更新ID
     */
    private String updateip;

    /**
     * credit.tender_value
     * 
     */
    private int tenderValue;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getOpUser() {
		return opUser;
	}

	public void setOpUser(int opUser) {
		this.opUser = opUser;
	}

	public long getAddtime() {
		return addtime;
	}

	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getUpdateip() {
		return updateip;
	}

	public void setUpdateip(String updateip) {
		this.updateip = updateip;
	}

	public int getTenderValue() {
		return tenderValue;
	}

	public void setTenderValue(int tenderValue) {
		this.tenderValue = tenderValue;
	}

    
}