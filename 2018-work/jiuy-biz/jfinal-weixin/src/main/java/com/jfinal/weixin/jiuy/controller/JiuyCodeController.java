

package com.jfinal.weixin.jiuy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.third.api.ThirdApi;
import com.jfinal.third.api.ThirdCodeApi;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;
import com.jfinal.weixin.jiuy.service.ApiManager;
import com.jfinal.weixin.sdk.api.CustomServiceApi;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;

import static com.jfinal.third.util.weixinpay.WapPayHttpUtil.sendHttpSaveImgToAliYun;

/**
 * 代码管理相关接口（提供内部接口，仅供玖远内部系统调用）
 * 文档地址：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1489140610_Uavc4&token=664e1e7e6af108a3005f641d7e8b06458c9a40e9&lang=zh_CN
 */
public class JiuyCodeController extends Controller {

    static Log logger = Log.getLog(JiuyCodeController.class);


    protected ThirdCodeApi thirdCodeApi = Duang.duang(ThirdCodeApi.class);
    protected ThirdApi thirdApi = Duang.duang(ThirdApi.class);


//	public static void main(String[] args) {
////		上传代码
//		String url = "https://weixintest.yujiejie.com/code/uploadCode";
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("appId", "wxc85ba29a5a96637b");
//		map.put("storeId","114" );
//		map.put("templateId", "0");
//	 	Response<String> resp = Requests.get(url).params(map).text();
//	 	String body = resp.getBody();
//	 	logger.info("body:"+body);
////		获取体验二维码
////		提交审核
//	}

    /**
     * 获取微信授权页面
     * http://dev.yujiejie.com/code/getGoToAuthUrl?storeId=164
     */
    public void getGoToAuthUrl() {
//		   String storeId = getPara("storeId");
        String authUrl = thirdApi.buildAuthUrl();
        logger.info("授权页面URL:" + authUrl);
        renderText(authUrl);
        return;
    }

    /**
     * 授权回调路径
     *
     * @return
     */
    public void gotoAuthCallback() {
        String auth_code = getPara("auth_code");
        String expires_in = getPara("expires_in");
        String storeId = getPara("storeId");

        //TODO 暂时放在这里除了，后续移到后台异步通知中进行处理
        String tipMsg = thirdApi.gotoAuthCallback(auth_code, expires_in, storeId);

        renderText(tipMsg);
        return;
    }

    /**
     * 添加小程序域名
     *
     * @author Aison
     * @date 2018/7/26 13:38
     */
    public void addWebview(String appId,String domain) {

        String ret = thirdApi.xbWebViewDomain(appId,domain);
        renderText(ret);
    }

    /**
     * 1、为授权的小程序帐号上传小程序代码
     * https://weixintest.yujiejie.com/jsp/auth.html
     * https://weixintest.yujiejie.com/code/uploadCode?appId=wxc85ba29a5a96637b&storeId=114&templateId=4
     * https://weixintest.yujiejie.com/code/uploadCode?appId=wx0927d4005f4e45df&storeId=114&templateId=4
     */
    public void uploadCode() {
        String appId = getPara("appId");
        String storeId = getPara("storeId");
        String storeName = getPara("storeName");
        String templateId = getPara("templateId");
        String testerId = getPara("testerId");
        String version = getPara("version");
        String desc = getPara("desc");
        String ret = thirdCodeApi.uploadCode(appId, storeId, storeName, templateId, version, desc, testerId);

        //
        try{
            String domain = getPara("domain");
            addWebview(appId,domain);
        }catch (Exception e) {
            e.printStackTrace();
        }

        renderText(ret);
    }

    /**
     * 刷新token
     *
     * @return
     */
    public void refresh_authorizer_token() {
        String appId = getPara("appId");

        //TODO 暂时放在这里除了，后续移到后台异步通知中进行处理
        Map<String, Object> map = thirdApi.refresh_authorizer_token(appId);
        String ret = JSONObject.toJSONString(map);
        renderText(ret);

        return;
    }


    /**
     * 绑定微信用户为小程序体验者
     * https://weixintest.yujiejie.com/code/bind_tester?appId=wxc85ba29a5a96637b&testerId=zhaoxinglin
     */
    public void bind_tester() {
        String appId = getPara("appId");
        String testerId = getPara("testerId");

        String ret = thirdCodeApi.bind_tester(appId, testerId);

        renderText(ret);
        return;
    }


    /**
     * 解除绑定小程序的体验者
     * https://weixintest.yujiejie.com/code/bind_tester?appId=wxc85ba29a5a96637b&testerId=zhaoxinglin
     */
    public void unbind_tester() {
        String appId = getPara("appId");
        String testerId = getPara("testerId");

        String ret = thirdCodeApi.unbind_tester(appId, testerId);

        renderText(ret);
        return;
    }

    /**
     * 2、获取体验小程序的体验二维码
     * https://weixintest.yujiejie.com/code/getWxaQrcodeUrl?appId=wxc85ba29a5a96637b
     */
    public void getWxaQrcodeUrl() {
        String appId = getPara("appId");
        String wxaQrcodeUrl = thirdCodeApi.getWxaQrcodeUrl(appId);
        renderText(wxaQrcodeUrl);
        return;
    }

