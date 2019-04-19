package com.jiuyuan.util.constant;

import java.lang.reflect.Field;

import com.jiuyuan.util.constant.ConstantBinder;


/* *
 *类名：BankCardPayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：1.0
 *日期：2016-06-21
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。

 */


public class DateConstants {
    static {
        ConstantBinder.bind(DateConstants.class, "UTF8", "/date_constants.properties");
    }

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    
    public static int SECONDS_FOREVER;
    
    public static int SECONDS_PER_MINUTE;

    public static int SECONDS_FIVE_MINUTES;

    public static int SECONDS_TEN_MINUTES;

    public static int SECONDS_PER_HOUR;

    public static int SECONDS_PER_DAY;

    public static int SECONDS_OF_TWO_WEEKS;
    
    public static int SECONDS_PER_YEAR;


    public static void main(String[] args) throws Exception {
        Field[] fields = DateConstants.class.getDeclaredFields();
        for (Field field : fields) {
        }
    }
}
