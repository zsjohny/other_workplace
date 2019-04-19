package com.jiuyuan.entity;

import java.util.List;

import com.jiuy.core.meta.jiucoinsetting.ContinuousSetting;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月23日 上午10:14:45
*/
public class JiuCoinSetting {
	
	private JiuCoinAttribute jiuCoinAttribute;
	
	private int registerJiuCoinSetting; 
	
	private ShoppingJiuCoinSetting shoppingJiuCoinSetting;
	
	private StoreScanReward storeScanReward;
	
	private PromoteShareSetting promoteShareSetting;
	
	private SignInSetting signInSetting;
	
	private int giftBag;
	
	private InvitationSetting invitationSetting;
	
	private ExchangeGoodsSetting exchangeGoodsSetting;
	
	
	
	public JiuCoinAttribute getJiuCoinAttribute() {
		return jiuCoinAttribute;
	}

	public void setJiuCoinAttribute(JiuCoinAttribute jiuCoinAttribute) {
		this.jiuCoinAttribute = jiuCoinAttribute;
	}

	public int getRegisterJiuCoinSetting() {
		return registerJiuCoinSetting;
	}

	public void setRegisterJiuCoinSetting(int registerJiuCoinSetting) {
		this.registerJiuCoinSetting = registerJiuCoinSetting;
	}

	public ShoppingJiuCoinSetting getShoppingJiuCoinSetting() {
		return shoppingJiuCoinSetting;
	}

	public void setShoppingJiuCoinSetting(ShoppingJiuCoinSetting shoppingJiuCoinSetting) {
		this.shoppingJiuCoinSetting = shoppingJiuCoinSetting;
	}

	public StoreScanReward getStoreScanReward() {
		return storeScanReward;
	}

	public void setStoreScanReward(StoreScanReward storeScanReward) {
		this.storeScanReward = storeScanReward;
	}

	public PromoteShareSetting getPromoteShareSetting() {
		return promoteShareSetting;
	}

	public void setPromoteShareSetting(PromoteShareSetting promoteShareSetting) {
		this.promoteShareSetting = promoteShareSetting;
	}

	public SignInSetting getSignInSetting() {
		return signInSetting;
	}

	public void setSignInSetting(SignInSetting signInSetting) {
		this.signInSetting = signInSetting;
	}

	public int getGiftBag() {
		return giftBag;
	}

	public void setGiftBag(int giftBag) {
		this.giftBag = giftBag;
	}

	public InvitationSetting getInvitationSetting() {
		return invitationSetting;
	}

	public void setInvitationSetting(InvitationSetting invitationSetting) {
		this.invitationSetting = invitationSetting;
	}

	public ExchangeGoodsSetting getExchangeGoodsSetting() {
		return exchangeGoodsSetting;
	}

	public void setExchangeGoodsSetting(ExchangeGoodsSetting exchangeGoodsSetting) {
		this.exchangeGoodsSetting = exchangeGoodsSetting;
	}

	public class JiuCoinAttribute{
		private int worthRmb;
		private long nextExpirationTime;
		
		public int getWorthRmb() {
			return worthRmb;
		}
		public void setWorthRmb(int worthRmb) {
			this.worthRmb = worthRmb;
		}
		public long getNextExpirationTime() {
			return nextExpirationTime;
		}
		public void setNextExpirationTime(long nextExpirationTime) {
			this.nextExpirationTime = nextExpirationTime;
		}
	}
	
	public class ShoppingJiuCoinSetting{
		private double returnPercentage;
		private int evaluationPercentage;
		public double getReturnPercentage() {
			return returnPercentage;
		}
		public void setReturnPercentage(double returnPercentage) {
			this.returnPercentage = returnPercentage;
		}
		public int getEvaluationPercentage() {
			return evaluationPercentage;
		}
		public void setEvaluationPercentage(int evaluationPercentage) {
			this.evaluationPercentage = evaluationPercentage;
		}
	}
	
