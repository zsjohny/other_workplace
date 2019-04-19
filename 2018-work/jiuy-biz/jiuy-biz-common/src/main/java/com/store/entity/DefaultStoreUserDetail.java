package com.store.entity;

import java.io.Serializable;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;



public class DefaultStoreUserDetail implements UserDetail<StoreBusiness>, Serializable {

    private static final long serialVersionUID = -1104663920836940976L;

    private StoreBusiness storeBusiness;

    private String virtualDeviceId;

    @Override
    public StoreBusiness getUserDetail() {
        return storeBusiness;
    }

    public void setStoreBusiness(StoreBusiness storeBusiness) {
        this.storeBusiness = storeBusiness;
    }

    @Override
    public long getId() {
        return getUserDetail() == null ? 0 : getUserDetail().getId();
    }

    @Override
    public String getVirtualDeviceId() {
        return virtualDeviceId;
    }

    public void setVirtualDeviceId(String virtualDeviceId) {
        this.virtualDeviceId = virtualDeviceId;
    }

    @Override
    public String toString() {
        return "" + getId();
    }
}
