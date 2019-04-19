package com.admin.core.util;

import com.admin.core.config.GunsProperties;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

	/**
	 * 获取验证码开关
	 *
	 * @author jiuyuan
	 * @Date 2017/5/23 22:34
	 */
	public static Boolean getKaptchaOnOff() {
		return SpringContextHolder.getBean(GunsProperties.class).getKaptchaOpen();
	}
}