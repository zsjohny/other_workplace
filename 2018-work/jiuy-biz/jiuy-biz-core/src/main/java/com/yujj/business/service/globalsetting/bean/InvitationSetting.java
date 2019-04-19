package com.yujj.business.service.globalsetting.bean;

/**
 * 后台设置：1、分享注册送的玖币数2、分享下单送的玖币数
 * 暂不确定是什么值
 * @author qyf
 *
 */
public class InvitationSetting {
	/**分享注册送玖币数量注册的玖币数*/
	private String eachObtain;//
	/**分享注册送玖币的限制天数(几天内只允许maxCountCycle次)*/
	private String invitationCycle;
	/**分享注册送玖币的限制次数(invitationCycle天内只允许几次)*/
	private String maxCountCycle;
	/**分享下单送玖币的限制总数(returnCycle天内只允许获得最多几个玖币)*/
	private String maxJiuCoinReturnCycle;
	/**分享下单送玖币的限制天数(几天内只允许获得最多maxJiuCoinReturnCycle个玖币)*/
	private String returnCycle;
	/**分享下单送玖币的数量(下单金额送的玖币百分比)*/
	private String returnPercentage;//
	public String getEachObtain() {
		return eachObtain;
	}
	public void setEachObtain(String eachObtain) {
		this.eachObtain = eachObtain;
	}
	public String getInvitationCycle() {
		return invitationCycle;
	}
	public void setInvitationCycle(String invitationCycle) {
		this.invitationCycle = invitationCycle;
	}
	public String getMaxCountCycle() {
		return maxCountCycle;
	}
	public void setMaxCountCycle(String maxCountCycle) {
		this.maxCountCycle = maxCountCycle;
	}
	public String getMaxJiuCoinReturnCycle() {
		return maxJiuCoinReturnCycle;
	}
	public void setMaxJiuCoinReturnCycle(String maxJiuCoinReturnCycle) {
		this.maxJiuCoinReturnCycle = maxJiuCoinReturnCycle;
	}
	public String getReturnCycle() {
		return returnCycle;
	}
	public void setReturnCycle(String returnCycle) {
		this.returnCycle = returnCycle;
	}
	public String getReturnPercentage() {
		return returnPercentage;
	}
	public void setReturnPercentage(String returnPercentage) {
		this.returnPercentage = returnPercentage;
	}
	
}
