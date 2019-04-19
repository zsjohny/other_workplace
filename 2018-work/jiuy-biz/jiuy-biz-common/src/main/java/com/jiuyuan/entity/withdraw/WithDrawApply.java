package com.jiuyuan.entity.withdraw;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class WithDrawApply extends BaseMeta<Long> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8497114727380290594L;
	
	private long id;//
	
	private long relatedId;
	
	private long tradeId;
	private String tradeNo;
	
	private int status;
	
	private int type;//商家类型 0 门店 1 品牌货款 2 品牌物流 3 品牌
	
	private double money;

	private int tradeWay;
	private String TradeName;
	private String TradeAccount;
	private String TradeBankName;
	
	private double applyMoney;

	private long dealTime;
	
	private String remark;
	
	private long createTime;
	
	private long updateTime;
	
	private int feedBack;//反馈
	
	public WithDrawApply() {
		
	}

	public WithDrawApply(long id, double money,int type, String tradeNo, int tradeWay, String remark, int status,
			long dealTime) {
		this.id = id;
		this.money = money;
		this.type =type;
		this.tradeNo = tradeNo;
		this.tradeWay = tradeWay;
		this.remark = remark;
		this.status = status;
		this.dealTime = dealTime;

	}

	public WithDrawApply(long id, int status, String remark, int feedBack, long dealTime) {
		this.id = id;
		this.status = status;
		this.remark = remark;
		this.feedBack = feedBack;
		this.dealTime = dealTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
	}

	public long getTradeId() {
		return tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getTradeWay() {
		return tradeWay;
	}

	public void setTradeWay(int tradeWay) {
		this.tradeWay = tradeWay;
	}
	
	
	public String getTradeName() {
		return TradeName;
	}

	public void setTradeName(String tradeName) {
		TradeName = tradeName;
	}

	public String getTradeAccount() {
		return TradeAccount;
	}

	public void setTradeAccount(String tradeAccount) {
		TradeAccount = tradeAccount;
	}

	public String getTradeBankName() {
		return TradeBankName;
	}

	public void setTradeBankName(String tradeBankName) {
		TradeBankName = tradeBankName;
	}

	public double getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(double applyMoney) {
		this.applyMoney = applyMoney;
	}

	public long getDealTime() {
		return dealTime;
	}

	public void setDealTime(long dealTime) {
		this.dealTime = dealTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Override
	public Long getCacheId() {
		
		return null;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public int getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(int feedBack) {
		this.feedBack = feedBack;
	}

	@Override
	public String toString() {
		return "WithDrawApply [id=" + id + ", relatedId=" + relatedId + ", tradeId=" + tradeId + ", tradeNo=" + tradeNo
				+ ", status=" + status + ", type=" + type + ", money=" + money + ", tradeWay=" + tradeWay
				+ ", TradeName=" + TradeName + ", TradeAccount=" + TradeAccount + ", TradeBankName=" + TradeBankName
				+ ", applyMoney=" + applyMoney + ", dealTime=" + dealTime + ", remark=" + remark + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

	
	
}
