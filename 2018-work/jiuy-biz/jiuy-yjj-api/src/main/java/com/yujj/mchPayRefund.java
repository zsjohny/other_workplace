package com.yujj;
import java.lang.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthToggleButtonUI;

//import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.oss.common.utils.DateUtil;
import com.cmb.b2b.B2BResult;
import com.cmb.b2b.Base64;
import com.cmb.b2b.SelfIssuedCert;
import com.jiuyuan.constant.Client;
import com.jiuyuan.util.bankcardpay.BankCardPayConfig;
//import com.yujj.business.facade.ChargeFacade;
import com.yujj.business.service.OrderService;
import com.yujj.entity.order.Order;

import cmb.netpayment.Settle;

import java.io.*;
class mchPayRefund {
	   
    static private Settle settle;
    static private int iRet;
    private static void testQueryUnsettledOrder()
    {
    	StringBuffer strbuf = new StringBuffer();
    	iRet = settle.QueryUnsettledOrder(strbuf);
    	if (iRet == 0)
		{
    	}
    	else
		{
		}
    }
    private static void testQuerySettledOrder()
    {
    	StringBuffer strbuf = new StringBuffer();
    	iRet = settle.QuerySettledOrder("20040101","20041010",strbuf);
    	if (iRet == 0)
		{
    	}
    	else
		{
		}
    }
    private static void testQueryTransact()
    {
    	StringBuffer strbuf = new StringBuffer();
    	iRet = settle.QueryTransact("20040302",strbuf);
    	if (iRet == 0)
		{
    	}
    	else
		{
		}
    }
    private static void testQueryPagedSettledOrder()
    {
    	StringBuffer strbuf = new StringBuffer();
        settle.PageReset();
        do
        {
    	    iRet = settle.QuerySettledOrderByPage("20040101","20041010",10,strbuf);
        } while (iRet==0 && !settle.m_bIsLastPage);
    	if (iRet == 0)
		{
    	}
    	else
		{
		}
    }
    private static void init()
    {
    	System.out.println("11");
        settle = new Settle();
       // settle.testit();
    	iRet = settle.SetOptions("payment.ebank.cmbchina.com");
    	//iRet = settle.SetOptions("61.144.248.29");
   		if (iRet == 0)
		{
    	}
    	else
		{
			return;
		}

   			iRet = settle.LoginC(BankCardPayConfig.BRANCH_ID, BankCardPayConfig.CO_NO, BankCardPayConfig.CO_PSWD);
   			int result=0;

    	
    	if (iRet == 0)
		{
    		System.out.println("111");
			iRet = settle.RefundOrder("20160712","0000043734","0.01","test refund",BankCardPayConfig.KEY);
			String err = settle.GetLastErr(result);
			System.out.println(err);
		    settle.Logout();

	    }
    	else
		{
			return;
		}
    	
    	
    	
    	
    	StringBuffer sBuffer= new StringBuffer();
    	//settle.QueryUnsettledOrderByPage(10, sBuffer);
//    	settle.QuerySettledOrderByPage("20160725","20160812",10, sBuffer);
        List<String> noStrings=new ArrayList<String>();
       
        
//        for(int i=43640; i <43750 ;i++){
//        	 noStrings.add(String.format("%010d", i));
//        }
        noStrings.add(String.format("%010d", 160));
        StringBuffer sb = new StringBuffer();
        
//        settle.QuerySettledOrderByPage("20160710","20160719",40,sb);
//        settle.QueryRefundByDatePage("20160710","20160719",10,sb);
      //  settle.QueryUnsettledOrderByPage(10, sb);
        
        
        for(String no:noStrings){
//        	if (Integer.parseInt(no) % 30 == 0) {
//        	}
//        	result = settle.QuerySingleOrder(no.substring(0,8),no.substring(8, 18),sBuffer);
        	result = settle.QuerySingleOrder("20160715",  no ,sBuffer);
        	if(result == 0){
        		iRet = settle.RefundOrder("20160715", no , "3","test refund", BankCardPayConfig.KEY);
        		if(iRet > 0){
        		}else{
        		}
        		
        	}
        }
        
//        String merchantPara = "out_trade_no%3D2016062809541906934274067";
//   
//    		String[] splitArray = merchantPara.split("%3D");
//         result = settle.QuerySingleOrder(UtilDate.getDate(),"2016062720590561772990068".substring(8, 18),sBuffer);
        
        String err = settle.GetLastErr(result);
        settle.Logout();

       // testVerifySign();
    }
    private static void testQueryPagedTransact()
    {
    	StringBuffer strbuf = new StringBuffer();
        settle.PageReset();
        do
        {
    	    iRet = settle.QueryTransactByPage("20040302",10,strbuf);
        } while (iRet==0 && !settle.m_bIsLastPage);
    	if (iRet == 0)
		{
    	}
    	else
		{
		}
    }
    private static void testVerifySign()
    {
		try
		{
			cmb.netpayment.Security pay = new cmb.netpayment.Security("c:\\public.key");

//			byte[] baSig = "Succeed=Y&BillNo=000000&Amount=12.00&Date=20001221&Msg=付款请求已被银行接受.&Signature=9|96|42|124|72|152|158|163|254|181|233|185|138|15|6|89|43|167|41|171|28|218|209|216|211|47|169|5|243|235|2|225|189|233|84|130|206|204|49|236|196|127|109|65|193|110|229|29|107|135|174|44|185|109|250|70|163|225|137|18|84|205|236|82|".getBytes("GB2312");
       //     byte[] baSig = "Succeed=Y&CoNo=000004&BillNo=8104700022&Amount=60&Date=20160622&MerchantPara=2015120710203249283722189&Msg=00270000042007121307321387100000002470&Signature=177|48|67|121|22|40|125|29|39|162|103|204|103|156|74|196|63|148|45|142|206|139|243|120|224|193|84|46|216|23|42|29|25|64|232|213|114|3|22|51|131|76|169|143|183|229|87|164|138|77|185|198|116|254|224|68|26|169|194|160|94|35|111|150|".getBytes();
			 byte[] baSig = "Succeed=Y&CoNo=000004&BillNo=8104700022&Amount=60&Date=20071213&MerchantPara=8120080420080414701013700022&Msg=00270000042007121307321387100000002470&Signature=177|48|67|121|22|40|125|29|39|162|103|204|103|156|74|196|63|148|45|142|206|139|243|120|224|193|84|46|216|23|42|29|25|64|232|213|114|3|22|51|131|76|169|143|183|229|87|164|138|77|185|198|116|254|224|68|26|169|194|160|94|35|111|150|".getBytes();
				          
            boolean bRet = pay.checkInfoFromBank(baSig);
		}
		catch(Exception e)
		{
		}
    }
    
    
    public static B2BResult privateSign(String sOrder) {  
    	
    	  char []certPassword = {'1','2','3','4','5','6','7','8'};  //私钥密码 
//    	char[] certPassword = {'U','m','V','z','e','O','B','o'};   //UmVzeOBo
    //	char[] certPassword = {'1','1','1'};
    	String sCertPath = "C:\\M000001269.jks";
    	//String sCertPath = "D:\\javaProj\\B2BFileCert\\selfissuedcert.jks";   //jks私钥
//    	 sOrder = "NTBNBR=N0004949&TRSCOD=CMCX&COMMID=1465293742&SIGTIM=201606071802220000&BUSDAT=PHhtbD48bWVyY2hfZGF0ZT4yMDE2MDYwNzwvbWVyY2hfZGF0ZT48bWVyY2hfdGltZT4xODAyMjI8L21lcmNoX3RpbWU+PG1lcmNoX3NlcmlhbD48IVtDREFUQVtTVEQyMDE2MDYwNzE2MDc0MjE0NV1dPjwvbWVyY2hfc2VyaWFsPjxjdXN0X2FyZ25vPjcwMDIwMTYwNjA3MTYwNzQyMTMxNDM8L2N1c3RfYXJnbm8+PC94bWw+";
    	//读取证书
    	byte []bPFXFile = null;				
		File fCert = new File(sCertPath);
		if(fCert.exists()){
			int iFileLen = (int)fCert.length();
			if(iFileLen==0){
				
			}
			bPFXFile = new byte[iFileLen];
			FileInputStream fis = null;
			try{
				fis = new FileInputStream(sCertPath);
				fis.read(bPFXFile, 0, iFileLen);
				fis.close();									
			}catch(IOException ex){
				
			}
		}else{
			
		}
		
		//初始化证书
		B2BResult bRet = SelfIssuedCert.initCert(bPFXFile, certPassword);
		if(bRet.isError()){
			
		}
		
		//签名
		bRet = SelfIssuedCert.signature(sOrder.getBytes());
		if(bRet.isError()){
		}else{
		}
    	return bRet;
    	
    	
    	
    	
  
    }
    
    
 
