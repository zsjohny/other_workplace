package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Trade;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2p.trade.cancel response.
 *
 * @author auto
 * @since 2.0
 */
public class D2pTradeCancelResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回是否成功is_success
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
