package org.dream.utils.constants;

public class FinanceSettleType extends FinanceType {

	public FinanceSettleType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	public static final FinanceSettleType MARGIN_RETURN = new FinanceSettleType(22, 2201, "返还保证金");

	public static final FinanceSettleType PORFIT_INCREASE = new FinanceSettleType(22, 2202, "收益增加");

	public static final FinanceSettleType PORFIT_DECREASE = new FinanceSettleType(22, -2202, "收益减少");

}
