package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 部门信息
 *
 * @author auto
 * @since 2.0
 */
public class Organization extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * admin编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 部门层级
	 */
	@ApiField("depth")
	private Integer depth;

	/**
	 * 关联员工数量
	 */
	@ApiField("emp_num")
	private Integer empNum;

	/**
	 * 部门编号
	 */
	@ApiField("org_id")
	private String orgId;

	/**
	 * 部门名称
	 */
	@ApiField("org_name")
	private String orgName;

	/**
	 * 父部门编号
	 */
	@ApiField("p_org_id")
	private String pOrgId;

	/**
	 * 关联销售区域
	 */
	@ApiListField("sale_areas")
	@ApiField("sale_area")
	private List<SaleArea> saleAreas;

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public Integer getDepth() {
		return this.depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getEmpNum() {
		return this.empNum;
	}
	public void setEmpNum(Integer empNum) {
		this.empNum = empNum;
	}

	public String getOrgId() {
		return this.orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return this.orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getpOrgId() {
		return this.pOrgId;
	}
	public void setpOrgId(String pOrgId) {
		this.pOrgId = pOrgId;
	}

	public List<SaleArea> getSaleAreas() {
		return this.saleAreas;
	}
	public void setSaleAreas(List<SaleArea> saleAreas) {
		this.saleAreas = saleAreas;
	}

}