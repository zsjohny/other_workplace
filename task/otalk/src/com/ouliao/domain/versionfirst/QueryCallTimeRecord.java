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
 * @version $Id: QueryCallTimeRecord.java, 2016年3月17日 下午8:37:26
 */
@Table(name = "querycalltimerecord")
@Entity
public class QueryCallTimeRecord {

	private Integer queryCallTimeRecordId;
	private Integer userCallId;
	private String userCallPhone;
	private String userCallNickName;
	private Integer userCalledId;
	private String userCalledPhone;
	private String userCalledNickName;
	private Double userCallCost;
	private Long callTime;
	private Date creatTime;

	@GeneratedValue
	@Id
	/**
	 * @return the queryCallTimeRecordId
	 */
	public Integer getQueryCallTimeRecordId() {
		return queryCallTimeRecordId;
	}

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

	/**
	 * @return the userCallId
	 */
	public Integer getUserCallId() {
		return userCallId;
	}

	@Column(length = 11)
	/**
	 * @return the userCallPhone
	 */
	public String getUserCallPhone() {
		return userCallPhone;
	}

	@Column(length = 128)
	/**
	 * @return the userCallNickName
	 */
	public String getUserCallNickName() {
		return userCallNickName;
	}

	/**
	 * @return the userCalledId
	 */
	public Integer getUserCalledId() {
		return userCalledId;
	}

	@Column(length = 11)
	/**
	 * @return the userCalledPhone
	 */
	public String getUserCalledPhone() {
		return userCalledPhone;
	}

	@Column(length = 128)
	/**
	 * @return the userCalledNickName
	 */
	public String getUserCalledNickName() {
		return userCalledNickName;
	}

	/**
	 * @return the creatTime
	 */
	public Date getCreatTime() {
		return creatTime;
	}

	/**
	 * @param queryCallTimeRecordId
	 *            the queryCallTimeRecordId to set
	 */
	public void setQueryCallTimeRecordId(Integer queryCallTimeRecordId) {
		this.queryCallTimeRecordId = queryCallTimeRecordId;
	}

	/**
	 * @param userCallId
	 *            the userCallId to set
	 */
	public void setUserCallId(Integer userCallId) {
		this.userCallId = userCallId;
	}

	/**
	 * @param userCallPhone
	 *            the userCallPhone to set
	 */
	public void setUserCallPhone(String userCallPhone) {
		this.userCallPhone = userCallPhone;
	}

	/**
	 * @param userCallNickName
	 *            the userCallNickName to set
	 */
	public void setUserCallNickName(String userCallNickName) {
		this.userCallNickName = userCallNickName;
	}

	/**
	 * @param userCalledId
	 *            the userCalledId to set
	 */
	public void setUserCalledId(Integer userCalledId) {
		this.userCalledId = userCalledId;
	}

	/**
	 * @param userCalledPhone
	 *            the userCalledPhone to set
	 */
	public void setUserCalledPhone(String userCalledPhone) {
		this.userCalledPhone = userCalledPhone;
	}

	/**
	 * @param userCalledNickName
	 *            the userCalledNickName to set
	 */
	public void setUserCalledNickName(String userCalledNickName) {
		this.userCalledNickName = userCalledNickName;
	}

	/**
	 * @return the callTime
	 */
	public Long getCallTime() {
		return callTime;
	}

	/**
	 * @param callTime
	 *            the callTime to set
	 */
	public void setCallTime(Long callTime) {
		this.callTime = callTime;
	}

	/**
	 * @param creatTime
	 *            the creatTime to set
	 */
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

}
