package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.DeliveryTemplate;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.delivery.template.default.set response.
 *
 * @author auto
 * @since 2.0
 */
public class DeliveryTemplateDefaultSetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 只返回是否成功 is_success
	 */
	@ApiField("delivery_template")
	private DeliveryTemplate deliveryTemplate;

	public void setDeliveryTemplate(DeliveryTemplate deliveryTemplate) {
		this.deliveryTemplate = deliveryTemplate;
	}
	public DeliveryTemplate getDeliveryTemplate( ) {
		return this.deliveryTemplate;
	}

}
