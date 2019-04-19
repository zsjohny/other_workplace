package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.SupplierCustomerGroup;

/**
 * <p>
  * 供应商分组表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
@DBMaster
public interface SupplierCustomerGroupMapper extends BaseMapper<SupplierCustomerGroup> {

	long insertAndGetId(SupplierCustomerGroup customerGroup);

	List<Map<String, Object>> getGroupList(@Param("userId")long userId);

	List<Map<String, Object>> getGroupListBySearchArgs(@Param("supplierId")long supplierId, @Param("groupName")String groupName, @Param("remark")String remark,
			@Param("page")Page<Map<String, Object>> page);

}