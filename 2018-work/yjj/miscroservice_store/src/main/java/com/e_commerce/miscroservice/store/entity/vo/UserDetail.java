package com.e_commerce.miscroservice.store.entity.vo;

public interface UserDetail<T> {
    long getId();

    T getUserDetail();

    String getVirtualDeviceId();
}
