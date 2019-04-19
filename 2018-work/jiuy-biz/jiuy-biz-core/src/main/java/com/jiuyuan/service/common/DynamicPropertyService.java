package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyWithValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.dynamicproperty.DynamicPropertyMapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicProperty;

/**
 * <p>
 * 动态属性表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@Service
public class DynamicPropertyService implements IDynamicPropertyService {
	@Autowired
	private DynamicPropertyMapper dynamicPropertyMapper;
	
	
	/**
	 * 添加动态属性
	 */
	@Transactional(rollbackFor = Exception.class)
	public long addAndGetId(DynamicProperty dynamicProperty) {
		long dynaPropId = dynamicPropertyMapper.addAndGetId(dynamicProperty);
		return dynaPropId;
	}


	/**
	 * 获取动态属性
	 */
	public List<DynamicProperty> getDynamicProperty(Wrapper<DynamicProperty> wrapper) {
		List<DynamicProperty> dynamicPropertyList = dynamicPropertyMapper.selectList(wrapper);
		return dynamicPropertyList;
	}


	/**
	 *获取动态属性列表
	 */
	public List<Map<String, Object>> selectPageList(String name, String dynaPropValue, int formType, int isFill,
			long dynaPropGroupId, int status,Page<Map<String, Object>> page) {
		List<Map<String, Object>> list = dynamicPropertyMapper.selectPageList(name,dynaPropValue,formType, isFill,dynaPropGroupId,status,page);
		return list;
	}

	/**
	 * 修改动态属性
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(DynamicProperty dynamicProperty) {
		dynamicPropertyMapper.updateById(dynamicProperty);
	}


	/**
	 * 根据id获取动态属性
	 */
	public DynamicProperty getDynamicPropertyById(long id) {
		DynamicProperty dynamicProperty = dynamicPropertyMapper.selectById(id);
		return dynamicProperty;
	}

	/**
	 * 根据动态属性id 关联获取动态属性和动态属性值
	 * @param dynamicPropIdList
	 * @date: 2018/5/11 17:17
	 * @author: charlie
	 */
	@Override
	public List<DynamicPropertyWithValue> getPropertyAndValues(List<Long> dynamicPropIdList) {
		return dynamicPropertyMapper.getPropertyAndValues(dynamicPropIdList);
	}


}
