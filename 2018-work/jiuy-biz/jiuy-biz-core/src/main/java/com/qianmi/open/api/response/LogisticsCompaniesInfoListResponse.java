package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.LogisticsCompanyInfo;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.logistics.companies.info.list response.
 *
 * @author auto
 * @since 2.0
 */
public class LogisticsCompaniesInfoListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * null
	 */
	@ApiListField("logisticsCompanyInfos")
	@ApiField("logistics_company_info")
	private List<LogisticsCompanyInfo> logisticsCompanyInfos;

	public void setLogisticsCompanyInfos(List<LogisticsCompanyInfo> logisticsCompanyInfos) {
		this.logisticsCompanyInfos = logisticsCompanyInfos;
	}
	public List<LogisticsCompanyInfo> getLogisticsCompanyInfos( ) {
		return this.logisticsCompanyInfos;
	}

}
