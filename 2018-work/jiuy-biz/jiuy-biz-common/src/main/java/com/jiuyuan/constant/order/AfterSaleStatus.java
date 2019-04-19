package com.jiuyuan.constant.order;

public enum AfterSaleStatus {
	
	/**
	 * 待审核
	 */
	TO_BE_CHECKED(0),
	
	/**
	 * 已驳回
	 */
	REJECTED(1),
	
	/**
	 * 待买家发货
	 */
	TO_DELIVERY(2),
	
	/**
	 * 待确认
	 */
	TO_CONFIRM(3),
	
	/**
	 * 待付款
	 */
	TO_PAY(4),
	
	/**
	 * 待退款(换货处理中)
	 */
	TO_REFUND_OR_EXCHANGE(5),
	
	/**
	 * 退款成功(已发货)
	 */
    RETURNED_OR_DELIVERED(6),

    /**
     * 换货买家确认收货（换货成功）
     */
    CONFIRM_RECEIVE(7);
	
	
	private AfterSaleStatus(int intValue) {
		this.intValue = intValue;
	}

	private int intValue;

	public int getIntValue() {
		return intValue;
	}

}
