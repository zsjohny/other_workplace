package com.yujj.business.service.globalsetting.bean;

import java.util.List;

/**
 * 数据库yjj_GlobalSetting表中jiucoin_global_setting对应的propertyValue的一个值
 * 暂不确定是什么值
 * @author qyf
 *
 */
public class SignInSetting {
	/**
	 * 
	 */
	private List<ContinuousSetting> continuousSetting;
	/**
	 * 
	 */
	private String dailyObtain;
	public List<ContinuousSetting> getContinuousSetting() {
		return continuousSetting;
	}
	public void setContinuousSetting(List<ContinuousSetting> continuousSetting) {
		this.continuousSetting = continuousSetting;
	}
	public String getDailyObtain() {
		return dailyObtain;
	}
	public void setDailyObtain(String dailyObtain) {
		this.dailyObtain = dailyObtain;
	}
	
}
