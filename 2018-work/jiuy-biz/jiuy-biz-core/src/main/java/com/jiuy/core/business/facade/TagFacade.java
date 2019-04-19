package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.product.ProductTagService;
import com.jiuy.core.service.tag.TagService;
import com.jiuyuan.entity.product.ProductTag;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.tag.TagVO;
import com.jiuyuan.util.CollectionUtil;

@Service
public class TagFacade {
	
	@Autowired
	private TagService tagService;

	@Autowired
	private ProductTagService productTagService;
	
	public List<TagVO> search(PageQuery pageQuery, String name, Integer countMin, Integer countMax, Integer productCountMin,
			Integer productCountMax, Integer groupId, String groupName, Integer isTop) {
		List<Tag> tags = new ArrayList<Tag>();
		if (groupId.equals(-1)) {
			tags = tagService.search(pageQuery, groupName, countMin, countMax, productCountMin, productCountMax, CollectionUtil.createSet(-1L), 1,isTop);
		} else if(groupId.equals(0)) {
			tags = tagService.search(-1L, groupName,0);
			Set<Long> groupIds = new HashSet<Long>();
			for (Tag tag : tags) {
				groupIds.add(tag.getId());
			}
			tags = tagService.search(pageQuery, name, countMin, countMax, productCountMin, productCountMax, groupIds, 0,isTop);
		} else {
			throw new ParameterErrorException("groupId参数有误!");
		}

		Set<Long> tagIds = new HashSet<Long>();
		for(Tag tag : tags) {
			tagIds.add(tag.getId());
		}
		
		if (groupId.equals(-1)) {
			return assembleGroupTags(tagIds, tags);
		} else {
			return assembleTags(tagIds, tags);
		}
	}

	private List<TagVO> assembleGroupTags(Set<Long> tagIds, List<Tag> tags) {
		Map<Long, TagVO> tagVOMap = tagService.tagTreeMap();
		Map<Long, Integer> productCountByGroupTag = productTagService.productCountOfGroupTagMap(tagIds);
		
		List<TagVO> tagVOs = new ArrayList<TagVO>();
		for(Tag tag : tags) {
			long tagId = tag.getId();
			TagVO tagVO = tagVOMap.get(tagId);
			tagVO.setChildTagCount(tagVO.getChildTags().size());
			Integer productCount = productCountByGroupTag.get(tagId);
			tagVO.setProductCount(productCount == null ? 0 : productCount);
			tagVOs.add(tagVO);
		}
		
		return tagVOs;
	}

	private List<TagVO> assembleTags(Set<Long> tagIds, List<Tag> tags) {
		Map<Long, Integer> productCountByTag = productTagService.productCountOfTagMap(tagIds);
		
		List<TagVO> tagVOs = new ArrayList<TagVO>();
		for(Tag tag : tags) {
			TagVO tagVO = new TagVO();
			BeanUtils.copyProperties(tag, tagVO);
			tagVO.setProductCount(productCountByTag.get(tag.getId()));
			tagVOs.add(tagVO);
		}
		
		return tagVOs;
	}

	public void delete(Long id, Long groupId) {
		if(groupId==-1) {
			List<Tag> tags = tagService.search(id, null,0);
			if(tags.size() > 0) {
				throw new ParameterErrorException("标签组下还存在标签！");
			}
		} else if (groupId > 0) {
			List<ProductTag> productTags = productTagService.search(id);
			if(productTags.size() > 0) {
				throw new ParameterErrorException("该标签下还存在关联商品！");
			}
		} else {
			throw new ParameterErrorException("前端传输的标签组Id有误！");
		}
		
		tagService.update(-1, id);
	}

	public int searchCount(String name, Integer countMin, Integer countMax, Integer productCountMin,
			Integer productCountMax, Integer groupId, String groupName, Integer isTop) {
		if (groupId.equals(-1)) {
			return tagService.searchCount(groupName, countMin, countMax, productCountMin, productCountMax, CollectionUtil.createSet(-1L), 1,isTop);
		} else if(groupId.equals(0)) {
			List<Tag> tags = tagService.search(-1L, groupName,0);
			Set<Long> groupIds = new HashSet<Long>();
			for (Tag tag : tags) {
				groupIds.add(tag.getId());
			}
			return tagService.searchCount(name, countMin, countMax, productCountMin, productCountMax, groupIds, 0,isTop);
		}
		return 0;
	}
}
