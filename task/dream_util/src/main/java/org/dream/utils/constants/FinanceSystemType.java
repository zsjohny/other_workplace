package org.dream.utils.constants;

public class FinanceSystemType extends FinanceType {

	public FinanceSystemType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	// public static final FinanceInnerType DEPOSIT_MONEY = new
	// FinanceInnerType(41, 4101, "内部存入资金");
	//
	// public static final FinanceInnerType EXTRACT_MONEY = new
	// FinanceInnerType(41, -4101, "内部取出资金");
	public static final FinanceSystemType MAKE_MONEY = new FinanceSystemType(41, 4101, "系统补单");
	
	public static final FinanceSystemType REGISTER_DEPOSIT_SCORE = new FinanceSystemType(42, 4201, "注册赠送");

	// public static final FinanceInnerType EXTRACT_SCORE = new
	// FinanceInnerType(42, -4202, "内部取出积分");

	// public static final FinanceSystemType GIVE_REDBAG = new
	// FinanceSystemType(3, 3001, "充值红包");
	//
	// public static final FinanceSystemType ERASE_REDBAG = new
	// FinanceSystemType(-3, -3001, "抹除红包");
	//
	// public static final FinanceSystemType GIVE_SOCRE = new
	// FinanceSystemType(3, 3002, "充值积分");
	//
	// public static final FinanceSystemType ERASE_SOCRE = new
	// FinanceSystemType(-3, -3002, "抹除积分");
	//
	// public static final FinanceSystemType GIVE_MONEY = new
	// FinanceSystemType(3, 3003, "充值资金");
	//
	// public static final FinanceSystemType ERASE_MONEY = new
	// FinanceSystemType(-3, -3003, "抹除资金");
	//
	// public static final FinanceSystemType MAKE_MONEY = new
	// FinanceSystemType(3, 3004, "系统补单");
	//
	// public static final FinanceSystemType INNER_DEPOSIT_MONEY = new
	// FinanceSystemType(3, 3005, "内部存入资金");
	//
	// public static final FinanceSystemType INNER_DEPOSIT_SCORE = new
	// FinanceSystemType(3, 3006, "内部存入积分");
	//
	// public static final FinanceSystemType INNER_EXTRACT_MONEY = new
	// FinanceSystemType(-3, -3005, "内部取出资金");
	//
	// public static final FinanceSystemType INNER_EXTRACT_SCORE = new
	// FinanceSystemType(-3, -3006, "内部取出积分");

	// public static final FinanceSystemType GIVE_COUPON = new
	// FinanceSystemType(3, 3003, "赠送优惠券");
	//
	// public static final FinanceSystemType ERASE_COUPON = new
	// FinanceSystemType(-3, -3003, "抹除优惠券");
}
