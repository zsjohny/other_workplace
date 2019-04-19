package com.jiuy.base.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.IgnoreCopy;
import com.jiuy.base.annotation.ModelName;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.model.MyJob;
import org.apache.commons.lang3.StringUtils;

import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 * request 的工具类
 **/

public class Biz{


    /**
     * getPageMap
     * 分页map 如果查询使用的是map作为参数则通过调用此方法可以实现分页
     *
     * @param startIndex
     * @param pageSize
     * @return Map<String       ,       Object>
     * @throws
     */
    public static Map<String, Object> getPageMap(Integer startIndex, Integer pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);
        PageHelper.startPage(startIndex, pageSize);
        return map;
    }


    /**
     * getPageMap
     * 分页map 如果查询使用的是map作为参数则通过调用此方法可以实现分页
     *
     * @param @param map
     * @return Map<String       ,       Object>
     * @throws
     */
    public static Map<String, Object> getPageMap(Map<String, Object> map) {

        if (Biz.isNotEmpty(map.get("offset"), map.get("limit"))) {
            Integer offset = Integer.valueOf(map.get("offset").toString());
            offset = offset < 1 ? 1 : offset;
            PageHelper.startPage(offset, Integer.valueOf(map.get("limit").toString()));
        }
        if (Biz.isNotEmpty(map.get("orderBy"))) {
            PageHelper.orderBy(map.get("orderBy").toString());
        }
        return map;
    }

    /**
     * 统一处理异常
     *
     * @param e
     * @date: 2018/4/20 10:11
     * @author: Aison
     */
    public static ResponseResult exceptionHandler(Throwable e) {

        e.printStackTrace();
        if (e instanceof BizException) {
            BizException be = (BizException) e;
            return new ResponseResult().setCode(be.getCode() + "").setMsg(be.getMsg());
        }
        else {
            return new ResponseResult("400", "系统繁忙请稍后再试");
        }
    }


    /**
     * isEmpty
     * 判断一个或者多个参数是否为空或者为空字符串，只要一个为空则返回false
     *
     * @param @param  arg
     * @param @return
     * @return boolean
     * @throws
     */
    public static boolean isNotEmpty(Object... arg) {
        for (Object o : arg) {
            if (o == null || StringUtils.isBlank(o.toString().trim())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否有空如果有空则返回true
     *
     * @param arg 变长字段
     * @date: 2018/4/24 15:24
     * @author: Aison
     */
    public static boolean hasEmpty(Object... arg) {
        for (Object o : arg) {
            if (o == null || StringUtils.isBlank(o.toString().trim())) {
                return true;
            }
        }
        return false;
    }


    /**
     * filterSqlString
     * sql注入过滤
     *
     * @param @param  sqlStr
     * @param @return
     * @return String
     * @throws
     */
    public static String filterSqlString(String sqlStr) {
        if (StringUtils.isEmpty(sqlStr)) {
            return sqlStr;
        }
        sqlStr = sqlStr.replace("'", "''");
        sqlStr = sqlStr.replace("/", "//");
        sqlStr = sqlStr.replaceAll("%", "/%");
        return sqlStr.trim();
    }

    /**
     * singlObjectStr
     * 将对象转换成json 英文属性转换成中文
     *
     * @param ob
     * @return StringBuffer
     * @throws
     */
    public static StringBuffer singlObjectStr(Object ob) {
        StringBuffer sb = new StringBuffer();
        Class<?> clazz = ob.getClass();
        ModelName modelName = clazz.getAnnotation(ModelName.class);
        sb.append(modelName.name().replace("表", "")).append("【");
        Field[] field = clazz.getDeclaredFields();
        FieldName fn = null;
        Object val = null;
        for (Field f : field) {
            f.setAccessible(true);
            try {
                f.get(ob);
                fn = f.getAnnotation(FieldName.class);
                val = f.get(ob);
                if (val instanceof Date) {
                    val = formatDate((Date) val, null);
                }
                String fieldName = fn == null ? f.getName() : fn.name();
                sb.append(fieldName).append(":").append(val).append(", ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.append("】");
    }

    /**
     * compareVoBuffer
     * 比较连个对象的差异 返回一个String buffer
     *
     * @param old
     * @param newOb
     * @return StringBuffer
     * @throws
     */
    public static StringBuilder compareVoBuffer(Object old, Object newOb) {
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = old.getClass();
        Field[] field = clazz.getDeclaredFields();
        String get;
        String olds;
        String nstr;
        try {
            for (Field f : field) {
                f.setAccessible(true);
                get = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                olds = getVal(clazz, old, get, true);
                nstr = getVal(clazz, newOb, get, true);
                if (! olds.equals(nstr) && ! nstr.trim().equals("")) {
                    sb.append(f.getAnnotation(FieldName.class).name()).append(" :从 [").append(olds).append("] 修改到 [")
                            .append(nstr).append("];");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }


    /**
     * getVal
     * 通过反射获取一个对象的某个属性的值
     *
     * @param clazz
     * @param ob
     * @param method
     * @return Object
     * @throws
     */
    public static Object getVal(Class<?> clazz, Object ob, String method) {
        try {
            return clazz.getDeclaredMethod(method).invoke(ob);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * getVal
     * 通过反射获取某个对象的某个属性的值
     *
     * @param clazz
     * @param ob
     * @param method
     * @param isString
     * @return String
     * @throws
     */
    public static String getVal(Class<?> clazz, Object ob, String method, boolean isString) {
        try {
            Object val = clazz.getDeclaredMethod(method).invoke(ob);
            if (val instanceof Date) {
                val = formatDate((Date) val, null);
            }
            return val.toString();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * obToMap
     * 将对象转换成一个map
     *
     * @param ob
     * @param
     * @return Map<String       ,       Object>
     * @throws
     */
    public static Map<String, Object> obToMap(Object ob) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ob == null) {
            return map;
        }
        Field[] field = ob.getClass().getDeclaredFields();
        String get = "";
        Object val = null;
        for (Field f : field) {
            f.setAccessible(true);
            get = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
            val = getVal(ob.getClass(), ob, get);
            map.put(f.getName(), val);
        }
        return map;
    }


    /**
     * obToMap
     * 将对象转换成一个map
     *
     * @param ob
     * @param flag 如果是空的值则不需要放入map
     * @return Map<String       ,       Object>
     * @throws
     */
    public static Map<String, Object> obToMap(Object ob, boolean flag) {

        Map<String, Object> map = new HashMap<String, Object>();
        if (ob == null) {
            return map;
        }
        Field[] field = ob.getClass().getDeclaredFields();
        String get = "";
        Object val = null;
        for (Field f : field) {
            f.setAccessible(true);
            get = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
            val = getVal(ob.getClass(), ob, get);
            if (flag) {
                if (val != null) {
                    map.put(f.getName(), val);
                }
            }
            else {
                map.put(f.getName(), val);
            }
        }
        return map;
    }

    /**
     * listOb2ListMap
     * 将一个list里面的所有对象都转换成map
     *
     * @param obs
     * @param flag 空值是否需要转换
     * @return List<Map       <       String       ,       Object>>
     * @throws
     */
    public static List<Map<String, Object>> listOb2ListMap(List<?> obs, boolean flag) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object ob : obs) {
            list.add(obToMap(ob, flag));
        }
        return list;
    }

    /**
     * rad
     * 转换弧度
     *
     * @param d
     * @return double
     * @throws
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public synchronized static String getRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    /**
     * formatDate
     * 时间格式转换
     *
     * @param date   日期
     * @param format 格式
     * @return String
     * @throws
     */
    public static String formatDate(Date date, String format) {
        format = ! isNotEmpty(format) ? GlobalsFields.DATE_FORMAT : format;
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * subDate
     * 两个时间相减返回毫秒
     *
     * @param begin
     * @param end
     * @return Long
     * @throws
     */
    public static Long subDate(Date begin, Date end) {
        return end.getTime() - begin.getTime();
    }


    /**
     * getBeforDay
     * 获取昨天的时间
     *
     * @param @param  date
     * @param @return
     * @return Date
     * @throws
     */
    public static Date getBeforDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, - 1);
        date = calendar.getTime();
        return date;
    }


    /**
     * addDate
     * 添加时间 单位为小时
     *
     * @param @param date
     * @param @param i
     * @return Date
     * @throws
     */
    public static Date addDate(Date date, Integer i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, i);
        date = calendar.getTime();
        return date;
    }

    /**
     * 添加时间 单位为分钟
     **/
    public static Date addMinute(Date date, Integer i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, i);
        date = calendar.getTime();
        return date;
    }

    public static Date afterDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 两个时间相减 返回秒
     **/
    public static Integer subSec(Date beginTime, Date endTime) {

        Long end = endTime.getTime();
        Long begin = beginTime.getTime();
        Long pix = end - begin;
        Long hour = (pix / (1000));

        return hour.intValue();

    }

    /**
     * 两个时间相减 返回分钟
     **/
    public static Integer subMin(Date beginTime, Date endTime) {

        Long end = endTime.getTime();
        Long begin = beginTime.getTime();
        Long pix = end - begin;
        Long hour = (pix / (60 * 1000));

        return hour.intValue();

    }

    /**
     * 两个时间相减 返回小时
     **/
    public static Integer subHour(Date beginTime, Date endTime) {

        Long end = endTime.getTime();
        Long begin = beginTime.getTime();
        Long pix = end - begin;
        Long hour = (pix / (60 * 60 * 1000));

        return hour.intValue();

    }

    /**
     * 获取时间 前后时间 添加年
     */
    public static Date getDateYear(Date date, Integer i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取时间 前后时间
     */
    public static Date getDate(Date date, Integer i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取时间 前后时间
     */
    public static String getDateStr(Date date, Integer i, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return new java.text.SimpleDateFormat(format).format(date);
    }

    public static String dateBeforToStr(String str, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, - 1);
            date = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(date);
    }

    public static String dateAfterToStr(String str, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(date);
    }

    /**
     * 根据文件名获取配置指定日期前的文件列表
     *
     * @param fileList
     * @return
     */
    public static List<String> getDeleteList(List<String> fileList, int day) {
        List<String> list = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, - day);
        for (String fileName : fileList) {
            try {
                Date date = dateFormat.parse(fileName.substring(fileName.length() - 14, fileName.length() - 6));
                if (date.before(calendar.getTime())) {
                    list.add(fileName);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 减时间 单位为天
     **/
    public static String beforeDate(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - day);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    /**
     * 验证手机号码 1开头的11位数字
     **/

    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((1[0-9]))\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    /**
     * list2SQLString
     * 将数组里的值转换成sql能用的字符串， 例如：{"a","b","c"}转换成"('a','b','c')"
     *
     * @param strList
     * @return
     */
    public static String list2SQLString(List<String> strList) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(");
        if (strList == null || strList.isEmpty()) {
            buffer.append("''");
        }
        else {
            for (int i = 0; i < strList.size(); i++) {
                if (i + 1 == strList.size()) {
                    buffer.append("'").append(strList.get(i)).append("'");
                }
                else {
                    buffer.append("'").append(strList.get(i)).append("',");
                }
            }

        }
        buffer.append(") ");
        return buffer.toString();
    }

    /**
     * @author 艾成松 下划线转驼峰
     **/
    public static final String UNDERLINE = "_";

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        String[] chars = param.split(UNDERLINE);
        StringBuffer sb = new StringBuffer();
        for (String str : chars) {
            sb.append(str.substring(0, 1).toUpperCase()).append(str.substring(1).toLowerCase());
        }
        String sbstr = sb.toString();
        sbstr = sbstr.substring(0, 1).toLowerCase() + sbstr.substring(1);
        return sbstr;
    }

    /**
     * @author 艾成松 map的key大写转小写
     **/
    public static Map<String, Object> mapKey2Camel(Map<String, Object> map) {
        Map<String, Object> nMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> en : map.entrySet()) {
            nMap.put(underlineToCamel(en.getKey()), en.getValue());
        }
        map = null;
        return nMap;
    }

    /**
     * @author 艾成松 map的key大写转小写
     **/
    public static List<Map<String, Object>> mapKey2CamelList(List<Map<String, Object>> maps) {

        List<Map<String, Object>> nList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : maps) {
            nList.add(mapKey2Camel(map));
        }
        return nList;
    }

    /**
     * @author 艾成松 将null 转换未空的对象
     **/
    @SuppressWarnings( "unchecked" )
    public static Object NullToEmpty(Object object) {

        if (object == null) {
            return new ArrayList<Object>();
        }

        if (Map.class.isAssignableFrom(object.getClass())) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            for (Map.Entry<Object, Object> en : map.entrySet()) {
                if (en.getValue() == null) {
                    map.put(en.getKey(), "");
                }
            }
            return map;
        }
        else if (List.class.isAssignableFrom(object.getClass())) {
            List<Object> obs = (List<Object>) object;
            for (Object ob : obs) {
                NullToEmpty(ob);
            }
            return obs;
        }
        else {

            Field[] fs = object.getClass().getDeclaredFields();
            Object val = null;
            for (Field f : fs) {
                f.setAccessible(true);
                try {
                    val = f.get(object);
                    if (val == null) {
                        f.set(object, "");
                    }
                } catch (Exception e) {
                }
            }
            return object;
        }
    }

    /**
     * jsonStrToMap
     * 将一个json字符串转换成map
     *
     * @param json
     * @return Map<String       ,       Object>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public static Map<String, Object> jsonStrToMap(String json) {
        return new GsonBuilder().
                registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
                    if (src == src.longValue()) {
                        return new JsonPrimitive(src.longValue());
                    }
                    return new JsonPrimitive(src);
                }).create().fromJson(json, Map.class);
    }

    /**
     * jsonStr2Obj
     * 将一个json字符串转换成对象
     *
     * @param json
     * @param clazz
     * @param @return
     * @return T
     * @throws
     */
    public static <T> T jsonStr2Obj(String json, Class<? extends T> clazz) {
        return new Gson().fromJson(json, clazz);
    }


    /**
     * jsonStrToListMap
     * 将json字符串转换成listMap
     *
     * @param json
     * @return List<Map       <       String       ,       Object>>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public static List<Map<String, Object>> jsonStrToListMap(String json) {
        return new Gson().fromJson(json, List.class);
    }


    /**
     * jsonStrToListObject
     * 将一个json字符串转换成List object
     *
     * @param json
     * @param clazz 集合的class
     * @param item  集合里面元素的class
     * @return T
     * @throws
     */
    public static <T> List<T> jsonStrToListObject(String json, Class<? extends Collection> clazz, Class<T> item) {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Type type = type(clazz, item);
        return gson.fromJson(json, type);
    }

    @SuppressWarnings( "rawtypes" )
    private static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType(){
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
     * copyBean
     * 将source对象里面的值复制到target对象里面
     *
     * @param source 源对象
     * @param target 目标对象
     * @return void
     * @throws
     */
    public static void copyBean(Object source, Object target) {
        Field[] fields = getAllFields(target.getClass());
        Class<?> sourceClass = source.getClass();
        for (Field field : fields) {
            field.setAccessible(true);
            //忽略不需要复制的值
            IgnoreCopy ignoreCopy = field.getAnnotation(IgnoreCopy.class);
            if (ignoreCopy != null) {
                continue;
            }
            String fieldName = field.getName();
            Object val = getVal(sourceClass, source, "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            try {
                field.set(target, val);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * obToJson
     * 对象转换成json字符串
     *
     * @param ob
     * @return String
     * @throws
     */
    public static String obToJson(Object ob) {
        return new Gson().toJson(ob);
    }


    /**
     * map2bean
     * 将map转换成bean
     *
     * @param map
     * @param clazz
     * @param @return
     * @return T
     * @throws
     */
    @SuppressWarnings( {"unchecked"} )
    public static <T> T map2bean(Map<String, Object> map, Class<?> clazz) {
        map = mapKey2Camel(map);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return (T) gson.fromJson(jsonStr, clazz);
    }


    /**
     * getFullException
     * 获取完整的异常信息
     *
     * @param @param e
     * @return String
     * @throws
     */
    public static String getFullException(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        try {
            pw.flush();
            sw.flush();
            return sw.toString();
        } finally {
            try {
                pw.close();
                sw.close();
            } catch (IOException e1) {
                // ignore
            }
        }
    }

    /**
     * 只要str里面存在一个fix 则返回tre 否则返回false
     *
     * @param fixStr 需要判断是字符串 如 jpg,gif,png
     * @param str    目标字符串
     * @author 艾成松
     **/
    public static boolean hasStr(String str, String fixStr) {

        if (fixStr == null || fixStr.length() == 0) {
            return true;
        }
        String[] fixs = fixStr.split(",");
        boolean flag = false;
        for (String fix : fixs) {
            if (str.indexOf(fix) != - 1) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * @author Aison  getAllFields  获取某个对象的所有属性 @param @param
     * object @param @return @return Field[] 返回类型 @throws
     */
    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * getUuid  获取一个uuid 不带 - 的 @param @return @author
     * Aison @return String 返回类型 @throws
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * RandomInt  生成一个指定长度的随机整数 @param @param
     * len @param @return @author Aison @return Integer 返回类型 @throws
     */
    public static Long RandomInt(int len) {
        Long lv = (long) Math.pow(10, len - 1);
        return (long) ((Math.random() * 9 + 1) * lv);
    }

    /**
     * camelToUnderline  驼峰转下划线 @param @param
     * param @param @return @author Aison @return String 返回类型 @throws
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        param = param.trim();
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString().toUpperCase();
    }


    /**
     * 获取请求的参数map
     *
     * @param request
     * @author Aison
     * @date 2018/5/28 17:42
     */
    public static Map<String, Object> getRequestMap(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> maps = new HashMap<String, Object>();
        String[] vals;
        for (Map.Entry<String, String[]> en : map.entrySet()) {
            vals = en.getValue();
            if (vals != null && vals.length > 0) {
                // 单个属性
                if (vals.length == 1 && ! StringUtils.isBlank(vals[0])) {
                    maps.put(en.getKey(), vals[0]);
                }
                // 数组属性
                if (vals.length > 1) {
                    maps.put(en.getKey().replace("[]", ""), en.getValue());
                }
            }
        }
        return maps;
    }

    /**
     * 获取时间表达式
     *
     * @param cron
     * @param date
     * @author Aison
     * @date 2018/5/31 15:19
     */
    public static String date2cron(String cron, Date date) {

        if (Biz.isNotEmpty(cron)) {
            return cron.trim();
        }
        else {
            return Biz.hasEmpty(date) ? "" : Biz.formatDate(date, "ss mm HH dd MM ? yyyy");
        }

    }

    /**
     * 获取时间表达式
     *
     * @param cron
     * @param date
     * @author Aison
     * @date 2018/5/31 15:19
     */
    public static String date2cron(String cron, String date) {
        if (Biz.hasEmpty(date)) {
            return cron;
        }
        else {
            return date2cron(cron, dateStr2Date(date, "yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 时间字符串转时间
     *
     * @param dateStr
     * @param format
     * @author Aison
     * @date 2018/5/31 15:28
     */
    public static Date dateStr2Date(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 毫秒数转日期
     *
     * @param timestamp
     * @date: 2018/4/26 15:20
     * @author: Aison
     */
    public static Date timestamp2Date(Long timestamp) {
        return new Date(timestamp);
    }


    /**
     * 将list中的元素复制成targetClz实例的元素
     * 泛型使用 static <T,S> 表示声明了两个泛型变量..作用范围是 copy 方法..
     * 泛型的上限只能在声明的时候定义 ..不能在使用的时候定义
     *
     * @param targetClass 目标class targetClass 必须是 list元素的子类
     * @author Aison
     * @date 2018/5/30 16:45
     */
    public static <T, E extends T> List<E> copy2Child(List<T> sourceList, Class<E> targetClass, MyJob<T, E> myJob) {
        List<E> list = new ArrayList<>();
        try {
            for (T row : sourceList) {
                E targetObj = targetClass.newInstance();
                Biz.copyBean(row, targetObj);
                myJob.execute(row, targetObj);
                list.add(targetObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
