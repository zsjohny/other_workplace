package com.jiuyuan.service.common;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
//import com.jiuy.util.http.HttpUtil;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.SmsRecordMapper;
import com.jiuyuan.entity.yunxin.UserYxRelation;
import com.jiuyuan.entity.yunxin.YxAudioMessage;
import com.jiuyuan.entity.yunxin.YxCommonMessage;
import com.jiuyuan.entity.yunxin.YxImageMessage;
import com.jiuyuan.entity.yunxin.YxTextMessage;
import com.jiuyuan.entity.yunxin.YxVideoMessage;
import com.jiuyuan.util.http.component.JsonHttpClientQuery;
import com.jiuyuan.util.http.component.JsonHttpResponse;
import com.jiuyuan.util.http.log.LogBuilder;

//import com.yujj.util.http.HttpUtil;

/**
 * 接口文档参考http://dev.netease.im/docs?doc=server&#发送模板短信<br>
 * u: dev@yujiejie.com<br>
 * p: yujiejie2015<br>
 */
@Service
public class YunXinSmsService {
	private static final Logger logger = LoggerFactory.getLogger(YunXinSmsService.class);

	private static final String SMS_URL = "https://api.netease.im/sms/sendtemplate.action";

	private static final String SMS_QUERYSTATUS = "https://api.netease.im/sms/querystatus.action";

	private static final String CHAT_URL = "https://api.netease.im/nimserver/msg/sendMsg.action";

	private static final String SMS_SENDCODE_URL = "https://api.netease.im/sms/sendcode.action";

	private static final String SMS_VERIFYCODE_URL = "https://api.netease.im/sms/verifycode.action";

	private long sendid = 0;;

	StringBuilder retParams = new StringBuilder();

	@Autowired
	private JsonHttpClientService jsonHttpClientService;

	@Autowired
	private MemcachedService memcachedService;

	@Autowired
	private SmsRecordMapper smsRecordMapper;

	/**
	 * 发送短信随机码
	 * 
	 * @param phone
	 * @param randomCode
	 */
	public boolean send(final String phone, final JSONArray params, final int templateId) {
		long time = System.currentTimeMillis();
		int count = smsRecordMapper.getRecordCount(phone, time - DateUtils.MILLIS_PER_DAY);
		if (count >= 9) { // 一天内同一个手机号发送过多会被运营商视为短信轰炸，导致用户接收不了短信
			logger.error("sms send limited, phone:{}, already send count:{}", phone, count);
			return false;
		}

		final String jsonParams = params.toJSONString();

		boolean success = jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin sms") {

			@Override
			public void initLog(LogBuilder log) {
				log.append("phone", phone).append("params", jsonParams);
			}

			@Override
			public JsonHttpResponse sendRequest() throws IOException {
				List<NameValuePair> postparams = new ArrayList<NameValuePair>();
				postparams.add(new BasicNameValuePair("templateid", String.valueOf(templateId)));
				JSONArray mobiles = new JSONArray();
				mobiles.add(phone);
				postparams.add(new BasicNameValuePair("mobiles", mobiles.toJSONString()));
				postparams.add(new BasicNameValuePair("params", jsonParams));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders();

				return jsonHttpClientService.post(SMS_URL, postparams, headers);
			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

		});

		if (success) {
			smsRecordMapper.addSmsRecord(phone, jsonParams, time);

		}

