package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.LiveDao;
import com.e_commerce.miscroservice.operate.entity.request.FindLiveRequest;
import com.e_commerce.miscroservice.operate.entity.response.FindAllLiveResponse;
import com.e_commerce.miscroservice.operate.mapper.LiveMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/15 15:43
 */
@Repository
public class LiveDaoImpl implements LiveDao {
    @Resource
    private LiveMapper liveMapper;

    @Override
    public List<FindAllLiveResponse> findAllAnchorByType(FindLiveRequest liveRequest, Integer type) {
        PageHelper.startPage(liveRequest.getPageNumber(),liveRequest.getPageSize());
        List<FindAllLiveResponse> liveUsers = liveMapper.findAllAnchorByType(liveRequest,type);
        return liveUsers;
    }

    @Override
    public LiveUser findAnchorPhoneNumberByStoreIdType(Long storeId, int type) {
        LiveUser liveUser = liveMapper.findAnchorPhoneNumberByStoreIdType(storeId,type);
        return liveUser;
    }

    @Override
    public void addAnchor(LiveUser liveUser) {
        MybatisOperaterUtil.getInstance().save(liveUser);
    }

    @Override
    public void upOfficialAnchor(LiveUser liveUser) {
        MybatisOperaterUtil.getInstance().update(liveUser, new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getId,liveUser.getId()));
    }

    @Override
    public LiveUser findAnchorByPhone(String phone) {
        LiveUser liveUser = MybatisOperaterUtil.getInstance().findOne(new LiveUser(), new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getPhone,phone).eq(LiveUser::getDelStatus, StateEnum.NORMAL));

        return liveUser;
    }
}
