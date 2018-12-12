/**
 * 
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserReflect.java, 2016年2月26日 下午1:20:34
 */
@Entity
@Table(name = "userreflect")
public class UserReflect {

	private Integer userReflectId;

	private Integer userId;

	private Double userReflectMoney;
	private String isDeleted;

	private Date modifyTime;
	private Date createTime;
	private String userName;
	private String userAccount;

	@Id
	@GeneratedValue
	/**
	 * @return the userReflectId
	 */
	public Integer getUserReflectId() {
		return userReflectId;
	}

	@Column(length = 8)
	/**
	 * @return the isDeleted
	 */
	public String getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @param modifyTime
	 *            the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @return the userReflectMoney
	 */
	public Double getUserReflectMoney() {
		return userReflectMoney;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param userReflectId
	 *            the userReflectId to set
	 */
	public void setUserReflectId(Integer userReflectId) {
		this.userReflectId = userReflectId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @param userReflectMoney
	 *            the userReflectMoney to set
	 */
	public void setUserReflectMoney(Double userReflectMoney) {
		this.userReflectMoney = userReflectMoney;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(length = 128)
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	@Column(length = 215)
	/**
	 * @return the userAccount
	 */
	public String getUserAccount() {
		return userAccount;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param userAccount
	 *            the userAccount to set
	 */
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

}
