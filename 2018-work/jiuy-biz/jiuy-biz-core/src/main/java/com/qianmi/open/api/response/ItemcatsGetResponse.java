package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.ItemCat;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.itemcats.get response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemcatsGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 仓库商品类目
	 */
	@ApiListField("itemCats")
	@ApiField("item_cat")
	private List<ItemCat> itemCats;

	public void setItemCats(List<ItemCat> itemCats) {
		this.itemCats = itemCats;
	}
	public List<ItemCat> getItemCats( ) {
		return this.itemCats;
	}

}
