package com.finace.miscroservice.commons.utils.tools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName:     TimeUtil.java
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-8-15 上午11:08:20
 * <b>Copyright (c)</b> 一桶金版权所有 <br/>
 */
public class TimeUtil {

	/**
	 * 将秒转换成时间
	 * @param times
	 * @return
	 */
	public static Date getDate(String times) {
		long time = Long.parseLong(times);
		return new Date(time*1000);
	}
	
	public static String getStr(String times){
		if(times!=null && !times.equals("")){
		long time = Long.parseLong(times)*1000;
		  Timestamp ts = new Timestamp(time);  
          String tsStr = "";  
          DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
          tsStr = sdf.format(ts);  
		return tsStr;  
		}else{
			return "";
		}
	}

	
	public static String dateStr(String times) {
		return dateStr(getDate(times));
	}
	
	/**
	 * 
	 * @Title: getDateStrYYYYMMDD 
	 * @param @param times
	 * @param @param formatstr
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
    public static String getDateStrYYYYMMDD(String times, String formatstr) {
        return getdateStr(getDate(times), formatstr);
    }
	
	public static String dateStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String str = format.format(date);
		return str;
	}
	
	/**
	 * 获取日期格式
	 * @Title: getdateStr 
	 * @param @param date
	 * @param @param formatstr
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
    public static String getdateStr(Date date, String formatstr) {
        SimpleDateFormat format = new SimpleDateFormat(formatstr);
        String str = format.format(date);
        return str;
    }
	
    public static String getNow(){
        
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String str = format.format(now);
        return str;
        
    }
	
	public static String getNowTimeStr(){
		String str=Long.toString(System.currentTimeMillis() / 1000);
		return str;
	}
	
	public static String getTime(String time) throws ParseException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    Date date = sdf.parse(time);  
		String times =String.valueOf(date.getTime() / 1000);
		return times;
	}

	/**
	 * 
	 * @Description:  剩余时间
	 * @param:        @param time
	 * @param:        @param datevalue
	 * @param:        @return   
	 * @return:       String   
	 * @throws
	 */
	public static String getRemainTime(String time, String datevalue) {

		String tt="不详";
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		/** 系统时间 */
	    String str1=sdf.format(new Date());
	    Calendar cal1=Calendar.getInstance();
	    try {
			cal1.setTime(sdf.parse(str1));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
	    long l=Long.valueOf(time)*1000+(Integer.valueOf(datevalue)*1000*60*60*24)-cal1.getTimeInMillis();
	    
	    int days=new Long(l/(1000*60*60*24)).intValue()+1;
	   
	   
	    if(days==1){
	     tt="最后一天";
	    }
	    
	    else if(days==2){
	     tt="最后两天";
	    }

	    else if(days==3){
	     tt="最后三天";
	    }
	   
	   
	    else if(days<0){
	     tt="已结束";
	    }
	   
	    else{
	     tt="还剩"+days+"天";
	    }
		return tt;
		
	}
	
	
	/**
	 * +
	 * 
	 * @Description: 判断是否过期
	 * @param:        @param time
	 * @param:        @param datevalue
	 * @param:        @return   
	 * @return:       boolean   
	 * @throws
	 */
	public static boolean isOver(String time, String datevalue) {
		if(time.equals(""))
		{
			return true;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		/** 系统时间 */
	    String str1=sdf.format(new Date());
	    Calendar cal1=Calendar.getInstance();
	    try {
			cal1.setTime(sdf.parse(str1));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
	    long l=Long.valueOf(time)*1000+(Integer.valueOf(datevalue)*1000*60*60*24)-cal1.getTimeInMillis();
	    
	    int days=new Long(l/(1000*60*60*24)).intValue()+1;
	    
	    if(days<0){
	    	return true;
	    }else{
	    	return false;
	    }
		
	}
	
	/**
	 * 是否提前
	 * @Title: isAhead 
	 * @param @param time
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
    public static boolean isAhead(String time, int datevalue) {
        if(time.equals(""))
        {
            return true;
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        
        /** 系统时间 */
        String str1=sdf.format(new Date());
        Calendar cal1=Calendar.getInstance();
        try {
            cal1.setTime(sdf.parse(str1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        long l=Long.valueOf(time)*1000-(Integer.valueOf(datevalue)*1000*60*60*24)-cal1.getTimeInMillis();
        
        int days=new Long(l/(1000*60*60*24)).intValue()+1;
        
        if(days<0){
            return false;
        }else{
            return true;
        }
        
    }
	
	/**
	 * 传入时间加上参数获得之后的时间 ---传入时只用传入一个即可，多传入不做计算,其他参数传入0
	 * @param timeStr   传入时间类型 1406703365 time 
	 * @param month     获取传入时间几个月后的时间。
	 * @param day       获取传入时间几个天后的时间。
	 * @param hour	        获取传入时间几个小时后的时间
	 * @param minute    获取传入时间几分钟后的时间
	 * @param second    获取传入时间几秒后的时间
	 * @return    返回参数类型 yyyy-MM-dd HH:mm:ss
	 */
	public static  String getEndTime(String timeStr,Integer month,Integer day,Integer hour,Integer minute,Integer second){
		String strDate="";
		try {
			String time ="";
				Calendar calendar = Calendar.getInstance();
				if(null!=timeStr)
					time =   TimeUtil.getStr(timeStr.trim());
				else
			        time =   TimeUtil.getStr(System.currentTimeMillis() / 1000+"");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
				Date date = sdf.parse(time);
				calendar.setTime(date);
				if(month>0 && day==0 && hour==0 && minute==0 && second==0){
					calendar.add(Calendar.MONTH, month);
				}else if(month==0 && day>0 && hour==0 && minute==0 && second==0){
					calendar.add(Calendar.DATE, day);
				}else if(month==0 && day==0 && hour>0 && minute==0 && second==0){
					calendar.add(Calendar.HOUR, day);
				}else if(month==0 && day==0 && hour==0 && minute>0 && second==0){
					calendar.add(Calendar.MINUTE, minute);
				}else if(month==0 && day==0 && hour==0 && minute==0 && second>0){
					calendar.add(Calendar.SECOND, second);
				}else if(month==0 && day<0 && hour==0 && minute==0 && second==0){
					calendar.add(Calendar.DAY_OF_MONTH , day);
				}
				strDate = sdf.format(calendar.getTime()); 
		} catch (ParseException e) {
			e.printStackTrace();
		}           
		return strDate;
	}


	public static void main(String[] args) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-01 00:00:00");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		TimeUtil.getTime(date);
		System.out.println(date);
	}
}
