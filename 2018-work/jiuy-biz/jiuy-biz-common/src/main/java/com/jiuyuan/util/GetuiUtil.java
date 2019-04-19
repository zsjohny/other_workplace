package com.jiuyuan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
//import com.jiuy.core.service.task.CouponNotificationJob;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.LinkType;
import com.jiuyuan.constant.ThirdPartService;

public class GetuiUtil {

    private static final Logger logger = Logger.getLogger(GetuiUtil.class);
//	private final static String appId = "fQmAeKJfJG7d1o0sR1Zdi4";
//	
//	private final static String appKey = "7izZKABcuy6WS5PR4a7DB1";
//	
//	private final static String masterSecret = "DtslzkyPjK81fqr4DXylK1";
//	
//	private final static String host = ThirdPartService.GETUI_HOST;
//	
//	@SuppressWarnings("unused")
//	private final static String appSecret = "QxiUFlkufe5EKin8OpGtq1";
	
	private final static String appId = ThirdPartService.GETUI_APP_ID;
	
	private final static String appKey = ThirdPartService.GETUI_APP_KEY;
	
	private final static String masterSecret = ThirdPartService.GETUI_MASTER_SECRET;
	
	private final static String host = ThirdPartService.GETUI_HOST;
	
	@SuppressWarnings("unused")
	private final static String appSecret = ThirdPartService.GETUI_APP_SECRET;
	
    public static void main(String[] args) throws Exception {
		JSONObject jsonObject = new JSONObject();
	    jsonObject.put("title", "这个是标题");
	    jsonObject.put("abstracts", "这个是描述");
	    jsonObject.put("linkUrl", "这个是url");
	    jsonObject.put("image", "这个是图片地址");
	    jsonObject.put("type", 5);
	    jsonObject.put("pushTime", System.currentTimeMillis());
	    List<String> cids = new ArrayList<String>();
	    cids.add("9dee8245259f323532278ded37c57750");
	    cids.add("65a3e35bd248c88dec069c676c652eec");
	    pushGeTui(cids, jsonObject);
    }
    
   
    
    /**
     * 特定cid用户推送
     * @param cid 设备号
     * @param title 标题
     * @param abstracts
     * @param linkUrl
     * @param image
     * @param type
     * @param pushTime
     * @return
     * @throws Exception
     */
    public static boolean pushGeTui(String cid, String title,String abstracts,String linkUrl,String image,String type ,String pushTime) throws Exception {
	    return pushGeTui(CollectionUtil.createList(cid), title, abstracts, linkUrl, image, type , pushTime);
    }
    
    /**
     * 特定cids用户推送  {result=NoValidPush, details={0df40bb9a7193670a3f928df8655c3e2=AppidError}}
     */
    public static boolean pushGeTui(List<String> cids, String title,String abstracts,String linkUrl,String image,String type ,String pushTime) throws Exception {
    	JSONObject jsonObject = new JSONObject();
	    jsonObject.put("title", title);
	    jsonObject.put("abstracts", abstracts);
	    jsonObject.put("linkUrl", linkUrl);
	    jsonObject.put("image", image);
	    jsonObject.put("type", type);
	    jsonObject.put("pushTime", pushTime);
	    logger.info("开始推送，cids:"+cids.toString()+",jsonObject：" + jsonObject.toJSONString());
	    String result = pushGeTui(cids, jsonObject);
	    if (StringUtils.contains(result, "{result=ok,")) {
	    	logger.info("推送成功。result：" + result);
			return true;
		} else {
			logger.error("推送失败。result：" + result);
        	return false;
		}
    }
    
    /**
     * 所有用户推送
     */
    public static boolean pushGeTui( String title,String abstracts,String linkUrl,String image,String type ,String pushTime) throws Exception {
    	JSONObject jsonObject = new JSONObject();
	    jsonObject.put("title", title);
	    jsonObject.put("abstracts", abstracts);
	    jsonObject.put("linkUrl", linkUrl);
	    jsonObject.put("image", image);
	    jsonObject.put("type", type);
	    jsonObject.put("pushTime", pushTime);
	    String result = pushGeTui(null, jsonObject);
	    if (StringUtils.contains(result, "{result=ok,")) {
			return true;
		} else {
			logger.error("推送失败。result：" + result);
        	return false;
		}
    }
    
