package com.jiuyuan.constant;

import org.apache.commons.lang3.time.DateUtils;

public enum TimeFilter {
    /**
     * 最近三天
     */
    DAY3("DAY3", 3 * DateUtils.MILLIS_PER_DAY),

    /**
     * 最近七天
     */
    DAY7("DAY7", 7 * DateUtils.MILLIS_PER_DAY),
    
    /**
     * 最近半月
     */
    DAY15("HALFMONTH", 15 * DateUtils.MILLIS_PER_DAY),

    /**
     * 最近一个月
     */
    DAY30("DAY30", DAY15.longValue * 2),

    /**
     * 最近三个月
     */
    MONTH3("MONTH3", DAY30.longValue * 3),

    /**
     * 最近半年
     */
    HALFYEAR("HALFYEAR", MONTH3.longValue * 2),

    /**
     * 最近一年
     */
    YEAR("YEAR", HALFYEAR.longValue * 2);

    private TimeFilter(String value, long longValue) {
        this.stringValue = value;
        this.longValue = longValue;
    }

    private String stringValue;

    private long longValue;

    public static TimeFilter fromStringValue(String stringValue) {
        if (stringValue != null) {
            for (TimeFilter filter : TimeFilter.values()) {
                if (filter.stringValue.equals(stringValue.trim().toUpperCase())) {
                    return filter;
                }
            }
        }

        return DAY30;
    }

    public String getStringValue() {
        return stringValue;
    }

    public long getLongValue() {
        return longValue;
    }
}
