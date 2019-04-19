package com.e_commerce.miscroservice.activity.dao;


import com.e_commerce.miscroservice.commons.entity.application.activity.CityPartner;

/**
 * Create by hyf on 2018/10/8
 */
public interface CityPartnerDao {

    /**
     * 添加城市合伙人
     * @param name 姓名
     * @param phone 手机
     * @param province 省
     * @param city 市
     * @param district 区
     * @return
     */
    void insertCityPartner(String name, String phone, String province, String city, String district);

    /**
     * 根据手机号查找城市合伙人
     * @param phone
     */
    CityPartner findCityPartner(String phone);
}
