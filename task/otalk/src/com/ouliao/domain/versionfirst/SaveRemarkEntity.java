/**
 * 
 */
package com.ouliao.domain.versionfirst;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: SaveSayEntity.java, 2016年3月12日 下午2:04:10
 */

public class SaveRemarkEntity {
	private String phone;
	private String userRemark;
	private String createTime;
	private Integer id;

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the userRemark
	 */
	public String getUserRemark() {
		return userRemark;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @param userRemark
	 *            the userRemark to set
	 */
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
