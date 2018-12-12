package com.xiaoluo.util;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Getui {

//    static String appId = "as04Xnd2SZ6vLW4KuuRtN2";
//    static String appkey = "hg5ux6rzlL7ReQjRLMfk97";
//    static String master = "f8wt6G4oPZAozCRRAWdeq9";

        static String appId = "570Lwsx7DC6BKkfNKleJ79";
      static String appkey = "91v4TkJaxm61sGP4Ews5yA";
        static String master = "aULQLOh6dJ7iQoGgL7n0B7";

    static String CID = "40d8dd66c0d23da2aac34605f41b0951";
    static String Alias = "";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    static String url = "http://sdk.open.api.igexin.com/serviceex";

    public static String SendAndroid(String msg, String cid) {
        IGtPush push = new IGtPush(host, appkey, master);

        // 透传
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(msg);

        // NotificationTemplate template = null;
        // try {
        // template = NotificationTemplateDemo(msg);
        // } catch (Exception e1) {
        // e1.printStackTrace();
        // }

        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 60 * 7);
        message.setData(template);

        List<Target> targets = new ArrayList<Target>();
        Target target1 = new Target();
        Target target2 = new Target();
        target1.setAppId(appId);
        target1.setClientId(cid);

        try {
            IPushResult ret = push.pushMessageToSingle(message, target1);
            return ret.getResponse().toString();
        } catch (RequestException e) {
            String requstId = e.getRequestId();
            IPushResult ret = push.pushMessageToSingle(message, target1, requstId);

            return ret.getResponse().toString();
        }
    }

    public static NotificationTemplate NotificationTemplateDemo(String msg) throws Exception {
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        template.setTitle("");
        template.setText("");
        template.setLogo("icon.png");
        template.setTransmissionType(1);
        template.setTransmissionContent(msg);
        return template;
    }

    public static String SendIos(String msg, String cid, String tips,String apns) {
        IGtPush push = new IGtPush(host, appkey, master);
        TransmissionTemplate template = null;
        try {
            template = getTemplate(msg, tips,apns);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 60 * 7);
        message.setData(template);

        List<Target> targets = new ArrayList<Target>();
        Target target1 = new Target();
        Target target2 = new Target();
        target1.setAppId(appId);
        target1.setClientId(cid);
        try {
            IPushResult ret = push.pushMessageToSingle(message, target1);
            return ret.getResponse().toString();
        } catch (RequestException e) {
            String requstId = e.getRequestId();
            IPushResult ret = push.pushMessageToSingle(message, target1, requstId);

            return ret.getResponse().toString();
        }

    }

    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<>();
        map.put("callSatus", "start");
        map.put("acceptId", 947);
        map.put("acceptName", "我是魏晓堃");
        map.put("acceptUrl", "http://139.196.40.11:18887/ouliao/user/download/setUser/947/1461067401414/head/download");
        map.put("acceptAuthor", "");
        map.put("acceptIsContract", "false");
        map.put("acceptRoom", 947);
        map.put("acceptPhoneId", 771);

        for (int i = 0; i < 2; i++)

        {
            System.out.println(SendIos(map.toString(), "1a6ea51d3364aac09a962dfe0cba36ab", "test",map.toString()));

//            String result = SendAndroid(map.toString(), "48e1e018d6dbf9d3aea4b2f78ac9141d");

//            System.out.println(result);
        }

    }

    public static TransmissionTemplate getTemplate(String msg, String tips,String apns) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        template.setTransmissionContent(msg);

        template.setTransmissionType(2);
        if (StringUtils.isNotEmpty(tips)) {
            APNPayload payload = new APNPayload();
            payload.setBadge(0);
            payload.setContentAvailable(1);
            payload.setSound("default");
            payload.setCategory(apns);
            //设置铃声
            payload.setSound("ringingWithCallComing.aif");

            payload.setBadge(0);

            payload.setAlertMsg(new APNPayload.SimpleAlertMsg(tips));
            // 字典模式使用下者
            // payload.setAlertMsg(getDictionaryAlertMsg());
            template.setAPNInfo(payload);
        }
        return template;

    }

    public static String IosApns(String msg, String lockTips, String devicen_token) {
        try {
            IGtPush push = new IGtPush(url, appkey, master);
            APNTemplate t = new APNTemplate();
            APNPayload apnpayload = new APNPayload();
            apnpayload.setSound("");
            APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
            alertMsg.setTitle(msg);
            alertMsg.setBody(msg);
            alertMsg.setTitleLocKey("偶聊");
            alertMsg.setActionLocKey(lockTips);

            //设置铃声
            /*apnpayload.setSound("ringingWithCallComing.aif");
            apnpayload.setBadge(1);*/
            apnpayload.setAlertMsg(alertMsg);

            t.setAPNInfo(apnpayload);
            SingleMessage sm = new SingleMessage();
            sm.setData(t);
            IPushResult ret0 = push.pushAPNMessageToSingle(appId, devicen_token, sm);

            return (String) ret0.getResponse().get("result");
        } catch (Exception e) {

        }

        return null;

    }

}