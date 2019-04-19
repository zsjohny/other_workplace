package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.MapHelper;
import com.e_commerce.miscroservice.order.dao.ShopMemberOrderDao;
import com.e_commerce.miscroservice.order.entity.ShopMemberOrderItem;
import com.e_commerce.miscroservice.order.entity.ShopOrderAfterSale;
import com.e_commerce.miscroservice.order.entity.StoreOrder;
import com.e_commerce.miscroservice.order.mapper.ShopMemberOrderItemMapper;
import com.e_commerce.miscroservice.order.mapper.ShopMemberOrderMapper;
import com.e_commerce.miscroservice.order.mapper.ShopOrderAfterSaleMapper;
import com.e_commerce.miscroservice.order.mapper.StoreOrderMapper;
import com.e_commerce.miscroservice.order.rpc.user.StoreBusinessAccountRpcService;
import com.e_commerce.miscroservice.order.service.wx.ShopMemberOrderService;
import com.e_commerce.miscroservice.order.service.yjj.StoreOrderService;
import com.e_commerce.miscroservice.order.vo.StoreOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/17 11:01
 */
@Service
public class StoreOrderServiceImpl implements StoreOrderService {

    private Log logger = Log.getInstance(StoreOrderServiceImpl.class);

    @Autowired
    private ShopOrderAfterSaleMapper shopOrderAfterSaleMapper;
    @Autowired
    private StoreBusinessAccountRpcService storeBusinessAccountRpcService;
    @Autowired
    private ShopMemberOrderMapper shopMemberOrderMapper;
    @Autowired
    private StoreOrderMapper storeOrderMapper;


    @Override
    public void platformInsteadOfSendGoodsAfter15Days(StoreOrderDTO dto) {
        Long orderNo = dto.getOrderNo();

        StoreOrder updTime = new StoreOrder();
        updTime.setOrderNo(orderNo);
        updTime.setUpdateTime(System.currentTimeMillis());
        int rec = storeOrderMapper.updateTimeById(updTime);
        ErrorHelper.declare(rec == 1, "订单状态异常");

        StoreOrder storeOrder = storeOrderMapper.findByOrderNo(orderNo);
        if (storeOrder == null) {
            logger.warn("没有找到订单");
            return;
        }
        if (! storeOrder.getType().equals(1)) {
            logger.warn("不是待发货订单");
            return;
        }
        Long shopMemberOrderId = storeOrder.getShopMemberOrderId();
        if (shopMemberOrderId == 0) {
            logger.warn("没有小程序订单信息");
            return;
        }
        int orderStatus = storeOrder.getOrderStatus();
        if (! storeOrder.getStatus().equals(0)) {
            logger.warn("平台订单状态异常");
            return;
        }
        boolean userHasPay = orderStatus == 10 || orderStatus == 50 || orderStatus == 70;
        if (! userHasPay) {
            //未付款,交易关闭的不结算
            logger.warn("平台订单状态异常");
            return;
        }

        ShopMemberOrder query = new ShopMemberOrder();
        query.setId(shopMemberOrderId);
        ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.selectOne(query);
        if (shopMemberOrder == null) {
            logger.warn("没有找到小程序订单");
            return;
        }

        List<ShopOrderAfterSale> shopOrderAfterSaleList = shopOrderAfterSaleMapper.listByShopMemberOrderNo(shopMemberOrderId);
        for (ShopOrderAfterSale afterSale : shopOrderAfterSaleList) {
            if (afterSale.getStatus().equals(0)) {
                logger.info("订单正在售后");
                return;
            }
        }

        //账户待结算到已结算
        boolean isSuccess = storeBusinessAccountRpcService.sendGoodsAfter15DaysWaitMoneyIn(shopMemberOrder.getOrderNumber(), storeOrder.getStoreId(), orderNo);
        if (! isSuccess) {
            logger.error("发货15天,更新账户失败!!!!");
        }
    }
}
