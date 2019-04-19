package com.jiuy.core.service.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.TagDao;
import com.jiuy.core.dao.modelv2.PropertyNameMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.tag.TagVO;

@Service
public class TagServiceImpl implements TagService {
	
	@Autowired
	private TagDao tagDao;
	
	@Autowired
	private PropertyNameMapper propertyNameMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Tag add(Tag tag) {
		if (isDuplicate(tag.getName())) {
			throw new ParameterErrorException("名字不可和标签名重名");
		}
		
		if(tag.getGroupId() > 0) {
			tagDao.addCount(tag.getGroupId());
		} else if (tag.getGroupId() == -1) {
			tag.setCount(0);
		}
		
		long time = System.currentTimeMillis();
		tag.setCreateTime(time);
		tag.setUpdateTime(time);
		
		return tagDao.add(tag);
	}

	@Override
	public int searchCount(String name, Integer countMin, Integer countMax, Integer productCountMin,
			Integer productCountMax, Collection<Long> groupIds, Integer isGroup, Integer isTop) {
		return tagDao.searchCount(name, countMin, countMax, productCountMin, productCountMax, groupIds, isGroup,isTop);
	}

	@Override
	public List<Tag> search(PageQuery pageQuery, String name, Integer countMin, Integer countMax, Integer productCountMin,
			Integer productCountMax, Collection<Long> groupIds, Integer isGroup, Integer isTop) {
		return tagDao.search(pageQuery, name, countMin, countMax, productCountMin, productCountMax, groupIds, isGroup ,isTop);
	}

	@Override
	public int update(Tag tag) {
		if (isDuplicate(tag.getName())) {
			throw new ParameterErrorException("名字不可和标签名重名");
		}
		
		return tagDao.update(tag);
	}

	@Override
	public int update(int status, Long id) {
		return tagDao.update(status, id);
	}

	@Override
	public List<Tag> search(Long groupId, String name,int isTop) {
		return tagDao.search(groupId, name,isTop);
	}

	@Override
	public List<TagVO> getTagTree() {
		List<TagVO> tagVOs = new ArrayList<TagVO>();
		List<Tag> tags = tagDao.search();
		Map<Long, TagVO> parentTagMap = new HashMap<Long, TagVO>();
		
		for (Iterator<Tag> it = tags.iterator(); it.hasNext();) {
			Tag tag = it.next();
			
			if(tag.getGroupId() == -1) {
				TagVO tagVO = new TagVO();
				BeanUtils.copyProperties(tag, tagVO);
				parentTagMap.put(tag.getId(), tagVO);
				
				tagVOs.add(tagVO);
				
				it.remove();
			}
        }
		
		for(Tag tag : tags) {
			long groupId = tag.getGroupId();
			TagVO tagVO = parentTagMap.get(groupId);
			tagVO.getChildTags().add(tag);
		}
		
		return tagVOs;
	}
	
	public Map<Long, TagVO> tagTreeMap() {
		List<TagVO> tagVOs = getTagTree();
		Map<Long, TagVO> tagVOMap = new HashMap<Long, TagVO>();
		for (TagVO tagVO : tagVOs) {
			tagVOMap.put(tagVO.getId(), tagVO);
		}
		return tagVOMap;
	}

	@Override
	public Map<Long, Tag> itemsOfIds(Collection<Long> ids) {
		if (ids.size() < 1) {
			return new HashMap<Long, Tag>();
		}
		
		return tagDao.itemsOfIds(ids);
	}

	private boolean isDuplicate(String name) {
		List<String> list = propertyNameMapper.search();
		if (list.contains(name)) {
			return true;
		}
		
		return false;
	}

	@Override
	public List<Tag> searchWithChild() {
		return tagDao.searchWithChild();
	}

	@Override
	public int updTagTop(long tagId, long isTop) {
		long time = System.currentTimeMillis();
		if(isTop!=0){
			isTop = time;
		}
		return tagDao.updTagTop(tagId,isTop,time);
	}
}
