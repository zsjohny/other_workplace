package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.BrandCategoryDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.brand.Partner;
import com.jiuyuan.entity.brand.PartnerVO;
import com.jiuyuan.entity.brandcategory.PartnerCategory;

public class BrandCategoryDaoSqlImpl extends DomainDaoSqlSupport<PartnerCategory, Long> implements BrandCategoryDao {

	@Override
	public int addBrandCategoryList(List<PartnerCategory> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("collection", list);
		int count = getSqlSession().insert("PartnerCategory.insertPartnerCategory", map);
		return count;
	}

	@Override
	public int deleteBrandCategory(List<Long> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", ids);
		int count = getSqlSession().update("PartnerCategory.deletePartnerCategory", map);
		return count;
	}

	@Override
	public List<PartnerCategory> getBrandCategoryByCategoryId(Long categoryId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryId", categoryId);
		List<PartnerCategory> list = getSqlSession().selectList("PartnerCategory.getPartnerCategoryListByCategoryId",
				map);
		return list;
	}

	@Override
	public List<Partner> getWildBrand(String brandName) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("name", brandName);
		
		return getSqlSession().selectList("PartnerCategory.getWildBrand", map);
		

	}

	@Override
	public List<PartnerCategory> getCustomBrandCategory() {
		List<PartnerCategory> list = getSqlSession().selectList("PartnerCategory.getCustomBrandCategory");
		return list;
	}

	@Override
	public List<PartnerCategory> getAllBrandCategory() {

		List<PartnerCategory> list = getSqlSession().selectList("PartnerCategory.getAllBrandCategory");
		return list;

	}

	@Override
	public int addBrand(long categoryId, int partnerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryId", categoryId);
		map.put("partnerId", partnerId);
		return getSqlSession().insert("PartnerCategory.addBrand",map);
	}

	@Override
	public int deleteBrandPartner(long id) {
		return getSqlSession().delete("PartnerCategory.deleteBrandPartner",id);
	}

	@Override
	public int updateBrand(PartnerVO paVo) {
		return getSqlSession().insert("PartnerCategory.updateBrand", paVo);
	}

}
