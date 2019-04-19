package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Member;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.members.list response.
 *
 * @author auto
 * @since 2.0
 */
public class MembersListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 会员列表
	 */
	@ApiListField("members")
	@ApiField("member")
	private List<Member> members;

	/** 
	 * 总数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setMembers(List<Member> members) {
		this.members = members;
	}
	public List<Member> getMembers( ) {
		return this.members;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
