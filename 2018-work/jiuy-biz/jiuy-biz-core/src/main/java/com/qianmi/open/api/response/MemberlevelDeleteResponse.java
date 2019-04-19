package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.MemberLevel;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.memberlevel.delete response.
 *
 * @author auto
 * @since 2.0
 */
public class MemberlevelDeleteResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 操作状态
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
