package com.jiuy.core.meta.global;
/**
* @author WuWanjian
* @version 创建时间: 2016年11月4日 下午1:59:44
*/
public class ProductSeasonWeight {
	private String name;
	private long property_value_id;
	private double weight;
	private int status;
	private long createTime;
	private long updateTime;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getProperty_value_id() {
		return property_value_id;
	}
	public void setProperty_value_id(long property_value_id) {
		this.property_value_id = property_value_id;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
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
	
}
