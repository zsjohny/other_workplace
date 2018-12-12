package com.finace.miscroservice.borrow.service.fuiou;

import com.finace.miscroservice.commons.log.Log;
import com.fuiou.util.MD5;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RefreshScope
public class FuiouPayService {

	private Log logger = Log.getInstance(FuiouPayService.class);

	@Value("${borrow.fuiou.webhost}")
	private String webhost;

	@Value("${borrow.fuiou.furl}")
	private String furl;

	@Value("${borrow.fuiou.fcd}")
	private String fcd;

	@Value("${borrow.fuiou.fkey}")
	private String fkey;

	@Value("${borrow.fuiou.send.msg}")
	private String fuiou_send_msg;

	@Value("${borrow.fuiou.do.pay}")
	private String fuiou_do_pay;

	/**
	 * 富有支付发送短信
	 *
	 * @param paramStr
	 * @return
	 */
	public String sendFuiouMessage(String paramStr) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("FM", paramStr);

		try {
			String respStr = HttpPostUtil.postForward(fuiou_send_msg, params);
			logger.info("富有支付发送短信,返回数据={}",respStr);
			return respStr;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("富有支付发送短信,异常信息{}", e);
			return null;
		}
	}

	/**
	 * 富有最后支付接口
	 *
	 * @param paramStr
	 * @return
	 */
	public String doFuiouPay(String paramStr) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("FM", paramStr);
		try {
			String respStr = HttpPostUtil.postForward(fuiou_do_pay, params);
			logger.info("富有最后支付接口,返回数据={}",respStr);
			return respStr;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("富有最后支付接口,异常信息{}", e);
			return null;
		}
	}

}
