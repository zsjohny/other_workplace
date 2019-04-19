package com.e_commerce.miscroservice.activity.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/8 19:08
 * @Copyright 玖远网络
 */
public interface CityPartnerService {

    /**
     * 添加城市合伙人
     * @param name 姓名
     * @param phone 手机
     * @param province 省
     * @param city 市
     * @param district 区
     * @return
     */
    Response insertCityPartner(String name, String phone, String province, String city, String district);
}
