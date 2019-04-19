package com.e_commerce.miscroservice.payment.controller;

import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import com.e_commerce.miscroservice.payment.mapper.SqlMapper;
import com.e_commerce.miscroservice.payment.wx.service.WxOrderService;
import com.e_commerce.miscroservice.payment.wx.util.Sign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

import static com.e_commerce.miscroservice.commons.utils.wx.PaymentUtils.xmlToMap;

@RestController
@RequestMapping("/pay/wx")
public class WxController {

    private static final String FIELD_SIGN = "sign";
    private static final String SUCCESS = "SUCCESS";
    private static final String RETURN_CODE = "return_code";
    private static final String ATTACH = "attach";
    private Log logger = Log.getInstance(WxController.class);

    @Autowired
    private WxOrderService wxOrderService;

    @Autowired
    //@LoadBalanced
    private RestTemplate restTemplate;

    @Value ("${public.account.key}")
    private String publicAccountKey;



    @Autowired
    private SqlMapper sqlMapper;

    @RequestMapping( "updNull" )
    public void updNull(Long id) {
        //用来刷sql
        sqlMapper.updShareShopQrImg(id);
    }



    /**
     * 描述  生成预付单  最好返回原始对象  by-hyq
     *
     * @param amount       订单总金额，单位为分
     * @param title        商品描述
     * @param out_trade_no 商户订单号
     * @param attach       附加参数。用于各个系统调用支付的时候，针对异步操作的时候，是那个系统的请求的   可以写一个模块的url 或者弄个监听器  hyq
     * @return
     * @author hyq
     * @date 2018/9/21 14:43
     */
    @RequestMapping(value = "/createPreOrder")
    public PreOrderResult createPreOrder(@RequestParam(value = "amount", required = true)BigDecimal amount,
                                         @RequestParam(value = "title", required = true)String title,
                                         @RequestParam(value = "out_trade_no", required = true)String out_trade_no,
                                         @RequestParam(value = "attach", required = true) String attach,
                                         @RequestParam(value = "ip", required = true) String ip,
                                         @RequestParam(value = "openid", required = true) String openid,
                                         @RequestParam(value = "trade_type", required = false) String trade_type) throws Exception {

        String body = title;
        BigDecimal total_fee = amount;
        // 统一下单
        PreOrderResult preOrderResult = wxOrderService.placeOrder(body, out_trade_no, total_fee,attach,openid, ip,trade_type);

        return preOrderResult;
    }

    /**
     * 描述 调用统一下单后，对返回参数进行封装。客户端唤起微信支付
     * @param preOrderResult  原始返回参数
     * @param payKey   秘钥
     * @author hyq
     * @date 2018/9/25 16:57
     * @return java.util.SortedMap<java.lang.Object,java.lang.Object>
     */
    @RequestMapping(value = "/getBrandWCPayRequest")
    public SortedMap<Object, Object> getBrandWCPayRequest(PreOrderResult preOrderResult) {

        SortedMap<Object,Object> signMap = new TreeMap<Object,Object>();
        //公众号id
        signMap.put("appId", preOrderResult.getAppid());
        //时间戳  timeStamp	String	是	时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
        signMap.put("timeStamp", Sign.getTimeStamp());
        //随机字符串	nonceStr	String	是	随机字符串，长度为32个字符以下。
        signMap.put("nonceStr", preOrderResult.getNonce_str());
        //订单详情扩展字符串  packageValue	package	String	是	统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
        signMap.put("package", "prepay_id="+preOrderResult.getPrepay_id());
        //签名方式	signType	String	是	签名算法，暂支持 MD5
        signMap.put("signType", "MD5");
        //WX_PAY_KEY
//        String paySign = Sign.createSign("utf-8",signMap,WeChatConfig.KEY);
        String paySign = Sign.createSign("utf-8",signMap,publicAccountKey);
        logger.info("生成的签名PaySIGN:"+paySign);
        //签名	paySign	String	是	签名,具体签名方案参见小程序支付接口文档;
        signMap.put("paySign", paySign);
        return signMap;
    }


