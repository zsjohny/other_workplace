/**
 *
 */
package com.store.service;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.entity.ExpressInfo;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.IOrderNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.util.DateUtil;
import com.store.dao.mapper.OrderItemMapper;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderItemNewVO;
import com.store.entity.ShopStoreOrderVo;

/**
 * @author LWS
 */
@Service
public class OrderListDelegator{
    private static final Logger logger = LoggerFactory.getLogger("PAYMENT");
    @Autowired
    private IProductNewService productNewService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderListFacade orderListFacade;
    @Autowired
    private ExpressInfoService expressInfoService;
    @Autowired
    private IOrderNewService orderNewService;


    @Autowired
    private RestrictionActivityProductMapper restrictionActivityProductMapper;


    /**
     * 从新订单表中取列表
     *
     * @param orderStatus
     * @param pageQuery
     * @param userDetail
     * @return
     */
    public Map<String, Object> getNewOrderList(OrderStatus orderStatus, PageQuery pageQuery,
                                               UserDetail<StoreBusiness> userDetail,Long userId) {
        //long userId = userDetail.getId();
        long time1 = System.currentTimeMillis();
        /*long supplierId = userDetail.getUserDetail().getSupplierId();
        if (supplierId < 0) {
            throw new RuntimeException("供应商ID为空，请确认");
        }*/
        int totalCount = 0;
        List<ShopStoreOrder> orderNews = null;
        PageQueryResult pageQueryResult;
        Map<String, Object> result = new Hashtable<> ();
        Set<Long> orderNOs = new HashSet<Long>();
        Map<Long, List<ShopStoreOrderItemNewVO>> orderItemsMap;
        List<ShopStoreOrderVo> resultOrderList2 = new ArrayList<> ();
        result.put("tip", "温馨提示：商品退换货请联系客服，给您带来不便敬请谅解，谢谢");
        long time2 = System.currentTimeMillis();
        logger.info("time2：" + (time2 - time1));

        long time10 = System.currentTimeMillis();
        //取某状态订单，不包含母订单
        totalCount = orderService.getUserNewOrderCountWithoutParent(userDetail.getId(), orderStatus.getIntValue());
        orderNews = orderService.getUserOrdersNewWithoutParent(userDetail.getId(), orderStatus.getIntValue(), pageQuery);

        /**
         * 判断改商家是否是店中店用户
         */
        long storeId = userDetail.getId();
        Integer type=orderService.isDdStore(storeId);
        /**
         * 判断是不是平台同款
         */




        long time11 = System.currentTimeMillis();
        logger.info("time11：" + (time11 - time10));
        pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        if (CollectionUtils.isEmpty(orderNews)) {
            return result;
        }

        //获取某个状态的用户的所有订单(order表)
        for (ShopStoreOrder order : orderNews) {
            orderNOs.add(order.getOrderNo());
        }
        orderItemsMap = orderListFacade.getOrderNewItemVOMap(userDetail, orderNOs,userDetail.getId());
        long time12 = System.currentTimeMillis();
        logger.info("time12：" + (time12 - time11));

        for (ShopStoreOrder orderObj : orderNews) {

            ShopStoreOrderVo orderVo = buildShopStoreOrderVo(orderObj);
            long orderNo = orderObj.getOrderNo();
//            /**
//             * 根据订单查询是否是平台商品
//             */
//            Integer own=orderService.selectOwn(orderNo);
//            if (type==1&&own==0){
//                //显示平台代发按钮
//                orderVo.setType("1");
//            }else {
//                orderVo.setType("0");
//            }
            /**
             * 显示平台代发四个字
             */
            //根据订单号到商家订单里面去查询
//            orderService.selectIsAppOrder(orderObj.getOrderNo());
            List<ShopStoreOrderItemNewVO> orderItems = orderItemsMap.get(orderObj.getOrderNo());
            //是否启用确认收货按钮
            boolean disableConfirmationReceipt = false;
            //如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
            long restrictionActivityProductId = orderObj.getRestriction_activity_product_id();
            String platformProductState = "-1";
            if (restrictionActivityProductId > 0) {
                RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);
                int productStatus = restrictionActivityProduct.getProductStatus();
                //限购活动商品的状态0:待上架;1:已上架;2:已下架;3:已删除
                //平台商品状态:0已上架、1已下架、2已删除
                switch (productStatus) {
                    case 1:
                        platformProductState = "0";
                        break;
                    case 2:
                        platformProductState = "1";
                        break;
                    case 3:
                        platformProductState = "2";
                        break;
                }
            }

            if (orderItems != null && orderItems.size() > 0) {
                int count = 0;
                for (ShopStoreOrderItemNewVO orderItem : orderItems) {

                    //如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
                    if (! "-1".equals(platformProductState)) {
                        orderItem.setPlatformProductState(platformProductState);
                    }
                    count += orderItem.getBuyCount();
                }
                orderVo.setTotalBuyCount(count);

                //分别获取订单sku状态
                Map<String, String> refundOrderMap = orderService.getRefundOrderStatus(orderObj.getOrderNo(), orderObj.getOrderStatus());
                String refundOrderStatus = refundOrderMap.get("refundOrderStatus");
                if ("售后中".equals(refundOrderStatus)) {
                    disableConfirmationReceipt = true;
                }
                orderVo.setOrderItemStatus(refundOrderStatus);
                orderVo.setRefundOrderId(refundOrderMap.get("refundOrderId"));
                if ("申请退款".equals(refundOrderStatus) || "申请售后".equals(refundOrderStatus)) {
                    //有售后按钮
                    orderVo.setIsApplyAfterSaleButton(ShopStoreOrder.applyAfterSaleButton);
                } else {
                    //无售后按钮
                    orderVo.setIsApplyAfterSaleButton(ShopStoreOrder.unApplyAfterSaleButton);
                }

                //设置是否启用确认收货按钮
                orderVo.setDisableConfirmationReceipt(disableConfirmationReceipt);

                //获取未售后时自动确认收货倒计时毫秒值
                long payTime = orderObj.getPayTime();
                if (payTime > 0 && orderObj.getOrderStatus() == OrderStatus.DELIVER.getIntValue()) {
                    long autoConfirmTime = payTime + 14 * 24 * 60 * 60 * 1000;
                    orderVo.setAutoConfirmTime(autoConfirmTime);
                }
                //获取申请售后时自动确认收货暂停倒计时
                if (orderObj.getRefund_start_time() > 0) {
                    long buildSurplusSupplierAutoTakeTime = orderNewService.buildSurplusSupplierAutoTakeTime(orderObj.getSendTime(),
                            orderObj.getRefund_start_time(), orderObj.getAuto_take_delivery_pause_time_length());
                    orderVo.setAutoConfirmTimeString(DateUtil.formatDuring(buildSurplusSupplierAutoTakeTime));
                }
            }
            if (orderItems != null) {
                orderVo.setOrderItemsNew(new ArrayList<ShopStoreOrderItemNewVO>(orderItems));
            }

            ExpressInfo expressInfo = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getId(), orderObj.getOrderNo());

