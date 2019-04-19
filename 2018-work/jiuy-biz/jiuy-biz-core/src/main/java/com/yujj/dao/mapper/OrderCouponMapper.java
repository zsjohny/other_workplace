/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.order.Coupon;

@DBMaster
public interface OrderCouponMapper {

	int getUserCouponCount(@Param("userId") long userId, @Param("status") int status);
	
	int getUserCouponCountHistory(@Param("userId") long userId);
	
	int getUserCouponCountByCode(@Param("exchangeCode") String exchangeCode);
    
    List<Coupon> getUserCoupons(@Param("userId") long userId, @Param("status") int status, @Param("pageQuery") PageQuery pageQuery);
    
    List<Coupon> getUserCouponsHistory(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery);
    
    List<Coupon> getUserCouponListByOrderNo(@Param("orderNo") long orderNo);
    
    int userExchangeCouponByCode(@Param("userId") long userId, @Param("exchangeCode") String exchangeCode, @Param("yjjNumber") long yjjNumber, @Param("time") long time);
    
    List<Coupon> getUserOrderCoupon(@Param("userId") long userId, @Param("status") int status);

    Coupon getCouponById(@Param("id") long id, @Param("userId") long userId);
    
    List<Coupon> getCouponByIdArr(@Param("ids") String ids, @Param("userId") long userId);
    
    @MapKey("id")
    Map<Long, Coupon> getCouponByIds(@Param("ids") Collection<Long> ids);

    int updateCouponUsed(@Param("idArr") String[] idArr, @Param("orderNo") long orderNo, @Param("time") long time, @Param("newStatus") int newStatus,
            @Param("oldStatus") int oldStatus);
    
    int updateCouponUnuse(@Param("orderNo") long orderNo, @Param("time") long time, @Param("newStatus") int newStatus,
    		@Param("oldStatus") int oldStatus);

	List<Coupon> searchAvailableCoupons(@Param("templateId") long templateId, @Param("limitCount") int count, @Param("validityTime") long time);

	int batchUpdate(@Param("sql") String sql);
   
	int batchAdd(@Param("coupons") List<Coupon> coupons);
	
	List<Coupon> search(@Param("pageQuery") PageQuery pageQuery, @Param("userId") long userId, @Param("getWay") Integer couponGetWay);

	int searchCount(@Param("userId") long userId, @Param("getWay") Integer couponGetWay);

	void batchUpdateGrant(@Param("ids") Collection<Long> couponIds, @Param("adminId") int adminId, @Param("pushStatus") int pushStatus, @Param("userId") long userId, 
			@Param("yjjNumber") long yjjNumber, @Param("time") long time, @Param("getWay") Integer couponGetWay);


}
