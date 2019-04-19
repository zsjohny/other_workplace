package com.jiuyuan.service.common.area;

/**
 * 该类用来解决一个商品参加多个活动的逻辑判断, 不建议使用做其他功能
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/8/9 18:24
 * @Copyright 玖远网络
 */
public class ActivityInfoCache{

    /**
     * 微信小程序版本
     */
    public static final int VERSION_140 = 140;
    private static final ThreadLocal<ActivityInfoCache> activityCache = new ThreadLocal<> ();

    private ActivityInfoCache() {

    }

    /**
     * 活动id
     */
    private Long activityId;
    /**
     * 活动类型 0:未知活动,1:团购活动,2:秒杀活动
     * <p>策略:</p>
     * <p>if 0: 会查询当前商品绑定的一个最近的有效的活动(如果是开始中的活动,返回开始中,如果没有开始中,返回待开始,如果没有待开始,返回null)</p>
     * <p>if 1: 用活动id, 商品id 查询一个团购活动, 没有就返回null</p>
     * <p>if 2: 用活动id, 商品id 查询一个秒杀活动, 没有就返回null</p>
     */
    private Integer activityType;
    /**
     * 微信版本号
     */
    private Integer wxVersion;


    /**
     * 创建对象
     *
     * @param activityId   activityId
     * @param activityType activityType
     * @param wxVersion    wxVersion
     * @return com.jiuyuan.service.common.area.ActivityInfoCache
     * @author Charlie
     * @date 2018/8/9 18:44
     */
    public static ActivityInfoCache createInstance(Long activityId, Integer activityType, Integer wxVersion) {
        ActivityInfoCache activity = new ActivityInfoCache ();
        activity.activityId = activityId;
        activity.activityType = activityType;
        activity.wxVersion = wxVersion;
        activityCache.set (activity);
        return activity;
    }



    /**
     * 获取
     *
     * @return com.jiuyuan.service.common.area.ActivityInfoCache
     * @author Charlie
     * @date 2018/8/9 18:44
     */
    public static ActivityInfoCache get() {
        return activityCache.get ();
    }



    /**
     * 清空
     *
     * @author Charlie
     * @date 2018/8/9 18:45
     */
    public static void clear() {
        ActivityInfoCache cache = activityCache.get ();
        if (cache != null) {
            activityCache.remove ();
        }
    }


    public Long getActivityId() {
        return activityId;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public Integer getWxVersion() {
        return wxVersion;
    }

}
