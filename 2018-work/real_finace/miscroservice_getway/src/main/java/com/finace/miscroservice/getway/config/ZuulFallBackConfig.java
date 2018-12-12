package com.finace.miscroservice.getway.config;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * zuul调取服务失败的返回的配置
 */
@Configuration
public class ZuulFallBackConfig implements ZuulFallbackProvider {

    private Log log = Log.getInstance(ZuulFallBackConfig.class);
    @Autowired
    private HttpServletRequest request;

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {

                //获取服务名称
                String serviceName = request.getRequestURI();

                serviceName = serviceName == null || serviceName.length() < 1 ? "" : serviceName.substring(1);

                if (serviceName.contains("/")) {
                    serviceName = serviceName.split("/")[0];
                }
                log.warn("客户端={}获取服务={} 访问方法={} 当前没有可用实例 需要等待重试 如未启动请检查服务={}是否启动" +
                        "", Iptools.gainRealIp(request), serviceName, request.getRequestURI(), serviceName);
                return new ByteArrayInputStream(JSONObject.toJSONString(Response.retry()).getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return httpHeaders;
            }
        };
    }
}
