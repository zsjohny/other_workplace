package com.e_commerce.miscroservice.store.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.store.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface WXOrderMapper {
    /**
     * 查询所有的列表
     * @return
     */
    List<ShopMemberOrderResponse> getRefundOrderList(@Param("request") ShopMemberOrderRequest request);
    /**
     * 商家查询订单列表
     */
    List<ShopMemberOrderResponse>getRefundOrderListNew(@Param("request") ShopMemberOrderRequest request);
    /**
     * 根据订单号查询订单详情
     */
    List<ShopMemberOrderItem> selectByOrderNo(@Param("orderNo") Long orderNo);
    /**
     * 根据订单号查询订单详情
     */
    List<ShopMemberOrderItemResponse> selectByOrderId(Long orderNo);
    /**
     * 根据订单号和skuid查询售后商品
     */
    ShopOrderAfterSale selectRefund(@Param("orderNo") String orderNo,@Param("skuId") Long skuId);
    ShopOrderAfterSale selectRefundNew(@Param("orderNo") String orderNo,@Param("skuId") Long skuId);

    /**
     * 根据订单号和skuid查询订单详情
     */
    ShopMemberOrderItem selectItem(@Param("orderId")Long orderId,@Param("skuId")Long skuId);
    ShopMemberOrderItem selectItemNew(@Param("orderId")Long orderId,@Param("skuId")Long skuId);
    /**
     * 根据订单查询商品
     */
    ShopMemberOrder selectOrder(Long orderId);

    /**
     * 更改订单状态
     */
    void updateOrder(Long orderId);
    /**
     * 插入售后订单
     */
    void insertRefundOrder(ShopOrderAfterSale refund);

    /**
     * 查询所有售后订单列表
     */
    List<RefundResponse> selectRefundList(@Param("request") RefundRquest request);

    /**
     * 根据售后订单号查询详情
     */
    ShopOrderAfterSale selectRefundItem(@Param("afterSaleId") Long afterSaleId,@Param("userId") Long userId);

    /**
     * 查询订单详情
     */
    ShopMemberOrder selectOrderItem(@Param("id") Long id,@Param("userId") Long userId);


    /**
     * 我的售后订单列表
     */
    List<RefundResponse> selectMyRefundList(@Param("request")RefundRquest request);


    /**
     * 根据售后订单号查询订单详情
     */
    ShopOrderAfterSale selectRufund(@Param("afterSaleId") String afterSaleId,@Param("userId") Long userId);


    /**
     *
     */
    ShopOrderAfterSale selectRefundNew(@Param("afterSaleId") Long afterSaleId,@Param("storeId")Long storeId);
    /**
     * 更新售后订单
     */
    void updateRefoundOrder(@Param("shopOrderAfterSale") ShopOrderAfterSale shopOrderAfterSale);

    /**
     * 查找商家信息
     */
    ArrayList<Long> selectStorePhone(Long storeId);

    /**
     * 更改订单信息
     */
    void updateOrderNew(@Param("id")Long id,@Param("userId")Long userId);


    /**
     * 查询团购人数
     */
    TeamBuyActivity selectById(@Param("teamId") Long teatmId);

    /**
     * 查询时间
     */
    SecondBuyActivity selectBySId(@Param("secondId") Long secondId);

    /**
     * 根据订单编号和商家id查询订单详情
     */
    ShopOrderAfterSale selectByStoreId(@Param("refundOrderNo") Long refundOrderNo,@Param("storeId") Long storeId);

    /**
     * 更改售后订单信息
     */
    int updateRefundOrder(@Param("request")ShopOrderAfterSale request);

    /**
     * 删除订单
     */
    void deleteOrderMy(@Param("refundOrderNo")String refundOrderNo,@Param("storeId")Long storeId);

    /**
     * 获取默认地址
     */
    List<Address> selecrMyAdress(Integer storeId);

    /**
     * 收货成功
     */
    void refoundOrder(@Param("refundOrderNo")Long refundOrderNo,@Param("storeId")Long storeId);

    /**
     * 更新日志
     */
    void updateLog(@Param("log") StoreRefundOrderActionLog log);

    /**
     * 查找日志记录
     */
    List<StoreRefundOrderActionLog> selectLog(Long refundOrderNo);

    /**
     * 删除已存在的售后订单
     */
    void deleteRefundOrder(@Param("orderId") Long orderId,@Param("skuId") Long skuId);

    /**
     * 删除用户售后订单
     */
    void deleteRefundOrderNew(@Param("afterSaleId")Long afterSaleId,@Param("userId")Long userId);
    /**
     * 查询售后订单列表
     */
    List<RefundResponse> selectNewRefund(@Param("request")RefundRquest request);

    /**
     * 查询商家地址
     */
    StoreBusiness selectAdress(Long storeId);

    SecondBuyActivity selectActive(Long secondId);
    SecondBuyActivity selectActiveNew(Long secondId);
    TeamBuyActivity selectTeam(Long teanId);
    TeamBuyActivity selectTeamNew(Long teanId);
    /**
     *
     */

    StoreBusiness getStoreBusinessById(Long storeId);
    StoreBusiness getStoreBusinessByIdNew(Long storeId);

    ShopMember getMemberById(Long memberId);
    ShopMember getMemberByIdNew(Long memberId);

    /**
     * 查询小程序
     */
       List<StoreWxa> selectStoreWxaList(Long storeId);
       List<StoreWxa> selectStoreWxaListNew(Long storeId);
    /**
     * 修改数据
     */
     int updateById(@Param("shopMemberOrder") ShopMemberOrder shopMemberOrder);


    /**
     * 根据memeberId查询会员信息
     */
    ShopMember selectShopMember(Long memberId);
    ShopMember selectShopMemberNew(Long memberId);

    /**
     * 根据售后单号查询该订单号
     */
    Long selectOrderByRefundOrder(@Param("refundOrderNo") Long refundOrderNo);
    /**
     * 根据订单号查询信息
     */
    StoreOrderNew selectByOrderNoNewF(@Param("orderNo") Long orderNo);
    /**
     * 根据sku和订单号查询商品详情
     */
    StoreRefundOrder selectRefundOrder(@Param("orderNo") Long orderNo);

    /**
     * 删除售后单
     */
    void deleteRefundOrderNo(@Param("orderNo") Long orderNo);

    Integer selectMemberOrderById(Long wxOrderIdNew);

    Long selectAppOrderId(@Param("orderId")Long orderId);

    YjjStoreBusinessAccount selectBusinessAccount(@Param("storeId") Long storeId);

    String selectName(@Param("memberId") Long memberId);

    String selectOrderNumber(@Param("orderId") String orderId);

    Long selectCreateTime(@Param("wxOrderId") String wxOrderId);
}
