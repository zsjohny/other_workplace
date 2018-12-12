package com.goldplusgold.td.sltp.core.operate.enums;

/**
 * 当日的涨停板限制和可用手数
 * Created by Ness on 2017/5/23.
 */
public enum AccordLimitAndLotsEnum {
    /**
     * AUTO_TRIGGER 自动触发
     * <p>
     * LIMIT_LOTS 手数限制
     * <p>
     * SETTING_ERROR 设置手数失败
     * <p>
     * BEAR_LOW_TP(空头的止盈价格小于跌停板)
     * <p>
     * BEAR_HIGH_TP(空头的止损价格大于涨停板)
     * <p>
     * EMPTY_RECORD(订单中没有符合记录)
     * <p>
     * <p>
     * BULL_HIGH_TP(多头的止盈价格大于涨停板)
     * <p>
     * <p>
     * <p>
     * BEAR_HIGH_TP(多头的止损价格 小于涨停板)
     * <p>
     * SUCCESS_CODE(成功的标识)
     */
    AUTO_TRIGGER(220), LIMIT_LOTS(221), SETTING_LOTS_ERROR(222),
    BEAR_LOW_TP(223), BEAR_HIGH_SL(224), EMPTY_RECORD(225), BULL_HIGH_TP(226), BULL_LOW_SL(227),
    EMPTY_PARAM(228), SUCCESS_CODE(230);


    private int code;


    AccordLimitAndLotsEnum(int code) {
        this.code = code;
    }

    public int toCode() {

        return code;
    }

}
