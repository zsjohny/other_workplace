package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.IntegralSettingModifyResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.integral.setting.modify request
 *
 * @author auto
 * @since 1.0
 */
public class IntegralSettingModifyRequest implements QianmiRequest<IntegralSettingModifyResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 积分设置，范围1-1000之间的整数 
	 */
	private String rate;

	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getRate() {
		return this.rate;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.integral.setting.modify";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("rate", this.rate);
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

	public Class<IntegralSettingModifyResponse> getResponseClass() {
		return IntegralSettingModifyResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(rate, "rate");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}