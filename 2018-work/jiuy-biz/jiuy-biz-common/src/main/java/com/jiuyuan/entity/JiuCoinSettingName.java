package com.jiuyuan.entity;

import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.constant.GlobalSettingName;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月22日 下午3:48:41
*/
public enum JiuCoinSettingName {

	/**
	 * 积分属性设置
	 */
	JIU_COIN_ATTRIBUTE("jiuCoinAttribute"),
	
	/**
	 * 注册积分设置
	 */
	REGISTER_JIU_COIN_SETTING("registerJiuCoinSetting"),
	
	/**
	 * 购物积分设置
	 */
	SHOPPING_JIU_COIN_SETTING("shoppingJiuCoinSetting"),
	
	/**
	 * 门店扫码奖励
	 */
	STORE_SCAN_REWARD("storeScanReward"),
	
	/**
	 * 推广分享设置
	 */
	PROMOTE_SHARE_SETTING("promoteShareSetting"),
	
	/**
	 * 签到设置
	 */
	SIGN_IN_SETTING("signInSetting"),
	
	/**
	 * 每月礼包积分
	 */
	GIFT_BAG("giftBag"),
	
	/**
	 * 邀请有礼
	 */
	INVITATION_SETTING("invitationSetting"),
	
	/**
	 * 积分兑换商品设置
	 */
	EXCHANGE_GOODS_SETTING("exchangeGoodsSetting");
	
	private JiuCoinSettingName(String stringValue) {
        this.stringValue = stringValue;
    }

    private String stringValue;

    public String getStringValue() {
        return stringValue;
    }
    
    public static JiuCoinSettingName getByStringValue(String stringValue) {
		for (JiuCoinSettingName jiuCoinSettingName : JiuCoinSettingName.values()) {
			if (StringUtils.equals(jiuCoinSettingName.getStringValue(), stringValue)) {
				return jiuCoinSettingName;
			}
		}
		return null;
	}
}
