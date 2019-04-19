package com.jiuy.core.meta.account;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.BaseMeta;


public class User extends BaseMeta<Long> implements Serializable {

    private static final long serialVersionUID = -8864986366310346751L;

    private long userId;

    private String userName; // 用户名

    private String userRelatedName;// 用户关联账号：邮箱or手机号

    private UserType userType;// 用户类型：邮箱账号、手机号账号

    private String userNickname;

    private String userIcon;

    @JsonIgnore
    private String userPassword;

    @JsonIgnore
    private short userRole;

    @JsonIgnore
    private long userPoints;

    @JsonIgnore
    private UserStatus status;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

	String receiverName;
	
    String telephone;
    
    String addrFull;
    
    private String userDefinedLocations;

    private String accessToken;

    private long accessUpdateTime;

    private long accessValidTime;

    private String userCID;

    private long yJJNumber;

    private String bindPhone;

    private String bindWeixin;

    public String getAddrFull() {
		return addrFull;
	}

	public void setAddrFull(String addrFull) {
		this.addrFull = addrFull;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRelatedName() {
        return userRelatedName;
    }

    public void setUserRelatedName(String userRelatedName) {
        this.userRelatedName = userRelatedName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public short getUserRole() {
        return userRole;
    }

    public void setUserRole(short userRole) {
        this.userRole = userRole;
    }

    public long getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(long userPoints) {
        this.userPoints = userPoints;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserDefinedLocations() {
        return userDefinedLocations;
    }

    public void setUserDefinedLocations(String userDefinedLocations) {
        this.userDefinedLocations = userDefinedLocations;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getAccessUpdateTime() {
        return accessUpdateTime;
    }

    public void setAccessUpdateTime(long accessUpdateTime) {
        this.accessUpdateTime = accessUpdateTime;
    }

    public long getAccessValidTime() {
        return accessValidTime;
    }

    public void setAccessValidTime(long accessValidTime) {
        this.accessValidTime = accessValidTime;
    }

    public String getUserCID() {
        return userCID;
    }

    public void setUserCID(String userCID) {
        this.userCID = userCID;
    }

    public long getyJJNumber() {
        return yJJNumber;
    }

    public void setyJJNumber(long yJJNumber) {
        this.yJJNumber = yJJNumber;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getBindWeixin() {
        return bindWeixin;
    }

    public void setBindWeixin(String bindWeixin) {
        this.bindWeixin = bindWeixin;
    }

    @Override
    public Long getCacheId() {
        return this.userId;
    }

}
