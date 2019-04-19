package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.activity.entity.WxaShare;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/22 11:31
 * @Copyright 玖远网络
 */
public interface WxaShareDao{



    /**
     * 两个人是不是已经是邀请关系
     *
     * @param targetId 被邀请者
     * @param sourceId 邀请者
     * @return boolean
     * @author Charlie
     * @date 2018/11/22 11:35
     */
    boolean isFriend(Long targetId, Long sourceId);



    /**
     * 用户是否已有被邀请关系
     *
     * @param userId currUserId
     * @return boolean
     * @author Charlie
     * @date 2018/11/22 11:40
     */
    boolean hasFriend(Long userId);

    int save(WxaShare wxaShare);



    /**
     * target为唯一约束的插入
     *
     * @param wxaShare wxaShare
     * @return int
     * @author Charlie
     * @date 2018/12/24 17:25
     */
    int safeSave(WxaShare wxaShare);


    /**
     * 查询有效粉丝的上级
     *
     * @param shareUserId shareUserId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/27 11:00
     */
    Long findEffectiveHigherUserId(Long shareUserId);

    /**
     * 根据邀请者 被邀请者 查询分享
     * @param sourceId 被邀请者
     * @param currUserId 邀请者
     * @return
     */
    WxaShare findFriendBySourceIdUserId(Long sourceId, Long currUserId);

    /**
     * 更新用户关系
     * @param wxaShare
     * @return
     */
    int updateWxaShare(WxaShare wxaShare);
}
