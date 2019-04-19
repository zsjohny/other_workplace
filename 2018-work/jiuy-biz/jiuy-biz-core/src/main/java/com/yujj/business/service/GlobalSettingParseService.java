package com.yujj.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.yujj.business.service.globalsetting.bean.AdTitles;
import com.yujj.business.service.globalsetting.bean.AllDiscount;
import com.yujj.business.service.globalsetting.bean.CategoryFirstNavigation;
import com.yujj.business.service.globalsetting.bean.CommunityCarousel;
import com.yujj.business.service.globalsetting.bean.CommunityNavigation;
import com.yujj.business.service.globalsetting.bean.ContinuousSetting;
import com.yujj.business.service.globalsetting.bean.CouponLimitSet;
import com.yujj.business.service.globalsetting.bean.CouponSetting;
import com.yujj.business.service.globalsetting.bean.DrawLottery;
import com.yujj.business.service.globalsetting.bean.ExchangeGoodsSetting;
import com.yujj.business.service.globalsetting.bean.FetchCoupon;
import com.yujj.business.service.globalsetting.bean.FirstDiscount;
import com.yujj.business.service.globalsetting.bean.GiftContent;
import com.yujj.business.service.globalsetting.bean.HotCategory;
import com.yujj.business.service.globalsetting.bean.InvitationSetting;
import com.yujj.business.service.globalsetting.bean.InviteGift1;
import com.yujj.business.service.globalsetting.bean.InviteGift2;
import com.yujj.business.service.globalsetting.bean.InviteGift3;
import com.yujj.business.service.globalsetting.bean.InviteGiftSetting;
import com.yujj.business.service.globalsetting.bean.InviteGiftShareWeixin;
import com.yujj.business.service.globalsetting.bean.JiuCoinAttribute;
import com.yujj.business.service.globalsetting.bean.JiucoinGlobalSetting;
import com.yujj.business.service.globalsetting.bean.NavigationBar;
import com.yujj.business.service.globalsetting.bean.OrderAutoConfirm;
import com.yujj.business.service.globalsetting.bean.OrderSetting;
import com.yujj.business.service.globalsetting.bean.ProductShare;
import com.yujj.business.service.globalsetting.bean.PromoteShareSetting;
import com.yujj.business.service.globalsetting.bean.PromotionSeason;
import com.yujj.business.service.globalsetting.bean.RecommendedProduct;
import com.yujj.business.service.globalsetting.bean.RegisterCoupons;
import com.yujj.business.service.globalsetting.bean.RemainCountTips;
import com.yujj.business.service.globalsetting.bean.SearchAd;
import com.yujj.business.service.globalsetting.bean.SearchHotKeyWords;
import com.yujj.business.service.globalsetting.bean.SearchRecommendedProduct;
import com.yujj.business.service.globalsetting.bean.SearchTip;
import com.yujj.business.service.globalsetting.bean.SearchWeightSetting;
import com.yujj.business.service.globalsetting.bean.SevenLimitValue;
import com.yujj.business.service.globalsetting.bean.ShoppingCartBanner;
import com.yujj.business.service.globalsetting.bean.ShoppingJiuCoinSetting;
import com.yujj.business.service.globalsetting.bean.SignInSetting;
import com.yujj.business.service.globalsetting.bean.SpecifyCouponLimitSet;
import com.yujj.business.service.globalsetting.bean.StartAdPage;
import com.yujj.business.service.globalsetting.bean.StoreScanReward;
import com.yujj.business.service.globalsetting.bean.StoreversionUpdate;
import com.yujj.business.service.globalsetting.bean.VersionUpdate;
import com.yujj.business.service.globalsetting.bean.Watermark;
import com.yujj.business.service.globalsetting.bean.ZeroLimit;
import com.yujj.business.service.globalsetting.bean.ZeroLimitMonthly;

@Service
public class GlobalSettingParseService {
	
	
    @Autowired
	 private GlobalSettingService globalSettingService;

