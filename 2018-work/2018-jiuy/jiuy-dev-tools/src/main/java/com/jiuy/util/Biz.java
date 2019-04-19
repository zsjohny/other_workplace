package com.jiuy.util;

import com.jiuy.exception.BizException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工具类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/24 14:02
 * @Copyright 玖远网络
 */
public class Biz{

    /**
     * 验证一个对象或者多个对象是否为空.. 只要有一个为空则返回false
     * 只有所有的对象都不为空的情况下才为treu
     *
     * @param args 可变长参数
     * @return boolean    返回类型
     * @author Aison
     */
    public static boolean valiArg(Object... args) {
        for (Object ob : args) {
            if (ob == null || ob.toString().trim().equals("")) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取完整的异常信息
     *
     * @param e 异常
     * @return String    返回类型
     * @author Aison
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
     * 首字母大写
     *
     * @param field 属性名称
     * @return String    返回类型
     * @author Aison
     */
    public static String firstUpcase(String field) {

        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }


    private static final String UNDERLINE = "_";

    /**
     * 下划线转驼峰
     *
     * @param param 参数
     * @return String    返回类型
     * @author Aison
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        String[] chars = param.split(UNDERLINE);
        StringBuffer sb = new StringBuffer();
        if (chars.length <= 1) {

            int z = 0;
            // 判断是否全是大写
            for (int i = 0; i < param.length(); i++) {
                if (Character.isUpperCase(param.charAt(i))) {
                    z++;
                }
            }
            if (z == param.length()) {
                return param.toLowerCase();
            }

            return param.substring(0, 1).toLowerCase() + param.substring(1);
        }
        for (String str : chars) {
            sb.append(str.substring(0, 1).toUpperCase()).append(str.substring(1).toLowerCase());
        }
        String sbstr = sb.toString();
        sbstr = sbstr.substring(0, 1).toLowerCase() + sbstr.substring(1);
        return sbstr;
    }

    /**
     * 获取异常的信息
     *
     * @param e 异常
     * @return String    返回类型
     * @author Aison
     */
    public static String getExceptionStr(Throwable e) {
        String rs;
        if (e instanceof BizException) {
            rs = ((BizException) e).getMsg();
        }
        else {
            rs = Biz.getFullException(e);
        }
        return rs;
    }

    /**
     * 取包的名称
     *
     * @return String    返回类型
     * @author Aison
     */
    public static String path2pacakge(String path) {
        if (path.lastIndexOf("\\") == - 1) {
            path = path.trim();
        }
        else {
            path = path.substring(path.lastIndexOf("\\") + 1);
        }
        return path;
    }

    /**
     * 获取当前登录的系统用户名
     *
     * @return String    返回类型
     * @author Aison
     */
    public static String getSysUserName() {
        return System.getProperty("user.name");
    }


    /**
     * 日期格式化
     *
     * @param data   日期
     * @param format 格式
     * @return String    返回类型
     * @author Aison
     */
    public static String dateFormat(Date data, String format) {

        return new SimpleDateFormat(format).format(data);
    }


    public static void main(String[] args) throws ParseException {

    }

}
