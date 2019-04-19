package com.e_commerce.miscroservice.store.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.StoreOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.StoreOrderItem;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.store.entity.vo.ShopMemberOrder;
import com.e_commerce.miscroservice.store.entity.vo.ShopMemberOrderItem;
import com.e_commerce.miscroservice.store.entity.vo.StoreOrderItemNew;
import com.e_commerce.miscroservice.store.entity.vo.StoreOrderNew;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FictitiousMapper {

    /**
     * 查询用户待结算金额
     */
     YjjStoreBusinessAccount selectMoney(@Param("storeId") Long storeId);
    /**
     * 根据订单号查询订单信息
     */
     List<StoreOrderNew> selectOrderByOrderNo(@Param("orderNo")Long orderNo);

    /**
     * 更新待结算资金
     * @param money
     * @param storeId
     * @return
     */
     int updateyjjStoreBusinessAccount(@Param("money")Double money,@Param("totalMoney")Double totalMoney, @Param("storeId")Long storeId);

    /**
     * 更新订单信息
     * @param storeOrderNew
     * @return
     */
     int updateOrder(@Param("storeOrderNew")StoreOrderNew storeOrderNew);


    /**
     * 查询门店名称
     * @param storeId
     * @return
     */
     String selectName(@Param("storeId")Long storeId);

    /**
     * 根据parentId查询信息
     */
    List<StoreOrderNew> selectOrderByParentId(Long orderNo);

    /**
     * 很据storeId查询是否是店中店用户
     */
    Integer selectTypeBystoreId(@Param("storeId") Long storeId);

    /**
     * 根据订单号和storeId查询订单详情
     */
    List<StoreOrderItem> selectOrderItem(@Param("storeId") Long storeId,@Param("orderNo")Long orderNo);
    /**
     * 根据订单号和storeId查询订单
     */
//    List<StoreOrderNew> selectOrder(@Param("storeId") Long storeId,@Param("orderNo")Long orderNo);
    /**
     * 根据storeId查询商品订单
     */
    List<ShopMemberOrder> selectMemberOrder(@Param("storeId")Long storeId);

    /**
     * 根据订单号查询商品的详细信息
     */
    List<ShopMemberOrderItem>selectMemberOrderItem(@Param("orderNumber")String orderNumber,@Param("skuId")Long skuId);

}
