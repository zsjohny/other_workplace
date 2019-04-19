package com.e_commerce.miscroservice.store.utils;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具类
 * BizUtil.java
 *
 * @author Aison
 * @version V1.0
 * @date 2018年4月19日 下午9:54:53
 * @Copyright 玖远网络
 */
public class BizUtil {

    public static final Integer VERSION372 = 372;

    /**
     * isNotEmpty
     * 判断参数是否为null 或者空字符串..只要有一个参数为空则返回false
     *
     * @param arg 可变的参数
     * @return boolean
     */
    public static boolean isNotEmpty(Object... arg) {
        for (Object o : arg) {
            if (o == null || StringUtils.isBlank (o.toString ().trim ())) {
                return false;
            }
        }
        return true;
    }


    /**
     * isNotEmpty
     * 判断参数是否为null 或者空字符串..只要有一个参数为空则返回false
     *
     * @param arg 可变的参数
     * @return boolean
     */
    public static boolean isEmpty(Object... arg) {
        return ! isNotEmpty (arg);
    }

    /**
     * hasEmpty
     * 只要有一个为空则返回true
     *
     * @param arg 可变的参数
     * @return boolean
     */
    public static boolean hasEmpty(Object... arg) {
        for (Object o : arg) {
            if (o == null || StringUtils.isBlank (o.toString ().trim ())) {
                return true;
            }
        }
        return false;
    }



    /**
     * 过滤参数防止sql注入
     *
     * @param sqlStr sqlStr
     * @date 2018/4/23 16:42
     * @author Aison
     */
    public static String filterSqlString(String sqlStr) {

        if (StringUtils.isEmpty (sqlStr)) {
            return sqlStr;
        }
        sqlStr = sqlStr.replace ("'", "''");
        sqlStr = sqlStr.replace ("/", "//");
        sqlStr = sqlStr.replaceAll ("%", "/%");
        return sqlStr.trim ();
    }


    /**
     * 是
     *
     * @param date date
     * @date 2018/5/16 15:38
     * @author Aison
     */
    public static String formart(Long date) {
        Date newDate = new Date (date);
        return new SimpleDateFormat ("HH:mm:ss").format (newDate);
    }


    /**
     * 获取当月第一天时间戳
     *
     * @param
     * @date 2018/4/24 10:03
     * @author Aison
     */
    public static Long mothFirstDay() {
        Calendar cale = Calendar.getInstance ();
        // 获取前月的第一天
        cale = Calendar.getInstance ();
        cale.add (Calendar.MONTH, 0);
        cale.set (Calendar.DAY_OF_MONTH, 1);
        cale.set (Calendar.HOUR_OF_DAY, 0);
        cale.set (Calendar.MINUTE, 0);
        cale.set (Calendar.SECOND, 0);
        cale.set (Calendar.MILLISECOND, 0);
        return cale.getTime ().getTime ();
    }

    /**
     * 将一个对象转换成map
     *
     * @param ob ob
     * @date 2018/4/27 14:06
     * @author Aison
     */
    public static Map<String, Object> bean2Map(Object ob) {
        Map<String, Object> map = new HashMap<> ();
        if (ob == null) {
            return map;
        }
        Field[] field = ob.getClass ().getDeclaredFields ();
        String get;
        Object val;
        for (Field f : field) {
            f.setAccessible (true);
            get = "get" + f.getName ().substring (0, 1).toUpperCase () + f.getName ().substring (1);
            val = getVal (ob.getClass (), ob, get);
            map.put (f.getName (), val);

        }
        return map;
    }


    /**
     * 将一个对象转换成map
     *
     * @param ob ob
     * @date 2018/4/27 14:06
     * @author Aison
     */
    public static Map<String, Object> bean2MapAllField(Object ob) {
        Map<String, Object> map = new HashMap<> ();
        if (ob == null) {
            return map;
        }
        Field[] field = getAllFields (ob.getClass ());
        Object val;
        for (Field f : field) {
            f.setAccessible (true);
            try {
                val = f.get (ob);
                String key = f.getName ().substring (0, 1).toLowerCase () + f.getName ().substring (1);
                map.put (key, val);
            } catch (Exception e) {
                e.printStackTrace ();
                map.put (f.getName (), "");
            }

        }
        return map;
    }

