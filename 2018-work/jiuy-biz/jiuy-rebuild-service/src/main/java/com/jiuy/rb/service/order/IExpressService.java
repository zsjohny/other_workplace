package com.jiuy.rb.service.order;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.order.*;
import com.jiuyuan.entity.ExpressInfo;

import java.util.List;
import java.util.Set;

/**
 * 物流相关的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/28 17:21
 * @Copyright 玖远网络
 */
public interface IExpressService {

    /**
     * 物流公司列表
     *
     * @param query query
     * @author Aison
     * @date 2018/6/28 17:26
     * @return java.util.List<com.jiuy.rb.model.order.ExpressSupplierRb>
     */
     List<ExpressSupplierRb> expressSupplierRbList(ExpressSupplierRbQuery query);

    /**
     * 获取订单的物流信息
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/6/28 17:27
     * @return com.jiuy.rb.model.order.ExpressInfoRb
     */
    ExpressInfoRbQuery orderExpressInfo(Long orderNo);


    /**
     * 添加订单的物流信息
     *
     * @param expressInfoRb expressInfoRb
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/29 11:15
     * @return com.jiuy.base.model.MyLog<com.jiuy.rb.model.order.ExpressInfoRb>
     */
    MyLog<ExpressInfoRb> addExpressInfo(ExpressInfoRb expressInfoRb, UserSession userSession);


    /**
     * 修改订单的物流信息
     *
     * @param expressInfoRb expressInfoRb
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/29 11:15
     * @return com.jiuy.base.model.MyLog<com.jiuy.rb.model.order.ExpressInfoRb>
     */
     MyLog<ExpressInfoRb> updateExpressInfo(ExpressInfoRb expressInfoRb, UserSession userSession);


    /**
     * 逻辑删除物流单号
     *
     * @param orders orders
     * @author Aison
     * @date 2018/6/29 16:49
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     */
     MyLog<Long> removeExpress(Set<Long> orders, UserSession userSession);
}
