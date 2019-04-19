package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 物流公司
 *
 * @author auto
 * @since 2.0
 */
public class LogisticsCompany extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 物流公司编码
	 */
	@ApiField("code")
	private String code;

	/**
	 * 卖家物流公司编号
	 */
	@ApiField("id")
	private String id;

	/**
	 * 是否成功 1:成功 0:失败
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 物流公司名称
	 */
	@ApiField("name")
	private String name;

	/**
	 * 卖家编号, A开头
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 * 物流公司官网地址
	 */
	@ApiField("url")
	private String url;

	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSellerNick() {
		return this.sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}