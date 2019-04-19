package com.e_commerce.miscroservice.supplier.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.*;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.supplier.entity.request.Query;
import com.e_commerce.miscroservice.supplier.entity.request.RefundGoodsMoneyRequest;
import com.e_commerce.miscroservice.supplier.entity.request.StoreOrderNew;
import com.e_commerce.miscroservice.supplier.entity.request.StoreRefundOrderActionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RefundOrderMapper {
    /**
     * 查询所有售后订单
     * @param request
     * @return
     */
    List<RefundOrderResponce> findAllRefunOrder(@Param("request") RefundOrderRequest request);

    List<StoreOrderItem> selectProductIdByOrderNo(@Param("orderNo")Long orderNo);

    Product selectNameById(@Param("id")Long id);

    List<Product> selectByName(@Param("name")String name);
    List<StoreOrderItem> selectItemByProductId(@Param("productId")Long productId);
    /**
     * 查询售后订单详情
     */
    RefundOrder getRefundOrderInfo(Long refundStoreId);

    StoreOrder selectPayTypeAndTotalMoney(Long orderNo);
    /**
     * 根据供应商id查询供应商收货地址列表
     */
    List<SupplierDeliveryAddress> selectListBySupplierId(Long supplierId);
    /**
     *根据productId查询信息
     */
    ProductNew selectProdectById(Long productId);

    /**
     * 根据skuid和订单号查询商品信息
     */
    StoreOrderItem selectItem(@Param("orderNo") Long orderNo,@Param("skuId") Long skuId);
    /**
     * 售后处理 退货退款
     *
     * @param obj
     * @return
     */
    Integer refundGoodsMoney(@Param("obj") RefundGoodsMoneyRequest obj);

    /**
     * 确认收货
     * @param id
     * @param code
     * @return
     */
    Integer confirmTackGoods(@Param("id") Long id, @Param("code") Integer code);

    /**
     * 根据id查询售后单号
     */
    Long selectRefundNo(Long id);

    /**
     * 插入日志
     * @param storeRefundOrderActionLog
     */
    void insertLog(StoreRefundOrderActionLog storeRefundOrderActionLog);

    /**
     * 更改地址
     */
    void updateAdress(@Param("obj") RefundGoodsMoneyRequest obj);


    RefundOrder selectRefundOrder(Long id);

    StoreOrder selectStoreOrder(Long orderNo);

    StoreOrderNew selectStoreOrderNew(@Param("query") Query query);

    /**
     * 根据售后订单查询 用户
     * @param id
     */
    StoreBusiness findRefundUser(@Param("id") Long id);
    /**
     * 查询用户待结算金额
     */
    YjjStoreBusinessAccount selectMoney(@Param("storeId") Long storeId);

    RefundOrder selectRefundByOrderNew(@Param("orderNo")Long orderNo);

    int updateOrderStatus(@Param("orderStatus")Integer orderStatus,@Param("orderNo")Long orderNo);

    RefundOrder selectRefundNew(@Param("id")Long id);

    Integer selectSupplierName(@Param("name")String name);
}
