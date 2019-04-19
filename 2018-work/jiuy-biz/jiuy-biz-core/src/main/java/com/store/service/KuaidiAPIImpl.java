/**
 * 
 */
package com.store.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.Express.ExpressSupport;
import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.jiuyuan.util.http.component.HttpClientQuery;
import com.jiuyuan.util.http.log.LogBuilder;
import com.yujj.util.uri.UriBuilder;

/**
 * @author LWS 
 *
 */
@Service("kuaidiservice")
public class KuaidiAPIImpl implements IExpressQuery, Ordered {
    
    @Autowired
    private HttpClientService httpClientService;

    @Override
    public boolean support(String expressSupplier) {
        return StringUtils.equals(expressSupplier, "YD");
    }

    @Override
    public String queryExpressInfo(final String expressSupplier, final String orderNoWithSupplier) {
        final StringBuilder text = new StringBuilder();
        
        httpClientService.execute(new HttpClientQuery("query Express") {
            
            @Override
            public void initLog(LogBuilder log) {
                log.append("supplier", expressSupplier).append("no", orderNoWithSupplier);
            }
            
            @Override
            public CachedHttpResponse sendRequest() throws IOException {
                UriBuilder builder = new UriBuilder(_REQUEST_ADDRESS);
                builder.add("uuid", _UUID);
                builder.add("key", _ACCESS_KEY);
                builder.add("id", ExpressSupport.parse(expressSupplier).getValidName().toLowerCase());
                builder.add("order", orderNoWithSupplier);
                return httpClientService.get(builder.toUri());
            }
            
            @Override
            public boolean readResponse(String responseText, LogBuilder errorLog) {
                String temp = StringUtils.replace(responseText, "content", "context");
                temp = StringUtils.replace(temp, "name", "company");
                temp = StringUtils.replace(temp, "errcode", "error_code");
                text.append(temp);
                return true;
            }
        });
        
        return text.toString();
    }

    private final String _REQUEST_ADDRESS = "http://www.kuaidiapi.cn/rest/";
    private final String _UUID = "44020";
    private final String _ACCESS_KEY = "61bd793b492a4d3dacce106462525f5a";

    @Override
    public int getOrder() {
        return 2;
    }
}
