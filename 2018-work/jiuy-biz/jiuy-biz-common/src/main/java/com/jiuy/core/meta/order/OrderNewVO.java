package com.jiuy.core.meta.order;

public class OrderNewVO extends OrderNew {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4944324437223548336L;
	

	private int buyCount;

	private String expressSupplier;

	private String expressOrderNo;

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
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
	
}