    /**
     * 获取发布版小程序的二维码
     * https://weixintest.yujiejie.com/code/getOnlineWxaQrcodeUrl?appId=wxc85ba29a5a96637b
     */
    public void getOnlineWxaQrcodeUrl() {
        String appId = getPara("appId");
        String wxaQrcodeUrl = thirdCodeApi.getOnlineWxaQrcodeUrl(appId);
        renderText(wxaQrcodeUrl);
        return;
    }

    /**
     * 获取发布版小程序的二维码
     * https://weixintest.yujiejie.com/code/getProductQrcodeUrl?appId=wxc85ba29a5a96637b
     */
    public void getProductQrcodeUrl() {
//    	logger.info("==========开始将小程序商品二维码图片保存到服务器()");
        String realPath = getRequest().getSession().getServletContext().getRealPath("/");
        logger.info("==========将小程序商品二维码图片保存到服务器()realPath:{" + realPath + "}");
        String wxaQrcodeUrl = "";
        try {

            String appId = getPara("appId");
            String productId = getPara("productId");

            String operType =  getPara ("operType");
            String storeId = getPara ("storeId");
            logger.info ("生成小程序二维码 ===> operType:"+operType+";storeId="+storeId);
            if (operType != null) {
                if ("shareShopQrImg".equals (operType)) {
                    //共享店铺二维码
                    logger.info ("共享店铺二维码");
                    String authorizer_token = thirdApi.get_authorizer_token(appId);
                    if (StringUtils.isEmpty(authorizer_token)) {
                        logger.info("getOnlineWxaQrcodeUrl获取小程序authorizer_token为空，请排查问题！！！！");
                    }
                    String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + authorizer_token;
                    Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("scene", "sceneType=inShareShop&storeId="+storeId);
                    paramMap.put("page", "pages/index/index");
                    paramMap.put("width", "430");
                    String param = JSONObject.toJSONString(paramMap);
                    logger.info("==========getwxacodeurl:{" + url + "}, param:{" + param + "}");
                    //二维码图片二进制
                    wxaQrcodeUrl = sendHttpSaveImgToAliYun( url , "GET" ,  param,realPath);
                    logger.info("==========将共享店铺二维码图片保存到服务器()imgPath:{" + wxaQrcodeUrl + "}");
                    renderText(wxaQrcodeUrl);
                }
                return;
            }


            wxaQrcodeUrl = thirdCodeApi.getProductQrcodeUrl(appId, productId, realPath);
            logger.info("获取发布版小程序的二维码wxaQrcodeUrl：" + wxaQrcodeUrl);
            if (StringUtils.isEmpty(wxaQrcodeUrl)) {
                logger.info("获取发布版小程序的二维码失败！请尽快排查问题！！！！！！！！！！！wxaQrcodeUrl：" + wxaQrcodeUrl);
            }
        } catch (Exception e) {
            logger.info("获取发布版小程序的二维码失败请尽快排查问题！！！！！！！！！！！");
            e.printStackTrace();
        }
//    	logger.info("==========发送小程序商品二维码图片");
        renderText(wxaQrcodeUrl);
        return;
    }

    /**
     * 3、获取授权小程序帐号的可选类目
     */
    public void getWxaCategory() {
        String appId = getPara("appId");

        List<Map<String, Object>> list = thirdCodeApi.getWxaCategory(appId);

        renderText(JSONObject.toJSONString(list));
        return;
    }


    /**
     * 4、获取小程序的第三方提交代码的页面配置（仅供第三方开发者代小程序调用）
     */
    public void getPageConfig() {
        String appId = getPara("appId");

        String ret = thirdCodeApi.getPageConfig(appId);

        renderText(ret);
        return;
    }

    /**
     * 5、将第三方提交的代码包提交审核（仅供第三方开发者代小程序调用）
     */
    public void submitAudit() {
        String appId = getPara("appId");
//    	String storeId = getPara("storeId");
//    	String templateId = getPara("templateId");
        String testVersion = getPara("testVersion");
        System.out.println("testVersion=" + testVersion+";appId="+appId);
        String ret = thirdCodeApi.submitAudit(appId, testVersion);

        renderText(ret);
        return;
    }


    /**
     * 7、获取第三方提交的审核版本的审核状态（仅供第三方代小程序调用）
     */
    public void getAuditState() {
        String appId = getPara("appId");
        String auditid = getPara("auditid");

        String ret = thirdCodeApi.getAuditState(appId, auditid);

        renderText(ret);
        return;
    }

    /**
     * 7、查询最新一次提交的审核状态（仅供第三方代小程序调用）
     */
    public void getLastAuditState() {
        String appId = getPara("appId");

        String ret = thirdCodeApi.getLastAuditState(appId);

        renderText(ret);
        return;
    }


    /**
     * 8、发布已通过审核的小程序（仅供第三方代小程序调用）
     */
    public void releaseWxa() {
        String appId = getPara("appId");

        String ret = thirdCodeApi.releaseWxa(appId);

        renderText(ret);
        return;
    }

    /**
     * 9、修改小程序线上代码的可见状态（仅供第三方代小程序调用）
     */
    public void changeVisitStatus() {
        String appId = getPara("appId");
        int state = getParaToInt("state");

        String ret = thirdCodeApi.changeVisitStatus(appId, state);

        renderText(ret);
        return;
    }


}






