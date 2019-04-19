package com.e_commerce.miscroservice.supplier.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.*;
import com.e_commerce.miscroservice.supplier.entity.request.StoreRefundOrderActionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderCountMapper {
    /**
     * 根据商家id查询订单个数
     */
    Integer selectCount(Long supplierId);


    /**
     * 更新订单
     */
    void updateStore(StoreOrder storeOrder);
    /**
     * 根据商家id查询今日订单
     */
    Integer todayCount(TodayCount todayCount);

    /**
     * 获取为待处理的订单个数
     */
    Integer unDealWithCount(Long supplierId);
    /**
     * 获取售后中订单的数量
     */
    Integer  unDealRefundOrderCount(Long supplierId);

    /**
     * 根据订单号查询商品的详情信息
     */
    StoreOrder selectInfo(Long orderNo);

    /**
     * 根据用户id查询号码
     */
    public String selectById(Long storeId);
    /**
     * 根据phoneNumber,storeId,supplierId查询信息
     * @param phoneNumber
     * @param storeId
     * @param supplierId
     * @return
     */
    List<SupplierCustomer> getCustomerByStoreIdOrPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("storeId") Long storeId, @Param("supplierId") Long supplierId);


    /**
     * 是否有售后：0没有售后、1有售后
     * @param orderNo
     * @return
     */
     Integer getHaveRefund(Long orderNo);

    /**
     * 根据用户id查询商家店铺信息
     * @param orderNo
     * @return
     */
     String selectBusinessNameById(Long orderNo);

    /**
     * 根据订单编号查询物流信息
     */
    StoreExpressInfo selectOne(Long orderNo);

    /**
     * 根据快递名称查询快递商家信息
     */
    List<ExpressSupplier> selectList(String EngName);

    /**
     * 根据订单号查询该订单商品集合(包括商品的详细信息)
     */
    List<StoreOrderItem> selectItemList(Long orderNo);

    /**
     * 根据商品的id查询商品信息
     */
    Product selectProdectById(Long productId);
    /**
     * 获取邮递公司名称列表用于数据回显
     */
    List<ExpressSupplier> getAllExpressCompanyNames();

    /**
     * 根据订单号查询售后信息
     * @param orderNo
     * @return
     */
    List<RefundOrder> getRefundItemList(Long orderNo);

    /**
     * 根据订单号查询该订单是否在售后中
     * `refund_underway` tinyint(4) DEFAULT '0' COMMENT '是否是售后进行中：0(否)、1(是)',
     */
    StoreOrder selectByOrderNo(Long orderNo);

    /**
     * 根据订单号查询订单的状态
     */
    StoreOrder selectOrderStatus(Long orderNo);
    /**
     * 查看订单是状态是否支持分批发货
     */
    StoreOrder selectParentId(Long orderNo);


    /**
     * 将物流信息插入表内
     */
    void insert(StoreExpressInfo storeExpressInfo);

    /**
     *将订单信息更新
     */
    void updateById(StoreOrder storeOrder);

    /**
     * 插入日志
     */
    Integer insertLog(StoreOrderLog storeOrderLog);


    void updateOrderNoAttachmentStr(StoreOrder storeOrder);

    /**
     * 根据ParentId查询信息
     */
    StoreOrder selectParentId(StoreOrder storeOrder);
    /**
     * 根据skuid和订单号查询商详情品信息
     */
    StoreOrderItem selectBySkuId(@Param("skuId") Long skuId, @Param("orderNo") Long orderNo);


    /**
     * 根据skuid和订单号更新商品信息
     */
    void updateSotrePay(StoreOrderItem storeOrderItem);

    /**
     * 根据订单号更新订单信息
     */
     void updateParentId(StoreOrder storeOrder);

    /**
     * 生成新的订单并返回订单号
     */
    Long insertNewOrderNo(StoreOrder storeOrder);


    /**
     * 根据OrderNo和SkuId 对商品详情表进行更新
     */
    void updateItem(StoreOrderItem storeOrderItem);


    /**
     * 根据OrderNo对商品详情表进行插入
     */
    void insertItem(StoreOrderItem storeOrderItem);


    /**
     * 更新新生成的订单
     */
    void updateTotalMoney(StoreOrder storeOrder);

    /**
     * 根据storeId查询商家信息
     */
    StoreBusiness selectByTtoreId(Long storeId);

    /**
     * 根据商家id查询用户信息
     */
    UserNew SelectBySupplierId(Long supplierId);

    /**
     * 根据订单号和关联品牌id查询信息
     */

    /**
     * 根据订单号查询物流信息
     */
    List<StoreExpressInfo> selectExpressInfo(Long orderNo);

    /**
     * 恢复价格
     */
    void changePriceByOrderNo(@Param("orderNo") long orderNo, @Param("changePrice") double changePrice, @Param("parentId") long parentId);

    /**
     * 更新母订单为发货中
     */
     void updateParentIdStatus(Long parentId);


    /**
     * 查询售后订单
     */
    RefundOrder selectRefundOrder(Long orderNo);

    /**
     * 更改母订单为已发货
     */
    void updateStatus(Long parentId);

    /**
     * 获取所有的子订单
     */
    List<StoreOrder> selectOrderSonAll(Long orderNo);

    List<Long> selectByParentIdNew(@Param("orderNo") Long orderNo);

    List<StoreRefundOrderActionLog> selectRefundLog(@Param("orderNo") Long orderNo);
}
