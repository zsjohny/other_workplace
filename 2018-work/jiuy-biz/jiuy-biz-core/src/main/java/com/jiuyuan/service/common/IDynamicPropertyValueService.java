package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyValue;

/**
 * <p>
 * 属性值表 服务类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
public interface IDynamicPropertyValueService  {

	void add(DynamicPropertyValue dynamicPropertyValue);

	List<DynamicPropertyValue> selectByDynaPropId(Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper);

	void update(DynamicPropertyValue dynamicPropertyValue);

	DynamicPropertyValue selectById(long dynaPropValueId);

	boolean isExitsSameValue(String value, long dynaPropId);

	int selectCount(Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper);


}
