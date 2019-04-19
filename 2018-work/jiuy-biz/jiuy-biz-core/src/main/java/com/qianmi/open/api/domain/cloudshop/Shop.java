package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 商家信息
 *
 * @author auto
 * @since 2.0
 */
public class Shop extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 商家A编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 商家登录账号
	 */
	@ApiField("nick_name")
	private String nickName;

	/**
	 * null
	 */
	@ApiListField("site")
	@ApiField("site")
	private List<Site> site;

	/**
	 * 已开通销售渠道数量(仅计算云订货 云商城)
	 */
	@ApiField("site_num")
	private Integer siteNum;

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getNickName() {
		return this.nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public List<Site> getSite() {
		return this.site;
	}
	public void setSite(List<Site> site) {
		this.site = site;
	}

	public Integer getSiteNum() {
		return this.siteNum;
	}
	public void setSiteNum(Integer siteNum) {
		this.siteNum = siteNum;
	}

}