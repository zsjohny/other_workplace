/**
 * 
 */
package com.store.entity;

import java.io.Serializable;

/**
* @author DongZhong
* @version 创建时间: 2016年9月23日 上午8:48:10
*/
public class StoreSearchLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6970395155987478303L;

	private long id;
	private long storeBusinessId;
	private String content;
	private long createTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getStoreBusinessId() {
		return storeBusinessId;
	}
	public void setStoreBusinessId(long storeBusinessId) {
		this.storeBusinessId = storeBusinessId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
