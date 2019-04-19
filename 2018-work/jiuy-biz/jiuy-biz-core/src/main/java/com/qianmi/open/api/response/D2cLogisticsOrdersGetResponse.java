package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Shipping;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2c.logistics.orders.get response.
 *
 * @author auto
 * @since 2.0
 */
public class D2cLogisticsOrdersGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 物流单列表
	 */
	@ApiListField("shippings")
	@ApiField("shipping")
	private List<Shipping> shippings;

	/** 
	 * 总条数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setShippings(List<Shipping> shippings) {
		this.shippings = shippings;
	}
	public List<Shipping> getShippings( ) {
		return this.shippings;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
