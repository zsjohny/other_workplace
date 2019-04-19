package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyProduct;

/**
 * <p>
 * 动态属性与商品表 服务类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
public interface IDynamicPropertyProductService {


	List<Map<String, Object>> getDynaPropAndValue(Long productId);

	List<DynamicPropertyProduct> selectList(Wrapper<DynamicPropertyProduct> dynamicPropertyProductWrapper);

	void insert(DynamicPropertyProduct dynamicPropertyProduct);

	void delete(DynamicPropertyProduct dynamicPropertyProduct);

	void deleteByWrapper(Wrapper<DynamicPropertyProduct> dynamicPropertyProductWrapper);

	boolean show(long productId);
	
	
	
}
