package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Trade;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2c.trades.sold.increment.get response.
 *
 * @author auto
 * @since 2.0
 */
public class D2cTradesSoldIncrementGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 查询得到的交易信息总数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	/** 
	 * 交易数据列表
	 */
	@ApiListField("trades")
	@ApiField("trade")
	private List<Trade> trades;

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}
	public List<Trade> getTrades( ) {
		return this.trades;
	}

}
