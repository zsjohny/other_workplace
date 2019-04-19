package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.dynamicproperty.DynamicPropertyGroupMapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyGroup;

/**
 * <p>
 * 动态属性组表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@Service
public class DynamicPropertyGroupService implements IDynamicPropertyGroupService {

	@Autowired
	private DynamicPropertyGroupMapper dynamicPropertyGroupMapper;
	
	
	
	/**
	 * 添加动态属性组
	 */
	@Transactional(rollbackFor = Exception.class)
	public void add(DynamicPropertyGroup dynamicPropertyGroup) {
		dynamicPropertyGroupMapper.insert(dynamicPropertyGroup);
	}

	
	/**
	 * 获取动态属性组列表
	 */
	public List<Map<String, Object>> selectPageList(String name, Page<Map<String, Object>> page) {
		List<Map<String, Object>> selectPageList = dynamicPropertyGroupMapper.selectPageList(name, page);
		return selectPageList;
	}

	/**
	 * 根据id获取动态属性组
	 */
	public DynamicPropertyGroup getDynaPropGroupById(long id) {
		DynamicPropertyGroup dynamicPropertyGroup = dynamicPropertyGroupMapper.selectById(id);
		return dynamicPropertyGroup;
	}

	/**
	 * 修改动态属性组
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(DynamicPropertyGroup dynamicPropertyGroup) {
		dynamicPropertyGroupMapper.updateById(dynamicPropertyGroup);
	}


	/**
	 * 删除动态属性组
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(long id) {
		dynamicPropertyGroupMapper.deleteById(id);
	}


	/**
	 * 获取动态属性组
	 */
	public List<DynamicPropertyGroup> getDynaPropGroupList(Wrapper<DynamicPropertyGroup> wrapper) {
		List<DynamicPropertyGroup> dynamicPropertyGroupList = dynamicPropertyGroupMapper.selectList(wrapper);
		return dynamicPropertyGroupList;
	}


}
