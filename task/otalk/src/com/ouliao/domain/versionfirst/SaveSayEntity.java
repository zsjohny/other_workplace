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

public class SaveSayEntity {
	private String phone;
	private String userContent;
	private String createTime;
	private Integer id;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the userContent
	 */
	public String getUserContent() {
		return userContent;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @param userContent
	 *            the userContent to set
	 */
	public void setUserContent(String userContent) {
		this.userContent = userContent;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
