package com.jiuyuan.dao.mapper.dynamicproperty;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;

/**
 * <p>
  * 动态属性与分类关系表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@DBMaster
public interface DynamicPropertyCategoryMapper extends BaseMapper<DynamicPropertyCategory> {

	List<Map<String, Object>> getChoosedDynamicProperty(@Param("cateGoryId")long cateGoryId,@Param("status")long status);

}