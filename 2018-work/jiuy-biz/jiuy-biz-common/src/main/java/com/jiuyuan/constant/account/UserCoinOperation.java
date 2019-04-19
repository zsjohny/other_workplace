package com.jiuyuan.constant.account;

import com.jiuyuan.util.enumeration.IntEnum;

public enum UserCoinOperation implements IntEnum {
    /** 扫码兑换 */
    EXCHANGE(1),

    /** 购买消费 */
    PURCHASE(2),

    /** 取消订单 */
    ORDER_CANCEL(3),

    /** 注册赠送 */
    REGISTER_GRANT(4),

    /** 分享赠送 */
    SHARE_GRANT(5),

    /** 签到赠送 */
    SIGN_GRANT(6),

    /** 分享被点击 */
    SHARE_CALLBACK(7),  // RETURN_GOODS(7),

    /** 邀请好友 */
    INVITE(8),

    /** 被邀请 */
    INVITED(9),
    
    /** 积分兑换商品*/
    CLOTHES_EXCHANGE(10),
    
    /** 优惠券兑换*/
    COUPON_EXCHANGE(11),
    
    /** 领取礼包*/
    FETCH_GIFT(12),
    
    /** 扫门店二维码*/
    SCAN_STORE_QRCODE(13),
    
    /** 被邀请人下单得玖币*/
    INVITED_ORDER(14),
    
    /** 评论得玖币*/
    COMMENT(15),
    
    /** 发放玖币*/
    GRANT_JIUCOIN(16),
    
    /** 玖币抵钱*/
    JIUCOIN_DEDUCT(18),
    
    /** 抽奖获取*/
    DRAW_LOTTERY_JIUCOIN(17),

    /** 活动 */
    ACTIVITY(50),

    /** 订单被关闭 */
    ORDER_CLOSE(100), // ORDER_CLOSED(100),
	
	/** 分享注册 */
	SHARED_REGISTER(101),
	/** 分享下单 */
	SHARED_ORDER(102);

    private UserCoinOperation(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

}