            if (expressInfo == null) {
                orderVo.setExpressOrderNo("");
                orderVo.setExpressCnName("");
            } else {
                //根据快递公司英文名获取对应的中文名
                String expressCnName = expressInfoService.getExpressChineseNameByExpressSupplier(expressInfo.getExpressSupplier());
                orderVo.setExpressOrderNo(expressInfo.getExpressOrderNo());
                orderVo.setExpressCnName(expressCnName);
            }

            resultOrderList2.add(orderVo);

        }
        long time13 = System.currentTimeMillis();
        logger.info("time13：" + (time13 - time12));


        result.put("orderlist", resultOrderList2);

        result.put("pageQuery", pageQueryResult);


        List<String> cancelReasonList = new ArrayList<String>();
        cancelReasonList.add("我不想买了");
        cancelReasonList.add("信息填写错误");
        cancelReasonList.add("其它");
        result.put("cancelReasonList", cancelReasonList);

        return result;
    }


    private ShopStoreOrderVo buildShopStoreOrderVo(ShopStoreOrder orderObj) {
        ShopStoreOrderVo orderVo = new ShopStoreOrderVo();
        orderVo.setOrderNo(orderObj.getOrderNo());
        orderVo.setOrderStatus(orderObj.getOrderStatus());
//		orderVo.setStatus(orderObj.getStatus());
        orderVo.setCreateTime(DateUtil.format(orderObj.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        orderVo.setUpdateTime(DateUtil.format(orderObj.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
        orderVo.setExpiredTime(orderObj.getExpiredTime());
        orderVo.setParentId(orderObj.getParentId());
        orderVo.setRestriction_activity_product_id(orderObj.getRestriction_activity_product_id());
        orderVo.setMatchWholesaleLimit(orderObj.getMatchWholesaleLimit());
        orderVo.setTotalPay(orderObj.getTotalPay());
        orderVo.setTotalMoney(orderObj.getTotalMoney());
        orderVo.setTotalExpressMoney(orderObj.getTotalExpressMoney());
        return orderVo;
    }


    /**
     * 模糊查询用户历史订单
     *
     * @param userDetail
     * @param keyword    过滤查询关键字
     * @param pageQuery  分页参数
     * @return Map
     * @auther Charlie(唐静)
     * @date 2018/5/28 17:29
     */
    public Map<String, Object> listSearch(UserDetail<StoreBusiness> userDetail, String keyword, PageQuery pageQuery) {
        long userId = userDetail.getId();

        Map<String, Object> result = new Hashtable<>();
        result.put("tip", "温馨提示：商品退换货请联系客服，给您带来不便敬请谅解，谢谢");


        /* 订单信息 : 根据关键字过滤查询订单信息 */
        long time10 = System.currentTimeMillis();
        int totalCount = orderService.countByKeyword(userId, keyword);
        // 总记录为空 return
        if (0 == totalCount) {
            result.put("orderlist", new ArrayList<>(0));
            result.put("pageQuery", PageQueryResult.copyFrom(pageQuery, 0));
            return result;
        }
        List<ShopStoreOrder> orderNews = orderService.findByKeyword(userId, pageQuery, keyword);
        long time11 = System.currentTimeMillis();
        logger.info("订单关键字查询耗时-1：" + (time11 - time10));


        // 分页结果是空,return
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        if (orderNews.isEmpty()) {
            result.put("orderlist",  new ArrayList<>(0));
            result.put("pageQuery", pageQueryResult);
            return result;
        }


        /* 获取某个状态的用户的所有订单(order表) */
        Set<Long> orderNoSet = new HashSet<>(orderNews.size());
        for (ShopStoreOrder order : orderNews) {
            orderNoSet.add(order.getOrderNo());
        }
        //OrderItems
        Map<Long, List<ShopStoreOrderItemNewVO>> orderItemsMap = orderListFacade.getOrderNewItemVOMap(userDetail, orderNoSet,userId);

        long time12 = System.currentTimeMillis();
        logger.info("time12：" + (time12 - time11));



        /* 组装返回订单信息 : 活动商品, 快递信息, 订单状态 */
        List<ShopStoreOrderVo> orderVOList = buildShopStoreOrderVos(userId, orderNews, orderItemsMap);
        long time13 = System.currentTimeMillis();
        logger.info("time13：" + (time13 - time12));

        result.put("orderlist", orderVOList);
        result.put("pageQuery", pageQueryResult);
        return result;
    }




    /**
     * 组装返回订单信息
     * @param userId
     * @param orderNews
     * @param orderItemsMap
     * @return java.util.List<com.store.entity.ShopStoreOrderVo>
     * @auther Charlie(唐静)
     * @date 2018/5/29 16:26
     * @reference {@link com.store.service.OrderListDelegator#getNewOrderList}
     */
    public List<ShopStoreOrderVo> buildShopStoreOrderVos(long userId, List<ShopStoreOrder> orderNews, Map<Long, List<ShopStoreOrderItemNewVO>> orderItemsMap) {
        if (null == orderNews || orderNews.isEmpty()) {
            return new ArrayList<>();
        }

        List<ShopStoreOrderVo> orderVOList = new ArrayList<>(orderNews.size());


        for (ShopStoreOrder orderObj : orderNews) {
            ShopStoreOrderVo orderVo = buildShopStoreOrderVo(orderObj);
            List<ShopStoreOrderItemNewVO> orderItems = orderItemsMap.get(orderObj.getOrderNo());
            //是否启用确认收货按钮
            boolean disableConfirmationReceipt = false;
            //如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
            long restrictionActivityProductId = orderObj.getRestriction_activity_product_id();
            String platformProductState = "-1";
            if (restrictionActivityProductId > 0) {
                RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);
                int productStatus = restrictionActivityProduct.getProductStatus();
                //限购活动商品的状态0:待上架;1:已上架;2:已下架;3:已删除  平台商品状态:0已上架、1已下架、2已删除
                switch (productStatus) {
                    case 1:
                        platformProductState = "0";
                        break;
                    case 2:
                        platformProductState = "1";
                        break;
                    case 3:
                        platformProductState = "2";
                        break;
                }
            }

            if (orderItems != null && orderItems.size() > 0) {
                int count = 0;
                for (ShopStoreOrderItemNewVO orderItem : orderItems) {

                    //如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
                    if (! "-1".equals(platformProductState)) {
                        orderItem.setPlatformProductState(platformProductState);
                    }
                    count += orderItem.getBuyCount();
                }
                orderVo.setTotalBuyCount(count);

                //分别获取订单sku状态
                Map<String, String> refundOrderMap = orderService.getRefundOrderStatus(orderObj.getOrderNo(), orderObj.getOrderStatus());
                String refundOrderStatus = refundOrderMap.get("refundOrderStatus");
                if ("售后中".equals(refundOrderStatus)) {
                    disableConfirmationReceipt = true;
                }
                orderVo.setOrderItemStatus(refundOrderStatus);
                orderVo.setRefundOrderId(refundOrderMap.get("refundOrderId"));
                if ("申请退款".equals(refundOrderStatus) || "申请售后".equals(refundOrderStatus)) {
                    //有售后按钮
                    orderVo.setIsApplyAfterSaleButton(ShopStoreOrder.applyAfterSaleButton);
                } else {
                    //无售后按钮
                    orderVo.setIsApplyAfterSaleButton(ShopStoreOrder.unApplyAfterSaleButton);
                }

                //设置是否启用确认收货按钮
                orderVo.setDisableConfirmationReceipt(disableConfirmationReceipt);

                //获取未售后时自动确认收货倒计时毫秒值
                long payTime = orderObj.getPayTime();
                if (payTime > 0 && orderObj.getOrderStatus() == OrderStatus.DELIVER.getIntValue()) {
                    long autoConfirmTime = payTime + 14 * 24 * 60 * 60 * 1000;
                    orderVo.setAutoConfirmTime(autoConfirmTime);
                }
                //获取申请售后时自动确认收货暂停倒计时
                if (orderObj.getRefund_start_time() > 0) {
                    long buildSurplusSupplierAutoTakeTime = orderNewService.buildSurplusSupplierAutoTakeTime(orderObj.getSendTime(),
                            orderObj.getRefund_start_time(), orderObj.getAuto_take_delivery_pause_time_length());
                    orderVo.setAutoConfirmTimeString(DateUtil.formatDuring(buildSurplusSupplierAutoTakeTime));
                }
            }
            if (orderItems != null) {
                orderVo.setOrderItemsNew(new ArrayList<>(orderItems));
            }

            ExpressInfo expressInfo = expressInfoService.getUserExpressInfoByOrderNo(userId, orderObj.getOrderNo());

            if (expressInfo == null) {
                orderVo.setExpressOrderNo("");
                orderVo.setExpressCnName("");
            } else {
                //根据快递公司英文名获取对应的中文名
                String expressCnName = expressInfoService.getExpressChineseNameByExpressSupplier(expressInfo.getExpressSupplier());
                orderVo.setExpressOrderNo(expressInfo.getExpressOrderNo());
                orderVo.setExpressCnName(expressCnName);
            }

            orderVOList.add(orderVo);

        }
        return orderVOList;
    }
}