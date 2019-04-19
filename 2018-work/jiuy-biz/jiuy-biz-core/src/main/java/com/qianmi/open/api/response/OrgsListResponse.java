package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Organization;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.orgs.list response.
 *
 * @author auto
 * @since 2.0
 */
public class OrgsListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 部门列表
	 */
	@ApiListField("organizations")
	@ApiField("organization")
	private List<Organization> organizations;

	/** 
	 * 总条数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}
	public List<Organization> getOrganizations( ) {
		return this.organizations;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
