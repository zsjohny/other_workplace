package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.entity.order.YjjOrder;
import com.e_commerce.miscroservice.order.dao.YjjOrderDao;
import com.e_commerce.miscroservice.order.mapper.YjjOrderMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/13 19:08
 * @Copyright 玖远网络
 */
@Repository
public class YjjOrderDaoImpl implements YjjOrderDao {
    @Resource
    private YjjOrderMapper yjjOrderMapper;
    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    @Override
    public YjjOrder findYjjOrderByOrderNo(String orderNo) {
        YjjOrder yjjOrder =  yjjOrderMapper.findYjjOrderByOrderNo(orderNo);
        return yjjOrder;
    }
}
