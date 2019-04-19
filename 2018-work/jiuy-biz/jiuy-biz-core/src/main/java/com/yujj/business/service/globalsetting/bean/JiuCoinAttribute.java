package com.yujj.business.service.globalsetting.bean;
/**
 * 数据库yjj_GlobalSetting表中jiucoin_global_setting对应的propertyValue的一个值
 * 暂不确定是什么值
 * @author qyf
 *
 */
public class JiuCoinAttribute {
	/**
	 * 
	 */
	private String nextExpirationTime;
	/**
	 * 
	 */
	private String worthRmb;
	public String getNextExpirationTime() {
		return nextExpirationTime;
	}
	public void setNextExpirationTime(String nextExpirationTime) {
		this.nextExpirationTime = nextExpirationTime;
	}
	public String getWorthRmb() {
		return worthRmb;
	}
	public void setWorthRmb(String worthRmb) {
		this.worthRmb = worthRmb;
	}
	
}
