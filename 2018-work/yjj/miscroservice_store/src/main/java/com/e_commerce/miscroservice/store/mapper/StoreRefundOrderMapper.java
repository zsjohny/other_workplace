package com.e_commerce.miscroservice.store.mapper;



import com.e_commerce.miscroservice.store.entity.response.RefundOrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 售后
 */
@Mapper
public interface StoreRefundOrderMapper {

    /**
     *  根据用户id售后列表
     *  @param userId
     * @return
     */
    List<RefundOrderListResponse> findRefundOrderListByUserId(@Param("userId") Long userId);

    /**
     *根据productId查询信息
     */
   ProductNew selectProdectById(Long productId);

    /**
     * 根据sku和订单号查询商品详情
     */
    StoreRefundOrder selectRefundOrder(@Param("orderNo") Long orderNo, @Param("skuId") Long skuId);

    /**
     * 根据订单号查询信息
     */
    StoreOrderNew selectByOrderNo(Long orderNo);

    /**
     * 根据商品的订单号和用户id查询商品详情信息
     */
    List<StoreOrderItemNew> getOrderNewItemsByOrderNO(@Param("storeId") Long storeId, @Param("orderNo") Long orderNo);


    List<ShopStoreAuthReason> selectList(Integer type);

    /**
     * 生成订单
     */
    int insertRefundOrder(RefundOrderNewL  refundOrderNewL);

    /**
     * 根据售后订单号查询订单信息
     */
    StoreRefundOrder selectRefundByRefundOrder(Long refundOrderNo);

    /**
     * 根据订单信息更改订单
     */
    void updateByRefundOrder(StoreRefundOrder storeRefundOrder);

    /**
     * 根据订单状态
     */
    void updateById(StoreOrderNew storeOrderNew);

    /**
     * 插入日志
     */
    void insertLog(StoreRefundOrderActionLog storeRefundOrderActionLog);

    /**
     * 根据订单号和skuid查询订单详情
     */
    StoreOrderItemNew selectOrderItem(@Param("orderNo") Long orderNo, @Param("skuId") Long skuId);

    OrderNewLog selectOrderLogByOrderNoAndStatus(@Param("orderNo") long orderNo, @Param("orderStatus") int orderStatus);

 /**
  *
  */
  ExpressInfo getUserExpressInfoByOrderNo(Long orderNo);


 /**
  * 查看该订单是否在售后中
  */
 List<StoreRefundOrder> selectByOrderOrderNo(Long orderNo);
 //ExpressInfo getUserExpressInfoByOrderNo();

 /**
  * 根据订单编号查询售后订单详情
  */
 StoreRefundOrder selectDetail(Long refundOrder);

 /**
  * 查看订单交易物流
  */
 ExpressSupplier selectListExpressInfo(String EngName);
 /**

  */


 /**
  * getBrandByBrandId
  */
 String getBrandByBrandId(Long brandId);

 /**
  * getRefundOrderActionLogList
  */
 List<StoreRefundOrderActionLog>  getRefundOrderActionLogList(Long refundOrderNo);

 /**
  * 删除售后单
  */
 void deleteRefundOrderNo(@Param("orderNo") Long orderNo,@Param("skuId") Long skuId);

 /**
  * 查找物流公司
  */
 List<ExpressSupplier> getAllExpressCompanyNames();

 /**
  * 修改订单状态
  */
 void updateIsRefundOrder(Long orderNo);
}
