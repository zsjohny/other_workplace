package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.LogisticsAddressDeleteResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.logistics.address.delete request
 *
 * @author auto
 * @since 1.0
 */
public class LogisticsAddressDeleteRequest implements QianmiRequest<LogisticsAddressDeleteResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 地址库ID
	 */
	private String contactId;

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getContactId() {
		return this.contactId;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.logistics.address.delete";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("contact_id", this.contactId);
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

	public Class<LogisticsAddressDeleteResponse> getResponseClass() {
		return LogisticsAddressDeleteResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(contactId, "contactId");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}