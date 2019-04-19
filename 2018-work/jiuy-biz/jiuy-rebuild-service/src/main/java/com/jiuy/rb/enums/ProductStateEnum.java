package com.jiuy.rb.enums;


import java.util.HashMap;
import java.util.Map;

/**
 *  商品的状态
 *
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/6/13 10:23
 */
public enum ProductStateEnum {


	/**
	 * 未知
	 */
	UNKNOWN(-1,"未知"),
	/**
	 * 编辑中
	 */
	EDIT(0, "编辑中"),
	/**
	 * 新建
	 */
	NEW_FINISH(1, "新建"),
	/**
	 * 待提审
	 */
	WAIT_SUBMIT_AUDIT(2, "待提审"),
	/**
	 * 审核中
	 */
	AUDIT_ING(3, "审核中"),
	/**
	 * 审核不通过
	 */
	AUDIT_NO_PASS(4, "审核不通过"),
	/**
	 * 待上架
	 */
	WAIT_UP_SOLD(5, "待上架"),
	/**
	 * 已上架
	 */
	UP_SOLD(6, "已上架"),
	/**
	 * 已下架
	 */
	DOWN_SOLD(7, "已下架");
	
	private Integer code;
	
	private String name;

	ProductStateEnum(Integer code,String name){
		this.code = code;
		this.name = name;
	}


	/**
	 * 判断是否是当前的枚举
	 *
	 * @param code code
	 * @author Aison
	 * @date 2018/6/13 9:59
	 */
	public boolean isThis(Integer code) {
		return this.getCode().equals(code);
	}

	private static Map<String,ProductStateEnum> auditStateMap = new HashMap<>();

	static {
		for (ProductStateEnum productAuditState : ProductStateEnum.values()) {
			auditStateMap.put(productAuditState.getCode().toString(),productAuditState);
		}
	}

	public static ProductStateEnum getByCode(Integer code) {

		return auditStateMap.get(code == null ? "" : code.toString());
	}

	public static String getByCode(String code) {
		ProductStateEnum productAuditState =  auditStateMap.get(code);
		return productAuditState == null ? "" : productAuditState.getName();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
