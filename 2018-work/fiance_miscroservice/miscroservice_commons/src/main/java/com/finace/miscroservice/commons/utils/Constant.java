package com.finace.miscroservice.commons.utils;

public class Constant {

    /**
     * 活动开始时间
     */
    public final static String ACTIVITY_START_TIME = "2018-01-28 00:00:00";

    /**
     *活动结束时间
     */
    public final static String ACTIVITY_END_TIME = "2018-02-25 23:59:59";


    /**
     * 普通理财用户
     */
    public final static int WEB_USER_TYPEID_L = 2;

    /**
     *普通借款用户
     */
    public final static int WEB_USER_TYPEID_J = 3;

    /**
     * 用户验证码有效时长
     */
    public final static int USER_VER_CODE_TIME = 300;

    /**
     *token失效
     */
    public final static int ECODE_506 = 506;

    /**
     * 点单登录
     */
    public final static int ECODE_507 = 507;

    /**
     * 今日已签
     */
    public final static int ECODE_601 = 601;

    /**
     * 用户推送消息别名在redis中的key
     */
    public final static String PUSH_ID = "pushId";

    /**
     * 获取标的的可购金额redis中的key
     */
    public final static String PURCHASE_AMT = "purchaseAmt";

    /**
     * 获取标的的可购金额redis中的key
     */
    public final static String NEW_PURCHASE_AMT = "newPurchaseAmt";

    /**
     * 用户购买时的token在redis中的key
     */
    public final static String USER_TOKEN = "userToken";

    /**
     * 协议支付绑定银行卡发送短信
     */
    public final static String AGREE_BIND_BANK_CARD_SEND_MSG = "agree:bind:bank:card:send:msg";

    /**
     * 关闭订单的redis中的key
     */
    public final static String CLOSE_ORDER = "closeOrder";

    /**
     * 每日分享送金豆redis中的key
     */
    public final static String VERY_DAY_SHARE = "veryDayShare";

    /**
     * 客服电话
     */
    public final static String SERVICE_PHONE = "400-888-7140";

    /**
     * 天眼数据查询加密解密key
      */
    public static final String EYEKEY = "EYEDATAYITONGJIN";

    /**
     * 标的购买枷锁名称
     */
    public static final String BORROW_LOCK = "BORROWLOCK";

    /**
     * 标的购买枷锁名称
     */
    public static final String AGREE_BORROW_LOCK = "AGREEBORROWLOCK";

    /**
     * 用户是否需要测评redis中的key
     */
    public static final String USER_IS_EVALUATION = "user:is:evaluation";




}
