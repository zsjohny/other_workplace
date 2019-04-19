package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.dynamicproperty.DynamicPropertyProductMapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyProduct;

/**
 * <p>
 * 动态属性与商品表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@Service
public class DynamicPropertyProductService implements IDynamicPropertyProductService {
	@Autowired
	private DynamicPropertyProductMapper dynamicPropertyProductMapper;

	@Override
	public List<Map<String, Object>> getDynaPropAndValue(Long productId) {
		 List<Map<String, Object>> list =dynamicPropertyProductMapper.getDynaPropAndValue(productId);
		return list;
	}

	@Override
	public List<DynamicPropertyProduct> selectList(Wrapper<DynamicPropertyProduct> wrapper) {
		
		return dynamicPropertyProductMapper.selectList(wrapper);
	}

	@Transactional(rollbackFor = Exception.class)
	public void insert(DynamicPropertyProduct dynamicPropertyProduct) {
		dynamicPropertyProductMapper.insert(dynamicPropertyProduct);
	}

	@Transactional(rollbackFor = Exception.class)
	public void delete(DynamicPropertyProduct dynamicPropertyProduct) {
		dynamicPropertyProductMapper.deleteById(dynamicPropertyProduct.getId());
		
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteByWrapper(Wrapper<DynamicPropertyProduct> dynamicPropertyProductWrapper) {
		dynamicPropertyProductMapper.delete(dynamicPropertyProductWrapper);
	}

	@Override
	public boolean show(long productId) {
		List<Map<String,Object>> list = dynamicPropertyProductMapper.show(productId);
		if (list.size()>0) {
			return true;
		}
		return false;
	}
	
}
