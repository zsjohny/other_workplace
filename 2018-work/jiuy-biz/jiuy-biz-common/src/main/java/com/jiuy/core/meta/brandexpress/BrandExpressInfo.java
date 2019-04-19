package com.jiuy.core.meta.brandexpress;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2017年1月9日 下午2:50:47
 * 
 */

public class BrandExpressInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7327501134671526182L;

	private long id;
	
	private long orderNo;
	
	private String expressSupplier;
	
	private String expressOrderNo;
	
	private long expressUpdateTime;
	
	private long createTime;
	
	private long updateTime;
	
	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public String getExpressSupplier() {
		return expressSupplier;
	}

	public void setExpressSupplier(String expressSupplier) {
		this.expressSupplier = expressSupplier;
	}

	public String getExpressOrderNo() {
		return expressOrderNo;
	}

	public void setExpressOrderNo(String expressOrderNo) {
		this.expressOrderNo = expressOrderNo;
	}

	public long getExpressUpdateTime() {
		return expressUpdateTime;
	}

	public void setExpressUpdateTime(long expressUpdateTime) {
		this.expressUpdateTime = expressUpdateTime;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
    
}
