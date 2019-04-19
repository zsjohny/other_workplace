package com.jiuyuan.entity;

public interface UserDetail<T> {

    long getId();

	T getUserDetail();

    String getVirtualDeviceId();
}
