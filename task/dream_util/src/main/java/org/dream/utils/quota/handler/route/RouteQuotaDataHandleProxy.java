package org.dream.utils.quota.handler.route;

import org.dream.model.quota.Quota;
import org.dream.utils.log.ProxyLog;
import org.dream.utils.quota.handler.QuotaDataHandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yhj on 16/11/11.
 */
public class RouteQuotaDataHandleProxy implements QuotaDataHandleService {


    private String name;

    private QuotaDataHandleService realQuotaDataHandleService;

    private boolean defaultProxy ;


    private Map<String, RouterData> routerDataMap = new ConcurrentHashMap<>();

    private RouterData clientData = new RouterData(true); //默认client都是打开的


    @Override
    public void quotaHandle(Quota quota) {

        if (isOpen(quota.getInstrumentId())) {
            ProxyLog.log(name,quota);
            realQuotaDataHandleService.quotaHandle(quota);
        }
        flushTimeCache(quota);
    }

    public Map<String, RouterData> getRouterDataMap() {
        return routerDataMap;
    }

    public void flushTimeCache(Quota quota) {

        clientData.flushLastTime();
        getRouterData(quota.getInstrumentId()).flushUpTime(quota.getUpTime());
    }


    public RouterData getRouterData(String key) {
        if (!routerDataMap.containsKey(key)) {
            routerDataMap.putIfAbsent(key,  new RouterData(defaultProxy));
            //默认设置所有品种为默认数据源
            RouterData.setCurrProxyMap(key,QuotaDataHandleRouter.getDefaultProxy());
        }
        return routerDataMap.get(key);
    }

    public boolean isOpen(String key) {
        return clientData.isOpen() && getRouterData(key).isOpen();
    }



    public boolean isAvailability(String key) {
        return !clientData.isOverTime() && getRouterData(key).isAvailability();
    }

    public boolean isUnavailable(String key) {
        return clientData.isOverTime() || getRouterData(key).isUnavailable()  ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultProxy() {
        defaultProxy = true;
    }

    @Override
    public Integer getOnlineContractByOpen(String contractCode) {
        return realQuotaDataHandleService.getOnlineContractByOpen(contractCode);
    }


    public void setRealQuotaDataHandleService(QuotaDataHandleService realQuotaDataHandleService) {
        this.realQuotaDataHandleService = realQuotaDataHandleService;
    }
}
