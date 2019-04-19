package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.TradeMemoUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.trade.memo.update request
 *
 * @author auto
 * @since 1.0
 */
public class TradeMemoUpdateRequest implements QianmiRequest<TradeMemoUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 订单备注，reset为false时必传
	 */
	private String memo;

	/** 
	 * 是否需要对memo的值清空， 若传true，则忽略新传入的memo值，直接清空原有的memo，若为false，新传入的memo会覆盖原有的memo值
	 */
	private Boolean reset;

	/** 
	 * 订单编号
	 */
	private String tid;

	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemo() {
		return this.memo;
	}

	public void setReset(Boolean reset) {
		this.reset = reset;
	}
	public Boolean getReset() {
		return this.reset;
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
		return "qianmi.cloudshop.trade.memo.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("memo", this.memo);
		txtParams.put("reset", this.reset);
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

	public Class<TradeMemoUpdateResponse> getResponseClass() {
		return TradeMemoUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(reset, "reset");
		RequestCheckUtils.checkNotEmpty(tid, "tid");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}