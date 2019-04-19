/**
 * 
 */
package com.jiuy.store.api.tool.controller;

import java.util.ArrayList;
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
import com.jiuyuan.constant.ad.AdType;
import com.jiuyuan.entity.AdConfig;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.web.help.JsonResponse;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.util.RSAUtil;
import com.jiuyuan.util.VersionUtil;
import com.yujj.entity.product.Product;

/**
 * @author LWS
 */
@Controller
@RequestMapping("/mobile/config")
public class MobileConfigLoaderController {



    @Autowired
    private ShopGlobalSettingService globalSettingService;
    
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

		if (!clientPlatform.isAndroid() && !clientPlatform.isIphone())
			return jsonResponse;

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("brandTypeList", getBrandTypeList());
        data.put("webOrder", "false");
        data.put("webAftersale", "false");
        data.put("brandSearch", "搜索品牌");
        data.put("productSearch", "搜索商品");
        data.put("address", getAddress());
        data.put("serverUrl", Constants.SERVER_URL);
        data.put("serverUrlHttps", Constants.SERVER_URL_HTTPS);
        data.put("exchangeDescription", "以旧换新兑换商品仅适用于在实体店购买的，且带有俞姐姐二维码吊牌的衣服，您需要将旧衣服和二维码吊牌都一起回寄，您确定继续兑换吗？");
        data.put("phoneBindTips", "说明：因《移动互联网应用程序信息服务管理规定》正式实施，请您按照相关步骤进行真实身份信息认证，给您带来不便敬请谅解。");
        data.put("loginUserNameTips", "请输入商家号码或者注册手机号码");
        data.put("loginPasswordTips", "请输入登录密码");

    	String versionUpdateJson = globalSettingService.getSetting(GlobalSettingName.STORE_VERSION_UPDATE);

        Map<String, Object> umengInfo = new HashMap<String, Object>();
        
        if (clientPlatform.isAndroid() && versionUpdateJson != null) {
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
        } else if (clientPlatform.isIphone() && versionUpdateJson != null) {
            Map<String, Object> iphoneUpdate = new HashMap<String, Object>();
            iphoneUpdate.put("latestVersion", Client.IPHONE_LATEST_VERSION);
            iphoneUpdate.put("downloadUrl", Client.IPHONE_LATEST_URL);
            iphoneUpdate.put("forceUpdate", Client.IPHONE_FORCE_UPDATE);
            data.put("iphoneUpdate", iphoneUpdate);
            
            // 弥补ios app强制升级true误写为ture 2016-06-24
			Object versionUpdate = JSON.parse(versionUpdateJson);

            data.put("versionUpdate", versionUpdate);

            umengInfo.put("appKey", Client.IPHONE_UMENG_APP_KEY);
        }

        Map<String, Object> shareContent = new HashMap<String, Object>();
        shareContent.put("title", "衣旧换新，免费兑换尽在俞姐姐！玖币拿不停，最优惠最时尚尽在俞姐姐！");
        shareContent.put("description", "免费兑换，注册送玖币，豪送千万，最优惠的活动，俞姐姐带你进入折扣的疯狂！赶紧注册下载吧");
        shareContent.put("imageUrl", Constants.SERVER_URL + "/static/img/share-icon.png");
        shareContent.put("url", Constants.SERVER_URL_HTTPS + "/static/app/login/registershare.html");
        data.put("shareContent", shareContent);
        data.put("navConfig", globalSettingService.getJsonArray(GlobalSettingName.STORE_NAV_BAR));  

        Map<String, Object> ossInfo = new HashMap<String, Object>();
        ossInfo.put("defaultBasePathName", Client.OSS_DEFAULT_BASEPATH_NAME);

		if (VersionUtil.ge(clientPlatform.getVersion(), "3.0.1")) {
			ossInfo.put("accessKeyId", RSAUtil.encryptAsBase64(Client.OSS_ACCESS_KEY_ID));
			ossInfo.put("accessKeySecret", RSAUtil.encryptAsBase64(Client.OSS_ACCESS_KEY_SECRET));
		} else {
			ossInfo.put("accessKeyId", Client.OSS_ACCESS_KEY_ID);
			ossInfo.put("accessKeySecret", Client.OSS_ACCESS_KEY_SECRET);
		}

