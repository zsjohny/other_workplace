package com.store.dao.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.order.ShopMemberOrderItem;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.entity.newentity.ShopMemberOrder;

/**
 * <p>
  * 会员订单表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-05
 */
@DBMaster
public interface ShopMemberOrderMapper extends BaseMapper<ShopMemberOrder> {

	Map<String,Object> selectOrderType(@Param(value="memberId")Long memberId, @Param(value="storeId")Long storeId);



    /**
     * 根据状态查询订单
     *
     * @param page page
     * @param storeId storeId
     * @param orderStatus orderStatus
     * @param memberId memberId
     * @return java.util.List<com.jiuyuan.entity.newentity.ShopMemberOrder>
     * @author Charlie
     * @date 2018/8/24 10:37
     */
    List<ShopMemberOrder> listByOrderStatus(
            @Param( "page" ) Page<ShopMemberOrder> page,
            @Param( "storeId" ) Long storeId,
            @Param( "orderStatus" ) Integer orderStatus,
            @Param( "memberId" ) Long memberId);


    /**
     * 根据状态查询订单总数
     *
     * @param storeId storeId
     * @param orderStatus orderStatus
     * @param memberId memberId
     * @return java.util.List<com.jiuyuan.entity.newentity.ShopMemberOrder>
     * @author Charlie
     * @date 2018/8/24 10:37
     */
    int countByOrderStatus(@Param( "storeId" ) Long storeId,
                           @Param( "orderStatus" ) Integer orderStatus,
                           @Param( "memberId" ) Long memberId);

    /**
     * 查询小程序订单详情
     * @param orderId
     * @return
     */
    ShopMemberOrder findWxaOrderDetailById(@Param("orderId") long orderId);

    ShopMemberOrder findInformationById(@Param("orderId") long orderId);

    /**
     * 收藏订单列表
     * @param orderId
     * @return
     */
    List<ShopMemberOrderItem> findProductListByOrderId(@Param("orderId") long orderId);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    Integer findShopMemberOrderStatus(@Param("orderId") long orderId);

    Integer selectType(@Param("storeId") Long storeId);

    List<Integer> selectIsSupplierPro(@Param("orderId") Long orderId);
}