package com.jiuyuan.entity.newentity;

import com.store.enumerate.MessageSendTypeeEnum;

/**
 * 商品状态枚举
 * 商品状态： 
 * 0（编辑中，未完成编辑）、 
 * 1（新建，编辑完成、待提审）、
 * 2（待审核，审核中）、
 * 3（审核不通过）、
 * 4（待上架，审核通过、待上架）、
 * 5（上架，审核通过、已上架）、 
 * 6（下架，审核通过、已下架）
 * 
 * 
 *
 */
public enum ProductNewStateEnum {
	unknown(-1,"未知"),
	edit(0, "编辑中"),
	new_finish(1, "新建"),
	wait_submit_audit(2, "待提审"),
	audit_ing(3, "审核中"),
	audit_no_pass(4, "审核不通过"),
	wait_up_sold(5, "待上架"),
	up_sold(6, "已上架"),
	down_sold(7, "已下架");
	
	private int intValue;
	
	private String desc;
	
	public static ProductNewStateEnum getEnum(int intValue) {
		if (intValue == 0){
			return edit;
		}else if (intValue == 1){
			return new_finish;
		}else if (intValue == 2){
			return wait_submit_audit;
		}else if (intValue == 3){
			return audit_ing;
		}else if (intValue == 4){
			return audit_no_pass ;
		}else if (intValue == 5){
			return wait_up_sold;
		}else if (intValue == 6){
			return up_sold;
		}else if (intValue == 7){
			return down_sold;
		}else{
			return unknown;
		}
	}
	
	ProductNewStateEnum(int intValue, String desc) {
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
