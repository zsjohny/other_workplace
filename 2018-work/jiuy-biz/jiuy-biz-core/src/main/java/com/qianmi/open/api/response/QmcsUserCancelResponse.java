package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.qmcs.user.cancel response.
 *
 * @author auto
 * @since 2.0
 */
public class QmcsUserCancelResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 是否成功
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
