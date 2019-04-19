package com.jiuy.web.controller.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class DateUtil {
	
	public static String convertMSEL(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(time));
	}
    
	public static long convertToMSEL(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.equals(time, ""))
			return 0;
		else
			return  sdf.parse(time).getTime();
	}
	
	public static int getPeriodDays(long beginTime, long endTime) {
		DateTime begin = new DateTime(beginTime);
		DateTime end = new DateTime(endTime);
		Period period = new Period(begin, end, PeriodType.days());
		
		return period.getDays();
	}
	
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String format(long time, String pattern) {
        return format(new Date(time), pattern);
    }
    
    public static long getDayZeroTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static String formatSimpleDate(long pastTime) {
        Calendar now = Calendar.getInstance();
        int yearCurrent = now.get(Calendar.YEAR);
        long timeCurrent = now.getTimeInMillis();
        long zeroTimeCurrent = getDayZeroTime(timeCurrent);

        Calendar past = Calendar.getInstance();
        past.setTimeInMillis(pastTime);
        int year = past.get(Calendar.YEAR);

        long timeDiff = timeCurrent - pastTime;
        if (timeDiff < DateUtils.MILLIS_PER_SECOND) {
            return "刚刚";
        } else if (timeDiff < DateUtils.MILLIS_PER_MINUTE) {// 一分钟之内
            return timeDiff / DateUtils.MILLIS_PER_SECOND + " 秒前";
        } else if (timeDiff < DateUtils.MILLIS_PER_HOUR) {// 一小时之内
            return timeDiff / DateUtils.MILLIS_PER_MINUTE + " 分钟前";
        }

        if (pastTime >= zeroTimeCurrent) {
            return new SimpleDateFormat("今天  HH:mm").format(new Date(pastTime));
        } else if (pastTime >= zeroTimeCurrent - DateUtils.MILLIS_PER_DAY) {
            return new SimpleDateFormat("昨天  HH:mm").format(new Date(pastTime));
        }

        if (yearCurrent - year <= 0) {
            int month = past.get(Calendar.MONTH) + 1;
            int day = past.get(Calendar.DAY_OF_MONTH);
            return month + "月" + day + "日  " + new SimpleDateFormat("HH:mm").format(new Date(pastTime));
        }

        String format = "yyyy-MM-dd";
        return new SimpleDateFormat(format).format(new Date(pastTime));
    }
    
    public static long getTodayStart() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
    
    public static long getTodayEnd(){
        return getTodayStart() + DateUtils.MILLIS_PER_DAY;
    }
    
    public static long parseStrTime2Long(String time) throws ParseException {
        SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        secFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return secFormatter.parse(time).getTime();
    }
    
    public static long getERPTime() {
    	Calendar calendar = new GregorianCalendar();
    	calendar.set(Calendar.HOUR_OF_DAY, 15);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	
    	return calendar.getTimeInMillis();
    }
    
    public static Date getTimesWeekStartTime() {
    	Calendar cal = Calendar.getInstance();
    	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    	return cal.getTime();
    }
    
    public static Calendar getStartMounth(){  
    	Calendar calendar = new GregorianCalendar();
    	calendar.set(Calendar.HOUR_OF_DAY,0);  
    	calendar.set(Calendar.MINUTE, 0);    
    	calendar.set(Calendar.SECOND, 0);    
    	calendar.set(Calendar.MILLISECOND, 0);    
    	
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));   
        return calendar;  
    } 
	
    public static void main(String[] args) throws ParseException {
    	System.out.println(convertToMSEL("2017-02-01 00:00:00"));
    	System.out.println(convertToMSEL("2017-02-28 23:59:59"));
    	System.out.println(convertToMSEL("2117-02-28 23:59:59"));
    	
    	StringBuilder retParams = new StringBuilder("sss");
    	System.out.println(retParams.length());
    	retParams.delete(0, retParams.length() - 1);
    	System.out.println(retParams);
    	
    	StringBuilder builder = new StringBuilder("sss");
	    change(builder);
	    System.out.println(builder.toString());
    }
    
    public static void isInteger() {
	    
    	String content = "{\"id\":2059,\"clothesNum\":\"S663\",\"name\":\"简约蕾丝拼接上衣\",\"brandId\":550,\"marketPrice\":299,\"marketPriceMin\":0,\"marketPriceMax\":0,\"price\":0,\"remainCount\":68,\"cash\":30,\"jiuCoin\":269,\"payAmountInCents\":0,\"code\":\"560\"}";
//    	String regEx = "\"id\":([0-9]*)";
    	String regEx = "\"id\":([0-9_]*),\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":[0-9.]*,\"jiuCoin\":[0-9.]*";
	    Pattern pat = Pattern.compile(regEx);    
	    Matcher mat = pat.matcher(content);
	    while (mat.find()) {
	    	System.out.println(mat.group(1));
	    	System.out.println(pat.matcher(content).matches());
	    }
	    
	}

	private static void change(StringBuilder builder) {
		builder.append("aaa");
	}  
    
}
