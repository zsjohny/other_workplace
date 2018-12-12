package com.goldplusgold.mq.p2p;

/**
 * 默认频道
 * Created by Administrator on 2017/5/18.
 */
public class P2PChannels {
    /** 测试频道 */
    public static final String P2P_CH_FOR_TEST = "p2p_ch_for_test";
    /** 止盈止损交易处理 */
    public static final String CH_TRADE_OFFSET = "ch_trade_offset";
    /** 止盈止损回调处理 */
    public static final String CH_TRADE_NOTIFY= "ch_trade_notify";
    /** 用户主动平仓 */
    public static final String CH_USER_OFFSET = "ch_user_offset";
    public static final String CH_USER_OFFSET_TEST = "ch_user_offset_test";
}
