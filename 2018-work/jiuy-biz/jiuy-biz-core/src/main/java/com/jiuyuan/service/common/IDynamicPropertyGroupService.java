package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyGroup;

/**
 * <p>
 * 动态属性组表 服务类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
public interface IDynamicPropertyGroupService {

	void add(DynamicPropertyGroup dynamicPropertyGroup);

	List<Map<String,Object>> selectPageList(String name, Page<Map<String, Object>> page);

	DynamicPropertyGroup getDynaPropGroupById(long id);

	void update(DynamicPropertyGroup dynamicPropertyGroup);

	void delete(long id);

	List<DynamicPropertyGroup> getDynaPropGroupList(Wrapper<DynamicPropertyGroup> wrapper);
	
}
