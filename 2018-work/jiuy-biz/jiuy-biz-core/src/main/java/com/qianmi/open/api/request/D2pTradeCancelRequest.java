package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.D2pTradeCancelResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.d2p.trade.cancel request
 *
 * @author auto
 * @since 1.0
 */
public class D2pTradeCancelRequest implements QianmiRequest<D2pTradeCancelResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 订单编号
	 */
	private String tid;

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
		return "qianmi.cloudshop.d2p.trade.cancel";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
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

	public Class<D2pTradeCancelResponse> getResponseClass() {
		return D2pTradeCancelResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(tid, "tid");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}