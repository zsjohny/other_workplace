package org.dream.utils.constants;

public class FinanceCommissionType extends FinanceType {

	public FinanceCommissionType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	public static final FinanceCommissionType COMMISSION_IN = new FinanceCommissionType(5, 5001, "佣金存入");

	public static final FinanceCommissionType COMMISSION_OUT = new FinanceCommissionType(-5, -5001, "佣金转出");

	// public static final FinanceCommissionType COMMISSION_CARRYOVER = new
	// FinanceCommissionType(5, 5002, "佣金转余额");

	public static final FinanceCommissionType COMMISSION_CARRYOVER = new FinanceCommissionType(6, 6001, "佣金转余额");
}
