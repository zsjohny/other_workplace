package com.jiuyuan.entity.account;

import java.io.Serializable;

import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;

public class DefaultUserDetail implements UserDetail, Serializable {

    private static final long serialVersionUID = -1104663920836940976L;

    private User user;

    private String virtualDeviceId;

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public long getUserId() {
        return getUser() == null ? 0 : getUser().getUserId();
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
        return "" + getUserId();
    }
}
