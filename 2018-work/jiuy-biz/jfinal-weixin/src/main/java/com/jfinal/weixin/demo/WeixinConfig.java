/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.demo;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import com.jfinal.third.ThirdWxaConfig;
import com.jfinal.third.ThirdWxaConfigKit;
import com.jfinal.third.controller.ThirdWxaUserApiController;
import com.jfinal.third.controller.WeixinThirdController;
import com.jfinal.third.controller.WeixinThirdWxaMsgController;
import com.jfinal.weixin.jiuy.controller.JiuyCodeController;
import com.jfinal.weixin.jiuy.controller.JiuyServerMsgController;
import com.jfinal.weixin.jiuy.controller.JiuyServiceAdviceController;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;

public class WeixinConfig extends JFinalConfig {
    // 本地开发模式
//    private boolean isLocalDev = false;


    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 80, "/", 5);
    }

//    /**
//     * 如果生产环境配置文件存在，则优先加载该配置，否则加载开发环境配置文件
//     * @param pro 生产环境配置文件
//     * @param dev 开发环境配置文件
//     */
//    public void loadProp(String pro, String dev) {
//        try {
//            PropKit.use(pro);
//        }
//        catch (Exception e) {
//            PropKit.use(dev);
//            isLocalDev = true;
//        }
//    }

    public void configConstant(Constants me) {
//        loadProp("a_little_config_pro.txt", "a_little_config.txt");
    	PropKit.use("a_little_config.txt");
        me.setDevMode(PropKit.getBoolean("devMode", false));
        // ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据
        ApiConfigKit.setDevMode(me.getDevMode());//设置公众号开发模式
        WxaConfigKit.setDevMode(me.getDevMode());//设置小程序开发模式
        ThirdWxaConfigKit.setDevMode(me.getDevMode());//设置第三方小程序开发模式
    }

    public void configRoute(Routes me) {
    	//公众号
    	 me.add("/msg", WeixinWxaMsgController.class);
//    	 me.add("/msg", WeixinMsgController.class);
        me.add("/api", WeixinApiController.class, "/api");
        me.add("/pay", WeixinPayController.class);
        me.add("/wxa/user", WxaUserApiController.class);
        me.add("/servermsg", JiuyServerMsgController.class);
        me.add("/serviceAdvice", JiuyServiceAdviceController.class);
        me.add("/code", JiuyCodeController.class);
        me.add("/third", WeixinThirdController.class);
        me.add("/third/callback", WeixinThirdWxaMsgController.class);
        me.add("/third/wxa/user", ThirdWxaUserApiController.class);



        //小程序
        //me.add("/wxaMsg", WeixinWxaMsgController.class);
    }



    public void configPlugin(Plugins me) {
        // C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        // me.adbioldstd(c3p0Plugin);

        // EhCachePlugin ecp = new EhCachePlugin();
        // me.add(ecp);

        // 使用redis分布accessToken
        // RedisPlugin redisPlugin = new RedisPlugin("weixin", "127.0.0.1");
        // redisPlugin.setSerializer(JdkSerializer.me); // 需要使用fst高性能序列化的用户请删除这一行（Fst jar依赖请查看WIKI）
        // me.add(redisPlugin);
    }

    public void configInterceptor(Interceptors me) {
        // 设置默认的 appId 规则，默认值为appId，可采用url挂参数 ?appId=xxx 切换多公众号
        // ApiInterceptor.setAppIdParser(new AppIdParser.DefaultParameterAppIdParser("appId")); 默认无需设置
        // MsgInterceptor.setAppIdParser(new AppIdParser.DefaultParameterAppIdParser("appId")); 默认无需设置
    }

    public void configHandler(Handlers me) {

    }

    public void afterJFinalStart() {
        // 1.5 之后支持redis存储access_token、js_ticket，需要先启动RedisPlugin
//        ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache());
        // 1.6新增的2种初始化
//        ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache(Redis.use("weixin")));
//        ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache("weixin"));

        ApiConfig ac = new ApiConfig();
        // 配置微信 API 相关参数
        ac.setToken(PropKit.get("token"));
        ac.setAppId(PropKit.get("appId"));
        ac.setAppSecret(PropKit.get("appSecret"));

        /**
         *  是否对消息进行加密，对应于微信平台的消息加解密方式：
         *  1：true进行加密且必须配置 encodingAesKey
         *  2：false采用明文模式，同时也支持混合模式
         */
        ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", true));
        ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));

        /**
         * 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认。
         */
        ApiConfigKit.putApiConfig(ac);

        /**
         * 1.9 新增LocalTestTokenCache用于本地和线上同时使用一套appId时避免本地将线上AccessToken冲掉
         *
         * 设计初衷：https://www.oschina.net/question/2702126_2237352
         *
         * 注意：
         * 1. 上线时应保证此处isLocalDev为false，或者注释掉该不分代码！
         *
         * 2. 为了安全起见，此处可以自己添加密钥之类的参数，例如：
         * http://localhost/weixin/api/getToken?secret=xxxx
         * 然后在WeixinApiController#getToken()方法中判断secret
         *
         * @see WeixinApiController#getToken()
         */
//        if (isLocalDev) {
//            String onLineTokenUrl = "http://localhost/weixin/api/getToken";
//            ApiConfigKit.setAccessTokenCache(new LocalTestTokenCache(onLineTokenUrl));
//        }


        //初始化小程序配置
        WxaConfig wc = new WxaConfig();
        wc.setAppId(PropKit.get("wxa_appId"));
        wc.setAppSecret(PropKit.get("wxa_appSecret"));
        wc.setToken(PropKit.get("wxa_token"));
        wc.setMessageEncrypt(PropKit.getBoolean("wxa_encryptMessage", true));
        wc.setEncodingAesKey(PropKit.get("wxa_encodingAesKey", "setting it in config file"));
        WxaConfigKit.setWxaConfig(wc);

      //初始化第三方小程序配置
        ThirdWxaConfig twc = new ThirdWxaConfig();
        twc.setAppId(PropKit.get("third_appId"));
        twc.setAppSecret(PropKit.get("third_appSecret"));
        twc.setToken(PropKit.get("third_token"));
        twc.setMessageEncrypt(PropKit.getBoolean("third_encryptMessage", true));
        twc.setEncodingAesKey(PropKit.get("third_encodingAesKey", "setting it in config file"));
        ThirdWxaConfigKit.setThirdWxaConfig(twc);


    }



	@Override
	public void configEngine(Engine engine) {

	}
}
