package com.finace.miscroservice.commons.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.enums.PushExtrasEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * java后台极光推送方式二：使用Java SDK
 */
public class JiguangPush {
    private static final Logger log = LoggerFactory.getLogger(JiguangPush.class);

//    private static String APP_KEY;
    private static String APP_KEY = "e65a2e8168da3bbb8979a85c";
    private static String MASTER_SECRET ="ceb4130ed20d29adf46a8ffe";

//    private static Boolean ENVIRONMENT;
    private static Boolean ENVIRONMENT=false;

//    TEST_APP_KEY=e65a2e8168da3bbb8979a85c
//    TEST_MASTER_SECRET=ceb4130ed20d29adf46a8ffe
//    TEST_ISDEV=false

    private static final String CONFIG_NAME = "properties/jiguang.properties";

    private static Properties properties = new Properties();

//    private static void init(){
//          if( properties.isEmpty() ){
//              try {
//                  properties.load(new ClassPathResource(CONFIG_NAME).getInputStream());
//
//                  Environment environment = ApplicationContextUtil.getBean(Environment.class);
//                  String active = environment.getProperty("spring.profiles.active").toUpperCase();
//
//                  APP_KEY = properties.getProperty(active + "_APP_KEY");
//                  MASTER_SECRET = properties.getProperty(active + "_MASTER_SECRET");
//                  ENVIRONMENT = Boolean.parseBoolean(properties.getProperty(active + "_ISDEV"));
//
//              } catch (Exception e) {
//                  log.error("激光推送加载配置文件出错", e);
//              }
//          }
//    }

    /**
     * 极光推送
     */
    public static void jiguangPush() {
        String alias = "1234567";//声明别名
        log.info("对别名" + alias + "的用户推送信息");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("1", "111");
        jsonObject.put("2", "222");

        PushResult result = push(String.valueOf(alias), jsonObject.toJSONString());
        if (result != null && result.isResultOK()) {
            log.info("针对别名" + alias + "的信息推送成功！");
        } else {
            log.info("针对别名" + alias + "的信息推送失败！");
        }
    }

    /**
     * 根据别名alias 发送透传消息给android_ios用户
     */
    public static void sendPushIosAndroidMsgByalias(String msg, String alias, Map<String, String> map) {
        //init();
        ClientConfig clientConfig = ClientConfig.getInstance();
        final JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
        final PushPayload payload = buildPushObject_all_alias_Message(msg, alias, map);
        try {
            PushResult result = jpushClient.sendPush(payload);
            log.info("Got result - " + result);
            // 如果使用 NettyHttpClient，需要手动调用 close 方法退出进程
            // If uses NettyHttpClient, call close when finished sending request, otherwise process will not exit.
            // jpushClient.close();
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            log.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        }
    }

    /**
     * 发送消息给android_ios用户
     * @param title 标题
     * @param alert 内容
     * @param map 参数信息 例如：key:0,value:2
     */
    public static void sendPushIosAndroid(String title, String alert, Map<String , String > map) {
        //init();
        ClientConfig clientConfig = ClientConfig.getInstance();
        final JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
        final PushPayload payload = buildPushObject_android_and_ios(title, alert, map);
        try {
            PushResult result = jpushClient.sendPush(payload);
            log.info("Got result - " + result);
            // 如果使用 NettyHttpClient，需要手动调用 close 方法退出进程
            // If uses NettyHttpClient, call close when finished sending request, otherwise process will not exit.
            // jpushClient.close();
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            log.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        }
    }

     /**
     *根据别名 alias 发送消息给android_ios用户
     * @param alias 别名
     * @param title 标题
     * @param alert 内容
     * @param map 参数信息 例如：key:0,value:2
     */
    public static void sendPushIosAndroidByAlias(String alias,String title, String alert, Map<String , String > map) {
        //init();
        ClientConfig clientConfig = ClientConfig.getInstance();
        final JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
        final PushPayload payload = buildPushObject_android_and_ios_alias(alias,title, alert, map);
        try {
            PushResult result = jpushClient.sendPush(payload);
            log.info("Got result - " + result);
            // 如果使用 NettyHttpClient，需要手动调用 close 方法退出进程
            // If uses NettyHttpClient, call close when finished sending request, otherwise process will not exit.
            // jpushClient.close();
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            log.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        }
    }


