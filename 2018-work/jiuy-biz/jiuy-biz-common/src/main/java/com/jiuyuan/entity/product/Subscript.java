package com.jiuyuan.entity.product;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.BaseMeta;

/**
 * 角标
 */
public class Subscript extends BaseMeta<Long> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 700953114600544930L;


	public Subscript(){
		super();
	}
	
	public Subscript(long id, String name, String logo, int productSum, String description, int status) {
		long time = System.currentTimeMillis();
		
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.productSum = productSum;
		this.description = description;
		this.status = status;
		this.createTime = time;
		this.updateTime = time;
	}

	private long id;
	
	private String name;
	
	private String logo;
	
	private int productSum;
	
	private String description;
	
	private int status;
	
	@JsonIgnore
	private long createTime;
	
	@JsonIgnore
	private long updateTime;
	
	

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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getProductSum() {
		return productSum;
	}

	public void setProductSum(int productSum) {
		this.productSum = productSum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Long getCacheId() {
		return null;
	}
	
}
