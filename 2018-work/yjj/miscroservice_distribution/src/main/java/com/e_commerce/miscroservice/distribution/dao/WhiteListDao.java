package com.e_commerce.miscroservice.distribution.dao;

import com.e_commerce.miscroservice.distribution.entity.WhiteList;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/14 15:35
 * @Copyright 玖远网络
 */
public interface WhiteListDao{



    /**
     * 至少有一条记录
     *
     * @param type 白名单类型
     * @return boolean
     * @author Charlie
     * @date 2018/12/14 15:39
     */
    boolean existOne(Integer type);

    /**
     * 在白名单中
     *
     * @param type type
     * @param targetId targetId
     * @return boolean
     * @author Charlie
     * @date 2018/12/14 15:42
     */
    boolean isInWhiteList(Integer type, Long targetId);




    /**
     * 删除白名单
     *
     * @param type type
     * @param targetId targetId
     * @author Charlie
     * @date 2018/12/14 15:59
     */
    void del(Integer type, Long targetId);



    /**
     * 新增白名单
     *
     * @param whiteList whiteList
     * @author Charlie
     * @date 2018/12/14 15:59
     */
    void save(WhiteList whiteList);

}