    /**
     * 将一个对象转换成map
     *
     * @param ob ob
     * @date 2018/4/27 14:06
     * @author Aison
     */
//    public static Map<String, Object> bean2MapAllField(Object ob, boolean notNull, MyJobOld<Map<String, Object>, String> myJobOld) {
//        Map<String, Object> map = new HashMap<> ();
//        if (ob == null) {
//            return map;
//        }
//        Field[] field = getAllFields (ob.getClass ());
//        Object val;
//        for (Field f : field) {
//            f.setAccessible (true);
//            try {
//                String key = f.getName ().substring (0, 1).toLowerCase () + f.getName ().substring (1);
//                val = f.get (ob);
//                if (notNull) {
//                    if (val != null) {
//                        map.put (key, val);
//                    }
//                }
//                else {
//                    map.put (key, val);
//                }
//                if (myJobOld != null) {
//                    myJobOld.execute (map, key);
//                }
//            } catch (Exception e) {
//                e.printStackTrace ();
//                map.put (f.getName (), "");
//            }
//
//        }
//        return map;
//    }


    /**
     * 将一个对象转换成map
     *
     * @param list ob
     * @date 2018/4/27 14:06
     * @author Aison
     */
//    public static List<Map<String, Object>> listBean2listMap(List list, boolean notNull, MyJobOld<Map<String, Object>, String> jobOld) {
//
//        List<Map<String, Object>> map = new ArrayList<> (20);
//        list.forEach (action -> map.add (bean2MapAllField (action, notNull, jobOld)));
//        return map;
//    }

    /**
     * 获取某个对象的所有属性
     *
     * @param clazz clazz
     * @return Field[]    返回类型
     * @author Aison
     */
    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<> ();
        while (clazz != null) {
            fieldList.addAll (new ArrayList<> (Arrays.asList (clazz.getDeclaredFields ())));
            clazz = clazz.getSuperclass ();
        }
        Field[] fields = new Field[fieldList.size ()];
        fieldList.toArray (fields);
        return fields;
    }


    /**
     * 将ids转换成 数组
     *
     * @param strIds strIds
     * @author Aison
     * @date 2018/6/20 11:28
     */
