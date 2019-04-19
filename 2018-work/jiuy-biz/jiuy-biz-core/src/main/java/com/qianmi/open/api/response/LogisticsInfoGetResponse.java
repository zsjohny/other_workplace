package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.LogisticsInfo;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.logistics.info.get response.
 *
 * @author auto
 * @since 2.0
 */
public class LogisticsInfoGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 物流详情
	 */
	@ApiListField("logistics_infos")
	@ApiField("logistics_info")
	private List<LogisticsInfo> logisticsInfos;

	public void setLogisticsInfos(List<LogisticsInfo> logisticsInfos) {
		this.logisticsInfos = logisticsInfos;
	}
	public List<LogisticsInfo> getLogisticsInfos( ) {
		return this.logisticsInfos;
	}

}
