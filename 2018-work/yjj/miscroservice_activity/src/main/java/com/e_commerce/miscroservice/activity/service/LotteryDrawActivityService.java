package com.e_commerce.miscroservice.activity.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/8 19:08
 * @Copyright 玖远网络
 */
public interface LotteryDrawActivityService {

    /**
     * 参加活动
     * @param phone
     * @return
     */
    Response joinActivity(String phone);

    /**
     * 找到所有活动产品
     * @return
     * @param code
     */
    Response findAllProductList(Integer code);
}
