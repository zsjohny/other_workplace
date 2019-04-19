package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.ItemProp;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.props.get response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemPropsGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 商品规格列表
	 */
	@ApiListField("item_props")
	@ApiField("item_prop")
	private List<ItemProp> itemProps;

	public void setItemProps(List<ItemProp> itemProps) {
		this.itemProps = itemProps;
	}
	public List<ItemProp> getItemProps( ) {
		return this.itemProps;
	}

}
