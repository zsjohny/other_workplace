package com.yujj;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.cmb.b2b.B2BResult;
import com.cmb.b2b.Base64;
import com.cmb.b2b.FirmbankCert;
import com.cmb.b2b.SelfIssuedCert;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
class mchProtocolCancel {
	   

    public static B2BResult privateSign(String sOrder) {  
    	
    	char []certPassword = {'1','2','3','4','5','6','7','8'};  //私钥密码 
    	String sCertPath = "C:\\M000001269.jks";
    	
    	//    	sCertPath = mchProtocolCancel.class.getClassLoader().getResource("/M000001269.jks").getPath();
    	//String sCertPath = "D:\\javaProj\\B2BFileCert\\selfissuedcert.jks";   //jks私钥
//    	 sOrder = "NTBNBR=N0004949&TRSCOD=CMCX&COMMID=1465293742&SIGTIM=201606071802220000&BUSDAT=PHhtbD48bWVyY2hfZGF0ZT4yMDE2MDYwNzwvbWVyY2hfZGF0ZT48bWVyY2hfdGltZT4xODAyMjI8L21lcmNoX3RpbWU+PG1lcmNoX3NlcmlhbD48IVtDREFUQVtTVEQyMDE2MDYwNzE2MDc0MjE0NV1dPjwvbWVyY2hfc2VyaWFsPjxjdXN0X2FyZ25vPjcwMDIwMTYwNjA3MTYwNzQyMTMxNDM8L2N1c3RfYXJnbm8+PC94bWw+";
    	//读取证书
    	byte [] bPFXFile = null;				
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
    
    public static B2BResult privateVerify(String sSource) {  
    	
//    	String sSource= "NTBNBR= N0004949&TRSCOD=CMCX&COMMID=1465293742&SIGTIM=201606071802220000&BUSDAT=PHhtbD48bWVyY2hfZGF0ZT4yMDE2MDYwNzwvbWVyY2hfZGF0ZT48bWVyY2hfdGltZT4xODAyMjI8L21lcmNoX3RpbWU+PG1lcmNoX3NlcmlhbD48IVtDREFUQVtTVEQyMDE2MDYwNzE2MDc0MjE0NV1dPjwvbWVyY2hfc2VyaWFsPjxjdXN0X2FyZ25vPjcwMDIwMTYwNjA3MTYwNzQyMTMxNDM8L2N1c3RfYXJnbm8+PC94bWw+";//签名原文-按照返回报文字段组织
//    	String sSign= "PHhtbD4NCjxyZXNwY29kPkNNQk1COTk8L3Jlc3Bjb2Q+DQo8cmVzcG1zZz7lj5bmtojmiJDlip88L3Jlc3Btc2c+DQo8bWVyY2hfZGF0ZT4yMDE2MTAyODwvbWVyY2hfZGF0ZT4NCjxtZXJjaF90aW1lPjE1MTQyOTwvbWVyY2hfdGltZT4NCjxtZXJjaF9zZXJpYWw+MjAxNjEwMjgxNTE0MjkwNzI4NjA1NTY8L21lcmNoX3NlcmlhbD4NCjxiYW5rX3NlcmlhbD4yNzdZNjEwMjgwMDAwNDMxPC9iYW5rX3NlcmlhbD4NCjwveG1sPg==";//签名，对应返回报文里的SIGDAT字段内容
//    	String sBase64PubKey = "MIGJAoGBANmziXwTPcXgwbSQXtFFIv6mfwybFUTed91fOPHp3esfwh8Ns281EtLRfRvmImc/IU7yxttSjUibjNfQzxcDTGee+IbKfkTqhxyLJRclGCiSmFZvkHBph1WGcuV1y12KYeglOTc0URAVq/eC7KBHniLe3KasNzyVa24bwRzNW7wXAgMBAAE="; //企业网银公钥BASE64字符串
    	sSource= "NTBNBR=P4569764&TRSCOD=CMQX&COMMID=0000256&SIGTIM=201611031022584132&BUSDAT=PHhtbD4NCjxyZXNwY29kPkNNQk1COTk8L3Jlc3Bjb2Q+DQo8cmVzcG1zZz7lj5bmtojmiJDlip88L3Jlc3Btc2c+DQo8bWVyY2hfZGF0ZT4yMDE2MTEwMzwvbWVyY2hfZGF0ZT4NCjxtZXJjaF90aW1lPjEwMjQ0MTwvbWVyY2hfdGltZT4NCjxtZXJjaF9zZXJpYWw+MjAxNjExMDMxMDI0NDExMDA2MzYzMzY8L21lcmNoX3NlcmlhbD4NCjxiYW5rX3NlcmlhbD4wOTRFNjExMDMwMDAxMTIwPC9iYW5rX3NlcmlhbD4NCjwveG1sPg==";//签名原文-按照返回报文字段组织
    	String sSign= "RxaNnjNhRoKWIm8KWODK3h3mNySnT49iz07F0QWreCK6J6KwY2uLMZC3x58q2jwyWnTSIoov3dty0JAmB/A/wLrfb/17LwwjzTavYPPsFwBPyZsILgT+aMK7ZsWeUVLpQmscm7ZoVt24aupVZKUWs6zbs8u8tNrAMz7SGZ8uHNg=";//签名，对应返回报文里的SIGDAT字段内容
    	String sBase64PubKey = "MIGJAoGBALKsktbh7j9O9pM0p7qnxxImgODqxjpiT7Xl2bvZCywJtwsNI6CchqAagOYGJjG0NZsnjFunTw5YM9TD5KxsUOILAL6IaNMH/fWREhVjkUDJ4CYtLWlKozElvXRp1iZxf66yHHhN4t7TE5S9NWpEBSn37TEfFLU99Go1WReI1XN1AgMBAAE=";
    	
    	
   	 
    	            //初始化公钥,验证签名
    				B2BResult bRet = FirmbankCert.initPublicKey(sBase64PubKey);
    				if(bRet.isError()){
    					
    				}			
    				bRet= FirmbankCert.verifySignatureByPubKey(sSource.getBytes(), sSign);
    				
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
		
		
		
		
    

    public static void main(String args[])
    {
    	String TURL = "http://58.61.30.110/CmbBank_B2B/UI/DIDI/DoBusiness.ashx";
    	String URL = "https://b2b.cmbchina.com/CmbBank_B2B/UI/DIDI/DoBusiness.ashx";    	
//    	String mchNo = "P0020056";  
    	String mchNo = "P4569764";  
    	String COMMID = "0000256";  //201610261734550001
    	String timeStr = UtilDate.getTimeStrMs()+"1";  //201610261734550001
    	String timeDate = UtilDate.getDate();  //20161026
    	String timeHms = UtilDate.getOrderNum().substring(8);  //173455
    	String squ = UtilDate.getTimeStrMs() + (int)(Math.random() * 1000000);
    	String pno = "2069125201341"; //需要取消的协议号  2020585175156 2062614093832 2020585175156  2215111184846
//    春哥	42257200732  2069125201341
    	
    	//2052201195949
    	String BUSDAT = ("<xml><merch_date>" + timeDate + "</merch_date><merch_time>" + timeHms + "</merch_time><merch_serial>" + squ + "</merch_serial><cust_argno>"+ pno +"</cust_argno></xml>");
    	
    	String BUSDAT64 = Base64.encode(BUSDAT.getBytes());
    	int BUSDAT64Length = BUSDAT64.length();
    	
    	
    	String PKG = "{\"NTBNBR\":\""+mchNo+"\",\"TRSCOD\":\"CMQX\",\"DATLEN\":\""+BUSDAT64Length+"\",\"COMMID\":\""+COMMID+"\",\"BUSDAT\":\""+BUSDAT64+"\",\"SIGTIM\":\"";
    	String signBefore64 = "NTBNBR="+mchNo+"&TRSCOD=CMQX&COMMID="+COMMID+"&SIGTIM="+timeStr+"&BUSDAT="+BUSDAT64;
    	String signAfter64 = privateSign(signBefore64).getResultBase64Str();
    	PKG += timeStr;
    	PKG += "\",\"SIGDAT\":\""+signAfter64+"\"}";
   		
    	
    	String responseStr = "PHhtbD4NCjxyZXNwY29kPkNNQk1COTk8L3Jlc3Bjb2Q+DQo8cmVzcG1zZz7lj5bmtojmiJDlip88L3Jlc3Btc2c+DQo8bWVyY2hfZGF0ZT4yMDE2MTEwMzwvbWVyY2hfZGF0ZT4NCjxtZXJjaF90aW1lPjEwMjQ0MTwvbWVyY2hfdGltZT4NCjxtZXJjaF9zZXJpYWw+MjAxNjExMDMxMDI0NDExMDA2MzYzMzY8L21lcmNoX3NlcmlhbD4NCjxiYW5rX3NlcmlhbD4wOTRFNjExMDMwMDAxMTIwPC9iYW5rX3NlcmlhbD4NCjwveG1sPg==";
    	
    	post(URL,PKG);
//    	privateVerify(responseStr);
    	
        
    }
}
