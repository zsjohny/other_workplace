package com.wuai.company.util.comon;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.util.Arith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/3.
 */
public class SimpDateEndTime implements SimpDate {
    @Override
    public String endDate(String startTime, Double hour, Integer minute) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date = simpleDateFormat.parse(startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        String doubleString = String.valueOf(hour).split(".")[1];
        String s = String.valueOf(hour).split("[.]")[0];

        Integer start= Integer.valueOf(s);
        String e =String.valueOf(hour).split("[.]")[1];
        Integer end= Integer.valueOf(e);
        if (end>0){
            calendar.add(Calendar.HOUR,start);  //对小时进行操作
            Double mi =Arith.divides(0,Arith.multiplys(0,end,60),10);
            minute=minute+mi.intValue();
            calendar.add(Calendar.MINUTE,minute);  //对分钟进行操作
        }else {
            calendar.add(Calendar.HOUR,start);  //对小时进行操作
            calendar.add(Calendar.MINUTE,minute);  //对分钟进行操作
        }

        String endTime = simpleDateFormat.format(calendar.getTime()); //把得到的日期格式化成字符串类型的时间
//            int res = startTime.compareTo(endTime);
        return endTime;
    }

    @Override
    public Map<String,String> transformTime(String time) throws ParseException {

//        String[] time1 = time.split("-");
//        String yyyy=time1[0];
//        String MM = time1[1];
//        String[] aa = time1[2].split(":");
//
//        String[] sp1=time1[2].split(" ");
//        String dd = sp1[0];
////        String time2 = time1[2].substring(time1[2].length()-6,time1[2].length()).trim();
//        String[] time3 = sp1[1].split(":");
//        String HH = time3[0];
//        String mm = time3[1];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = dateFormat.parse(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH)+1;//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR_OF_DAY);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天


        Map<String,String> map = new HashMap<String,String>();
        map.put("yyyy", String.valueOf(year));
        map.put("MM", String.valueOf(month));
        map.put("dd", String.valueOf(day));
        map.put("HH", String.valueOf(hour));
        map.put("mm", String.valueOf(minute));

        return map;
    }


    @Override
    public Map<String,Object> cycleTimeChange(String time) throws ParseException {
       String weeks = time.substring(0,2);
       Integer week = null;
       Integer weekDay = null;
       switch (weeks){
           case "周一":
               week=1;
               weekDay=2;
               break;
           case "周二":
               week=2;
               weekDay=3;
               break;
           case "周三":
               week=3;
               weekDay=4;
               break;
           case "周四":
               week=4;
               weekDay=5;
               break;
           case "周五":
               week=5;
               weekDay=6;
               break;
           case "周六":
               week=6;
               weekDay=7;
               break;
           case "周日":
               week=7;
               weekDay=1;
               break;
       }
       String startTime = time.substring(2).trim();
        String[] startTimeList = startTime.split(":");
        String HH = startTimeList[0];
        String mm = startTimeList[1];
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("week",week);
        map.put("weekDay",weekDay);
        map.put("HH",HH);
        map.put("mm",mm);
       return map;
    }

    @Override
    public String cycleTimeChangeCommon(String time) throws ParseException {
        String weeks = time.substring(0,2);
        SimpleDateFormat sdfm = new SimpleDateFormat("EEEE");
        String startTime = time.substring(2).trim();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        String e =sdfm.format(date);
        String today=smf.format(date);
//        if (e.substring(2).equals(time.substring(1,2))){
//            time= simpleDateFormat.format(date)+" "+startTime;
//            return time;
//        }
        Integer week = null;
        switch (weeks){
            case "周一":
                week=2;
                break;
            case "周二":
                week=3;
                break;
            case "周三":
                week=4;
                break;
            case "周四":
                week=5;
                break;
            case "周五":
                week=6;
                break;
            case "周六":
                week=7;
                break;
            case "周日":
                week=1;
                break;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer week2 =calendar.get(Calendar.DAY_OF_WEEK);
        Integer interval =null;
        if (week>=week2){
             interval=week-week2;
        }else if (week<week2){
            interval=7-week2+week;
        }
        SimpDate simpDate = SimpDateFactory.endDate();
        System.out.println(e);
        Map<String,String> map = simpDate.transformTime(today);




        Integer dd = Integer.valueOf(map.get("dd"))+interval;
        Integer yyyy =Integer.valueOf(map.get("yyyy"));
        Integer mm =Integer.valueOf(map.get("MM"))-1;
        calendar.set(yyyy,mm,dd);
//        calendar.set(2017,12,9);
        time = simpleDateFormat.format(calendar.getTime()) +" "+startTime; //把得到的日期格式化成字符串类型的时间
        
       return time;
    }

    @Override
    public String cycleToCommon(String time) throws ParseException {
        return null;
    }

    @Override
    public Boolean weekTime(String time) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String e =simpleDateFormat.format(date);

        if (e.substring(2).equals(time.substring(1,2))){

            return true;
        }
        return false;
    }

    public static void main(String[] args) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String t = "周日  9:00";
//        try {
//           Date a= simpleDateFormat.parse(t);
//            System.out.println(simpleDateFormat.format(a));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE HH:mm");
//        Date date = new Date();
//        String e =simpleDateFormat.format(date);
//        System.out.println(e);
//        String a="周一";
//        System.out.println(e.substring(2));
//        System.out.println(a.substring(1));
//        System.out.println(e.substring(2).equals(a.substring(1)));
//        String aaa= "周五  12:03:00";
        SimpDateEndTime simpDateEndTime = new SimpDateEndTime();
        try {
            String map=simpDateEndTime.cycleTimeChangeCommon(t);
            System.out.println(map);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        Calendar calendar = Calendar.getInstance ();
//        calendar.set (2014, 3, 28);
//        System.out.println(calendar.getTime());
//
//        System.out.println( aaa.substring(0,2)+"==");
//        System.out.println( aaa.substring(2).trim()+"==");
//
//        String a= "yyyy-MM-dd HH:mm:ss";
//        String[] b = a.split("-");
//        System.out.println(b[0]);
//        System.out.println(b[1]);
//        String[] c = b[2].split(" ");
//        System.out.println( b[2].substring(b[2].length()-8,b[2].length()).trim());
//        System.out.println( b[2].substring(0,2)+"--");
//        System.out.println(c[0]);
//
//
//        String[] d = c[1].split(":");
//        System.out.println(d[0]);
//        System.out.println(d[1]);
//        System.out.println(d[2]);
    }
}
