package com.e_commerce.miscroservice.operate.service.order;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.RefundOrderFindReqeust;

/**
 * Create by hyf on 2018/11/30
 */

public interface RefundOrderService {
    /**
     * 售后订单管理
     * @param obj
     * @return
     */
    Response findAllRefundOrder(RefundOrderFindReqeust obj);

    /**
     * 结束工单
     * @param id
     * @param money
     * @param msg
     * @return
     */
    Response finishService(Long id, Double money, String msg);
}
