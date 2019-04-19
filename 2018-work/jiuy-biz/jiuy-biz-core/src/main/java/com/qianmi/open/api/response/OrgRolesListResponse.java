package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Role;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.org.roles.list response.
 *
 * @author auto
 * @since 2.0
 */
public class OrgRolesListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 岗位信息列表
	 */
	@ApiListField("roles")
	@ApiField("role")
	private List<Role> roles;

	/** 
	 * 总条数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public List<Role> getRoles( ) {
		return this.roles;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
