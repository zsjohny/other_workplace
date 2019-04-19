package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicProperty;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyWithValue;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 动态属性表 服务类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
public interface IDynamicPropertyService {

	long addAndGetId(DynamicProperty dynamicProperty);

	List<DynamicProperty> getDynamicProperty(Wrapper<DynamicProperty> wrapper);

	List<Map<String, Object>> selectPageList(String name, String dynaPropValue, int formType, int isFill,
			long dynaPropGroupId, int status,Page<Map<String, Object>> page);

	void update(DynamicProperty dynamicProperty);

	DynamicProperty getDynamicPropertyById(long id);

	/**
	 * 根据动态属性id 关联获取动态属性和动态属性值
	 * @param dynamicPropIdList
	 * @date: 2018/5/11 17:17
	 * @author: charlie
	 */
    List<DynamicPropertyWithValue> getPropertyAndValues(@Param("dynamicPropIdList")List<Long> dynamicPropIdList);
}
