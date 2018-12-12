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
public class FuiouCloseOrderService {

	private Log logger = Log.getInstance(FuiouCloseOrderService.class);

	@Value("${borrow.fuiou.webhost}")
	private String webhost;

	@Value("${borrow.fuiou.furl}")
	private String furl;

	@Value("${borrow.fuiou.fcd}")
	private String fcd;

	@Value("${borrow.fuiou.fkey}")
	private String fkey;

	@Value("${borrow.fuiou.closeOrder}")
	private String close_order_url;


	public class CloseOrderResult {
         private String pescode;
         private String pesmsg;
         private String mchntOrderId;
         private String orderId;
         private String amt;

		public String getPescode() {
			return pescode;
		}

		public void setPescode(String pescode) {
			this.pescode = pescode;
		}

		public String getPesmsg() {
			return pesmsg;
		}

		public void setPesmsg(String pesmsg) {
			this.pesmsg = pesmsg;
		}

		public String getMchntOrderId() {
			return mchntOrderId;
		}

		public void setMchntOrderId(String mchntOrderId) {
			this.mchntOrderId = mchntOrderId;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public String getAmt() {
			return amt;
		}

		public void setAmt(String amt) {
			this.amt = amt;
		}
	}

	/**
	 * 未支付订单关闭
	 * @param mcOrderId  商户订单号
	 * @param orderId  富有订单号
	 * @param amt  支付的金额
	 * @return
	 */
	public CloseOrderResult closeOrder(String mcOrderId,String orderId, String amt) {
		String sign = MD5.MD5Encode(new StringBuffer().append(fcd).append("|").append(mcOrderId).append("|").append(orderId).append("|").append(amt).append("|").append(fkey).toString());
		String query = new StringBuffer().append("<ORDER>")
				.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>")
				.append("<MCHNTORDERID>").append(mcOrderId).append("</MCHNTORDERID>")
				.append("<ORDERID>").append(orderId).append("</ORDERID>")
				.append("<AMT>").append(amt).append("</AMT>")
				.append("<SIGN>").append(sign).append("</SIGN>")
				.append("</ORDER>").toString();

		Map<String, String> params = new HashMap<String, String>();
		params.put("FM", query);

		try {
			String respStr = HttpPostUtil.postForward(close_order_url, params);
			logger.info("未支付订单关闭,返回数据={}",respStr);
			String pesmsg = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));
			String pescode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
			String mchntcd = respStr.substring(respStr.indexOf("<MCHNTCD>") + 9, respStr.indexOf("</MCHNTCD>"));

			String mchntOrderId = respStr.substring(respStr.indexOf("<MCHNTORDERID>") + 14, respStr.indexOf("</MCHNTORDERID>"));
			String horderId = respStr.substring(respStr.indexOf("<ORDERID>") + 9, respStr.indexOf("</ORDERID>"));
			String hamt = respStr.substring(respStr.indexOf("<AMT>") + 5, respStr.indexOf("</AMT>"));
			String rSign = respStr.substring(respStr.indexOf("<SIGN>") + 6, respStr.indexOf("</SIGN>"));

			// 校验签名
			if ( rSign.equals(MD5.MD5Encode(pescode+"|"+mchntcd+"|"+mchntOrderId+ "|"+ horderId+"|"+ hamt  + fkey)) ) {
				CloseOrderResult result = new CloseOrderResult();
				result.setMchntOrderId(mchntOrderId);
				result.setPescode(pescode);
				result.setPesmsg(pesmsg);
				result.setAmt(hamt);
				result.setOrderId(horderId);
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}





}
