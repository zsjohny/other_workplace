package com.jiuy.core.service;


import java.util.*;

import com.jiuyuan.util.BizUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.core.dao.modelv2.CategoryMapper;
//import com.jiuy.core.service.common.MemcachedService;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.CategoryNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;

@Service
public class CategoryService{

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductNewMapper productNewMapper;

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private CategoryNewMapper categoryNewMapper;

    @SuppressWarnings( "unchecked" )
    public List<Category> getCategories() {
        String groupKey = MemcachedKey.GROUP_KEY_CATEGORY;
        String key = "all";
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (List<Category>) obj;
        }

        List<Category> categories = categoryMapper.getCategories();
        if (CollectionUtils.isNotEmpty(categories)) {
            memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, categories);
        }

        return categories;
    }

    public List<Category> getCategoryTree(List<Category> categories) {
        List<Category> result = new ArrayList<Category>();
        Map<Long, Category> categoryMap = new HashMap<Long, Category>();

        for (Iterator<Category> it = categories.iterator(); it.hasNext(); ) {
            Category cate = it.next();

            categoryMap.put(cate.getId(), cate);
            if (cate.getParentId() == 0) {
                result.add(cate);
                it.remove();
            }
        }

        for (Category cate : categories) {
            Category parentCate = categoryMap.get(cate.getParentId());
            if (parentCate != null) {
                parentCate.getChildCategories().add(cate);
            }
        }

        return result;
    }

    public Category getCategoryById(long categoryId) {
        return getCategoryMap().get(categoryId);
    }

    public Map<Long, Category> getCategoryMap() {
        Map<Long, Category> map = new HashMap<Long, Category>();
        for (Category category : getCategories()) {
            map.put(category.getId(), category);
        }
        return map;
    }

    public Map<String, Category> getCategoryMapByName() {
        Map<String, Category> map = new HashMap<String, Category>();
        for (Category category : getCategories()) {
            map.put(category.getCategoryName(), category);
        }
        return map;
    }

    /**
     * 返回带父级名称的分类信息
     * author:Jeff.Zhan
     *
     * @return
     */
    public List<Map<String, Object>> getCategoriesParentName() {
        List<Map<String, Object>> categories = categoryMapper.getCategoriesParentName();
        return categories;
    }

    /**
     * 更新分类数据
     * author:Jeff.Zhan
     *
     * @param category
     * @return
     */
    public int updateCategory(Category category) {
        return categoryMapper.updateCategory(category);
    }

    /**
     * 新增分类 author:Jeff.Zhan
     *
     * @param description
     * @param category
     * @return
     */
    public Category addCategory(Category category) {
        return categoryMapper.addCategory(category);
    }

    public List<Map<String, Object>> getCategory(PageQuery query, int categoryType, String categoryName) {
        return categoryMapper.getCategory(query, categoryType, categoryName);
    }

    /**
     * 获取总分类数目
     *
     * @param categoryType
     * @param categoryName
     * @param partnerId
     * @param query
     * @return
     */
    public int getCategoryListCount(int categoryType, String categoryName) {
        return categoryMapper.getCategoryListCount(categoryType, categoryName);
    }

    /**
     * 获取商家定义的分类
     *
     * @param modelMap
     * @return
     * @author Jeff.Zhan
     */
    public List<Category> getPartnerCategories() {
        return categoryMapper.getPartnerCategories();
    }

    public int getCategoryStatus(long id) {
        return categoryMapper.getCategoryStatus(id);
    }

    public int hideShowCategory(long id, int status) {
        return categoryMapper.hideShowCategory(id, status);
    }

    public int rmCategoty(Collection<Long> ids) {
        return categoryMapper.rmCategoty(ids);
    }

    public List<Category> getSubCat(Long parentId) {
        return categoryMapper.getSubCat(parentId);
    }

    public List<Map<String, Object>> getTopCat(int categoryType) {
        return categoryMapper.getTopCat(categoryType);
    }

    public List<Category> getTopCategory(int categoryType) {
        return categoryMapper.getTopCategory(categoryType);
    }

    public List<Category> getAllTopCategory() {
        return categoryMapper.getAllTopCategory();
    }

    public List<Category> search(Integer categoryType) {
        List<Category> categories = new ArrayList<Category>();

        for (Category category : getCategories()) {
            if (category.getCategoryType() == categoryType) {
                categories.add(category);
            }
        }

        return categories;
    }

    public List<Category> search(String name, int type, int parentId, int status) {
        return categoryMapper.search(name, type, parentId, status);
    }

    public List<Category> search(Collection<Long> ids) {
        if (ids.size() < 1) {
            return new ArrayList<Category>();
        }
        return categoryMapper.search(ids);
    }

    public List<Category> search(Integer categoryType, Long pushTime) {
        return categoryMapper.search(categoryType, pushTime);
    }

    public int batchUpdate(Collection<Long> ids, Long qMPushTime) {
        return categoryMapper.batchUpdate(ids, qMPushTime);
    }

    public List<Category> search(Integer categoryType, Collection<Long> Ids) {
        return categoryMapper.search(categoryType, Ids);
    }

    public List<Category> getParentCategories() {
        String groupKey = MemcachedKey.GROUP_KEY_CATEGORY;
        String key = "parent";
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (List<Category>) obj;
        }

        List<Category> categories = categoryMapper.getParentCategories();
        if (CollectionUtils.isNotEmpty(categories)) {
            memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, categories);
        }

        return categories;
    }

    /**
     * 修改商品分类信息
     *
     * @param id
     * @param categoryName
     * @param cateGoryId
     */
