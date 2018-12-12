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
 * @version $Id: UserCallMark.java, 2016年2月27日 上午7:24:43
 */
@Entity
@Table(name = "usercallmark")
public class UserCallMark {
	private Integer userCallMarkId;
	private Integer userCalledId;
	private Integer userId;
	private String userCallTime;
	private Double userCallCost;
	private Double userCallEarn;
	private String isDeleted;
	private String isScore;
	private Date userCreateTime;
	private Date userModifyTime;

	@Id
	@GeneratedValue

	/**
	 * @return the userCallMarkId
	 */
	public Integer getUserCallMarkId() {
		return userCallMarkId;
	}

	/**
	 * @return the userCallEarn
	 */
	public Double getUserCallEarn() {
		return userCallEarn;
	}

	/**
	 * @param userCallEarn
	 *            the userCallEarn to set
	 */
	public void setUserCallEarn(Double userCallEarn) {
		this.userCallEarn = userCallEarn;
	}

	@Column(length = 6)
	/**
	 * @return the isScore
	 */
	public String getIsScore() {
		return isScore;
	}

	/**
	 * @param isScore
	 *            the isScore to set
	 */
	public void setIsScore(String isScore) {
		this.isScore = isScore;
	}

	/**
	 * @param userCallMarkId
	 *            the userCallMarkId to set
	 */
	public void setUserCallMarkId(Integer userCallMarkId) {
		this.userCallMarkId = userCallMarkId;
	}

	@Column(length = 6)
	/**
	 * @return the userCallCost
	 */
	public Double getUserCallCost() {
		return userCallCost;
	}

	/**
	 * @param userCallCost
	 *            the userCallCost to set
	 */
	public void setUserCallCost(Double userCallCost) {
		this.userCallCost = userCallCost;
	}

	@Column(length = 128)
	/**
	 * @return the userCallTime
	 */
	public String getUserCallTime() {
		return userCallTime;
	}

	/**
	 * @param userCallTime
	 *            the userCallTime to set
	 */
	public void setUserCallTime(String userCallTime) {
		this.userCallTime = userCallTime;
	}

	/**
	 * @return the userCalledId
	 */
	public Integer getUserCalledId() {
		return userCalledId;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	@Column(length = 8)
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
	 * @param userCalledId
	 *            the userCalledId to set
	 */
	public void setUserCalledId(Integer userCalledId) {
		this.userCalledId = userCalledId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
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
