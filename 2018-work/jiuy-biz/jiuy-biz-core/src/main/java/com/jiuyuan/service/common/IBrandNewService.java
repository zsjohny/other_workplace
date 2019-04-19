package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.Brand;

public interface IBrandNewService {

	/**
	 * 获取对应品牌信息并封装
	 * @return
	 */
	Map<Long, BrandLogo> getBrandMap();

	List<BrandVO> getBrandListShow(String searchBrand, PageQuery pageQuery, int type, long userId,int brandType);

	Brand getBrand(long brandId);

	Map<String,Object> CheckBrandList(List<String> idList);

	BrandNew getBrandByBrandId(long brandId);

	BrandNew getBrandNew(long brandId);

	Map<String,Object> checkBrowsePermission(String phoneNumber, long brandId, Map<String, Object> data);
	
	int checkBrowsePermission(String phoneNumber, long brandId);

	List<BrandVO> getHsitory(long userId,PageQuery pageQuery);
}