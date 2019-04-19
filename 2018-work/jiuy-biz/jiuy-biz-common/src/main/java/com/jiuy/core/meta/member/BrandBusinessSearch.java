package com.jiuy.core.meta.member;

import java.io.Serializable;

import com.jiuyuan.entity.BrandBusiness;


/**
* @author WuWanjian
* @version 创建时间: 2016年10月27日 下午4:36:37
*/
public class BrandBusinessSearch extends BrandBusiness implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6693649974876224396L;

	private int storeNumberMin;
	
	private int storeNumberMax;
	
	private long createTimeMin;
	
	private long createTimeMax;
	
	private double cashIncomeMin;
	
	private double cashIncomeMax;
	
	private double availableBalanceMin;
	
	private double availableBalanceMax;

	public int getStoreNumberMin() {
		return storeNumberMin;
	}

	public void setStoreNumberMin(int storeNumberMin) {
		this.storeNumberMin = storeNumberMin;
	}

	public int getStoreNumberMax() {
		return storeNumberMax;
	}

	public void setStoreNumberMax(int storeNumberMax) {
		this.storeNumberMax = storeNumberMax;
	}

	public long getCreateTimeMin() {
		return createTimeMin;
	}

	public void setCreateTimeMin(long createTimeMin) {
		this.createTimeMin = createTimeMin;
	}

	public long getCreateTimeMax() {
		return createTimeMax;
	}

	public void setCreateTimeMax(long createTimeMax) {
		this.createTimeMax = createTimeMax;
	}

	public double getCashIncomeMin() {
		return cashIncomeMin;
	}

	public void setCashIncomeMin(double cashIncomeMin) {
		this.cashIncomeMin = cashIncomeMin;
	}

	public double getCashIncomeMax() {
		return cashIncomeMax;
	}

	public void setCashIncomeMax(double cashIncomeMax) {
		this.cashIncomeMax = cashIncomeMax;
	}

	public double getAvailableBalanceMin() {
		return availableBalanceMin;
	}

	public void setAvailableBalanceMin(double availableBalanceMin) {
		this.availableBalanceMin = availableBalanceMin;
	}

	public double getAvailableBalanceMax() {
		return availableBalanceMax;
	}

	public void setAvailableBalanceMax(double availableBalanceMax) {
		this.availableBalanceMax = availableBalanceMax;
	}
	
	
}