    /**
     * 描述  微信支付异步通知  请做好重复接收的准备(客户端 服务端)
     * ps  客户端收到后，必须返回true.
     * 1. 支付成功修改订单状态，标记为成功
     * 2. 生成支付流水
     *
     * @param request
     * @param response
     * @author hyq
     * @date 2018/9/21 15:00
     */
    @RequestMapping(value = "/yjjnotify")
    public synchronized void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info ("进入支付回调...");
        String result = wxOrderService.getWxPayResult(request);
        //PayResult payResult = wxOrderService.getWxPayResult(request);
        logger.info ("支付回调 publicAccountKey={}, result={}", publicAccountKey, result);

        //格式化
        Map<String, String> data = xmlToMap(result);
        //验签
        boolean isSignatureValid = isSignatureValid (data, publicAccountKey);
        if (! isSignatureValid) {
            logger.error ("参数验签失败!!! fromIp={}", HttpUtils.getIpAddress (request));
            return;
        }

        String returnCode = data.get (RETURN_CODE);
        String attach = data.get (ATTACH);
        logger.info ("支付回调 returnCode={},attach={}", returnCode, attach);
        boolean isPaid = SUCCESS.equals (returnCode);

        // 查询该笔订单在微信那边是否成功支付
        // 支付成功，商户处理后同步返回给微信参数
        PrintWriter writer = response.getWriter();
        if (isPaid) {
            logger.info("================================= 支付成功 =================================");

            // 这里应该设置一个新的表，代表每笔的订单号的成功失败，其他的系统去查询这个表就行了。hyq
            // 或者 用MQ来进行转发或者进行服务的调用 根据多余的字段 attach

            String[] split = attach.split("@_@");

            String url =  "http://"+split[0]+split[1]+"?result="+result;
            //String url =  "http://ORDER/order/proxy/order/returnPayDetail?result="+result;
            //如果这里不返回true,就不对微信返回suceess.
            logger.info ("回调url ={}", url);
            Object isOk = restTemplate.getForObject (url, Object.class);
            logger.info ("回调 result={}", isOk);

            //String  msg  = restTemplate.getForObject("http://PRODUCT/server/msg",String.class);
            //通知微信已经收到消息，不要再给我发消息了，否则微信会8连击调用本接口
            //if (isOk){
            String noticeStr = setXML("SUCCESS", "");
            writer.write(noticeStr);
            writer.flush();
            writer.close();
            //}

        } else {
            logger.info("================================= 支付失败 =================================");

            // 支付失败
            String noticeStr = setXML("FAIL", "");
            writer.write(noticeStr);
            writer.flush();
        }

    }





    private String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }


    /**
     * 判断签名是否正确
     *
     * @param data 返回参数
     * @param key API密钥
     * @return 签名是否正确
     * @author Charlie
     * @date 2018/10/29 17:49
     */
    private static boolean isSignatureValid(Map<String, String> data, String key) {
        //将xml格式数据转化为map格式
        if (!data.containsKey(FIELD_SIGN) ) {
            //如果返回xml数据中不包含sign签名标记数据，则直接返回false
            return false;
        }
        //获取微信返回数据中的sign签名数据
        String sign = data.get(FIELD_SIGN);
        //将data和key进行签名组装，与返回数据中的sign签名数据对比
        return generateSignature(data, key).equals(sign);
    }



    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    private static String generateSignature(final Map<String, String> data, String key) {
        //通过keySet获取所有的key集合
        Set<String> keySet = data.keySet();
        //将set转化为数组keyArray
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //数组升序排序
        Arrays.sort(keyArray);
        //构建StringBuilder字符串变量
        StringBuilder sb = new StringBuilder();
        //for循环key数组
        for (String k : keyArray) {
            //（重点1）如果数组中包含sign，则继续，不做字符串拼接操作
            if (k.equals(FIELD_SIGN)) {
                continue;
            }
            // 参数值为空，则不参与签名
            if (data.get(k).trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
            }
        }
        //（重点2）拼接密钥，参数上传的密钥
        sb.append("key=").append(key);
        //签名加密方式为MD5，则将字符串所有的英文字符转换为大写字母，再做MD5编码，返回md5加密结果
        //返回加密结果字符串
        return Md5Util.md5 (sb.toString());
    }

}
