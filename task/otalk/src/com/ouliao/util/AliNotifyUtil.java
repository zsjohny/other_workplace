/**
 * 
 */
package com.ouliao.util;

import com.ouliao.service.versionfirst.UserAliPayService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: AliNotifyUtil.java, 2016年3月3日 下午6:23:29
 */
@Controller
@RequestMapping(value = "user/aliNotify")
public class AliNotifyUtil {
	@Autowired
	private UserAliPayService userAliPayService;

	// 支付宝回调函数
	@RequestMapping(value = "replyFree")
	public void uploadPro(@RequestParam("out_trade_no") String out_trade_no, @RequestParam("sign") String sign,
			@RequestParam("total_fee") Double total_fee, @RequestParam("buyer_email") String buyer_email) {

		try {

			if (StringUtils.isNotEmpty(out_trade_no) && StringUtils.isNotEmpty(sign) && total_fee != null
					&& StringUtils.isNotEmpty(buyer_email)) {

				userAliPayService.updateUserAlipayIsAuthorByPayId("true", sign, total_fee, out_trade_no, buyer_email);
			}

		} catch (Exception e) {
		}
	}
}
