package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.WxaShareDao;
import com.e_commerce.miscroservice.activity.entity.WxaShare;
import com.e_commerce.miscroservice.activity.mapper.WxaShareMapper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/22 11:31
 * @Copyright 玖远网络
 */
@Component
public class WxaShareDaoImpl implements WxaShareDao {


    private Log logger = Log.getInstance(WxaShareDaoImpl.class);

    @Autowired
    private WxaShareMapper wxaShareMapper;


    /**
     * 两个人是不是已经是邀请关系
     *
     * @param targetId 被邀请者
     * @param sourceId 邀请者
     * @return boolean
     * @author Charlie
     * @date 2018/11/22 11:35
     */
    @Override
    public boolean isFriend(Long targetId, Long sourceId) {
        return MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(WxaShare.class)
                        .eq(WxaShare::getTargetUser, targetId)
                        .eq(WxaShare::getSourceUser, sourceId)
        ) > 0;
    }


    /**
     * 用户是否已有被邀请关系
     *
     * @param currUserId currUserId
     * @return boolean
     * @author Charlie
     * @date 2018/11/22 11:40
     */
    @Override
    public boolean hasFriend(Long currUserId) {
        return MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(WxaShare.class)
                        .eq(WxaShare::getTargetUser, currUserId)
        ) > 0;
    }


    /**
     * 保存
     *
     * @param wxaShare wxaShare
     * @return int
     * @author Charlie
     * @date 2018/11/22 11:49
     */
    @Override
    public int save(WxaShare wxaShare) {
        return MybatisOperaterUtil.getInstance().save(wxaShare);
    }


    /**
     * target为唯一约束的插入
     *
     * @param wxaShare wxaShare
     * @return int
     * @author Charlie
     * @date 2018/12/24 17:25
     */
    @Override
    public int safeSave(WxaShare wxaShare) {
        Long sourceUser = wxaShare.getSourceUser();
        return wxaShareMapper.safeSave(
                sourceUser,
                wxaShare.getChannelUserId(),
                wxaShare.getTargetUser(),
                wxaShare.getShareType(),
                wxaShare.getFansType()
        );
    }


    /**
     * 查询有效粉丝的上级
     *
     * @param shareUserId shareUserId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/27 11:00
     */
    @Override
    public Long findEffectiveHigherUserId(Long shareUserId) {
        logger.info("查询用户={}的上级",shareUserId);
        WxaShare higher = MybatisOperaterUtil.getInstance().findOne(
                new WxaShare(),
                new MybatisSqlWhereBuild(WxaShare.class)
                        .eq(WxaShare::getFansType, 1)
                        .eq(WxaShare::getTargetUser, shareUserId)
        );
        return higher == null ? null : higher.getSourceUser();
    }

    /**
     * 根据邀请者 被邀请者 查询分享
     * @param targetId 被邀请者
     * @param sourceId 邀请者
     * @return
     */
    @Override
    public WxaShare findFriendBySourceIdUserId(Long targetId, Long sourceId) {
        WxaShare wxaShare = MybatisOperaterUtil.getInstance().findOne(new WxaShare(), new MybatisSqlWhereBuild(WxaShare.class)
                .eq(WxaShare::getTargetUser, targetId)
                .eq(WxaShare::getSourceUser, sourceId));
        return wxaShare;
    }

    /**
     * 更新用户关系
     * @param wxaShare
     * @return
     */
    @Override
    public int updateWxaShare(WxaShare wxaShare) {
        Integer ret = MybatisOperaterUtil.getInstance().update(wxaShare, new MybatisSqlWhereBuild(WxaShare.class).eq(WxaShare::getId,wxaShare.getId()));
        return ret;
    }
}
