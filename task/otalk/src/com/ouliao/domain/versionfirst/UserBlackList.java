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
 * @version $Id: UserBlackList.java, 2016年2月23日 下午6:03:30
 */
@Entity
@Table(name = "userblacklist")
public class UserBlackList {
	private Integer userBlackListId;
	private Integer userId;
	private Integer userBlackId;

	private String isDeleted;
	private Date userCreateTime;
	private Date userModifyTime;

	@Id
	@GeneratedValue
	/**
	 * @return the userBlackListId
	 */
	public Integer getUserBlackListId() {
		return userBlackListId;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @return the userBlackId
	 */
	public Integer getUserBlackId() {
		return userBlackId;
	}

	@Column(length = 4)
	/**
	 * @return the isDeleted
	 */
	public String getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @return the userCreateTime
	 */
	public Date getUserCreateTime() {
		return userCreateTime;
	}

	/**
	 * @return the userModifyTime
	 */
	public Date getUserModifyTime() {
		return userModifyTime;
	}

	/**
	 * @param userBlackListId
	 *            the userBlackListId to set
	 */
	public void setUserBlackListId(Integer userBlackListId) {
		this.userBlackListId = userBlackListId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @param userBlackId
	 *            the userBlackId to set
	 */
	public void setUserBlackId(Integer userBlackId) {
		this.userBlackId = userBlackId;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @param userCreateTime
	 *            the userCreateTime to set
	 */
	public void setUserCreateTime(Date userCreateTime) {
		this.userCreateTime = userCreateTime;
	}

	/**
	 * @param userModifyTime
	 *            the userModifyTime to set
	 */
	public void setUserModifyTime(Date userModifyTime) {
		this.userModifyTime = userModifyTime;
	}

}