    // 客户端底部导航栏配置
    public NavigationBar getNavigationBar() {
		String jsonStr = getJsonStr(GlobalSettingName.NAV_BAR);
		return null;
	}
    // 客户端首页模块
    public HotCategory getHotCategory() {
		String jsonStr = getJsonStr(GlobalSettingName.INDEX_CATEGORY_MODULE);
		return null;
	}
    // 推广季节
    public PromotionSeason getPromotionSeason() {
		String jsonStr = getJsonStr(GlobalSettingName.PROMOTION_SEASON);
		return null;
	}
	// 启动广告页
    public StartAdPage getStartAdPage() {
		String jsonStr = getJsonStr(GlobalSettingName.START_PAGE_AD);
		return null;
	}
	// 版本更新
    public VersionUpdate getVersionUpdate() {
		String jsonStr = getJsonStr(GlobalSettingName.VERSION_UPDATE);
		return null;
	}
	//  门店版本更新
    public StoreversionUpdate getStoreversionUpdate() {
		String jsonStr = getJsonStr(GlobalSettingName.STORE_VERSION_UPDATE);
		// 带解析实现
		return null;
	}
	//购物车广告
	public ShoppingCartBanner getShoppingCartBanner() {
		String jsonStr = getJsonStr(GlobalSettingName.SHOPPINGCARTBANNER);
		return null;
	}
	//订单设置
	public OrderSetting getOrderSetting() {
		String jsonStr = getJsonStr(GlobalSettingName.ORDER_SETTING);
		//带解析实现
		return null;
	}
	//订单自动确认时间
	public OrderAutoConfirm getOrderAutoConfirm() {
		String jsonStr = getJsonStr(GlobalSettingName.ORDER_AUTO_CONFIRM_TIME);
		return null;
	}
	//0元设置
	public ZeroLimit getZeroLimit() {
		String jsonStr = getJsonStr(GlobalSettingName.ZERO_LIMIT);
		// 带解析实现
		return null;
	}
	//首单优惠
	public FirstDiscount getFirstDiscount() {
		String jsonStr = getJsonStr(GlobalSettingName.FIRST_DISCOUNT);
		return null;
	}
	//全场满减
	public AllDiscount getAllDiscount() {
		String jsonStr = getJsonStr(GlobalSettingName.ALL_DISCOUNT);
		return null;
	}
	//分享广告标题
	public AdTitles getAdTitles() {
		String jsonStr = getJsonStr(GlobalSettingName.AD_TITLES);
		return null;
	}
	//商品分享标题+ URL
	public ProductShare getProductShare() {
		String jsonStr = getJsonStr(GlobalSettingName.PRODUCT_SHARE);
		// 带解析实现
		return null;
	}
	//月度0元购限制
	public ZeroLimitMonthly getZeroLimitMonthly() {
		String jsonStr = getJsonStr(GlobalSettingName.ZERO_LIMIT_MONTHLY);
		return null;
	}
	
