package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.dao.modelv2.CategoryMapper;
import com.jiuy.core.service.CategoryFilterService;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.product.ProductTagService;
import com.jiuy.core.service.tag.TagService;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.product.CategoryFilter;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.tag.TagVO;

@Service
public class ProductTagFacade {

	private static Logger logger = Logger.getLogger(ProductTagFacade.class);
	@Autowired
	private ProductTagService productTagService;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryFilterService categoryFilterService;
	
	@Transactional(rollbackFor = Exception.class)
	public void addTag(Long productId, String tagIdsToString) {
		if(StringUtils.equals("", tagIdsToString)) {
			productTagService.delete(productId);
			return;
		}
		
		String[] tagIdsStr = tagIdsToString.split(",");
		Set<Long> tagIds = new HashSet<Long>();
		for(String tagIdStr : tagIdsStr) {
			tagIds.add(Long.parseLong(tagIdStr));
		}
		
		productTagService.delete(productId);
		productTagService.add(productId, tagIds);
	}

	public Set<TagVO> tagsOfProductId(Long productId) {
		List<Tag> tags = productTagService.tagsOfProductId(productId);
		
		
		Set<Long> tagIds = new HashSet<Long>();
		for (Tag tag : tags) {
			tagIds.add(tag.getGroupId());
		}

		Map<Long, TagVO> tagVOMap = new HashMap<Long, TagVO>();
		Map<Long, Tag> tagMap = tagService.itemsOfIds(tagIds);
		for (Map.Entry<Long, Tag> entry : tagMap.entrySet()) {
			Long id = entry.getKey();
			Tag tag = entry.getValue();
			
			TagVO tagVO = new TagVO();
			BeanUtils.copyProperties(tag, tagVO);
			tagVOMap.put(id, tagVO);
		}
		
		Set<TagVO> tagVOs = new HashSet<TagVO>();
		for (Tag tag : tags) {
			TagVO tagVO = tagVOMap.get(tag.getGroupId());
			tagVO.getChildTags().add(tag);
			tagVOs.add(tagVO);
		}
		
		return tagVOs;
	}

	public Map<String, Object> getRelatedTags(Long productId, Map<Long, TagVO> tagVOMap) {
		List<Category> categories = categoryMapper.getRelatedCatsOfProduct(productId);
		logger.info("categories:"+JSON.toJSONString(categories)+",productId:"+productId);
		Set<Long> categoryIds = new HashSet<>();
		Map<Long, Category> caMap = new HashMap<>();
		for (Category category : categories) {
			long categoryId = category.getId();
			Category category3 = categoryService.getCategoryById(categoryId);
			if(category3 != null){
				logger.info("categoryId:"+categoryId);
				long parentId = category.getParentId();
				if (parentId != 0) {
					categoryIds.add(categoryId);
					caMap.put(categoryId, category);
					
					logger.info("parentId:"+parentId);
					Category category2 = categoryService.getCategoryById(parentId);
					logger.info("category2:"+category2);
					if(category2 != null ){
						caMap.put(category2.getId(), category2);
						categoryIds.add(parentId);
					}else{
						logger.info("商品分类为空，可能是该分类已经删除导致，做的特殊处理！category2:"+category2+",parentId"+parentId);
					}
				}
			}else{
				logger.info("商品分类为空，可能是该分类已经删除导致，做的特殊处理！category3:"+category3);
			}
		}
		
		String sort = " order by CategoryId asc";
		List<CategoryFilter> categoryFilters = categoryFilterService.search(categoryIds, 0, sort);
		
		Map<Long, List<CategoryFilter>> cfMap = new HashMap<>();
		long lastCategoryId = Long.MIN_VALUE;
		List<CategoryFilter> caFilters = null;
		for (CategoryFilter categoryFilter : categoryFilters) {
			long categoryId = categoryFilter.getCategoryId();
			if (lastCategoryId != categoryId) {
				lastCategoryId = categoryId;
				caFilters = new ArrayList<>();
				cfMap.put(lastCategoryId, caFilters);
			}
			caFilters.add(categoryFilter);
		}
		
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<Long, List<CategoryFilter>> entry : cfMap.entrySet()) {
			long categoryId = entry.getKey();
			map.put("category_name", caMap.get(categoryId).getCategoryName());
			
			List<TagVO> tagVOs = new ArrayList<>();
			List<CategoryFilter> cFilters = entry.getValue();
			for (CategoryFilter categoryFilter : cFilters) {
				TagVO tagVO = tagVOMap.get(categoryFilter.getRelatedId());
				tagVOs.add(tagVO);
			}
			map.put("category_tag", tagVOs);
		}
		logger.info("商品分类为空，可能是该分类已经删除导致，做的特殊处理！getRelatedTags 返回 map:"+JSON.toJSONString(map));
		return map;
	}
	
}
