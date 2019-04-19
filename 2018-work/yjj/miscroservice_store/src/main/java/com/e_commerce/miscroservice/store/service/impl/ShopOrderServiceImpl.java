package com.e_commerce.miscroservice.store.service.impl;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.dao.ProductDao;
import com.e_commerce.miscroservice.store.dao.StoreOrderDao;
import com.e_commerce.miscroservice.store.dao.ShopOrderItemDao;
import com.e_commerce.miscroservice.store.entity.ExchangeType;
import com.e_commerce.miscroservice.store.entity.response.OrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.OrderDetailsVo;
import com.e_commerce.miscroservice.store.entity.vo.StoreOrder;
import com.e_commerce.miscroservice.store.service.ShopOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 售后
 * @author hyf
 * @version V1.0
 * @date 2018/9/26 17:27
 * @Copyright 玖远网络
 */
@Service
public class ShopOrderServiceImpl implements ShopOrderService {

    private Log logger = Log.getInstance(ShopOrderServiceImpl.class);
    @Autowired
    private StoreOrderDao shopOrderDao;

    @Autowired
    private ShopOrderItemDao shopOrderItemDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public Response findOrderList(Integer status, Integer type, Long userId, Long supplierId, Integer pageNum) {
            if (supplierId < 0) {
                throw new RuntimeException("供应商ID为空，请确认");
            }
            //查询所有母订单
            List<OrderListResponse> orderNews = shopOrderDao.getUserOrders(userId, status, pageNum);


            if (CollectionUtils.isEmpty(orderNews)) {
                return Response.errorMsg("温馨提示：商品退换货请联系客服，给您带来不便敬请谅解，谢谢");
            }
            //获取某个状态的用户的所有订单(order表)
            for (OrderListResponse order : orderNews) {
                //如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
                long restrictionActivityProductId = order.getRestrictionActivityProductId();
                String platformProductState = "-1";
                if (restrictionActivityProductId>0){
//                    RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);
//                    int productStatus = restrictionActivityProduct.getProductStatus();
                    //限购活动商品的状态0:待上架;1:已上架;2:已下架;3:已删除
                    //平台商品状态:0已上架、1已下架、2已删除
//                    if(productStatus==2){
//                        platformProductState = "1";
//                    }
                }
                List<OrderDetailsVo> childOrders = shopOrderDao.getChildOrders(userId,order.getOrderno());
                order.setOrderDetailsVoList(childOrders);
            }

            return Response.success(orderNews);
    }

    @Override
    public Response getOrderDetail(Long userId, Long orderNo) {
        List<OrderDetailsVo> childOrders = shopOrderDao.getChildOrders(userId,orderNo);
        StoreOrder order = shopOrderDao.getStoreOrderByOrderNo(orderNo);
        Map<String,Object> map = new HashMap<>();
        String status = ExchangeType.buildInfoRefundStatusName(order.getOrderstatus());
        //订单状态
        map.put("status",status);
//        //收货人
//        map.put("expressName",order.getExpressName());
//        //联系电话
//        map.put("expressPhone",order.getExpressPhone());
//        //收货地址
//        map.put("expressAddress",order.getExpressAddress());
//        //订单号
//        map.put("orderNo",order.getOrderno());
//        //订单详情列表
//        map.put("orderDetailList",childOrders);
//        //订单编号
//        map.put("name","");
//        //交易编号
//        map.put("name","");
//        //创建时间
//        map.put("name","");
//        //成交时间
//        map.put("name","");
//        //订单编号
//        map.put("name","");
        map.put("orderDetailList",childOrders);
        map.put("order",order);
        return Response.success(map);
    }
}
