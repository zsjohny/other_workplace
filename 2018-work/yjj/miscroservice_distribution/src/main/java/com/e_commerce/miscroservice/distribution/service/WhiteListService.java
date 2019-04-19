package com.e_commerce.miscroservice.distribution.service;

import com.e_commerce.miscroservice.distribution.entity.WhiteList;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/14 15:12
 * @Copyright 玖远网络
 */
public interface WhiteListService{

    /**
     * 是否展示分析页面
     *
     * @param type 白名单类型
     * @param targetId 目标id
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/14 15:45
     */
    boolean checkAuth(Integer type, Long targetId);





    /**
     * 添加白名单
     *
     * @param whiteList whiteList
     * @author Charlie
     * @date 2018/12/14 15:54
     */
    void add(WhiteList whiteList);



    /**
     * 删除白名单
     *
     * @param whiteList whiteList
     * @author Charlie
     * @date 2018/12/14 15:54
     */
    void del(WhiteList whiteList);

}
