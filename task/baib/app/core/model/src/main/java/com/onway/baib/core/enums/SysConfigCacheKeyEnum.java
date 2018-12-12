package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;

import com.onway.platform.common.enums.EnumBase;

/**
 * 系统参数key的枚举
 * 
 * @author guangdong.li
 * @version $Id: SysConfigCacheKeyEnum.java, v 0.1 2015年11月2日 下午3:32:16 liudehong Exp $
 */
public enum SysConfigCacheKeyEnum implements EnumBase {

    /******************************************YUDAO**********************************************/
    INIT_PASSWORD("INIT_PASSWORD", "初始密码"),

    MARRAY_CAR_MILEAGE_LIMIT("MARRAY_CAR_MILEAGE_LIMIT", "婚车限制公里数"),

    /******************************************YUDAO**********************************************/
    
    CARD_BIN_VERIFY_FLAG("CARD_BIN_VERIFY_FLAG", "是否卡bin校验开关"),

    APP_IOS_GUIDE_IMG("APP_IOS_GUIDE_IMG", "IOS引导页链接地址"),

    APP_ANDROID_GUIDE_IMG("APP_ANDROID_GUIDE_IMG", "安卓引导页链接地址"),

    ABOUT_INTRODUCE_INFO("ABOUT_INTRODUCE_INFO", "APP介绍信息"),

    ABOUT_QQ("ABOUT_QQ", "客服qq群"),

    ABOUT_EMAIL("ABOUT_EMAIL", "客服邮箱"),

    ABOUT_HOTLINE("ABOUT_HOTLINE", "客服热线"),

    ABOUT_SERVICE_TIME("ABOUT_SERVICE_TIME", "客服服务时间"),

    ABOUT_WEBSITE("ABOUT_WEBSITE", "网站"),

    ALIPAY_PAY_CALL_BACK_URL("ALIPAY_PAY_CALL_BACK_URL", "支付宝回调地址"),

    SHOW_RYT_PRODUCT("SHOW_RYT_PRODUCT", "是否展示RYT产品"),

    PROD_RECO_ACTIVITY_DESC_FOR_NEW("PROD_RECO_ACTIVITY_DESC_FOR_NEW", "新手标首页产品推荐活动文案"),

    PROD_RECO_ACT_DESC_COLOR_FOR_NEW("PROD_RECO_ACT_DESC_COLOR_FOR_NEW", "新手标首页产品推荐活动文案颜色"),

    PROD_RECO_ACTIVITY_DESC("PROD_RECO_ACTIVITY_DESC", "非新手标首页产品推荐活动文案"),

    PROD_RECO_ACT_DESC_COLOR("PROD_RECO_ACT_DESC_COLOR", "非新手标首页产品推荐活动文案颜色"),

    PROD_RECO_SECURITY_DESC("PROD_RECO_SECURITY_DESC", "首页产品推荐安全文案"),

    // 安卓客户端最新版本号
    VERSION_APP_ANDROID("VERSION_APP_ANDROID", "安卓客户端最新版本号"),

    APP_ANDROID_FORCE_UPDATE("APP_ANDROID_FORCE_UPDATE", "安卓强制更新标记"),
    // 安卓强制更新标记
    APP_ANDROID_SHOW_UPDATE("APP_ANDROID_SHOW_UPDATE", "安卓提示更新信息标记"),
    // 安卓下载地址
    APP_ANDROID_DOWNLOAD_URL("APP_ANDROID_DOWNLOAD_URL", "安卓下载地址"),

    APP_ANDROID_UPDATE_MSG("APP_ANDROID_UPDATE_MSG", "安卓更新提示信息"),

    VERSION_APP_IOS("VERSION_APP_IOS", "IOS客户端最新版本号"),

    APP_IOS_SHOW_UPDATE("APP_IOS_SHOW_UPDATE", "IOS提示更新信息标记"),

    APP_IOS_FORCE_UPDATE("APP_IOS_FORCE_UPDATE", "IOS强制更新标记"),

    APP_IOS_UPDATE_MSG("APP_IOS_UPDATE_MSG", "IOS更新提示信息"),

