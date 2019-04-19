package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;

/**
 * <p>
  * 品牌表 Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2017-10-16
 */
@DBMaster
public interface RestrictionActivityProductMapper extends BaseMapper<RestrictionActivityProduct> {

	int updateRemainCountById(@Param("restrictionActivityProductId")long restrictionActivityProductId, @Param("allBuyCount")int allBuyCount);

}