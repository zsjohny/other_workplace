package com.jiuyuan.util;


public interface TimeConstant {

	/*****************
	 * 毫秒常量
	 * @author zhuliming
	 */
	public static enum Miliseconds{
		HalfSecond(500), // "1/2秒的毫秒数"
		OneSecond(1000), // "1秒的毫秒数"
		TenSeconds(10*1000), // "10秒的毫秒数"
		TweentySeconds(20*1000), // "20秒的毫秒数"
		ThirdSeconds(30*1000), // "30秒的毫秒数"
		OneMinute(60*1000), // "一分钟的毫秒数"
		TenMinutes(10*60*1000), // "10分钟的毫秒数"
		OneHour(60*60*1000), // "一小时的毫秒数"
		TwoHours(2*60*60*1000), // "2小时的毫秒数"
		FourHours(4*60*60*1000),
		HalfAnHour(30*60*1000), // "1/2小时的毫秒数"
		HalfDay(12*60*60*1000), // "半天的毫秒数"
		OneDay(24L*60*60*1000), // "一天的毫秒数"
		TwoDays(2L*24*60*60*1000), // "2天的毫秒数"
		TenDays(10L*24*60*60*1000), // "10天的毫秒数"
		ElevenDays(11L*24*60*60*1000), // "11天的毫秒数"
		OneWeek(7L*24*60*60*1000); // "一周的毫秒数"
		public final long miliseconds;
		private Miliseconds(final long miliseconds){
			this.miliseconds = miliseconds;
		}
	}
	
	/************
	 * 秒数常量
	 */
	public static enum Seconds{
		OneWeek(60 * 60 * 24 * 7), // "一周的秒"
		ThreeDays(3 * 24 * 60 * 60),
		OneDay(24 * 60 * 60), // "一天的秒数"
		HalfDay(12 * 60 * 60), // "半天的秒数"
		OneHour(60 *60), // "一小时的秒数"
		HalfAnHour(30 *60), // "1/2小时的秒数"
		OneMinute(60); // "一分钟的秒数"
		public final int seconds;
		private Seconds(final int seconds){
			this.seconds = seconds;
		}
	}
	
	/************
	 * 分钟常量
	 */
	public static enum Minutes{
		OneDay(24 * 60), // "一天的分钟数"
		OneHour(60), // "一小时的分钟数"
		HalfAnHour(30), // "1/2小时的分钟数"
		TwoDays(2*24 * 60), // "2天的分钟数"
		OneWeek(7*24 * 60); // "一周的分钟数"
		public final int minutes;
		private Minutes(final int minutes){
			this.minutes = minutes;
		}
	}	
	
	/************
	 * 小时常量
	 */
	public static enum Hours{
		OneDay(24), //"一天的小时数"
		HalfADay(12); //"1/2天的小时数"
		public final int hours;
		private Hours(final int hours){
			this.hours = hours;
		}
	}		
	
	/******************
	 * 所有需要用到的时间的格式化器
	 * @author zhuliming
	 *
	 */
	public static enum TimeFormat{
		YYYYMMDDHHMMSS("yyyyMMddHHmmss"), //"年月日时分秒(无下划线) yyyyMMddHHmmss"
		YYYYMMDD("yyyyMMdd"), //"年月日(无下划线) yyyyMMdd"
		YYYYMMDDHH("yyyyMMddHH"), //"年月日时(无下划线) yyyyMMddHH"
		yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"), //"完整时间 yyyy-MM-dd HH:mm:ss"
		yyyy_MM_dd("yyyy-MM-dd"), //"完整时间 yyyy-MM-dd"
		MMDDHHMMSS("MMddHHmmss"), //"月日时分秒(无下划线) MMddHHmmss"
		HH("HH"),//小时
		YYYYMMDDHHMMssSS("yyyyMMddHHmmssSS"); //"年月日时分秒(无下划线) yyyyMMddHHmmssSS"
		public final String format;
		private TimeFormat(final String f){
			this.format = f;
		}
	}
	
	/**
	 * @ClassName: DateType
	 * @Description: 规定时期类型及天数
	 * @author yunshang_734@163.com
	 * @date 2015-1-6 下午4:10:46
	 */
	public static enum DateType{
		DAY(1),WEEK(7),MONTH(30),YEAR(365);
		public final int day;
		private DateType(final int day){
			this.day = day;
		}
		public final static DateType parse(int d){
			if(DAY.day == d){
				return DAY;
			}else if (WEEK.day == d) {
				return WEEK;
			}else if (MONTH.day == d) {
				return MONTH;
			}else if (YEAR.day == d) {
				return YEAR;
			}
			return null;
		}
	}
}