    // IOS下载地址
    APP_IOS_DOWNLOAD_URL("APP_IOS_DOWNLOAD_URL", "安卓下载地址"),

    NOTICE_INFO("NOTICE_INFO", "公告信息"),

    SIGN_ENTRANCE_URL("SIGN_ENTRANCE_URL", "签到服务地址信息"),

    /** 通用配置 */
    COMMON_LIMIT_DESC_URL("COMMON_LIMIT_DESC_URL", "通用交易限额表地址"),

    yudaoD_BIN_VERIFY_FLAG("yudaoD_BIN_VERIFY_FLAG", "是否卡bin校验开关"),

    // 银行同期收益
    BANK_CURRENT_PROFIT("BANK_CURRENT_PROFIT", "银行活期同期收益"),

    DEPOSIT_LIMIT("DEPOSIT_LIMIT", "充值限制最低金额"),

    USER_BANK_LIMIT_DESC("USER_BANK_LIMIT_DESC", "用户银行限额说明文案"),

    SYS_TYPE("SYS_TYPE", "环境：测试环境，生产环境"),

    APP_VJSON("APP_VJSON", "客户端串号"),

    SHOW_BUTTON_WITHDRAW("SHOW_BUTTON_WITHDRAW", "是否展示提现按钮"),

    SHOW_BUTTON_DEPOSIT("SHOW_BUTTON_DEPOSIT", "是否展示充值按钮"),

    SHOW_WITHDRAW_RECORD("SHOW_WITHDRAW_RECORD", "是否展示提现记录"),

    SHOW_DEPOSIT_RECORD("SHOW_DEPOSIT_RECORD", "是否展示充值记录"),

    WITHDRAW_AMOUNT_LIMIT("WITHDRAW_AMOUNT_LIMIT", "提现余额限制"),

    BALANCE_DEF_URL("BALANCE_DEF_URL", "什么是余额的URL"),

    DEPOSIT_URL("DEPOSIT_URL", "充值页面URL"),

    APP_IOS_APPSTORE_TEST("", "appstore测试账号"),

    GUIDE_IMAGE("", "guideImage首页图片"),

    PRE_NOTICE("", "preNotice首页图片"),

    PRE_NOTICE_NEW("", "新的preNotice首页图片"),

    // 默认显示的基金编号
    @Deprecated
    DEFAULT_FUND_NO("", "默认显示的主基金编号"),

    // 盈盈理财的好买商户ID
    YYLC_HW_CORP_NO("", "盈盈理财的好买合作代码"),

    FUND_DIVIDEND_TITLE("", "基金分红提醒"),

    /** 订单反馈 */
    FEEDBACK_PARSE_DATE("", "订单反馈文件解析时间"),

    /** 消息相关配置 */
    MSG_NEW_USER_AUTO("", "新用户消息生成"),

    // 申购完成表示 好买接受了
    MSG_PURCHASE_AUTO("", "申购完成消息生成"),

    // 赎回完成表示 好买接受了
    MSG_REDEEM_AUTO("", "赎回完成消息生成"),

    MSG_SMS_VERIFY_CONTENT("", "手机验证内容"),

    // 手机验证码模板名
    MSG_$0_TEMPLATE("", "手机验证码模板"),

    MSG_SMS_TRANS_CONTENT("", "短信转发内容"),

    MSG_USER_PROFIT("", "收益消息"),

    MIN_USER_PROFIT("", "收益消息阀值"),

    //
    MSG_SHARE_CONTENT("", "转发内容"),

    MSG_COMMON_CONTENT("", "通用转发内容"),

    TEMPLATE_URL("", "后端页面资源地址"),

    YYLC_PWD_SET_URL("", "盈盈理财密码设置页面URL"),

    YYLC_TRADE_PWD_URL("", "盈盈理财交易密码页面URL"),

    YYLC_GET_BACK_PAY_PWD_URL("", "盈盈理财找回交易密码页面URL"),

    IOS_APNS_SWITCH("", "IOS推送服务开关配置"),

    USER_PROFIT_SWITCH("", "用户收益统计开关"),

    TRADE_DETAIL_DAY_COUNT("", "获取交易明细历史的天数"),

