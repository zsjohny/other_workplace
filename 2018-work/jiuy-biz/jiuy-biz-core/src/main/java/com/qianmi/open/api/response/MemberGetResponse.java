package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Member;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.member.get response.
 *
 * @author auto
 * @since 2.0
 */
public class MemberGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 会员信息
	 */
	@ApiField("member")
	private Member member;

	public void setMember(Member member) {
		this.member = member;
	}
	public Member getMember( ) {
		return this.member;
	}

}