		return success;
	}

	/**
	 * 发送短信随机码 + 获取发送状态
	 * 
	 * @param phone
	 * @param randomCode
	 */
	public boolean send1(final String phone, final JSONArray params, final int templateId) {
		long time = System.currentTimeMillis();
		int count = smsRecordMapper.getRecordCount(phone, time - DateUtils.MILLIS_PER_DAY);
		if (count >= 9) { // 一天内同一个手机号发送过多会被运营商视为短信轰炸，导致用户接收不了短信
			logger.error("sms send1 limited, phone:{}, already send count:{}", phone, count);
			return false;
		}

		final String jsonParams = params.toJSONString();

		boolean success = jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin sms") {

			@Override
			public void initLog(LogBuilder log) {
				log.append("phone", phone).append("params", jsonParams);
			}

			@Override
			public JsonHttpResponse sendRequest() throws IOException {
				List<NameValuePair> postparams = new ArrayList<NameValuePair>();
				postparams.add(new BasicNameValuePair("templateid", String.valueOf(templateId)));
				JSONArray mobiles = new JSONArray();
				mobiles.add(phone);
				postparams.add(new BasicNameValuePair("mobiles", mobiles.toJSONString()));
				postparams.add(new BasicNameValuePair("params", jsonParams));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders();

				return jsonHttpClientService.post(SMS_URL, postparams, headers);
			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				if (jsonObject.containsKey("msg"))
					sendid = jsonObject.getLongValue("msg");

				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

		});

		if (success) {
			success = jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin sms") {

				@Override
				public void initLog(LogBuilder log) {
					log.append("phone", phone).append("params", jsonParams);
				}

				@Override
				public JsonHttpResponse sendRequest() throws IOException {
					List<NameValuePair> postparams = new ArrayList<NameValuePair>();
					postparams.add(new BasicNameValuePair("sendid", String.valueOf(sendid)));
					List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders();

					return jsonHttpClientService.post(SMS_QUERYSTATUS, postparams, headers);
				}

				@Override
				public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
					if (jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200) {
						// obj中返回JSONArray,格式如下(其中status取值:0-未发送,1-发送成功,2-发送失败,3-反垃圾)：
						if (!jsonObject.containsKey("code"))
							return false;

						JSONArray obj = jsonObject.getJSONArray("obj");
						if (obj.size() > 1) {
							int status = obj.getJSONObject(0).getIntValue("status");
							if (status == 1)
								return true;
						}
					}
					return false;
				}

			});
			if (success)
				smsRecordMapper.addSmsRecord(phone, jsonParams, time);
		}

		return success;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param phone
	 * @param randomCode
	 *            app: 1 会员版 2门店 3后台工具
	 */
	public boolean sendCode(final String phone, int app) {

		String groupKey = MemcachedKey.GROUP_KEY_PHONE_YUNXIN_TOO_MUCH;
		String key = phone;
		Object obj = memcachedService.get(groupKey, key);

		if (obj != null) {
			return true;
		}

		long time = System.currentTimeMillis();
		if (app == 1) {

			int count = smsRecordMapper.getRecordCount(phone, time - DateUtils.MILLIS_PER_DAY);
			if (count >= 10) { // 一天内同一个手机号发送过多会被运营商视为短信轰炸，导致用户接收不了短信
				logger.error("sms send limited, phone:{}, already send count:{}", phone, count);
				return false;
			}

		} else if (app == 2) {
			int count = smsRecordMapper.getStoreRecordCount(phone, time - DateUtils.MILLIS_PER_DAY, 0);
			if (count >= 10) { // 一天内同一个手机号发送过多会被运营商视为短信轰炸，导致用户接收不了短信
				logger.error("sms send limited, phone:{}, already send count:{}", phone, count);
				return false;
			}

		}

		boolean success = jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin sms") {

			@Override
			public void initLog(LogBuilder log) {
				log.append("phone", phone);
			}

			@Override
			public JsonHttpResponse sendRequest() throws IOException {
				List<NameValuePair> postparams = new ArrayList<NameValuePair>();
				postparams.add(new BasicNameValuePair("mobile", phone));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders1();

				return jsonHttpClientService.post(SMS_SENDCODE_URL, postparams, headers);
			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				try {
					retParams.delete(0, retParams.length());
					retParams.append("sendid:" + jsonObject.getString("msg"));
					retParams.append("code:" + jsonObject.getString("obj"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

		});

		if (success) {
			if (app == 1) {

				smsRecordMapper.addSmsRecord(phone, retParams.toString(), time);
			} else if (app == 2) {
				smsRecordMapper.addStoreSmsRecord(phone, retParams.toString(), time, 0, 0, 0);

			}
			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, phone);
		}

		return success;
	}

	/**
	 * 校验短信验证码
	 * 
	 * @param phone
	 * @param randomCode
	 */
	public boolean verifyCode(final String phone, final String code) {
		boolean success = jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin sms") {

			@Override
			public void initLog(LogBuilder log) {
				log.append("phone", phone).append("code", code);
			}

			@Override
			public JsonHttpResponse sendRequest() throws IOException {
				List<NameValuePair> postparams = new ArrayList<NameValuePair>();
				postparams.add(new BasicNameValuePair("mobile", phone));
				postparams.add(new BasicNameValuePair("code", code));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders1();

				return jsonHttpClientService.post(SMS_VERIFYCODE_URL, postparams, headers);
			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

		});

		return success;
	}

	public static class CheckSumBuilder {
		// 计算并获取CheckSum
		public static String getCheckSum(String appSecret, String nonce, String curTime) {
			return encode("sha1", appSecret + nonce + curTime);
		}

		// 计算并获取md5值
		public static String getMD5(String requestBody) {
			return encode("md5", requestBody);
		}

		private static String encode(String algorithm, String value) {
			if (value == null) {
				return null;
			}
			try {
				MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
				messageDigest.update(value.getBytes());
				return getFormattedText(messageDigest.digest());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private static String getFormattedText(byte[] bytes) {
			int len = bytes.length;
			StringBuilder buf = new StringBuilder(len * 2);
			for (int j = 0; j < len; j++) {
				buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
				buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
			}
			return buf.toString();
		}

		private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
	}

	/**
	 * 发送文本
	 * 
	 * @param textMessage
	 */
	public Boolean sendText(final YxTextMessage textMessage) {

		return jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin text") {

			@Override
			public void initLog(LogBuilder log) {

			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

			@Override
			public JsonHttpResponse sendRequest() throws Exception {
				List<NameValuePair> postparams = getYunxinCommonParams(textMessage);

				String jsonStr = JSON.toJSONString(textMessage, new SupperFieldFilter());
				postparams.add(new BasicNameValuePair("body", jsonStr));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders();

				return jsonHttpClientService.post(CHAT_URL, postparams, headers);
			}

		});

	}

	/**
	 * 发送图片
	 * 
	 * @param imageMessage
	 */
	public Boolean sendImage(final YxImageMessage imageMessage) {

		return jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin text") {

			@Override
			public void initLog(LogBuilder log) {

			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

			@Override
			public JsonHttpResponse sendRequest() throws Exception {
				List<NameValuePair> postparams = getYunxinCommonParams(imageMessage);

				String jsonStr = JSON.toJSONString(imageMessage, new SupperFieldFilter());
				postparams.add(new BasicNameValuePair("body", jsonStr));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders();

				return jsonHttpClientService.post(CHAT_URL, postparams, headers);
			}

		});
	}

	/**
	 * 发送音频
	 * 
	 * @param audioMessage
	 */
	public Boolean sendAudio(final YxAudioMessage audioMessage) {

		return jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin text") {

			@Override
			public void initLog(LogBuilder log) {

			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

			@Override
			public JsonHttpResponse sendRequest() throws Exception {
				List<NameValuePair> postparams = getYunxinCommonParams(audioMessage);

				String jsonStr = JSON.toJSONString(audioMessage, new SupperFieldFilter());
				postparams.add(new BasicNameValuePair("body", jsonStr));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders();

				return jsonHttpClientService.post(CHAT_URL, postparams, headers);
			}

		});
	}

	/**
	 * 发送视频
	 * 
	 * @param videoMessage
	 */
	public Boolean sendVideo(final YxVideoMessage videoMessage) {

		return jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin text") {

			@Override
			public void initLog(LogBuilder log) {

			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

			@Override
			public JsonHttpResponse sendRequest() throws Exception {
				List<NameValuePair> postparams = getYunxinCommonParams(videoMessage);

				String jsonStr = JSON.toJSONString(videoMessage, new SupperFieldFilter());
				postparams.add(new BasicNameValuePair("body", jsonStr));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders();

				return jsonHttpClientService.post(CHAT_URL, postparams, headers);
			}

		});
	}

	private class SupperFieldFilter implements PropertyFilter {

		@Override
		public boolean apply(Object object, String name, Object value) {
			Class<?> clazz = object.getClass().getSuperclass();

			try {
				clazz.getDeclaredField(name);

			} catch (Exception e) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 提取发送消息时(文本，图片，音频，视频)
	 * 
	 * @param commonMessage
	 * @return
	 */
	private List<NameValuePair> getYunxinCommonParams(YxCommonMessage commonMessage) {
		List<NameValuePair> postparams = new ArrayList<NameValuePair>();

		postparams.add(new BasicNameValuePair("from", commonMessage.getFrom()));
		postparams.add(new BasicNameValuePair("to", commonMessage.getTo()));
		postparams.add(new BasicNameValuePair("ope", commonMessage.getOpe()));
		postparams.add(new BasicNameValuePair("type", commonMessage.getType()));

		return postparams;
	}

	/**
	 * 创建云信用户
	 * 
	 * @param relation
	 * @return
	 */
	public Boolean createYunxinUser(UserYxRelation relation) {

		// 构造请求参数
		List<NameValuePair> postparams = new ArrayList<NameValuePair>();

		postparams.add(new BasicNameValuePair("accid", relation.getUsername()));
		postparams.add(new BasicNameValuePair("name", relation.getNickname()));
		postparams.add(new BasicNameValuePair("icon", relation.getIcon()));
		postparams.add(new BasicNameValuePair("token", relation.getPassword()));

		Boolean success = false;
		try {
			JsonHttpResponse response = jsonHttpClientService.post(
					"https://api.netease.im/nimserver/user/create.action", postparams,
					YunXinSmsHeadersBuild.constructYuxinAuthHeaders());
			success = response.getStatusCode() == 200;
			if (success) {
				JSONObject obj = response.parseJson();

				JSONObject info = obj.getJSONObject("info");

				String username = info.getString("accid");

				String password = info.getString("token");

				String nickname = info.getString("name");
				relation.setUsername(username);
				relation.setPassword(password);
				relation.setNickname(nickname);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * 更新云信帐号信息
	 * 
	 * @param relation
	 */
	public Boolean updateYunxinUserInfo(UserYxRelation relation) {

		// 构造请求参数
		List<NameValuePair> postparams = new ArrayList<NameValuePair>();

		postparams.add(new BasicNameValuePair("accid", relation.getUsername()));
		postparams.add(new BasicNameValuePair("name", relation.getNickname()));
		postparams.add(new BasicNameValuePair("token", relation.getPassword()));

		Boolean success = false;
		try {
			JsonHttpResponse response = jsonHttpClientService.post(
					"https://api.netease.im/nimserver/user/update.action", postparams,
					YunXinSmsHeadersBuild.constructYuxinAuthHeaders());
			success = response.getStatusCode() == 200;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;

	}

	/**
	 * 封锁云信帐号
	 * 
	 * @param relation
	 */
	public Boolean blockYunxinUser(UserYxRelation relation) {

		// 构造请求参数
		List<NameValuePair> postparams = new ArrayList<NameValuePair>();

		postparams.add(new BasicNameValuePair("accid", relation.getUsername()));

		Boolean success = false;
		try {
			JsonHttpResponse response = jsonHttpClientService.post("https://api.netease.im/nimserver/user/block.action",
					postparams, YunXinSmsHeadersBuild.constructYuxinAuthHeaders());
			success = response.getStatusCode() == 200;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;

	}

	/**
	 * 解锁云信帐号
	 * 
	 * @param relation
	 */
	public Boolean unblockYuxinUser(UserYxRelation relation) {

		// 构造请求参数
		List<NameValuePair> postparams = new ArrayList<NameValuePair>();

		postparams.add(new BasicNameValuePair("accid", relation.getUsername()));

		Boolean success = false;
		try {
			JsonHttpResponse response = jsonHttpClientService.post(
					"https://api.netease.im/nimserver/user/unblock.action", postparams,
					YunXinSmsHeadersBuild.constructYuxinAuthHeaders());
			success = response.getStatusCode() == 200;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;

	}

	/**
	 * 刷新token
	 * 
	 * @param relation
	 */
	public Boolean refreshToken(UserYxRelation relation) {

		// 构造请求参数
		List<NameValuePair> postparams = new ArrayList<NameValuePair>();

		postparams.add(new BasicNameValuePair("accid", relation.getUsername()));

		Boolean success = false;
		try {
			JsonHttpResponse response = jsonHttpClientService.post(
					"https://api.netease.im/nimserver/user/refreshToken.action", postparams,
					YunXinSmsHeadersBuild.constructYuxinAuthHeaders());
			success = response.getStatusCode() == 200;
			if (success) {
				JSONObject obj = response.parseJson();

				JSONObject info = obj.getJSONObject("info");

				String password = info.getString("token");

				relation.setPassword(password);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * 发送通知短信
	 * 
	 * @param phone
	 * @param randomCode
	 */
	public boolean sendNotice(final String phone, final JSONArray params, final int templateId) {
		long time = System.currentTimeMillis();
		final String jsonParams = params.toJSONString();

		boolean success = jsonHttpClientService.execute(new JsonHttpClientQuery("yunxin sms") {

			@Override
			public void initLog(LogBuilder log) {
				log.append("phone", phone).append("params", jsonParams);
			}

			@Override
			public JsonHttpResponse sendRequest() throws IOException {
				List<NameValuePair> postparams = new ArrayList<NameValuePair>();
				postparams.add(new BasicNameValuePair("templateid", String.valueOf(templateId)));
				JSONArray mobiles = new JSONArray();
				mobiles.add(phone);
				postparams.add(new BasicNameValuePair("mobiles", mobiles.toJSONString()));
				postparams.add(new BasicNameValuePair("params", jsonParams));

				List<Header> headers = YunXinSmsHeadersBuild.constructYuxinAuthHeaders2();

				return jsonHttpClientService.post(SMS_URL, postparams, headers);
			}

			@Override
			public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
				return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200;
			}

		});

		if (success) {
			smsRecordMapper.addSmsRecord(phone, jsonParams, time);
		}

		return success;
	}
}
