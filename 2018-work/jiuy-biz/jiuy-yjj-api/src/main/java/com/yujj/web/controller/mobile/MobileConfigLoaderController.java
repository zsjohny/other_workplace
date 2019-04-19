/**
 * 
 */
package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.ad.AdEnum;
import com.yujj.business.service.AdService;
import com.yujj.business.service.GlobalSettingService;
import com.jiuyuan.entity.AdConfig;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.entity.product.Product;

/**
 * @author LWS
 */
@Controller
@RequestMapping("/mobile/config")
public class MobileConfigLoaderController {

    @Autowired
    private AdService adService;

    @Autowired
    private GlobalSettingService globalSettingService;

    /**
     * 加载配置信息
     * 
     * @param reqdev
     * @return
     */
    @RequestMapping
    @ResponseBody
    public JsonResponse loadConfiguration(ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("address", getAddress());
        data.put("serverUrl", Constants.SERVER_URL);
        data.put("serverUrlHttps", Constants.SERVER_URL_HTTPS);
        data.put("exchangeDescription", "以旧换新兑换商品仅适用于在实体店购买的，且带有俞姐姐二维码吊牌的衣服，您需要将旧衣服和二维码吊牌都一起回寄，您确定继续兑换吗？");
        data.put("phoneBindTips", "说明：因《移动互联网应用程序信息服务管理规定》正式实施，请您按照相关步骤进行真实身份信息认证，给您带来不便敬请谅解。");

        if (clientPlatform.isAndroid()) {
            Map<String, Object> androidUpdate = new HashMap<String, Object>();
            androidUpdate.put("latestVersion", Client.ANDROID_LATEST_VERSION);
            androidUpdate.put("lasterVersion", Client.ANDROID_LATEST_VERSION);
            androidUpdate.put("downloadUrl", Client.ANDROID_LATEST_URL);
            androidUpdate.put("size", Client.ANDROID_LATEST_SIZE);
            androidUpdate.put("forceUpdate", Client.ANDROID_FORCE_UPDATE);
            data.put("androidUpdate", androidUpdate);
        } else if (clientPlatform.isIphone()) {
            Map<String, Object> iphoneUpdate = new HashMap<String, Object>();
            iphoneUpdate.put("latestVersion", Client.IPHONE_LATEST_VERSION);
            iphoneUpdate.put("downloadUrl", Client.IPHONE_LATEST_URL);
            iphoneUpdate.put("forceUpdate", Client.IPHONE_FORCE_UPDATE);
            data.put("iphoneUpdate", iphoneUpdate);
        }

        Map<String, Object> shareContent = new HashMap<String, Object>();
        
        String title = "20元品牌时代来临，不用剁手买买买，还不上车？";
        String description = "下载俞姐姐客户端，品牌女装全场1折，靓衣20元抢不停，注册更送价值千元玖币！";
        String imageUrl = "/static/img/share-icon.png";
        String url = "/static/app/login/registershare.html";
      
        JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.AD_TITLES);
		for(Object obj : jsonArrayConfirm) {
		    
		    if(((JSONObject)obj).get("title") != null ){
				title = (String) ((JSONObject)obj).get("title");
			}
			if(((JSONObject)obj).get("description") != null){
				description = (String) ((JSONObject)obj).get("description");
			}
			if(((JSONObject)obj).get("imageUrl") != null){
				imageUrl = (String) ((JSONObject)obj).get("imageUrl");
			}
			if(((JSONObject)obj).get("url") != null){
				imageUrl = (String) ((JSONObject)obj).get("url");
			}
		  
		}   
        shareContent.put("title", title);
        shareContent.put("description", description);
        shareContent.put("imageUrl", Constants.SERVER_URL + imageUrl);
        shareContent.put("url", Constants.SERVER_URL_HTTPS + url);
        data.put("shareContent", shareContent);
        
//        shareContent.put("title", "20元品牌时代来临，不用剁手买买买，还不上车？");
//        shareContent.put("description", "下载俞姐姐客户端，品牌女装全场1折，靓衣20元抢不停，注册更送价值千元玖币！");
//        shareContent.put("imageUrl", Constants.SERVER_URL + "/static/img/share-icon.png");
//        shareContent.put("url", Constants.SERVER_URL_HTTPS + "/static/app/login/registershare.html");
//        data.put("shareContent", shareContent);


        data.put("navConfig", globalSettingService.getJsonArray(GlobalSettingName.NAV_BAR));       
        
