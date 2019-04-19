package com.jiuyuan.constant;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class ShopHomeTemplate extends BaseMeta<Long>  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 237135825375054646L;
	
	private Long id;
	private String content;
	private int status;
	private Long createTime;
	private Long updateTime;
	private String name;
	private String imgUrl;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return "HomeTemplate [id=" + id + ", content=" + content + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", name=" + name + ", imgUrl=" + imgUrl + "]";
	}
	@Override
	public Long getCacheId() {
		return null;
	}
	
	
}
