package com.jiuyuan.entity.storeorder;


/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午4:52:33
*/
public class StoreOrderVO extends StoreOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5793672660601862367L;

	private int buyCount;

	
	private String expressSupplier;

	private String expressOrderNo;
	
	private String message;//最近的一条留言
	
	private String adminName;//最近的一条留言操作人
	
	private String messageCreateTime;//最近的一条留言操作时间

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getMessageCreateTime() {
		return messageCreateTime;
	}

	public void setMessageCreateTime(String messageCreateTime) {
		this.messageCreateTime = messageCreateTime;
	}
}