package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.AddressResult;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.logistics.address.add response.
 *
 * @author auto
 * @since 2.0
 */
public class LogisticsAddressAddResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 只返回是否成功 is_success
	 */
	@ApiField("address")
	private AddressResult address;

	public void setAddress(AddressResult address) {
		this.address = address;
	}
	public AddressResult getAddress( ) {
		return this.address;
	}

}
