package com.finace.miscroservice.commons.utils.tools;

import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;


public class TextUtil {

    protected org.slf4j.Logger log = LoggerFactory.getLogger(TextUtil.class);

    public static String hideLastChar(String str, int len) {
        if (str == null)
            return null;
        char[] chars = str.toCharArray();
        if (str.length() <= len) {
            for (int i = 0; i < chars.length; i++) {
                chars[i] = '*';
            }
        } else {
            for (int i = chars.length - 1; i > chars.length - len - 1; i--) {
                chars[i] = '*';
            }
        }
        str = new String(chars);
        return str;
    }

    public static String hideFirstChar(String str, int len) {
        if (str == null)
            return null;
        char[] chars = str.toCharArray();
        if (str.length() <= len) {
            for (int i = 0; i < chars.length; i++) {
                chars[i] = '*';
            }
        } else {
            for (int i = 0; i < 1; i++) {
                chars[i] = '*';
            }
        }
        str = new String(chars);
        return str;
    }

    /**
     * 前四后四 隐藏银行卡号
     *
     * @param cardNo
     * @return java.lang.String
     * @Date:16:57 2017/11/7
     */
    public static String hideCardNo(String cardNo) {
        if (StringUtils.isBlank(cardNo)) {
            return cardNo;
        }
        if (cardNo.length() > 10) {
            //前六后四
            StringBuilder stringBuilder = new StringBuilder();
            return stringBuilder.append(cardNo.substring(0, 4)).append("****")
                    .append(cardNo.substring(cardNo.length() - 4)).toString();
        } else {
            return cardNo;
        }
    }

    /**
     * 前三后四 隐藏手机号
     *
     * @param phoneNo
     * @return java.lang.String
     * @Date:17:00 2017/11/7
     */
    public static String hidePhoneNo(String phoneNo) {
        if (StringUtils.isBlank(phoneNo)) {
            return phoneNo;
        }
        if (phoneNo.length() > 7) {
            StringBuilder stringBuilder = new StringBuilder();
            return stringBuilder.append(phoneNo.substring(0, 3)).append("****")
                    .append(phoneNo.substring(phoneNo.length() - 4)).toString();
        } else {
            return phoneNo;
        }
    }

    public static String hideIdNo(String idNo){
        if (StringUtils.isBlank(idNo)) {
            return idNo;
        }
        if (idNo.length() >= 15) {
            StringBuilder stringBuilder = new StringBuilder();
            return stringBuilder.append(idNo.substring(0, 6)).append("****")
                    .append(idNo.substring(idNo.length() - 4)).toString();
        } else {
            return idNo;
        }
    }
    /**
     * @Description: 隐藏手机号码用 @param: @param str @param: @param
     * len @param: @return @return: String @throws
     */
    public static String hidePhoneChar(String str, int len) {
        if (str == null)
            return null;
        char[] chars = str.toCharArray();
        for (int i = 3; i < chars.length - len; i++) {
            chars[i] = '*';
        }
        str = new String(chars);
        return str;
    }

    /**
     * 隐藏手机第三位开始
     *
     * @param str
     * @param len
     * @return
     */
    public static String hidePhoneCharByLast(String str, int len) {
        if (str == null)
            return null;
        char[] chars = str.toCharArray();
        for (int i = 3; i < chars.length - 2; i++) {
            chars[i] = '*';
        }
        str = new String(chars);
        return str;
    }

    /**
     * 隐藏银行卡 信息
     *
     * @param str
     * @return
     */
    public static String hideBankCard(String str) {
        if (str == null)
            return null;
        char[] chars = str.toCharArray();
        for (int i = 4; i < chars.length - 3; i++) {
            chars[i] = '*';
        }
        str = new String(chars);
        return str;
    }

