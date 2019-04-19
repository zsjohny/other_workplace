package com.jiuy.core.service.product;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.product.ProductTag;
import com.jiuyuan.entity.product.Tag;

public interface ProductTagService {

	Map<Long, Integer> productCountOfTagMap(Collection<Long> tagIds);

	List<ProductTag> search(Long tagId);

	int add(Long productId, Collection<Long> tagIds);

	int delete(Long productId);

	List<Tag> tagsOfProductId(Long productId);

	Map<Long, Integer> productCountOfGroupTagMap(Collection<Long> tagIds);

	List<ProductTag> search(Collection<Long> tagIds);

}
