package org.dream.utils.quota.client;

import org.dream.utils.quota.OrderQuotaInfoModel;
import org.dream.utils.quota.handler.QuotaDataHandleService;
import org.dream.utils.quota.handler.route.QuotaDataHandleRouter;
import org.dream.utils.quota.handler.route.RouteQuotaDataHandleProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by yhj on 16/11/11.
 */
public class MutliQuotaClient implements QuotaClient,InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(MutliQuotaClient.class);

    private QuotaDataHandleService quotaDataHandleService;

    private Map<String, QuotaClient> quotaClients;

    private QuotaDataHandleRouter quotaDataHandleRouter;



    @Override
    public void afterPropertiesSet() throws Exception {
        final List<RouteQuotaDataHandleProxy> quotaDataHandleProxies = new ArrayList<>();

        if(quotaClients == null) {
            throw new RuntimeException("发生重大错误,quotaClients配置不能为空!");
        }

        if(quotaDataHandleService == null) {
            throw new RuntimeException("发生重大错误,quotaClients配置不能为空!");
        }

        quotaClients.forEach(new BiConsumer<String, QuotaClient>() {
            @Override
            public void accept(String s, QuotaClient quotaClient) {
                RouteQuotaDataHandleProxy proxy =  createRouteDataHandle(s);
                quotaDataHandleProxies.add(proxy);
                quotaClient.setQuotaDataHandleService(proxy);
            }
        });

        if(quotaDataHandleRouter != null){
            quotaDataHandleRouter.setRouterProxyList(quotaDataHandleProxies);
            quotaDataHandleRouter.init();
        }
    }

    @Override
    public boolean destory() {
        quotaClients.forEach(new BiConsumer<String, QuotaClient>() {
            @Override
            public void accept(String s, QuotaClient quotaClient) {
                quotaClient.destory();
            }
        });
        return true;
    }

    @Override
    public boolean subscribe(final OrderQuotaInfoModel... params) throws Exception {
        quotaClients.forEach(new BiConsumer<String, QuotaClient>() {
            @Override
            public void accept(String s, QuotaClient quotaClient) {
                try {
                    quotaClient.subscribe(params);
                } catch (Exception e) {
                    LOG.error("请阅异常:",e);
                }
            }
        });
        return true;
    }

    @Override
    public boolean unSubscribe(final OrderQuotaInfoModel... params) throws Exception {
        quotaClients.forEach(new BiConsumer<String, QuotaClient>() {
            @Override
            public void accept(String s, QuotaClient quotaClient) {
                try {
                    quotaClient.unSubscribe(params);
                } catch (Exception e) {
                    LOG.error("取消订阅异常",e);
                }
            }
        });
        return true;
    }

    @Override
    public void setQuotaDataHandleService(QuotaDataHandleService quotaDataHandleService) {

        this.quotaDataHandleService = quotaDataHandleService;
    }

    public void setQuotaClients(Map<String, QuotaClient> quotaClients) {
        this.quotaClients = quotaClients;
    }

    public void setQuotaDataHandleRouter(QuotaDataHandleRouter quotaDataHandleRouter) {
        this.quotaDataHandleRouter = quotaDataHandleRouter;
    }





    private RouteQuotaDataHandleProxy createRouteDataHandle(String name) {

        RouteQuotaDataHandleProxy proxy = new RouteQuotaDataHandleProxy();
        proxy.setName(name);
        proxy.setRealQuotaDataHandleService(quotaDataHandleService);

        return proxy;
    }


}
