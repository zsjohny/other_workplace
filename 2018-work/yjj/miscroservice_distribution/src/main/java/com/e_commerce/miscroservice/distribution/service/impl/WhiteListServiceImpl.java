package com.e_commerce.miscroservice.distribution.service.impl;

import com.e_commerce.miscroservice.distribution.dao.WhiteListDao;
import com.e_commerce.miscroservice.distribution.entity.WhiteList;
import com.e_commerce.miscroservice.distribution.service.WhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/14 15:12
 * @Copyright 玖远网络
 */
@Service
public class WhiteListServiceImpl implements WhiteListService{


    @Autowired
    private WhiteListDao whiteListDao;


    /**
     * 检查权限
     *
     * @param type 白名单类型
     * @param targetId 目标id
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/14 15:45
     */
    @Override
    public boolean checkAuth(Integer type, Long targetId) {
        boolean existOne = whiteListDao.existOne (type);
        if (! existOne) {
            //白名单一个都没有,就表示白名单不启用
            return Boolean.TRUE;
        }
        return whiteListDao.isInWhiteList (type, targetId);
    }



    /**
     * 添加白名单
     *
     * @param whiteList whiteList
     * @author Charlie
     * @date 2018/12/14 15:54
     */
    @Override
    public void add(WhiteList whiteList) {
        whiteListDao.save (whiteList);
    }


    /**
     * 删除白名单
     *
     * @param whiteList whiteList
     * @author Charlie
     * @date 2018/12/14 15:54
     */
    @Override
    public void del(WhiteList whiteList) {
        whiteListDao.del (whiteList.getType (), whiteList.getTargetId ());
    }


}
