package org.dream.utils.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 统计中需要用到的时间，如今天，昨天，上周，上月等
 */
public class Days {

	private static final String six = " 06:00:00";
	private static final String five = " 05:59:59";
	
	public static final String TODAY = "today";
	public static final String YESTERDAY = "yesterday";
	public static final String THIS_WEEK = "thisWeek";
	public static final String LAST_WEEK = "lastWeek";
	public static final String THIS_MONTH = "thisMonth";
	public static final String LAST_MONTH = "lastMonth";
	public static final String SELF_CONTROL = "selfControl";

	/**
	 * 从今天往前一周 由于统计的需要，这里有9天的数据
	 */
	public static List<String> getSevenDays() {
		List<String> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DATE, +2);
		for (int i = 0; i < 9; i++) {
			calendar.add(Calendar.DATE, -1);
			String name = "before" + i;
			name = sdf.format(calendar.getTime());
			list.add(name);
		}
		return list;
	}

	/**
	 * 从今天往前三十天 由于统计的需要，这里有32天的数据
	 */
	public static List<String> getThityDays() {
		List<String> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DATE, +2);
		for (int i = 0; i < 32; i++) {
			calendar.add(Calendar.DATE, -1);
			String name = "before" + i;
			name = sdf.format(calendar.getTime());
			list.add(name);
		}
		return list;
	}

	/**
	 * 今天，统一从六点开始计算
	 */
	public static List<String> getTodayBeginEnd() {
		List<String> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(calendar.getTime());
		if (beforeSix()) {
			calendar.add(Calendar.DATE, -1);
			String yesterday = sdf.format(calendar.getTime());
			String t0 = yesterday + six;
			String t1 = today + five;
			list.add(t0);
			list.add(t1);
		} else {
			calendar.add(Calendar.DATE, +1);
			String tomorrow = sdf.format(calendar.getTime());
			String t0 = today + six;
			String t1 = tomorrow + five;
			list.add(t0);
			list.add(t1);
		}
		return list;
	}

	/**
	 * 昨天，统一从六点开始计算
	 */
	public static List<String> getYesterdayBeginEnd() {
		List<String> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(calendar.getTime());
		calendar.add(Calendar.DATE, -1);
		String yesterday = sdf.format(calendar.getTime());
		if (beforeSix()) {
			calendar.add(Calendar.DATE, -1);
			String beforeYesterday = sdf.format(calendar.getTime());
			String t0 = beforeYesterday + six;
			String t1 = yesterday + five;
			list.add(t0);
			list.add(t1);
		} else {
			String t0 = yesterday + six;
			String t1 = today + five;
			list.add(t0);
			list.add(t1);
		}
		return list;
	}

	/**
	 * 上周，统一从六点开始计算 如果是周一的凌晨6点之前，则计算上上周
	 */
	public static List<String> getLastweekBeginEnd() {
		List<String> list = new ArrayList<>();
		// 先获取上周的起始时间
		Calendar calendar = Calendar.getInstance();
		// 判定是否是周一早上六点前
		if (calendar.get(Calendar.DAY_OF_WEEK) == 2 && beforeSix()) {// 周一是一星期的第二天
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.add(Calendar.DATE, -14);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date sTime = calendar.getTime();
			calendar.add(Calendar.DATE, +7);
			Date eTime = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String orderTimeStart = sdf.format(sTime) + six;
			String orderTimeEnd = sdf.format(eTime) + five;
			list.add(orderTimeStart);
			list.add(orderTimeEnd);
		} else {
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.add(Calendar.DATE, -7);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date sTime = calendar.getTime();
			calendar.add(Calendar.DATE, +7);
			Date eTime = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String orderTimeStart = sdf.format(sTime) + six;
			String orderTimeEnd = sdf.format(eTime) + five;
			list.add(orderTimeStart);
			list.add(orderTimeEnd);
		}
		return list;
	}

	/**
	 * 本周，统一从六点开始计算 如果是周一的凌晨6点之前，则计算上周
	 */
	public static List<String> getThisweekBeginEnd() {
		List<String> list = new ArrayList<>();
		// 先获取上周的起始时间
		Calendar calendar = Calendar.getInstance();
		// 判定是否是周一早上六点前
		if (calendar.get(Calendar.DAY_OF_WEEK) == 2 && beforeSix()) {// 周一是一星期的第二天
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.add(Calendar.DATE, -7);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date sTime = calendar.getTime();
			calendar.add(Calendar.DATE, +7);
			Date eTime = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String orderTimeStart = sdf.format(sTime) + six;
			String orderTimeEnd = sdf.format(eTime) + five;
			list.add(orderTimeStart);
			list.add(orderTimeEnd);
		} else {
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date sTime = calendar.getTime();
			calendar.add(Calendar.DATE, +7);
			Date eTime = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String orderTimeStart = sdf.format(sTime) + six;
			String orderTimeEnd = sdf.format(eTime) + five;
			list.add(orderTimeStart);
			list.add(orderTimeEnd);
		}
		return list;
	}

	/**
	 * 上个月，从上个月第一个周一六点到这个月第一个周一六点。
	 */
	public static List<String> getLastMonthBeginEnd() {
		List<String> list = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		Date thisFirstMonday = getFirstMondayOfMonth(year, month);
		Date lastFirstMonday = getFirstMondayOfMonth(year, month - 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastMonday = sdf.format(lastFirstMonday) + six;
		list.add(lastMonday);
		String thisMonday = sdf.format(thisFirstMonday) + five;
		list.add(thisMonday);
		return list;
	}

	/**
	 * 这个月，从这个月第一个周一六点到现在。 
	 * 如果现在处于月初，本月还没有开始，则返回空.
	 */
	public static List<String> getThisMonthBeginEnd() {
		List<String> list = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		Date nowa = cal.getTime();
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		// 获取这个月第一个周一六点的时间
		Date thisFirstMonday = getFirstMondayOfMonth(year, month);
		//判定本月是否开始
		if(nowa.before(thisFirstMonday))
			return null;
		if(nowa.equals(thisFirstMonday)&&beforeSix())
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String thisMonday = sdf.format(thisFirstMonday) + six;
		// 获取当前时间
		SimpleDateFormat newa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		list.add(thisMonday);
		String nowaa = newa.format(nowa);
		list.add(nowaa);
		return list;
	}

	public static Date getFirstMondayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1); // 设为第一天
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			cal.add(Calendar.DATE, 1);
		}
		return cal.getTime();
	}

	public static void main(String[] args) {
		getLastMonthBeginEnd();
		List<String> timeList = Days.getThisMonthBeginEnd();
		System.out.println(timeList.get(0));
		System.out.println(timeList.get(1));
//		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
//	     Date date = null;
//	     Date date2 = null;
//		try {
//			date = simpleDateFormat .parse("2010-06-25");
//			date2 = simpleDateFormat .parse("2010-06-25"+five);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//	     Long timeStemp = date.getTime();
//	     System.out.println(timeStemp );
//	     Long timeStemp2 = date2.getTime();
//	     System.out.println(timeStemp2 );
	}

	/**
	 * 判定是否早上六点前
	 */
	public static boolean beforeSix() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= 6)
			return false;
		else
			return true;
	}
	
	/**
	 * 获取启示时间
	 */
	public static List<String> getStartAndEnd(String dateType, String timeStart, String timeEnd){
		List<String> timeList = new ArrayList<>();
		if(dateType.equals(Days.TODAY)){
			timeList = Days.getTodayBeginEnd();
		}else if (dateType.equals(Days.YESTERDAY)) {
			timeList = Days.getYesterdayBeginEnd();
		}else if (dateType.equals(Days.THIS_WEEK)) {
			timeList = Days.getThisweekBeginEnd();
		}else if (dateType.equals(Days.LAST_WEEK)) {
			timeList = Days.getLastweekBeginEnd();
		}else if (dateType.equals(Days.THIS_MONTH)) {
			timeList = Days.getThisMonthBeginEnd();
		}else if (dateType.equals(Days.LAST_MONTH)) {
			timeList = Days.getLastMonthBeginEnd();
		}else if (dateType.equals(Days.SELF_CONTROL)) {//自己选择的时间
			timeList.add(timeStart);
			timeList.add(timeEnd);
		}
		return timeList;
	}
}
