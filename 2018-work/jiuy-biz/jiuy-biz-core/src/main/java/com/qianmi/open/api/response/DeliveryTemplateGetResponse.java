package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.DeliveryTemplate;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.delivery.template.get response.
 *
 * @author auto
 * @since 2.0
 */
public class DeliveryTemplateGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 运费模板详情
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
