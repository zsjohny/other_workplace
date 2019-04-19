package com.jiuy.operator.common.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.admin.core.node.ZTreeNode;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * 部门dao
 *
 * @author fengshuonan
 * 
 * @date 2017年2月17日20:28:58
 */
@DBMirror
public interface DeptDao {

	/**
	 * 获取ztree的节点列表
	 *
	 * @return
	 * @date 2017年2月17日 下午8:28:43
	 */
	List<ZTreeNode> tree();

	List<Map<String, Object>> list(@Param("condition") String condition);
}
