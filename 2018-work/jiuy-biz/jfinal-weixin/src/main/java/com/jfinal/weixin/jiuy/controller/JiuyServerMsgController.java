

package com.jfinal.weixin.jiuy.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.third.api.ThirdCustomServiceApi;
import com.jfinal.third.api.WxaMaterialApi;
import com.jfinal.weixin.jiuy.util.EncodeUtil;
//import com.jfinal.weixin.jiuy.EncodeUtil;
import com.jfinal.weixin.jiuy.util.JYFileUtil;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.xiaoleilu.hutool.date.DateUtil;

/**
 * 客服消息相关接口（提供内部接口，仅供玖远内部系统调用）
 */
public class JiuyServerMsgController  extends Controller {

    static Log logger = Log.getLog(JiuyServerMsgController.class);
    protected static WxaMaterialApi wxaMaterialApi = Duang.duang(WxaMaterialApi.class);
    /**
     * 发送文本客服消息
     * http://dev.yujiejie.com/servermsg/sendText?content=111222222222&toUserName=oPsob0cG0vfetPNi_QbrqszuMEjg
     * https://weixinzxl.yujiejie.com/servermsg/sendText?content=111222222222&toUserName=oPsob0cG0vfetPNi_QbrqszuMEjg&appId=wxc85ba29a5a96637b
     */
    public void sendText() {
    	String appId = getPara("appId");
    	String content = getPara("content");
    	String toUserName = getPara("toUserName");
    	logger.info("发送文本客服消息,appId:"+appId+",content:"+content+",toUserName:"+toUserName);
         // 测试发送纯文本：pass
    	ApiResult apiResult = ThirdCustomServiceApi.sendText(toUserName, content,appId);
    	logger.info("发送文本客服消息完成，ret："+JSONObject.toJSONString(apiResult));
        renderText(apiResult.getJson());
        return;
        
    }
    
    /**
     * 发送图片客服消息
     * https://weixinzxl.yujiejie.com/servermsg/sendImage?content=111222222222&toUserName=oPsob0cG0vfetPNi_QbrqszuMEjg&appId=wxc85ba29a5a96637b
     * https://weixinzxl.yujiejie.com/servermsg/sendImage?content=111222222222&toUserName=oPsob0cG0vfetPNi_QbrqszuMEjg&appId=wxc85ba29a5a96637b&imgUrl=http://wx.qlogo.cn/mmopen/NB8ZmN2uvfxwQqaqOVIVYQo3LcIqYbfnEAhz1siaIGbF2HVTWavxvPkTbHwiae7NW7gCYJnNTCVKgS1VWeNj9u5uldB3c9eRCZ/0
     */
    public void sendImage() {
    	String fileDir=System.getProperty("java.io.tmpdir")+"/";
    	String appId = getPara("appId");
    	String imgUrl = getPara("imgUrl");
    	logger.info("URL编码前imgUrl："+imgUrl);
    	imgUrl = EncodeUtil.decodeURL(imgUrl);
    	logger.info("URL编码后imgUrl："+imgUrl);
    	String toUserName = getPara("toUserName");
    		
    	logger.info("发送文本客服消息,appId:"+appId+",imgUrl:"+imgUrl+",toUserName:"+toUserName);
//    	imgUrl = "http://wx.qlogo.cn/mmopen/NB8ZmN2uvfxwQqaqOVIVYQo3LcIqYbfnEAhz1siaIGbF2HVTWavxvPkTbHwiae7NW7gCYJnNTCVKgS1VWeNj9u5uldB3c9eRCZ/0";
    	HttpServletRequest request = getRequest();
//    	String fileDir = request.getSession().getServletContext().getRealPath("");
    	String fileName = DateUtil.current(true)+".jpg";
    	try {
			JYFileUtil.downLoadFromUrl(imgUrl,fileName ,fileDir);
		} catch (IOException e1) {
			 logger.info("发送文本客服消息,appId:"+appId+",imgUrl:"+imgUrl+",toUserName:"+toUserName);
			e1.printStackTrace();
		}  
    	
    	File file = new File(fileDir+fileName);
//    	File file = new File(fileDir+fileName);
//    	logger.info("发送文本客服消息,file:"+file.getPath());
    	ApiResult uploadImageApiResult = wxaMaterialApi.uploadImageMedia(file, appId);
    	file.delete();//上传微信服务完成后进行删除
    	String media_id = uploadImageApiResult.get("media_id");
    	ApiResult apiResult = ThirdCustomServiceApi.sendImage(toUserName, media_id,appId);
    	//{"errcode":45015,"errmsg":"response out of time limit or subscription is canceled hint: [M18c60466ge21]"}
//    	String errcode = apiResult.get("errcode");
        logger.info("发送图片客服消息完成，ret："+JSONObject.toJSONString(apiResult));
        renderText(apiResult.getJson());
        return;
    }
    
}






