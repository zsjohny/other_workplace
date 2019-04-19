package com.qianmi.open.api.request;

import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MemberLevelModifyResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.member.level.modify request
 *
 * @author auto
 * @since 1.0
 */
public class MemberLevelModifyRequest implements QianmiRequest<MemberLevelModifyResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 会员级别编号
	 */
	private String levelId;

	/** 
	 * 会员编号
	 */
	private String memberId;

	/** 
	 * 会员类型: 1-个人会员, 4-分销商
	 */
	private String memberType;

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getLevelId() {
		return this.levelId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMemberType() {
		return this.memberType;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.member.level.modify";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("level_id", this.levelId);
		txtParams.put("member_id", this.memberId);
		txtParams.put("member_type", this.memberType);
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

	public Class<MemberLevelModifyResponse> getResponseClass() {
		return MemberLevelModifyResponse.class;
	}

	public void check() throws ApiRuleException {
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}