package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.LogisticsCompany;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.logistics.companies.get response.
 *
 * @author auto
 * @since 2.0
 */
public class LogisticsCompaniesGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 物流公司信息列表
	 */
	@ApiListField("logistics_companies")
	@ApiField("logistics_company")
	private List<LogisticsCompany> logisticsCompanies;

	public void setLogisticsCompanies(List<LogisticsCompany> logisticsCompanies) {
		this.logisticsCompanies = logisticsCompanies;
	}
	public List<LogisticsCompany> getLogisticsCompanies( ) {
		return this.logisticsCompanies;
	}

}
