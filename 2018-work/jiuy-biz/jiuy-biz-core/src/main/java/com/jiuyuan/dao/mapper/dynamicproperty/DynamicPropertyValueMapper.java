package com.jiuyuan.dao.mapper.dynamicproperty;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyValue;

/**
 * <p>
  * 属性值表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@DBMaster
public interface DynamicPropertyValueMapper extends BaseMapper<DynamicPropertyValue> {

	List<Map<String, Object>> getDynaPropAndValue(@Param("productId")Long productId);

}