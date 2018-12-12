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
 * @version $Id: UserCallRoom.java, 2016年3月1日 下午6:09:19
 */

@Entity
@Table(name = "usercallroom")

public class UserCallRoom {

	private Integer userCallRoomId;
	private Integer userId;
	private Integer userCalledId;

	private Long userRoomNum;

	private Date userCreateTime;
	private Date userModifyTime;

	private int version;

	@Version
	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	@Id
	@GeneratedValue

	/**
	 * @return the userCallRoomId
	 */
	public Integer getUserCallRoomId() {
		return userCallRoomId;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @return the userCalledId
	 */
	public Integer getUserCalledId() {
		return userCalledId;
	}

	/**
	 * @return the userRoomNum
	 */
	public Long getUserRoomNum() {
		return userRoomNum;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @param userCalledId
	 *            the userCalledId to set
	 */
	public void setUserCalledId(Integer userCalledId) {
		this.userCalledId = userCalledId;
	}

	/**
	 * @param userRoomNum
	 *            the userRoomNum to set
	 */
	public void setUserRoomNum(Long userRoomNum) {
		this.userRoomNum = userRoomNum;
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
	 * @param userCallRoomId
	 *            the userCallRoomId to set
	 */
	public void setUserCallRoomId(Integer userCallRoomId) {
		this.userCallRoomId = userCallRoomId;
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
