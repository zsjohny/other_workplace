package com.e_commerce.miscroservice.crm.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
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

	public static String isNull(Object o) {
		if (o == null) {
			return "";
		}
		String str = "";
		if (o instanceof String) {
			str = (String) o;
		} else {
			str = o.toString();
		}
		return str;
	}

	/**
	 * 检查email是否是邮箱格式，返回true表示是，反之为否
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		email = isNull(email);
		String ems = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern regex = Pattern.compile(ems);
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 检查身份证的格式，返回true表示是，反之为否
	 * 
//	 * @param email
	 * @return
	 */
	public static boolean isCard(String cardId) {
		cardId = isNull(cardId);
		String id = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
		// 身份证正则表达式(15位)
		Pattern isIDCard1 = Pattern.compile(id);
		String ids = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
		// 身份证正则表达式(18位)
		Pattern isIDCard2 = Pattern
				.compile(ids);
		Matcher matcher1 = isIDCard1.matcher(cardId);
		Matcher matcher2 = isIDCard2.matcher(cardId);
		boolean isMatched = matcher1.matches() || matcher2.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (isEmpty(str)) {
			return false;
		}
		String s = "\\d*";
		Pattern regex = Pattern.compile(s);
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}
		String s = "\\d*(.\\d*)?";
		Pattern regex = Pattern.compile(s);
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String str) {

		return !isEmpty(str);
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

	public static String hideChar(String str, int len) {
		if (str == null){
			return null;
		}
		char[] chars = str.toCharArray();
		for (int i = 1; i > chars.length - 1; i++) {
			if (i < len) {
				chars[i] = '*';
			}
		}
		str = new String(chars);
		return str;
	}

	public static String hideFirstChar(String str, int len) {
		if (str == null){
			return null;
		}
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

	public static String hideLastChar(String str, int len) {
		if (str == null){
			return null;
		}
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

	/**
	 * 
	 * @return
	 */
	public static String format(String str, int len) {
		if (str == null){
			return "-";
		}
		if (str.length() <= len) {
			int pushlen = len - str.length();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < pushlen; i++) {
				sb.append("0");
			}
			sb.append(str);
			str = sb.toString();
		} else {
			String newStr = str.substring(0, len);
			str = newStr;
		}
		return str;
	}

	public static String contact(Object[] args) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (i < args.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 是否包含在以“，”隔开字符串内
	 * 
	 * @param s
	 * @param type
	 * @return
	 */
	public static boolean isInSplit(String s, String type) {
		if (isNull(s).equals("")) {
			return false;
		}
		List<String> list = Arrays.asList(s.split(","));
		if (list.contains(type)) {
			return true;
		}
		return false;
	}

	public static boolean isBlank(String str) {
		return StringUtils.isNull(str).equals("");
	}


	public synchronized static String wyTradeNO(String date, String vmid, long userid, String type) {
		String s;
		Random rand = new Random((new Date()).getTime());
		int tmp = Math.abs(rand.nextInt());
		int retmp = tmp % (99999 - 10000 + 1) + 10000;
		s = date + vmid + type + String.valueOf(retmp);
		return s;
	}



	public static String array2Str(Object[] arr) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			s.append(arr[i]);
			if (i < arr.length - 1) {
				s.append(",");
			}
		}
		return s.toString();
	}

	public static String array2Str(int[] arr) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			s.append(arr[i]);
			if (i < arr.length - 1) {
				s.append(",");
			}
		}
		return s.toString();
	}

	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
//	 * @param String
	 *            s 需要得到长度的字符串
	 * @return int 得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null){
			return 0;
		}
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	/**
	 * 获取客户端ip
	 * @param request
	 * @return
	 */
	public static String getRequestRealIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.contains(",")) {
			ip = ip.split(",")[0];
		}

		if (!checkIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("X-Real-IP");
		}

		if (!checkIp(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private static boolean checkIp(String ip) {
		if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 请求参数转为普通map
	 * @param request
	 * @return
	 */
	public static Map getParameterMap(HttpServletRequest request) {
	    // 参数Map
	    Map properties = request.getParameterMap();
	    // 返回值Map
	    Map returnMap = new HashMap();
	    Iterator entries = properties.entrySet().iterator();
	    Entry entry;
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = (Entry) entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value = values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	    }
	    return returnMap;
	}

	/**
	 * 方法名称:transMapToString 传入参数:map 返回值:String 形如
	 * username'chenziwen^password'1234
	 */
	public static String transMapToString(Map map) {
		Entry entry;
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			entry = (Entry) iterator.next();
			sb.append(entry.getKey().toString()).append("'")
					.append(null == entry.getValue() ? "" : entry.getValue().toString())
					.append(iterator.hasNext() ? "^" : "");
		}
		return sb.toString();
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if ((cs == null) || ((strLen = cs.length()) == 0)){
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}


	public static boolean hasEmpty(String... str) {
		for (String s : str) {
			if (s == null || s.trim().equals("")) {
				return true;
			}
		}
		return false;
	}




}
