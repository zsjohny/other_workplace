package com.store.dao.mapper.coupon;

import java.util.Collection;
import java.util.Map;

import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.store.entity.coupon.ShopCouponTemplate;

/**
 * <p>
  * 优惠券模板表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-08-14
 */
public interface ShopCouponTemplateMapper extends BaseMapper<ShopCouponTemplate> {

	/**
	 * 领取数量+1
	 * @param shopCouponTemplateId
	 * @return
	 */
	int increaseGetCount(@Param("shopCouponTemplateId") long shopCouponTemplateId);
	
	@MapKey("id")
	Map<Long, ShopCouponTemplate> searchMap(@Param("ids") Collection<Long> couponTemplateIds);

	/**
	 * 根据id, 获取StoreCouponTemplate
	 * @param id
	 * @return: com.jiuyuan.constant.coupon.StoreCouponTemplate
	 * @author: Charlie(唐静)
	 * @date: 18/05/23
	 */
	StoreCouponTemplate selectCouponTemplateById(@Param("id")long id);

	/**
	 *  参考 {@link com.jiuy.core.dao.StoreCouponTemplateDao#update}
	 * @param: id
	 * @param: money
	 * @param: publishCount
	 * @param: grantCount
	 * @param: availableCount
	 * @return: void
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/24 0:05
	 */
	void updateSpecial(@Param("id") Long id, @Param("money") Double money, @Param("publishCount") Integer publishCount, @Param("grantCount") Integer grantCount, @Param("availableCount") Integer availableCount);

}