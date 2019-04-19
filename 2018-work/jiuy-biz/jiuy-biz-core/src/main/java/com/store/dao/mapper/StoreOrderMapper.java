package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.ShopStoreOrder;

/**
 * @author jeff.zhan
 * @version 2016年11月29日 下午8:47:23
 * 
 */

@DBMaster
public interface StoreOrderMapper {
    
    int getUserOrderCountForFirstDiscount(@Param("userId") long userId);

	int insertOrder(ShopStoreOrder orderNew);

	ShopStoreOrder getUserOrderByNoOnly(@Param("orderNo") String orderNo);

	ShopStoreOrder getByOrderNo(@Param("orderNo") long orderNo);

	int updateOrderParentId(@Param("orderNo") long orderNo, @Param("parentId") long parentId , @Param("lOWarehouseId") long lOWarehouseId,    @Param("time") long time);

	ShopStoreOrder getUserOrderByNo(@Param("storeId") long storeId, @Param("orderNo") String orderNo);

	int updateOrderPayStatus(@Param("orderNo") long orderNo, @Param("paymentNo") String paymentNo,
            @Param("paymentType") int paymentType, @Param("newStatus") int newStatus,
            @Param("oldStatus") int oldStatus, @Param("time") long time);
	int updateOrderPayStatusByParentOrderNo(@Param("parentOrderNo") long parentOrderNo, @Param("paymentNo") String paymentNo,
            @Param("paymentType") int paymentType, @Param("newStatus") int newStatus,
            @Param("oldStatus") int oldStatus, @Param("time") long time);
	
	int updateOrderCommission(@Param("order") ShopStoreOrder order, @Param("time") long time);

	int getUserOrderCount(@Param("userId") long userId, @Param("orderStatus") int orderStatus);

	List<ShopStoreOrder> getUserOrders(@Param("storeId") long storeId, @Param("orderStatus") int orderStatus,
                              @Param("pageQuery") PageQuery pageQuery);

	List<ShopStoreOrder> getChildOrderList(@Param("storeId") long storeId, @Param("orderNOs") Collection<Long> orderNOs);

	/**
	 * 只会查询商品的订单,不会查询会员订单
	 */
	int getUserOrderCountWithoutParent(@Param("userId") long userId, @Param("orderStatus") int orderStatus);
	/**
	 * 只会查询商品的订单,不会查询会员订单
	 */
	List<ShopStoreOrder> getUserOrdersWithoutParent(@Param("storeId") long storeId, @Param("orderStatus") int orderStatus,
    		@Param("pageQuery") PageQuery pageQuery);

	int updateOrderStatus(@Param("orderNo") long orderNo, @Param("newStatus") int newStatus,
                          @Param("oldStatus") int oldStatus, @Param("time") long time, @Param("today")int today);

	int cancelOrder(@Param("id") long id, @Param("time") long time, @Param("cancelReason") String cancelReason);

	int getSupplierOrderCount(@Param("supplierId")long supplierId, @Param("orderStatus")int orderStatus);

	List<ShopStoreOrder> getSupplierOrdersNew(@Param("supplierId")long supplierId, @Param("orderStatus")int orderStatus, @Param("pageQuery")PageQuery pageQuery);

	int getSupplierOrderCountWithoutParent(@Param("supplierId")long supplierId, @Param("orderStatus")int orderStatus);

	List<ShopStoreOrder> getSupplierOrdersNewWithoutParent(@Param("supplierId")long supplierId, @Param("orderStatus")int orderStatus, 
			@Param("pageQuery")PageQuery pageQuery);

	/**
	 * 获取用户累计订单商品实付总计金额
	 * @param storeId
	 * @return
	 */
	double getAllOrderAccumulatedSum(@Param("storeId")long storeId);

	/**
	 * 获取当前母订单的子订单
	 * @param parentId
	 * @return
	 */
	List<ShopStoreOrder> getStoreOrderByParentId(@Param("parentId")long parentId);

	/**
	 * 获取用户累计订单商品退款金额
	 * @param storeId
	 * @return
	 */
	double getAllOrderTotalRefundCostSum(long storeId);

	/**
	 * 通过关键字,和订单状态, 过滤查询用户订单总数
	 * @param userId
	 * @param keyword 过滤关键字
	 * @return int
	 * @auther Charlie(唐静)
	 * @date 2018/5/29 16:30
	 */
	int countByKeyword(@Param("userId")long userId, @Param("keyword")String keyword);

	/**
	 * 根据订单状态,和关键字, 过滤查询用户的订单
	 * @param userId 用户id
	 * @param pageQuery 分页参数
	 * @param keyword 查询的关键字
	 * @return java.util.List<com.store.entity.ShopStoreOrder>
	 * @auther Charlie(唐静)
	 * @date 2018/5/29 16:10
	 */
	List<ShopStoreOrder> findByKeyword(@Param("userId")long userId, @Param("keyword")String keyword, @Param("pageQuery") PageQuery pageQuery);
}