package com.jiuyuan.dao.mapper.dynamicproperty;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyGroup;

/**
 * <p>
  * 动态属性组表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@DBMaster
public interface DynamicPropertyGroupMapper extends BaseMapper<DynamicPropertyGroup> {
	List<Map<String, Object>> selectPageList(@Param("name")String name,@Param("page")Page<Map<String, Object>> page ) ;
}