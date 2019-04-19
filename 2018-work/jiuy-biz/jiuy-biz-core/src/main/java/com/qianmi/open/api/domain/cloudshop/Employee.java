package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 员工信息
 *
 * @author auto
 * @since 2.0
 */
public class Employee extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 */
	@ApiField("created")
	private String created;

	/**
	 * 员工编号
	 */
	@ApiField("emp_id")
	private String empId;

	/**
	 * 员工昵称
	 */
	@ApiField("emp_nick")
	private String empNick;

	/**
	 * 是否销售员
	 */
	@ApiField("is_sale_user")
	private String isSaleUser;

	/**
	 * 绑定会员个数
	 */
	@ApiField("member_num")
	private Integer memberNum;

	/**
	 * 绑定会员列表
	 */
	@ApiListField("members")
	@ApiField("member")
	private List<Member> members;

	/**
	 * 手机号
	 */
	@ApiField("mobile")
	private String mobile;

	/**
	 * 员工姓名
	 */
	@ApiField("name")
	private String name;

	/**
	 * 所属部门编号
	 */
	@ApiField("org_id")
	private String orgId;

	/**
	 * 所属部门名称
	 */
	@ApiField("org_name")
	private String orgName;

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

	/**
	 * 绑定分销商列表
	 */
	@ApiListField("sales")
	@ApiField("member")
	private List<Member> sales;

	/**
	 * 绑定分销商个数
	 */
	@ApiField("sales_num")
	private Integer salesNum;

	/**
	 * 锁定状态
	 */
	@ApiField("status")
	private String status;

	public String getCreated() {
		return this.created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

	public String getEmpId() {
		return this.empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpNick() {
		return this.empNick;
	}
	public void setEmpNick(String empNick) {
		this.empNick = empNick;
	}

	public String getIsSaleUser() {
		return this.isSaleUser;
	}
	public void setIsSaleUser(String isSaleUser) {
		this.isSaleUser = isSaleUser;
	}

	public Integer getMemberNum() {
		return this.memberNum;
	}
	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public List<Member> getMembers() {
		return this.members;
	}
	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public String getMobile() {
		return this.mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
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

	public List<Member> getSales() {
		return this.sales;
	}
	public void setSales(List<Member> sales) {
		this.sales = sales;
	}

	public Integer getSalesNum() {
		return this.salesNum;
	}
	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}

	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}