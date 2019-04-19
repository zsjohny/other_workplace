package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 销售区域信息
 *
 * @author auto
 * @since 2.0
 */
public class SaleArea extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * admin编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 销售区域编号
	 */
	@ApiField("area_id")
	private String areaId;

	/**
	 * 销售区域名称
	 */
	@ApiField("area_name")
	private String areaName;

	/**
	 * 部门编号
	 */
	@ApiField("org_id")
	private String orgId;

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAreaId() {
		return this.areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return this.areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getOrgId() {
		return this.orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}