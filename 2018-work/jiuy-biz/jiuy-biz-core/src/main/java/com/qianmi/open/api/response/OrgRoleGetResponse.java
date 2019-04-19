package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Role;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.org.role.get response.
 *
 * @author auto
 * @since 2.0
 */
public class OrgRoleGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 岗位信息
	 */
	@ApiField("role")
	private Role role;

	public void setRole(Role role) {
		this.role = role;
	}
	public Role getRole( ) {
		return this.role;
	}

}
