package com.jiuyuan.entity.newentity.weixinpay;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.constant.ConstantBinder;


public class WeixinPayConfig {
    static {
            ConstantBinder.bind(WeixinPayConfig.class, "UTF8", "/weixinpay_constants.properties");
    }
  
    public static String APP_ID;
    public static String API_KEY;
    public static String MCH_ID;
    public static String NOTIFY_URL;
    
    public static String APP_ID_NATIVE;
    public static String API_KEY_NATIVE;
    public static String MCH_ID_NATIVE;
    public static String NOTIFY_URL_NATIVE;
    
    //公众号
    public static String APP_ID_PUBLIC;
    public static String APP_SECRET_PUBLIC;
    public static String API_KEY_PUBLIC;
    public static String MCH_ID_PUBLIC;
    public static String NOTIFY_URL_PUBLIC;

    public static String IN_SHOP_STORE_ID;
    
    //小程序
    public static String NOTIFY_URL_WXA;
    
    public static String WEIXIN_SERVER_URL;
    
    public static String getWeiXinServerUrl() {
    	return WEIXIN_SERVER_URL;
    }
    
    public static String getPublicSecret() {
    	return APP_SECRET_PUBLIC;
    }
    
    /**
     * 获取证书
     * @throws FileNotFoundException 
     */
    public static InputStream getCertStream() throws FileNotFoundException{
    	InputStream inputStream = WeixinPayConfig.class.getClassLoader().getResourceAsStream("apiclient_cert.p12");
    	return inputStream;
    }

    
    /**
     * HTTP(S) 连接超时时间，单位毫秒
     *
     * @return
     */
    public static int getHttpConnectTimeoutMs() {
        return 6*1000;
    }
    
    
    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     *
     * @return
     */
    public static int getHttpReadTimeoutMs() {
        return 8*1000;
    } 
    
    public static String getAppId(PaymentType paymentType) {
    	if (PaymentType.WEIXINPAY_SDK.getIntValue() == paymentType.getIntValue()) {
    		return APP_ID;
    	}else if (PaymentType.WEIXINPAY_NATIVE.getIntValue() == paymentType.getIntValue()) {
    		return APP_ID_NATIVE;
    	}else if (PaymentType.WEIXINPAY_PUBLIC.getIntValue() == paymentType.getIntValue()) {
    		return APP_ID_PUBLIC;
		}else{
			return APP_ID;
		}
    }

    public static String getStoreId() {
       return IN_SHOP_STORE_ID;
    }
    public static String getApiKey(PaymentType paymentType) {
        if (PaymentType.WEIXINPAY_SDK.getIntValue() == paymentType.getIntValue()) {
    		return API_KEY;
    	}else if (PaymentType.WEIXINPAY_NATIVE.getIntValue() == paymentType.getIntValue()) {
    		return API_KEY_NATIVE;
    	}else if (PaymentType.WEIXINPAY_PUBLIC.getIntValue() == paymentType.getIntValue()) {
    		return API_KEY_PUBLIC;
		}else{
			return API_KEY;
		}
    }

    public static String getMchId(PaymentType paymentType) {
        if (PaymentType.WEIXINPAY_SDK.getIntValue() == paymentType.getIntValue()) {
    		return MCH_ID;
    	}else if (PaymentType.WEIXINPAY_NATIVE.getIntValue() == paymentType.getIntValue()) {
    		return MCH_ID_NATIVE;
    	}else if (PaymentType.WEIXINPAY_PUBLIC.getIntValue() == paymentType.getIntValue()) {
    		return MCH_ID_PUBLIC;
		}else{
			return MCH_ID;
		}
    }

    public static String getNotifyUrl(PaymentType paymentType) {
        if (PaymentType.WEIXINPAY_SDK.getIntValue() == paymentType.getIntValue()) {
    		return NOTIFY_URL;
    	}else if (PaymentType.WEIXINPAY_NATIVE.getIntValue() == paymentType.getIntValue()) {
    		return NOTIFY_URL_NATIVE;
    	}else if (PaymentType.WEIXINPAY_PUBLIC.getIntValue() == paymentType.getIntValue()) {
    		return NOTIFY_URL_PUBLIC;
    	}else if (PaymentType.WEIXINPAY_WXA.getIntValue() == paymentType.getIntValue()) {
    		return NOTIFY_URL_WXA;
		}else{
			return NOTIFY_URL;
		}
    }
    

    public static String getAppId(boolean app) {
        BizUtil.test (378, "getAppId, 先这样吧");
        return "wx6ad169bccc57554a";
//        return app ? APP_ID : APP_ID_NATIVE;
    }

    public static String getApiKey(boolean app) {
        return app ? API_KEY : API_KEY_NATIVE;
    }

    public static String getMchId(boolean app) {
        return app ? MCH_ID : MCH_ID_NATIVE;
    }

    public static String getNotifyUrl(boolean app) {
        return app ? NOTIFY_URL : NOTIFY_URL_NATIVE;
    }


}
