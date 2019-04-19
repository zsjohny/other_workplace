package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.LogisticsCompany;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.logistics.company.remove response.
 *
 * @author auto
 * @since 2.0
 */
public class LogisticsCompanyRemoveResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 只返回是否成功 is_success 1-成功 0-失败
	 */
	@ApiField("logistics_company")
	private LogisticsCompany logisticsCompany;

	public void setLogisticsCompany(LogisticsCompany logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}
	public LogisticsCompany getLogisticsCompany( ) {
		return this.logisticsCompany;
	}

}
