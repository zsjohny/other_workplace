package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.order.YjjOrder;
import com.e_commerce.miscroservice.order.dao.YjjOrderDao;
import com.e_commerce.miscroservice.order.service.yjj.YjjOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Create by hyf on 2018/11/13
 */
@Service
public class YjjOrderServiceImpl implements YjjOrderService {
    @Resource
    private YjjOrderDao yjjOrderDao;
    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    @Override
    public YjjOrder findYjjOrderByOrderNo(String orderNo) {
        return yjjOrderDao.findYjjOrderByOrderNo(orderNo);
    }
}
