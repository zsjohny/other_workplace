package com.jiuy.core.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.BrandDao;
//import com.jiuy.core.service.common.MemcachedService;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.brand.BrandLogoCommissionPercentage;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;

@Service("brandLogoService")
public class BrandLogoServiceImpl {

	@Resource
	private BrandDao brandDaoSqlImpl;
	
	@Autowired
	private MemcachedService memcachedService;

	@SuppressWarnings("unchecked")
	public List<BrandLogo> getBrands() {
		String groupKey = MemcachedKey.GROUP_KEY_BRAND;
		String key = "";
		Object obj = memcachedService.get(groupKey, key);
		if (obj != null) {
			return (List<BrandLogo>)obj;
		}
		
		List<BrandLogo> brandLogos = brandDaoSqlImpl.getBrands();
		memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, brandLogos);
		
		return brandLogos;
	}

	public int getBrandIdByName(String brandName) {
		return brandDaoSqlImpl.getBrandIdByName(brandName);
	}

    public void remove(Collection<Long> brandIds) {
        brandDaoSqlImpl.remove(brandIds);
    }
    
    public void updateStatus(int status, long brandId) {
		int i = brandDaoSqlImpl.updateStatus(status,brandId);
		if(i == -1){
			throw new RuntimeException("更改品牌起禁用状态失败！");
		}
		
	}
    
	public ResultCode updateBrand(BrandLogo brandLogo) {
		long updateTime = System.currentTimeMillis();
		
		brandLogo.setUpdateTime(updateTime);
		brandDaoSqlImpl.updateBrand(brandLogo);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public List<BrandLogo> search(String name, PageQuery pageQuery) {
		return brandDaoSqlImpl.search(name, pageQuery);
	}
    
	public List<BrandLogo> search(int status, String keywords, PageQuery pageQuery, int brandType) {
		return brandDaoSqlImpl.searchList(status,keywords,pageQuery,brandType);
	}
	
	public int searchCount(String name) {
		return brandDaoSqlImpl.searchCount(name);
	}
	
    public int searchCount(int status, String keywords, int brandType) {
		return brandDaoSqlImpl.searchListCount(status, keywords, brandType);
	}

	public Map<Long, BrandLogo> getBrandMap() {
		Map<Long, BrandLogo> brandMap = new HashMap<Long, BrandLogo>();
	
		List<BrandLogo> brandLogos = brandDaoSqlImpl.getBrands();
		for(BrandLogo brandLogo : brandLogos) {
			brandMap.put(brandLogo.getBrandId(), brandLogo);
		}
		
		return brandMap;
	}

	public BrandLogo addBrand(BrandLogo brandLogo) {
		return brandDaoSqlImpl.addBrand(brandLogo);
	}

	public BrandLogo getByBrandId(long brandId) {
		return brandDaoSqlImpl.getByBrandId(brandId);
	}

	public List<BrandLogo> search(Collection<Long> brandIds) {
		if (brandIds.size() < 1) {
			return new ArrayList<BrandLogo>();
		}
		return brandDaoSqlImpl.search(brandIds);
	}

	public List<BrandLogoCommissionPercentage> getBrandLogosWithCommissionPercentage(){
		return brandDaoSqlImpl.getBrandLogosWithCommissionPercentage();
	}

	

	

	
}
