package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MemberAddResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.member.add request
 *
 * @author auto
 * @since 1.0
 */
public class MemberAddRequest implements QianmiRequest<MemberAddResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 会员等级编号 ，读取会员等级设置
	 */
	private String memberLevel;

	/** 
	 * 用户名长度在1-20个字符之间
	 */
	private String memberNick;

	/** 
	 * 会员类型,1:个人会员，4:网点会员
	 */
	private String memberType;

	/** 
	 * 密码,不能明文传输，需要AES对称加密
	 */
	private String password;

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}
	public String getMemberLevel() {
		return this.memberLevel;
	}

	public void setMemberNick(String memberNick) {
		this.memberNick = memberNick;
	}
	public String getMemberNick() {
		return this.memberNick;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMemberType() {
		return this.memberType;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return this.password;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.member.add";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("member_level", this.memberLevel);
		txtParams.put("member_nick", this.memberNick);
		txtParams.put("member_type", this.memberType);
		txtParams.put("password", this.password);
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

	public Class<MemberAddResponse> getResponseClass() {
		return MemberAddResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(memberLevel, "memberLevel");
		RequestCheckUtils.checkNotEmpty(memberNick, "memberNick");
		RequestCheckUtils.checkNotEmpty(memberType, "memberType");
		RequestCheckUtils.checkNotEmpty(password, "password");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}