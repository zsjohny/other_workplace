package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.activity.CityPartner;

import java.util.List;

/**
 * Create by hyf on 2018/11/22
 */
public interface CityPartnerDao {
    /**
     * 城市合伙人列表
     * @param name
     * @param phone
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<CityPartner> findAllCityPartner(String name, String phone, Integer pageNum, Integer pageSize);

    /**
     * 总数量
     * @param name
     * @param phone
     * @param pageNumber
     * @param pageSize
     * @return
     */
    int findTotal(String name, String phone);
}
