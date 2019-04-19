package com.jfinal.third.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.kit.PropKit;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;
import com.jfinal.weixin.jiuy.cache.MemcacheApi;
import com.jfinal.weixin.jiuy.service.ApiManager;
import com.jfinal.weixin.jiuy.service.MemberService;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *  测试类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/9 14:55
 * @Copyright 玖远网络
 */
public class Test {


    protected static MemcacheApi memcacheApi = Duang.duang(MemcacheApi.class);

    private static String customMessageUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

    private static String thirdAppid = "wx22beffcfcd7aea09";

    private static String memberId = "oJ82J5TguPrYYBHAEyAxFejC0qhk";

    private static String wxaAppid = "wx12208447172b2bda";

    public static void main(String[] args) {

        String accessToken = getAccessToken();
        String message = "老地方见..";


        Map<String, Object> json = new HashMap<>();
        json.put("touser", memberId);
        json.put("msgtype", "text");
        Map<String, Object> textObj = new HashMap<>();
        textObj.put("content", message);
        json.put("text", textObj);

        for(int z=0;z<2;z++){
            String jsonResult = HttpUtils.post(customMessageUrl + accessToken, JsonUtils.toJson(json));
            System.out.println(jsonResult);
        }


    }


    /**
     * 从数据库获取刷新token
     * @param appId
     * @return
     */
    public static String getRefreshToken(String appId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appId);
        Response<String> resp = Requests.get("http://wxaonline.yujiejie.com"+ApiManager.getRefreshTokenUrl).params(map).text();
        String body = resp.getBody();

        JSONObject jsonObject = JSONObject.parseObject(body);
        return jsonObject.getJSONObject("data").getString("refreshToken");
    }





    public static String getAccessToken() {

        String component_access_token = get_component_access_token();
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="+component_access_token;
        String refCode = "refreshtoken@@@tyd5JFIpvuxA2ptLO9H9ko1rc7yE4Ua_zOuMXtSIe8Y";
        String refCode2 = getRefreshToken(wxaAppid);
        //        //组装参数调用接口
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("component_appid", thirdAppid);
        paramMap.put("authorizer_appid", wxaAppid);
        paramMap.put("authorizer_refresh_token", refCode2);
        String param = JSONObject.toJSONString(paramMap);
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        //解析返回值，将token放入缓存
        System.out.println(retMap+"  accessToken");

        return (String) retMap.get("authorizer_access_token");
    }


    public static String get_component_access_token (){

        /**
         *
         * third_appId=wx22beffcfcd7aea09
         * third_appSecret=a797b78144a7229ad7d72c8e182e0c93
         * third_token=jiuyuan2017
         * third_encodingAesKey=9HuP5H2P2Fk5wiZyYoWAAMjwJqq3Vt9HuP5H2P2Fk5w
         *
         **/
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
        //1、从缓存中获取ticket
        String component_verify_ticket = memcacheApi.getTicket("wx22beffcfcd7aea09");
        if(StringUtils.isEmpty(component_verify_ticket)){
            return null;
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("component_appid", "wx22beffcfcd7aea09");
        paramMap.put("component_appsecret","a797b78144a7229ad7d72c8e182e0c93");
        paramMap.put("component_verify_ticket", component_verify_ticket);
        String param = JSONObject.toJSONString(paramMap);
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);

        String component_access_token = (String) retMap.get("component_access_token");
        //2、将token存入缓存{errcode=61004, errmsg=access clientip is not registered hint: [x0ZhJA0817e575] requestIP: 58.101.18.64}
        if(StringUtils.isEmpty(component_access_token)){
            return null;
        }else{
            //7200
            return component_access_token;
        }
    }
}
