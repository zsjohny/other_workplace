package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.product.ProductTag;
import com.jiuyuan.entity.product.Tag;

public interface ProductTagDao {

	List<Map<String, Object>> productCountOfTagMap(Collection<Long> tagIds);

	List<ProductTag> search(Long tagId);

	int add(Long productId, Collection<Long> tagIds);

	int delete(Long productId);

	List<Tag> tagsOfProductId(Long productId);

	List<Map<String, Object>> productCountOfGroupTagMap(Collection<Long> tagIds);

	List<ProductTag> search(Collection<Long> tagIds);

}
