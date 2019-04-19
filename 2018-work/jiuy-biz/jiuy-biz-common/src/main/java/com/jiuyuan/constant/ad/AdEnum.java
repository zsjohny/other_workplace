package com.jiuyuan.constant.ad;


public enum AdEnum {

	PARTENER(0, "品牌主页录播图"), 
	MAIN_BANNER(1, "首页轮播图"), 
	BRANDPAGEAD(2, "品牌馆首页轮播图");

	AdEnum(int type, String name) {
		this.type = type;
		this.name = name;
	}

	private final int type;

	private final String name;

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
