package com.jiuyuan.entity.account;

import java.io.Serializable;

public class MailRegister implements Serializable {

    private static final long serialVersionUID = 2864146777350141402L;

    private String id;

    private String registerUuid;

    private String userName;
    
    private String userEmail;
    
    private String userPassword;
    
    private long expireTime;

    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisterUuid() {
        return registerUuid;
    }

    public void setRegisterUuid(String registerUuid) {
        this.registerUuid = registerUuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}
