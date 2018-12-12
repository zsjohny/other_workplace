package com.goldplusgold.td.sltp.monitor.conf;

/**
 * 监听模块用到的一些常量
 * Created by Administrator on 2017/5/12.
 */
public class Constant {
    /** 连字符 */
    public static final String HYPHEN = "_";

    /** 合约名称: AU */
    public static final String CONTRACT_NAME_AU = "AU";
    /** 合约名称: AG */
    public static final String CONTRACT_NAME_AG = "AG";
    /** 合约名称: MAU */
    public static final String CONTRACT_NAME_MAU = "MAU";

    /** 空仓: BEAR */
    public static final String BEARBULL_BEAR = "BEAR";
    /** 多仓: BULL */
    public static final String BEARBULL_BULL = "BULL";
    /** 止盈: TP */
    public static final String SLTP_TP = "TP";
    /** 止损: SL */
    public static final String SLTP_SL = "SL";
    /** 组合: 空仓止盈 */
    public static final String COM_BEAR_TP = "BEAR_TP";
    /** 组合: 空仓止损 */
    public static final String COM_BEAR_SL = "BEAR_SL";
    /** 组合: 多仓止盈 */
    public static final String COM_BULL_TP = "BULL_TP";
    /** 组合: 多仓止损 */
    public static final String COM_BULL_SL = "BULL_SL";


    /** ZSET KEY: Au(T+D)空仓止盈 */
    public static final String ZKEY_AUTD_BEAR_TP = "ZKEY_AUTD_BEAR_TP";
    /** ZSET KEY: Au(T+D)空仓止损 */
    public static final String ZKEY_AUTD_BEAR_SL = "ZKEY_AUTD_BEAR_SL";
    /** ZSET KEY: Au(T+D)多仓止盈 */
    public static final String ZKEY_AUTD_BULL_TP = "ZKEY_AUTD_BULL_TP";
    /** ZSET KEY: Au(T+D)多仓止损 */
    public static final String ZKEY_AUTD_BULL_SL = "ZKEY_AUTD_BULL_SL";
    /** ZSET KEY: Ag(T+D)空仓止盈 */
    public static final String ZKEY_AGTD_BEAR_TP = "ZKEY_AGTD_BEAR_TP";
    /** ZSET KEY: Ag(T+D)空仓止损 */
    public static final String ZKEY_AGTD_BEAR_SL = "ZKEY_AGTD_BEAR_SL";
    /** ZSET KEY: Ag(T+D)多仓止盈 */
    public static final String ZKEY_AGTD_BULL_TP = "ZKEY_AGTD_BULL_TP";
    /** ZSET KEY: Ag(T+D)多仓止损 */
    public static final String ZKEY_AGTD_BULL_SL = "ZKEY_AGTD_BULL_SL";
    /** ZSET KEY: mAu(T+D)空仓止盈 */
    public static final String ZKEY_MAUTD_BEAR_TP = "ZKEY_MAUTD_BEAR_TP";
    /** ZSET KEY: mAu(T+D)空仓止损 */
    public static final String ZKEY_MAUTD_BEAR_SL = "ZKEY_MAUTD_BEAR_SL";
    /** ZSET KEY: mAu(T+D)多仓止盈 */
    public static final String ZKEY_MAUTD_BULL_TP = "ZKEY_MAUTD_BULL_TP";
    /** ZSET KEY: mAu(T+D)多仓止损 */
    public static final String ZKEY_MAUTD_BULL_SL = "ZKEY_MAUTD_BULL_SL";

    /** ZSET KEY MODE: Au(T+D)空仓 */
    public static final String ZKEY_AUTD_BEAR_MODE = "ZKEY_AUTD_BEAR";
    /** ZSET KEY MODE: Au(T+D)多仓 */
    public static final String ZKEY_AUTD_BULL_MODE = "ZKEY_AUTD_BULL";
    /** ZSET KEY MODE: Ag(T+D)空仓 */
    public static final String ZKEY_AGTD_BEAR_MODE = "ZKEY_AGTD_BEAR";
    /** ZSET KEY MODE: Au(T+D)多仓 */
    public static final String ZKEY_AGTD_BULL_MODE = "ZKEY_AGTD_BULL";
    /** ZSET KEY MODE: mAu(T+D)空仓 */
    public static final String ZKEY_MAUTD_BEAR_MODE = "ZKEY_MAUTD_BEAR";
    /** ZSET KEY MODE: mAu(T+D)多仓 */
    public static final String ZKEY_MAUTD_BULL_MODE = "ZKEY_MAUTD_BULL";
}
