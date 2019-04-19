package com.jiuyuan.dao.mapper.supplier;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreCouponNew;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2018-03-13
 */
@DBMaster
public interface StoreCouponNewMapper extends BaseMapper<StoreCouponNew> {

	List<StoreCouponNew> availableCoupon(@Param("storeId")long storeId, @Param("brandId")String brandId, @Param("amount")double amount);

}