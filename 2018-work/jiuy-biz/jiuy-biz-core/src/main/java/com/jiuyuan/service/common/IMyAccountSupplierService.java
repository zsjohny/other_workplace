package com.jiuyuan.service.common;

import java.util.Map;

public interface IMyAccountSupplierService {

	/**
	 * 更改密码
	 * @param phoneNumber
	 * @param verifyCode
	 * @param sendType
	 * 
	 */
	void checkVerifyCode(String phoneNumber, String verifyCode, String sendType);
	  
	  

	/**
	 * 发送验证码
	 * @param phone
	 * @return
	 */
	Map<String, Object> sendVerifyCode(String phone);

}