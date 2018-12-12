package test;

import java.text.ParseException;
import java.util.Date;

import com.onway.common.lang.DateUtils;
import com.onway.common.lang.Money;

public class MoneyTest {
  public static void main(String[] args) throws ParseException {
    Money money=new Money(100);
    String money1=money.toSimpleString();
    System.out.println("-------"+money1);
    String cTime="2016-12-29 16:26:30";
    //判断预约时间和当天时间对比，预约时间必须大于当前日期，否则报错
    long diffDays=DateUtils.getDiffDays(DateUtils.parseDate(cTime, "yyyy-MM-dd"), new Date());
    if(diffDays<0){
        System.out.println("预约时间无效");
    }
}
}
