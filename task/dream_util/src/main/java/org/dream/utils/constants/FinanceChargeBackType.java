package org.dream.utils.constants;

public class FinanceChargeBackType extends FinanceType {

	public FinanceChargeBackType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	public static final FinanceChargeBackType COMMISSION_RETURN = new FinanceChargeBackType(23, 2301, "返还手续费");

	public static final FinanceChargeBackType MARGIN_RETURN = new FinanceChargeBackType(23, 2302, "返还保证金");
}
