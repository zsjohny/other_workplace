package com.yujj.business.service.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.dao.mapper.supplier.SmsRecordMapper;
import com.jiuyuan.service.common.JsonHttpClientService;
import com.jiuyuan.util.http.component.JsonHttpClientQuery;
import com.jiuyuan.util.http.component.JsonHttpResponse;
import com.jiuyuan.util.http.log.LogBuilder;

/**
 * 有短信正文模板规范限制https://www.yunpian.com/api/howto.html
 * u: help@yujiejie.com
 * p: lws2015
 */
@Service
public class SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private static final String SMS_URL = "https://yunpian.com/v1/sms/send.json";

    private static final String API_KEY = "7fd79ce4913b516464ceafb64b3e3a52";

    @Autowired
    private JsonHttpClientService jsonHttpClientService;
    
    @Autowired
    private SmsRecordMapper smsRecordMapper;

	/**
	 * 发送短信随机码
	 * 
	 * @param phone
	 * @param randomCode
	 * app:
	 */
    public boolean send(final String phone, final String content, int app) {
        long time = System.currentTimeMillis();
        int count = smsRecordMapper.getRecordCount(phone, time - DateUtils.MILLIS_PER_DAY);
        if (count >= 9) { // 一天内同一个手机号发送过多会被运营商视为短信轰炸，导致用户接收不了短信
            logger.error("sms send limited, phone:{}, already send count:{}", phone, count);
            return false;
        }

        boolean success = jsonHttpClientService.execute(new JsonHttpClientQuery("sendSMS") {

            @Override
            public void initLog(LogBuilder log) {
                log.append("phone", phone).append("content", content);
            }

            @Override
            public JsonHttpResponse sendRequest() throws IOException {
                List<NameValuePair> postparams = new ArrayList<NameValuePair>();
                postparams.add(new BasicNameValuePair("apikey", API_KEY));
                postparams.add(new BasicNameValuePair("mobile", phone));
                postparams.add(new BasicNameValuePair("text", content));
                return jsonHttpClientService.post(SMS_URL, postparams);
            }

            @Override
            public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
                return jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 0;
            }

        });

        if (success) {
        	if(app == 1 ){
        		
        		smsRecordMapper.addSmsRecord(phone, content, time);
        	}
        	else if(app == 2 ){
        		smsRecordMapper.addStoreSmsRecord(phone, content, time,0,0,0);
        		
        	}
        }

        return success;
	}

}
