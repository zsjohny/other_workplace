package com.finace.miscroservice.borrow.service;

import com.finace.miscroservice.borrow.po.FinanceBidPO;

import java.math.BigDecimal;


public interface SaleService {
	public class Param {
		private int userId;
		private int borrowId;
		private BigDecimal buyAmt;
		private String name;
		private String pid;
		private String bankCard;
		private String regChannel;
		private String hbid;
		private String channel;
		private BigDecimal apr;

		public String getRegChannel() {
			return regChannel;
		}

		public void setRegChannel(String regChannel) {
			this.regChannel = regChannel;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}

		public String getBankCard() {
			return bankCard;
		}

		public void setBankCard(String bankCard) {
			this.bankCard = bankCard;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int getBorrowId() {
			return borrowId;
		}

		public void setBorrowId(int borrowId) {
			this.borrowId = borrowId;
		}

		public BigDecimal getBuyAmt() {
			return buyAmt;
		}

		public void setBuyAmt(BigDecimal buyAmt) {
			this.buyAmt = buyAmt;
		}

		public String getHbid() {
			return hbid;
		}

		public void setHbid(String hbid) {
			this.hbid = hbid;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}

		public BigDecimal getApr() {
			return apr;
		}

		public void setApr(BigDecimal apr) {
			this.apr = apr;
		}
	}

	public FinanceBidPO makeOrder(Param orderParam, String version);

	public FinanceBidPO makeAgreeOrder(Param orderParam, String version);


	public FinanceBidPO onFuiouPaySuccess(FuiouH5PayService.PayCallBackResult result);

	public FinanceBidPO onAgreeFuiouPaySuccess(FuiouH5PayService.PayCallBackResult result);








}
