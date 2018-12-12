package com.finace.miscroservice.commons.utils.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
*   
* 类名称：RegExUtil  
* 类描述
* 创建人：zhouliang 
* 创建时间：2014-6-5 上午11:55:34   
* @version   
*
 */
public class RegExUtil {

	/**
	 * 手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobile(String mobiles){
		  
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
			  
		Matcher m = p.matcher(mobiles);  
			   
		return m.matches();  
	}
	
	/**
	 * 检查email是否是邮箱格式，返回true表示是，反之为否
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		Pattern regex = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
}
