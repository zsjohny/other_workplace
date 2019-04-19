package com.e_commerce.miscroservice.operate.service.activity;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/22 17:15
 * @Copyright 玖远网络
 */
public interface CityPartnerService {
    /**
     * 城市合伙人列表
     * @param name
     * @param phone
     * @param pageNum
     * @param pageSize
     * @return
     */
    Response findAllCityPartner(String name, String phone, Integer pageNum, Integer pageSize);
}
