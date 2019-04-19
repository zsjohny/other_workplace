package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.MemberAddress;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.member.logistics.address.list response.
 *
 * @author auto
 * @since 2.0
 */
public class MemberLogisticsAddressListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 会员地址列表
	 */
	@ApiListField("addresses")
	@ApiField("address")
	private List<MemberAddress> addresses;

	public void setAddresses(List<MemberAddress> addresses) {
		this.addresses = addresses;
	}
	public List<MemberAddress> getAddresses( ) {
		return this.addresses;
	}

}
