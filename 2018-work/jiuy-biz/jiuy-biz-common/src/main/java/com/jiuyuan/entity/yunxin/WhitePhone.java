/**
 * 
 */
package com.jiuyuan.entity.yunxin;

import java.io.Serializable;

/**
* @author DongZhong
* @version 创建时间: 2016年12月28日 下午2:34:44
*/
public class WhitePhone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4524001535095058798L;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	private long id;
	private String name;
	private String phone;
	private long createTime;
	
	
}
