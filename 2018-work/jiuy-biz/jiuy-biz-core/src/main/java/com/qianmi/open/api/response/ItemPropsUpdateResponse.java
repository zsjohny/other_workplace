package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemProp;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.props.update response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemPropsUpdateResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 商品规格列表
	 */
	@ApiField("item_prop")
	private ItemProp itemProp;

	public void setItemProp(ItemProp itemProp) {
		this.itemProp = itemProp;
	}
	public ItemProp getItemProp( ) {
		return this.itemProp;
	}

}
