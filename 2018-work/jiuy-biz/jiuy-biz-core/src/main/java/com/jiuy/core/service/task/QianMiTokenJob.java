package com.jiuy.core.service.task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.util.QianMiUtil;
import com.jiuyuan.constant.GlobalSettingName;

/**
 * 定时刷新千米的token，以防过期
 * 
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Service
public class QianMiTokenJob {
	
	private static final Logger logger = LoggerFactory.getLogger("QIANMI");
 	
	@Autowired
	private GlobalSettingService globalSettingService;
	
    public void execute() {
    	JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.QIANMI_TOKEN);
    	String refreshToken = jsonObject.getString("refreshToken");
    	try {
    		String content = QianMiUtil.refreshToken(refreshToken);
    		String access_token = getMatch(content, "\"access_token\":\"([A-Z a-z 0-9_]*)\"");
    		String refresh_token = getMatch(content, "\"refresh_token\":\"([A-Z a-z 0-9_]*)\"");
    		
    		updateToken(access_token, refresh_token);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("com.jiuy.core.service.task.QianMiTokenJob.execute() ERROR: " + e.getMessage());
		}
    }
    
    private void updateToken(String access_token, String refresh_token) {
		JSONObject jObject = new JSONObject();
		
		jObject.put("accessToken", access_token);
		jObject.put("refreshToken", refresh_token);
		
		globalSettingService.update("qianmi_token", jObject.toJSONString());
	}

	private String getMatch(String content, String regEx) {
    	Pattern pat = Pattern.compile(regEx);
    	Matcher mat = pat.matcher(content);
		while (mat.find()) {
			return mat.group(1);  			
		}
		
		return "";
    }
}
