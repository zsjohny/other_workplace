package com.e_commerce.miscroservice.activity.service;

import com.e_commerce.miscroservice.activity.entity.WxaShare;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/21 15:59
 * @Copyright 玖远网络
 */
public interface ShareService{

    /**
     * 分享商品
     *
     * @param userId 用户id
     * @param shareType 分享类型
     * @param productId 商品id
     * @param beSharedUserId 被分享者用户id
     * @param isEffectiveFans 是否有效粉丝(有效粉丝可以获取收益)
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/21 17:38
     */
    Map<String, Object> doShareProduct(Long userId, Integer shareType, Long productId, Long beSharedUserId, boolean isEffectiveFans);

    /**
     * 确认分享关系
     *
     * @param wxaShare wxaShare
     * @param currentUserSex
     * @author Aison
     * @date 2018/7/6 18:12
     *
     */
    boolean shareFriend(WxaShare wxaShare, Integer currentUserSex);


    /**
     * 我的有效粉丝数量
     *
     * @param shopMemberId 小程序用户主键
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/14 14:44
     */
    Long myEffectiveFans(Long shopMemberId);



    /**
     * 渠道商分享
     *
     * @param channelUserId 渠道商用户id
     * @param currUserId 当前小程序用户id
     * @author Charlie
     * @date 2018/12/24 11:38
     */
    void shareFromChannel(Long channelUserId, Long currUserId);

    /**
     * 获取图片
     *
     * @param type
     * @param shareType
     * @return
     */
    String getImage(Integer type, Integer shareType);
}
