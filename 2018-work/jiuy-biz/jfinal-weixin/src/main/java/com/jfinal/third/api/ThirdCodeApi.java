
package com.jfinal.third.api;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.Version;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;

/**
 * 微信 第三方代码管理相关api接口
 *
 * @author zhaoxinglin
 * 相关文档地址：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1489140610_Uavc4&token=&lang=zh_CN
 */
public class ThirdCodeApi extends Controller {
    static Log logger = Log.getLog(ThirdCodeApi.class);
    //为授权的小程序帐号上传小程序代码URL
//	protected ThirdWxaQrcodeApi thirdWxaQrcodeApi = Duang.duang(ThirdWxaQrcodeApi.class);
    public static void main(String[] args) {

    }
    private static final String VERSION_PREX_IN_SHOP = "inShop:";

    protected ThirdApi thirdApi = Duang.duang(ThirdApi.class);

//	public static void main(String[] args) {
//		String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=3Nt4YUUiCDFEi4FOabIJddT3mBpBxBGAGOFHa6E_4S-ooQxpdGg8WTxOAwMwohSGwkPYo8ZPozUJXGdR095R3NbLn6ZTqcuWVZeG0GjVUYKJgCAAJ10rYSUUyytAEZIzPTKiAHDWAR";
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("path", "pages/index/index");
//		paramMap.put("width", "430");
////		paramMap.put("auto_color", true);//自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
//		//paramMap.put("line_color", "{r:0,g:0,b:0}");
//    	String param = JSONObject.toJSONString(paramMap);
//    	logger.info("==========url:{"+url+"}, param:{"+param+"}");
//    	String ret = WapPayHttpUtil.sendGetHttp(url, param);
//    	logger.info("==========ret:"+ret);
//	}


//	public static void main(String[] args) {
//		//String url = "https://api.weixin.qq.com/wxa/getwxacode";
//		String authorizer_token = "Rxsx95En3ccqaGjPsIYObfvgRqpS2LOEDWYrQHDAob1x5lRrcQceKIcwUk0B4nMybJ2YZDXcRgEnXssS2MqT8xwhGDhblxo68Y3-ewwUoCQHxwpwgkDvqGhlq6XVn3pBNEHcADDZQT";
//		String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token="+authorizer_token;
////		Map<String, Object> paramMap = new HashMap<String, Object>();
////		paramMap.put("access_token", access_token);
////		Response<String> resp = Requests.post(url).params(paramMap).text();
////		String body = resp.getBody();
////		logger.info(" url:{"+url+"}, ，body："+body);
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("path", "pages/index/index");
//		paramMap.put("width", "430");
//		paramMap.put("auto_color", true);//自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
//		//paramMap.put("line_color", "{r:0,g:0,b:0}");
//		String param = JSONObject.toJSONString(paramMap);
//		String ret = WapPayHttpUtil.sendPostHttp(url, param);
//    	logger.info(" url:{"+url+"}, ，ret："+ret);
//	}


