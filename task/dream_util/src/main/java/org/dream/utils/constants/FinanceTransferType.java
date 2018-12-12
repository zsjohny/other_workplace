package org.dream.utils.constants;

public class FinanceTransferType extends FinanceType {

	public FinanceTransferType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	public static final FinanceTransferType AILPAY = new FinanceTransferType(-4, -4001, "支付宝转账");

	public static final FinanceTransferType TIANFUBAO_WECHAT = new FinanceTransferType(-4, -4002, "天付宝微信转账");

	public static final FinanceTransferType MANUAL = new FinanceTransferType(-4, -4003, "人工转账");
	
	public static final FinanceTransferType FUYOU = new FinanceTransferType(-4 ,-4006,"富友转账");
}
