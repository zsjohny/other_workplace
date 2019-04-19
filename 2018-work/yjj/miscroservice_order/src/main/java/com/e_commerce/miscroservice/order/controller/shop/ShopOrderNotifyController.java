package com.e_commerce.miscroservice.order.controller.shop;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaVo;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.pay.Sign;
import com.e_commerce.miscroservice.commons.utils.pay.WeChatPay;
import com.e_commerce.miscroservice.order.rpc.user.WxaPayConfigRpcService;
import com.e_commerce.miscroservice.order.service.wx.ShopMemberOrderService;
import com.e_commerce.miscroservice.order.utils.OrderNotifyHelper;
import com.e_commerce.miscroservice.order.utils.PayConstantPool;
import com.e_commerce.miscroservice.order.vo.StoreBizOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/12 10:20
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "order/shopOrderNotify" )
public class ShopOrderNotifyController{
    private static final String SUCCESS = "SUCCESS";

    private Log logger = Log.getInstance (ShopOrderNotifyController.class);


    @Autowired
    private PayConstantPool payConstantPool;
    @Autowired
    private WxaPayConfigRpcService wxaPayConfigRpcService;
    @Autowired
    private ShopMemberOrderService shopMemberOrderService;

    /**
     * 店中店
     *支付回调
     * @param request request
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/12 20:40
     */
    @RequestMapping( "wxPayNotify" )
    public String fromWeiXin(HttpServletRequest request) throws ParseException {
        Map map;
        try {
            map = WeChatPay.doParseRquest (request);
            logger.info ("店中店,微信支付回调 result={}", map);
        } catch (Exception e) {
            e.printStackTrace ();
            return "FAIL";
        }

        String attach = map.get ("attach").toString ();
        JSONObject notifyJson = JSONObject.parseObject (attach);

        //验签
        int notifyType = notifyJson.getIntValue (OrderNotifyHelper.WX_PAY_ATTACH);
        boolean isSign = checkSign (notifyType, map);
        if (! isSign) {
            logger.warn ("店中店,微信支付回调,验签失败");
            return null;
        }


        boolean isSuccess = SUCCESS.equals (map.get ("return_code").toString ());
        if (isSuccess) {

            String orderNo = map.get ("out_trade_no").toString ();
            String payTime = map.get ("time_end").toString ();
            String thirdPayNo = map.get ("transaction_id").toString ();
            try {
                StoreBizOrderQuery query = new StoreBizOrderQuery ();
                query.setAttach (notifyJson);
                query.setOrderNo (orderNo);
                query.setThirdPayNo (thirdPayNo);
                query.setPayTime (new SimpleDateFormat ("yyyyMMddHHmmss").parse (payTime).getTime ());
                //调起支付成功
                shopMemberOrderService.paySuccess (query);
                return SUCCESS;
            } catch (ErrorHelper e) {
                logger.error ("支付成功,业务执行失败!!! orderNo={} msg==>{}", orderNo, e.getMsg ());
                return null;
            }
        }
        else {
            logger.warn ("店中店,微信支付回调,失败!!!");
            return "fail";
        }
    }


    /**
     * 验签
     *
     * @param notifyType  支付回调类型,在预支付的时候,发给微信,在支付回调时候它会返还给我们
     * @param wxReturnMap 微信返回值map
     * @return boolean
     * @author Charlie
     * @date 2018/12/15 10:45
     */
    private boolean checkSign(int notifyType, Map<String, String> wxReturnMap) {
        OrderNotifyHelper.NotifyType type = OrderNotifyHelper.NotifyType.create (notifyType);
        if (type == null) {
            logger.error ("未知的支付回调类型");
            return Boolean.FALSE;
        }

        switch (type) {
            case BUY_FROM_IN_SHOP:
                //开通店中店,在小粉蝶里获取key
                StoreWxaVo payConfig = wxaPayConfigRpcService.findByStoreId (payConstantPool.getInShopStoreId ());
                String payKey = payConfig.getPayKey ();
                return Sign.checkIsSignValidFromResponseString (wxReturnMap, payKey);
            default:
                logger.error ("未支持的支付回调类型");
                return Boolean.FALSE;
        }
    }


}
