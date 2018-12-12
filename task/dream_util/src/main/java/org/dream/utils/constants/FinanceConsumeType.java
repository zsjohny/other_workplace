package org.dream.utils.constants;

public class FinanceConsumeType extends FinanceType {

	public FinanceConsumeType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	public static final FinanceConsumeType COMMISSION_PAY = new FinanceConsumeType(21, -2101, "支付手续费");

	public static final FinanceConsumeType MARGIN_FROZEN = new FinanceConsumeType(21, -2102, "冻结保证金");
}
