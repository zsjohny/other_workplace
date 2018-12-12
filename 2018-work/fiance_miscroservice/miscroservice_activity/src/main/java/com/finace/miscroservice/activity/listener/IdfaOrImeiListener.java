package com.finace.miscroservice.activity.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.dao.UserChannelGeneralizeDao;
import com.finace.miscroservice.activity.po.entity.UserChannelGeneralize;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.HttpUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户初次打开APP
 */
@Component
public class IdfaOrImeiListener extends MqListenerConvert{
    private static Log logger = Log.getInstance(IdfaOrImeiListener.class);
	@Autowired
	private UserChannelGeneralizeDao userChannelGeneralizeDao;
	@Override
	protected void transferTo(String transferData) {
		logger.info("用户初次打开app,接受数据={}", transferData);
		if(StringUtils.isEmpty(transferData) && "0".equals(transferData)) {
			logger.warn("用户初次打开app,接受数据失败", transferData);
			return;
		}

		JSONObject jsonData = JSONObject.parseObject(transferData);
		String os = jsonData.get("os").toString();
		String idfa = null;
		if ("android".equals(os)){
			if (jsonData.get("imei")==null||""==jsonData.get("imei")){
				return;
			}
			 idfa = jsonData.get("imei").toString();
		}
		if ("ios".equals(os)){
			if (jsonData.get("idfa")==null||""==jsonData.get("idfa")){
				return;
			}
			idfa = jsonData.get("idfa").toString();
		}
		if (idfa!=null){
			UserChannelGeneralize userChannelGeneralize = userChannelGeneralizeDao.findChannelGeneralize(idfa,os);
			if (userChannelGeneralize!=null){
				userChannelGeneralize.setActivate(1);
				logger.info("渠道用户 激活 phone = {},Channel = {},Timestamp = {},Idfa = {}",userChannelGeneralize.getPhone(),userChannelGeneralize.getChannel(),userChannelGeneralize.getTimestamp(),userChannelGeneralize.getIdfa());
				userChannelGeneralizeDao.upUserChannelGeneralize(userChannelGeneralize.getIdfa());
				Map<String,String> map = new HashMap<>();
				map.put("idfa",idfa);
				map.put("phone",os);
				map.put("timestamp",userChannelGeneralize.getTimestamp());
				String reqMsg =	HttpUtil.doPost(userChannelGeneralize.getCallback(),map,"UTF-8");
				logger.info("reqMsg 回调参数={}",reqMsg);
			}

		}

		return;
	}

}
