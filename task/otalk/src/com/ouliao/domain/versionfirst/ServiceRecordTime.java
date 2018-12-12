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
 * @version $Id: ServiceRecordTime.java, 2016年3月10日 下午5:12:14
 */
@Table(name = "userfreetime")
@Entity
public class ServiceRecordTime {

	private Integer serviceRecordTimeId;
	private Integer userId;
	private Long userCallTime;
	private String isSysSend;
	private Date creatTime;

	@GeneratedValue
	@Id
	/**
	 * @return the serviceRecordTimeId
	 */
	public Integer getServiceRecordTimeId() {
		return serviceRecordTimeId;
	}

	@Column(length = 8)
	/**
	 * @return the isSysSend
	 */
	public String getIsSysSend() {
		return isSysSend;
	}

	/**
	 * @param isSysSend
	 *            the isSysSend to set
	 */
	public void setIsSysSend(String isSysSend) {
		this.isSysSend = isSysSend;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @return the userCallTime
	 */
	public Long getUserCallTime() {
		return userCallTime;
	}

	/**
	 * @return the creatTime
	 */
	public Date getCreatTime() {
		return creatTime;
	}

	/**
	 * @param serviceRecordTimeId
	 *            the serviceRecordTimeId to set
	 */
	public void setServiceRecordTimeId(Integer serviceRecordTimeId) {
		this.serviceRecordTimeId = serviceRecordTimeId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @param userCallTime
	 *            the userCallTime to set
	 */
	public void setUserCallTime(Long userCallTime) {
		this.userCallTime = userCallTime;
	}

	/**
	 * @param creatTime
	 *            the creatTime to set
	 */
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

}
