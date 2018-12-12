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
public class FuiouBinCardQueryService {

	private Log logger = Log.getInstance(FuiouBinCardQueryService.class);

	@Value("${borrow.fuiou.webhost}")
	private String webhost;

	@Value("${borrow.fuiou.furl}")
	private String furl;

	@Value("${borrow.fuiou.fcd}")
	private String fcd;

	@Value("${borrow.fuiou.fkey}")
	private String fkey;

	@Value("${borrow.fuiou.qryurl}")
	private String CARD_BIN_QRY_URL;

	@Value("${borrow.fuiou.limurl}")
	private String CARD_BIN_LIM_URL;

	@Value("${borrow.fuiou.cardbin}")
	private String CARD_BIN_URL;

	public class Result {
		private String bankName;
		private String cardNo;
		private String inscd;
		private String ctp;  //01-借记卡,02-信用卡,03-准贷记卡,04-富友卡,05-非法卡号

		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}

		public String getCardNo() {
			return cardNo;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getBankName() {
			return bankName;
		}

		public String getInscd() {
			return inscd;
		}

		public void setInscd(String inscd) {
			this.inscd = inscd;
		}

		public String getCtp() {
			return ctp;
		}

		public void setCtp(String ctp) {
			this.ctp = ctp;
		}
	}

	/**
	 * 获取银行卡所属银行接口
	 * @param cardNo
	 * @return
	 */
	public Result queryByCard(String cardNo) {
		String sign = MD5.MD5Encode(new StringBuffer().append(fcd).append("|").append(cardNo).append("|").append(fkey).toString());
		String fm = new StringBuffer().append("<FM>").append("<MchntCd>").append(fcd).append("</MchntCd>").append("<Ono>").append(cardNo).append("</Ono>").append("<Sign>").append(sign).append("</Sign>").append("</FM>").toString();
		Map<String, String> params = new HashMap<String, String>();
		params.put("FM", fm);
		try {
			String respStr = HttpPostUtil.postForward(CARD_BIN_QRY_URL, params);
			logger.info("获取银行卡所属银行接口,返回数据={}",respStr);
			String rcd = respStr.substring(respStr.indexOf("<Rcd>") + 5, respStr.indexOf("</Rcd>"));
			String rDesc = respStr.substring(respStr.indexOf("<RDesc>") + 7, respStr.indexOf("</RDesc>"));
			String cTp = respStr.substring(respStr.indexOf("<Ctp>") + 5, respStr.indexOf("</Ctp>"));
			String cNm = respStr.substring(respStr.indexOf("<Cnm>") + 5, respStr.indexOf("</Cnm>"));
			String insCd = respStr.substring(respStr.indexOf("<InsCd>") + 7, respStr.indexOf("</InsCd>"));
			String rSign = respStr.substring(respStr.indexOf("<Sign>") + 6, respStr.indexOf("</Sign>"));
			// 校验签名
			if (rSign.equals(MD5.MD5Encode(rcd + "|" + fkey))) {
				Result result = new Result();
				result.setBankName(cNm); //银行名称
				result.setInscd(insCd); //银行机构号
				result.setCtp(cTp);  //卡类型
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取银行卡所属银行接口,异常信息{}",e);
			return null;
		}
	}

	public class AmtResultLimit {
		private String amtlimittime;
		private String amtlimittimelow;
		private String amtlimitday;
		private String amtlimitmonth;


		public String getAmtlimittime() {
			return amtlimittime;
		}

		public void setAmtlimittime(String amtlimittime) {
			this.amtlimittime = amtlimittime;
		}

		public String getAmtlimittimelow() {
			return amtlimittimelow;
		}

		public void setAmtlimittimelow(String amtlimittimelow) {
			this.amtlimittimelow = amtlimittimelow;
		}

		public String getAmtlimitday() {
			return amtlimitday;
		}

		public void setAmtlimitday(String amtlimitday) {
			this.amtlimitday = amtlimitday;
		}

		public String getAmtlimitmonth() {
			return amtlimitmonth;
		}

		public void setAmtlimitmonth(String amtlimitmonth) {
			this.amtlimitmonth = amtlimitmonth;
		}
	}

	/**
	 * 获取银行卡限额接口
	 * @param inscd
	 * @return
	 */
	public AmtResultLimit queryLimitByCard(String inscd) {
		String sign = MD5.MD5Encode(new StringBuffer().append(fcd).append("|").append(inscd).append("|").append(fkey).toString());
		String fm = new StringBuffer().append("<FM>").append("<MCHNTCD>").append(fcd).append("</MCHNTCD>").append("<INSCD>").append(inscd).append("</INSCD>").append("<SIGN>").append(sign).append("</SIGN>").append("</FM>").toString();
		Map<String, String> params = new HashMap<String, String>();
		params.put("FM", fm);

		try {
			String respStr = HttpPostUtil.postForward(CARD_BIN_LIM_URL, params);
			logger.info("获取银行卡限额接口,返回数据={}",respStr);
			String pescode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
			if( !"0000".equals(pescode) ){
				return null;
			}

			String mch = respStr.substring(respStr.indexOf("<MCHNTCD>") + 9, respStr.indexOf("</MCHNTCD>"));
			String pesmsg = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));
			String amtlimittime = respStr.substring(respStr.indexOf("<AMTLIMITTIME>") + 14, respStr.indexOf("</AMTLIMITTIME>"));
			/*String amtlimittimelow = respStr.substring(respStr.indexOf("<AMTLIMITTIMELOW>") + 17, respStr.indexOf("</AMTLIMITTIMELOW>"));*/
			String amtlimitday = respStr.substring(respStr.indexOf("<AMTLIMITDAY>") + 13, respStr.indexOf("</AMTLIMITDAY>"));
			//String amtlimitmonth = respStr.substring(respStr.indexOf("<AMTLIMITMONTH>") + 15, respStr.indexOf("</AMTLIMITMONTH>"));
			String rSign = respStr.substring(respStr.indexOf("<SIGN>") + 6, respStr.indexOf("</SIGN>"));
			// 校验签名
			if ( rSign.equals(MD5.MD5Encode(mch+"|"+pescode+"|"+pesmsg+ "|" + fkey)) ) {
				AmtResultLimit result = new AmtResultLimit();
				result.setAmtlimitday(amtlimitday);
				result.setAmtlimittime(amtlimittime);
				//result.setAmtlimitmonth(amtlimitmonth);
				/*result.setAmtlimittimelow(amtlimittimelow);*/
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取银行卡限额接口,异常信息{}",e);
			return null;
		}
	}



