package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.helper.log.Log;

import com.e_commerce.miscroservice.commons.utils.DebugUtils;
import com.e_commerce.miscroservice.order.entity.*;
import com.e_commerce.miscroservice.order.mapper.WXOrderMapper;
import com.e_commerce.miscroservice.order.method.WapPayHttpUtil;
import com.e_commerce.miscroservice.order.method.WxPayResultData;
import com.e_commerce.miscroservice.order.method.WxPaySendData;
import com.e_commerce.miscroservice.order.method.WxSign;
import com.e_commerce.miscroservice.order.service.wx.MemberService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class MemberServiceImp implements MemberService {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private Log logger = Log.getInstance(MemberServiceImp.class);
    @Autowired
    private WXOrderMapper wxOrderMapper;

    @Value("${notify.wxUrl}")
    private String notifyWxUrl;
    @Override
    public ShopMemberOrder getMemberById(Long orderId) {
        ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(orderId);
        return shopMemberOrder;
    }

    @Override
    public SecondBuyActivity selectById(Long secondId) {
        SecondBuyActivity secondBuyActivity = wxOrderMapper.selectActive(secondId);
        return secondBuyActivity;
    }

    @Override
    public TeamBuyActivity selectTeamId(Long teamId) {
        TeamBuyActivity teamBuyActivity = wxOrderMapper.selectTeam(teamId);
        return teamBuyActivity;
    }

    @Override
    public StoreWxa getStoreWxaByStoreId(String storeId) {
        List<StoreWxa> list = wxOrderMapper.selectStoreWxaListNew(storeId);
        StoreWxa storeWxa = null;
        if (list.size() > 0) {
            storeWxa = list.get(0);
        }
        return storeWxa;
    }

    @Override
    public List<ShopMemberOrderItem> getMemberOrderItemList(Long orderId) {
        List<ShopMemberOrderItem> shopMemberOrderItems = wxOrderMapper.selectByOrderNo(orderId);
        return shopMemberOrderItems;
    }


    @Override
    public String unifiedorder(String orderNo, String productName, int total_fee, String publicOpenId, String businessName, String appId, String mchId, String payKey, String user_ip,Integer type) {
        WxPaySendData data = buildWxPaySenData(orderNo, productName,total_fee, publicOpenId,businessName,appId,mchId, user_ip,type);
        //统一下单支付
        String returnXml = null;
        try {
            //生成sign签名
            SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
            parameters.put("appid", data.getAppid());
            parameters.put("attach", data.getAttach());
            parameters.put("body", data.getBody());
            parameters.put("mch_id", data.getMch_id());
            parameters.put("nonce_str", data.getNonce_str());
            parameters.put("notify_url", data.getNotify_url());
            parameters.put("out_trade_no", data.getOut_trade_no());
            parameters.put("total_fee", data.getTotal_fee());
            parameters.put("trade_type", data.getTrade_type());
            parameters.put("spbill_create_ip", data.getSpbill_create_ip());
            parameters.put("openid", data.getOpenid());
            parameters.put("device_info", data.getDevice_info());
            parameters.put("time_start", data.getTime_start());
            logger.info("SIGN:"+WxSign.createSign(parameters,payKey));
            data.setSign(WxSign.createSign(parameters,payKey));
            logger.info("获取回调函数={}"+data.getNotify_url());
            XStream xs = new XStream(new DomDriver("UTF-8",new XmlFriendlyNameCoder("-_", "_")));
            xs.alias("xml", WxPaySendData.class);
            String xml = xs.toXML(data);
            returnXml = WapPayHttpUtil.sendPostHttp("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("调用统一下单方法错误",e);
        }
        return returnXml;
    }

    @Override
    public WxPayResultData buildWxPayResultData(String result) {
        logger.info("调用微信下单接口返回XML result:"+result);
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("xml", WxPayResultData.class);
        WxPayResultData resultData = (WxPayResultData) xstream.fromXML(result);
        return resultData;
    }

    @Override
    public SortedMap<Object, Object> buildSignData(WxPayResultData resultData, String payKey) {
        SortedMap<Object,Object> signMap = new TreeMap<Object,Object>();
        signMap.put("appId", resultData.getAppid()); //公众号id
        signMap.put("timeStamp", WxSign.getTimeStamp());//时间戳 		timeStamp	String	是	时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
        signMap.put("nonceStr", resultData.getNonce_str());//随机字符串	nonceStr	String	是	随机字符串，长度为32个字符以下。
        signMap.put("package", "prepay_id="+resultData.getPrepay_id());//订单详情扩展字符串  packageValue	package	String	是	统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
        signMap.put("signType", "MD5");//签名方式		signType	String	是	签名算法，暂支持 MD5
        String paySign = WxSign.createSign(signMap,payKey);	//WapPublicConstants.WX_PAY_KEY
        logger.info("生成的签名PaySIGN:"+paySign);
        signMap.put("paySign", paySign);//签名	paySign	String	是	签名,具体签名方案参见小程序支付接口文档;
        return signMap;
    }

    @Override
    public int updatePrepayId(long orderId, String prepay_id) {
        ShopMemberOrder shopMemberOrderUpd = new ShopMemberOrder();
        shopMemberOrderUpd.setId(orderId);
        shopMemberOrderUpd.setPayFormId(prepay_id);

        //2018年12月25日09:53:59
        //shopMemberOrderUpd.setOrderStatus(1);
        return wxOrderMapper.updateById(shopMemberOrderUpd);
    }



    @Override
    public ShopMember selectShopMember(Long memberId) {
        ShopMember shopMember = wxOrderMapper.selectShopMember(memberId);
        return shopMember;
    }

    @Override
    public StoreBusiness selectStoreBusiness(Long storeId) {
        return wxOrderMapper.selectStoreBusiness(storeId);
    }

    @Override
    public Long selectOppenId(String bindWeixin,Long storeId) {
        return wxOrderMapper.selectOppenId(bindWeixin,storeId);
    }


    /**
     * 组装订单发送参数数据
     * @param orderNo
     * @param publicOpenId  注意这里是openid
     * @param user_ip
     * @return
     */

    private WxPaySendData buildWxPaySenData(String orderNo, String  productName, int total_fee, String publicOpenId, String businessName, String appId, String mchId, String user_ip,Integer type) {
        WxPaySendData sendData = new WxPaySendData();
        sendData.setAppid(appId);//WapPublicConstants.APPID
        sendData.setAttach(businessName);
        sendData.setBody(productName);
        sendData.setMch_id(mchId);//WapPublicConstants.MCH_ID
        sendData.setNonce_str(WxSign.getNonceStr());
        sendData.setNotify_url(notifyWxUrl);//这里使用APP微信异步回调//WapPayConstants.PAY_NOTIFY_URL);
        sendData.setOut_trade_no(orderNo);
        sendData.setTotal_fee(total_fee);//单位：分
        sendData.setTrade_type("JSAPI");
        sendData.setSpbill_create_ip(user_ip);
        sendData.setOpenid(publicOpenId);//payUser.getWechatId()
        Date date = new Date();
        sendData.setTime_start(sdf.format(date));
        return sendData;
    }
    /**
     * 判断秒杀订单保留时间是否过期
     * @param shopMemberOrder
     * @return
     */
    public  boolean isOverTime(ShopMemberOrder shopMemberOrder) {
        long overdueTime = shopMemberOrder.getCreateTime()+ 2*60*60*1000;
        long nowTime = System.currentTimeMillis();
        long time = overdueTime-nowTime;
        if (time < 0) {
            return true;
        }else{
            return false;
        }
    }

}
