package com.yujj.business.service.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.service.common.JsonHttpClientService;
import com.jiuyuan.util.http.component.JsonHttpClientQuery;
import com.jiuyuan.util.http.component.JsonHttpResponse;
import com.jiuyuan.util.http.log.LogBuilder;

/**
 * 有邮件正文模板规范限制http://sendcloud.sohu.com/doc/guide/base/#template
 * u : dev@yujiejie.com
 * p : hzjy2015
 */
@Service
public class MailService {
    private static final String SENDCLOUD_URL = "http://sendcloud.sohu.com/webapi/mail.send_template.json";
    
    private static final String SENDCLOUD_APIUSER = "yujiejie";
    
    private static final String SENDCLOUD_APIKEY = "ZsvbGePEXTeCfbeY";
    
    private static final String SENDCLOUD_MAIL_ALIASNAME = "俞姐姐网站";
    
    private static final String SENDCLOUD_MAIL_FROM = "helper@yujiejie.com";
	
	@Autowired
    private JsonHttpClientService jsonHttpClientService;

    public boolean sendMail(final String subject, final JSONObject params, final String toEmail, final String templateName) {
        return jsonHttpClientService.execute(new JsonHttpClientQuery("sendMail") {
            
            @Override
            public void initLog(LogBuilder log) {
                log.append("subject", subject).append("toEmail", toEmail).append("params", params.toJSONString()).append(
                    "templateName", templateName);
            }
            
            @Override
            public JsonHttpResponse sendRequest() throws IOException {
                JSONObject vars = new JSONObject();
                vars.put("sub", params);

                JSONArray to = new JSONArray();
                to.add(toEmail);
                vars.put("to", to);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("api_user", SENDCLOUD_APIUSER));
                params.add(new BasicNameValuePair("api_key", SENDCLOUD_APIKEY));
                params.add(new BasicNameValuePair("substitution_vars", vars.toJSONString()));
                params.add(new BasicNameValuePair("template_invoke_name", templateName));
                params.add(new BasicNameValuePair("from", SENDCLOUD_MAIL_FROM));
                params.add(new BasicNameValuePair("fromname", SENDCLOUD_MAIL_ALIASNAME));
                params.add(new BasicNameValuePair("subject", subject));
                params.add(new BasicNameValuePair("html", ""));
                params.add(new BasicNameValuePair("resp_email_id", "true"));

                return jsonHttpClientService.post(SENDCLOUD_URL, params);
            }

            @Override
            public boolean readResponse(JSONObject jsonObject, LogBuilder errorLog) {
                return jsonObject.containsKey("message") &&
                    StringUtils.equals(jsonObject.getString("message"), "success");
            }

        });
    }

}
