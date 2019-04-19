package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.DeliveryTemplate;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.delivery.templates.list response.
 *
 * @author auto
 * @since 2.0
 */
public class DeliveryTemplatesListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 运费模板列表
	 */
	@ApiListField("delivery_templates")
	@ApiField("delivery_template")
	private List<DeliveryTemplate> deliveryTemplates;

	/** 
	 * 返回运费模板的总条数
	 */
	@ApiField("total_result")
	private Integer totalResult;

	public void setDeliveryTemplates(List<DeliveryTemplate> deliveryTemplates) {
		this.deliveryTemplates = deliveryTemplates;
	}
	public List<DeliveryTemplate> getDeliveryTemplates( ) {
		return this.deliveryTemplates;
	}

	public void setTotalResult(Integer totalResult) {
		this.totalResult = totalResult;
	}
	public Integer getTotalResult( ) {
		return this.totalResult;
	}

}
