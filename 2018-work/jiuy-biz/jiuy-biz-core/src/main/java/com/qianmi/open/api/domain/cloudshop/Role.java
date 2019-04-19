package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 岗位信息
 *
 * @author auto
 * @since 2.0
 */
public class Role extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 岗位所属admin编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 创建时间
	 */
	@ApiField("created")
	private String created;

	/**
	 * 岗位下的员工数量
	 */
	@ApiField("emp_num")
	private Integer empNum;

	/**
	 * 备注
	 */
	@ApiField("remark")
	private String remark;

	/**
	 * 岗位编号
	 */
	@ApiField("role_id")
	private String roleId;

	/**
	 * 岗位名称
	 */
	@ApiField("role_name")
	private String roleName;

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getCreated() {
		return this.created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

	public Integer getEmpNum() {
		return this.empNum;
	}
	public void setEmpNum(Integer empNum) {
		this.empNum = empNum;
	}

	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRoleId() {
		return this.roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}