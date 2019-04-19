package com.e_commerce.miscroservice.activity.dao.impl;


import com.e_commerce.miscroservice.activity.dao.CityPartnerDao;
import com.e_commerce.miscroservice.commons.entity.application.activity.CityPartner;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.stereotype.Repository;

/**
 * Create by hyf on 2018/10/8
 */
@Repository
public class CityPartnerDaoImpl implements CityPartnerDao {
    /**
     * 添加城市合伙人
     * @param name 姓名
     * @param phone 手机
     * @param province 省
     * @param city 市
     * @param district 区
     * @return
     */
    @Override
    public void insertCityPartner(String name, String phone, String province, String city, String district) {
        CityPartner cityPartner = new CityPartner();
        cityPartner.setName(name);
        cityPartner.setPhone(phone);
        cityPartner.setProvince(province);
        cityPartner.setCity(city);
        cityPartner.setDistrict(district);
        cityPartner.setApplyTime(System.currentTimeMillis());

        MybatisOperaterUtil.getInstance().save(cityPartner);
    }
    /**
     * 根据手机号查找城市合伙人
     * @param phone
     */
    @Override
    public CityPartner findCityPartner(String phone) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(CityPartner.class);
        mybatisSqlWhereBuild.eq(CityPartner::getPhone,phone);
        return MybatisOperaterUtil.getInstance().findOne(new CityPartner(),mybatisSqlWhereBuild);

    }
}
