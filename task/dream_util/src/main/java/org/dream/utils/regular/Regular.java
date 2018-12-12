package org.dream.utils.regular;

import java.util.regex.Pattern;

public class Regular {
	// 姓名
	public static boolean checkNameMatch(String realName) {
		String reg = "^[\u4e00-\u9fa5]{2,25}$";
		return Pattern.compile(reg).matcher(realName).matches();
	}

	// 身份证
	public static boolean checkIdCardMatch(String idCard) {
		String reg = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
		return Pattern.compile(reg).matcher(idCard).matches();
	}

	// 银行卡号
	public static boolean checkBankCardMatch(String bankCard) {
		String reg = "^\\d{16,19}$";
		return Pattern.compile(reg).matcher(bankCard).matches();
	}

	// 手机号
	public static boolean checkPhone(String phone) {
		String reg = "^1(3|4|5|7|8)\\d{9}$";
		return Pattern.compile(reg).matcher(phone).matches();
	}

	// 昵称
	public static boolean checkNickName(String userName) {
		String reg = "^[a-zA-Z0-9\u4e00-\u9fa5]{1,10}$";
		return Pattern.compile(reg).matcher(userName).matches();
	}

	// QQ
	public static boolean checkQQ(String qq) {
		String reg = "^[1-9][0-9]{4,14}$";
		return Pattern.compile(reg).matcher(qq).matches();
	}

	public static void main(String[] args) {
		System.out.println(checkNickName("123456sddsggegege"));
	}

}
