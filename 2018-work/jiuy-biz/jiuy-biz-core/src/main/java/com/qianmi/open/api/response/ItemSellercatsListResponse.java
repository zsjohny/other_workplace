package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.ItemSellerCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.sellercats.list response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemSellercatsListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 已挂靠的销售渠道中的展示类目列表
	 */
	@ApiListField("item_seller_cats")
	@ApiField("item_seller_cat")
	private List<ItemSellerCat> itemSellerCats;

	public void setItemSellerCats(List<ItemSellerCat> itemSellerCats) {
		this.itemSellerCats = itemSellerCats;
	}
	public List<ItemSellerCat> getItemSellerCats( ) {
		return this.itemSellerCats;
	}

}
