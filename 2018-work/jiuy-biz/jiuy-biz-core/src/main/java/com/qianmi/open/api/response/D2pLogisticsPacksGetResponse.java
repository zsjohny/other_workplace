package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Pack;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2p.logistics.packs.get response.
 *
 * @author auto
 * @since 2.0
 */
public class D2pLogisticsPacksGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 包裹信息
	 */
	@ApiListField("packs")
	@ApiField("pack")
	private List<Pack> packs;

	public void setPacks(List<Pack> packs) {
		this.packs = packs;
	}
	public List<Pack> getPacks( ) {
		return this.packs;
	}

}
