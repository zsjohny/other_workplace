package com.e_commerce.miscroservice.store.utils;

import org.apache.commons.lang3.StringUtils;

public enum GlobalSettingName  {
	// 客户端底部导航栏配置
	NAV_BAR("navigationBar"),

	// 客户端首页模块
	INDEX_CATEGORY_MODULE("hotCategory"),

	// 推广季节
	PROMOTION_SEASON("promotionSeason"),

	// 启动广告页
	START_PAGE_AD("startAdPage"),

	// 版本更新
	VERSION_UPDATE("versionUpdate"),

	// 门店版本更新
	STORE_VERSION_UPDATE("storeversionUpdate"),

	// 购物车广告
	SHOPPINGCARTBANNER("shoppingCartBanner"),

	// 订单设置
	ORDER_SETTING("orderSetting"),

	// 订单自动确认时间
	ORDER_AUTO_CONFIRM_TIME("orderAutoConfirm"),

	// 0元设置
	ZERO_LIMIT("zeroLimit"),

	// 首单优惠
	FIRST_DISCOUNT("firstDiscount"),

	// 全场满减
	ALL_DISCOUNT("allDiscount"),

	// 分享广告标题
	AD_TITLES("adTitles"),

	// 商品分享标题+ URL
	PRODUCT_SHARE("productShare"),

	// 月度0元购限制
	ZERO_LIMIT_MONTHLY("zeroLimitMonthly"),

	/**
	 * 售后换货，用户自动确认收货时间(单位：分)
	 */
	AFTER_SALE_AUTO_CONFIRM("afterSaleAutoConfirm"),

	/**
	 * 优惠券设置
	 */
	STORE_COUPON("store_coupon"),

	/**
	 * 优惠券设置
	 */
	COUPON("coupon"),

	/**
	 * 邀请有礼设置
	 */
	INVITE_GIFT_SETTING("inviteGift_setting"),

	/**
	 * 邀请有礼1
	 */
	INVITE_GIFT_1("inviteGift_1"),

	/**
	 * 邀请有礼2
	 */
	INVITE_GIFT_2("inviteGift_2"),

	/**
	 * 邀请有礼3
	 */
	INVITE_GIFT_3("inviteGift_3"),

	/**
	 * 礼包内容
	 */
	GIFT_CONTENT("giftContent"),

	/**
	 * 水印
	 */
	WATERMARK("watermark"),

	/**
	 * 商品橱窗库存提醒设置
	 */
	REMAIN_COUNT_TIPS("remainCountTips"),

	/**
	 * 代金券领取中心
	 */
	FETCH_COUPON("fetchCoupon"),

	/**
	 * 社区轮播图
	 */
	COMMUNITY_CAROUSEL("communityCarousel"),

	/**
	 * 社区导航设置
	 */
	COMMUNITY_NAVIGATION("communityNavigation"),

	/**
	 * 搜索提示
	 */
	SEARCH_TIP("searchTip"),

	/**
	 * 搜索-广告设置
	 */
	SEARCH_AD("searchAd"),

	/**
	 * 搜索-推荐商品设置
	 */
	SEARCH_RECOMMENDED_PRODUCT("searchRecommendedProduct"),

	/**
	 * 商品推荐设置
	 */
	RECOMMENDED_PRODUCT("recommendedProduct"),

	/**
	 * 搜索-热搜关键词
	 */
	SEARCH_HOT_KEY_WORDS("searchHotKeyWords"),

	/**
	 * 搜索-权重配置
	 */
	SEARCH_WEIGHT_SETTING("searchWeightSetting"),

	/**
	 * 品类界面初始设置
	 */
	CATEGORY_FIRST_NAVIGATION("category_first_navigation"),

	/**
	 * 邀请有奖分享到微信,friends_circle：朋友圈，friend朋友
	 */
	INVITEGIFT_SHARE_WEIXIN("inviteGift_share_weixin"),

	/**
	 * 新注册用户赠送优惠券设置
	 */
	REGISTER_COUPONS("register_coupons"),

	/**
	 * 推广季节(最新的,之前的promotionSeason废弃)
	 */
	PROMOTION_SEASON_NEW("promotion_season"),

	/**
	 * 千米token
	 */
	QIANMI_TOKEN("qianmi_token"),

	/**
	 * 抽奖转盘
	 */
	DRAW_LOTTERY("draw_lottery"),

	/**
	 * 优惠券限用设置
	 */
	COUPON_LIMIT_SET("coupon_limit_set"),

	/**
	 * 指定优惠券限用设置
	 */
	SPECIFY_COUPON_LIMIT_SET("specify_coupon_limit_set"),

	/**
	 * 统计设置
	 */
	STATISTICAL_SETTING("statistical_setting"),

	/**
	 * 积分设置
	 */
	JIUCOIN_DEDUCTION_SETTING("jiucoin_deduction_setting"),

	/**
	 * 优惠券限用设置
	 */
	STORE_SETTING("store_setting"),

	/**
	 * 分享有礼设置
	 */
	SHARE_GIFT_SETTING("share_gift_setting"),

	/**
	 * 玖币设置
	 */
	JIUCOIN_GLOBAL_SETTING("jiucoin_global_setting"),

	/**
	 * 首单优惠设置
	 */
	FIRST_DISCOUNT_SETTING("first_discount_setting"),

	/**
	 * 门店优惠券发放设置
	 */
	STORE_COUPON_SEND_SETTING("store_coupon_send_setting"),

	/**
	 * 赠送优惠券模版一
	 */
	STORE_COUPON_SEND_1_SETTING("store_coupon_send_1"),

	/**
	 * 门店赠送优惠券 邀请会员绑定
	 */
	STORE_COUPON_SEND_2_SETTING("store_coupon_send_2"),

	/**
	 * 俞姐姐会员版优惠券发放设置
	 */
	YJJ_COUPON_SEND_SETTING("yjj_coupon_send_setting"),

	/**
	 * 俞姐姐会员版 优惠券发放设置一 新注册用户
	 */
	YJJ_COUPON_SEND_1_SETTING("yjj_coupon_send_1"),

	/**
	 * 俞姐姐会员版 优惠券发放设置二 关注门店
	 */
	YJJ_COUPON_SEND_2_SETTING("yjj_coupon_send_2"),

	// 客户端底部导航栏配置
	STORE_NAV_BAR("storenavigationBar"),
	// 门店优惠券限用设置
	STORE_COUPON_LIMIT_SET("store_coupon_limit_set"),
	// 店家自有商品数量和已上传且状态为在售的平台商品数量的比例
	OWN_PLATFORM_PRODUCT_RATIO("own_platform_product_ratio"),

	// 平台客服电话
	PLATFORM_CUSTOMER_PHONENUMBER("platform_customer_phonenumber"),

	/**
	 * 门店审核发送通知
	 */
	STORE_AUDIT_SEND("store_audit_send"),

	// 敏感词（英文逗号分隔）
	SENSITIVE_WORD("sensitive_word"),
	
	/**
	 * 新用户首次登录时发送优惠券图片
	 */
	NEW_USER_COUPON("new_user_coupon"),
	
	/**
	 * 门店注册认证风格
	 */
	STORE_STYLE("store_style"),
	/**
	 * 门店注册店铺面积
	 */
	STORE_AREA_SCOPE("store_area_scope"),
	/**
	 * 门店注册年龄范围
	 */
	STORE_AGE("store_age"),
	/**
	 * 限购活动标题
	 */
	RESTRICTION_ACTIVITY_TITLE("restriction_activity_title");


	private GlobalSettingName(String stringValue) {
		this.stringValue = stringValue;
	}

	private String stringValue;



}