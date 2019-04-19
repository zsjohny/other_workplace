package com.jiuyuan.constant.account;

import com.jiuyuan.util.enumeration.IntEnum;

public enum UserCoinOperationDetail implements IntEnum {
 
    
    /** 扫码兑换 */
    EXCHANGE(1, "扫码兑换"),

    /** 购买消费 */
    PURCHASE(2, "购买消费"),

    /** 取消订单 */
    ORDER_CANCEL(3, "取消订单"),

    /** 注册赠送 */
    REGISTER_GRANT(4, "注册赠送"),

    /** 分享赠送 */
    SHARE_GRANT(5, "分享赠送"),

    /** 签到赠送 */
    SIGN_GRANT(6, "签到赠送"),

    /** 分享被点击 */
    SHARE_CALLBACK(7, "分享被点击"),

    /** 邀请好友 */
    INVITE(8, "邀请好友"),

    /** 被邀请 */
    INVITED(9, "被邀请"),
    
    /** 积分兑换*/
    CLOTHES_EXCHANGE(10, "兑换衣服"),
    
    /** 优惠券兑换*/
    COUPON_EXCHANGE(11, "兑换优惠券"),

    /** 领取礼包*/
    FETCH_GIFT(12, "每月礼包领取"),
    
    /** 扫门店二维码*/
    SCAN_STORE_QRCODE(13, "门店扫码得玖币"),
    
    /** 被邀请人下单得玖币*/
    INVITED_ORDER(14, "邀请有礼"),
    
    /** 评论得玖币*/
    COMMENT(15, "评论得玖币"),
    
    /** 玖币发放*/
    GRANT_JIUCOIN(16, "发放玖币"),
    
    /** 抽奖获取*/
    DRAW_LOTTERY_JIUCOIN(17, "抽奖获取"),
    
    /** 玖币抵扣*/
    JIUCOIN_DEDUCT(18, "玖币抵钱"),
    
    /** 活动 */
    ACTIVITY(50, "活动"),

    /** 退还玖币 */
    ORDER_CLOSE(100, "退还玖币"),
    
    /** 分享注册 */
	SHARED_REGISTER(101,"分享注册"),
	/** 分享下单 */
	SHARED_ORDER(102,"分享下单");

    private UserCoinOperationDetail(int intValue, String name) {
        this.intValue = intValue;
        this.name = name;
    }

    private int intValue;
    /** 名称 */
    private String name;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public String getName() {
        return name;
    }


    /**
     * 通过代号获取活动
     * @param shortName
     * @return
     */
    public static UserCoinOperationDetail getNameByValue(int intValue ) {
        if (intValue<=0) {
            return null;
        }

        for (UserCoinOperationDetail pointTypeEnum : UserCoinOperationDetail.values()) {
            if (pointTypeEnum.getIntValue()==intValue) {
                return pointTypeEnum;
            }
        }

        return null;
    }



}
