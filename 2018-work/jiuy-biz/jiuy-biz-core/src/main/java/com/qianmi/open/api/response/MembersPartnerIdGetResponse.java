package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.MemberPartner;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.members.partner.id.get response.
 *
 * @author auto
 * @since 2.0
 */
public class MembersPartnerIdGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 会员第三方平台信息
	 */
	@ApiListField("member_partners")
	@ApiField("member_partner")
	private List<MemberPartner> memberPartners;

	public void setMemberPartners(List<MemberPartner> memberPartners) {
		this.memberPartners = memberPartners;
	}
	public List<MemberPartner> getMemberPartners( ) {
		return this.memberPartners;
	}

}
