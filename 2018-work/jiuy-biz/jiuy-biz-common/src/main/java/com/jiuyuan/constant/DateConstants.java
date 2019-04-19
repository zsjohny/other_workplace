package com.jiuyuan.constant;

import com.jiuyuan.util.constant.ConstantBinder;

public class DateConstants {
	 static {
	        ConstantBinder.bind(DateConstants.class, "UTF8", "/date_constants.properties");
	    }
	 	public static int SECONDS_FOREVER;
	    
	    public static int SECONDS_PER_MINUTE;

	    public static int SECONDS_FIVE_MINUTES;

	    public static int SECONDS_TEN_MINUTES;

	    public static int SECONDS_PER_HOUR;

	    public static int SECONDS_PER_DAY;

	    public static int SECONDS_OF_TWO_WEEKS;
	    
	    public static int SECONDS_PER_YEAR;

//    int SECONDS_FOREVER= 0;
//    
//    int SECONDS_PER_MINUTE = 60;
//
//    int SECONDS_FIVE_MINUTES = 300;
//
//    int SECONDS_TEN_MINUTES = 600;
//
//    int SECONDS_PER_HOUR = 3600;
//
//    int SECONDS_PER_DAY = 24 * 3600;
//
//    int SECONDS_OF_TWO_WEEKS = 14 * SECONDS_PER_DAY;
//    
//    int SECONDS_PER_YEAR = 365 * SECONDS_PER_DAY;
}
