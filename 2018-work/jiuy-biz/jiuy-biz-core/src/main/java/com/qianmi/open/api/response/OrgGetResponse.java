package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Organization;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.org.get response.
 *
 * @author auto
 * @since 2.0
 */
public class OrgGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 部门信息
	 */
	@ApiField("organization")
	private Organization organization;

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public Organization getOrganization( ) {
		return this.organization;
	}

}
