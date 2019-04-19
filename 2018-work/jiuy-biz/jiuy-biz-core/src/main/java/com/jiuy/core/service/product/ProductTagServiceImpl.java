package com.jiuy.core.service.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.ProductTagDao;
import com.jiuyuan.entity.product.ProductTag;
import com.jiuyuan.entity.product.Tag;

@Service
public class ProductTagServiceImpl implements ProductTagService {
	
	@Autowired
	private ProductTagDao productTagDao;

	@Override
	public Map<Long, Integer> productCountOfTagMap(Collection<Long> tagIds) {
		if(tagIds.size() < 1) {
			return new HashMap<Long, Integer>();
		}
		
		Map<Long, Integer> productTagMap = new HashMap<Long, Integer>();
		List<Map<String, Object>> list = productTagDao.productCountOfTagMap(tagIds);
		for (Map<String, Object> map : list) {
			productTagMap.put(Long.parseLong(map.get("TagId").toString()), Integer.parseInt(map.get("count").toString()));
		}
		return productTagMap;
	}

	@Override
	public List<ProductTag> search(Long tagId) {
		return productTagDao.search(tagId);
	}

	@Override
	public int add(Long productId, Collection<Long> tagIds) {
		return productTagDao.add(productId, tagIds);
	}

	@Override
	public int delete(Long productId) {
		return productTagDao.delete(productId);
	}

	@Override
	public List<Tag> tagsOfProductId(Long productId) {
		return productTagDao.tagsOfProductId(productId);
	}

	@Override
	public Map<Long, Integer> productCountOfGroupTagMap(Collection<Long> tagIds) {
		if(tagIds.size() < 1) {
			return new HashMap<Long, Integer>();
		}
		
		Map<Long, Integer> productGroupTagMap = new HashMap<Long, Integer>();
		List<Map<String, Object>> list = productTagDao.productCountOfGroupTagMap(tagIds);
		for (Map<String, Object> map : list) {
			productGroupTagMap.put(Long.parseLong(map.get("GroupId").toString()), Integer.parseInt(map.get("count").toString()));
		}
		return productGroupTagMap;
	}

	@Override
	public List<ProductTag> search(Collection<Long> tagIds) {
		if(tagIds.size() < 1) {
			return new ArrayList<>();
		}
		
		return productTagDao.search(tagIds);
	}

}
