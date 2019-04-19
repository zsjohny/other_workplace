package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.brand.BrandLogoCommissionPercentage;
import com.jiuyuan.entity.query.PageQuery;

public interface BrandDao extends DomainDao<BrandLogo, Long>{

	public BrandLogo addBrand(BrandLogo brand);

	public List<BrandLogo> getBrands();

	public int getBrandIdByName(String brandName);

	public int remove(Collection<Long> brandIds);

	public BrandLogo getByBrandId(long brandId);

	public int updateBrand(BrandLogo brandLogo);

	public List<BrandLogo> search(String name, PageQuery pageQuery);

	public int searchCount(String name);

	public List<BrandLogo> search(Collection<Long> brandIds);
	
	public List<BrandLogoCommissionPercentage> getBrandLogosWithCommissionPercentage();

	public List<BrandLogo> searchList(int status, String keywords, PageQuery pageQuery, int brandType);
	
	public int searchListCount(int status, String keywords, int brandType);

	public int updateStatus(int status, long brandId);

	public List<Long> searchIdsByClothNumberPrefix(String clothNumberPrefix);
}
