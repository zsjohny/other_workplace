package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.AddressResult;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.logistics.address.search response.
 *
 * @author auto
 * @since 2.0
 */
public class LogisticsAddressSearchResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 地址库信息列表
	 */
	@ApiListField("addresses")
	@ApiField("address_result")
	private List<AddressResult> addresses;

	public void setAddresses(List<AddressResult> addresses) {
		this.addresses = addresses;
	}
	public List<AddressResult> getAddresses( ) {
		return this.addresses;
	}

}
