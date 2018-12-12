package org.dream.utils.push;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dream.model.push.PushModel;
import org.dream.model.user.UserModel;
import org.dream.utils.prop.SpringProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.gexin.rp.sdk.template.style.Style1;

public class PushUtils {

	public static int ANDROID_DEVICE = 0;
	public static int IOS_DEVICE = 1;

	private static String appId;
	private static String appkey;
	private static String master;

	private static String Alias;
	private static String host;
	private static String url;

	private static Logger logger = LoggerFactory.getLogger(PushUtils.class);

	static {
		try {
			SpringProperties properties = SpringProperties.getBean(SpringProperties.class);
			if (properties != null) {
				// appId = properties.getProperty("sys.all.gettui.appId");
				// appkey = properties.getProperty("sys.all.gettui.appkey");
				// master = properties.getProperty("sys.all.gettui.master");
				Alias = properties.getProperty("sys.all.gettui.alias");
				host = properties.getProperty("sys.all.gettui.host");
				url = properties.getProperty("sys.all.gettui.url");
			}
		} catch (Exception e) {
			// appId = "gnUGtVngQGA80LcPdfbHj9";
			// appkey = "LwJyVosAAu5WcdtMsWQSx6";
			// master = "XmC7k0Ybsm6COcvk0uyIx8";
			/*
			 * appId = "zDqB744dvm5uKOw5X4LUh5"; appkey
			 * ="RdPe1ldvUp8CD6YvsuI218"; master = "Uiowkjy0o69g8CFz8Aomk1";
			 */
			Alias = "";
			host = "http://sdk.open.api.igexin.com/apiex.htm";
			url = "http://sdk.open.api.igexin.com/serviceex";
		}

	}

