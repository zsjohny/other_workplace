package com.jiuyuan.entity;

import java.io.Serializable;
/**
 * 用户分享记录
 * @author zhuzl
 *
 */
public class UserSharedClickRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4467330087148843098L;

	private long id;
	
	private long sharedId;
	
	private long sharedUserId;
	
	private long userId;
	
	private int type;
	
	private long relatedId;
	
	private int jiuCoin;
	
	private int status;
	
	private long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSharedId() {
		return sharedId;
	}

	public void setSharedId(long sharedId) {
		this.sharedId = sharedId;
	}

	public long getSharedUserId() {
		return sharedUserId;
	}

	public void setSharedUserId(long sharedUserId) {
		this.sharedUserId = sharedUserId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
	}

	public int getJiuCoin() {
		return jiuCoin;
	}

	public void setJiuCoin(int jiuCoin) {
		this.jiuCoin = jiuCoin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	
	@Override
	public String toString() {
		return "UserSharedClickRecord [id=" + id + ", sharedId=" + sharedId + ", sharedUserId=" + sharedUserId
				+ ", userId=" + userId + ", type=" + type + ", relatedId=" + relatedId + ", jiuCoin=" + jiuCoin
				+ ", status=" + status + ", createTime=" + createTime + "]";
	}

	
}
