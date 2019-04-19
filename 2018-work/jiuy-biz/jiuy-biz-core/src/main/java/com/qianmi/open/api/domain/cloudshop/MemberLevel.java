package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 会员等级信息
 *
 * @author auto
 * @since 2.0
 */
public class MemberLevel extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否是默认等级
	 */
	@ApiField("is_default")
	private String isDefault;

	/**
	 * 是否发货成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 等级编号
	 */
	@ApiField("level_id")
	private String levelId;

	/**
	 * 等级名称
	 */
	@ApiField("level_name")
	private String levelName;

	/**
	 * 等级类型，1：个人会员等级，2：分销商等级
	 */
	@ApiField("type")
	private String type;

	/**
	 * 会员等级升级金额
	 */
	@ApiField("upgrade_money")
	private String upgradeMoney;

	public String getIsDefault() {
		return this.isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
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

	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getUpgradeMoney() {
		return this.upgradeMoney;
	}
	public void setUpgradeMoney(String upgradeMoney) {
		this.upgradeMoney = upgradeMoney;
	}

}