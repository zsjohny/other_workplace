package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.itemcat.update response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemcatUpdateResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 修改后的类目对象
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
