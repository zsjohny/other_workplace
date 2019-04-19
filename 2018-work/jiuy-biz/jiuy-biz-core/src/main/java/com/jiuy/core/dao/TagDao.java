package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;

public interface TagDao {

	int addCount(Long groupId);

	Tag add(Tag tag);

	int searchCount(String name, Integer countMin, Integer countMax, Integer productCountMin, Integer productCountMax, Collection<Long> groupIds, Integer isGroup, Integer isTop);

	List<Tag> search(PageQuery pageQuery, String name, Integer countMin, Integer countMax, Integer productCountMin,
			Integer productCountMax, Collection<Long> groupIds, Integer isGroup, Integer isTop);

	int update(Tag tag);

	int update(int status, Long id);

	List<Tag> search(Long groupId, String name, int isTop);

	List<Tag> search();

	Map<Long, Tag> itemsOfIds(Collection<Long> ids);

	List<Tag> searchWithChild();

	int updTagTop(long tagId, long isTop,long updateTime);

}
