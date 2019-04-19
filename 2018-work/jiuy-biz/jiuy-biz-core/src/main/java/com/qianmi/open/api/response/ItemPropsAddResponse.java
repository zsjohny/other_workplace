package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemProp;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.props.add response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemPropsAddResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回为新增的规格项
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
