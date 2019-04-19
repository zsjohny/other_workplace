package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.BrandDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.brand.BrandLogoCommissionPercentage;
import com.jiuyuan.entity.query.PageQuery;

public class BrandDaoSqlImpl extends DomainDaoSqlSupport<BrandLogo, Long> implements BrandDao {

	@Override
	public List<BrandLogo> getBrands() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.getBrands");
	}

	@Override
	public int getBrandIdByName(String brandName) {
		Integer brandId = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.getBrandIdByName",
				brandName);
		return brandId == null ? -1 : brandId;
	}

	@Override
	public int remove(Collection<Long> brandIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("brandIds", brandIds);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.remove", params);
	}

	@Override
	public int updateStatus(int status, long brandId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("status", status);
		params.put("brandId", brandId);

		return getSqlSession().update("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.updateStatus", params);
	}

	@Override
	public BrandLogo getByBrandId(long brandId) {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.getByBrandId", brandId);
	}

	@Override
	public int updateBrand(BrandLogo brandLogo) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.updateBrand", brandLogo);
	}

	@Override
	public List<BrandLogo> search(String name, PageQuery pageQuery) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("name", name);
		params.put("pageQuery", pageQuery);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.search", params);
	}

	@Override
	public List<BrandLogo> searchList(int status, String keywords, PageQuery pageQuery, int brandType) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("status", status);
		params.put("keywords", keywords);
		params.put("pageQuery", pageQuery);
		params.put("brandType", brandType);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.searchList", params);
	}

	@Override
	public int searchCount(String name) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("name", name);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.searchCount", params);
	}

	@Override
	public int searchListCount(int status, String keywords, int brandType) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("status", status);
		params.put("keywords", keywords);
		params.put("brandType", brandType);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.searchListCount", params);

	}

	@Override
	public BrandLogo addBrand(BrandLogo brand) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.addBrand2", brand);

		return brand;
	}

	@Override
	public List<BrandLogo> search(Collection<Long> brandIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("brandIds", brandIds);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.search", params);
	}

	@Override
	public List<BrandLogoCommissionPercentage> getBrandLogosWithCommissionPercentage() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.getBrandListWithPercentage");
	}

	@Override
	public List<Long> searchIdsByClothNumberPrefix(String clothNumberPrefix) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("clothNumberPrefix", clothNumberPrefix);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.BrandDaoSqlImpl.searchCountByClothNumberPrefix", params);
	}

}
