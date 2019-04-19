package com.yujj.layimjavaclient.socket.manager;

//import pojo.SocketUser;

import javax.websocket.Session;

import com.yujj.layimjavaclient.pojo.SocketUser;

/**
 * Created by pz on 16/11/23.
 */
public interface IUserManager {

    boolean addUser(SocketUser user);

    boolean removeUser(SocketUser user);

    int getOnlineCount();

    SocketUser getUser(int userId);

}
