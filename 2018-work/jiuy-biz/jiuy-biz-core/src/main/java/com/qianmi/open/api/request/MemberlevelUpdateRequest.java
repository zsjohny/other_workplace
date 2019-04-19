package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MemberlevelUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.memberlevel.update request
 *
 * @author auto
 * @since 1.0
 */
public class MemberlevelUpdateRequest implements QianmiRequest<MemberlevelUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * admin编号
	 */
	private String adminId;

	/** 
	 * 是否默认等级，1.默认的true,2.非默认会员false,默认false
	 */
	private boolean isDefault;

	/** 
	 * 等级id
	 */
	private String levelId;

	/** 
	 * 等级名称
	 */
	private String levelName;

	/** 
	 * 等级类型：1:个人会员等级 2:分销商等级，默认值1
	 */
	private String type;

	/** 
	 * 升级方式：手动/自动
	 */
	private String upgradeMode;

	/** 
	 *  满此额，则升级
	 */
	private String upgradeMoney;

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminId() {
		return this.adminId;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public boolean getIsDefault() {
		return this.isDefault;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getLevelId() {
		return this.levelId;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getLevelName() {
		return this.levelName;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return this.type;
	}

	public void setUpgradeMode(String upgradeMode) {
		this.upgradeMode = upgradeMode;
	}
	public String getUpgradeMode() {
		return this.upgradeMode;
	}

	public void setUpgradeMoney(String upgradeMoney) {
		this.upgradeMoney = upgradeMoney;
	}
	public String getUpgradeMoney() {
		return this.upgradeMoney;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.memberlevel.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("admin_id", this.adminId);
		txtParams.put("is_default", this.isDefault);
		txtParams.put("level_id", this.levelId);
		txtParams.put("level_name", this.levelName);
		txtParams.put("type", this.type);
		txtParams.put("upgrade_mode", this.upgradeMode);
		txtParams.put("upgrade_money", this.upgradeMoney);
		if(udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new QianmiHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<MemberlevelUpdateResponse> getResponseClass() {
		return MemberlevelUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(upgradeMoney, "upgradeMoney");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}