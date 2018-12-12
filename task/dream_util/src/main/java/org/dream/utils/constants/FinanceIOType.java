package org.dream.utils.constants;

public class FinanceIOType extends FinanceType {

	public FinanceIOType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	public static final FinanceIOType UNIONPAY_IN = new FinanceIOType(1, 1001, "银联充值");

	public static final FinanceIOType ALIPAY_IN = new FinanceIOType(1, 1002, "支付宝充值");

	public static final FinanceIOType WECHAT_IN = new FinanceIOType(1, 1003, "微信充值");

	public static final FinanceIOType BAOFOO_IN = new FinanceIOType(1, 1004, "宝付充值");

	public static final FinanceIOType IPAYNOW_IN = new FinanceIOType(1, 1005, "现在充值");
	
	public static final FinanceIOType QIANTONG_IN = new FinanceIOType(1, 1006, "钱通充值");

	public static final FinanceIOType WITHDRAW = new FinanceIOType(-1, -1201, "用户提现");

	public static final FinanceIOType DRAW_REFUSE = new FinanceIOType(-1, 1201, "拒绝提现");
	
	public static final FinanceIOType JINGDONG_IN = new FinanceIOType(1, 1007, "京东充值");

	// public static final FinanceIOType DRAW_SUCCESS = new FinanceIOType(-1,
	// -1003, "提现成功");
}
