/**
 * 
 */
package com.store.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.alipay.direct.AlipayConfig;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayCore;
import com.jiuyuan.service.common.HttpClientService;
import com.store.entity.ShopStoreOrder;
import com.yujj.util.uri.UriParams;

/**
 * @author LWS
 *
 */
@Service
public class ChargeDelegator {
    private static final Logger logger = LoggerFactory.getLogger("PAY");

    @Autowired
    private ChargeFacade chargeFacade;
    
    @Autowired
    private WeixinChargeFacade weixinChargeFacade;
    
    @Autowired
    private HttpClientService httpClientService;
    
    public String mobilePayDisposeNew(ShopStoreOrder order) {
    	// 生成请求数据给客户端做自行处理
    	// String paramString = MobileAlipaySubmit.buildRequestParaString(sParaTemp);
    	String paramString = chargeFacade.buildRequestParaString4Mobile(order);
    	logger.info("redirect pay sdk, orderNo:{}, paramString:{}", order.getOrderNo(), paramString);
    	return paramString;
    }

	public Map<String, Object> weixinPayDisposeNew(ShopStoreOrder order, String ip) {
		logger.error("order:" + order.toString()+";ip:"+ip);
    	return weixinChargeFacade.genPayData4AppNew(order, ip);
    }

	/**
     * 
     * @param request
     * @return
     */
    public String payCallbackDispose(UriParams uriParams,String version) {
        if (StringUtils.equals(uriParams.getSingle("sign_type"), "RSA")) {
        	logger.error("UriParams:mobilePayCallbackDispose:" + uriParams.toString());
            return chargeFacade.mobilePayCallbackDispose(uriParams, version);
        } else {
        	logger.error("UriParams:payCallbackDispose:" + uriParams.toString());
            return chargeFacade.payCallbackDispose(uriParams, version);
        }
    }

	public String weixinPayCallbackDispose(String requestBody, boolean app ,String version) {
		logger.error("requestBody:" + requestBody+";app:"+app+";version:"+version);
        return weixinChargeFacade.weixinPayCallbackDispose(requestBody, app, version);
    }
	
