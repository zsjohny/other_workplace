package com.finace.miscroservice.commons.utils.tools.message;

import com.finace.miscroservice.commons.log.Log;
import com.jianzhou.sdk.BusinessService;

/**
 * 
 * @ClassName:     SendMessageUtil.java
 * @Description:   发送短信彩信工具
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-10-10 上午10:57:48
 * <b>Copyright (c)</b> 一桶金版权所有 <br/>
 */
public class SendMessageUtil {
	private static Log logger = Log.getInstance(SendMessageUtil.class);
	
	/**
	 * 
	 * @Description:  发送短信
	 * @param:        @param phone
	 * @param:        @param content
	 * @param:        @return
	 * @param:        @throws InterruptedException   
	 * @return:       boolean   
	 * @throws
	 */
	public static boolean sendSMS(String phone, String content) throws InterruptedException {

		BusinessService bs = new BusinessService();
		bs.setWebService("http://www.jianzhou.sh.cn/JianzhouSMSWSServer/services/BusinessService");
//		String account = "jzyy903";
		String account = "sdk_yitongjin";
		String password = "506320";
		String sign = "【一桶金】";
		int restr = bs.sendBatchMessage(account, password, phone, content + sign);
		logger.info("提交内容到短信接口，接口返回值：" + restr);
		if (restr > 0) {
			return true;
		} else {
			return false;
		}
	}

}
