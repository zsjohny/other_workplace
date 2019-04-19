package com.jiuyuan.service.common.area;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.jiuyuan.service.common.area.BizCacheKey.PlatformName.小程序API;
import static com.jiuyuan.service.common.area.BizCacheKey.PlatformName.新运营后台;


/**
 * 专门用来存放缓存key
 *
 * <p>不同平台的缓存key,用{@link @Platform}标识</p>
 * @author Charlie
 * @version V1.0
 * @date 2018/8/2 14:46
 * @Copyright 玖远网络
 */
public class BizCacheKey{

    /**
     * 团购活动(需要后缀,活动id)
     */
    @Platform ({小程序API, 新运营后台})
    public static final String SHOP_TEAM_ACTIVITY_INVENTORY = "wxapi:activity:teamBuy:";
    /**
     * 秒杀活动(需要后缀,活动id)
     */
    @Platform ({小程序API, 新运营后台})
    public static final String SHOP_SECOND_ACTIVITY_INVENTORY = "wxapi:activity:secondBuy:";


    //================================================================================================
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.FIELD)
    public @interface Platform{
        PlatformName[] value();
    }

    public enum PlatformName{
        /**门店宝API*/
        门店宝API(),
        /**小程序API*/
        小程序API(),
        /**供应商后台*/
        供应商后台(),
        /**老运营后台*/
        老运营后台(),
        /**新运营后台*/
        新运营后台(),
        /**代理商后台*/
        代理商后台(),
        /**小程序后台*/
        小程序后台()
    }
}
