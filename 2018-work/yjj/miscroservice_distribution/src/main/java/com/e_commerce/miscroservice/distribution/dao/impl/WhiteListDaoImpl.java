package com.e_commerce.miscroservice.distribution.dao.impl;

import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.distribution.dao.WhiteListDao;
import com.e_commerce.miscroservice.distribution.entity.WhiteList;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/14 15:35
 * @Copyright 玖远网络
 */
@Component
public class WhiteListDaoImpl implements WhiteListDao{


    /**
     * 至少有一条记录
     *
     * @param type type
     * @return boolean
     * @author Charlie
     * @date 2018/12/14 15:39
     */
    @Override
    public boolean existOne(Integer type) {
        return MybatisOperaterUtil.getInstance ().count (
                new MybatisSqlWhereBuild (WhiteList.class)
                        .eq (WhiteList::getDelStatus, StateEnum.NORMAL)
                        .eq (WhiteList::getType, type)
        )>0;
    }




    /**
     * 在白名单中
     *
     * @param type type
     * @param targetId targetId
     * @return boolean
     * @author Charlie
     * @date 2018/12/14 15:42
     */
    @Override
    public boolean isInWhiteList(Integer type, Long targetId) {
        return MybatisOperaterUtil.getInstance ().count (
                new MybatisSqlWhereBuild (WhiteList.class)
                        .eq (WhiteList::getDelStatus, StateEnum.NORMAL)
                        .eq (WhiteList::getTargetId, targetId)
                        .eq (WhiteList::getType, type)
        )>0;
    }




    /**
     * 删除白名单
     *
     * @param type type
     * @param targetId targetId
     * @author Charlie
     * @date 2018/12/14 15:59
     */
    @Override
    public void del(Integer type, Long targetId) {
        WhiteList updInfo = new WhiteList ();
        updInfo.setDelStatus (StateEnum.DELETE);
        MybatisOperaterUtil.getInstance ().update (
                updInfo, new MybatisSqlWhereBuild (WhiteList.class)
                        .eq (WhiteList::getType, type)
                        .eq (WhiteList::getTargetId, targetId)
        );
    }




    /**
     * 新增白名单
     *
     * @param whiteList whiteList
     * @author Charlie
     * @date 2018/12/14 15:59
     */
    @Override
    public void save(WhiteList whiteList) {
        MybatisOperaterUtil.getInstance ().save (whiteList);
    }
}
