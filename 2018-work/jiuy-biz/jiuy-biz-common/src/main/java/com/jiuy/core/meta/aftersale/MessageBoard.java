package com.jiuy.core.meta.aftersale;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class MessageBoard extends BaseMeta<Long> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3334429696328838169L;
	
	private long id;
		
	private long serviceId;

	private long adminId;
	
	private String adminName;
	
	private String message;

	private int status;
	
	private long createTime;
	
	public MessageBoard(long serviceId2, long adminId2, String adminName2, String message2) {
		this.serviceId = serviceId2;
		this.adminId = adminId2;
		this.adminName = adminName2;
		this.message = message2;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
		
	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
	public Long getCacheId() {
		return null;
	}

}
