package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.MemberLevel;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.memberlevel.get response.
 *
 * @author auto
 * @since 2.0
 */
public class MemberlevelGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 会员等级信息
	 */
	@ApiField("member_level")
	private MemberLevel memberLevel;

	public void setMemberLevel(MemberLevel memberLevel) {
		this.memberLevel = memberLevel;
	}
	public MemberLevel getMemberLevel( ) {
		return this.memberLevel;
	}

}
