package com.jiuy.rb.service.account;

import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.enums.ShareTypeEnum;
import com.jiuy.rb.model.coupon.ShareCoinsRule;
import com.jiuy.rb.model.coupon.WxaShare;
import com.jiuy.rb.model.coupon.WxaShareQuery;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 分享的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 17:09
 * @Copyright 玖远网络
 */
public interface IShareService {

    /**
     * 获取分享规则
     *
     * @param shareType shareType
     * @author Aison
     * @date 2018/7/6 15:57
     * @return com.jiuy.rb.model.coupon.ShareCoinsRule
     */
     ShareCoinsRule getRule(ShareTypeEnum shareType);

    /**
     * 分享商品
     *
     * @param productId 商品id
     * @param userSession 当前用户
     * @author Aison
     * @date 2018/7/5 17:11
     *
     */
    Map<String, Object> shareProduct(Long productId, UserSession userSession);


    /**
     * 分享活动
     *
     * @param activityId activityId
     * @param userSession userSession
     * @author Aison
     * @date 2018/7/6 10:45
     *
     */
    Map<String, Object> shareActivity(Long activityId,UserSession userSession,Integer type);

    /**
     * 分享优惠券
     *
     * @param couponId couponId
     * @param userSession userSession
     * @author Aison
     * @date 2018/7/6 10:45
     *
     */
    Map<String, Object> shareCoupon(Long couponId,UserSession userSession);

    /**
     * 确认分享关系
     *
     * @param wxaShare wxaShare
     * @param userSession userSession
     * @author Aison
     * @date 2018/7/6 18:12
     *
     */
     void shareFriend(WxaShare wxaShare, UserSession userSession);

    /**
     * 分享列表
     *
     * @param query query
     * @author Aison
     * @date 2018/7/6 18:27
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.coupon.WxaShareQuery>
     */
     MyPage<WxaShareQuery> shareFriendList(WxaShareQuery query);


    /**
     * 获取用户的邀请数量
     *
     * @param userId userId
     * @author Aison
     * @date 2018/7/11 17:51
     * @return java.util.Map<java.lang.Long,java.lang.Long>
     */
    Map<Long,Long> inviteCountMap(Set<Long> userId);

    /**
     * 查询单个分享
     *
     * @param query query
     * @author Aison
     * @date 2018/7/17 11:39
     * @return com.jiuy.rb.model.coupon.WxaShare
     */
    List<WxaShare> getShare(WxaShareQuery query);


}
