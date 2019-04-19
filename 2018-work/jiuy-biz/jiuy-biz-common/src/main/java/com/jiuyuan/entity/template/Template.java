package com.jiuyuan.entity.template;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class Template extends BaseMeta<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7582088815245117666L;

	private long id;
	
	private long partnerId;
	
	private int Status;
	
	private long CreateTime;
	
	private long UpdateTime;
	
	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getPartnerId() {
		return partnerId;
	}



	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}



	public int getStatus() {
		return Status;
	}



	public void setStatus(int status) {
		Status = status;
	}



	public long getCreateTime() {
		return CreateTime;
	}



	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}



	public long getUpdateTime() {
		return UpdateTime;
	}



	public void setUpdateTime(long updateTime) {
		UpdateTime = updateTime;
	}



	@Override
	public String toString() {
		return "Template [id=" + id + ", partnerId=" + partnerId + ", Status=" + Status + ", CreateTime=" + CreateTime
				+ ", UpdateTime=" + UpdateTime + "]";
	}



	@Override
	public Long getCacheId() {
		return null;
	}
	
}
