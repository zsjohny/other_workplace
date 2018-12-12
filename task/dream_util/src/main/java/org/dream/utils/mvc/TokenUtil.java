/**
 * 
 */
package org.dream.utils.mvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.dream.utils.encrypt.PasswordEncryptUtil;

/**
 * @author Boyce 上午11:22:19
 */
public class TokenUtil {
	public static final String TOKEN1 = "token1";
	public static final String TOKEN2 = "token2";
	private static final int TOKEN2_ENCRYPT_TIMES = 2;
	private static final int BASE_NUM=349374762;
	public static  void  generateToken(int id,HttpServletRequest req, HttpServletResponse resp) {
		String numFormat = Integer.toHexString(BASE_NUM + id);
		char[] cs = numFormat.toCharArray();
		String result = "";
		for (int i = 0; i < cs.length; i++) {
			int c = cs[i];
			result += (char) (97 + (c + (i % 2 == 0 ? i * 2 : i + 3)) % 26);
			result += (char) (Math.random() * 26 + 97);
			result += (char) (Math.random() * 26 + 97);
		}
		result=Integer.toString((int)(Math.random()*36), 36)+Integer.toString((int)(Math.random()*36), 36)+result;
		result=result+Integer.toString((int)(Math.random()*36), 36)+Integer.toString((int)(Math.random()*36), 36);
		Cookie token1 = new Cookie(TOKEN1, Base64.encodeBase64String(result.getBytes()));
		token1.setPath("/");
		resp.addCookie(token1);
		String result2 = PasswordEncryptUtil.digest(result, req.getHeader("Host"), TOKEN2_ENCRYPT_TIMES);
		Cookie token2 = new Cookie(TOKEN2, Base64.encodeBase64String(result2.getBytes()));
		token2.setPath("/");
		resp.addCookie(token2);
		
		UserUtil.recordUser(id, result);
	}

	static String checkAndReturnToken(HttpServletRequest req) {
		Cookie[] cks = req.getCookies();
		if(cks==null){
			return null;
		}
		String token1 = null;
		String token2 = null;
		for (int i = 0; i < cks.length; i++) {
			Cookie cookie = cks[i];
			if (TOKEN1.equals(cookie.getName())) {
				token1 = cookie.getValue();
			}
			if (TOKEN2.equals(cookie.getName())) {
				token2 = cookie.getValue();
			}
		}
		if (token1 == null || token2 == null) {
			return null;
		}
		token1 = new String(Base64.decodeBase64(token1.getBytes()));
		token2 = new String(Base64.decodeBase64(token2.getBytes()));
		if (token1 != null && PasswordEncryptUtil.digest(token1, req.getHeader("Host"), TOKEN2_ENCRYPT_TIMES).equals(token2)) {
			return token1;
		}
		return null;
	}

	public static void main(String[] args) {
		String token1 = new String(Base64.decodeBase64("12".getBytes()));
		System.out.println(token1);
	}
}