        ossInfo.put("endPoint", Client.OSS_END_POINT);
        ossInfo.put("imgService", Client.OSS_IMG_SERVICE);
        data.put("ossInfo", ossInfo);

        Map<String, Object> getuiInfo = new HashMap<String, Object>();
        getuiInfo.put("appId", Client.STORE_GETUI_APP_ID);
        getuiInfo.put("appKey", Client.STORE_GETUI_APP_KEY);
        getuiInfo.put("appSecret", Client.STORE_GETUI_APP_SECRET);
        getuiInfo.put("masterSecret", Client.STORE_GETUI_MASTER_SECRET);
        data.put("getuiInfo", getuiInfo);
        
        Map<String, Object> qiyukfInfo = new HashMap<String, Object>();

        qiyukfInfo.put("appKey", Client.QIYUKF_APP_KEY);
        qiyukfInfo.put("productGroupId", Client.QIYUKF_PRODUCT_GROUPID);
        qiyukfInfo.put("helpGroupId", Client.QIYUKF_HELP_GROUPID);
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
    	
    	data.put("usernameTips", "请输入用户名");
    	
    	data.put("passwordTips", "请输入密码");
    	
    	data.put("helpInfo", helpInfo());
    	
        
//    	Product.appVersion = clientPlatform.getVersion();
//    	System.out.println("clientPlatform.getVersion():"+ clientPlatform.getVersion());
//    	System.out.println("Product.appVersion:" + Product.appVersion );
        //一网通优惠提示
        data.put("cmbPayTips", "");
        data.put("umengInfo", umengInfo);    
        return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 获取品牌类型
     * @return
     */
    private Object getBrandTypeList() {
    	
    	List<Map<String, Object>> brandTypeList = new  ArrayList<Map<String, Object>>();
    	Map<String, Object> brandType1 = new HashMap<String, Object>();
    	brandType1.put("brandTypeValue", "1");//品牌类型：1：高档，2：中档
    	brandType1.put("brandTypeName", "高档");//品牌类型：1：高档，2：中档
    	brandType1.put("homeBrandTypeIcon", "http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/9D8C6103-7252-404B-A598-10CE1567826C.jpg");//首页高低档图标
    	brandType1.put("selectBrandTypeIcon", "http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/4ECB063F-8264-485B-A848-E373FA0F9D3A.jpg");//选择高低档图标
    	brandType1.put("activityPlaceId", "21");//专场ID
    	Map<String, Object> brandType2 = new HashMap<String, Object>();
    	brandType2.put("brandTypeValue", "2");//品牌类型：1：高档，2：中档
    	brandType2.put("brandTypeName", "中档");//品牌类型：1：高档，2：中档
    	brandType2.put("homeBrandTypeIcon", "http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/FE974595-22E2-4DCB-A46E-F595E34CE55D.jpg");//首页高低档图标
    	brandType2.put("selectBrandTypeIcon", "http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/844250B8-1D41-4B83-B1C3-AA5AF4293449.jpg");//选择高低档图标
    	brandType2.put("activityPlaceId", "22");//专场ID
    	brandTypeList.add(brandType1);
    	brandTypeList.add(brandType2);
        return brandTypeList;
	}

	private Map<String, Object> helpInfo() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("helpUrl", Constants.SERVER_URL + "/static/app/help.html");
        result.put("aboutUrl", Constants.SERVER_URL + "/static/app/about.html");
        result.put("agreementlUrl", Constants.SERVER_URL + "/static/app/policy.html");
        return result;
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

//    @RequestMapping("/banners")
//    @ResponseBody
//    public JsonResponse getBanner(@RequestParam("type") AdType adType) {
//        JsonResponse jsonResponse = new JsonResponse();
//        if (adType == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
//        }
//
//        List<AdConfig> adConfigs = adService.getAdsByType(adType);
//        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("banners", adConfigs);
//
//        return jsonResponse.setSuccessful().setData(data);
//    }
}
