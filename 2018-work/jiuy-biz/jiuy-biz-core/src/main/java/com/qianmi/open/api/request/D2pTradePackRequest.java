package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.D2pTradePackResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.d2p.trade.pack request
 *
 * @author auto
 * @since 1.0
 */
public class D2pTradePackRequest implements QianmiRequest<D2pTradePackResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 发货包裹清单，包含商品单编号和商品出库数量，格式：oid:num;oid:num。商品出库数量为整数，且出库总数量不能大于商品的购买数量
	 */
	private String packItems;

	/** 
	 * 订单编号
	 */
	private String tid;

	public void setPackItems(String packItems) {
		this.packItems = packItems;
	}
	public String getPackItems() {
		return this.packItems;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTid() {
		return this.tid;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.d2p.trade.pack";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("pack_items", this.packItems);
		txtParams.put("tid", this.tid);
		if(udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new QianmiHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<D2pTradePackResponse> getResponseClass() {
		return D2pTradePackResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(packItems, "packItems");
		RequestCheckUtils.checkNotEmpty(tid, "tid");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}