//    public static List<Long> strIds2LongIds(String strIds) {
//
//        if (hasEmpty (strIds)) {
//            throw BizException.defulat ().msg ("ids为空");
//        }
//        String[] ids = strIds.split (",");
//        List<Long> longs = new ArrayList<> ();
//        for (String id : ids) {
//            longs.add (Long.valueOf (id));
//        }
//        return longs;
//    }

    /**
     * 通过反射获取某个方法的返回值
     *
     * @param clazz  clazz
     * @param ob     ob
     * @param method method
     * @date 2018/4/27 14:07
     * @author Aison
     */
    public static Object getVal(Class<?> clazz, Object ob, String method) {
        try {
            return clazz.getDeclaredMethod (method).invoke (ob);
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 加减时间单位是天
     *
     * @param date 某个时间
     * @param day  天数
     * @date 2018/5/3 9:30
     * @author Aison
     */
    public static Date addDay(Date date, Integer day) {
        Calendar calendar = Calendar.getInstance ();
        calendar.setTime (date);
        calendar.add (Calendar.DAY_OF_MONTH, day);
        date = calendar.getTime ();
        return date;
    }

    /**
     * 加减时间 单位是小时
     *
     * @param date date
     * @param hour hour
     * @date 2018/5/3 9:30
     * @author Aison
     */
    public static Date addHour(Date date, Integer hour) {

        Calendar calendar = Calendar.getInstance ();
        calendar.setTime (date);
        calendar.add (Calendar.HOUR_OF_DAY, hour);
        date = calendar.getTime ();
        return date;
    }

    @SuppressWarnings( "rawtypes" )
    private static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType (){
            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }


    /**
     * getFullException
     * 获取完整的异常信息
     *
     * @param e e
     * @return String
     */
    public static String getFullException(Throwable e) {
        StringWriter sw = new StringWriter ();
        PrintWriter pw = new PrintWriter (sw, true);
        e.printStackTrace (pw);
        pw.flush ();
        sw.flush ();
        return sw.toString ();
    }

    /**
     * base64 编码转成string
     *
     * @param base64 base64
     * @date 2018/5/16 16:46
     * @author Aison
     */
    public static String base64Decode(String base64) {
        try {
            BASE64Decoder decoder = new BASE64Decoder ();
            return new String (decoder.decodeBuffer (base64));
        } catch (Exception e) {
            e.printStackTrace ();
            return "";
        }

    }


    /**
     * 获取版本号
     *
     * @param request request
     * @author Aison
     * @date 2018/5/25 18:12
     */
    public static Integer getVersion(HttpServletRequest request) {
        return getVersion (request.getHeader ("X-User-Agent"));
    }


    /**
     * 获取app版本
     *
     * @param useAge userAge
     * @author Aison
     * @date 2018/5/25 10:57
     */
    public static Integer getVersion(String useAge) {
        if (hasEmpty (useAge)) {
            return 0;
        }
        else {
            try {
                String[] platform = useAge.split ("\\|");
                int len = 2;
                if (platform.length >= len) {
                    String[] versions = platform[1].split ("=");
                    String version = versions[1].replace (".", "");
                    return Integer.valueOf (version);
                }
                else {
                    return 0;
                }
            } catch (Exception e) {
                return 0;
            }
        }

    }

    /**
     * 保留几位小数
     *
     * @param d     数字
     * @param point 几位小数
     * @author Aison
     * @date 2018/5/29 10:36
     */
    public static double savepoint(Double d, Integer point) {

        point = point == null ? 2 : point;
        d = d == null ? 0 : d;
        return new BigDecimal (d).setScale (point, BigDecimal.ROUND_HALF_UP).doubleValue ();
    }

    /**
     * 获取请求的map
     *
     * @param request request
     * @author Aison
     * @date 2018/6/8 15:16
     */
    public static Map<String, Object> getRequestMap(HttpServletRequest request) {

        Map<String, String[]> map = request.getParameterMap ();
        Map<String, Object> maps = new HashMap<> (10);
        String[] val;
        for (Map.Entry<String, String[]> en : map.entrySet ()) {
            val = en.getValue ();
            if (val != null && val.length > 0) {
                // 单个属性
                if (val.length == 1 && ! StringUtils.isBlank (val[0])) {
                    maps.put (en.getKey (), BizUtil.filterSqlString (val[0]));
                }
                // 数组属性
                if (val.length > 1) {
                    maps.put (en.getKey ().replace ("[]", ""), en.getValue ());
                }
            }
        }
        return maps;
    }


    /**
     * 判断是否是整数
     *
     * @param bigDecimal
     * @return boolean
     * @author Charlie(唐静)
     * @date 2018/6/21 18:25
     */
    public static boolean isInt(BigDecimal bigDecimal) {
        double decimals = bigDecimal.doubleValue () % 1;
        return decimals == 0;
    }


    public static void debug(String msg) {
        System.out.println ("debug......................................................................");
        System.out.println ("    " + msg);
        System.out.println ("......................................................................debug");
    }


    public static void test(Integer version, String description) {}


    /**
     * @param
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/15 18:40
     */
    public static void todo(String str) {
        //
    }


    public static void main(String[] args) {
        String url = "http://localhost:8074/distribution/cashOutIn/addDstbFromOrder";
        Map<String, Object> map = new HashMap<> (4);
        map.put ("orderNumber", 123456789);
        map.put ("memberId", 1);
        map.put ("storeId", 1);
        map.put ("realPay", 100);
        map.put ("payTime", 0);
        map.put ("wx2DstbSign", new Md5Hash(123456789 + "xiaochengxu2dstb").toString ());
//        String response = HttpClientUtils.get (url, map);

//        String url = "http://localhost:8074/distribution/cashOutIn/dstbSuccess";
//        Map<String, Object> map = new HashMap<> (6);
//        map.put ("orderNumber", 123456789);
//        map.put ("orderSuccessTime", 0);
//        String sign = new Md5Hash (123456789 + "xiaochengxu2dstb").toString ();
//        map.put ("wx2DstbSign", sign);
//        String response = HttpClientUtils.get (url, map);
    }
}
