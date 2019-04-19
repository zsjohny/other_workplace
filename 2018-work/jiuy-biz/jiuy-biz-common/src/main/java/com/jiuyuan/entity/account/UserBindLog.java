package com.jiuyuan.entity.account;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jiuyuan.constant.account.UserCoinOperation;

public class UserBindLog implements Serializable {

    private static final long serialVersionUID = -6718137924763381737L;

    private long id;

    private long userId;

    private String oldPhone;

    private String newPhone;

    private String weixinId;

    private int type;

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

	public String getOldPhone() {
		return oldPhone;
	}

	public void setOldPhone(String oldPhone) {
		this.oldPhone = oldPhone;
	}

	public String getNewPhone() {
		return newPhone;
	}

	public void setNewPhone(String newPhone) {
		this.newPhone = newPhone;
	}

	public String getWeixinId() {
		return weixinId;
	}

	public void setWeixinId(String weixinId) {
		this.weixinId = weixinId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}



    
}
