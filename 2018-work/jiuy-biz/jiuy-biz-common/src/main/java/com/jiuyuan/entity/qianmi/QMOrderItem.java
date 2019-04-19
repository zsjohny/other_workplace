package com.jiuyuan.entity.qianmi;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public class QMOrderItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1949652688414225132L;

	private Long id;
	
	private String tid;
	
	private String buyerNick;
	
	private Integer buyCount;
	
	/**
	 * 千米的商品单编号,用于回写物流
	 */
	private String oid;
	
	private Long skuId;
	
	/**
	 * 千米skuId
	 */
	private String qMSkuId;
	
	private Double totalMoney;
	
	private Double totalPay;
	
	private String numIid;
	
	private Integer status;
	
	private Long createTime;
	
	private Long updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getqMSkuId() {
		return qMSkuId;
	}

	public void setqMSkuId(String qMSkuId) {
		this.qMSkuId = qMSkuId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	public String getNumIid() {
		return numIid;
	}

	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}

	
}
