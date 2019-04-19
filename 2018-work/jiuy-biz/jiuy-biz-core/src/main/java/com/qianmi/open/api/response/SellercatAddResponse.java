package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.SellerCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.sellercat.add response.
 *
 * @author auto
 * @since 2.0
 */
public class SellercatAddResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 新增成功的类目对象
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
