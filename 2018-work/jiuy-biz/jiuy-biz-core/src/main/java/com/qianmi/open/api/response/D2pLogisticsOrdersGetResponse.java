package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Shipping;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2p.logistics.orders.get response.
 *
 * @author auto
 * @since 2.0
 */
public class D2pLogisticsOrdersGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回订单对应物流单列表，字段以fields中输入的为准
	 */
	@ApiListField("shipping")
	@ApiField("shipping")
	private List<Shipping> shipping;

	/** 
	 * 总条数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setShipping(List<Shipping> shipping) {
		this.shipping = shipping;
	}
	public List<Shipping> getShipping( ) {
		return this.shipping;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
