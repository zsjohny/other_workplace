package com.store.entity.member;

import java.io.Serializable;

//import com.store.entity.StoreBusiness;
//import com.store.entity.UserDetail;



public class DefaultMemberUserDetail implements UserDetail, Serializable {

    private static final long serialVersionUID = -1104663920836940976L;

    private ShopMember member;

    private String virtualDeviceId;

    @Override
    public ShopMember getMember() {
        return member;
    }

    public void setMember(ShopMember member) {
        this.member = member;
    }

    @Override
    public long getId() {
        return getMember() == null ? 0 : getMember().getId();
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
