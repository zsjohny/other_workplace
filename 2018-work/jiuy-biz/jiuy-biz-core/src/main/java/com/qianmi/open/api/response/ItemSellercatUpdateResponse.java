package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemSellerCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.sellercat.update response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemSellercatUpdateResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 商品挂靠站点信息
	 */
	@ApiField("item_seller_cat")
	private ItemSellerCat itemSellerCat;

	public void setItemSellerCat(ItemSellerCat itemSellerCat) {
		this.itemSellerCat = itemSellerCat;
	}
	public ItemSellerCat getItemSellerCat( ) {
		return this.itemSellerCat;
	}

}
