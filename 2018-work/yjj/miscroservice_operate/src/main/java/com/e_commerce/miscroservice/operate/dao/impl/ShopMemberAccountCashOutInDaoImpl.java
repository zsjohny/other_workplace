package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.mapper.ShopMemberAccountCashOutInMapper;
import com.e_commerce.miscroservice.operate.dao.ShopMemberAccountCashOutInDao;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/23 11:02
 * @Copyright 玖远网络
 */
@Component
public class ShopMemberAccountCashOutInDaoImpl implements ShopMemberAccountCashOutInDao{

    @Autowired
    private ShopMemberAccountCashOutInMapper shopMemberAccountCashOutInMapper;

    /**
     * 根据id查询
     *
     * @param id id
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn
     * @author Charlie
     * @date 2018/10/23 11:03
     */
    @Override
    public ShopMemberAccountCashOutIn findById(Long id) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberAccountCashOutIn (),
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .eq (ShopMemberAccountCashOutIn::getId, id)
        );
    }


    /**
     * 查询某订单收益人数量
     *
     * @param orderNumber orderNumber
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/11/9 14:33
     */
    @Override
    @SuppressWarnings ("unchecked")
    public Long findEarningCount(String orderNumber) {
        List<ShopMemberAccountCashOutIn> outIns = MybatisOperaterUtil.getInstance ().finAll (
                new ShopMemberAccountCashOutIn (),
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .eq (ShopMemberAccountCashOutIn::getOrderNo, orderNumber)
                        .eq (ShopMemberAccountCashOutIn::getDelStatus, StateEnum.NORMAL)
                        .groupBy (ShopMemberAccountCashOutIn::getUserId)

        );
        return (long) outIns.size ();
    }





    /**
     * 订单收益列表
     *
     * @param query query
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.util.Objects>>
     * @author Charlie
     * @date 2018/11/9 16:54
     */
    @Override
    public List<Map<String, Object>> listOrderEarnings(ShopMemAcctCashOutInQuery query) {
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        return shopMemberAccountCashOutInMapper.listOrderEarnings(query);
    }

    @Override
    public ShopMemberOrderDstbRecord findOrderRecord(String orderNo) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberOrderDstbRecord (),
                new MybatisSqlWhereBuild (ShopMemberOrderDstbRecord.class)
                        .eq (ShopMemberOrderDstbRecord::getOrderNo, orderNo)
        );
    }


}
