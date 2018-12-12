package org.dream.utils.math;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Boyce 2016年7月4日 下午5:08:42
 */
public class PromoteCode {
	static char[] base = { 'n', 'i', 's', 'm', 'l', 'x', 'h', 'f', 'j', 'w', 'v', 'e', 'c', 'p', 'r', 'q', 'b', 't',
			'z', 'g', 'u', 'y', 'a', 'o', 'k', 'd' };
	static Map<Character, Integer> base_swap = new HashMap<>();
	static {
		for (int i = 0; i < base.length; i++) {
			char c = base[i];
			base_swap.put(c, 10 + i);
		}
	}
	static final int BASE_NUM = 12345678;
	static final int RATIO = 36;

	public static String getPromoteCode(int userId) {
		int num = BASE_NUM + 3 * userId;
		System.out.println(num);
		String result = "";
		int mod = 0;
		while ( num!= 0) {
			mod = num % RATIO;
			if (mod < 10) {
				result = mod + result;
			} else {
				result = base[mod - 10] + result;
			}
			num = num / RATIO;
		}
		return result;
	}

	public static int getPromoteUserId(String code) {

		if (StringUtils.isBlank(code) || !code.trim().toLowerCase().matches("[0-9a-z]+")) {
			return 0;
		}
		code = code.trim();
		int pow = code.length() - 1;
		int result = 0;
		char[] carray = code.toCharArray();
		for (char x : carray) {
			if (x < 97)
				result = (int) (result +(x - 48)* Math.pow(RATIO, pow--));
			else
				result = (int) (result + base_swap.get(x)*Math.pow(RATIO, pow--));
		}
		result=result-BASE_NUM;
		if(result%3!=0){
			return 0;
		}
		result/=3;
		return result;
	}

	public static void main(String[] args) {
		String code = getPromoteCode(18);
		System.out.println(code);
		System.out.println(getPromoteUserId(code));
	}
}
