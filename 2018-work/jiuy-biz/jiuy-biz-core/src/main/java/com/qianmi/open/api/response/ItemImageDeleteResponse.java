package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.image.delete response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemImageDeleteResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 操作是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Boolean getIsSuccess( ) {
		return this.isSuccess;
	}

}