	/**
	 * 优惠券设置
	 */
	public CouponSetting getCouponSetting() {
		String jsonStr = getJsonStr(GlobalSettingName.COUPON);
		return null;
	}
    /**
     * 邀请有礼设置
     */
	public InviteGiftSetting getInviteGiftSetting() {
		String jsonStr = getJsonStr(GlobalSettingName.INVITE_GIFT_SETTING);
		return null;
	}
	/**
     * 邀请有礼1
     */
	public InviteGift1 getInviteGift1() {
		String jsonStr = getJsonStr(GlobalSettingName.INVITE_GIFT_1);
		return null;
	}
    /**
     * 邀请有礼2
     */
	public InviteGift2 getInviteGift2() {
		String jsonStr = getJsonStr(GlobalSettingName.INVITE_GIFT_2);
		return null;
	}
    /**
     * 邀请有礼3
     */
	public InviteGift3 getInviteGift3() {
		String jsonStr = getJsonStr(GlobalSettingName.INVITE_GIFT_3);
		return null;
	}
	/**
	 * 礼包内容
	 */
	public GiftContent getGiftContent() {
		String jsonStr = getJsonStr(GlobalSettingName.GIFT_CONTENT);
		return null;
	}
	/**
	 * 水印
	 */
	public Watermark getWatermark() {
		String jsonStr = getJsonStr(GlobalSettingName.WATERMARK);
		return null;
	}
	/**
	 * 商品橱窗库存提醒设置
	 */
	public RemainCountTips getRemainCountTips() {
		String jsonStr = getJsonStr(GlobalSettingName.REMAIN_COUNT_TIPS);
		return null;
	}
	/**
	 * 代金券领取中心
	 */
	public FetchCoupon getFetchCoupon() {
		String jsonStr = getJsonStr(GlobalSettingName.FETCH_COUPON);
		return null;
	}
	/**
	 * 社区轮播图
	 */
	public CommunityCarousel getCommunityCarousel() {
		String jsonStr = getJsonStr(GlobalSettingName.COMMUNITY_CAROUSEL);
		return null;
	}
	/**
	 * 社区导航设置
	 */
	public CommunityNavigation getCommunityNavigation() {
		String jsonStr = getJsonStr(GlobalSettingName.COMMUNITY_NAVIGATION);
		return null;
	}
	/**
	 * 搜索提示
	 */
	public SearchTip getSearchTip() {
		String jsonStr = getJsonStr(GlobalSettingName.SEARCH_TIP);
		return null;
	}
	/**
	 * 搜索-广告设置
	 */
	public SearchAd getSearchAd() {
		String jsonStr = getJsonStr(GlobalSettingName.SEARCH_AD);
		return null;
	}
	/**
	 * 搜索-推荐商品设置
	 */
	public SearchRecommendedProduct getSearchRecommendedProduct() {
		String jsonStr = getJsonStr(GlobalSettingName.SEARCH_RECOMMENDED_PRODUCT);
		return null;
	}
	/**
	 * 商品推荐设置
	 */
	public RecommendedProduct getRecommendedProduct() {
		String jsonStr = getJsonStr(GlobalSettingName.RECOMMENDED_PRODUCT);
		return null;
	}
	/**
	 * 搜索-热搜关键词
	 */
	public SearchHotKeyWords getSearchHotKeyWords() {
		String jsonStr = getJsonStr(GlobalSettingName.SEARCH_HOT_KEY_WORDS);
		return null;
	}
	/**
	 * 搜索-权重配置
	 */
	public SearchWeightSetting getSearchWeightSetting() {
		String jsonStr = getJsonStr(GlobalSettingName.SEARCH_WEIGHT_SETTING);
		return null;
	}
	/**
	 * 品类界面初始设置
	 */
	public CategoryFirstNavigation getCategoryFirstNavigation() {
		String jsonStr = getJsonStr(GlobalSettingName.CATEGORY_FIRST_NAVIGATION);
		return null;
	}
	/**
	 * 邀请有奖分享到微信,friends_circle：朋友圈，friend朋友
	 */
	public InviteGiftShareWeixin getInviteGiftShareWeixin() {
		String jsonStr = getJsonStr(GlobalSettingName.INVITEGIFT_SHARE_WEIXIN);
		return null;
	}