    HW_BANK_LIMIT("", "银行卡购买限额"),

    MORE_RECOMMEND_INFO("", "更多栏位推荐信息"),

    APP_RECOMMEND_JSON("", "应用推荐列表"),

    ABOUT_YY_INTRODUCE_INFO("", "盈盈介绍信息"),

    //比如理财型老版本不支持,那么就按照版本屏蔽掉,配置格式(按平台的首字母顺序android,ios,syibam,wp7)：1.0.3.2|1.4.0
    SHOW_NEW_PRODUCT("", "是否要展示新产品"),

    BANK_PAY_LIMIT("", "银行支付限额"),

    PURCHASE_NOT_VERIFY_LIMIT("", "未鉴权单日限额"),

    SHOW_PRE_NOTICE("", "是否展示产品预告"),

    SHOW_RYT_PRODUCT_CODE("", "版本对应展示的融盈通产品编号，版本乡下兼容"),

    SHOW_RYT_FUND_RECOMMEND("", "是否在首页推荐展示稳盈货产品"),

    SHOW_FUND_INFO_ITEMS("", "是否在详情页上端展示信息项"),

    SHOW_FUND_PRE_BACK("", "是否支持显示提前还款"),

    SHOW_BANNER_RECO_LOAN("", "是否支持banner推荐借款"),

    SHOW_BANNER_RECO_LOAN_NEW("", "是否支持banner推荐借款"),

    SHOW_QUICK_PAY("", "是否支持快捷支付"),

    /** 从配置的版本以上就可显示差异化收益数据 */
    SHOW_DIFFER_PROFIT_DATA("", "是否展示差异化的收益数据"),

    MONITOR_EXCEPTION_COUNT("", "监控Exception出现的行数"),

    PHONE_LIST("", "接受监控报警消息手机号列表"),

    AUTH_ERROR_COUNT("", "鉴权错误次数"),

    SHOW_DAILY_LIMIT("", "是否进行当日累计检查"),

    FIRST_TRADE_LIMIT_RYT("", "融盈通首次申购额度限制"),

    REQUEST_PURCHASE_COUNT_RYT_LIMIT("", "融盈通并发数"),

    PURCHASE_QUEUE_RYT_SWITCH("", "融盈通队列开关"),

    PURCHASE_QUEUE_COUNT_RYT_LIMIT("", "融盈通队列最大长度"),

    ACC_DEFAULT_PAY_CHANNEL("", "账户中心默认支付渠道"),

    GOOGLE_SPEECH_REQUEST_URL("", "谷歌语音系统请求URL"),

    GOOGLE_SPEECH_REQUEST_KEY("", "谷歌语音系统请求KEY"),

    P2C_PROFIT_CALC_DESC_URL("", "P2C收益计算描述链接地址"),

    DEFAULT_FUND_TYPE("", "默认展示的产品类型"),

    USER_SHARE_PAGE_TITLE("", "用户分享页面标题"),

    USER_SHARE_PAGE_URL("", "用户分享页面地址"),

    ACCOUNT_INDEX_HOLD_TIP("", "我的盈盈持有资产TIP"),

    ACCOUNT_INDEX_BALANCE_TIP("", "我的盈盈账户余额TIP"),

    NOTIFY_TIME_PERIOD("", "用户通知时间段"),

    REPAYMENT_NOTIFY_TITLE("", "用户回款通知标题"),

    REPAYMENT_NOTIFY_CONTENT("", "用户回款通知内容"),

    SUPPORT_QUICK_PAY_BANKCODE("", "支持快捷支付的银行"),

    NOT_SUPPORT_QUICK_AUTH_BANKCODE("", "不支持快捷认证的银行"),

    USER_AUTH_LIST_TITLE("", "用户鉴权列表抬头"),

    QUICKPAY_AUTH_SEC_TIP("", "快捷认证安全提示文案"),

    QUICKPAY_AUTH_RESERVE_TIP("", "快捷认证预留提示文案"),

    QUICKPAY_LOCK_TIP("", "快捷银行卡锁定提示文案"),