	public class CardBinResult {
		private String Rcd;
		private String RDesc;

		public String getRcd() {
			return Rcd;
		}

		public void setRcd(String rcd) {
			Rcd = rcd;
		}

		public String getRDesc() {
			return RDesc;
		}

		public void setRDesc(String RDesc) {
			this.RDesc = RDesc;
		}
	}

	/**
	 * 支持银行卡查询
	 * @param on
	 * @return
	 */
	public CardBinResult queryCardBin(String on) {

		String sign = MD5.MD5Encode(new StringBuffer().append(fcd).append("|").append(on).append("|").append(fkey).toString());
		String fm = new StringBuffer().append("<FM>").append("<MchntCd>").append(fcd).append("</MchntCd>").append("<Ono>").append(on).append("</Ono>").append("<Sign>").append(sign).append("</Sign>").append("</FM>").toString();
		Map<String, String> params = new HashMap<String, String>();
		params.put("FM", fm);

		try {
			String respStr = HttpPostUtil.postForward(CARD_BIN_URL, params);
			logger.info("支持银行卡查询接口,返回数据={}",respStr);
			String Rcd = respStr.substring(respStr.indexOf("<Rcd>") + 5, respStr.indexOf("</Rcd>"));
			String RDesc = respStr.substring(respStr.indexOf("<RDesc>") + 7, respStr.indexOf("</RDesc>"));
			String rSign = respStr.substring(respStr.indexOf("<Sign>") + 6, respStr.indexOf("</Sign>"));

			// 校验签名
			if ( rSign.equals(MD5.MD5Encode(Rcd+ "|" + fkey)) ) {
				CardBinResult result = new CardBinResult();
				if( "0000".equals(Rcd)){
					String InsCd = respStr.substring(respStr.indexOf("<InsCd>") + 7, respStr.indexOf("</InsCd>"));
					result.setRcd(Rcd);
					result.setRDesc(RDesc);
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("支持银行卡查询,异常信息{}",e);
		}

		return null;
	}

}
