package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.dynamicproperty.DynamicPropertyValueMapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyValue;

/**
 * <p>
 * 属性值表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@Service
public class DynamicPropertyValueService implements IDynamicPropertyValueService {
	
	@Autowired
	private DynamicPropertyValueMapper dynamicPropertyValueMapper;

	/**
	 * 添加动态属性值
	 */
	@Transactional(rollbackFor = Exception.class)
	public void add(DynamicPropertyValue dynamicPropertyValue) {
		dynamicPropertyValueMapper.insert(dynamicPropertyValue);
	}


	/**
	 * 获取属性值列表
	 */
	public List<DynamicPropertyValue> selectByDynaPropId(Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper) {
		List<DynamicPropertyValue> dynamicPropertyValueList = dynamicPropertyValueMapper.selectList(dynamicPropertyValueWrapper);
		return dynamicPropertyValueList;
	}
	public void delete(Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper) {
		dynamicPropertyValueMapper.delete(dynamicPropertyValueWrapper);
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(DynamicPropertyValue dynamicPropertyValue) {
		dynamicPropertyValueMapper.updateById(dynamicPropertyValue);
	}


	@Override
	public DynamicPropertyValue selectById(long dynaPropValueId) {
		DynamicPropertyValue dynamicPropertyValue = dynamicPropertyValueMapper.selectById(dynaPropValueId);
		return dynamicPropertyValue;
	}


	@Override
	public boolean isExitsSameValue(String value, long dynaPropId) {
		Wrapper<DynamicPropertyValue> dynamicPropertyValueWrapper = new EntityWrapper<DynamicPropertyValue>().eq("dyna_prop_value", value).eq("dyna_prop_id", dynaPropId);
		List<DynamicPropertyValue> dynamicPropertyValueList = selectByDynaPropId(dynamicPropertyValueWrapper);
		if(dynamicPropertyValueList.size()>0){
//			delete(dynamicPropertyValueWrapper);
			return true;
		}
		return false;
	}


	@Override
	public int selectCount(Wrapper<DynamicPropertyValue> wrapper) {
		
		return dynamicPropertyValueMapper.selectCount(wrapper);
	
	}


	
	
}
