package com.jiuyuan.service.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.WhitePhoneMapper;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class MyAccountSupplierService implements IMyAccountSupplierService {
	private static final Log logger = LogFactory.get("MyAccountService"); 
	
	@Autowired
	private WhitePhoneMapper whitePhoneMapper;
	
	@Autowired
	private YunXinSmsService yunXinSmsService;
	
	@Autowired
	private UcpaasService ucpaasService;
	
	/* (non-Javadoc)
	 * @see com.supplier.service.IMyAccountSupplierService#updatePassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void checkVerifyCode(String phoneNumber, String verifyCode, String sendType) {
		//检验验证码
		if (whitePhoneMapper.getWhitePhone(phoneNumber) == 0){//如果手机号不在白名单
//	        if (verifyCode == null || !yunXinSmsService.verifyCode(phone, verifyCode)) {
//	        	return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
//	        } 
	        if (sendType.equals("sms") && !yunXinSmsService.verifyCode(phoneNumber, verifyCode)) {
	        	 logger.info("短信验证码错误");
				 throw new RuntimeException("短信验证码错误");
			}else if (sendType.equals("voice") && !ucpaasService.verifyCode(phoneNumber, verifyCode)) {
				logger.info("短信验证码错误");
				 throw new RuntimeException("短信验证码错误");
			}
    	}

	}


    /* (non-Javadoc)
	 * @see com.supplier.service.IMyAccountSupplierService#sendVerifyCode(java.lang.String)
	 */
	@Override
	public Map<String,Object> sendVerifyCode(String phone) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (phone != null && phone.length() == 11) {
			boolean result = false;
			if (whitePhoneMapper.getWhitePhone(phone) > 0) {
				result = true;
			} else {
				result = yunXinSmsService.sendCode(phone, 2);
				logger.info("已发送验证码,电话:"+phone);
			}

			// boolean result = true;

			if (result) {
				data.put("sendResult", "SUCCESS");
				logger.info("发送验证码成功！");
			} else {
				if (whitePhoneMapper.getWhitePhone(phone) == 0) {// 如果手机号不在白名单
					logger.info("手机号发送失败，不在白名单中");
					throw new RuntimeException("手机号发送失败，不在白名单中");
				} else {
					data.put("sendResult", "SUCCESS");
					logger.info("发送验证码成功！");
					return data;
				}
			}

		} else {
			logger.info("手机号码有误！");
			throw new RuntimeException("手机号码有误！");
		}
		return data;
	}
	


	
}
