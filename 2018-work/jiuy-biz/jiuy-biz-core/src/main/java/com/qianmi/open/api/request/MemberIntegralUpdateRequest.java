package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MemberIntegralUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.member.integral.update request
 *
 * @author auto
 * @since 1.0
 */
public class MemberIntegralUpdateRequest implements QianmiRequest<MemberIntegralUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 积分的修改值，两位小数，不能为0。值为正数时，添加积分。值为负数时，减少积分
	 */
	private String integral;

	/** 
	 * 会员编号
	 */
	private String memberId;

	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getIntegral() {
		return this.integral;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberId() {
		return this.memberId;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.member.integral.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("integral", this.integral);
		txtParams.put("member_id", this.memberId);
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

	public Class<MemberIntegralUpdateResponse> getResponseClass() {
		return MemberIntegralUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(integral, "integral");
		RequestCheckUtils.checkNotEmpty(memberId, "memberId");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}