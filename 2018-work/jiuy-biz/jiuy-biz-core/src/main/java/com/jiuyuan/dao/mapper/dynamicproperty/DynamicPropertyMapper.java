package com.jiuyuan.dao.mapper.dynamicproperty;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyWithValue;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicProperty;

/**
 * <p>
 * 动态属性表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@DBMaster
public interface DynamicPropertyMapper extends BaseMapper<DynamicProperty> {

	long addAndGetId(DynamicProperty dynamicProperty);

	List<Map<String, Object>> selectPageList(@Param("name") String name, @Param("dynaPropValue") String dynaPropValue,
			@Param("formType") int formType, @Param("isFill") int isFill,
			@Param("dynaPropGroupId") long dynaPropGroupId, @Param("status") int status,
			@Param("page")Page<Map<String, Object>> page );

	/**
	 *
	 * 根据动态属性id 关联获取动态属性和动态属性值
	 * @param dynamicPropIdList
	 * @date: 2018/5/11 17:17
	 * @author: charlie
	 */
	List<DynamicPropertyWithValue> getPropertyAndValues(@Param("dynamicPropIdList") List<Long> dynamicPropIdList);
}