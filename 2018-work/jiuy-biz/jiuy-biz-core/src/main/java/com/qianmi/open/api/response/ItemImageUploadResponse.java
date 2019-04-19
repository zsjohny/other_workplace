package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ItemImg;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.image.upload response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemImageUploadResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 图片对象
	 */
	@ApiField("item_img")
	private ItemImg itemImg;

	public void setItemImg(ItemImg itemImg) {
		this.itemImg = itemImg;
	}
	public ItemImg getItemImg( ) {
		return this.itemImg;
	}

}