	/**
	 * 新注册用户赠送优惠券设置
	 */
	public RegisterCoupons getRegisterCoupons() {
		String jsonStr = getJsonStr(GlobalSettingName.REGISTER_COUPONS);
		return null;
	}
	/**
	 * 抽奖转盘
	 */
	public DrawLottery getDrawLottery() {
		String jsonStr = getJsonStr(GlobalSettingName.DRAW_LOTTERY);
		return null;
	}
	/**
	 * 优惠券限用设置
	 */
	public CouponLimitSet getCouponLimitSet() {
		String jsonStr = getJsonStr(GlobalSettingName.COUPON_LIMIT_SET);
		return null;
	}
	/**
	 * 指定优惠券限用设置
	 */
	public SpecifyCouponLimitSet getSpecifyCouponLimitSet() {
		String jsonStr = getJsonStr(GlobalSettingName.SPECIFY_COUPON_LIMIT_SET);
		return null;
	}
	/**
	 * 获取积分玖币如何赠送规则等相关设置
	 * @return
	 */
	public JiucoinGlobalSetting getJiucoin_global_setting() {
		String jsonStr = getJsonStr(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
		JiucoinGlobalSetting jiucoinGlobalSetting = new JiucoinGlobalSetting();
		//TODO json解析成对象
		JSONObject object = JSON.parseObject(jsonStr);
		
		//解析ExchangeGoodsSetting对象
		JSONObject exchangeGoodsSettingObj = object.getJSONObject("exchangeGoodsSetting");
		ExchangeGoodsSetting exchangeGoodsSetting = new ExchangeGoodsSetting();
		exchangeGoodsSetting.setCycle(exchangeGoodsSettingObj.getString("cycle"));
		exchangeGoodsSetting.setMaxCount(exchangeGoodsSettingObj.getString("maxCount"));
		jiucoinGlobalSetting.setExchangeGoodsSetting(exchangeGoodsSetting);
		
		//解析giftBag
		String giftBag = object.getString("giftBag");
		jiucoinGlobalSetting.setGiftBag(giftBag);
		
		//解析InvitationSetting对象
		JSONObject invitationSettingObj = object.getJSONObject("invitationSetting");
		InvitationSetting invitationSetting = new InvitationSetting();
		invitationSetting.setEachObtain(invitationSettingObj.getString("eachObtain"));
		invitationSetting.setInvitationCycle(invitationSettingObj.getString("invitationCycle"));
		invitationSetting.setMaxCountCycle(invitationSettingObj.getString("maxCountCycle"));
		invitationSetting.setMaxJiuCoinReturnCycle(invitationSettingObj.getString("maxJiuCoinReturnCycle"));
		invitationSetting.setReturnCycle(invitationSettingObj.getString("returnCycle"));
		invitationSetting.setReturnPercentage(invitationSettingObj.getString("returnPercentage"));
		jiucoinGlobalSetting.setInvitationSetting(invitationSetting);
		
		//解析JiuCoinAttribute对象
		JSONObject jiuCoinAttributeObj = object.getJSONObject("jiuCoinAttribute");
		JiuCoinAttribute jiuCoinAttribute = new JiuCoinAttribute();
		jiuCoinAttribute.setNextExpirationTime(jiuCoinAttributeObj.getString("nextExpirationTime"));
		jiuCoinAttribute.setWorthRmb(jiuCoinAttributeObj.getString("worthRmb"));
		jiucoinGlobalSetting.setJiuCoinAttribute(jiuCoinAttribute);
		
		//解析PromoteShareSetting对象
		JSONObject promoteShareSettingObj = object.getJSONObject("promoteShareSetting");
		PromoteShareSetting promoteShareSetting = new PromoteShareSetting();
		promoteShareSetting.setClickCycle(promoteShareSettingObj.getString("clickCycle"));
		promoteShareSetting.setEachClickObtain(promoteShareSettingObj.getString("eachClickObtain"));
		promoteShareSetting.setEachShareObtain(promoteShareSettingObj.getString("eachShareObtain"));
		promoteShareSetting.setMaxCountEachClickCycle(promoteShareSettingObj.getString("maxCountEachClickCycle"));
		promoteShareSetting.setMaxCountEachShareCycle(promoteShareSettingObj.getString("maxCountEachShareCycle"));
		promoteShareSetting.setShareCycle(promoteShareSettingObj.getString("shareCycle"));
		jiucoinGlobalSetting.setPromoteShareSetting(promoteShareSetting);
		
		//解析RegisterJiuCoinSetting对象
		String registerJiuCoinSetting = object.getString("registerJiuCoinSetting");
		jiucoinGlobalSetting.setRegisterJiuCoinSetting(registerJiuCoinSetting);
		
		//解析SevenLimitValue对象
		ArrayList<SevenLimitValue> sevenLimitValueList = new ArrayList<SevenLimitValue>();
		JSONArray sevenLimitValueArray = object.getJSONArray("sevenLimitValue");
		for (Object object2 : sevenLimitValueArray) {
			SevenLimitValue sevenLimitValue = new SevenLimitValue();
			JSONObject sevenLimitValueObj = (JSONObject)object2;
			sevenLimitValue.setGiftBag(sevenLimitValueObj.getString("giftBag"));
			sevenLimitValue.setInvitationJiuCoin(sevenLimitValueObj.getString("invitationJiuCoin"));
			sevenLimitValue.setPromoteShareJiuCoin(sevenLimitValueObj.getString("promoteShareJiuCoin"));
			sevenLimitValue.setRegisterJiuCoin(sevenLimitValueObj.getString("registerJiuCoin"));
			sevenLimitValue.setScanJiuCoin(sevenLimitValueObj.getString("scanJiuCoin"));
			sevenLimitValue.setSignInJiuCoin(sevenLimitValueObj.getString("signInJiuCoin"));
			sevenLimitValue.setSubTotal(sevenLimitValueObj.getString("subTotal"));
			sevenLimitValueList.add(sevenLimitValue);
		}
		jiucoinGlobalSetting.setSevenLimitValue(sevenLimitValueList);
		
		//解析ShoppingJiuCoinSetting对象
		JSONObject shoppingJiuCoinSettingObj = object.getJSONObject("shoppingJiuCoinSetting");
		ShoppingJiuCoinSetting shoppingJiuCoinSetting = new ShoppingJiuCoinSetting();
		shoppingJiuCoinSetting.setEvaluationPercentage(shoppingJiuCoinSettingObj.getString("evaluationPercentage"));
		shoppingJiuCoinSetting.setReturnPercentage(shoppingJiuCoinSettingObj.getString("returnPercentage"));
		jiucoinGlobalSetting.setShoppingJiuCoinSetting(shoppingJiuCoinSetting);
		
		//解析SignInSetting对象
		JSONObject signInSettingObj = object.getJSONObject("signInSetting");
		SignInSetting signInSetting = new SignInSetting();
		JSONArray continuousSettingArray = signInSettingObj.getJSONArray("continuousSetting");
		List<ContinuousSetting> continuousSettingList = new ArrayList<ContinuousSetting>();
		for (Object object2 : continuousSettingArray) {
			ContinuousSetting continuousSetting = new ContinuousSetting();
			JSONObject continuousSettingObj = (JSONObject)object2;
			continuousSetting.setDays(continuousSettingObj.getString("days"));
			continuousSetting.setExtraJiuCoin(continuousSettingObj.getString("extraJiuCoin"));
			continuousSettingList.add(continuousSetting);
		}
		signInSetting.setContinuousSetting(continuousSettingList);
		signInSetting.setDailyObtain(signInSettingObj.getString("dailyObtain"));
		jiucoinGlobalSetting.setSignInSetting(signInSetting);
		
		//解析StoreScanReward对象
		JSONObject storeScanRewardObj = object.getJSONObject("storeScanReward");
		StoreScanReward storeScanReward = new StoreScanReward();
		storeScanReward.setEachObtain(storeScanRewardObj.getString("eachObtain"));
		storeScanReward.setMaxCountEachCycle(storeScanRewardObj.getString("maxCountEachCycle"));
		storeScanReward.setMaxScanCount(storeScanRewardObj.getString("maxScanCount"));
		storeScanReward.setObtainCycle(storeScanRewardObj.getString("obtainCycle"));
		jiucoinGlobalSetting.setStoreScanReward(storeScanReward);
		
		return jiucoinGlobalSetting;
	}


	
	
	

	private String getJsonStr(GlobalSettingName jiucoinGloableSetting) {
		return globalSettingService.getSetting(jiucoinGloableSetting);
	}





	


	
	

}
