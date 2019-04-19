package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyGroup;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyValue;

/**
 * <p>
 * 动态属性与分类关系表 服务类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
public interface IDynamicPropertyCategoryService {


	List<Map<String,Object>> getChoosedDynamicProperty(long cateGoryId,int status);

	Map<String, Object> getDynamicPropertyAndGroup(List<Map<String, Object>> dynamicPropertyCategorylist,
			List<DynamicPropertyGroup> dynaPropGroupList);

	void update(DynamicPropertyCategory dynamicPropertyCategory);

	DynamicPropertyCategory selectById(long id);

	void insert(DynamicPropertyCategory dynamicPropertyCategory);

	List<DynamicPropertyCategory> selectList(Wrapper<DynamicPropertyCategory> wrapper);

	int selectCount(Wrapper<DynamicPropertyCategory> wrapper);


}