	public class StoreScanReward{
		private int eachObtain;
		private int maxScanCount;
		private int obtainCycle;
		private int maxCountEachCycle;
		public int getEachObtain() {
			return eachObtain;
		}
		public void setEachObtain(int eachObtain) {
			this.eachObtain = eachObtain;
		}
		public int getMaxScanCount() {
			return maxScanCount;
		}
		public void setMaxScanCount(int maxScanCount) {
			this.maxScanCount = maxScanCount;
		}
		public int getObtainCycle() {
			return obtainCycle;
		}
		public void setObtainCycle(int obtainCycle) {
			this.obtainCycle = obtainCycle;
		}
		public int getMaxCountEachCycle() {
			return maxCountEachCycle;
		}
		public void setMaxCountEachCycle(int maxCountEachCycle) {
			this.maxCountEachCycle = maxCountEachCycle;
		}
	}
	
	public class PromoteShareSetting{
		private int eachShareObtain;
		private int shareCycle;
		private int maxCountEachShareCycle;
		private int eachClickObtain;
		private int clickCycle;
		private int maxCountEachClickCycle;
		public int getEachShareObtain() {
			return eachShareObtain;
		}
		public void setEachShareObtain(int eachShareObtain) {
			this.eachShareObtain = eachShareObtain;
		}
		public int getShareCycle() {
			return shareCycle;
		}
		public void setShareCycle(int shareCycle) {
			this.shareCycle = shareCycle;
		}
		public int getMaxCountEachShareCycle() {
			return maxCountEachShareCycle;
		}
		public void setMaxCountEachShareCycle(int maxCountEachShareCycle) {
			this.maxCountEachShareCycle = maxCountEachShareCycle;
		}
		public int getEachClickObtain() {
			return eachClickObtain;
		}
		public void setEachClickObtain(int eachClickObtain) {
			this.eachClickObtain = eachClickObtain;
		}
		public int getClickCycle() {
			return clickCycle;
		}
		public void setClickCycle(int clickCycle) {
			this.clickCycle = clickCycle;
		}
		public int getMaxCountEachClickCycle() {
			return maxCountEachClickCycle;
		}
		public void setMaxCountEachClickCycle(int maxCountEachClickCycle) {
			this.maxCountEachClickCycle = maxCountEachClickCycle;
		}
	}
	
	public class SignInSetting{
		private int dailyObtain;
		private List<ContinuousSetting> continuousSetting;
		
		public SignInSetting(){}
		
		public int getDailyObtain() {
			return dailyObtain;
		}
		public void setDailyObtain(int dailyObtain) {
			this.dailyObtain = dailyObtain;
		}
		public List<ContinuousSetting> getContinuousSetting() {
			return continuousSetting;
		}
		public void setContinuousSetting(List<ContinuousSetting> continuousSetting) {
			this.continuousSetting = continuousSetting;
		}
	}
	
	public class InvitationSetting{
		private int eachObtain;
		private int invitationCycle;
		private int maxCountCycle;
		private double returnPercentage;
		private int returnCycle;
		private int maxJiuCoinReturnCycle;
		
		public InvitationSetting(){}
		
		public int getEachObtain() {
			return eachObtain;
		}
		public void setEachObtain(int eachObtain) {
			this.eachObtain = eachObtain;
		}
		public int getInvitationCycle() {
			return invitationCycle;
		}
		public void setInvitationCycle(int invitationCycle) {
			this.invitationCycle = invitationCycle;
		}
		public int getMaxCountCycle() {
			return maxCountCycle;
		}
		public void setMaxCountCycle(int maxCountCycle) {
			this.maxCountCycle = maxCountCycle;
		}
		public double getReturnPercentage() {
			return returnPercentage;
		}
		public void setReturnPercentage(double returnPercentage) {
			this.returnPercentage = returnPercentage;
		}
		public int getReturnCycle() {
			return returnCycle;
		}
		public void setReturnCycle(int returnCycle) {
			this.returnCycle = returnCycle;
		}
		public int getMaxJiuCoinReturnCycle() {
			return maxJiuCoinReturnCycle;
		}
		public void setMaxJiuCoinReturnCycle(int maxJiuCoinReturnCycle) {
			this.maxJiuCoinReturnCycle = maxJiuCoinReturnCycle;
		}
	}
	
	public class ExchangeGoodsSetting{
		private int cycle;
		private int maxCount;
		
		public ExchangeGoodsSetting(){}
		
		public int getCycle() {
			return cycle;
		}
		public void setCycle(int cycle) {
			this.cycle = cycle;
		}
		public int getMaxCount() {
			return maxCount;
		}
		public void setMaxCount(int maxCount) {
			this.maxCount = maxCount;
		}
	}
}
