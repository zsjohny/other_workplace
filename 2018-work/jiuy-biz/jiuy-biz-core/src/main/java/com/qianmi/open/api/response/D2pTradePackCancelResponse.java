package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Pack;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2p.trade.pack.cancel response.
 *
 * @author auto
 * @since 2.0
 */
public class D2pTradePackCancelResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 出库结果，只返回is_success字段
	 */
	@ApiField("pack")
	private Pack pack;

	public void setPack(Pack pack) {
		this.pack = pack;
	}
	public Pack getPack( ) {
		return this.pack;
	}

}
