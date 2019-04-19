package com.jiuyuan.util.bankcardpay;

import java.lang.reflect.Field;

import com.jiuyuan.util.constant.ConstantBinder;


/* *
 *类名：BankCardPayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：1.0
 *日期：2016-06-21
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。

 */

public class BankCardPayConfig {
    static {
        ConstantBinder.bind(BankCardPayConfig.class, "UTF8", "/bankcardpay_constants.properties");
    }

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 支付商户开户分行号
    public static String BRANCH_ID;
	
	// 支付商户号
    public static String CO_NO;
    // 密码
    public static String CO_PSWD;
	// 商户的私钥
    public static String KEY;

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	
	// 服务器异步通知页面路径
    public static String MERCHANT_URL;

    
	//页面跳转同步通知页面路径
    public static String MERCHANT_RET_URL;
    

    
    // 服务器异步通知页面路径
    public static String MERCHANT_SIGN_URL;
    
    //公钥地址
    public static String PUBLIC_KEY;
    
    //请求支付地址
    public static String PAY_REQUEST_URL;
    
    
    //协议商户企业编号
    public static String MCH_NO;


    public static void main(String[] args) throws Exception {
        Field[] fields = BankCardPayConfig.class.getDeclaredFields();
        for (Field field : fields) {
        }
    }
}
