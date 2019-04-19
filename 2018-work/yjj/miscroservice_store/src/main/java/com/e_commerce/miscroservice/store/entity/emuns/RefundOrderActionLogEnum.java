package com.e_commerce.miscroservice.store.entity.emuns;

/**
 * 售后操作日志枚举
 * 
 * 商品状态： 
 * 1:买家已发起售后，等待卖家确认
 * 2:卖家拒绝了买家的售后申请
 * 3:卖家同意了买家的售后申请，等待买家发货
 * 4:买家未按时发货，导致售后单超时关闭失效
 * 5:买家已发货，等待卖家确认收货
 * 6:卖家已确认收货，卖家已确认退款
 * 7:买家在卖家未确认之前撤消售后单
 * 8:仅对于退货售后单，买家在卖家同意后，未发货前，主动撤消售后单
 * 9:
 * 10:售后单被卖家拒绝后，买家申请平台介入
 * 11:平台输入后被平台增加一次申请售后机会
 * 12:结束平台介入
 * 13:卖家在待确认收货前，申请平台介入
 * 14:
 * 15:平台关闭售后单    平台关闭售后单：这里写关闭理由
 *
 * 
 * 
 */
public enum RefundOrderActionLogEnum {
	unknown(-1,"未知"),
	A(1, "已提交申请，待卖家确认"),
	B(2, "卖家拒绝"),
	C(3, "卖家同意"),
	D(4, "买家超时未发货，售后单关闭"),
	E(5, "买家已发货"),
	F(6, "卖家确认退款"),
	G(7, "买家撤消"),
	H(8, "买家撤消"),
	I(10, "买家申请平台介入"),
	J(11, "平台增加1次售后申请机会"),
	K(12, "平台介入结束"),
	L(13, "卖家申请平台介入"),
	M(15, "平台关闭售后单");
	
	private int intValue;
	
	private String desc;
	
	public static RefundOrderActionLogEnum getEnum(int intValue) {
		if (intValue == 1){
			return A;
		}else if (intValue == 2){
			return B;
		}else if (intValue == 3){
			return C;
		}else if (intValue == 4){
			return D;
		}else if (intValue == 5){
			return E ;
		}else if (intValue == 6){
			return F;
		}else if (intValue == 7){
			return G;
		}else if (intValue == 8){
			return H;
		}else if (intValue == 10){
			return I;
		}else if (intValue == 11){
			return J;
		}else if (intValue == 12){
			return K;
		}else if (intValue == 13){
			return L;
		}else if (intValue == 15){
			return M;
		}else{
			return unknown;
		}
	}
	
	RefundOrderActionLogEnum(int intValue, String desc) {
		this.intValue = intValue;
		this.desc = desc;
	}

	public int getIntValue() {
		return intValue;
	}

	public String getDesc() {
		return desc;
	}
}
