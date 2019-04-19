/**
 * 
 */
package com.store.dao.mapper;

import java.util.List;

import com.jiuyuan.constant.coupon.StoreCoupon;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.ShopCoupon;

@DBMaster
public interface OrderCouponMapper {
	
    List<ShopCoupon> getCouponByIdArr(@Param("ids") String ids, @Param("userId") long userId);

	List<ShopCoupon> getUserOrderCoupon(@Param("userId") long userId, @Param("status") int status);

	int updateCouponUsed(@Param("idArr") String[] idArr, @Param("orderNo") long orderNo, @Param("time") long time, @Param("newStatus") int newStatus,
            @Param("oldStatus") int oldStatus);

	List<ShopCoupon> getUserCouponListByOrderNo(@Param("orderNo") long orderNo);

	int updateCouponUnuse(@Param("orderNo") long orderNo, @Param("time") long time, @Param("newStatus") int newStatus,
    		@Param("oldStatus") int oldStatus);

	List<ShopCoupon> getAllMemberCouponListByOrderNo(@Param("orderNo") long orderNo);

	List<ShopCoupon> getUnusedShopCouponList(@Param("storeId")long storeId, @Param("status")int status, @Param("pageQuery")PageQuery pageQuery);

	List<ShopCoupon> getUsedShopCouponList(@Param("storeId")long storeId, @Param("status")int status, @Param("pageQuery")PageQuery pageQuery);


	int deleteShopCoupon(@Param("shopCouponId")long shopCouponId,@Param("updateTime")long updateTime);

	int getUsedShopCouponListCount(@Param("storeId")long storeId);

	int getUnusedShopCouponListCount(@Param("storeId")long storeId);

	/**
	 * 批量更新优惠券
	 * @param: storeCouponList
	 * @return: void
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/24 0:12
	 */
	@Deprecated
    void batchAddByCoupons(@Param("coupons")List<StoreCoupon> coupons);
}