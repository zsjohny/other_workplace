package com.wuai.company.util.comon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hyf on 2018/1/22.
 */
public class TimeUtil {
    /**
     * 当前时间yyyy-MM-dd HH:mm
     * @return
     */
    public static String currentTime(){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        return date;
    }
    /**
     * changeStringTimeToDate
     * 当前时间yyyy-MM-dd HH:mm
     * @return
     */
    public static Date changeStringTimeToDate(String time){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 比较日期大小
     *
     * @param time1
     * @param time2
     * @return 0 相等  负数 time1 小于 time2  正数 time1大于time2
     */
    public static Integer compareTime(String time1 , String time2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long date1 = null;
        Long date2 = null;
        try {
            date1 = simpleDateFormat.parse(time1).getTime();
            date2 = simpleDateFormat.parse(time2).getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int compare = date1.compareTo(date2);
        return compare;
    }
    /**
     * 今日yyyy-MM-dd
     */
    public static String today(){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    /**
     * 时间计算
     * @param hour 小时
     * @param minute 分钟
     * @return
     */
    public static String afterTime(String date ,Integer hour,Integer minute){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //计算日期
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR,hour);
        calendar.add(Calendar.MINUTE,minute);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static void main(String[] args) {
        String day = "2018-03-14 14:00";
        System.out.println(day.substring(0,10));
    }
}
