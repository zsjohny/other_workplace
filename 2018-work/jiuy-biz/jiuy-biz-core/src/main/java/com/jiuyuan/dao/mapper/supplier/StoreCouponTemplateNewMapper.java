package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreCouponTemplateNew;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2018-03-13
 */
@DBMaster
public interface StoreCouponTemplateNewMapper extends BaseMapper<StoreCouponTemplateNew> {

	List<Map<String,Object>> getSupplierCouponTemplate(@Param("brandId")long brandId);
	int getSupplierCouponTemplateCount(@Param("brandId")long brandId);

	/**
	 * 批量查找优惠券
	 *
	 * @param supplierIds supplierIds
	 * @author Aison
	 * @date 2018/6/26 14:00
	 */
	@MapKey("supplierId")
	Map<Long,StoreCouponTemplateNew> selectCouponGroup(@Param("supplierIds") List<Long> supplierIds);

}