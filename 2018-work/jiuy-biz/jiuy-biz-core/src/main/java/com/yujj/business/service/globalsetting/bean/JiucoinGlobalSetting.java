package com.yujj.business.service.globalsetting.bean;

import java.util.List;

/**
 * 积分设置
 * @author zhaoxinglin
 *
 * @version 2017年4月25日上午11:14:45
 */
public class JiucoinGlobalSetting {
	private  ExchangeGoodsSetting exchangeGoodsSetting;
	private  String giftBag;
	private  InvitationSetting invitationSetting;
	private  JiuCoinAttribute jiuCoinAttribute;
	private  PromoteShareSetting promoteShareSetting;
	private  String registerJiuCoinSetting;
	private  List<SevenLimitValue> sevenLimitValue;
	private  ShoppingJiuCoinSetting shoppingJiuCoinSetting;
	private  SignInSetting signInSetting;
	private  StoreScanReward storeScanReward;
	public ExchangeGoodsSetting getExchangeGoodsSetting() {
		return exchangeGoodsSetting;
	}
	public void setExchangeGoodsSetting(ExchangeGoodsSetting exchangeGoodsSetting) {
		this.exchangeGoodsSetting = exchangeGoodsSetting;
	}
	public String getGiftBag() {
		return giftBag;
	}
	public void setGiftBag(String giftBag) {
		this.giftBag = giftBag;
	}
	public InvitationSetting getInvitationSetting() {
		return invitationSetting;
	}
	public void setInvitationSetting(InvitationSetting invitationSetting) {
		this.invitationSetting = invitationSetting;
	}
	public JiuCoinAttribute getJiuCoinAttribute() {
		return jiuCoinAttribute;
	}
	public void setJiuCoinAttribute(JiuCoinAttribute jiuCoinAttribute) {
		this.jiuCoinAttribute = jiuCoinAttribute;
	}
	public PromoteShareSetting getPromoteShareSetting() {
		return promoteShareSetting;
	}
	public void setPromoteShareSetting(PromoteShareSetting promoteShareSetting) {
		this.promoteShareSetting = promoteShareSetting;
	}
	public String getRegisterJiuCoinSetting() {
		return registerJiuCoinSetting;
	}
	public void setRegisterJiuCoinSetting(String registerJiuCoinSetting) {
		this.registerJiuCoinSetting = registerJiuCoinSetting;
	}
	public List<SevenLimitValue> getSevenLimitValue() {
		return sevenLimitValue;
	}
	public void setSevenLimitValue(List<SevenLimitValue> sevenLimitValue) {
		this.sevenLimitValue = sevenLimitValue;
	}
	public ShoppingJiuCoinSetting getShoppingJiuCoinSetting() {
		return shoppingJiuCoinSetting;
	}
	public void setShoppingJiuCoinSetting(ShoppingJiuCoinSetting shoppingJiuCoinSetting) {
		this.shoppingJiuCoinSetting = shoppingJiuCoinSetting;
	}
	public SignInSetting getSignInSetting() {
		return signInSetting;
	}
	public void setSignInSetting(SignInSetting signInSetting) {
		this.signInSetting = signInSetting;
	}
	public StoreScanReward getStoreScanReward() {
		return storeScanReward;
	}
	public void setStoreScanReward(StoreScanReward storeScanReward) {
		this.storeScanReward = storeScanReward;
	}
	
}

