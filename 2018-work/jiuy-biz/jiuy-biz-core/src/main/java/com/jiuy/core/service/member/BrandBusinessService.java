package com.jiuy.core.service.member;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.member.BrandBusinessSO;
import com.jiuy.core.meta.member.BrandBusinessSearch;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author WuWanjian
* @version 创建时间: 2016年10月27日 下午5:03:14
*/
public interface BrandBusinessService {
	public List<Map<String,Object>> search(BrandBusinessSearch brandBusinessSearch, PageQuery pageQuery);
	
	public int searchCount(BrandBusinessSearch brandBusinessSearch);
	
	public boolean add(UserNew brandBusiness,String password,String storeBusinessPhone);
	
	public int updateBrandBusiness(UserNew brandBusiness,String password,String storeBusinessPhone);
	
	public List<Province> getProvinceList();
	
	public List<City> getCitysByProvinceId(long parentId);
	
	public List<BrandLogo> getBrandList();

	public BrandBusiness get(BrandBusinessSO brandBusinessSO);

	public BrandBusiness getById(long brandBusinessId);

	public boolean sendInitPassword(String phone, long id);

	public List<BrandLogo> getBrandListWithClothNumberPrefix();

}
