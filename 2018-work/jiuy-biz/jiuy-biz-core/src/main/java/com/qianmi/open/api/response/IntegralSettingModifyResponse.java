package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.IntegralRate;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.integral.setting.modify response.
 *
 * @author auto
 * @since 2.0
 */
public class IntegralSettingModifyResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回是否成功is_success
	 */
	@ApiField("integralRate")
	private IntegralRate integralRate;

	public void setIntegralRate(IntegralRate integralRate) {
		this.integralRate = integralRate;
	}
	public IntegralRate getIntegralRate( ) {
		return this.integralRate;
	}

}
