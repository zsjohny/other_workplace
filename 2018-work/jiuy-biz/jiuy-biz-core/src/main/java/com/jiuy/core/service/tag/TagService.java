package com.jiuy.core.service.tag;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.tag.TagVO;

public interface TagService {

	Tag add(Tag tag);

	/**
	 * 
	 * @param name
	 * @param countMin
	 * @param countMax
	 * @param productCountMin
	 * @param productCountMax
	 * @param groupIds
	 * @param isGroup 0:否，属于标签，1:是，属于标签组
	 * @param isTop 
	 * @return
	 */
	int searchCount(String name, Integer countMin, Integer countMax, Integer productCountMin, Integer productCountMax, Collection<Long> groupIds, Integer isGroup, Integer isTop);

	List<Tag> search(PageQuery pageQuery, String name, Integer countMin, Integer countMax, Integer productCountMin,
			Integer productCountMax, Collection<Long> groupIds, Integer isGroup, Integer isTop);

	int update(Tag tag);

	int update(int status, Long id);

	List<Tag> search(Long groupId, String name, int isTop);

	List<TagVO> getTagTree();

	Map<Long, Tag> itemsOfIds(Collection<Long> tagIds);

	Map<Long, TagVO> tagTreeMap();

	List<Tag> searchWithChild();

	int updTagTop(long tagId, long isTop);

}