    /**
     * @Description: 隐藏用户名 @param: @param username @param: @return @return:
     * String @throws
     */
    public static String hideUsernameChar(String username) {

        if (username == null) {
            return null;
        } else {
            if (RegExUtil.isMobile(username)) {
                return hidePhoneChar(username, 4);
            } else {
                String bb = username.substring(1, username.length() - 1);

                String cc = "";

                for (int i = 0; i < bb.length(); i++) {

                    if (i == 0) {
                        cc = bb.replace(String.valueOf(bb.charAt(i)), "*");
                    } else {
                        cc = cc.replace(String.valueOf(bb.charAt(i)), "*");
                    }
                }
                return username.replace(bb, cc);
            }
        }
    }

    public static String hideRealnameChar(String username) {
        if (username == null) {
            return null;
        } else {
            return username.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + createAsterisk(username.length() - 1));
        }
    }

    public static String hideCenterChar(String str) {
        if (str == null) {
            return null;
        } else {
            return str.replaceAll("([\\u4e00-\\u9fa5]{3})[\\u4e00-\\u9fa5]{3}([\\u4e00-\\u9fa5]{3})", "$1****$2");
        }
    }

    public static String hideCompanyChar(String str) {
        if (str == null) {
            return null;
        } else {
            if( str.length() > 5){
                return str.substring(0, 2) + "****" + str.substring(str.length() - 4, str.length());
            }else{
                return str.substring(0, 1) + "****";
            }
        }
    }

    public static String hideAddressChar(String str) {
        if (str == null) {
            return null;
        } else {
            return str.substring(0, 6) + "****";
        }
    }


    public static String createAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }

    public static String hideChar(String str, int len) {
        if (str == null)
            return null;
        char[] chars = str.toCharArray();
        if (chars.length <= 1) {
            return str;
        } else {
            for (int i = 1; i > chars.length - 1; i++) {
                if (i < len) {
                    chars[i] = '*';
                }
            }
            str = new String(chars);
            return str;
        }

    }

    /**
     * 如果str为null，返回“”,否则返回str
     *
     * @param str
     * @return
     */
    public static String isNull(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static int getInt(String str) {
        if (str == null || str.equals(""))
            return 0;
        int ret = 0;
        try {
            ret = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            ret = 0;
        }
        return ret;
    }

    public static double getDouble(String str) {
        if (str == null || str.equals(""))
            return 0.0;
        double ret = 0.0;
        try {
            ret = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            ret = 0.0;
        }
        return format6(ret);
    }

    public static double format6(double d) {
        DecimalFormat df = new DecimalFormat("0.000000");
        String ds = df.format(d);
        return Double.parseDouble(ds);
    }

    /**
     * 首字母大写
     *
     * @param s
     * @return
     */
    public static String firstCharUpperCase(String s) {
        StringBuffer sb = new StringBuffer(s.substring(0, 1).toUpperCase());
        sb.append(s.substring(1, s.length()));
        return sb.toString();
    }

    /**
     * @Description: 去除HTML标签 @param: @param
     * inputString @param: @return @return: String @throws
     */
    public static String html2Text(String inputString) {

        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        java.util.regex.Matcher m_script;
        Pattern p_style;
        java.util.regex.Matcher m_style;
        Pattern p_html;
        java.util.regex.Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            textStr = htmlStr;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return textStr;// 返回文本字符串
    }

    /**
     * userid @param: @param type @param: @return @return: String @throws
     */
    public synchronized static String wyTradeNO(String date, String vmid, long userid, String type) {
        String s;
        Random rand = new Random((new Date()).getTime());
        int tmp = Math.abs(rand.nextInt());
        int retmp = tmp % (99999 - 10000 + 1) + 10000;
        s = date + vmid + type + String.valueOf(retmp);
        return s;
    }

    /**
     * @Title: qianwei @Description: 以逗号作为每千位的间隔符，并且小数点后四舍五入为两位 @param @param
     * account @param @return 设定文件 @return String 返回类型 @throws
     */
    public static String qianwei(String account) {

        DecimalFormat format = new DecimalFormat("####,####,####,####,####.##");
        return format.format(Double.valueOf(account));
    }


}
