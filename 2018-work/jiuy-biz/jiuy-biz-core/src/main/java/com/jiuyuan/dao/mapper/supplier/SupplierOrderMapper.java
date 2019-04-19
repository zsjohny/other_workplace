package com.jiuyuan.dao.mapper.supplier;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.StoreOrderNewSon;
import com.jiuyuan.entity.newentity.StoreRefundOrderActionLog;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreOrderNew;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-11
 */
@DBMaster
public interface SupplierOrderMapper extends BaseMapper<StoreOrderNew> {

	int updateOrderId(@Param("orderNo")Long orderNo,@Param("storeOrderNew")StoreOrderNew storeOrderNew);
	void getSupplierOrderList();

	List<Map<String,Object>> getSupplierCustomerList(@Param("storeId")long storeId,@Param("businessName") String businessName,  @Param("phoneNumber")String phoneNumber,@Param("moneyMin")double moneyMin, 
			@Param("moneyMax")double moneyMax,@Param("countMin")int countMin, @Param("countMax")int countMax, @Param("province")String province, 
			@Param("city")String city, @Param("page")Page<Map<String, Object>> page);

	int getSupplierCustomerAllCount(@Param("storeId")long storeId);

	int getSupplierCustomerTodayNewCount(@Param("storeId")long storeId, @Param("time")long time);

	List<StoreOrderNew> storeOrdersOfOrderNos(@Param("orderNos")Collection<Long> orderNos);

	double getAllOrderAccumulatedSum(@Param("storeId")long storeId);

	double getAllOrderTotalRefundCostSum(long storeId);
	void changePriceByOrderNo(@Param("orderNo")long orderNo, @Param("changePrice")double changePrice, @Param("parentId")long parentId);
	

	List<Map<String, Object>> exportOrderData(@Param("beginTime")long beginTime, @Param("endTime")long endTime);

	Integer getRestrictionActivityProductAllBuyCount(@Param("restrictionActivityProductId")long restrictionActivityProductId, @Param("storeId")long storeId);

	/**
	 * 查询用户最后一笔未支付,且未过期的会员订单
     * <p>按照当前逻辑的话, 用户永远只有一个未支付的订单</p>
	 *
	 * @param storeId storeId
	 * @param memberPackageType 会员套餐类型
	 * @return com.jiuyuan.entity.newentity.StoreOrderNew
	 * @author Charlie
	 * @date 2018/8/17 18:14
	 */
	StoreOrderNewSon findLastNoPayAndNoPastDueMemberOrder(@Param ("storeId") Long storeId, @Param ("type") Integer memberPackageType);

    /**
     * 重新初始化未支付的订单
     *
     * @param order order
     * @return int
     * @author Charlie
     * @date 2018/8/18 13:34
     */
    int salvageHistoryOrder(StoreOrderNew order);

	/**
	 * 根据订单号删除订单
	 * @param orderNo
	 */
	void deleteOrderByOrderNo(@Param ("orderNo")Long orderNo);

	/**
	 * 根据id 删除订单细目
	 * @param orderItemId
	 */
	void deleteOrderItemById(@Param ("orderItemId")Long orderItemId);

	/**
	 * 查询用户已购买的会员
	 *
	 * @param storeId storeId
	 * @param packageType packageType
	 * @return java.util.List<com.jiuyuan.entity.newentity.StoreOrderNew>
	 * @author Charlie
	 * @date 2018/9/26 10:45
	 */
    List<StoreOrderNew> findHistorySuccessMemberOrder(@Param ("storeId") Long storeId, @Param ("packageType") Integer packageType);

	int subWaitMoney(@Param( "accountId" ) Long accountId, @Param( "operMoney" ) double operMoney);

    List<StoreRefundOrderActionLog> selectRefundLog(@Param("orderNo") Long orderNo);
}