package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.activity.CityPartner;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.CityPartnerDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by hyf on 2018/11/22
 */
@Repository
public class CityPartnerDaoImpl implements CityPartnerDao {
    /**
     * 城市合伙人列表
     * @param name
     * @param phone
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<CityPartner> findAllCityPartner(String name, String phone, Integer pageNum, Integer pageSize) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(CityPartner.class);
        if (StringUtils.isNotEmpty(name)){
            mybatisSqlWhereBuild.like(CityPartner::getName,name);
        }
        if (StringUtils.isNotEmpty(phone)){
            mybatisSqlWhereBuild.like(CityPartner::getPhone,phone);
        }
        mybatisSqlWhereBuild.page(pageNum,pageSize);
        List<CityPartner>  list = MybatisOperaterUtil.getInstance().finAll(new CityPartner(),mybatisSqlWhereBuild);
        return list;
    }

    @Override
    public int findTotal(String name, String phone) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(CityPartner.class);
        if (StringUtils.isNotEmpty(name)){
            mybatisSqlWhereBuild.like(CityPartner::getName,name);
        }
        if (StringUtils.isNotEmpty(phone)){
            mybatisSqlWhereBuild.like(CityPartner::getPhone,phone);
        }
        Long total = MybatisOperaterUtil.getInstance().count(mybatisSqlWhereBuild);
        return total.intValue();
    }
}
