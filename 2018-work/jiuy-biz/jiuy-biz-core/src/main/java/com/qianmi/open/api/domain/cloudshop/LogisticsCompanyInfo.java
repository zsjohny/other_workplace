package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 标准物流公司
 *
 * @author auto
 * @since 2.0
 */
public class LogisticsCompanyInfo extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 内部映射码
	 */
	@ApiField("code")
	private String code;

	/**
	 * 快递公司名称
	 */
	@ApiField("name")
	private String name;

	/**
	 * 删除标识:false表示未删除,true表示已经删除
	 */
	@ApiField("state")
	private String state;

	/**
	 * 快递公司官方网站
	 */
	@ApiField("url")
	private String url;

	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return this.state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}