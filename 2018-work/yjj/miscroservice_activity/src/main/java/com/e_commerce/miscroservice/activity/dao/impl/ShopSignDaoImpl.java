package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.ShopSignDao;
import com.e_commerce.miscroservice.activity.mapper.ShopSignMapper;
import com.e_commerce.miscroservice.commons.entity.application.activity.Sign;
import com.e_commerce.miscroservice.commons.entity.application.activity.SignLog;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/8 19:09
 * @Copyright 玖远网络
 */
@Repository
public class ShopSignDaoImpl implements ShopSignDao {
    @Resource
    private ShopSignMapper shopSignMapper;
    @Override
    public Integer getNowSignByUser(Long userId) {

        Integer isSign = shopSignMapper.getNowSignByUser(userId);
        return isSign;
    }

    @Override
    public Sign getSignByUser(Long userId) {
        Sign sign = MybatisOperaterUtil.getInstance().findOne(new Sign(), new MybatisSqlWhereBuild(Sign.class).eq(Sign::getUserId,userId));
        return sign;
    }

    @Override
    public void updateSign(Sign sign) {
        MybatisOperaterUtil.getInstance().update(sign,new MybatisSqlWhereBuild(Sign.class).eq(Sign::getUserId,sign.getUserId()));
    }

    @Override
    public void saveSign(Sign sign) {
        MybatisOperaterUtil.getInstance().save(sign);

    }

    @Override
    public DataDictionary getDataDictionary(String code, String groupCode) {

        DataDictionary dataDictionary = MybatisOperaterUtil.getInstance().findOne(new DataDictionary(),new MybatisSqlWhereBuild(DataDictionary.class).eq(DataDictionary::getCode,code).eq(DataDictionary::getGroupCode,groupCode));
        return dataDictionary;
    }

    @Override
    public void saveSignLog(SignLog sginLog) {
        MybatisOperaterUtil.getInstance().save(sginLog);
    }

    @Override
    public List<String> getSignLogMonthByUser(Long userId) {
        return shopSignMapper.getSignLogMonthByUser(userId);
    }

    @Override
    public SignLog getSignLogLimitDescByUser(Long userId) {
        return  shopSignMapper.getSignLogLimitDescByUser(userId);
    }
}
