package com.e_commerce.miscroservice.publicaccount.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.publicaccount.dao.PublicAccountUserDao;
import com.e_commerce.miscroservice.publicaccount.mapper.PublicAccountUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/24 15:16
 * @Copyright 玖远网络
 */
@Component
public class PublicAccountUserDaoImpl implements PublicAccountUserDao{

    private Log logger = Log.getInstance(PublicAccountUserDaoImpl.class);

    @Autowired
    private PublicAccountUserMapper publicAccountUserMapper;



    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/9/24 23:34
     */
    @Override
    public int updateByPrimaryKeySelective(PublicAccountUser updInfo) {
        return publicAccountUserMapper.updateByPrimaryKeySelective (updInfo);
    }

    /**
     * 根据id查询账号
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/28 14:00
     */
    @Override
    public PublicAccountUser findById(Long userId) {
        return !BeanKit.gt0 (userId) ? null : MybatisOperaterUtil.getInstance ().findOne (
                new PublicAccountUser (),
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                .eq (PublicAccountUser::getId, userId)
                .eq (PublicAccountUser::getDelStatus, StateEnum.NORMAL)
        );
    }

    @Override
    public List<PublicAccountUserQuery> listUser(PublicAccountUserQuery query) {
        return publicAccountUserMapper.listUser (query);
    }



    /**
     * 根据openId查找用户,按登录时间limit one
     *
     * @param openId openId
     * @return com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser
     * @author Charlie
     * @date 2018/10/22 17:23
     */
    @Override
    @SuppressWarnings ("unchecked")
    public PublicAccountUser findSubjectByOpenId(String openId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new PublicAccountUser(),
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                .eq (PublicAccountUser::getOpenId, openId)
//                .eq (PublicAccountUser::getDelStatus, StateEnum.NORMAL)
                .eq (PublicAccountUser::getSubjectAccount, 1)
        );
    }



    /**
     * 关闭主体账号
     *
     * @param openId openId
     * @return int
     * @author Charlie
     * @date 2018/10/24 20:08
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeSubjectAccountByOpenId(String openId) {
        List<PublicAccountUser> history = MybatisOperaterUtil.getInstance ().finAll (
                new PublicAccountUser (),
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getOpenId, openId)
                        .eq (PublicAccountUser::getSubjectAccount, 1)
        );
        logger.info ("查询主体账号 openId={}, size={}", openId, history);
        if (history.isEmpty ()) {
            return;
        }

        for (PublicAccountUser user : history) {
            PublicAccountUser upd = new PublicAccountUser ();
            upd.setSubjectAccount (0);
            int rec = MybatisOperaterUtil.getInstance ().update (upd, new MybatisSqlWhereBuild (PublicAccountUser.class).eq (PublicAccountUser::getId, user.getId ()));
            ErrorHelper.declare (rec == 1, "关闭主体账号失败");
        }
    }


    /**
     * 根据手机号将账号设为主体账号
     *
     * @param phone phone
     * @return int
     * @author Charlie
     * @date 2018/10/24 20:50
     */
    @Override
    public int openSubjectAccountByPhone(String phone) {
        PublicAccountUser upd = new PublicAccountUser ();
        upd.setSubjectAccount (1);
        return MybatisOperaterUtil.getInstance ().update (upd,
                new MybatisSqlWhereBuild (PublicAccountUser.class)
                        .eq (PublicAccountUser::getPhone, phone)
                        .eq (PublicAccountUser::getDelStatus, StateEnum.NORMAL)
        );
    }
}
