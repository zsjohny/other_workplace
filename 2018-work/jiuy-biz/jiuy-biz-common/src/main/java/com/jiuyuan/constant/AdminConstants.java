package com.jiuyuan.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.util.constant.ConstantBinder;

public class AdminConstants {

    static {
        ConstantBinder.bind(AdminConstants.class, ConstantBinder.DEFAULT_CHARSET, "/constants.properties");
    }

    /** 定向404 */
    public static final String ERROR_PAGE_NOT_FOUND = "forward:/static/page_not_found.html";

    /** 定向维护 */
    public static final String ERROR_MAINTENANCE = "forward:/static/maintenance.html";

    public static final String KEY_USER_DETAIL = "userDetail";

    public static final String KEY_USER_AGENT_PLATFORM = "userAgentPlatform";

    public static final String KEY_CLIENT_PLATFORM = "clientPlatform";
    
    /** 微信服务url*/
    public static String WEIXIN_SERVER_URL;
    
    /** 小程序服务地址url*/
    public static String WXA_SERVER_URL;

    /* 旺店通配置 */
    public static String WDT_BASE_URL;

    public static String WDT_SID;
    
    public static String WDT_APPSECRET;

    public static String WDT_APPKEY;
    
    /* APP工程根路径 */
    public static String APP_ROOT_PATH;
    
    public static final String WDT_TRADE_PUSH_URL = "/openapi2/trade_push.php";
    
    public static final String WDT_STOCK_QUERY_URL = "/openapi2/stock_query.php";
    
    public static final int WDT_STOCK_QUERY_PAGE_SIZE = 40;
    
    public static final String WDT_GOODS_PUSH_URL  = "/openapi2/goods_push.php";
    
    public static final String WDT_LOGISTICS_SYN_QUERY_URL = "/openapi2/logistics_sync_query.php";
    
    public static final String  WDT_LOGISTICS_SYN_ACK_URL = "/openapi2/logistics_sync_ack.php";
    
    public static final List<Long> ERP_WAREHOUSE_ID_LIST = Arrays.asList(new Long[]{1L, 16L});
    
    public static final List<Long> PUSH_TO_ERP_WAREHOUSE_ID_LIST = Arrays.asList(new Long[]{1L, 14L, 16L, 17L});
    
    public static final List<Long> OWN_WAREHOUSE_ID_LIST = Arrays.asList(new Long[]{14L, 17L});
    
    public static final List<Long> OTHER_WAREHOUSE_ID_LIST = Arrays.asList(new Long[]{2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 15L, 18L, 19L, 20L, 21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L, 31L, 32L, 33L, 34L, 35L, 36L, 37L});
    
    public static final List<String> EXCLUDE_TEMPLATE_NAMES = Arrays.asList("模板1", "模板2", "模板3", "模板4", "模板5", "模板6", "模板7", "模板8", "模板9", "模板10", "模板11", "模板12");

    /* 取消订单的标准*/
    public static final List<OrderStatus> CANCEL_SITUATION = 
    		new ArrayList<OrderStatus>() {
				private static final long serialVersionUID = -371295768394462225L;
				{	
					add(OrderStatus.REFUNDED); 
				}
			}; 
			
	public static String FINANCE_NAME;
}