    /**
     * 1、为授权的小程序帐号上传小程序代码
     * https://api.weixin.qq.com/wxa/commit?access_token=TOKEN
     *
     * @param appId
     * @param storeId
     * @param templateId
     */
    public String uploadCode(String appId, String storeId, String storeName, String templateId, String version, String desc, String testerId) {

        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("uploadCode获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/commit?access_token=" + authorizer_token;


        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("template_id", templateId);//代码库中的代码模版ID
        paramMap.put("ext_json", getAppConfig(appId, storeId, storeName, version));


        String finalVersion;
        if (version.startsWith(VERSION_PREX_IN_SHOP)) {
            finalVersion = version.substring(VERSION_PREX_IN_SHOP.length(), version.length());
            logger.info("店中店版本号 version:"+version);
        } else if (VersionUtil.gt(version, "100.0.0")) {
            logger.info("店中店版本号 version:"+version);
            finalVersion = version;
        } else {
            finalVersion = version;
            logger.info("非店中店 version:" + version);
        }
        paramMap.put("user_version", finalVersion);//代码版本号，开发者可自定义
        paramMap.put("user_desc", desc);//代码描述，开发者可自定义
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始上传小程序代码commit, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
//    	上传小程序代码commit完成, url:{https://api.weixin.qq.com/wxa/commit?access_token=null},
//    	param:{{"user_version":"V1.0","user_desc":"test","ext_json":"{\"ext\":{\"appId\":\"wxc85ba29a5a96637b\",\"storeId\":\"114\"},\"networkTimeout\":\"\",\"pages\":\"\",\"extAppid\":\"wxc85ba29a5a96637b\",\"tabBar\":\"\",\"extPages\":\"\",\"window\":\"\"}","template_id":"0"}}，
//    	retMap：{errcode=40001, errmsg=invalid credential, access_token is invalid or not latest hint: [E_B_5a0505dx24]}

//    	上传小程序代码commit完成, url:{https://api.weixin.qq.com/wxa/commit?access_token=oRMXuHHdoHf0k7JdB_oEgmz3TtWIiz0LvxluVEhYA2BI7zG24sUjzA0sBJA2Wpki-tR22TPqIcvBzZw3i7vEwuxGaJ0H8AFKTsftYonsuuzXZlT3lUMUzMRrsFY835lcXRHbAHDDXN},
//    	param:{{"user_version":"V1.0","user_desc":"test","ext_json":"{\"ext\":{\"appId\":\"wxc85ba29a5a96637b\",\"storeId\":\"114\"},\"networkTimeout\":\"\",\"pages\":\"\",\"extAppid\":\"wxc85ba29a5a96637b\",\"tabBar\":\"\",\"extPages\":\"\",\"window\":\"\"}","template_id":"0"}}，
//    	retMap：{errcode=85047, errmsg=pages are empty hint: [Pg95Xa0926e622]}
//    	上传小程序代码commit完成, url:{https://api.weixin.qq.com/wxa/commit?access_token=3x80cKOGZEmvRKMmk2h4JjC5gpyOmmMehvXHNL2opHasNk6RI5cdu0Qyg1ciKKLNniq2mHZqrr_P6LVDRgXFvNUr8_ohE_guSj6XLoOQdq9jTfED1i-XjS0OJmiglLeMWUFiAJDHFQ}, param:{{"user_version":"V1.0","user_desc":"test","ext_json":"{\"ext\":{\"appId\":\"wxc85ba29a5a96637b\",\"storeId\":\"114\"},\"networkTimeout\":{\"downloadFile\":\"10000\",\"request\":\"10000\"},\"pages\":[\"pages/index/index\"],\"extAppid\":\"wxc85ba29a5a96637b\",\"tabBar\":{\"list\":[{\"text\":\"首页\",\"pagePath\":\"pages/index/index\"}]},\"extPages\":{},\"window\":{\"navigationBarTitleText\":\"ZhaoXingLinDemo\"}}","template_id":"0"}}，retMap：{errcode=0, errmsg=ok}
        logger.info("上传小程序代码commit完成, url:{" + url + "}, param:{" + param + "}，retMap：" + retMap.toString());

        //修改小程序配置服务器地址
        modify_domain(appId);
        //获取体验小程序的体验二维码
        String wxaQrcodeUrl = getWxaQrcodeUrl(appId);
        retMap.put("wxaQrcodeUrl", wxaQrcodeUrl);
        //绑定微信用户为小程序体验者
        if (StringUtils.isNotEmpty(testerId)) {
            bind_tester(appId, testerId);
        }
        return retMap.toString();
    }

    /**
     * 2、获取体验小程序的体验二维码
     *
     * @param appId
     */
    public String getWxaQrcodeUrl(String appId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("getWxaQrcodeUrl获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/get_qrcode?access_token=" + authorizer_token;
        logger.info("获取体验小程序的体验二维码get_qrcode, url:{" + url);
        return url;
    }

    /**
     * 2、获取发布版小程序的体验二维码
     *
     * @param appId
     */
    public String getOnlineWxaQrcodeUrl(String appId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("getOnlineWxaQrcodeUrl获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String getwxacodeurl = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + authorizer_token;
        logger.info("获取发布版二维码get_qrcode, getwxacodeurl:{" + getwxacodeurl);

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("path", "pages/index/index");
        paramMap.put("width", "430");
//		paramMap.put("auto_color", true);//自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
        //paramMap.put("line_color", "{r:0,g:0,b:0}");
        String param = JSONObject.toJSONString(paramMap);
        logger.info("==========getwxacodeurl:{" + getwxacodeurl + "}, param:{" + param + "}");
        //二维码图片二进制
        String imgPath = WapPayHttpUtil.sendHttpSaveImgToAliYun(getwxacodeurl, param, "");
        logger.info("==========将二维码图片保存到服务器（）imgPath:{" + imgPath + "}");

        return imgPath;
    }

    /**
     * 2、获取小程序商品二维码
     * 微信文档地址：https://mp.weixin.qq.com/debug/wxadoc/dev/api/qrcode.html
     *
     * @param appId
     * @param realPath
     */
    public String getProductQrcodeUrl(String appId, String productId, String realPath) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("getOnlineWxaQrcodeUrl获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String getwxacodeunlimit = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + authorizer_token;
//		logger.info("获取小程序商品二维码, getwxacodeunlimit:{"+getwxacodeunlimit);
        Map<String, String> paramMap = new HashMap<String, String>();

//		Map<String, String> sceneMap = new HashMap<String, String>();
//		sceneMap.put("spId", productId);
//		sceneMap.put("rs", "1");//消息已读
//		sceneMap.put("sf", "1");//分享标记，1表示表示是分享详情跳转
//		String scene = JSONObject.toJSONString(sceneMap);
//		logger.debug("获取小程序商品二维码scene:"+scene);
//		paramMap.put("scene", scene);
//		最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
//		String scene = "shopProductId:"+productId+"_readState:1_shareFlag:1";
//		logger.info("前scene:"+scene);
//		scene = URLEncoder.encode(scene);
//		String scene = productId;
//		String scene = "shopProductId"+"VV"+productId+"XX"+"readState"+"VV"+1+"XX"+"shareFlag"+"VV"+1;
//		logger.info("后scene:"+scene);
        paramMap.put("scene", productId);
        paramMap.put("page", "pages/component/detail/detail");
        paramMap.put("width", "430");
//		paramMap.put("auto_color", true);//自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
        //paramMap.put("line_color", "{r:0,g:0,b:0}");
        String param = JSONObject.toJSONString(paramMap);
        logger.info("==========getwxacodeurl:{" + getwxacodeunlimit + "}, param:{" + param + "}");
        //二维码图片二进制
        String imgPath = WapPayHttpUtil.sendHttpSaveImgToAliYun(getwxacodeunlimit, param, realPath);
        logger.info("==========将小程序商品二维码图片保存到服务器()imgPath:{" + imgPath + "}");
        return imgPath;
    }




    /**
     * 修改小程序配置服务器地址
     * https://api.weixin.qq.com/wxa/modify_domain?access_token=TOKEN
     * {errcode=85017, errmsg=no domain to modify after filtered hint: [VxMdNA0215e626]}
     *
     * @param appId
     * @return
     */
    public String modify_domain(String appId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("修改小程序配置服务器地址authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String weixin_server_url = PropKit.get("weixin_server_url");
        String wxa_api_url_https = PropKit.get("wxa_api_url_https");

        String requestdomainStr = PropKit.get("requestdomain");
        String wsrequestdomainStr = PropKit.get("wsrequestdomain");
        String uploaddomainStr = PropKit.get("uploaddomain");
        String downloaddomainStr = PropKit.get("downloaddomain");
        String[] requestdomainArr = requestdomainStr.split(",");
        String[] wsrequestdomainArr = wsrequestdomainStr.split(",");
        String[] uploaddomainArr = uploaddomainStr.split(",");
        String[] downloaddomainArr = downloaddomainStr.split(",");


        String url = "https://api.weixin.qq.com/wxa/modify_domain?access_token=" + authorizer_token;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("action", "add");
        paramMap.put("requestdomain", requestdomainArr);
        paramMap.put("wsrequestdomain", wsrequestdomainArr);//wss://weixintest.yujiejie.com
        paramMap.put("uploaddomain", uploaddomainArr);
        paramMap.put("downloaddomain", downloaddomainArr);

        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始修改小程序配置服务器地址, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("修改小程序配置服务器地址, url:{" + url + "}, param:{" + param + "}，retMap：" + retMap.toString());
        return retMap.toString();
    }

    /**
     * 绑定微信用户为小程序体验者
     * https://api.weixin.qq.com/wxa/bind_tester?access_token=TOKEN
     *
     * @param appId
     * @return
     */
    public String bind_tester(String appId, String testerId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("绑定微信用户为小程序体验者authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/bind_tester?access_token=" + authorizer_token;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("wechatid", testerId);
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始绑定微信用户为小程序体验者, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("绑定微信用户为小程序体验者成功, url:{" + url + "}, param:{" + param + "}，retMap：" + retMap.toString());
        return retMap.toString();
    }

    /**
     * 绑定微信用户为小程序体验者
     * https://api.weixin.qq.com/wxa/bind_tester?access_token=TOKEN
     *
     * @param appId
     * @return
     */
    public String unbind_tester(String appId, String testerId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("解除绑定微信用户为小程序体验者authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/unbind_tester?access_token=" + authorizer_token;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("wechatid", testerId);
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始解除绑定微信用户为小程序体验者, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("解除绑定微信用户为小程序体验者成功, url:{" + url + "}, param:{" + param + "}，retMap：" + retMap.toString());
        return retMap.toString();
    }


    /**
     * 3、获取授权小程序帐号的可选类目
     * {"errcode":0,"errmsg":"ok","category_list":[{"first_class":"生活服务","second_class":"综合生活服务平台","first_id":150,"second_id":664}]}
     * https://api.weixin.qq.com/wxa/get_category?access_token=TOKEN
     *
     * @param appId
     * @return
     */
    public List<Map<String, Object>> getWxaCategory(String appId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("getWxaCategory获取小程序authorizer_token为空，请排查问题！！！！");
            logger.info("=================================");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/get_category?access_token=" + authorizer_token;

        logger.info("开始获取授权小程序帐号的可选类目, url:" + url);
        Response<String> resp = Requests.get(url).text();
//	 	{"errcode":0,"errmsg":"ok","category_list":[{"first_class":"生活服务","second_class":"综合生活服务平台","first_id":150,"second_id":664}]}
        String body = resp.getBody();
        logger.info("获取授权小程序帐号的可选类目成功, url:{" + url + "}，retMap：" + body);

        JSONObject bodyJson = JSONObject.parseObject(body);
        int errcode = bodyJson.getIntValue("errcode");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (errcode == 0) {
            JSONArray category_list = bodyJson.getJSONArray("category_list");
            for (Object item : category_list) {
                Map<String, Object> itemMap = (Map<String, Object>) item;
                list.add(itemMap);
            }

        }
        return list;
    }


    /**
     * 4、获取小程序的第三方提交代码的页面配置（仅供第三方开发者代小程序调用）
     * {"errcode":0,"errmsg":"ok","page_list":
     * ["pages\/index\/index",
     * "pages\/footprint\/footprint",
     * "pages\/want\/want","pages\/my\/my",
     * "pages\/component\/detail\/detail",
     * "pages\/component\/editname\/editname","pages\/component\/useredit\/useredit"]}
     * https://api.weixin.qq.com/wxa/get_category?access_token=TOKEN
     *
     * @param appId
     * @return
     */
    public String getPageConfig(String appId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("getPageConfig获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/get_page?access_token=" + authorizer_token;
        logger.info("开始获取页面配置, url:" + url);
        Response<String> resp = Requests.get(url).text();
        String body = resp.getBody();
        logger.info("获取页面配置成功, url:{" + url + "}，body：" + body);
        return body;
    }


    /**
     * 5、将第三方提交的代码包提交审核（仅供第三方开发者代小程序调用）
     *
     * @param appId
     * @param testVersion
     * @return
     */
    public String submitAudit(String appId, String testVersion) {
//		[{"second_class":"综合生活服务平台","second_id":664,"first_id":150,"first_class":"生活服务"}]
        List<Map<String, Object>> categoryList = getWxaCategory(appId);
        if (categoryList.size() == 0) {
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            logger.info("提交审核失败，可选分类为空，请联系商家为该小程序设置分类！！！！appId：" + appId);
            return null;
        }
        Map<String, Object> categoryMap = categoryList.get(0);//直接取第一个，后期从页面选择
        String first_id = String.valueOf(categoryMap.get("first_id"));
        String first_class = (String) categoryMap.get("first_class");
        String second_id = String.valueOf(categoryMap.get("second_id"));
        String second_class = (String) categoryMap.get("second_class");

        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("submitAudit获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/submit_audit?access_token=" + authorizer_token;

        List<Map<String, Object>> item_list = new ArrayList<Map<String, Object>>();
//		{"errcode":0,"errmsg":"ok","category_list":[{"first_class":"生活服务","second_class":"综合生活服务平台","first_id":150,"second_id":664}]}


        if (testVersion.startsWith(VERSION_PREX_IN_SHOP) || VersionUtil.gt(testVersion, "100.0.0")) {
            logger.info("店中店版本号 testVersion:" + testVersion);
            Map<String, Object> item1 = new HashMap<String, Object>();
            item1.put("address", "page/index/index");
            item1.put("tag", "女装");
            item1.put("first_class", first_class);
            item1.put("second_class", second_class);
            item1.put("first_id", first_id);
            item1.put("second_id", second_id);
            item1.put("title", "首页");
            item_list.add(item1);
        }else {
            Map<String, Object> item1 = new HashMap<String, Object>();
            item1.put("address", "pages/index/index");
            item1.put("tag", "女装");
            item1.put("first_class", first_class);
            item1.put("second_class", second_class);
            item1.put("first_id", first_id);
            item1.put("second_id", second_id);
            item1.put("title", "首页");
            item_list.add(item1);
            Map<String, Object> item2 = new HashMap<String, Object>();
            item2.put("address", "pages/product/product");
            item2.put("tag", "女装");
            item2.put("first_class", first_class);
            item2.put("second_class", second_class);
            item2.put("first_id", first_id);
            item2.put("second_id", second_id);
            item2.put("title", "商城");
            item_list.add(item2);
            Map<String, Object> item3 = new HashMap<String, Object>();
            item3.put("address", "pages/want/want");
            item3.put("tag", "女装");
            item3.put("first_class", first_class);
            item3.put("second_class", second_class);
            item3.put("first_id", first_id);
            item3.put("second_id", second_id);
            item3.put("title", "喜欢");
            item_list.add(item3);
            Map<String, Object> item4 = new HashMap<String, Object>();
            item4.put("address", "pages/my/my");
            item4.put("tag", "女装");
            item4.put("first_class", first_class);
            item4.put("second_class", second_class);
            item4.put("first_id", first_id);
            item4.put("second_id", second_id);
            item4.put("title", "我的");
            item_list.add(item4);
        }


        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("item_list", item_list);
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始提交审核, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("获取提交审核成功, url:{" + url + "}, param:{" + param + "}，retMap：" + retMap.toString());
        return retMap.toString();
    }


    /**
     * 6、获取审核结果（是通过地址回调接收的）
     *
     * 注意：当小程序有审核结果后，第三方平台将可以通过开放平台上填写的回调地址，获得审核结果通知。
     * 除了消息通知之外，第三方平台也可通过接口查询审核状态。
     *
     * 审核通过时，接收到的推送XML数据包示例如下：
     <xml><ToUserName><![CDATA[gh_fb9688c2a4b2]]></ToUserName>
     <FromUserName><![CDATA[od1P50M-fNQI5Gcq-trm4a7apsU8]]></FromUserName>
     <CreateTime>1488856741</CreateTime>
     <MsgType><![CDATA[event]]></MsgType>
     <Event><![CDATA[weapp_audit_success]]></Event>
     <SuccTime>1488856741</SuccTime>
     </xml>
     审核不通过时，接收到的推送XML数据包示例如下：
     <xml><ToUserName><![CDATA[gh_fb9688c2a4b2]]></ToUserName>
     <FromUserName><![CDATA[od1P50M-fNQI5Gcq-trm4a7apsU8]]></FromUserName>
     <CreateTime>1488856591</CreateTime>
     <MsgType><![CDATA[event]]></MsgType>
     <Event><![CDATA[weapp_audit_fail]]></Event>
     <Reason><![CDATA[1:账号信息不符合规范:<br>
     (1):包含色情因素<br>
     2:服务类目"金融业-保险_"与你提交代码审核时设置的功能页面内容不一致:<br>
     (1):功能页面设置的部分标签不属于所选的服务类目范围。<br>
     (2):功能页面设置的部分标签与该页面内容不相关。<br>
     </Reason>
     <FailTime>1488856591</FailTime>
     </xml>
     * @param appId
     * @param storeId
     * @param templateId
     * @return
     */


    /**
     * 7、获取第三方提交的审核版本的审核状态（仅供第三方代小程序调用）
     *
     * @param appId
     * @param auditid 提交审核时获得的审核id
     * @return
     */
    public String getAuditState(String appId, String auditid) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("getAuditState获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/get_auditstatus?access_token=" + authorizer_token;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("auditid", auditid);
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始获取审核状态, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("获取获取审核状态成功, url:{" + url + "}, param:{" + param + "}，retMap：" + retMap.toString());
        return retMap.toString();

//		Response<String> resp = Requests.get(url).params(paramMap).text();
//		String body = resp.getBody();
//    	logger.info("获取审核状态, url:{"+url+"}, param:{"+paramMap.toString()+"}，body："+body);
//    	return body.toString();
    }

    /**
     * 8、查询最新一次提交的审核状态
     *
     * @param appId
     * @return
     */
    public String getLastAuditState(String appId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("getLastAuditState获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/get_latest_auditstatus?access_token=" + authorizer_token;
        Map<String, String> paramMap = new HashMap<String, String>();
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始获取审核状态, url:{" + url + "}, param:{" + param + "}");
        Response<String> resp = Requests.get(url).params(paramMap).text();
        String body = resp.getBody();
//		Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("获取审核状态, url:{" + url + "}, param:{" + param + "}，body：" + body);
        return body;
    }


    /**
     * 8、发布已通过审核的小程序（仅供第三方代小程序调用）
     *
     * @param appId
     * @return
     */
    public String releaseWxa(String appId) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("releaseWxa获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/release?access_token=" + authorizer_token;
        Map<String, String> paramMap = new HashMap<String, String>();
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始发布已通过审核的小程序, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("发布已通过审核的小程序成功, url:{" + url + "}, param:{" + param + "}，retMap：" + retMap.toString());
        return retMap.toString();
    }


    /**
     * 9、修改小程序线上代码的可见状态（仅供第三方代小程序调用）
     *
     * @param appId
     * @return
     */
    public String changeVisitStatus(String appId, int state) {
        String authorizer_token = thirdApi.get_authorizer_token(appId);
        if (StringUtils.isEmpty(authorizer_token)) {
            logger.info("changeVisitStatus获取小程序authorizer_token为空，请排查问题！！！！");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/change_visitstatus?access_token=" + authorizer_token;
        Map<String, String> paramMap = new HashMap<String, String>();
        String stateCode = state == 0 ? "close" : "open";
        paramMap.put("action", stateCode);//设置可访问状态，发布后默认可访问，close为不可见，open为可见
        String param = JSONObject.toJSONString(paramMap);
        logger.info("开始修改小程序线上代码的可见状, url:{" + url + "}, param:{" + param + "}");
        Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
        logger.info("修改小程序线上代码的可见状完成, url:{" + url + "}, param:{" + param + "}，retMap：" + retMap.toString());
        return retMap.toString();
    }


    /**
     * 第三方自定义的配置JSON
     *
     * @return
     */
    private String getAppConfig(String appId, String storeId, String storeName, String version) {
        //扩展字段
        Map<String, String> extMap = new HashMap<String, String>();
        extMap.put("storeId", storeId);
        extMap.put("appId", appId);

//		大于3.3.0版本的页面配置
        String[] pagesArr1 = {
            "pages/index/index",
            "pages/shareEarnMoney/shareEarnMoney",
            "pages/footprint/footprint",
            "pages/product/product",
            "pages/shoppingCart/shoppingCart",
            "pages/want/want",
            "pages/livePlayList/livePlayList",
            "pages/component/livePlay/livePlay",
            "pages/component/liveGoodDetail/liveGoodDetail",
            "pages/component/secondBuyActivity/secondBuyActivity",
            "pages/component/teamBuyActivity/teamBuyActivity",
            "pages/component/articleDetail/articleDetail",
            "pages/component/tagProduct/tagProduct",
            "pages/component/searchList/searchList",
            "pages/component/detail/detail",
            "pages/component/confirmOrder/confirmOrder",
            "pages/component/chooseCoupon/chooseCoupon",
            "pages/component/newAddress/newAddress",
            "pages/component/editAddress/editAddress",
            "pages/component/goodsWay/goodsWay",
            "pages/component/useredit/useredit",
            "pages/component/editname/editname",
            "pages/component/storeInfo/storeInfo",
            "pages/component/couponProduct/couponProduct",
            "pages/component/couponCenter/couponCenter",
            "pages/component/myCoupon/myCoupon",
            "pages/component/myOrder/myOrder",
            "pages/component/express/express",
            "pages/component/orderDetail/orderDetail",
            "pages/component/shareRule/shareRule",
            "pages/component/settlementRule/settlementRule",
            "pages/component/myCoinDetail/myCoinDetail",
            "pages/component/myShare/myShare",
            "pages/component/myCoin/myCoin",
            "pages/component/myTeamBuy/myTeamBuy",
            "pages/component/withdrawRule/withdrawRule",
            "pages/component/withdrawCash/withdrawCash",
            "pages/component/withdrawSuccess/withdrawSuccess",
            "pages/component/distributorAgreement/distributorAgreement",
            "pages/component/partnerAgreement/partnerAgreement",
            "pages/component/applyRights/applyRights",
            "pages/component/legalRight/legalRight",
            "pages/component/accountAmount/accountAmount",
            "pages/component/accountCoin/accountCoin",
            "pages/component/incomeDetail/incomeDetail",
            "pages/component/myFans/myFans",
            "pages/component/withdrawBonus/withdrawBonus",
            "pages/component/withdrawBonusSuccess/withdrawBonusSuccess",
            "pages/component/sign/sign",
            "pages/component/teamOrderList/teamOrderList",
            "pages/component/teamOrderDetail/teamOrderDetail",
            "pages/component/teamOrderSearch/teamOrderSearch",
            "pages/component/refundApply/refundApply",
            "pages/component/refundDetail/refundDetail",
            "pages/component/refundList/refundList",
            "pages/component/refundProgress/refundProgress",
            "pages/component/refundOrder/refundOrder",
            "pages/component/helperCenter/helperCenter",
            "pages/component/helperDetail/helperDetail",
            "pages/my/my"
        };


        //店中店配置
        String[] inShopConfig = {
            "page/index/index",
            "page/component/shopInfo/shopInfo",
            "page/component/login/login",
            "page/component/paySuccess/paySuccess",
            "page/component/buyType/buyType",
            "page/component/pay/pay",
            "page/component/storeInfo/storeInfo",
            "page/component/useredit/useredit",
            "page/component/order/order",
            "page/component/orderDetail/orderDetail",
            "page/component/express/express",
            "page/component/refundList/refundList",
            "page/component/refundApply/refundApply",
            "page/component/editname/editname",
            "page/component/refundDetail/refundDetail",
            "page/component/refundOrder/refundOrder",

        };

//		小于3.3.0版本的页面配置
        String[] pagesArr2 = {"pages/index/index",
            "pages/footprint/footprint",
            "pages/want/want",
            "pages/component/detail/detail",
            "pages/component/confirmOrder/confirmOrder",
            "pages/component/chooseCoupon/chooseCoupon",
            "pages/component/useredit/useredit",
            "pages/component/editname/editname",
            "pages/component/storeInfo/storeInfo",
            "pages/component/couponCenter/couponCenter",
            "pages/component/myCoupon/myCoupon",
            "pages/component/myOrder/myOrder",
            "pages/component/orderDetail/orderDetail",
            "pages/my/my",
        };




        Map<String, Object> tabBarMap = new HashMap<String, Object>();
        tabBarMap.put("color", "#828282");
        tabBarMap.put("selectedColor", "#ff2272");
        tabBarMap.put("borderStyle", "black");



        Map<String, Object> appCofingMap = new HashMap<String, Object>();
        appCofingMap.put("extAppid", appId);//授权方Appid，可填入商户AppID，以区分不同商户
        appCofingMap.put("ext", extMap);//自定义字段仅允许在这里定义，可在小程序中调用
        Map<String, String> extPagesMap = new HashMap<String, String>();
        appCofingMap.put("extPages", extPagesMap);

        if (version.startsWith(VERSION_PREX_IN_SHOP)) {
            version = version.substring(VERSION_PREX_IN_SHOP.length(), version.length());
            logger.info("店中店版本号 version:"+version);
            appCofingMap.put("pages", inShopConfig);
        } else if (VersionUtil.gt(version, "100.0.0")) {
            logger.info("店中店版本号 version:"+version);
            appCofingMap.put("pages", inShopConfig);
        } else {
            logger.info("专享小程序配置");
            if (VersionUtil.lt(version, "3.3.0")) {//小于3.3.0
                logger.info("小于3.3.0==========version:" + version + ",pagesArr2:" + pagesArr2);
                appCofingMap.put("pages", pagesArr2);
            } else {
                logger.info("大于等于3.3.0==========version:" + version + ",pagesArr1:" + pagesArr1);
                appCofingMap.put("pages", pagesArr1);
            }

            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("pagePath", "pages/index/index");
            map1.put("iconPath", "images/icon/home.png");
            map1.put("selectedIconPath", "images/icon/home-active.png");
            map1.put("text", "首页");
            Map<String, String> map2 = new HashMap<String, String>();
            map2.put("pagePath", "pages/product/product");
            map2.put("iconPath", "images/icon/product.png");
            map2.put("selectedIconPath", "images/icon/product-active.png");
            map2.put("text", "商城");
//            Map<String, String> map5 = new HashMap<String, String>();
//            map5.put("pagePath", "pages/shareEarnMoney/shareEarnMoney");
//            map5.put("iconPath", "images/icon/share.png");
//            map5.put("selectedIconPath", "images/icon/share.png");
//            map5.put("text", "分享");
            Map<String, String> map3 = new HashMap<String, String>();
            map3.put("pagePath", "pages/shoppingCart/shoppingCart");
            map3.put("iconPath", "images/icon/cart.png");
            map3.put("selectedIconPath", "images/icon/cart-active.png");
            map3.put("text", "购物车");
            Map<String, String> map4 = new HashMap<String, String>();
            map4.put("pagePath", "pages/my/my");
            map4.put("iconPath", "images/icon/my.png");
            map4.put("selectedIconPath", "images/icon/my-active.png");
            map4.put("text", "我的");
            Map[] listArr = {map1, map2, map3, map4};
            tabBarMap.put("list", listArr);
        }


        Map<String, String> windowMap = new HashMap<String, String>();
        windowMap.put("backgroundTextStyle", "light");
        windowMap.put("navigationBarBackgroundColor", "#fff");
        windowMap.put("navigationBarTitleText", storeName);
        windowMap.put("navigationBarTextStyle", "black");
        appCofingMap.put("window", windowMap);

        Map<String, String> networkTimeoutMap = new HashMap<String, String>();
        networkTimeoutMap.put("request", "10000");
        networkTimeoutMap.put("downloadFile", "10000");
        appCofingMap.put("networkTimeout", networkTimeoutMap);

        appCofingMap.put("tabBar", tabBarMap);
        return JSONObject.toJSONString(appCofingMap);
    }


}
