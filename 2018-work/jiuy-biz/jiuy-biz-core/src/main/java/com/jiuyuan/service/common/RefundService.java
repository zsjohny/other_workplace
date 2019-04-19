package com.jiuyuan.service.common;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.alipay.direct.AlipayConfig;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayCore;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class RefundService {
    private static final Log logger = LogFactory.get(RefundService.class);
    @Autowired
    IStoreOrderNewService storeOrderNewService;
    @Autowired
    private SupplierOrderMapper supplierOrderMapper;

    /**
     * 支付宝退款
     *
     * @param order
     * @param refundOrderNo
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    public void alipayRefund(StoreOrderNew order, String refundAmount, String refundReason, String refundOrderNo) throws Exception {
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
        //添加子母订单号
        String out_trade_no = "";
        if (order.getParentId() > -1) {
            out_trade_no = stringBuffer.append(order.getParentId()).toString();
        } else {
            out_trade_no = stringBuffer.append(order.getOrderNo()).toString();
        }
        String trade_no = order.getPaymentNo();
        String refund_amount = refundAmount;
        String refund_reason = refundReason;
        logger.info("支付宝退款：" + "out_trade_no:" + out_trade_no +
                ",子订单号：" + order.getOrderNo() +
                ",母订单号：" + (order.getParentId() == -1 ? order.getOrderNo() : order.getParentId()) +
                ",paymentNo:" + trade_no +
                ",refund_amount" + refundAmount +
                ",refund_reason" + refundReason);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appid, privateKey, "JSON", "utf-8", aliKey, "RSA");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
//    	AlipayRequest ar;
        String bizContent = "{" +
                "\"out_trade_no\":\"" + out_trade_no + "\"," +
                "\"trade_no\":\"" + trade_no + "\"," +
                "\"refund_amount\":" + refund_amount + "," +
                "\"refund_reason\":\"" + refund_reason + "\"," +
                "\"out_request_no\":\"" + refundOrderNo + "\"," +
                "\"operator_id\":\"OP004\"," +
                "\"store_id\":\"NJ_S_002\"," +
                "\"terminal_id\":\"NJ_T_003\"" +
                "}";
        request.setBizContent(bizContent);
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info("测试版，支付宝支付，如果是验签问题，就先放在一边");
            String reg = "check Sign and Data Fail";
            if (!e.getMessage().contains(reg)) {
                throw new RuntimeException("支付宝退款失败!orderNo:" + out_trade_no);
            }
        }
        logger.info("支付宝退款成功");

    }


    public Map<String, String> weixinRefundNew(BigDecimal totalMoney,String paymentNo,String resons,String refundOrderNo,String refundMoney)throws Exception {
        //系统后台向微信支付系统发送数据，生成退款
        //先向map中添加appid等请求参数
        Map<String, String> reqData = new HashMap<String, String>();
        reqData.put("transaction_id",paymentNo);
        //添加子母订单号
        String out_trade_no = "";
        String total_fee = "";
        BigDecimal bigDecimal = BigDecimal.valueOf(100);
        total_fee=String.valueOf(totalMoney.multiply(bigDecimal));//一种支付了多少钱
        reqData.put("total_fee", total_fee);
        BigDecimal refundFee = new BigDecimal(refundMoney).multiply(new BigDecimal(100));//退款金额
        reqData.put("refund_fee", String.valueOf(refundFee.intValue()));
        reqData.put("refund_desc", resons);
        reqData.put("out_refund_no", refundOrderNo);
        logger.info("微信退款：" + "out_trade_no:" + out_trade_no +
                ",transaction_id:" +paymentNo +
                ",refund_fee" + String.valueOf(refundFee.intValue()) +
                ",refund_desc" + resons +
                ",out_refund_no" + refundOrderNo +
                ",total_fee" + total_fee);

        reqData.put("total_fee",total_fee);
        reqData = fillRequestData(reqData, PaymentType.WEIXINPAY_WXA, "MD5");
        //
        //发送请求带证书
        String respXml = requestWithCert("https://api.mch.weixin.qq.com/secapi/pay/refund", reqData, WeixinPayConfig.getHttpConnectTimeoutMs(), WeixinPayConfig.getHttpReadTimeoutMs(), PaymentType.WEIXINPAY_WXA);
        logger.info("请求退款 responseXml====> {}", respXml);
        return processResponseXml(respXml, PaymentType.WEIXINPAY_WXA);
    }
    /**
     * 微信支付退款
     *
     * @param order
     * @param refundAmount
     * @param refundReason
     * @param
     * @throws Exception
     */
    public Map<String, String> weixinRefund(StoreOrderNew order, String refundAmount, String refundReason, boolean app, String refundOrderNo) throws Exception {
        //系统后台向微信支付系统发送数据，生成退款
        //先向map中添加appid等请求参数
        Map<String, String> reqData = new HashMap<String, String>();
        reqData.put("transaction_id", order.getPaymentNo());
        StringBuilder stringBuffer = new StringBuilder("S");
        //添加子母订单号
        String out_trade_no = "";
        String total_fee = "";
        String total_fee2=null;
        Long parentId = order.getParentId();

        if (order.getParentId() > 1) {

//            stringBuffer.append(order.getParentId());
            StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(order.getParentId());
//            if (storeOrderNew.getOrderNoAttachmentStr() != null) {
//                out_trade_no = stringBuffer.append(storeOrderNew.getOrderNoAttachmentStr()).toString();
//            }
            total_fee = String.valueOf((int) (100 * (storeOrderNew.getTotalPay() + storeOrderNew.getTotalExpressMoney())));
            reqData.put("total_fee", total_fee);

            /**
             * 分批后的子订单
             */

            StoreOrderNew storeOrderNewl =null;
            if (!(order.getParentId().equals(order.getOrderNo()))){
                StoreOrderNew query = new StoreOrderNew();
                query.setOrderNo(order.getParentId());
                query.setStatus(0);
                storeOrderNewl = supplierOrderMapper.selectOne(query);
                if(storeOrderNewl!=null){
                    Double totalPay = storeOrderNewl.getTotalPay();
                    Double totalPay1 = order.getTotalPay();
                    double a=(totalPay1+totalPay)*100+(storeOrderNewl.getTotalExpressMoney()+order.getTotalExpressMoney())*100;
                    total_fee2 = String.valueOf((int)a);
                }
            }
            reqData.put("total_fee", total_fee2);
           // reqData.put("total_fee", total_fee);
        } else {
//            stringBuffer.append(order.getOrderNo());
//            if (order.getOrderNoAttachmentStr() != null) {
//                out_trade_no = stringBuffer.append(order.getOrderNoAttachmentStr()).toString();
//            }
            total_fee = String.valueOf((int) (100 * (order.getTotalPay() + order.getTotalExpressMoney())));
           reqData.put("total_fee", total_fee);
        }


        //reqData.put("out_trade_no", out_trade_no);
        BigDecimal refundFee = new BigDecimal(refundAmount).multiply(new BigDecimal(100));
        reqData.put("refund_fee", String.valueOf(refundFee.intValue()));
        reqData.put("refund_desc", refundReason);

        reqData.put("out_refund_no", refundOrderNo);



        logger.info("微信退款：" + "out_trade_no:" + out_trade_no +
                ",子订单号：" + order.getOrderNo() +
                    ",母订单号：" + (order.getParentId() == 1 ? order.getOrderNo() : order.getParentId()) +
                ",transaction_id:" + order.getPaymentNo() +
                ",refund_fee" + String.valueOf(refundFee.intValue()) +
                ",refund_desc" + refundReason +
                ",out_refund_no" + refundOrderNo +
                ",total_fee" + total_fee);

        StoreOrderNew storeOrderNew1 =null;
        String total_fee1=null;

       if (!(order.getParentId().equals(order.getOrderNo()))){

           StoreOrderNew query = new StoreOrderNew();
           Long orderNo = order.getOrderNo();
           query.setParentId(order.getOrderNo());
           query.setStatus(0);
           storeOrderNew1 = supplierOrderMapper.selectOne(query);

           if(storeOrderNew1!=null){
               Double totalPay = storeOrderNew1.getTotalPay();
               Double totalPay1 = order.getTotalPay();
               double a=(totalPay1+totalPay)*100+(storeOrderNew1.getTotalExpressMoney()+order.getTotalExpressMoney())*100;
               total_fee1 = String.valueOf((int)a);
           }
       }
        /**
         * 对分批后的订单进行判断
         */
        if(storeOrderNew1==null){

               if (!(order.getParentId().equals(order.getOrderNo()))){
                   if (order.getParentId()>order.getOrderNo()){

                       reqData.put("total_fee",total_fee);

                   }
                    if (!(order.getOriginalPrice().equals(order.getTotalPay()))){

                        double a=(order.getOriginalPrice()+order.getTotalExpressMoney())*100;
                        String total_fee4=String.valueOf((int)a);

                       reqData.put("total_fee", total_fee4);

                   }else if ((order.getParentId().equals(order.getOrderNo()))&&(order.getOriginalPrice().equals(order.getTotalPay()))){

                       reqData.put("total_fee",total_fee2);

                   }
           }else {
                    reqData.put("total_fee", total_fee);
           }

        }else if (!(order.getOriginalPrice().equals(order.getTotalPay()))){
            double a=(order.getOriginalPrice()+order.getTotalExpressMoney())*100;
            String total_fee3=String.valueOf((int)a);
            reqData.put("total_fee", total_fee3);
         }else{
            reqData.put("total_fee", total_fee1);
        }
        //reqData.put("total_fee", "2");WEIXINPAY_SDK==3
        reqData = fillRequestData(reqData, PaymentType.WEIXINPAY_SDK, "MD5");
        //
        //发送请求带证书
        String respXml = requestWithCert("https://api.mch.weixin.qq.com/secapi/pay/refund", reqData, WeixinPayConfig.getHttpConnectTimeoutMs(), WeixinPayConfig.getHttpReadTimeoutMs(), PaymentType.WEIXINPAY_SDK);
        logger.info("请求退款 responseXml====> {}", respXml);
        return processResponseXml(respXml, PaymentType.WEIXINPAY_SDK);
    }
//	public static void main(String[] args) {
//		StoreOrderNew order = new StoreOrderNew();
//		order.setPaymentNo("4200000019201712197208190100");
//		order.setOrderNo(793L);
//		order.setTotalPay(0.01);
//		order.setTotalExpressMoney(0.0);
//		RefundService refundService = new RefundService();
//		try {
//			refundService.weixinRefund(order,"0.01","",true);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//    @PostConstruct
    public void init() throws Exception {
        System.out.println("___________________");
        Map<String, String> reqData = new HashMap<String, String>();
        reqData.put("transaction_id", "4200000178201809079393725152");
        reqData.put("out_trade_no", "S100006433_07164734");
        reqData.put("refund_fee", "1");
        reqData.put("refund_desc", "重新下单");
        reqData.put("out_refund_no", "50001040");
        reqData.put("total_fee", "1");

        reqData = fillRequestData(reqData, PaymentType.WEIXINPAY_SDK, "MD5");

        //发送请求带证书
        String respXml = requestWithCert("https://api.mch.weixin.qq.com/secapi/pay/refund", reqData, WeixinPayConfig.getHttpConnectTimeoutMs(), WeixinPayConfig.getHttpReadTimeoutMs(), PaymentType.WEIXINPAY_SDK);
        Map<String, String> map = processResponseXml(respXml, PaymentType.WEIXINPAY_SDK);
        System.out.println("开始使用.....");
        System.out.println(map);
        System.out.println(map);
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     *
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr, PaymentType paymentType) throws Exception {
        String RETURN_CODE = "return_code";
        String RESULT_CODE = "result_code";
        String return_code;
        String result_code;
        Map<String, String> respData = WeixinPayCore.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        } else {
            logger.error(String.format("No `return_code` in XML: %s", xmlStr));
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals("FAIL")) {
            logger.error("退款失败！");
            logger.info(respData.toString());
            throw new RuntimeException("退款失败！");
        } else if (return_code.equals("SUCCESS")) {
            if (WeixinPayCore.isSignatureValid(respData, WeixinPayConfig.getApiKey(paymentType), "MD5")) {
                logger.info("成功响应！");
                if (respData.containsKey(RESULT_CODE)) {
                    result_code = respData.get(RESULT_CODE);
                    if (result_code.equals("FAIL")) {
                        logger.info("resData:" + respData);
                        logger.info("退款失败！");
                        throw new RuntimeException("退款失败！");
                    } else if (result_code.equals("SUCCESS")) {
                        logger.info("resData:" + respData);
                        logger.info("退款成功！");
                    }
                }
                return respData;
            } else {
                logger.debug("无效签名！");
                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            logger.error(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }

    /**
     * 需要证书的请求,比如退款，撤销订单
     *
     * @param url              String
     * @param reqData          向wxpay post的请求数据  Map
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs    超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithCert(String url, Map<String, String> reqData,
                                  int connectTimeoutMs, int readTimeoutMs,
                                  PaymentType paymentType) throws Exception {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = WeixinPayCore.mapToXml(reqData);
        String resp = requestOnce(url, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, true, paymentType);
        return resp;
    }

    /**
     * 请求，只请求一次，不做重试
     *
     * @param url
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @param useCert          是否使用证书，针对退款、撤销等操作
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
        } else {
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
    public Map<String, String> fillRequestData(Map<String, String> reqData, PaymentType paymentType, String signType) throws Exception {
        reqData.put("appid", WeixinPayConfig.getAppId(paymentType));
        reqData.put("mch_id", WeixinPayConfig.getMchId(paymentType));
        reqData.put("nonce_str", WeixinPayCore.genNonceStr());
        reqData.put("sign_type", signType);
        reqData.put("sign", WeixinPayCore.generateSignature(reqData, WeixinPayConfig.getApiKey(paymentType), signType));
        return reqData;
    }

}