    /**
     * 支付宝退款
     * @param order
     * @return
     * @throws AlipayApiException 
     * @throws UnsupportedEncodingException 
     */
	public void alipayRefund(StoreOrderNew order,String refundAmount,String refundReason) throws Exception {
//	    //支付宝传入退款参数
//		final Map<String,String> result = new HashMap<String,String>();
//		httpClientService.execute(new HttpClientQuery("alipayRefund") {
//			
//			@Override
//			public void initLog(LogBuilder log) {
//				log.append("orderNo", "S"+order.getOrderNo());
//			}
//			
//			@Override
//			public CachedHttpResponse sendRequest() throws Exception {
//		
//		        String url = "https://openapi.alipay.com/gateway.do";
//		        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//		      //HttpClient  
//		        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();  
//		  
//                String result = null;
//		        try {
//					
//		        	List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//		        	List<NameValuePair> BizParams = new LinkedList<NameValuePair>();
//		        	BizParams.add(new BasicNameValuePair("out_trade_no", "S"+order.getOrderNo()));
//		        	BizParams.add(new BasicNameValuePair("trade_no", order.getPaymentNo()));
//		        	BizParams.add(new BasicNameValuePair("refund_amount", refundAmount));
//		        	BizParams.add(new BasicNameValuePair("refund_reason", refundReason));
//		        	BizParams.add(new BasicNameValuePair("out_request_no", "HZ01RF001"));
//		        	BizParams.add(new BasicNameValuePair("operator_id", "OP004"));
//		        	BizParams.add(new BasicNameValuePair("store_id", "NJ_S_002"));
//		        	BizParams.add(new BasicNameValuePair("terminal_id", "NJ_T_003"));
//		        	
//		        	packageParams.add(new BasicNameValuePair("biz_content", AlipayCore.genBizContent(BizParams)));
//		        	packageParams.add(new BasicNameValuePair("charset", "utf-8"));
//		        	packageParams.add(new BasicNameValuePair("format", "JSON"));
//		        	packageParams.add(new BasicNameValuePair("method", "alipay.trade.refund"));
//		        	packageParams.add(new BasicNameValuePair("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
//		        	packageParams.add(new BasicNameValuePair("version", "1.0"));
//		        	packageParams.add(new BasicNameValuePair("sign_type", "RSA"));
//		        	String app_id = "2016101902242172";
//		        	packageParams.add(new BasicNameValuePair("sign", AlipayCore.genSign(packageParams,app_id)));
//		        	packageParams.add(new BasicNameValuePair("app_id", app_id));
//		        	packageParams.add(new BasicNameValuePair("aliKey",AlipayConfig.ali_public_key ));
////		        	String request = JsonUtil.toJSON(packageParams);
//		        	
////		        	request = EncodeUtil.encodeURL(request, "UTF-8");
////		        	StringEntity se = new StringEntity(request);
////		        	se.setContentType("test/json");
////		        	se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
////                    
//		        	url = url+"?"+URLEncodedUtils.format(packageParams, "UTF-8");
//		        	HttpGet get = new HttpGet(url);
//		        	logger.info("请求参数:"+get.getURI()+",签名："+packageParams.get(packageParams.size()-2).getValue());
//		        	HttpResponse res = closeableHttpClient.execute(get); 
//		        	if(res.getStatusLine().getStatusCode() == 200){  
//		                HttpEntity entity = res.getEntity();  
//		                result =  EntityUtils.toString(entity, "UTF-8");
//		                logger.info(result);
//		            }else{
//		            	logger.info(res.getStatusLine().getStatusCode()+"");
//		            }
//				} catch (Exception e) {
//					throw e;
//				}
				
// 				//完整的符合退款支付宝参数规范的订单url
//				String ReqParams = AlipayCore.createLinkString(packageParams);
				
				

//		        CachedHttpResponse response = httpClientService.post(url, se);
//		        if(response.isStatusCodeOK()){  
//	                 logger.info("response:{}",response.getResponseText());
//		        }    
//		        
//				return response;
//			}
//
//			@Override
//			public boolean readResponse(String responseText, LogBuilder errorLog) {
//				return true;
//			}
//			
//
//		});
//		StringBuffer biz = new StringBuffer();
//		//
////		refundInfo.append("")
		String aliKey = AlipayConfig.ali_public_key;
    	String privateKey = AlipayConfig.rsa_private_key;
    	String appid = "2015072500186581";
    	StringBuffer stringBuffer = new StringBuffer("S");
    	String out_trade_no = stringBuffer.append(String.valueOf(order.getOrderNo())).toString();
    	String trade_no = order.getPaymentNo();
    	String refund_amount = refundAmount;
    	String refund_reason = refundReason;
    	AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appid,privateKey,"JSON","utf-8",aliKey,"RSA");
    	AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
    	String bizContent = "{" +
    	    	"\"out_trade_no\":\""+out_trade_no+"\"," +
    	    	"\"trade_no\":\""+trade_no+"\"," +
    	    	"\"refund_amount\":"+refund_amount+"," +
    	    	"\"refund_reason\":\""+refund_reason+"\"," +
    	    	"\"out_request_no\":\"HZ01RF001\"," +
    	    	"\"operator_id\":\"OP004\"," +
    	    	"\"store_id\":\"NJ_S_002\"," +
    	    	"\"terminal_id\":\"NJ_T_003\"" +
    	    	"}";
    	request.setBizContent(bizContent);
    	AlipayTradeRefundResponse response = alipayClient.execute(request);
    	if(response.isSuccess()){
    	logger.info("支付宝退款成功："+request.getBizContent());
    	throw new RuntimeException("支付宝退款成功");
    	} else {
    	logger.info("支付宝退款失败："+request.getBizContent());
    	throw new RuntimeException("支付宝退款失败");
    	}
		
	}
    
	/**
	 * 微信支付退款
	 * @param order
	 * @param refundAmount
	 * @param refundReason
	 * @throws Exception 
	 */
	public Map<String, String> weixinRefund(StoreOrderNew order, String refundAmount, String refundReason,boolean app) throws Exception {
		//系统后台向微信支付系统发送数据，生成退款
		//先向map中添加appid等请求参数
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("transaction_id", order.getPaymentNo());
		StringBuffer stringBuffer = new StringBuffer("S");
		stringBuffer.append(order.getOrderNo());
		reqData.put("out_trade_no", stringBuffer.toString());
		reqData.put("total_fee", String.valueOf((int)(100 * (order.getTotalPay() + order.getTotalExpressMoney()))));
		BigDecimal refundFee = new BigDecimal(refundAmount).multiply(new BigDecimal(100));
		reqData.put("refund_fee", String.valueOf(refundFee.intValue()));
		reqData.put("refund_desc", refundReason);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = simpleDateFormat.format(new Date());
		String out_refund_no = "njkceshi00002"+date;
		reqData.put("out_refund_no", out_refund_no);
		logger.info(out_refund_no);
		
		reqData = fillRequestData(reqData, PaymentType.WEIXINPAY_SDK, "MD5");
		
		//发送请求带证书
		 String respXml = requestWithCert("https://api.mch.weixin.qq.com/secapi/pay/refund", reqData, WeixinPayConfig.getHttpConnectTimeoutMs(), WeixinPayConfig.getHttpReadTimeoutMs(),PaymentType.WEIXINPAY_SDK);
		 return processResponseXml(respXml,PaymentType.WEIXINPAY_SDK);
	}
	
	
    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr,PaymentType paymentType) throws Exception {
        String RETURN_CODE = "return_code";
        String RESULT_CODE = "result_code";
        String return_code;
        String result_code;
        Map<String, String> respData = WeixinPayCore.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        }
        else {
        	logger.error(String.format("No `return_code` in XML: %s", xmlStr));
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals("FAIL")) {
        	logger.error("退款失败！");
        	logger.info(respData.toString());
            return respData;
        }
        else if (return_code.equals("SUCCESS")) {
           if (WeixinPayCore.isSignatureValid(respData,WeixinPayConfig.getApiKey(paymentType),"MD5")) {
               logger.info("成功响应！");
               if(respData.containsKey(RESULT_CODE)){
            	  result_code = respData.get(RESULT_CODE);
            	  if(result_code.equals("FAIL")){
            		  logger.info("退款失败！");
            	  }else if(result_code.equals("SUCCESS")){
            		  logger.info("退款成功！");
            	  }
               }
        	   return respData;
           }
           else {
        	   logger.debug("无效签名！");
               throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
           }
        }
        else {
        	logger.error(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }
	
	/**
     * 需要证书的请求,比如退款，撤销订单
     * @param url String
     * @param reqData 向wxpay post的请求数据  Map
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs 超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithCert(String url, Map<String, String> reqData,
                                  int connectTimeoutMs, int readTimeoutMs,
                                  PaymentType paymentType) throws Exception {
        String msgUUID= reqData.get("nonce_str");
        String reqBody = WeixinPayCore.mapToXml(reqData);
        String resp = requestOnce(url, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, true,paymentType);
        return resp;
    }
    
    /**
     * 请求，只请求一次，不做重试
     * @param url
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @param useCert 是否使用证书，针对退款、撤销等操作
     * @param paymentType 
     * @return
     * @throws Exception
     */
    private String requestOnce(String url, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert, PaymentType paymentType) throws Exception {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            // 证书
            char[] password = WeixinPayConfig.getMchId(paymentType).toCharArray();
            InputStream certStream = WeixinPayConfig.getCertStream();
            
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());

            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
        }
        else {
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

       
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + WeixinPayConfig.getMchId(paymentType));  // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");

    }
    
	
    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData,PaymentType paymentType,String signType) throws Exception {
        reqData.put("appid", WeixinPayConfig.getAppId(paymentType));
        reqData.put("mch_id", WeixinPayConfig.getMchId(paymentType));
        reqData.put("nonce_str", WeixinPayCore.genNonceStr());
        reqData.put("sign_type", signType);
        reqData.put("sign", WeixinPayCore.generateSignature(reqData, WeixinPayConfig.getApiKey(paymentType), signType));
        return reqData;
    }
    
}