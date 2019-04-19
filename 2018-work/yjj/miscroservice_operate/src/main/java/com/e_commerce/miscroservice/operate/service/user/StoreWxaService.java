package com.e_commerce.miscroservice.operate.service.user;

import com.e_commerce.miscroservice.commons.entity.user.StoreWxaDataQuery;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/17 15:39
 * @Copyright 玖远网络
 */
public interface StoreWxaService{


    /**
     * 店铺资料列表
     *
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/17 15:42
     * @param query
     */
    Map<String, Object> listAll(StoreWxaDataQuery query);
}
