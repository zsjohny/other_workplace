// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space
// Source File Name:   UserDao.groovy

package com.reliable.dao;


import com.reliable.domain.FindPhoneRecord
import com.reliable.domain.User;

public interface UserDao {

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    User findOne(User user);

    void saveUserFind(FindPhoneRecord findphonerecord);

    long queryCount();

    long queryAllCount();
}
