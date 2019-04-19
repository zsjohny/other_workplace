/**
 * 
 */
package com.jiuy.core.meta.admin;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.account.UserStatus;

public class AdminUser implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8839658191781144696L;

    private Long userId;

    private String userName; // 用户名

    @JsonIgnore
    private String userPassword;

    private Long roleId;
    
    private String userDeviceMac;
    
    private Long createTime;

    private Long updateTime;
    
    private String phone;
    
    private Integer status;
    
    private String userRealName;
    
    private String userDepartment;
    
    private String userJob;
    
    private String userPhone;

    public AdminUser() {
    }
    
    public AdminUser(String userName, Long roleId) {
		this.userName = userName;
		this.roleId = roleId;
	}
    
	public AdminUser(String userName, Long roleId, String userRealName, String userDepartment,
			String userJob, String userPhone) {
		this.userName = userName;
		this.roleId = roleId;
		this.userRealName = userRealName;
		this.userDepartment = userDepartment;
		this.userJob = userJob;
		this.userPhone = userPhone;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getUserDeviceMac() {
		return userDeviceMac;
	}

	public void setUserDeviceMac(String userDeviceMac) {
		this.userDeviceMac = userDeviceMac;
	}

	public UserStatus getUserStatus() {
		return UserStatus.getByIntValue(status);
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getUserDepartment() {
		return userDepartment;
	}

	public void setUserDepartment(String userDepartment) {
		this.userDepartment = userDepartment;
	}

	public String getUserJob() {
		return userJob;
	}

	public void setUserJob(String userJob) {
		this.userJob = userJob;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
    
	
}