    /**
     * 生成极光推送对象PushPayload（采用java SDK）
     *
     * @param alias
     * @param alert
     * @return PushPayload
     */
    private static PushPayload buildPushObject_android_ios_alias_alert(String alias, String alert) {
        //init();
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .addExtra("type", "infomation")
                                .setAlert(alert)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .addExtra("type", "infomation")
                                .setAlert(alert)
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(ENVIRONMENT)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();
    }

    /**
     * 根据别名alias 发送透传消息给android_ios用户
     * success
     *
     * @param message
     * @param extras
     * @return
     */
    private static PushPayload buildPushObject_all_alias_Message(String message, String alias,
                                                                 Map<String, String> extras) {
        //init();
        return PushPayload.newBuilder()
                // 设置平台
                .setPlatform(Platform.android_ios())
                // 按什么发送 tag alias
                .setAudience(Audience.alias(alias))
                // 发送通知
                .setMessage(Message.newBuilder().
                        setMsgContent(message).
                        addExtras(extras).build())
                //设置ios平台环境  True 表示推送生产环境，False 表示要推送开发环境   默认是开发
                .setOptions(Options.newBuilder().setApnsProduction(ENVIRONMENT).build())
                .build();

    }

    /**
     * 发送透传消息 给分组用户
     *
     * @param message
     * @param tag     分组
     * @param extras  透传信息
     * @return
     */
    private static PushPayload buildPushObject_tag_Message(String tag, String message,
                                                           Map<String, String> extras) {
        //init();
        return PushPayload.newBuilder()
                // 设置平台
                .setPlatform(Platform.all())
                // 按什么发送 tag alias
                .setAudience(Audience.tag())
                // 发送通知
                .setMessage(Message.newBuilder().
                        setMsgContent(message).
                        addExtras(extras).build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(ENVIRONMENT) //设置ios平台环境  True 表示推送生产环境，False 表示要推送开发环境   默认是开发
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();

    }


    /**
     * 发送通知 给 android_ios  别名
     *
     * @param alias
     * @param title
     * @param content
     * @return
     */
    public static PushPayload buildPushObject_android_and_iosByAlias(String alias, String title, String content) {
        //init();
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(content)
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtra(title, content).build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(ENVIRONMENT) //设置ios平台环境  True 表示推送生产环境，False 表示要推送开发环境   默认是开发
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();
    }


    /**
     * 通知推送 ios和android
     *
     * @return
     */
    public static PushPayload buildPushObject_android_and_ios(String title, String alert,Map<String , String > extras) {
        //init();

        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(alert)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title)
                                .addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtras(extras).build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(ENVIRONMENT)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();
    }
    /**
     * 通过alias通知推送 ios和android
     *
     * @return
     */
    public static PushPayload buildPushObject_android_and_ios_alias(String alias,String title, String alert, Map<String, String> extras) {
        //init();
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(alert)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title)
                                .addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtras(extras).build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(ENVIRONMENT)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();
    }



    /**
     * 极光推送方法(采用java SDK)
     *
     * @param alias
     * @param alert
     * @return PushResult
     */
    public static PushResult push(String alias, String alert) {
        //init();
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
        PushPayload payload = buildPushObject_android_ios_alias_alert(alias, alert);
        try {
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }
    }

    public static void main(String[] args) {
//        while (true) {

//            jiguangPush();
//            LockSupport.parkNanos(100 * 10000 * 1000);
        System.out.println("1111");

        Map<String, String> map = new HashMap<>();
        map.put("type", PushExtrasEnum.LOGIN_ONLY.getCode());  //101d8559094d6be1f3f
        JiguangPush.sendPushIosAndroidMsgByalias(PushExtrasEnum.LOGIN_ONLY.getValue(), "140fe1da9ef939696a4", map);
        JiguangPush.sendPushIosAndroidByAlias("140fe1da9ef939696a4","title", "alert", map);
        System.out.println("222222");

//        }

    }
}