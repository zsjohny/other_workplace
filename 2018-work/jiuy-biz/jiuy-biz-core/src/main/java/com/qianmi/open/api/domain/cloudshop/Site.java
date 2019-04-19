package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 店铺信息
 *
 * @author auto
 * @since 2.0
 */
public class Site extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 店铺主域名
	 */
	@ApiField("domain_name")
	private String domainName;

	/**
	 * 店铺favicon地址
	 */
	@ApiField("favicon")
	private String favicon;

	/**
	 * 店铺LOGO图片地址
	 */
	@ApiField("logo")
	private String logo;

	/**
	 * 系统类型
	 */
	@ApiField("site_flag")
	private String siteFlag;

	/**
	 * 店铺标题
	 */
	@ApiField("title")
	private String title;

	public String getDomainName() {
		return this.domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getFavicon() {
		return this.favicon;
	}
	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}

	public String getLogo() {
		return this.logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getSiteFlag() {
		return this.siteFlag;
	}
	public void setSiteFlag(String siteFlag) {
		this.siteFlag = siteFlag;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}