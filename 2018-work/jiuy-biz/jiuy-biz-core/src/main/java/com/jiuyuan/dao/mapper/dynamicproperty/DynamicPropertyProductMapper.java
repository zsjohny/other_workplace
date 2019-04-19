package com.jiuyuan.dao.mapper.dynamicproperty;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyProduct;

/**
 * <p>
  * 动态属性与商品表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@DBMaster
public interface DynamicPropertyProductMapper extends BaseMapper<DynamicPropertyProduct> {

	List<Map<String, Object>> getDynaPropAndValue(Long productId);

	List<Map<String, Object>> show(long productId);

}