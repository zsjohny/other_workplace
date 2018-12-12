package com.onway.baib.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.onway.common.lang.DateUtils;

public class DataExtraUtils extends DateUtils{
    
    
    public static void main(String[] args) {
        System.out.println(isInMonth(new Date()));
    }
    
    
    public static boolean isInMonth(Date date){
        
       String now= format(date, webFormat);
       
       String firstDay=getMouthFirstDay();
       
       String lastDay=getMouthLastDay();
       
       if(now.compareTo(firstDay)<0||now.compareTo(lastDay)>0){
           return false;
       }
        
        return true;
        
    }
    
    public static String getMouthFirstDay(){
      //规定返回日期格式
      SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
      Calendar calendar=Calendar.getInstance();
      Date theDate=calendar.getTime();
      GregorianCalendar gcLast=(GregorianCalendar)Calendar.getInstance();
      gcLast.setTime(theDate);
      //设置为第一天
      gcLast.set(Calendar.DAY_OF_MONTH, 1);
      String day_first=sf.format(gcLast.getTime());
      return day_first;
      }
    
    
      //2、获取当月最后一天
      public static String getMouthLastDay(){
      //获取Calendar
      Calendar calendar=Calendar.getInstance();
      //设置日期为本月最大日期
      calendar.set(Calendar.DATE, calendar.getActualMaximum(calendar.DATE));
      //设置日期格式
      SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
      String ss=sf.format(calendar.getTime());
      return ss;
      }

}