    /**
     * 特定cids用户推送
     */
    public static String pushGeTui(List<String> cids, JSONObject jsonObject) throws Exception {
        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        // 通知透传模板
        TransmissionTemplate template = getTemplate(jsonObject);
        if(cids == null) {
        	//广播
        	AppMessage message = new AppMessage();
            message.setData(template);

            message.setOffline(true);
            //离线有效时间，单位为毫秒，可选
            message.setOfflineExpireTime(24 * 1000 * 3600);
            //推送给App的目标用户需要满足的条件
            AppConditions cdt = new AppConditions(); 
            List<String> appIdList = new ArrayList<String>();
            appIdList.add(appId);
            logger.info("appId:"+appId);
            message.setAppIdList(appIdList);
            message.setConditions(cdt); 

            IPushResult ret = push.pushMessageToApp(message,"任务别名_toApp");
//            System.out.println(ret.getResponse().toString());
            return ret.getResponse().toString();
        } else {
        	//特定cid
        	ListMessage message = new ListMessage();
        	message.setData(template);
        	// 设置消息离线，并设置离线时间
        	message.setOffline(true);
        	// 离线有效时间，单位为毫秒，可选
        	message.setOfflineExpireTime(24 * 1000 * 3600);
        	// 配置推送目标
        	List<Target> targets = new ArrayList<Target>();
        	for(String cid : cids) {
        		Target target = new Target();
        		target.setAppId(appId);
        		target.setClientId(cid);
        		targets.add(target);
        	}
        	 logger.info("appId:"+appId);
        	// taskId用于在推送时去查找对应的message
        	String taskId = push.getContentId(message);
        	IPushResult ret = push.pushMessageToList(taskId, targets);
//        	System.out.println(ret.getResponse().toString());
        	return ret.getResponse().toString();
        }
    }
    

    private static TransmissionTemplate getTemplate(JSONObject jsonObject) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(JSONObject.toJSONString(jsonObject));
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        payload.setBadge(1);
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("$由客户端定义");
        payload.addCustomMsg("content", JSONObject.toJSONString(jsonObject));
        //简单模式APNPayload.SimpleMsg 
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(jsonObject.get("title").toString()));
        //字典模式使用下者
        //payload.setAlertMsg(getDictionaryAlertMsg());
        template.setAPNInfo(payload);
        return template;
    }
    
    @SuppressWarnings("unused")
	private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(){
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody("body");
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("LocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        // IOS8.2以上版本支持
        alertMsg.setTitle("Title");
        alertMsg.setTitleLocKey("TitleLocKey");
        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }


	/**
	 * 获取透传模板
	 * @param
	 * @date:   2018/5/15 17:25
	 * @author: Aison
	 */
	private static TransmissionTemplate transmissionTemplateDemo(String data) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		template.setTransmissionType(1);
		template.setTransmissionContent(data);
		return template;
	}

	/**
	 * 透传函数
	 * @param cids 需要透传的cids
	 * @param data 需要透传的数据
	 * @date:   2018/5/15 17:27
	 * @author: Aison
	 */
	public static String touchuan(Set<String> cids, String data) {

		try{
			TransmissionTemplate template =  transmissionTemplateDemo(data);
			List<String> appIds = new ArrayList<String>();
			appIds.add(appId);
			ListMessage message = new ListMessage();
			message.setData(template);
			// 设置消息离线，并设置离线时间
			message.setOffline(true);
			// 离线有效时间，单位为毫秒，可选
			message.setOfflineExpireTime(24 * 1000 * 3600);
			IGtPush push = new IGtPush(host, appKey, masterSecret);

			List<Target> targets = new ArrayList<>();
			for(String cid : cids) {
				Target target = new Target();
				target.setAppId(appId);
				target.setClientId(cid);
				targets.add(target);
			}
			// taskId用于在推送时去查找对应的message
			String taskId = push.getContentId(message);
			IPushResult ret = push.pushMessageToList(taskId, targets);
			String result = ret.getResponse().toString();
			if (StringUtils.contains(result, "{result=ok,")) {
				logger.info("推送成功。result：" + result);
				return "success";
			} else {
				logger.error("推送失败。result：" + result);
				return result;
			}
		}catch (Exception e) {
		    return BizUtil.getFullException(e);
		}
	}

}
