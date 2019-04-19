package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.MemberLevel;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.member.levels.list response.
 *
 * @author auto
 * @since 2.0
 */
public class MemberLevelsListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 会员等级列表
	 */
	@ApiListField("member_levels")
	@ApiField("member_level")
	private List<MemberLevel> memberLevels;

	public void setMemberLevels(List<MemberLevel> memberLevels) {
		this.memberLevels = memberLevels;
	}
	public List<MemberLevel> getMemberLevels( ) {
		return this.memberLevels;
	}

}