	/**
	 * 发送给安桌设备(对单个用户推送消息)
	 *
	 * @param title
	 *            安桌设备的通知栏标题
	 * @param msg
	 *            安桌设备的通知栏信息文本
	 * @param cid
	 *            设备标识
	 * @return
	 */
	private static String SendAndroid(PushModel pushModel, String appId, String appKey, String masterSecret) {
		String cid = pushModel.getDeviceId();
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		try {
			logger.info("安卓推送开始,设备={},内容={}", cid, pushModel.getPushMsg());
			NotificationTemplate template = null;
			try {
				template = notificationTemplateDemo(pushModel, appId, appKey);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			Target target = new Target();
			target.setAppId(appId);
			target.setClientId(cid);
			SingleMessage message = new SingleMessage();
			message.setOffline(true);
			message.setOfflineExpireTime(1000 * 60 * 1);
			message.setData(template);
			try {
				IPushResult ret = push.pushMessageToSingle(message, target);
				logger.info("安卓推送结束,设备={},内容={}", cid, pushModel.getPushMsg());
				return ret.getResponse().toString();
			} catch (RequestException e) {
				String requstId = e.getRequestId();
				IPushResult ret = push.pushMessageToSingle(message, target, requstId);
				logger.info("安卓推送结束,设备={},内容={}", cid, pushModel.getPushMsg());
				return ret.getResponse().toString();
			}
		} catch (Exception ex) {
			logger.warn("发送给安卓出错 设备={}", cid, ex);
			return "";
		}
	}

	private static NotificationTemplate notificationTemplateDemo(PushModel model, String appId, String appKey) {
		String title = model.getPushTopic();
		String content = model.getPushContent();
		NotificationTemplate template = new NotificationTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTransmissionType(2);
		template.setTransmissionContent(model.getPushMsg());
		Style0 style = new Style0();
		style.setLogo("push.png");
		style.setText(content);
		style.setTitle(title);
		template.setStyle(style);
		return template;

	}

	/**
	 * 发送给安桌设备(对多个用户推送消息)
	 * 
	 * @param string
	 *
	 **/
	public static String SendListAndroid(PushModel model, List<UserModel> users, String appId, String appKey,
			String masterSecret) {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		try {
			logger.info("安卓列表推送开始,{}", model);
			TransmissionTemplate template = null;
			try {
				template = TransmissionTemplateDemo(model, appId, appKey);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			ListMessage message = new ListMessage();
			message.setOffline(true);
			message.setOfflineExpireTime(1000 * 60 * 1);
			message.setData(template);

			List<Target> targets = new ArrayList<Target>();
			for (UserModel user : users) {
				Target target = new Target();
				target.setAppId(appId);
				target.setClientId(user.getDeviceId());
				targets.add(target);
			}
			String taskId = push.getContentId(message);
			IPushResult ret = push.pushMessageToList(taskId, targets);
			logger.info("安卓列表推送结束,内容={}", model.getPushMsg());
			return ret.getResponse().toString();
		} catch (Exception ex) {
			logger.warn("发送给安卓出错", ex);
			return "";
		}
	}

	private static TransmissionTemplate TransmissionTemplateDemo(PushModel model, String appId, String appKey)
			throws Exception {
		String title = model.getPushTopic();
		String msg = model.getPushMsg();
		String content = model.getPushContent();
		Date createTime = model.getCreateTime();
		Integer pushType = model.getPushType();
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTransmissionType(2);
		Map<String, Object> map = new HashMap<>();
		map.put("pushMsg", msg);
		map.put("pushContent", content);
		map.put("pushTopic", title);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(createTime);
		map.put("createTime", str);
		map.put("pushType", pushType);
		map.put("pushSendType", model.getPushSendType());
		template.setTransmissionContent(JSON.toJSONString(map));

		// 控制显示时间
		logger.info("测试延迟发送时间pushType ={},pushSendType={},start={},end={}", pushType, model.getPushSendType(),
				model.getTimeStart(), model.getTimeEnd());
		String timeStart = model.getTimeStart();
		String timeEnd = model.getTimeEnd();

		if (model.getPushSendType() == 1 || model.getPushSendType() == 2) {
			template.setDuration(splitStr(timeStart), splitStr(timeEnd));
		}
		return template;
	}

	private static String splitStr(String str) {
		String s[] = str.split("\\.");
		return s[0];
	}

	/**
	 * 发送给苹果设备(对单个用户推送消息 )
	 * 
	 * @param pushModel
	 *            推送
	 * @param appId
	 * @param appKey
	 * @param masterSecret
	 * @return
	 */
	private static String SendIos(PushModel pushModel, String appId, String appKey, String masterSecret) {
		String cid = pushModel.getDeviceId();
		String msg = pushModel.getPushMsg();
		Boolean isOpen = true;
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		try {
			logger.info("IOS推送开始,设备={},内容={}", cid, msg);
			TransmissionTemplate template = null;
			try {
				template = getTemplateToSingle(pushModel, isOpen, appId, appKey);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			SingleMessage message = new SingleMessage();
			message.setOffline(true);
			message.setOfflineExpireTime(1000 * 60 * 1);
			message.setData(template);

			Target target = new Target();
			target.setAppId(appId);
			target.setClientId(cid);
			try {
				IPushResult ret = push.pushMessageToSingle(message, target);
				logger.info("IOS推送结束,设备={},内容={}", cid, msg);
				return ret.getResponse().toString();
			} catch (RequestException e) {
				String requstId = e.getRequestId();
				IPushResult ret = push.pushMessageToSingle(message, target, requstId);
				logger.info("IOS推送结束,设备={},内容={}", cid, msg);
				return ret.getResponse().toString();
			}
		} catch (Exception ex) {
			logger.warn("发送给IOS出错 设备={}", cid, ex);
			return "";
		}
		
	}

	private static TransmissionTemplate getTemplateToSingle(PushModel model, Boolean isOpen, String appId,
			String appKey) throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		String title = model.getPushTopic();
		String msg = model.getPushMsg();
		String content = model.getPushContent();
		Integer pushType = model.getPushType();
		Date createTime = model.getCreateTime();
		Map<String, Object> map = new HashMap<>();
		map.put("pushMsg", msg);
		map.put("pushContent", content);
		map.put("pushTopic", title);
		map.put("pushType", pushType);
		map.put("pushSendType", model.getPushSendType());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(createTime);
		map.put("createTime", str);

		template.setTransmissionType(2);
		template.setTransmissionContent(JSON.toJSONString(map));
		APNPayload payload = new APNPayload();
		payload.setBadge(1);
		payload.setContentAvailable(1);
		payload.setSound("default");
		payload.addCustomMsg("pushMsg", msg);
		payload.addCustomMsg("pushContent", content);
		payload.addCustomMsg("pushTopic", title);
		payload.addCustomMsg("pushType", pushType);
		payload.addCustomMsg("pushSendType", model.getPushSendType());
		payload.addCustomMsg("createTime", str);
		// payload.setCategory("$由客户端定义")
		// 字典模式使用下者
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		alertMsg.setBody(content);
		alertMsg.setTitle(title);
		payload.setAlertMsg(alertMsg);
		template.setAPNInfo(payload);
		return template;
	}

	/**
	 * 发送给苹果设备(对多个用户推送消息 )
	 *
	 */
	public static String SendListIos(PushModel model, List<UserModel> users, String appId, String appKey,
			String masterSecret) {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		String msg = model.getPushMsg();
		Boolean isOpen = true;
		try {
			logger.info("IOS列表推送开始,内容={}", msg);
			TransmissionTemplate template = null;
			try {
				template = getTemplate(model, isOpen, appId, appKey);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			ListMessage message = new ListMessage();
			message.setOffline(true);
			message.setOfflineExpireTime(1000 * 60 * 1);
			message.setData(template);

			List<Target> targets = new ArrayList<Target>();
			for (UserModel user : users) {
				Target target = new Target();
				target.setAppId(appId);
				target.setClientId(user.getDeviceId());
				targets.add(target);
			}

			String taskId = push.getContentId(message);
			IPushResult ret = push.pushMessageToList(taskId, targets);
			logger.info("IOS列表推送结束,设备={},内容={}", targets, msg);
			return ret.getResponse().toString();
		} catch (Exception ex) {
			logger.warn("发送给IOS出错 ", ex);
			return "";
		}
	}

	private static TransmissionTemplate getTemplate(PushModel model, Boolean isOpen, String appId, String appKey)
			throws Exception {
		String title = model.getPushTopic();
		String msg = model.getPushMsg();
		String content = model.getPushContent();
		Date createTime = model.getCreateTime();
		Integer pushType = model.getPushType();
		Map<String, Object> map = new HashMap<>();
		map.put("pushMsg", msg);
		map.put("pushContent", content);
		map.put("pushTopic", title);
		map.put("pushType", pushType);
		map.put("pushSendType", model.getPushSendType());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(createTime);
		map.put("createTime", str);
		
		
		String timeStart = splitStr(model.getTimeStart());
		String timeEnd = splitStr(model.getTimeEnd());
		map.put("timeStart", timeStart);
		map.put("timeEnd", timeEnd);

		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTransmissionType(2);
		template.setTransmissionContent(JSON.toJSONString(map));
		
		APNPayload payload = new APNPayload();
		payload.setBadge(1);
		payload.setContentAvailable(1);
		payload.setSound("default");
		payload.addCustomMsg("pushMsg", msg);
		payload.addCustomMsg("pushContent", content);
		payload.addCustomMsg("pushTopic", title);
		payload.addCustomMsg("pushType", pushType);
		payload.addCustomMsg("pushSendType", model.getPushSendType());
		payload.addCustomMsg("createTime", str);
		payload.addCustomMsg("timeStart", timeStart);
		payload.addCustomMsg("timeEnd", timeEnd);
		
		// payload.setCategory("$由客户端定义")
		// 字典模式使用下者
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		alertMsg.setBody(content);
		alertMsg.setTitle(title);
		payload.setAlertMsg(alertMsg);
		template.setAPNInfo(payload);

		return template;
	}

	/**
	 * 发送给苹果锁屏信息
	 *
	 * @param msg
	 *            锁屏信息提示文本(应用内 客户看不到)
	 * @param lockTips
	 *            锁屏信息提示标题
	 * @param lockTitle
	 *            锁屏信息提示内容
	 * @param devicen_token
	 *            设备标别
	 * @return
	 */
	private static String IosApns(String msg, String lockTips, String lockTitle, String devicen_token) {
		try {
			IGtPush push = new IGtPush(url, appkey, master);
			APNTemplate t = new APNTemplate();
			APNPayload apnpayload = new APNPayload();
			apnpayload.setSound("");
			APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
			alertMsg.setTitle(msg);
			alertMsg.setBody(msg);
			alertMsg.setTitleLocKey(lockTitle);
			alertMsg.setActionLocKey(lockTips);
			// 设置铃声
			/*
			 * apnpayload.setSound("ringingWithCallComing.aif");
			 * apnpayload.setBadge(1);
			 */
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

	/**
	 * 个体发送Push
	 * 
	 * @date 2016年9月30日
	 */
	public static Boolean sendPush(PushModel pushModel, String appId, String appKey, String masterSecret) {
		Boolean flag = false;
		/**
		 * 发送push
		 */
		if (pushModel.getDeviceType() == PushUtils.ANDROID_DEVICE) {
			try {
				PushUtils.SendAndroid(pushModel, appId, appKey, masterSecret);
				flag = true;
			} catch (Exception ex) {
				logger.warn("执行安卓推送出错，渠道Id{},推送{}", pushModel.getChannelId(), pushModel.getPushMsg());
			}
		} else if (pushModel.getDeviceType() == PushUtils.IOS_DEVICE) {
			try {
				PushUtils.SendIos(pushModel, appId, appKey, masterSecret);
				flag = true;
			} catch (Exception ex) {
				logger.warn("执行IOS推送出错，渠道Id{},推送{}", pushModel.getChannelId(), pushModel.getPushMsg());
			}
		}
		return flag;
	}

}