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
 * @version $Id: SysMsgShow.java, 2016年3月13日 下午6:49:07
 */
@Table(name = "sysmsgshow")
@Entity
public class SysMsgShow {

	private Long sysMsgShowId;

	private String msg;
	private Integer userId;
	private Date creatTime;
	private String isDeleted;

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@GeneratedValue
	@Id
	/**
	 * @return the sysMsgShowId
	 */
	public Long getSysMsgShowId() {
		return sysMsgShowId;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param sysMsgShowId
	 *            the sysMsgShowId to set
	 */
	public void setSysMsgShowId(Long sysMsgShowId) {
		this.sysMsgShowId = sysMsgShowId;
	}

	@Column(length = 512)
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @return the creatTime
	 */
	public Date getCreatTime() {
		return creatTime;
	}

	@Column(length = 8)
	/**
	 * @return the isDeleted
	 */
	public String getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @param creatTime
	 *            the creatTime to set
	 */
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

}
