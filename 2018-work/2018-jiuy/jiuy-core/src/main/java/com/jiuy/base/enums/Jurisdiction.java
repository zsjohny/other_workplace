package com.jiuy.base.enums;

public enum Jurisdiction {

	/** 题库审核权限 */
	QUESTION_EXAMINE("QUESTION_EXAMINE", "题库审核权限"),
	/** 课件审核权限 **/
	COURSE_EXAMINE("COURSE_EXAMINE", "课件审核权限"),
	/** 模板审核权限 **/
	TEMPLET_EXAMINE("TEMPLET_EXAMINE", "模板审核权限");

	private String code;

	private String name;

	private String url;

	private Jurisdiction(String code, String name) {
		this.code = code;
		this.name = name;
	}

	private Jurisdiction(String code, String name, String url) {
		this.code = code;
		this.name = name;
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
