package org.dream.utils.date;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Created by 12 on 2016/11/9.
 */
public class SimpleDateFormatUtil {

    public static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyyMMdd HH:mm:ss.SSS");
    public static final FastDateFormat DATE_FORMAT1 = FastDateFormat.getInstance("yyyyMMdd");
    public static final FastDateFormat DATE_FORMAT2 = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat DATE_FORMAT3 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
    public static final FastDateFormat DATE_FORMAT4 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
    public static final FastDateFormat DATE_FORMAT5 = FastDateFormat.getInstance("yyyyMMddHHmm");
    public static final FastDateFormat DATE_FORMAT6 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public final static FastDateFormat MINUTES_FORMAT = FastDateFormat.getInstance("HHmm");


}
