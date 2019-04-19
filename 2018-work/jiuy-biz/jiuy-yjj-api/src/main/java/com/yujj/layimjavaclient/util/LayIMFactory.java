package com.yujj.layimjavaclient.util;

import com.yujj.layimjavaclient.socket.manager.IUserManager;
import com.yujj.layimjavaclient.socket.manager.UserManager;
import com.yujj.layimjavaclient.util.serializer.FastJsonSerializer;
import com.yujj.layimjavaclient.util.serializer.IJsonSerializer;

//import util.serializer.FastJsonSerializer;

//import socket.manager.IUserManager;
//import socket.manager.UserManager;
//import util.serializer.FastJsonSerializer;
////import util.serializer.IJsonSerializer;
//import util.serializer.IJsonSerializer;

/**
 * Created by pz on 16/11/23.
 */
public class LayIMFactory {
    //创建序列化器
    public static IJsonSerializer createSerializer(){
        return new FastJsonSerializer();
    }

    //创建在线人员管理工具
    public static IUserManager createManager(){
        return UserManager.getInstance();
    }



}
