package com.jfinal.weixin.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.HttpUtils;


/**
 *.appid=wxf99f985dc7f79695
 * .mchid=1309737801
 * .secret=yjj7chTcyBgGNPM3Yk2cESzmes53gyjj
 *
 */

/**
 * @author osc就看看
 * 企业付款demo
 */
public class WeixinTransfersController {

    /**
     * 商户账号
     */
    private static String appid = "wxdd7409661fc2e517";

    /**
     * 商户号
     */
    private static String mchid = "1309737801";
    /**
     * 密文
     */
    private static String paternerKey = "yjj7chTcyBgGNPM3Yk2cESzmes53gyjj";

    private static String transfer_url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    public static void index()  {


        Map<String,String> param = new HashMap<>();
        // 商户账号 mch_appid 是
        param.put("mch_appid",appid);
        // 商户号 mchid 是
        param.put("mchid",mchid);
        // 随机字符串 	nonce_str 是
        param.put("nonce_str",UUID.randomUUID().toString().replace("-",""));
        // 商户订单号	partner_trade_no 是
        param.put("partner_trade_no","TEST000120180729xxxxxx");
        // 用户openid	openid 是
        param.put("openid","oWIUi0ZaqnuToy8_bl2y3YXC3zyk");
        // 校验用户姓名选项	check_name 是
        param.put("check_name","NO_CHECK");
        // 收款用户姓名	re_user_name 可选
        // param.put("re_user_name","艾成松");
        // 金额	amount	是
        param.put("amount","100");
        // 企业付款描述信息	desc	是
        param.put("desc","玖币提现测试");
        // Ip地址	spbill_create_ip	是
        param.put("spbill_create_ip","127.0.0.1");

        String sign = PaymentKit.createSign(param, paternerKey);
        param.put("sign",sign);

        // （apiclient_cert.p12）证书存放路径
        String cerPath = "C:\\Users\\Aison\\Desktop\\apiclient_cert.p12";

        String xml = PaymentKit.toXml(param);
        System.out.println(xml);
        String xmlResult = HttpUtils.postSSL(transfer_url, xml, cerPath, mchid);
        Map<String, String> resultXML = PaymentKit.xmlToMap(xmlResult);
        System.out.println(resultXML);

    }


    public static void main(String[] args) throws Exception {
        index();
    }
}
