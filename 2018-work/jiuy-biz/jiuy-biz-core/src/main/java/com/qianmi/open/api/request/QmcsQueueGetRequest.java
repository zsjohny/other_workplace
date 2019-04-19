package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.QmcsQueueGetResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.qmcs.queue.get request
 *
 * @author auto
 * @since 1.0
 */
public class QmcsQueueGetRequest implements QianmiRequest<QmcsQueueGetResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * null
	 */
	private String groupName;

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupName() {
		return this.groupName;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.qmcs.queue.get";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("group_name", this.groupName);
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

	public Class<QmcsQueueGetResponse> getResponseClass() {
		return QmcsQueueGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(groupName, "groupName");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}