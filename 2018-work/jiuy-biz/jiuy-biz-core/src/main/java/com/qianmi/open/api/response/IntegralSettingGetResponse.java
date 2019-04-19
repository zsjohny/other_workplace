package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.IntegralRate;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.integral.setting.get response.
 *
 * @author auto
 * @since 2.0
 */
public class IntegralSettingGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 店铺积分设置
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
