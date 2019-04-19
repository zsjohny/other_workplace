package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.itemcat.delete response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemcatDeleteResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回是否成功is_success
	 */
	@ApiField("itemCat")
	private ItemCat itemCat;

	public void setItemCat(ItemCat itemCat) {
		this.itemCat = itemCat;
	}
	public ItemCat getItemCat( ) {
		return this.itemCat;
	}

}
