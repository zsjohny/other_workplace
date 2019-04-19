package com.jiuy.base.enums;

/**
 * 模块枚举类
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/6/5 16:16
 */
public enum ModelType {


	/** 商品模块 **/
	PRODUCT_MODEL("商品模块", "PRODUCT", null),
	/** 订单模块 **/
	ORDER_MODEL("订单模块", "ORDER", null),
	/** 日志模块 **/
	LOGS_MODEL("日志模块", "LOGS", null),
	/** 统计模块 **/
	MONITOR("统计模块", "MONITOR", null);
	
	
	private String name;
	private String code;
	private ModelType parentModel;

	ModelType(String name, String code, ModelType parentModel) {
		this.name = name;
		this.code = code;
		this.parentModel = parentModel;
	}

	public ModelType getParentModel() {
		return parentModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
