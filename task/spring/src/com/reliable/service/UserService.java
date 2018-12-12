// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UserService.groovy

package com.reliable.service;


import com.reliable.domain.FindPhoneRecord;
import com.reliable.domain.User;

public interface UserService {

    public abstract void saveUser(User user);

    public abstract void updateUser(User user);

    public abstract void deleteUser(User user);

    public abstract User findOne(User user);

    public abstract void saveUserFind(FindPhoneRecord findphonerecord);

    public abstract long queryCount();

    public abstract long queryAllCount();
}
