package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.modelv2.CategoryMapper;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.query.PageQuery;

public class CategoryMapperSqlImpl extends SqlSupport implements CategoryMapper {

    @Override
    public List<Category> getCategories() {
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getCategories");
    }

	@Override
	public List<Map<String, Object>> getCategoriesParentName() {
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getCategory");
	}

	@Override
    public int updateCategory(Category category) {
        return getSqlSession().update("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.update", category);
	}

	@Override
    public Category addCategory(Category category) {
	    long time = System.currentTimeMillis();
	    
        category.setCreateTime(time);
        category.setUpdateTime(time);
	    
        getSqlSession().insert("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.addCategory", category);
        
        return category;
	}

	@Override
	public List<Map<String, Object>> getCategory(PageQuery query, int categoryType, String categoryName) {
		Map<String,Object> params = new HashMap<String,Object>();
		
	    params.put("pageQuery", query);
	    params.put("categoryType", categoryType);
	    params.put("categoryName", categoryName);
	    
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getCategoriesByPage", params);
	}

	@Override
	public int getCategoryListCount(int categoryType, String categoryName) {
		Map<String,Object> params = new HashMap<String,Object>();
		
	    params.put("categoryType", categoryType);
	    params.put("categoryName", categoryName);
		
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getCategoryListCount", params);
	}

	@Override
	public int hideShowCategory(long id , int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", status);
        
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.update", params);
	}

	@Override
	public int getCategoryStatus(long id) {
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getCategoryStatus", id);
	}	
	
	public int deleteCategoty(int id) {
        return getSqlSession().update("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.deleteCategoty", id);
	}

	@Override
	public List<Category> getPartnerCategories() {
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getPartnerCategories");
	}

	@Override
	public int rmCategoty(Collection<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("ids", ids);
		
        return getSqlSession().update("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.rmCategoty", params);
	}

	@Override
	public List<Category> getSubCat(Long parentId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("parentId", parentId);
		
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getSubCat", params);
	}

	@Override
	public List<Map<String, Object>> getTopCat(int categoryType) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("categoryType", categoryType);
		
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getTopCat", params);
	}

	@Override
	public List<Category> getTopCategory(int categoryType) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("categoryType", categoryType);
		
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getTopCategory", params);
	}

	@Override
	public List<Category> search(Integer categoryType) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("categoryType", categoryType);
		
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.search", params);
	}

    @Override
    public List<Category> search(String name, int type, int parentId, int status) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("categoryName", name);
        params.put("categoryType", type);
        params.put("parentId", parentId);
        params.put("status", status);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.search", params);
    }

	@Override
	public List<Category> search(Collection<Long> ids) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("ids", ids);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.search", params);
	}

	@Override
	public List<Category> getRelatedCatsOfProduct(Long productId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("productId", productId);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getRelatedCatsOfProduct", params);
	}

	@Override
	public List<Category> search(Integer categoryType, Long pushTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("categoryType", categoryType);
        params.put("qMPushTime", pushTime);
        params.put("status", 0);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.search", params);
	}

	@Override
	public int batchUpdate(Collection<Long> ids, Long qMPushTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("ids", ids);
        params.put("qMPushTime", qMPushTime);

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.batchUpdate", params);
	}

	@Override
	public List<Category> search(Integer categoryType, Collection<Long> ids) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("categoryType", categoryType);
        params.put("ids", ids);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.search", params);
	}

	@Override
	public List<Category> getAllTopCategory() {
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getAllTopCategory");
	}

	@Override
	public List<Category> getParentCategories() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getParentCategories");
	}

	@Override
	public List<Category> getCategoriesByStatus(Collection<Integer> status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryMapperSqlImpl.getCategoriesByStatus", params);
	}

}
