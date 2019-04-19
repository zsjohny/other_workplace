package com.qianmi.open.api.request;

import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.QmcsMessagesConsumeResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.qmcs.messages.consume request
 *
 * @author auto
 * @since 1.0
 */
public class QmcsMessagesConsumeRequest implements QianmiRequest<QmcsMessagesConsumeResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 用户分组名称，不传表示消费默认分组
	 */
	private String groupName;

	/** 
	 * 每次批量消费消息的条数，默认值100，最多100
	 */
	private Integer quantity;

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupName() {
		return this.groupName;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getQuantity() {
		return this.quantity;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.qmcs.messages.consume";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("group_name", this.groupName);
		txtParams.put("quantity", this.quantity);
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

	public Class<QmcsMessagesConsumeResponse> getResponseClass() {
		return QmcsMessagesConsumeResponse.class;
	}

	public void check() throws ApiRuleException {
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}