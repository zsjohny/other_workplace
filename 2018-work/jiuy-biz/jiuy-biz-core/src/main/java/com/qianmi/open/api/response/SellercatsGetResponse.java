package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.SellerCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.sellercats.get response.
 *
 * @author auto
 * @since 2.0
 */
public class SellercatsGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 自定义类目列表
	 */
	@ApiListField("seller_cats")
	@ApiField("seller_cat")
	private List<SellerCat> sellerCats;

	public void setSellerCats(List<SellerCat> sellerCats) {
		this.sellerCats = sellerCats;
	}
	public List<SellerCat> getSellerCats( ) {
		return this.sellerCats;
	}

}
