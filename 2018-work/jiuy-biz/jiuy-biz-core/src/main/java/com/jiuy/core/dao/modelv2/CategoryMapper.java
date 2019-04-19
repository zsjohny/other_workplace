package com.jiuy.core.dao.modelv2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.query.PageQuery;
import org.apache.ibatis.annotations.Param;

public interface CategoryMapper {

    public List<Category> getCategories();

    /**
     * 返回带父级名称的类别数据
     * author:Jeff.Zhan
     * @return
     */
    public List<Map<String, Object>> getCategoriesParentName();

    /**
     * 更新分类数据
     * author:Jeff.Zhan
     * @return
     */
    public int updateCategory(Category category);

    /**
     * 新增分类 author:Jeff.Zhan
     * 
     * @param description
     * @param category
     * @return
     */
    public Category addCategory(Category category);

	 /**
     * 返回带父级名称的类别数据---分页功能
     * author:Jeff.Zhan
	 * @param categoryType 
	 * @param categoryName 
	 * @param partnerId 
     * @return
     */
	public List<Map<String, Object>> getCategory(PageQuery query, int categoryType, String categoryName);

	/**
	 * 获取总的分类数目
	 * @param categoryType 
	 * @param categoryName 
	 * @param query
	 * @return
	 */
	public int getCategoryListCount(int categoryType, String categoryName);

	public int getCategoryStatus(long id);
	
	public int deleteCategoty(int id);

	 /**
     * 获取商家定义的分类
     * @author Jeff.Zhan
     * @param modelMap
     * @return
     */
	public List<Category> getPartnerCategories();

	public int hideShowCategory(long id, int status);

	public int rmCategoty(Collection<Long> ids);

	public List<Category> getSubCat(Long parentId);

	public List<Map<String, Object>> getTopCat(int categoryType);

	public List<Category> getTopCategory(int categoryType);
	
	public List<Category> getAllTopCategory();

	public List<Category> search(Integer categoryType);

    public List<Category> search(String name, int type, int parentId, int status);

	public List<Category> search(Collection<Long> ids);

	public List<Category> getRelatedCatsOfProduct(Long productId);

	public List<Category> search(Integer categoryType, Long pushTime);

	public int batchUpdate(Collection<Long> ids, Long qMPushTime);

	public List<Category> search(Integer categoryType, Collection<Long> ids);

	public List<Category> getParentCategories();

	/**
	 * 查询所有非隐藏的类
	 * @return
	 * @author Charlie(唐静)
	 * @date 18/05/11
	 */
	List<com.jiuyuan.entity.Category > getCategoriesByStatus(@Param("status")Collection<Integer> status);

}