        return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 加载配置信息-新接口
     * 
     * @param reqdev
     * @return
     */
    @RequestMapping("/client15")
    @ResponseBody
    public JsonResponse loadConfiguration15(ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("webStore", "false");
        data.put("address", getAddress());
        data.put("serverUrl", Constants.SERVER_URL);
        data.put("serverUrlHttps", Constants.SERVER_URL_HTTPS);
        data.put("exchangeDescription", "以旧换新兑换商品仅适用于在实体店购买的，且带有俞姐姐二维码吊牌的衣服，您需要将旧衣服和二维码吊牌都一起回寄，您确定继续兑换吗？");
        data.put("phoneBindTips", "说明：因《移动互联网应用程序信息服务管理规定》正式实施，请您按照相关步骤进行真实身份信息认证，给您带来不便敬请谅解。");

    	String versionUpdateJson = globalSettingService.getSetting(GlobalSettingName.VERSION_UPDATE);

        Map<String, Object> umengInfo = new HashMap<String, Object>();
        
        if (clientPlatform.isAndroid()) {
            Map<String, Object> androidUpdate = new HashMap<String, Object>();
            androidUpdate.put("latestVersion", Client.ANDROID_LATEST_VERSION);
            androidUpdate.put("lasterVersion", Client.ANDROID_LATEST_VERSION);
            androidUpdate.put("downloadUrl", Client.ANDROID_LATEST_URL);
            androidUpdate.put("size", Client.ANDROID_LATEST_SIZE);
            androidUpdate.put("forceUpdate", Client.ANDROID_FORCE_UPDATE);
            data.put("androidUpdate", androidUpdate);
            
            Object versionUpdate = JSON.parse(versionUpdateJson);

            data.put("versionUpdate", versionUpdate);
            

            umengInfo.put("appKey", Client.ANDROID_UMENG_APP_KEY);
        } else if (clientPlatform.isIphone()) {
            Map<String, Object> iphoneUpdate = new HashMap<String, Object>();
            iphoneUpdate.put("latestVersion", Client.IPHONE_LATEST_VERSION);
            iphoneUpdate.put("downloadUrl", Client.IPHONE_LATEST_URL);
            iphoneUpdate.put("forceUpdate", Client.IPHONE_FORCE_UPDATE);
            data.put("iphoneUpdate", iphoneUpdate);
            
            // 弥补ios app强制升级true误写为ture 2016-06-24
            Object versionUpdate = JSON.parse(versionUpdateJson.replace("\"forceUpdate\":\"true\"", "\"forceUpdate\":\"ture\""));

            data.put("versionUpdate", versionUpdate);

            umengInfo.put("appKey", Client.IPHONE_UMENG_APP_KEY);
        }

        Map<String, Object> shareContent = new HashMap<String, Object>();
        shareContent.put("title", "衣旧换新，免费兑换尽在俞姐姐！玖币拿不停，最优惠最时尚尽在俞姐姐！");
        shareContent.put("description", "免费兑换，注册送玖币，豪送千万，最优惠的活动，俞姐姐带你进入折扣的疯狂！赶紧注册下载吧");
        shareContent.put("imageUrl", Constants.SERVER_URL + "/static/img/share-icon.png");
        shareContent.put("url", Constants.SERVER_URL_HTTPS + "/static/app/login/registershare.html");
        data.put("shareContent", shareContent);
        data.put("navConfig", globalSettingService.getJsonArray(GlobalSettingName.NAV_BAR));  

        Map<String, Object> ossInfo = new HashMap<String, Object>();
        ossInfo.put("defaultBasePathName", Client.OSS_DEFAULT_BASEPATH_NAME);
        ossInfo.put("accessKeyId", Client.OSS_ACCESS_KEY_ID);
        ossInfo.put("accessKeySecret", Client.OSS_ACCESS_KEY_SECRET);
        ossInfo.put("endPoint", Client.OSS_END_POINT);
        ossInfo.put("imgService", Client.OSS_IMG_SERVICE);
        data.put("ossInfo", ossInfo);

        Map<String, Object> getuiInfo = new HashMap<String, Object>();
        getuiInfo.put("appId", Client.GETUI_APP_ID);
        getuiInfo.put("appKey", Client.GETUI_APP_KEY);
        getuiInfo.put("appSecret", Client.GETUI_APP_SECRET);
        getuiInfo.put("masterSecret", Client.GETUI_MASTER_SECRET);
        data.put("getuiInfo", getuiInfo);
        
        Map<String, Object> qiyukfInfo = new HashMap<String, Object>();
        
        qiyukfInfo.put("appKey", Client.QIYUKF_APP_KEY);
        data.put("qiyukfInfo", qiyukfInfo);        
        
        Map<String, String> regSucessImgMap = new HashMap<String, String>();
        
    	String img2208 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/regist/1242x2208.png";
    	String img1136 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/regist/640x1136.png";
    	String img960 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/regist/640x960.png";
    	String img1334 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/regist/750x1334.png";
    	
    	regSucessImgMap.put("img2208", img2208);
    	regSucessImgMap.put("img1136", img1136);
    	regSucessImgMap.put("img960", img960);
    	regSucessImgMap.put("img1334", img1334);
    	
    	data.put("regSucessImgMap", regSucessImgMap);   
    	data.put("couponlicenseHtml", "static/app/couponlicense.html");

    	data.put("fetchlicenseHtml", "static/app/fetchlicense.html");
 
    	data.put("giftlicenseHtml", "static/app/giftlicense.html");
    	
    	data.put("couponlicenseHtmlTitle", "使用说明");
    	
    	data.put("fetchlicenseHtmlTitle", "使用说明");
    	
    	data.put("giftlicenseHtmlTitle", "领取说明");
    	
    	data.put("usernameTips", "请输入手机号码或用户名");
    	
    	data.put("passwordTips", "请输入密码");
        
//    	Product.appVersion = clientPlatform.getVersion();
        //一网通优惠提示
        data.put("cmbPayTips", "");
        data.put("umengInfo", umengInfo);    
        return jsonResponse.setSuccessful().setData(data);
    }

    // 加载公司收货地址
    private Address getAddress() {
        Address addr = new Address();
        addr.setAddrDetail("五星工业园3幢6楼");
        addr.setFixPhone("0571-23456789");
        addr.setAddrFull("杭州市余杭区乔司镇五星工业园3幢6楼\n" + addr.getFixPhone());
        addr.setProvinceName("浙江省");
        addr.setCityName("杭州市");
        addr.setDistrictName("余杭区");
        addr.setFixPhone("0571-23456789");
        addr.setReceiverName("俞姐姐");
        addr.setMailCode("310000");
        return addr;
    }

    @RequestMapping("/banners")
    @ResponseBody
    public JsonResponse getBanner(@RequestParam("type") AdEnum adType) {
        JsonResponse jsonResponse = new JsonResponse();
        if (adType == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        List<AdConfig> adConfigs = adService.getAdsByType(adType);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("banners", adConfigs);

        return jsonResponse.setSuccessful().setData(data);
    }
}
