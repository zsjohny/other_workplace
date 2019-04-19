package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Trade;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.trade.postage.update response.
 *
 * @author auto
 * @since 2.0
 */
public class TradePostageUpdateResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回修改交易物流费用是否成功 is_success
	 */
	@ApiField("trade")
	private Trade trade;

	public void setTrade(Trade trade) {
		this.trade = trade;
	}
	public Trade getTrade( ) {
		return this.trade;
	}

}
