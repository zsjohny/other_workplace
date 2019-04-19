package com.jiuyuan.entity.order;

import java.io.Serializable;

public class RestrictProductVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -826277777168639172L;
	
    private long productId;
    
    private int restrictCycle;
    
    private int restrictDayBuy;

    private int restrictHistoryBuy;
    
    private long restrictDayBuyTime; 
    
    private long restrictHistoryBuyTime;
    
    private int buyCount;
    
    //今天已经买的总件数
    private int daySum;
    
    //当前限购周期里,过去已买的总件数
    private int historySum;
    
    //订单生成时间
    private long createTime;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getRestrictCycle() {
		return restrictCycle;
	}

	public void setRestrictCycle(int restrictCycle) {
		this.restrictCycle = restrictCycle;
	}

	public int getRestrictDayBuy() {
		return restrictDayBuy;
	}

	public void setRestrictDayBuy(int restrictDayBuy) {
		this.restrictDayBuy = restrictDayBuy;
	}

	public int getRestrictHistoryBuy() {
		return restrictHistoryBuy;
	}

	public void setRestrictHistoryBuy(int restrictHistoryBuy) {
		this.restrictHistoryBuy = restrictHistoryBuy;
	}

	public long getRestrictDayBuyTime() {
		return restrictDayBuyTime;
	}

	public void setRestrictDayBuyTime(long restrictDayBuyTime) {
		this.restrictDayBuyTime = restrictDayBuyTime;
	}

	public long getRestrictHistoryBuyTime() {
		return restrictHistoryBuyTime;
	}

	public void setRestrictHistoryBuyTime(long restrictHistoryBuyTime) {
		this.restrictHistoryBuyTime = restrictHistoryBuyTime;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getDaySum() {
		return daySum;
	}

	public void setDaySum(int daySum) {
		this.daySum = daySum;
	}

	public int getHistorySum() {
		return historySum;
	}

	public void setHistorySum(int historySum) {
		this.historySum = historySum;
	}
    
}
