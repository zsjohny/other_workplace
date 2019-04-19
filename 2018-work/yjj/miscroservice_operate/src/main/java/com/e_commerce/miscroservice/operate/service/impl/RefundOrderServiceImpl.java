package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrderResponce;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.dao.RefundOrderDao;
import com.e_commerce.miscroservice.operate.entity.request.RefundOrderFindReqeust;
import com.e_commerce.miscroservice.operate.service.order.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by hyf on 2018/11/30
 */

@Service
public class RefundOrderServiceImpl implements RefundOrderService {
    private Log logger = Log.getInstance(RefundOrderServiceImpl.class);
    @Autowired
    private RefundOrderDao refundOrderDao;
    /**
     * 售后订单管理
     * @param obj
     * @return
     */
    @Override
    public Response findAllRefundOrder(RefundOrderFindReqeust obj) {
        logger.info("售后订单管理obj={}",obj);
        List<RefundOrderResponce> refundOrderResponce = refundOrderDao.findAllRefundOrder(obj);
        SimplePage<RefundOrderResponce> simplePage = new SimplePage<>(refundOrderResponce);
        return Response.success(simplePage);
    }

    /**
     * 结束工单
     * @param id
     * @param money
     * @param msg
     * @return
     */
    @Override
    public Response finishService(Long id, Double money, String msg) {
        logger.info("结束工单处理 id={},money={},msg={},",id,money,msg);
        RefundOrder order = refundOrderDao.findRefundOrderById(id);
        if (order==null){
            logger.warn("该订单不存在");
            return Response.errorMsg("该订单不存在");
        }

        refundOrderDao.updateRefundOrder( id, money,  msg);
        return Response.success();
    }
}
