package com.jiuy.rb.enums;

/**
 * 专门用来存放缓存key
 *
 * <p>不同平台的缓存key,用{@link @Platform}标识</p>
 * @author Charlie
 * @version V1.0
 * @date 2018/8/2 14:46
 * @Copyright 玖远网络
 */
public enum CacheKeyEnum{


    /**
     * 团购活动(需要后缀,活动id)
     */
    SHOP_TEAM_ACTIVITY_INVENTORY("wxapi:activity:teamBuy:"),
    /**
     * 秒杀活动(需要后缀,活动id)
     */
    SHOP_SECOND_ACTIVITY_INVENTORY("wxapi:activity:secondBuy:")
        ;

    private String cache;


    CacheKeyEnum(String cache) {
        this.cache = cache;
    }


    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
}
