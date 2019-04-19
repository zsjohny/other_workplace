package com.jiuyuan.entity.qianmi;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public class QMOrder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8860281170713685889L;

	private Long orderNo;
	
	/**
	 * 千米订单编号，T开头
	 */
	private String tid;
	
	private String buyerNick;
	
	private Integer orderStatus;
	
	private Double totalMoney;
	
	private Double totalPay;
	
	private Long mergedId;
	
	private Long lOWarehouseId;
	
	private String expressInfo;
	
	private String phone;
	
	private String mobile;
	
	private Long createTime;
	
	private Long updateTime;
	
	private Long qMCreateTime;
	
	private Long qMUpdateTime;

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(Double totalPay) {
		this.totalPay = totalPay;
	}

	public Long getMergedId() {
		return mergedId;
	}

	public void setMergedId(Long mergedId) {
		this.mergedId = mergedId;
	}

	public Long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(Long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

	public String getExpressInfo() {
		return expressInfo;
	}

	public void setExpressInfo(String expressInfo) {
		this.expressInfo = expressInfo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Long getqMCreateTime() {
		return qMCreateTime;
	}

	public void setqMCreateTime(Long qMCreateTime) {
		this.qMCreateTime = qMCreateTime;
	}

	public Long getqMUpdateTime() {
		return qMUpdateTime;
	}

	public void setqMUpdateTime(Long qMUpdateTime) {
		this.qMUpdateTime = qMUpdateTime;
	}
	
	
}
