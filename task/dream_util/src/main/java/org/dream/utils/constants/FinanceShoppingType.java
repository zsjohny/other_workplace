package org.dream.utils.constants;

public class FinanceShoppingType extends FinanceType {

	public FinanceShoppingType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	public static final FinanceShoppingType PURCHASE_SCORE = new FinanceShoppingType(71, -7101, "兑换金币");

	public static final FinanceShoppingType EXCHANGE_SCORE = new FinanceShoppingType(72, 7201, "资金兑换");

	public static final FinanceShoppingType PURCHASE_GOODS = new FinanceShoppingType(72, -7201, "兑换商品");

	public static final FinanceShoppingType PURCHASE_REFUSE = new FinanceShoppingType(72, 7202, "拒绝兑换");
	
	public static final FinanceShoppingType RECOVER_SCORE = new FinanceShoppingType(72, 7203, "获取金币");
}
