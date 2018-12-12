package com.finace.miscroservice.activity.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.po.UserChannelPO;
import com.finace.miscroservice.activity.po.UserHeadlineChannelPO;
import com.finace.miscroservice.activity.service.UserChannelService;
import com.finace.miscroservice.activity.service.UserHeadlineChannelService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Headline.HeadTools;
import com.finace.miscroservice.commons.utils.JwtToken;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.HttpUtil;
import com.finace.miscroservice.commons.utils.tools.MD5;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import it.sauronsoftware.base64.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * app端设备识别号接受
 */
@Component
public class DeviceImeiListener extends MqListenerConvert{
    private static Log logger = Log.getInstance(DeviceImeiListener.class);

    @Autowired
    private UserHeadlineChannelService userHeadlineChannelService;

	@Autowired
	private UserChannelService userChannelService;
    
	@Override
	protected void transferTo(String transferData) {
		logger.info("接受设备识别号{}", transferData);
		if(StringUtils.isEmpty(transferData)) {
			logger.warn("接受设备识别号{}", transferData);
			return;
		}

		JSONObject androidIos = JSONObject.parseObject(transferData);

		String imei = "";
		String os = "";
		String androidid = "";
		String mac = "";


		String uid = androidIos.getString(JwtToken.UID);
		String channel = androidIos.getString("channel") != null ? androidIos.getString("channel") : "";
		if("android".equals(androidIos.getString("os"))){
			imei = androidIos.getString("imei");
			if( null == imei ){
				return;
			}
			imei = MD5.getInstance().getMD5ofStr(imei);
			os = "android";
			androidid = androidIos.getString("androidid");
			mac = androidIos.getString("mac");
		}else if("ios".equals(androidIos.getString("os"))){
			imei = androidIos.getString("idfa");
			if( null == imei ){
				return;
			}
			os = "ios";
		}

		try {
			logger.info("保存设备识别号{}", transferData);
			UserChannelPO uc = new UserChannelPO();
			uc.setAddtime(DateUtils.getNowTimeStr());
			uc.setUser_id(imei);
			uc.setPhone(os);
			uc.setChannel(channel);
			uc.setRemark(uid);

			/********今日头条推广开始********/
			UserHeadlineChannelPO userHeadlineChannel = userHeadlineChannelService.getByImei(imei, "0");
			if( null != userHeadlineChannel){
				Map<String, String> toMap = new HashMap<>();
				String url = "http://ad.toutiao.com/track/activate/?callback=?"+userHeadlineChannel.getCallback()+""
						+ "&muid="+imei+"&source=td&os=0&event_type="+0+"&conv_time="+userHeadlineChannel.getTimestamp();
				String signature = HeadTools.getHmacSHA1(url, HeadTools.HEAD_KEY);
				signature = Base64.encode(signature);
				url = url+"&signature="+signature;
				String teString = HttpUtil.doGet(url, "UTF-8");
				JSONObject jObject = JSONObject.parseObject(teString);
				String msString = jObject.getString("msg");
				if( "success".equals(msString)){
					logger.info("头条激活成功，imei={}, url={}", imei, url);
					//userHeadlineChannelService.updateStatusByImei(imei, "1");
				}

				if(userHeadlineChannel.getConvertId() != null){
					uc.setChannel(userHeadlineChannel.getConvertId());
				}
			}
			/********今日头条推广开始********/

			userChannelService.addUserChannel(uc);


		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

}
