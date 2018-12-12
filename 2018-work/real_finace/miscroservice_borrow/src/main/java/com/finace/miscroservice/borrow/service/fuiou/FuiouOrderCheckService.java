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
public class FuiouOrderCheckService {

	private Log logger = Log.getInstance(FuiouOrderCheckService.class);

	@Value("${borrow.fuiou.webhost}")
	private String webhost;

	@Value("${borrow.fuiou.furl}")
	private String furl;

	@Value("${borrow.fuiou.fcd}")
	private String fcd;

	@Value("${borrow.fuiou.fkey}")
	private String fkey;

	@Value("${borrow.fuiou.checkurl}")
	private String check_result_url;

	@Value("${borrow.fuiou.queryorderurl}")
	private String query_order_url;




	public class CheckOrderResult {
         private String pescode;
         private String pesmsg;
         private String mchntOrderId;
         private String amt;
         private String bankCard;
         private String orderDate;

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

		public String getAmt() {
			return amt;
		}

		public void setAmt(String amt) {
			this.amt = amt;
		}

		public String getBankCard() {
			return bankCard;
		}

		public void setBankCard(String bankCard) {
			this.bankCard = bankCard;
		}

		public String getOrderDate() {
			return orderDate;
		}

		public void setOrderDate(String orderDate) {
			this.orderDate = orderDate;
		}
	}

	/**
	 * 根据商户订单号获取订单结果查询
	 * @param orderId
	 * @return
	 */
	public CheckOrderResult checkMchntcdResult(String orderId) {
		String sign = MD5.MD5Encode(new StringBuffer().append("2.0").append("|").append(fcd).append("|").append(orderId).append("|").append(fkey).toString());
		String query = new StringBuffer().append("<ORDER>")
				.append("<VERSION>2.0</VERSION>")
				.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>")
				.append("<MCHNTORDERID>").append(orderId).append("</MCHNTORDERID>")
				.append("<SIGN>").append(sign).append("</SIGN>")
				.append("<REM1></REM1>")
				.append("<REM2></REM2>")
				.append("<REM3></REM3>")
				.append("</ORDER>").toString();

		Map<String, String> params = new HashMap<String, String>();
		params.put("FM", query);

		try {
			String respStr = HttpPostUtil.postForward(check_result_url, params);
			logger.info("根据商户订单号获取订单结果查询,返回数据={}",respStr);
			String version = respStr.substring(respStr.indexOf("<VERSION>") + 9, respStr.indexOf("</VERSION>"));
			String pesmsg = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));
			String mchntOrderId = respStr.substring(respStr.indexOf("<MCHNTORDERID>") + 14, respStr.indexOf("</MCHNTORDERID>"));
			String pescode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
			String rSign = respStr.substring(respStr.indexOf("<SIGN>") + 6, respStr.indexOf("</SIGN>"));

			// 校验签名
			if ( rSign.equals(MD5.MD5Encode(version+"|"+pescode+"|"+pesmsg+ "|"+ mchntOrderId + "|"+ fkey)) ) {
				CheckOrderResult result = new CheckOrderResult();
				result.setMchntOrderId(mchntOrderId);
				result.setPescode(pescode);
				result.setPesmsg(pesmsg);
				if( !"5077".equals(pescode) ){
					String amt = respStr.substring(respStr.indexOf("<AMT>") + 5, respStr.indexOf("</AMT>"));
					String bankCard = respStr.substring(respStr.indexOf("<BANKCARD>") + 10, respStr.indexOf("</BANKCARD>"));
					String orderDate = respStr.substring(respStr.indexOf("<ORDERDATE>") + 11, respStr.indexOf("</ORDERDATE>"));
					result.setAmt(amt);
					result.setOrderDate(orderDate);
					result.setBankCard(bankCard);
				}
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