//	@Transactional(rollbackFor = Exception.class)
    public void updateProductCateGoryInfo(long categoryId, String categoryName) {
//		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
//		wrapper.eq("delState", 0).orNew("oneCategoryId ="+id).orNew("twoCategoryId ="+id).orNew("threeCategoryId ="+id);
//		List<ProductNew> list = productNewMapper.selectList(wrapper);

        productNewMapper.batchProductCategoryName(categoryId, categoryName);

//		for (ProductNew productNew : list) {
//			ProductNew newProduct = new ProductNew();
//			newProduct.setId(productNew.getId());
//			if (productNew.getOneCategoryId() == id) {
//				newProduct.setOneCategoryName(categoryName);
//				productNewMapper.updateById(newProduct);
//			}
//			if (productNew.getTwoCategoryId() == id) {
//				newProduct.setTwoCategoryName(categoryName);
//				productNewMapper.updateById(newProduct);
//			}
//			if (productNew.getThreeCategoryId() == id) {
//				newProduct.setThreeCategoryName(categoryName);
//				productNewMapper.updateById(newProduct);
//			}
//			
//		}

    }

//	/**
//	 * 获取树状的分类列表
//	 * @param wrapper
//	 * @return
//	 */
//	public List<CategoryNew> getTreeCategoryList(Wrapper<CategoryNew> wrapper) {
//		return categoryNewMapper.selectList(wrapper);
//	}

    /**
     * 获取对应条件的分类类列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    public List<CategoryNew> getConformingCategoryList(Page page, Wrapper<CategoryNew> wrapper) {
        return categoryNewMapper.selectPage(page, wrapper);
    }

    /**
     * 新增分类
     *
     * @param category
     */
    public void addCategoryNew(CategoryNew category) {
        categoryNewMapper.insert(category);
    }

    /**
     * 修改分类
     *
     * @param category
     */
    public void updateCategoryNew(CategoryNew category) {
        categoryNewMapper.updateById(category);
    }

    /**
     * 获取分类
     *
     * @param categoryId
     * @return
     */
    public CategoryNew getCategoryByIdNew(long categoryId) {
        return categoryNewMapper.selectById(categoryId);
    }

    /**
     * 修改对应二级分类的一级分类信息
     *
     * @param id
     * @param categoryName
     * @param categoryId
     */
    public void updateProductFirstCategoryInfo(long firstCategoryId, String firstCategoryName, long categoryId) {
        productNewMapper.updateProductFirstCategoryInfo(firstCategoryId, firstCategoryName, categoryId);
    }

    /**
     * 修改对应三级分类的二级分类信息
     *
     * @param id
     * @param categoryName
     * @param categoryId
     */
    public void updateProductSecondCategoryInfo(long secondCategoryId, String secondCategoryName, long categoryId) {
        productNewMapper.updateProductSecondCategoryInfo(secondCategoryId, secondCategoryName, categoryId);
    }

    public List<CategoryNew> getCategoryList(Wrapper<CategoryNew> wrapper) {
        return categoryNewMapper.selectList(wrapper);
    }

    /**
     * 获取对应条件的分类列表
     *
     * @param wrapper
     * @return
     */
    public int getConformingCategoryListCount(Wrapper<CategoryNew> wrapper) {
        return categoryNewMapper.selectCount(wrapper);
    }

}