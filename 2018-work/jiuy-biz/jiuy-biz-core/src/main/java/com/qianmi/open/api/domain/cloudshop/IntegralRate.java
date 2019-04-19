package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 店铺积分设置
 *
 * @author auto
 * @since 2.0
 */
public class IntegralRate extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 操作是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 多少积分=1元
	 */
	@ApiField("rate")
	private String rate;

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getRate() {
		return this.rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}

}