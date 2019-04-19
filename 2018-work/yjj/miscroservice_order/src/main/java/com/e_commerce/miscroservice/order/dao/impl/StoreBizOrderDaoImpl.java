package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.order.dao.StoreBizOrderDao;
import com.e_commerce.miscroservice.order.entity.StoreBizOrder;
import com.e_commerce.miscroservice.order.mapper.StoreBizOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 16:37
 * @Copyright 玖远网络
 */
@Component
public class StoreBizOrderDaoImpl implements StoreBizOrderDao{

    private static final long TOW_HOUR = 2 * 60 * 60 * 1000;


    @Autowired
    private StoreBizOrderMapper storeBizOrderMapper;


    /**
     * 查找还未支付的会员订单
     *
     * @param storeId    storeId
     * @param memberType 会员类型
     * @param totalFee   支付金额
     * @param canal      支付来源
     * @return com.e_commerce.miscroservice.order.entity.StoreBizOrder
     * @author Charlie
     * @date 2018/12/11 16:39
     */
    @Override
    public StoreBizOrder findNoPayMemberOrder(Long storeId, Integer memberType, BigDecimal totalFee, Integer canal) {
        //查询两小时的订单
        long lastTime = System.currentTimeMillis() - TOW_HOUR;
        List<StoreBizOrder> orderList = storeBizOrderMapper.findNoPayMemberOrder (storeId, memberType, totalFee, canal, TimeUtils.longFormatString(lastTime));
        return orderList.isEmpty () ? null : orderList.get (0);
    }


    /**
     * 保存
     *
     * @param order order
     * @return int
     * @author Charlie
     * @date 2018/12/11 17:36
     */
    @Override
    public int save(StoreBizOrder order) {
        return MybatisOperaterUtil.getInstance ().save (order);
    }


    /**
     * 查询用户可用订单
     *
     * @param orderNo orderNo
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.order.entity.StoreBizOrder
     * @author Charlie
     * @date 2018/12/11 20:43
     */
    @Override
    public StoreBizOrder findByOrderNo(String orderNo, Long storeId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new StoreBizOrder (),
                new MybatisSqlWhereBuild (StoreBizOrder.class)
                        .eq (StoreBizOrder::getOrderNo, orderNo)
                        .eq (StoreBizOrder::getBuyerId, storeId)
                        .eq (StoreBizOrder::getDelStatus, StateEnum.NORMAL)
        );
    }




    /**
     * 根据订单id更新
     *
     * @param order order
     * @return int
     * @author Charlie
     * @date 2018/12/12 1:09
     */
    @Override
    public int updateById(StoreBizOrder order) {
        return MybatisOperaterUtil.getInstance ().update (
                order,
                new MybatisSqlWhereBuild (StoreBizOrder.class)
                        .eq (StoreBizOrder::getId, order.getId ())
        );
    }


}
