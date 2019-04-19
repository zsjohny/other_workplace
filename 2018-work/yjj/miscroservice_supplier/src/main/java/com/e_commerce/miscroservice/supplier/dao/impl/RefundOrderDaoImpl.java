package com.e_commerce.miscroservice.supplier.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.order.StoreBusiness;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.supplier.dao.RefundOrderDao;
import com.e_commerce.miscroservice.supplier.entity.request.RefundGoodsMoneyRequest;
import com.e_commerce.miscroservice.supplier.entity.request.StoreRefundOrderActionLog;
import com.e_commerce.miscroservice.supplier.mapper.RefundOrderMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/12/4 10:15
 * @Copyright 玖远网络
 */
@Repository
public class RefundOrderDaoImpl implements RefundOrderDao {
    private Log logger = Log.getInstance(RefundOrderDaoImpl.class);
    @Value("${url}")
    private String url;
    @Resource
    private RefundOrderMapper refundOrderMapper;

    /**
     * 售后处理 退货退款
     *
     * @param obj
     * @return
     */
    @Override
    public void refundGoodsMoney(RefundGoodsMoneyRequest obj) {
        refundOrderMapper.refundGoodsMoney(obj);
    }

    /**
     * 确认收货
     * @param id
     * @param code
     * @return
     */
    @Override
    public Response confirmTackGoods(Long id, Integer code) {
        try {
            refundOrderMapper.confirmTackGoods(id,code);
            /**
             * 插入日志
             */
            Long refundOrderId = refundOrderMapper.selectRefundNo(id);
            StoreRefundOrderActionLog storeRefundOrderActionLog=new StoreRefundOrderActionLog();
            storeRefundOrderActionLog.setRefundOrderId(refundOrderId);
            storeRefundOrderActionLog.setActionTime(System.currentTimeMillis());
            storeRefundOrderActionLog.setActionName("卖家已确认收货,退款即将到账请注意查收");
            refundOrderMapper.insertLog(storeRefundOrderActionLog);
            return Response.success("确认收货成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.errorMsg("确认收货失败");
        }

    }

    /**
     * 根据售后订单查询 用户
     * @param id
     */
    @Override
    public StoreBusiness findRefundUser(Long id) {
       StoreBusiness storeBusiness = refundOrderMapper.findRefundUser(id);
       return storeBusiness;
    }


}
