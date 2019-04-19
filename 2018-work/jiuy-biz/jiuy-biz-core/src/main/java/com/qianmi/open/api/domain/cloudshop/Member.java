package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 会员信息
 *
 * @author auto
 * @since 2.0
 */
public class Member extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 所属admin编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 审核状态，0：待审核，1：审核通过，-1：审核拒绝，只有经销商需要审核
	 */
	@ApiField("audit_status")
	private String auditStatus;

	/**
	 * 邮箱
	 */
	@ApiField("email")
	private String email;

	/**
	 * 扩展信息
	 */
	@ApiField("expand_infos")
	private String expandInfos;

	/**
	 * 积分
	 */
	@ApiField("integral")
	private String integral;

	/**
	 * 是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 邮箱是否验证，0：未验证，1：已验证
	 */
	@ApiField("is_verify_email")
	private String isVerifyEmail;

	/**
	 * 会员等级编号
	 */
	@ApiField("level_id")
	private String levelId;

	/**
	 * 会员等级名称
	 */
	@ApiField("level_name")
	private String levelName;

	/**
	 * 锁定状态，0：锁定，1：正常
	 */
	@ApiField("lock_status")
	private Integer lockStatus;

	/**
	 * 会员编号
	 */
	@ApiField("member_id")
	private String memberId;

	/**
	 * 会员昵称
	 */
	@ApiField("member_nick")
	private String memberNick;

	/**
	 * 网点会员的店铺名称
	 */
	@ApiField("member_shop")
	private String memberShop;

	/**
	 * 用户类型，1：个人会员，4：分销商
	 */
	@ApiField("member_type")
	private String memberType;

	/**
	 * 手机号码
	 */
	@ApiField("mobile")
	private String mobile;

	/**
	 * 会员姓名
	 */
	@ApiField("name")
	private String name;

	/**
	 * 推荐人编号
	 */
	@ApiField("refer_user_id")
	private String referUserId;

	/**
	 * 推荐人姓名
	 */
	@ApiField("refer_user_name")
	private String referUserName;

	/**
	 * 注册来源
	 */
	@ApiField("reg_source")
	private String regSource;

	/**
	 * 注册时间
	 */
	@ApiField("register_time")
	private String registerTime;

	/**
	 * admin自定义销售区域编号
	 */
	@ApiField("sale_area_id")
	private String saleAreaId;

	/**
	 * admin自定义销售区域名称
	 */
	@ApiField("sale_area_name")
	private String saleAreaName;

	/**
	 * 销售员编号
	 */
	@ApiField("sale_user_id")
	private String saleUserId;

	/**
	 * 销售员名称
	 */
	@ApiField("sale_user_name")
	private String saleUserName;

	/**
	 * 用户状态，1：正常，2：已删除
	 */
	@ApiField("status")
	private Integer status;

	/**
	 * 累计消费
	 */
	@ApiField("total_consumption")
	private String totalConsumption;

	/**
	 * 会员等级更新类型，0：自动，1：手动
	 */
	@ApiField("update_type")
	private String updateType;

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getExpandInfos() {
		return this.expandInfos;
	}
	public void setExpandInfos(String expandInfos) {
		this.expandInfos = expandInfos;
	}

	public String getIntegral() {
		return this.integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getIsVerifyEmail() {
		return this.isVerifyEmail;
	}
	public void setIsVerifyEmail(String isVerifyEmail) {
		this.isVerifyEmail = isVerifyEmail;
	}

	public String getLevelId() {
		return this.levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return this.levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Integer getLockStatus() {
		return this.lockStatus;
	}
	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getMemberId() {
		return this.memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberNick() {
		return this.memberNick;
	}
	public void setMemberNick(String memberNick) {
		this.memberNick = memberNick;
	}

	public String getMemberShop() {
		return this.memberShop;
	}
	public void setMemberShop(String memberShop) {
		this.memberShop = memberShop;
	}

	public String getMemberType() {
		return this.memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getMobile() {
		return this.mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getReferUserId() {
		return this.referUserId;
	}
	public void setReferUserId(String referUserId) {
		this.referUserId = referUserId;
	}

	public String getReferUserName() {
		return this.referUserName;
	}
	public void setReferUserName(String referUserName) {
		this.referUserName = referUserName;
	}

	public String getRegSource() {
		return this.regSource;
	}
	public void setRegSource(String regSource) {
		this.regSource = regSource;
	}

	public String getRegisterTime() {
		return this.registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getSaleAreaId() {
		return this.saleAreaId;
	}
	public void setSaleAreaId(String saleAreaId) {
		this.saleAreaId = saleAreaId;
	}

	public String getSaleAreaName() {
		return this.saleAreaName;
	}
	public void setSaleAreaName(String saleAreaName) {
		this.saleAreaName = saleAreaName;
	}

	public String getSaleUserId() {
		return this.saleUserId;
	}
	public void setSaleUserId(String saleUserId) {
		this.saleUserId = saleUserId;
	}

	public String getSaleUserName() {
		return this.saleUserName;
	}
	public void setSaleUserName(String saleUserName) {
		this.saleUserName = saleUserName;
	}

	public Integer getStatus() {
		return this.status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTotalConsumption() {
		return this.totalConsumption;
	}
	public void setTotalConsumption(String totalConsumption) {
		this.totalConsumption = totalConsumption;
	}

	public String getUpdateType() {
		return this.updateType;
	}
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

}