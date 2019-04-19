package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.itemcat.add response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemcatAddResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 新增成功的类目对象
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
