package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.LogisticsAddressSearchResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.logistics.address.search request
 *
 * @author auto
 * @since 1.0
 */
public class LogisticsAddressSearchRequest implements QianmiRequest<LogisticsAddressSearchResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 需要返回的字段
	 */
	private String fields;

	/** 
	 * 地址类型，默认获取所有地址
	 */
	private String rdef;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setRdef(String rdef) {
		this.rdef = rdef;
	}
	public String getRdef() {
		return this.rdef;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.logistics.address.search";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("rdef", this.rdef);
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

	public Class<LogisticsAddressSearchResponse> getResponseClass() {
		return LogisticsAddressSearchResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}