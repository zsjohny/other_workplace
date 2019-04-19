package com.yujj.entity.account;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.account.MailRegister;


public class User implements Serializable {

    private static final long serialVersionUID = -8864986366310346751L;
    
    /** 用户id */
    private long userId;
    /**  */
    private long yJJNumber;
    /** 用户名 */
    private String userName; // 用户名
    /** 用户关联账号：邮箱、手机号 */
    private String userRelatedName;// 用户关联账号：邮箱or手机号
    /** 用户账号类型：0邮箱账号，1手机号账号,  2微信账号 */
    private UserType userType;// 用户类型：邮箱账号、手机号账号
    /** 用户昵称 */
    private String userNickname;
    /** 用户头像 */
    private String userIcon;
    /**  */
    private String UserCID;
    /** 绑定手机号 */
    private String bindPhone;
    /** 绑定微信 */
    private String bindWeixin;
    /**  */
    private int inviteCount;
    /**  */
    private int weekInviteCount;
    /**  */
    private long lastInviteTime;
    /**  */
    private int weekInviteOrderCount;
    /**  */
    private long lastInviteOrderTime;
    /** 密码 */
    @JsonIgnore
    private String userPassword;
    /** 角色，默认0-消费者，二进制 */
    @JsonIgnore
    private short userRole;
    /** 用户积分 */
    @JsonIgnore
    private long userPoints;
    /** 状态0正常，其他 */
    @JsonIgnore
    private UserStatus status;
    /**  */
    @JsonIgnore
    private long createTime;
    /**  */
    @JsonIgnore
    private long updateTime;
    
    /** 注册来源 0：app ios，1：app android， 2：web */
    private int registrationSource;

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
    
    public int getUserPasswordLength() {
    	if(userPassword == null){
    		return 0;
    	}
    	return userPassword.trim().length();
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
    

    public String getUserCID() {
		return UserCID;
	}

	public void setUserCID(String userCID) {
		UserCID = userCID;
	}

	public static User coryFrom(MailRegister mailRegister) {
        User user = new User();
        user.setUserName(mailRegister.getUserName());
        user.setUserRelatedName(mailRegister.getUserEmail());
        user.setUserType(UserType.EMAIL);
        user.setUserPassword(mailRegister.getUserPassword());
        user.setStatus(UserStatus.NORMAL);
        long time = System.currentTimeMillis();
        user.setCreateTime(time);
        user.setUpdateTime(time);
        return user;
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

	public int getInviteCount() {
		return inviteCount;
	}

	public void setInviteCount(int inviteCount) {
		this.inviteCount = inviteCount;
	}

	public int getWeekInviteCount() {
		return weekInviteCount;
	}

	public void setWeekInviteCount(int weekInviteCount) {
		this.weekInviteCount = weekInviteCount;
	}

	public long getLastInviteTime() {
		return lastInviteTime;
	}

	public void setLastInviteTime(long lastInviteTime) {
		this.lastInviteTime = lastInviteTime;
	}

	public int getWeekInviteOrderCount() {
		return weekInviteOrderCount;
	}

	public void setWeekInviteOrderCount(int weekInviteOrderCount) {
		this.weekInviteOrderCount = weekInviteOrderCount;
	}

	public long getLastInviteOrderTime() {
		return lastInviteOrderTime;
	}

	public void setLastInviteOrderTime(long lastInviteOrderTime) {
		this.lastInviteOrderTime = lastInviteOrderTime;
	}
	
	public String getUserPhone() {
		if (getUserType() == UserType.PHONE) {
			return getUserName();
		} else {
			return getBindPhone();
		}
	}

	public int getRegistrationSource() {
		return registrationSource;
	}

	public void setRegistrationSource(int registrationSource) {
		this.registrationSource = registrationSource;
	}
	/**
	 * //绑定手机字段为空且是微信用户类型则取userName字段
	 * @return
	 */
	public String getWeixinId(){
		String bindWeixin = this.bindWeixin;
	     //绑定手机字段为空且是微信用户类型则取userName字段
	     if(StringUtils.isEmpty(bindWeixin) && this.userType.getIntValue() == UserType.WEIXIN.getIntValue()){
	    	 bindWeixin = this.userName;
	     }
	     return bindWeixin;
	}
	/**
	 * 判断是否已经绑定手机
	 * @return
	 */
	public boolean checkBindPhone() {
		boolean isBind = false;
		if(StringUtils.isNotEmpty(this.bindPhone)){
			isBind = true;
		}else if( this.userType.getIntValue() == UserType.PHONE.getIntValue()){
			isBind = true;
		}
		return isBind;
	}

	
	
	
	 
	/*
	CREATE TABLE `yjj_User` (
			  `UserId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
			  `UserName` varchar(100) NOT NULL COMMENT '用户名',
			  `UserRelatedName` varchar(100) NOT NULL COMMENT '用户关联账号：邮箱、手机号..',
			  `UserType` tinyint(4) unsigned DEFAULT '0' COMMENT '用户账号类型：0邮箱账号，1手机号账号,  2微信账号',
			  `RegistrationSource` tinyint(4) DEFAULT '0' COMMENT '注册来源 0：app ios，1：app android， 2：web',
			  `UserNickname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
			  `UserIcon` varchar(500) DEFAULT NULL COMMENT '用户头像',
			  `UserPassword` varchar(50) NOT NULL COMMENT '密码',
			  `UserRole` tinyint(4) DEFAULT '0' COMMENT '角色，默认0-消费者，二进制',
			  `UserPoints` bigint(20) DEFAULT '0' COMMENT '用户积分',
			  `Status` tinyint(4) DEFAULT '0' COMMENT '状态0正常，其他..',
			  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
			  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
			  `UserDefinedLocations` varchar(2048) DEFAULT NULL COMMENT 'json格式，用户定义的收获地址，定义格式为{recipients:"",location:"||||",detail:"",code:"",phone:"",fixnumber:"",default:""}',
			  `AccessToken` varchar(64) DEFAULT NULL COMMENT 'accesstoken',
			  `AccessUpdateTime` bigint(20) DEFAULT '0' COMMENT '访问token创建时间',
			  `AccessValidTime` bigint(20) DEFAULT '0' COMMENT '访问token有效时间',
			  `UserCID` varchar(45) DEFAULT NULL,
			  `YJJNumber` bigint(20) DEFAULT NULL,
			  `BindPhone` varchar(45) DEFAULT NULL,
			  `BindWeixin` varchar(100) DEFAULT NULL,
			  `InviteCount` int(11) DEFAULT '0' COMMENT '邀请人数 ',
			  `WeekInviteCount` int(11) DEFAULT '0' COMMENT '这周邀请的人数',
			  `LastInviteTime` bigint(20) DEFAULT '0' COMMENT '上一次邀请时间',
			  `WeekInviteOrderCount` int(11) DEFAULT '0' COMMENT '邀请有奖设置-模板2 : 该周邀请有礼模板2规则领奖次数',
			  `LastInviteOrderTime` bigint(20) DEFAULT '0' COMMENT '邀请有奖设置-模板2 : 该周邀请有礼模板2规则上次领奖时间',
			  PRIMARY KEY (`UserId`),
			  UNIQUE KEY `uk_user_name` (`UserName`),
			  UNIQUE KEY `uk_user_related_name` (`UserRelatedName`),
			  KEY `idx_user_accesstoken` (`AccessToken`)
			) ENGINE=InnoDB AUTO_INCREMENT=224465 DEFAULT CHARSET=utf8 COMMENT='用户表';
*/

	
}