		public static void post(String URL, String PKG) {  
	        // 创建默认的httpClient实例.    
	        CloseableHttpClient httpclient = HttpClients.createDefault();  
	        // 创建httppost    
	        HttpPost httppost = new HttpPost(URL);  
	        // 创建参数队列    
	        List formparams = new ArrayList();  
	        formparams.add(new BasicNameValuePair("RequestData", PKG));  
	        UrlEncodedFormEntity uefEntity;
	        
	        try {  
	            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
	            httppost.setEntity(uefEntity);  
	            CloseableHttpResponse response = httpclient.execute(httppost);  
	            try {  
	                HttpEntity entity = response.getEntity();  
	                if (entity != null) {
	                }  
	            } finally {  
	                response.close();  
	            }  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (UnsupportedEncodingException e1) {  
	            e1.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            // 关闭连接,释放资源    
	            try {  
	                httpclient.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }
    
    
//    String sSource= "CHKNUM=123 ;C_PAYBBK=东莞 ;ORDSTS= ;PAYACC=769900038110701 ;ORDSTA=OPR ;C_RELNBR=中天世纪集团 ;SUBORD= ;CCYNBR=10 ;TRSAMT=8.55 ;PAYBBK=69 ;REFORD=201506030855";
//    String sSign= "ZZdn0W+cc9/p0tDXZxeTLhqli+zzuF15LOYXaCdpTnX9BLUx2mav+Q8tj3d68RxBxTbps4ue9tURgn2mwJoDtE/jfzgriV0nGqgTi2qj3RzAtNRRDNwo+m7dm7dsUDn5h22h79FvK81BPBITqAanxc3sf4xKtTgYRsExsSPE+xo=";
//    String sBase64PubKey = "MIGJAoGBANmziXwTPcXgwbSQXtFFIv6mfwybFUTed91fOPHp3esfwh8Ns281EtLRfRvmImc/IU7yxttSjUibjNfQzxcDTGee+IbKfkTqhxyLJRclGCiSmFZvkHBph1WGcuV1y12KYeglOTc0URAVq/eC7KBHniLe3KasNzyVa24bwRzNW7wXAgMBAAE=";
//    			
//                //初始化公钥,验证签名
//    			B2BResult bRet = FirmbankCert.initPublicKey(sBase64PubKey);
//    			if(bRet.isError()){
//    				return;
//    			}			
//    			bRet= FirmbankCert.verifySignatureByPubKey(sSource.getBytes(), sSign);
//    			
//    			if(bRet.isError()){
//    			}else{
//    }
    
  
	
	

    
    
    
    

    public static void main(String args[])
    { 
    	init();
//    	SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//    	long nowTime = System.currentTimeMillis();
//    	long startTime = 0;
//    	long endTime = 0;
//    	try {
//			startTime = sdf.parse("2016-11-02 15:08:32").getTime();
//		
//		} catch (ParseException e) {
//		
//			e.printStackTrace();
//		}
//    	
//    	
//    	Set<String> set=new HashSet<String>();
//		set.add("a");
//		set.add("b");
////		set.remove("a");
////		set.remove("a");
//		for(String as:set){
//		}
    	
//    	String callBack = "<xml><openid>oLRBVs3KSiPAdQXkQaaVA4YRaUQU</openid><sub_mch_id>1217189101</sub_mch_id><return_code>SUCCESS</return_code><time_end>20160801010932</time_end><mch_id>1272886401</mch_id><trade_type>APP</trade_type><cash_fee>4600</cash_fee><is_subscribe>N</is_subscribe><bank_type>ICBC_DEBIT</bank_type><out_trade_no>10645</out_trade_no><transaction_id>4005402001201607300149935720</transaction_id><total_fee>4600</total_fee><appid>wx9c5d8e90dc52e29c</appid><noncestr>20150531233956996351</noncestr><result_code>SUCCESS</result_code></xml>";
//    	Map<String, String> params = WeixinPayCore.decodeXml(callBack);
//    	 if (Signature.checkIsSignValidFromResponseString(params, WeixinPayConfig.getApiKey(true))) {
//    		 
//    	 }
//    	 String regEx ="<a>[\\s\\S]*?</a>";
//         String s = "<a>122</a><a>456</a><a>789</a>";
//         Pattern pat = Pattern.compile(regEx);
//         Matcher mat = pat.matcher(s);
//         boolean rs = mat.find();
//         for(int i=1;i<=mat.groupCount();i++){
//         }
		
//    	  int count = 0;      
//    	  String imgHead = Client.OSS_IMG_SERVICE;
//          String regEx = "\"id\":([0-9_]*),\"clothesNum\":\"[A-Z a-z 0-9_]*\"[^}]*\"cash\":[0-9.]*,\"jiuCoin\":[0-9.]*";  
//          String str ="{\"id\":2059,\"clothesNum\":\"S663\",\"name\":\"简约蕾丝拼接上衣\",\"promotionImage\":\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14715754490311471575449031.jpg\",\"brandId\":550,\"brandIdentity\":\"\",\"marketPrice\":299,\"marketPriceMin\":0,\"marketPriceMax\":0,\"price\":0,\"summaryImages\":\"[\"http:\\/\\/yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14715754490311471575449031.jpg\",\"http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/14715754536371471575453637.JPG\"]\",\"remainCount\":68,\"cash\":30,\"jiuCoin\":269,";
////          String str = "http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/sdfsdf.png  http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/sdf dfd/df1212/.jpg   http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/32432 /324.JPG ";    
////          String regEx = "[\\u4e00-\\u9fa5]";  
////          String str = "中文fdas测试 ";        
//          Pattern pat = Pattern.compile(regEx);
//          Matcher mat = pat.matcher(str);
//         while (mat.find()) { 
//                  count = count + 1;                  
//          } 


        
    }
}
