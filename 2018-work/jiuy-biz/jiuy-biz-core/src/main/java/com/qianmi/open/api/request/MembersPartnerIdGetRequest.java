package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MembersPartnerIdGetResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.members.partner.id.get request
 *
 * @author auto
 * @since 1.0
 */
public class MembersPartnerIdGetRequest implements QianmiRequest<MembersPartnerIdGetResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 会员编号，用逗号隔开
	 */
	private String memberIds;

	/** 
	 * 第三方平台标识
	 */
	private String partner;

	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}
	public String getMemberIds() {
		return this.memberIds;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getPartner() {
		return this.partner;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.members.partner.id.get";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("member_ids", this.memberIds);
		txtParams.put("partner", this.partner);
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

	public Class<MembersPartnerIdGetResponse> getResponseClass() {
		return MembersPartnerIdGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(memberIds, "memberIds");
		RequestCheckUtils.checkNotEmpty(partner, "partner");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}