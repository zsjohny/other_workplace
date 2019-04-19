/**
 * 
 */
package com.store.service;

import java.io.IOException;

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
 * @author Charles 
 *
 */
@Service("haoservice")
public class HaoserviceImpl implements IExpressQuery, Ordered {
    
    @Autowired
    private HttpClientService httpClientService;

    @Override
    public boolean support(String expressSupplier) {
        return true;
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
                builder.add("key", _ACCESS_KEY);
                builder.add("com", expressSupplier);
                builder.add("no", orderNoWithSupplier);
                return httpClientService.get(builder.toUri());
            }
            
            @Override
            public boolean readResponse(String responseText, LogBuilder errorLog) {
                text.append(responseText);
                return true;
            }
        });
        
        return text.toString();
    }

    private final String _REQUEST_ADDRESS = "http://apis.haoservice.com/lifeservice/exp";

//    private final String _ACCESS_KEY = "7d2f67815c284186b7c8146278af8990";
    private final String _ACCESS_KEY = "05e047bc88d746deadbec07b1b39e8c5";
    
    

    @Override
    public int getOrder() {
        return 1;
    }
}
