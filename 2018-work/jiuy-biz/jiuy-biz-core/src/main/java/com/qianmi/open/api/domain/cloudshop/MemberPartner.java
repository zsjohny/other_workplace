package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 会员第三方平台信息
 *
 * @author auto
 * @since 2.0
 */
public class MemberPartner extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 会员编号
	 */
	@ApiField("member_id")
	private String memberId;

	/**
	 * 会员的第三方平台编号
	 */
	@ApiField("outer_id")
	private String outerId;

	/**
	 * 第三方平台标志， (0:微信号 1:QQ号 2:手机号 3: 微博号 4: 云协作)
	 */
	@ApiField("partner")
	private String partner;

	public String getMemberId() {
		return this.memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOuterId() {
		return this.outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	public String getPartner() {
		return this.partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}

}