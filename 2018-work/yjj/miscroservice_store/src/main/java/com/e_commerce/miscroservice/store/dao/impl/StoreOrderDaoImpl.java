package com.e_commerce.miscroservice.store.dao.impl;


import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.store.dao.StoreOrderDao;
import com.e_commerce.miscroservice.store.entity.StaticVariableEntity;
import com.e_commerce.miscroservice.store.entity.response.OrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.OrderDetailsVo;
import com.e_commerce.miscroservice.store.mapper.StoreOrderMapper;
import com.e_commerce.miscroservice.store.entity.vo.StoreOrder;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 售后列表
 */
@Repository
public class StoreOrderDaoImpl implements StoreOrderDao {

    @Resource
    private StoreOrderMapper storeOrderMapper;
    @Override
    public List<OrderListResponse> getUserOrders(Long userId, Integer status, Integer pageNum) {
        PageHelper.startPage(pageNum, StaticVariableEntity.DEFAULT_SIZE);
        List<OrderListResponse> list = storeOrderMapper.getUserOrders(userId,status,pageNum);
        return list;
    }

    @Override
    public List<StoreOrder> getSupplierOrders(Long supplierId, Integer status, Integer pageNum) {
        PageHelper.startPage(pageNum, StaticVariableEntity.DEFAULT_SIZE);
        List<StoreOrder> list = storeOrderMapper.getSupplierOrders(supplierId,status,pageNum);
        return list;
    }

    @Override
    public List<OrderDetailsVo> getChildOrders(Long userId, Long orderNo) {
        return storeOrderMapper.getChildOrders(userId,orderNo);
    }

    @Override
    public StoreOrder getStoreOrderByOrderNo(Long orderNo) {
        return MybatisOperaterUtil.getInstance().findOne(new StoreOrder(), new MybatisSqlWhereBuild(StoreOrder.class).eq(StoreOrder::getOrderno, orderNo));
    }

    @Override
    public Integer addRefundSign(Long orderNo) {
        StoreOrder storeOrderNew = new StoreOrder();
        storeOrderNew.setOrderno(orderNo);
        storeOrderNew.setRefundUnderway(StaticVariableEntity.REFUND_UNDERWAY);
        //售后开始时间时间戳
        storeOrderNew.setRefundStartTime(System.currentTimeMillis());
        return MybatisOperaterUtil.getInstance().update(new StoreOrder(),new MybatisSqlWhereBuild(StoreOrder.class).eq(StoreOrder::getOrderno,orderNo));

    }

    @Override
    public StoreOrder getStoreOrderByOrderNoOrderItemId(Long orderNo, Long orderItemId) {
        return storeOrderMapper.getStoreOrderByOrderNoOrderItemId(orderNo,orderItemId);
    }
}
