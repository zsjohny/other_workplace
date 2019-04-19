package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.SellerCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.sellercat.delete response.
 *
 * @author auto
 * @since 2.0
 */
public class SellercatDeleteResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回是否成功is_success
	 */
	@ApiField("sellerCat")
	private SellerCat sellerCat;

	public void setSellerCat(SellerCat sellerCat) {
		this.sellerCat = sellerCat;
	}
	public SellerCat getSellerCat( ) {
		return this.sellerCat;
	}

}
