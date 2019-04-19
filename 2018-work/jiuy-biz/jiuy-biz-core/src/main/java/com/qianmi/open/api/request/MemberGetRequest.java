package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MemberGetResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.member.get request
 *
 * @author auto
 * @since 1.0
 */
public class MemberGetRequest implements QianmiRequest<MemberGetResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 返回的字段列表
	 */
	private String fields;

	/** 
	 * 会员编号，会员编号与会员昵称至少有一个不能为空，如果都不为空，则按会员编号查找
	 */
	private String memberId;

	/** 
	 * 会员昵称，会员编号与会员昵称至少有一个不能为空，如果都不为空，则按会员编号查找
	 */
	private String memberNick;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberNick(String memberNick) {
		this.memberNick = memberNick;
	}
	public String getMemberNick() {
		return this.memberNick;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.member.get";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("member_id", this.memberId);
		txtParams.put("member_nick", this.memberNick);
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

	public Class<MemberGetResponse> getResponseClass() {
		return MemberGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}