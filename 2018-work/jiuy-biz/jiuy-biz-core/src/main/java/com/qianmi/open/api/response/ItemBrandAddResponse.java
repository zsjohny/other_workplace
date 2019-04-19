package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemBrand;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.brand.add response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemBrandAddResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 具体的字段根据请求中设定的fileds决定
	 */
	@ApiField("item_brand")
	private ItemBrand itemBrand;

	public void setItemBrand(ItemBrand itemBrand) {
		this.itemBrand = itemBrand;
	}
	public ItemBrand getItemBrand( ) {
		return this.itemBrand;
	}

}
