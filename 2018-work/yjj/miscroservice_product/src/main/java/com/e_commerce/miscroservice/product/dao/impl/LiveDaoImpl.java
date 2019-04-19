package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.entity.user.LiveUserDTO;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.LiveDao;
import com.e_commerce.miscroservice.product.mapper.LiveMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/16 14:14
 */
@Repository
public class LiveDaoImpl implements LiveDao {
    @Resource
    private LiveMapper liveMapper;

    @Override
    public LiveUser findAnchorByUserId(Long userId) {

        return liveMapper.findAnchorByUserId(userId);
    }

    @Override
    public List<LiveUserDTO> findLiveUserByStoreAndType(Long storeId, Integer liveType) {
        return liveMapper.findLiveUserByStoreAndType(storeId, liveType);
    }

    @Override
    public void upLiveUser(LiveUser user) {
        MybatisOperaterUtil.getInstance().update(user, new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getId,user.getId()));
    }

}