    QUICKPAY_AUTH_AMOUNT("", "快捷认证支付金额"),

    QUICKPAY_CELL_TIP("", "快捷预留手机号提示文案"),

    QUICKPAY_WITHOUT_CELL_TIP("", "快捷支付无银行预留手机号文案"),

    QUICKPAY_WITHOUT_CELL_POPUP_TIP("", "快捷支付无银行预留手机号点击弹出文案"),

    QUICKPAY_NOT_BIND_LIMIT("", "快捷支付未绑卡限额"),

    ACCOUNT_DEFAULT_FUNDCODE("", "账户中心默认产品编号"),

    yudaoD_QUICK_VERIFY_FLAG("", "绑卡后无验证码快捷认证开关"),

    /** 用户最大地址数*/
    MAX_ADDRESS_AMOUNT("", "用户最大地址数"),

    APP_INVITE_URL("", "我的盈盈邀请好友入口"),

    APP_ACTIVITY_BEGIN_TIME("", "营销活动开始时间点"),

    APP_ACTIVITY_END_TIME("", "营销活动结束时间点"),

    APP_ACTIVITY_DESC("", "营销活动文案"),

    UNSETTLEMENT_PROFIT_TIPS("UNSETTLEMENT_PROFIT_TIPS", "未结算收益提示文案"),

    //-----------------------支付相关
    
    SUBSCRIPTIONRATE("SUBSCRIPTIONRATE", "订金占总租金的比率"),
    
    DEPOSITRATE("DEPOSITRATE", "押金占总租金的比率"),
    
    WE_PAY_APP_ID("WE_PAY_APP_ID", "微信公众号支付appid"),

    WE_PAY_PARTNER_ID("WE_PAY_PARTNER_ID", "微信公众号支付Partner ID"),

    WE_PAY_APP_SECRET_ID("WE_PAY_APP_SECRET_ID", "微信公众号支付APP Secret"),

    WE_PAY_MCH_ID("WE_PAY_MCH_ID", "微信支付MCH_ID"),

    WE_APP_PAY_APP_ID("WE_APP_PAY_APP_ID", "微信APP支付appid"),

    WE_APP_PAY_PAY_MCH_ID("WE_APP_PAY_PAY_MCH_ID", "微信APP支付MCH_ID"),

    WE_APP_PAY_APP_SECRET_ID("WE_APP_PAY_APP_SECRET_ID", "微信APP支付APP Secret"),

    WE_APP_PAY_PARTNER_ID("WE_APP_PAY_PARTNER_ID", "微信APP支付Partner ID"),

    ALIPAY_PARTNER("ALIPAY_PARTNER", "支付宝支付partner"),

    ALIPAY_SELLER("ALIPAY_SELLER", "支付宝支付seller"),

    ALIPAY_PRIVATE_KEY("ALIPAY_PRIVATE_KEY", "支付宝支付privateKey"),

    WECHAT_PAY_CALL_BACK_URL("WECHAT_PAY_CALL_BACK_URL", "微信支付回调地址"),

    WECHAT_PAY_CALL_AUTH_URL("WECHAT_PAY_CALL_AUTH_URL", "微信支付授权地址"),

    //--------------------协议---------------------------------
    PROTOCOL_REGISTER("PROTOCOL_REGISTER", "注册协议"),

    PROTOCOL_PAY("PROTOCOL_PAY", "注册协议"),

    NEW_VERSION("NEW_VERSION", "版本号"),

    ONLINERESCUE("ONLINERESCUE", "在线救援"),

    ENTERPRISE_NEW_VERSION("ENTERPRISE_NEW_VERSION", "企业端app版本号")

    ;

    /** 枚举码*/
    private String code;

    /** 描述信息*/
    private String message;

    private SysConfigCacheKeyEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /** 
     * @see com.onway.platform.common.enums.EnumBase#message()
     */
    @Override
    public String message() {
        return message;
    }

    /** 
     * @see com.onway.platform.common.enums.EnumBase#value()
     */
    @Override
    public Number value() {
        return null;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static SysConfigCacheKeyEnum getByCode(String code) {

        for (SysConfigCacheKeyEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }

        return null;
    }
}
