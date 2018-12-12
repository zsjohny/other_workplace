package org.dream.utils.quota.handler.route;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

/**
 * Created by yhj on 16/11/11.
 */
public class QuotaDataHandleRouter {

    private final static Logger LOG = LoggerFactory.getLogger(QuotaDataHandleRouter.class);

    private Long timerDelay = 3000L;

    private List<RouteQuotaDataHandleProxy> routerProxyList;

    private static RouteQuotaDataHandleProxy defaultProxy;


    private String defaultQuotaProxy;

    private Timer timer;

    public void init() {

        if (initProxy()) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RestartTask(), timerDelay*5, timerDelay);
        }
    }

    public void destroy() {
        if (timer != null) {
            timer.cancel();
        }
    }


    /**
     * 同时只会打开一个合约 切换合约的时候会关闭上一个合约 所以当前代理就是打开状态的那个
     * <p>
     * 不存在合约不存在的情况 因为如果是默认的数据源没有合约 判断可用性的时候会添加合约
     * 如果是默认的不存在 默认的不可用切换的时候 判断可用性也会添加合约
     */
    void timerRun() {


        RouterData.getCurrProxyMap().forEach(new BiConsumer<String, RouteQuotaDataHandleProxy>() {
            @Override
            public void accept(String key, RouteQuotaDataHandleProxy proxy) {
                //没开市的就不去判断和切换了
                if(proxy.getOnlineContractByOpen(key)==0)return;

                if (proxy != defaultProxy && defaultProxy.isAvailability(key)) {
                    // 如果当前源不是默认源,并且默认源可用,就切换到默认源
                    switchTo(proxy, defaultProxy, key);
                } else if (proxy.isUnavailable(key) ) {
                    // 如果当前不可用 并且处于打开状态 切换合约
                    switchToNext(proxy, key);
                }

            }
        });


    }

    /**
     * @return false , 不用启动了
     */
    private boolean initProxy() {
        if (StringUtils.isEmpty(defaultQuotaProxy)) {
            throw new RuntimeException("发生重大错误,defaultQuotaProxy配置不能为空!");
        }

        if (routerProxyList == null || routerProxyList.size() == 0) {
            throw new RuntimeException("发生重大错误,routerProxy配置不能为空!");
        }

        if (routerProxyList.size() == 1) {
            return false;
        }

        for (RouteQuotaDataHandleProxy proxy : routerProxyList) {
            if (defaultQuotaProxy.equals(proxy.getName())) {
                defaultProxy = proxy;
                defaultProxy.setDefaultProxy();

                LOG.info("初始化路由 当前默认路由为 {} ", defaultQuotaProxy);
                break;
            }
        }


        if (defaultProxy == null) {
            throw new RuntimeException("发生重大错误,defaultQuotaProxy不存在!");
        }

        return true;
    }

    private void switchToNext(RouteQuotaDataHandleProxy currentProxy, String key) {
        RouteQuotaDataHandleProxy proxy = getNextClient(currentProxy);


        do {
            if (proxy == currentProxy) {
                LOG.info("切换合约{}行情源,没有可用的行情源,不进行切换,当前源{}\r\n", key, currentProxy.getName());
                break;
            }

            // 判断目标源可不可用,不可用就查找下一个源
            if (proxy.isAvailability(key)) {
                switchTo(currentProxy, proxy, key);
                break;
            } else {
                proxy = getNextClient(proxy);
            }

        } while (true);
    }

    private synchronized void switchTo(RouteQuotaDataHandleProxy currentProxy, RouteQuotaDataHandleProxy proxy, String key) {

        if (!currentProxy.getRouterData(key).isOpen() && proxy.getRouterData(key).isOpen()) {
            return;
        }

        LOG.info("切换合约{}行情源,从{}到{}\r\n", key, currentProxy != null ? currentProxy.getName() : "", proxy.getName());

        if (currentProxy != null) {
            currentProxy.getRouterData(key).closed();
        }
        proxy.getRouterData(key).open();


        RouterData.setCurrProxyMap(key, proxy);

    }

    private RouteQuotaDataHandleProxy getNextClient(RouteQuotaDataHandleProxy currentProxy) {

        if (routerProxyList.size() == 1) {
            return routerProxyList.get(0);
        }

        for (int i = 0; i < routerProxyList.size(); i++) {
            if (routerProxyList.get(i) == currentProxy) {
                return i < routerProxyList.size() - 1 ? routerProxyList.get(i + 1) : routerProxyList.get(0);
            }
        }
        return defaultProxy;
    }


    public void setRouterProxyList(List<RouteQuotaDataHandleProxy> routerProxyList) {
        this.routerProxyList = routerProxyList;
    }

    public void setDefaultQuotaProxy(String defaultQuotaProxy) {
        this.defaultQuotaProxy = defaultQuotaProxy;
    }

    public static RouteQuotaDataHandleProxy getDefaultProxy() {
        return defaultProxy;
    }

    public void setTimerDelay(Long timerDelay) {
        this.timerDelay = timerDelay;
    }

    protected class RestartTask extends TimerTask {

        @Override
        public void run() {
            timerRun();
        }
    }


}
