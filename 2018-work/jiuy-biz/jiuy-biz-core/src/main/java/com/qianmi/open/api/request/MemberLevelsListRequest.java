package com.qianmi.open.api.request;

import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MemberLevelsListResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.member.levels.list request
 *
 * @author auto
 * @since 1.0
 */
public class MemberLevelsListRequest implements QianmiRequest<MemberLevelsListResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 会员等级类型，1：个人会员等级，2：分销商等级，不传则查询所有
	 */
	private String type;

	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return this.type;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.member.levels.list";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("type", this.type);
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

	public Class<MemberLevelsListResponse> getResponseClass() {
		return MemberLevelsListResponse.class;
	}

	public void check() throws ApiRuleException {
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}