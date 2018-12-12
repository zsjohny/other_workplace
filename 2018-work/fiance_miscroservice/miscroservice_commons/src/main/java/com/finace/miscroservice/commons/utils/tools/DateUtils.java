package com.finace.miscroservice.commons.utils.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
	public static String dateStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 hh:mm");
		String str = format.format(date);
		return str;
	}
	
	public static String dateStr(Date date,String f) {
		SimpleDateFormat format = new SimpleDateFormat(f);
		String str = format.format(date);
		return str;
	}

	public static String dateStr2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		return str;
	}
	public static String thisYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String str = format.format(new Date());
		String year = String.format("%s-01-01",str);
		return year;
	}

	public static String dateStr3(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = format.format(date);
		return str;
	}

	public static String dateStr4(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;

	}
	
	public static String dateStr5(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String str = format.format(date);
		return str;

	}
	
	public static String dateStr6(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		String str = format.format(date);
		return str;

	}

	/**
	 * 将秒转换成时间
	 * @param times
	 * @return
	 */
	public static Date getDate(String times) {
		long time = Long.parseLong(times);
		return new Date(time*1000);
	}

	public static String dateStr(String times) {
		return dateStr(getDate(times));
	}
	public static String dateStr2(String times) {
		return dateStr2(getDate(times));
	}
	public static String dateStr3(String times) {
		return dateStr3(getDate(times));
	}
	public static String dateStr4(String times) {
		return dateStr4(getDate(times));
	}
    public static String dateStr5(String times) {
        return dateStr5(getDate(times));
    }
	public static long getTime(Date date) {
		return date.getTime() / 1000;
	}
	
	/**
	 * 获取当前时间  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getNowDateStr() {
		return dateStr4(getDate(getNowTimeStr()));
	}
	
	/**
	 * 获取当前时间  yyyy-MM-dd
	 * @return
	 */
	public static String getNowDateStr2() {
		return dateStr2(getDate(getNowTimeStr()));
	}

	/**
	 * 获取当前时间  yyyyMMdd
	 * @return
	 */
	public static String getNowDateStr5() {
		return dateStr5(getDate(getNowTimeStr()));
	}

	public static int getDay(Date d){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * s - 表示 "yyyy-mm-dd" 形式的日期的 String 对象 
//	 * @param f
	 * @return
	 */
	public static Date valueOf(String s){
		final int YEAR_LENGTH = 4;
        final int MONTH_LENGTH = 2;
        final int DAY_LENGTH = 2;
        final int MAX_MONTH = 12;
        final int MAX_DAY = 31;
        int firstDash;
        int secondDash;
        Date d = null;

        if (s == null) {
            throw new IllegalArgumentException();
        }

        firstDash = s.indexOf('-');
        secondDash = s.indexOf('-', firstDash + 1);
        if ((firstDash > 0) && (secondDash > 0) && (secondDash < s.length()-1)) {
            String yyyy = s.substring(0, firstDash);
            String mm = s.substring(firstDash + 1, secondDash);
            String dd = s.substring(secondDash + 1);
            if (yyyy.length() == YEAR_LENGTH && mm.length() == MONTH_LENGTH &&
                dd.length() == DAY_LENGTH) {
                int year = Integer.parseInt(yyyy);
                int month = Integer.parseInt(mm);
                int day = Integer.parseInt(dd);
                if (month >= 1 && month <= MAX_MONTH) {
                    int maxDays = MAX_DAY;
                    switch (month) {
                        // February determine if a leap year or not
                        case 2:
                            if((year % 4 == 0 && !(year % 100 == 0)) || (year % 400 == 0)) {
                                maxDays = MAX_DAY-2; // leap year so 29 days in February
                            } else {
                                maxDays = MAX_DAY-3; //  not a leap year so 28 days in February 
                            }
                            break;
                        // April, June, Sept, Nov 30 day months
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            maxDays = MAX_DAY-1;
                            break;
                    }
                    if (day >= 1 && day <= maxDays) {
                        Calendar cal=Calendar.getInstance();
                        cal.set(year, month - 1, day,0,0,0);
                        cal.set(Calendar.MILLISECOND, 0);
                        d=cal.getTime();
                    }
                }
            }
        }
        if (d == null) {
            throw new IllegalArgumentException();
        }
        return d;
	}
	
	/**
	 * @author lijie
	 * @param Begin
	 * @param end
	 * 传入开始时间 和 结束时间 格式如：2012-09-07
	 * @return
	 * 返回Map  获取相隔多少年 get("YEAR")及为俩个时间年只差，月 天，类推
	 *  Key ：
	 *  YEAR MONTH DAY
	 *  如果开始时间 晚于 结束时间 return null；
	 */
	
	public static Map getApartTime(String Begin, String end) {
		  String[] temp = Begin.split("-");
	        String[] temp2 = end.split("-");
	        if (temp.length > 1 && temp2.length > 1) {
	            Calendar ends = Calendar.getInstance();
	            Calendar begin = Calendar.getInstance();

	            begin.set(NumberUtil.getInt(temp[0]),
	                    NumberUtil.getInt(temp[1]), NumberUtil.getInt(temp[2]));
	            ends.set(NumberUtil.getInt(temp2[0]),
	                    NumberUtil.getInt(temp2[1]), NumberUtil.getInt(temp2[2]));
	            if (begin.compareTo(ends) < 0) {
	                Map map = new HashMap();
	                ends.add(Calendar.YEAR, -NumberUtil.getInt(temp[0]));
	                ends.add(Calendar.MONTH, -NumberUtil.getInt(temp[1]));
	                ends.add(Calendar.DATE, -NumberUtil.getInt(temp[2]));
	                map.put("YEAR", ends.get(Calendar.YEAR));
	                map.put("MONTH", ends.get(Calendar.MONTH) + 1);
	                map.put("DAY", ends.get(Calendar.DATE));
	                return map;
	            }
	        }
	        return null;
	}
	
	public static Date rollDay(Date d,int day){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
	public static Date rollMon(Date d,int mon){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, mon);
		return cal.getTime();
	}
	public static Date rollYear(Date d,int year){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}
	public static Date rollDate(Date d,int year,int mon,int day){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, mon);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
	
	
	/**
	 * 获取当前时间的秒数字符串
	 * @return
	 */
	public static String getNowTimeStr(){
		String str=Long.toString(System.currentTimeMillis() / 1000);
		return str;
	}
	public static String getTimeStr(Date time){
		long date = time.getTime();
		String str=Long.toString(date / 1000);
		return str;
	}

	public static String rollMonth(String addtime,String time_limit){
		Date t=DateUtils.rollDate(DateUtils.getDate(addtime), 0, NumberUtil.getInt(time_limit),0);
		return t.getTime()/1000+"";
	}
	
	public static String rollDay(String addtime,String time_limit_day){
		Date t=DateUtils.rollDate(DateUtils.getDate(addtime), 0, 0,NumberUtil.getInt(time_limit_day));
		return t.getTime()/1000+"";
	}
	
	public static Date getIntegralTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	public static Date getLastIntegralTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	public static Date getLastSecIntegralTime(Date d){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static long getTime(String format){
		long t=0;
		if(StringUtils.isBlank(format)) return t;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(format);
			t=date.getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 从第二天开始算起 如计算5月1日到10月1日，计算从5月2日开始到10月1日的天数
	 * @Title: daysBetween 
	 * @param @param smdate
	 * @param @param bdate
	 * @param @return
	 * @param @throws ParseException 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
    public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    } 
    
    //rjg 20160104 yemian
    public static String dateAndDay(String a,String day,int mjt) throws ParseException{
    	 Date d = getDate(a);
         d.setDate(d.getDate()+Integer.parseInt(day)+mjt);
         SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
         String sdate = sdf1.format(d);
         return sdate;
    }

	public static Date dateAndDayByDate(String a,String day) throws ParseException{
		Date d = getDate(a);
		d.setDate(d.getDate()+Integer.parseInt(day));
		return d;
	}
    
    public static String dateAndMonthAndDay(String a,String month,int mjt) throws ParseException{
    	Date d = getDate(a);
        d.setDate(d.getDate()+mjt);
        d.setDate(d.getMonth()+Integer.parseInt(month));
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String sdate = sdf1.format(d);
        return sdate;
   }
    
    
    public static String dateAndDay(String a,String day) throws ParseException{
   	    Date d = getDate(a);
        d.setDate(d.getDate()+Integer.parseInt(day));
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String sdate = sdf1.format(d);
      
        return sdate;
   }
   
   public static String dateAndMonthAndDay(String a,String month) throws ParseException{
   	   Date d = getDate(a);
       d.setDate(d.getMonth()+Integer.parseInt(month));
       SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
       String sdate = sdf1.format(d);
       return sdate;
  }
   
   /** 
    * 两个时间之间相差距离多少天 
//    * @param one 时间参数 1：
//    * @param two 时间参数 2：
    * @return 相差天数 
    */  
   public static long getDistanceDays(String str1, String str2) throws Exception{  
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
       Date one;  
       Date two;  
       long days=0;  
       try {  
           one = df.parse(str1);  
           two = df.parse(str2);  
           long time1 = one.getTime();  
           long time2 = two.getTime();  
           long diff ;  
           if(time1<time2) {  
               diff = time2 - time1;  
           } else {  
               diff = time1 - time2;  
           }  
           days = diff / (1000 * 60 * 60 * 24);  
       } catch (ParseException e) {  
           e.printStackTrace();  
       }  
       return days;  
   }  
     
   /** 
    * 两个时间相差距离多少天多少小时多少分多少秒 
    * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
    * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
    * @return long[] 返回值为：{天, 时, 分, 秒} 
    */  
   public static long[] getDistanceTimes(String str1, String str2) {  
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
       Date one;  
       Date two;  
       long day = 0;  
       long hour = 0;  
       long min = 0;  
       long sec = 0;  
       try {  
           one = df.parse(str1);  
           two = df.parse(str2);  
           long time1 = one.getTime();  
           long time2 = two.getTime();  
           long diff ;  
           if(time1<time2) {  
               diff = time2 - time1;  
           } else {  
               diff = time1 - time2;  
           }  
           day = diff / (24 * 60 * 60 * 1000);  
           hour = (diff / (60 * 60 * 1000) - day * 24);  
           min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
           sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
       } catch (ParseException e) {  
           e.printStackTrace();  
       }  
       long[] times = {day, hour, min, sec};  
       return times;  
   }  
   /** 
    * 两个时间相差距离多少天多少小时多少分多少秒 
    * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
    * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
    * @return String 返回值为：xx天xx小时xx分xx秒 
    */  
   public static String getDistanceTime(String str1, String str2) {  
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
       Date one;  
       Date two;  
       long day = 0;  
       long hour = 0;  
       long min = 0;  
       long sec = 0;  
       try {  
           one = df.parse(str1);  
           two = df.parse(str2);  
           long time1 = one.getTime();  
           long time2 = two.getTime();  
           long diff ;  
           if(time1<time2) {  
               diff = time2 - time1;  
           } else {  
               diff = time1 - time2;  
           }  
           day = diff / (24 * 60 * 60 * 1000);  
           hour = (diff / (60 * 60 * 1000) - day * 24);  
           min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
           sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
       } catch (ParseException e) {  
           e.printStackTrace();  
       }  
       return day + "天" + hour + "小时" + min + "分" + sec + "秒";
   }
   
   /**
    * 两个时间比较大小
    * @param DATE1
    * @param DATE2
    * @return
    */
   public static boolean compareDate(String DATE1, String DATE2) {
//       前面的参数大于后面的参数 True
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       try {
           Date dt1 = df.parse(DATE1);
           Date dt2 = df.parse(DATE2);
           if (dt1.getTime() > dt2.getTime()) {
               return true;//dt1 在dt2前
           } else if (dt1.getTime() < dt2.getTime()) {
               return false; //dt1在dt2后
           } else {
               return false;
           }
       } catch (Exception exception) {
           exception.printStackTrace();
       }
       return false;
   }
   
   /**
    * 判断两个日期是否在同一个月
    * @param date1
    * @param date2
    * @return
    */
   public static boolean dateEqMonth(String date1, String date2) {
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   try {
		   Date	dt1 = df.parse(date1);
		   Date dt2 = df.parse(date2);
		   Calendar calendar1 = Calendar.getInstance();
	       calendar1.setTime(dt1);
	       Calendar calendar2 = Calendar.getInstance();
	       calendar2.setTime(dt2);
//	       int year1 = calendar1.get(Calendar.YEAR);
//	       int year2 = calendar2.get(Calendar.YEAR);
//	       int month1 = calendar1.get(Calendar.MONTH);
//	       int month2 = calendar2.get(Calendar.MONTH);
//	       System.out.println(year1 + "  " + month1);
//	       System.out.println(year2 + "  " + month2);
	       return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
		}
       return false;
   }

	/*
      * 将时间转换为时间戳
      */
	public static String dateToStamp(String s) throws ParseException{
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	/*
      * 将时间转换为时间戳
      */
	public static String dateToStampSimple(String s) throws ParseException{
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	/*
    * 将时间戳转换为时间
    */
	public static String stampToDate(String s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}


	/**
	 * 计算 afterYear后的日期
	 * @param afterYear
	 * @return
	 */
	public static String calDate(int afterYear){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
//		System.out.println(calendar.get(Calendar.YEAR));//今天的日期
		calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)+afterYear);
//		System.out.println(calendar.get(Calendar.YEAR));
//		calendar.getTime();
//		System.out.println(calendar.getTime());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String date = simpleDateFormat.format(calendar.getTime());
//		System.out.println(date);
		return date;
	}


	/**
	 * 当前时间距离 第二天凌晨 多少秒
	 */
	public static long calSec(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+1);
		String endTime = df.format(calendar.getTime());
		Date endDate;
//		long day = 0;
//		long hour = 0;
//		long min = 0;
		long sec = 0;
		try {
			endDate = df.parse(endTime);
			long time1 = new Date().getTime();
			long time2 = endDate.getTime();
			long diff ;
			if(time1<time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
//			day = diff / (24 * 60 * 60 * 1000);
//			hour = (diff / (60 * 60 * 1000) - day * 24);
//			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
//			sec = (diff/1000-day*24*60*60-hour*60*60-min*60);

			sec = diff/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		return day + "天" + hour + "小时" + min + "分" + sec + "秒";

		return sec;
	}



	public static void main(String[] args) {
//		System.out.println(calDate(5));
		System.out.println(compareDate("2018-07-03 00:00:00","2018-07-04 00:00:00"));
	}
	/**
	 * JAVA获得0-9,a-z,A-Z范围的随机数
	 * @param length 随机数长度
	 * @return String
	 */
		public static String getRandomChar(int length) {
			char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
					'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
					'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
					'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
					'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
					'X', 'Y', 'Z' };
			Random random = new Random();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < 6; i++) {
				buffer.append(chr[random.nextInt(62)]);
			}
			return buffer.toString();
		}
}
