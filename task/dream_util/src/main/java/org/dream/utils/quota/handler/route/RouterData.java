package org.dream.utils.quota.handler.route;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by 12 on 2016/11/14.
 */
class RouterData {

    /**
     * 单词最大的超时时间, 如果超过当前时间,则overTimeCount+1 ,否则则为0
     */
    private static final int MAX_OVER_TIME = 5000 ;
    /**
     * 最大超时计数,如果超过当前计数,表示当前代理将被替换
     */
    private static final int MAX_OVER_COUNT = 5 ;
    /**
     * 最小可用记录,如果数据连续超过此计数,表示当前连接可用.可以用作切入源
     */
    private static final int MIN_AVAILABILITY_COUNT = 5 ;

    private long maxOverTime = 20 * 1000;

    private volatile int overTimeCount = 0;
    private volatile int availabilityCount = 0;
    private boolean open = false;

    private Long lastTime = System.currentTimeMillis();

    private static Map<String ,RouteQuotaDataHandleProxy> currProxyMap = Maps.newConcurrentMap();


    public static Map<String, RouteQuotaDataHandleProxy> getCurrProxyMap() {
        return currProxyMap;
    }

    public static void setCurrProxyMap(String key,RouteQuotaDataHandleProxy  proxy) {
        currProxyMap.put(key,proxy);
    }

    public RouterData(boolean valid){
        this.open =valid;
    }


    public boolean isOpen() {
        return open;
    }

    void open() {
        this.open = true;
    }
    void closed() {
        this.open = false;
    }


    public boolean isAvailability(){
        return  !isOverTime()&& overTimeCount == 0 && availabilityCount > MIN_AVAILABILITY_COUNT;
    }

    public boolean isUnavailable(){
        return   isOverTime() ||overTimeCount > MAX_OVER_COUNT;
    }


    public   boolean isOverTime() {
        return System.currentTimeMillis() - lastTime > maxOverTime;
    }


    public  void flushLastTime(){  lastTime=System.currentTimeMillis();}



    public void flushUpTime(Long upTime) {
        flushLastTime();

        if (Math.abs(upTime - System.currentTimeMillis()) >= MAX_OVER_TIME) {
            availabilityCount = 0;
            if (overTimeCount <= 9999) {
                ++overTimeCount;
            }
        } else {
            overTimeCount = 0;
            if (availabilityCount <= 9999) {// 目的是防止这个数字太大
                ++availabilityCount;
            }

        }
    }


}
