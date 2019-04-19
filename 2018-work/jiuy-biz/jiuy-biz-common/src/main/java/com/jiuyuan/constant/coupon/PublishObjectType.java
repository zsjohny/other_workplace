package com.jiuyuan.constant.coupon;

public enum PublishObjectType {
	GIVEN_STOREBUSINESS(0, "指定商家号"),

	ALL_STOREBUSINESS(1, "所有商家号"),

	GIVEN_PHONE_NUMBER(3, "指定注册手机号码");

	private int value;

	private String description;

	PublishObjectType(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	public static PublishObjectType getByValue(int value) {
		for (PublishObjectType PublishObjectType : PublishObjectType.values()) {
			if (PublishObjectType.getValue() == value) {
				return PublishObjectType;
			}
		}
		return null;
	}
}
