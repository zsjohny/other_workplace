package org.dream.utils.constants;

public class FinanceInnerType extends FinanceType {

	public FinanceInnerType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	public static final FinanceInnerType DEPOSIT_MONEY = new FinanceInnerType(31, 3101, "内部存入");

	public static final FinanceInnerType EXTRACT_MONEY = new FinanceInnerType(31, -3101, "内部取出");

	public static final FinanceInnerType DEPOSIT_SCORE = new FinanceInnerType(32, 3201, "内部存入");

	public static final FinanceInnerType EXTRACT_SCORE = new FinanceInnerType(32, -3202, "内部取出");
}
