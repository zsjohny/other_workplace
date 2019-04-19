package com.jiuyuan.entity.newentity;

import java.io.Serializable;

import com.jiuyuan.entity.UserDetail;

public class DefaultGroundUserDetail implements UserDetail<GroundUser>,Serializable{
	
	
	private static final long serialVersionUID = -2926973577692618591L;
	
	private GroundUser groundUser;
	
	private String virtualDeviceId;

	@Override
	public long getId() {
		return getUserDetail() == null ? 0 : getUserDetail().getId();
	}

	@Override
	public GroundUser getUserDetail() {
		return groundUser;
	}
	
	public void setGroundUser(GroundUser groundUser) {
		this.groundUser = groundUser;
	}

	@Override
	public String getVirtualDeviceId() {
		return virtualDeviceId;
	}
	
	public void setVirtualDeviceId(String virtualDeviceId) {
		this.virtualDeviceId = virtualDeviceId;
	}

}
