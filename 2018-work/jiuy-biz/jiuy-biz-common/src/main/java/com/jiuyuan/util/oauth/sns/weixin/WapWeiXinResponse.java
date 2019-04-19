package com.jiuyuan.util.oauth.sns.weixin;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.util.constant.PropertiesLoader;
import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.jiuyuan.util.oauth.sns.common.response.AbstractSnsResponse;
import com.jiuyuan.util.oauth.sns.common.response.SnsResponseType;

public class WapWeiXinResponse<T> extends AbstractSnsResponse<T> {

    private static final Logger log = LoggerFactory.getLogger(WapWeiXinResponse.class);

    private static final Properties messages = loadMessages();

    private static final String ERRCODE = "errcode";

    public WapWeiXinResponse(CachedHttpResponse response) {
        super(response);
    }

    public WapWeiXinResponse(WapWeiXinResponse<?> response, T data) {
        super(response, data);
    }

    @Override
    public SnsResponseType parseResponse() {

        String responseText = this.getResponseText();
        if (StringUtils.isBlank(responseText)) {
            log.error("Response text is blank: {}", this);
            return SnsResponseType.ERROR_UNKNOWN;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(responseText);

            if (!jsonObject.containsKey(ERRCODE)) {//布包好errcode则表示成功
                return SnsResponseType.SUCCESS;
            }

            int errcode = jsonObject.getIntValue(ERRCODE);
            if (errcode == 0) {
                return SnsResponseType.SUCCESS;
            }

            setTextMessage(getMessage(errcode));

            if (errcode == 45009 || errcode == 45011) {
                return SnsResponseType.ERROR_FREQUENCY;
            } else if (errcode == 40014 || errcode == 41001 || errcode == 42001 || errcode == 42002) {
                return SnsResponseType.ERROR_AUTH;
            }

            log.error("Unknown error: " + this + ", errcode: " + errcode + ", message: " + getMessage(errcode));
            return SnsResponseType.ERROR_UNKNOWN;
        } catch (Exception e) {
            log.error("Unable to parse api response: {}", this, e);
            return SnsResponseType.ERROR_UNKNOWN;
        }
    }

    private static Properties loadMessages() {
        Properties properties = new Properties();
        Class<?> clazz = WapWeiXinResponse.class;
        try {
            properties = PropertiesLoader.loadClasspathProperties(clazz, "UTF-8");
        } catch (Exception e) {
            log.error("Failed to load messages for {}", clazz, e);
        }
        return properties;
    }

    private static String getMessage(int errcode) {
        return messages.getProperty(errcode + "");
    }
}
