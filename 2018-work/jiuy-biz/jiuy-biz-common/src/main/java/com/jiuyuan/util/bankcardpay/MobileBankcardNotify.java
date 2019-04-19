package com.jiuyuan.util.bankcardpay;

import java.util.Map;

import com.jiuyuan.entity.newentity.alipay.direct.AlipayConfig;
import com.jiuyuan.entity.newentity.alipay.direct.AlipayCore;
import com.jiuyuan.util.EncodeUtil;

/* *
 *类名：BankCardNotify
 *功能：一网通通知处理类
 *详细：处理一网通知返回
 *版本：1.0
 *日期：2016-06-21
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考

 *************************注意*************************
 *调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class MobileBankcardNotify {

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(Map<String, String> Params, String sign) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
        //获取待签名字符串
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
        if (AlipayConfig.mobile_sign_type.equals("RSA")) {
            //isSign = RSA.verify(preSignStr, sign, AlipayConfig.ali_public_key, AlipayConfig.input_charset);
        }
        return isSign;
    }

	
	public static boolean verifySign(String returnStr){
		try
		{
			cmb.netpayment.Security pay = new cmb.netpayment.Security(MobileBankcardNotify.class.getClassLoader().getResource("/public.key").getPath());
			//cmb.netpayment.Security pay = new cmb.netpayment.Security("c://public.key");

//			byte[] baSig = "Succeed=Y&BillNo=000000&Amount=12.00&Date=20001221&Msg=付款请求已被银行接受.&Signature=9|96|42|124|72|152|158|163|254|181|233|185|138|15|6|89|43|167|41|171|28|218|209|216|211|47|169|5|243|235|2|225|189|233|84|130|206|204|49|236|196|127|109|65|193|110|229|29|107|135|174|44|185|109|250|70|163|225|137|18|84|205|236|82|".getBytes("GB2312");
            byte[] baSig = returnStr.getBytes("GB2312");
            		//"Succeed=Y&CoNo=000004&BillNo=8104700022&Amount=60&Date=20071213&MerchantPara=8120080420080414701013700022&Msg=00270000042007121307321387100000002470&Signature=177|48|67|121|22|40|125|29|39|162|103|204|103|156|74|196|63|148|45|142|206|139|243|120|224|193|84|46|216|23|42|29|25|64|232|213|114|3|22|51|131|76|169|143|183|229|87|164|138|77|185|198|116|254|224|68|26|169|194|160|94|35|111|150|".getBytes();
			boolean bRet = pay.checkInfoFromBank(baSig);
			return bRet;
		}
		catch(Exception e)
		{
			return false;
		}
		
    }
	public static void main(String args[]){
		String baSig = " sign callback log:RequestData=%7B%22NTBNBR%22%3A%22P4569764%22%2C%22TRSCOD%22%3A%22BKQY%22%2C%22DATLEN%22%3A%22586%22%2C%22COMMID%22%3A%22CBFCROUKANCWXWGQMUMWPKWMHJIMGLEQJJVBGJL%22%2C%22BUSDAT%22%3A%22PHhtbD48Y3VzdF9hcmdubz4yMDIzOTY5MTgwNDAyPC9jdXN0X2FyZ25vPjxyZXNwY29kPkNNQk1COTk8L3Jlc3Bjb2Q+PHJlc3Btc2c+562+572y5Y2P6K6u5oiQ5YqfPC9yZXNwbXNnPjxub3RpY2VwYXJhLz48Y3VzdF9uby8+PGN1c3RfcGlkdHk+MTwvY3VzdF9waWR0eT48Y3VzdF9vcGVuX2RfcGF5Pk48L2N1c3Rfb3Blbl9kX3BheT48Y3VzdF9waWRfdj40NTI0ODg3MDU2MzA2ODc0NjcyNTIwNTU0NzgwNzU8L2N1c3RfcGlkX3Y+PC94bWw+%22%2C%22SIGTIM%22%3A%22201608010712250720%22%2C%22SIGDAT%22%3A%22VOjiWMB8pq7GbFwG1y1r+2b7WbpjNSc499qtsOgLmLQgSzCxOwvhvtUjQIEMMr2GQerOQ0p9EmcGuR5I3kGVhcCWCbv+j1a+SQ2CkIKSLqnq43ug8VFWXEpdfnWAEWOR4Q1qrUoI2B6YwKvgmn7utjuiju%2FgX7S78SfVeXsvwVM%3D%22%7D";
		//String baSig = "Succeed=Y&CoNo=053588&BillNo=0000043730&Amount=0.01&Date=20160712&MerchantPara=out_trade_no=43730&Msg=05710535882016071216271267100000008000&Signature=58|185|50|91|142|95|245|210|187|152|118|66|16|200|6|10|127|232|186|55|61|196|232|203|72|151|8|207|54|211|222|25|198|223|98|24|219|61|99|52|45|172|253|59|128|78|38|166|217|35|136|189|24|178|71|82|113|96|229|62|171|182|26|112|";
		//baSig=baSig.replaceAll("%3D", "=").replaceAll("%7C", "|");
		baSig = EncodeUtil.decodeBase64(baSig, "ISO8859-1");
		verifySign(baSig);
	}


}
