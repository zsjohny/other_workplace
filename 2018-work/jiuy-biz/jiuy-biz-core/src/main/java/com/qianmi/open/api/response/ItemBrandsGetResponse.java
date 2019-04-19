package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.ItemBrand;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.brands.get response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemBrandsGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 商品品牌列表
	 */
	@ApiListField("itemBrands")
	@ApiField("itemBrand")
	private List<ItemBrand> itemBrands;

	public void setItemBrands(List<ItemBrand> itemBrands) {
		this.itemBrands = itemBrands;
	}
	public List<ItemBrand> getItemBrands( ) {
		return this.itemBrands;
	}

}
