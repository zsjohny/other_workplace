package com.finace.miscroservice.borrow.service.impl;

import com.finace.miscroservice.borrow.service.FuiouH5PayService;
import com.finace.miscroservice.borrow.service.fuiou.HttpFormUtil;
import com.finace.miscroservice.commons.log.Log;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.fuiou.util.MD5;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RefreshScope
public class FuiouH5PayServiceImpl implements FuiouH5PayService {
	private Log logger = Log.getInstance(FuiouH5PayServiceImpl.class);

	@Value("${borrow.fuiou.webhost}")
	private String webhost;

	@Value("${borrow.fuiou.furl}")
	private String furl;

	@Value("${borrow.fuiou.fcd}")
	private String fcd;

	@Value("${borrow.fuiou.fkey}")
	private  String fkey;

	@Value("${borrow.fuiou.backurl}")
	private String BACK_URL;

	@Value("${borrow.fuiou.homeurl}")
	private String HOME_URL;

	@Value("${borrow.fuiou.returnurl}")
	private String RETURN_URL;

	@Value("${borrow.fuiou.payurl}")
	private String PAY_URL;

	private static final String TYPE = "10"; // 10:理财 11:电商
	private static final String ID_TYPE = "0"; // 身份证

	public String makePayForm(Param param) {
		try {
			StringBuffer orderPlain = new StringBuffer();
			long centAmt = param.getAmt().multiply(BigDecimal.valueOf(100)).longValue();
			String signPlain = TYPE + "|" + "2.0" + "|" + fcd + "|" + param.getOrderId() + "|" + param.getUserId() + "|" + centAmt + "|" + param.getBankCard() + "|" + BACK_URL + "|" + param.getName() + "|" + param.getPid() + "|" + ID_TYPE + "|" + "0" + "|" + HOME_URL + "|" + RETURN_URL + "|" + fkey;
			String sign = MD5.MD5Encode(signPlain);
			logger.info("[签名明文:]" + signPlain);

			orderPlain.append("<ORDER>");
			orderPlain.append("<VERSION>2.0</VERSION>");
			orderPlain.append("<LOGOTP>0</LOGOTP>");
			orderPlain.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>");
			orderPlain.append("<TYPE>").append(TYPE).append("</TYPE>");
			orderPlain.append("<MCHNTORDERID>").append(param.getOrderId()).append("</MCHNTORDERID>");
			orderPlain.append("<USERID>").append(param.getUserId()).append("</USERID>");
			orderPlain.append("<AMT>").append(centAmt).append("</AMT>");
			orderPlain.append("<BANKCARD>").append(param.getBankCard()).append("</BANKCARD>");
			orderPlain.append("<BACKURL>").append(BACK_URL).append("</BACKURL>");
			orderPlain.append("<HOMEURL>").append(HOME_URL).append("</HOMEURL>");
			orderPlain.append("<REURL>").append(RETURN_URL).append("</REURL>");
			orderPlain.append("<NAME>").append(param.getName()).append("</NAME>");
			orderPlain.append("<IDTYPE>").append(ID_TYPE).append("</IDTYPE>");
			orderPlain.append("<IDNO>").append(param.getPid()).append("</IDNO>");
			orderPlain.append("<REM1>").append(param.getUserId()).append("</REM1>");
			orderPlain.append("<REM2>").append(param.getUserId()).append("</REM2>");
			orderPlain.append("<REM3>").append(param.getUserId()).append("</REM3>");
			orderPlain.append("<SIGNTP>").append("md5").append("</SIGNTP>");
			orderPlain.append("<SIGN>").append(sign).append("</SIGN>");
			orderPlain.append("</ORDER>");

			logger.info("[订单信息:]" + orderPlain.toString());

			Map<String, String> fParam = new HashMap<String, String>();
			fParam.put("VERSION", "2.0");
			fParam.put("ENCTP", "1");
			fParam.put("LOGOTP", "0");
			fParam.put("MCHNTCD", fcd);
			fParam.put("FM", orderPlain.toString());
			fParam.put("FM", DESCoderFUIOU.desEncrypt(orderPlain.toString(), DESCoderFUIOU.getKeyLength8(fkey)));
			String formHtml = HttpFormUtil.formHtml(PAY_URL, fParam,"utf8");

			//logger.info("[请求信息:]" + fParam);
			return formHtml;
		} catch (Exception e) {
			return "error...";
		}
	}

	@Override
	public boolean isFromFuiouServer(PayCallBackResult result) {
		String signPain = new StringBuffer().append(result.getType()).append("|").append(result.getVersion()).append("|").append(result.getResponseCode()).append("|").append(result.getMchntCd()).append("|").append(result.getMchntOrderId()).append("|").append(result.getOrderId()).append("|").append(result.getAmt()).append("|").append(result.getBankCard()).append("|").append(fkey).toString();
		if (MD5.MD5Encode(signPain).equals(result.getSign())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isPaySuccess(PayCallBackResult result) {
		String signPain = new StringBuffer().append(result.getType()).append("|").append(result.getVersion()).append("|").append(result.getResponseCode()).append("|").append(result.getMchntCd()).append("|").append(result.getMchntOrderId()).append("|").append(result.getOrderId()).append("|").append(result.getAmt()).append("|").append(result.getBankCard()).append("|").append(fkey).toString();
		if (MD5.MD5Encode(signPain).equals(result.getSign())) {
			if ("0000".equals(result.getResponseCode())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
