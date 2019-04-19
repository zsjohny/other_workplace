package com.jiuyuan.util.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberUtil {

    private static Logger log = LoggerFactory.getLogger(NumberUtil.class);

    private static Pattern patternFloat = Pattern.compile("(\\d+\\.\\d*)|(\\d*\\.\\d+)|(\\d+)");

    private static Pattern patternDouble = patternFloat;

    private static Pattern patternInt = Pattern.compile("\\d+");

    private static Pattern patternLong = patternInt;

    private static String findFirstMatch(Pattern pattern, String string) {
        if (string == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static float parseFloat(String string, float defaultValue, boolean tryHard) {
        if (StringUtils.isNotBlank(string)) {
            if (tryHard) {
                string = findFirstMatch(patternFloat, string);
            }
            try {
                return Float.parseFloat(string);
            } catch (NumberFormatException e) {
                log.error(string, e);
            }
        }
        return defaultValue;
    }

    public static float parseFloat(Object object, float defaultValue, boolean tryHard) {
        if (object == null) {
            return defaultValue;
        } else {
            return parseFloat(object.toString(), defaultValue, tryHard);
        }
    }

    public static double parseDouble(String string, double defaultValue, boolean tryHard) {
        if (string != null) {
            if (tryHard) {
                string = findFirstMatch(patternDouble, string);
            }
            try {
                return Double.parseDouble(string);
            } catch (NumberFormatException e) {
                log.error(string, e);
            }
        }
        return defaultValue;
    }

    public static double parseDouble(Object object, double defaultValue, boolean tryHard) {
        if (object == null) {
            return defaultValue;
        } else {
            return parseDouble(object.toString(), defaultValue, tryHard);
        }
    }

    public static int parseInt(String string, int defaultValue, boolean tryHard) {
        if (StringUtils.isNotBlank(string)) {
            if (tryHard) {
                string = findFirstMatch(patternInt, string);
            }
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                log.error(string, e);
            }
        }
        return defaultValue;
    }

    public static int parseInt(Object object, int defaultValue, boolean tryHard) {
        if (object == null) {
            return defaultValue;
        } else {
            return parseInt(object.toString(), defaultValue, tryHard);
        }
    }

    public static long parseLong(String string, long defaultValue, boolean tryHard) {
        if (StringUtils.isNotBlank(string)) {
            if (tryHard) {
                string = findFirstMatch(patternLong, string);
            }
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException e) {
                log.error(string, e);
            }
        }
        return defaultValue;
    }

    public static long parseLong(Object object, long defaultValue, boolean tryHard) {
        if (object == null) {
            return defaultValue;
        } else {
            return parseLong(object.toString(), defaultValue, tryHard);
        }
    }

    /**
     * 注意：用于比较的数值，它们的类型必须相同，才可能返回true。
     */
    public static boolean in(Number number, Number... collection) {
        for (Number item : collection) {
            if (number == item || number.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * both min and max are inclusive
     */
    public static Number checkRange(Number number, Number min, Number max) {
        if (number.doubleValue() > max.doubleValue()) {
            number = max;
        } else if (number.doubleValue() < min.doubleValue()) {
            number = min;
        }
        return number;
    }
    
    public static boolean isNumeric(String str) {
        if (StringUtils.equals("", str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
		String s = "";
		System.out.println(isNumeric(s));
	}
    
}
