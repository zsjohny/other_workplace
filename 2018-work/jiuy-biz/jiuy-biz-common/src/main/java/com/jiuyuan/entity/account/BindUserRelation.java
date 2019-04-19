package com.jiuyuan.entity.account;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.account.ThirdAppType;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
/**
 * 绑定用户账号关系表
 * 
 * @author zhaoxinglin
 *
 * @version 2017年4月13日下午5:50:19
 */
public class BindUserRelation implements Serializable {

    private static final long serialVersionUID = -8864986366310346751L;

    
    private long id;//主键

    private long userId; //用户id

    private String openId; // 应用号Id

    private String uId;// 三方平台统一Id

    private ThirdAppType type;// 应用类型：0其他，1微信公众号1账号

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUId() {
		return uId;
	}

	public void setUId(String uId) {
		this.uId = uId;
	}

	public ThirdAppType getType() {
		return type;
	}

	public void setType(ThirdAppType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "BindUserRelation [id=" + id + ", userId=" + userId + ", openId=" + openId + ", uId=" + uId + ", type="
				+ type + "]";
	}

    
    
	
	
}
