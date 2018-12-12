package com.finace.miscroservice.commons.plug.mybatis.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

/**
 * mysql的sql默认属性值
 */
public class MybatisSqlDefaultUtil {

    private MybatisSqlDefaultUtil() {

    }

    public static final Integer INTEGER_DEFAULT_VALUE = 0;
    public static final String STRING_DEFAULT_VALUE = "";
    public static final Long LONG_DEFAULT_VALUE = 0L;
    public static final Double DOUBLE_DEFAULT_VALUE = 0D;
    public static final Float FLOAT_DEFAULT_VALUE = 0F;
    public static final Date DATE_DEFAULT_VALUE = new Date();
    public static final Timestamp TIMESTAMP_DEFAULT_VALUE = Timestamp.from(Instant.now());


}
