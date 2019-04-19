package com.yujj;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.jiuyuan.entity.newentity.alipay.direct.AlipayConfig;

class alipayRefund {
	   

		
	
    

    public static void main(String args[]) throws AlipayApiException
    {	
    	String aliKey = AlipayConfig.ali_public_key;
    	String privateKey = AlipayConfig.rsa_private_key;
    	String appid = "2015072500186581";
    	String out_trade_no = "S784";
//    	String trade_no = "2017011221001004270237701170";
    	String trade_no = "2017121321001004020549576764";
    	String refund_amount = "0.01";
    	String refund_reason = "正常退款";
//    	String aliKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
//    	String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMZYBIwKoen8lwRI+WJtyubqBbeqCHTkcS0/swrEjd0oIVEd2uFNQ0JDgpTnfyTtoJs7GJFOgmFohK2xxepOTDe3fbwAOC8NUWTswWgU3x9WitokuIa8Z4et2UqoXxYDNA1En8Oy0UjE4rvoP8d0T6zRdL5SG5BjREaLDAaa0pWZAgMBAAECgYEAm4wDZOAhwqK4vD+OdEauTRFSkoriPum4aEgAXX1v0/TYzAih0vcIvDq9eZFjAM7qmVJrHel4DnQtORqln+7vjY97aDTFhr0QGc84RzelGtAgG+ra0s9uNtm3lOwPDZ2khRIDW8m3FxP03gmtKiGvLynmpIhn8miwfORs7ic0cAECQQD3Yo2zKib3CCVJfLfJ4GDWTsWlAjAh0K1tNOVQMgjjmUZg2pC2u23c/+FvxkmQQ0r19TkR/CY6Qe8FczRwkPMZAkEAzUBCrpzY6iktT7KitJG/xRiyAElOrtOg5HZ/lfWnqxmT87oh7LW4GpKH6fbQHyzp2fR7CMrI2sEVc0IPaPGGgQJALKe3mF3FhtYLlQZUTraYBFdXyf9pHNGEXLAtrJo7jIoAcD9D3BhdLoVp9jk+0jGzeE55rMttQxrfwIYZMzCXEQJBAMkJ6Eaf2teA/aDSmAvFttCXH8KoCymyoCUm7FE2DMTKiOBxsEjqtSlR3U6NMc1XcLbLgLdb6OBbv2bljbJ84AECQQCk+kmViCyjD72IFBYb2+8a1Yhnc8q2q/HukEHikVJrtnZvB6wBlD8nqqcennsMpugfRkmnTYFFVQopKjoMT1Kp";
    	
    	AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appid,privateKey,"JSON","utf-8",aliKey,"RSA");
    	AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
    	request.setBizContent("{" +
    	"    \"out_trade_no\":\""+out_trade_no+"\"," +
    	"    \"trade_no\":\""+trade_no+"\"," +
    	"    \"refund_amount\":"+refund_amount+"," +
    	"    \"refund_reason\":\""+refund_reason+"\"," +
    	"    \"out_request_no\":\"HZ01RF003\"," +
    	"    \"operator_id\":\"OP004\"," +
    	"    \"store_id\":\"NJ_S_002\"," +
    	"    \"terminal_id\":\"NJ_T_003\"" +
    	"  }");
    	AlipayTradeRefundResponse response = alipayClient.execute(request);
    	if(response.isSuccess()){
    	System.out.println("调用成功");
    	} else {
    	System.out.println("调用失败");
    	}
    	
        
    }
}
