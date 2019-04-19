package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemBrand;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.brand.delete response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemBrandDeleteResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回是否成功is_success
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
