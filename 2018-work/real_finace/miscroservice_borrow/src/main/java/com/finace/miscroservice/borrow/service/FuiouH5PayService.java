package com.finace.miscroservice.borrow.service;

import java.math.BigDecimal;

public interface FuiouH5PayService {
	public class Param {

		private String userId;
		private BigDecimal amt;
		private String name;
		private String pid;
		private String bankCard;
		private String orderId;

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public String getOrderId() {
			return orderId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public BigDecimal getAmt() {
			return amt;
		}

		public void setAmt(BigDecimal amt) {
			this.amt = amt;
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

	}

	public class PayCallBackResult {
		private String version;
		private String type;
		private String responseCode;
		private String responseMsg;
		private String mchntCd;
		private String mchntOrderId;
		private String orderId;
		private String bankCard;
		private String amt;
		private String sign;

		@Override
		public String toString() {
			return "PayCallBackResult [version=" + version + ", type=" + type + ", responseCode=" + responseCode + ", responseMsg=" + responseMsg + ", mchntCd=" + mchntCd + ", mchntOrderId=" + mchntOrderId + ", orderId=" + orderId + ", bankCard=" + bankCard + ", amt=" + amt + ", sign=" + sign + "]";
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(String responseCode) {
			this.responseCode = responseCode;
		}

		public String getResponseMsg() {
			return responseMsg;
		}

		public void setResponseMsg(String responseMsg) {
			this.responseMsg = responseMsg;
		}

		public String getMchntCd() {
			return mchntCd;
		}

		public void setMchntCd(String mchntCd) {
			this.mchntCd = mchntCd;
		}

		public String getMchntOrderId() {
			return mchntOrderId;
		}

		public void setMchntOrderId(String mchntOrderId) {
			this.mchntOrderId = mchntOrderId;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public String getBankCard() {
			return bankCard;
		}

		public void setBankCard(String bankCard) {
			this.bankCard = bankCard;
		}

		public String getAmt() {
			return amt;
		}

		public void setAmt(String amt) {
			this.amt = amt;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

	}

	public String makePayForm(Param param);

	public boolean isFromFuiouServer(PayCallBackResult result);

	public boolean isPaySuccess(PayCallBackResult result);
}
