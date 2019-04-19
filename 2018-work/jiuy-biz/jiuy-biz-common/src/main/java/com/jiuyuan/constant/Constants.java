package com.jiuyuan.constant;

import com.jiuyuan.util.constant.ConstantBinder;

public class Constants {

	static {
		ConstantBinder.bind(Constants.class, ConstantBinder.DEFAULT_CHARSET, "/constants.properties");
	}

	public static String SERVER_URL;

	public static String SERVER_URL_HTTPS;
	
	public static String QRCODE_PROXY_URL;
	
	public static String OFFICIAL_PROJECT_PATH;

	public static String ENV_RUNTIME;

	public static String MEMCACHED_SERVERS;

	public static String REDIS_SERVERS;

	/** 定向404 */
	public static final String ERROR_PAGE_NOT_FOUND = "forward:/static/page_not_found.html";

	/** 定向维护 */
	public static final String ERROR_MAINTENANCE = "forward:/static/maintenance.html";

	public static final String KEY_USER_DETAIL = "userDetail";

	public static final String KEY_STORE_USER_DETAIL = "storeUserDetail";

	public static final String KEY_USER_AGENT_PLATFORM = "userAgentPlatform";

	public static final String KEY_CLIENT_PLATFORM = "clientPlatform";

	// 小程序商家
	public static final String KEY_WXA_SHOP_DETAIL = "shopDetail";
	// 小程序会员
	public static final String KEY_WXA_MEMBER_DETAIL = "memberDetail";

}