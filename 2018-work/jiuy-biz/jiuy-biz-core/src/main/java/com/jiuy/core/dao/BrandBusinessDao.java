package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.member.BrandBusinessSO;
import com.jiuy.core.meta.member.BrandBusinessSearch;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author WuWanjian
* @version 创建时间: 2016年10月27日 下午4:42:00
*/
public interface BrandBusinessDao {
	public List<Map<String,Object>> search(BrandBusinessSearch brandBusinessSearch, PageQuery pageQuery);
	
	public int searchCount(BrandBusinessSearch brandBusinessSearch);
	
	public long add(BrandBusiness brandBusiness);
	
	public int updateBrandBusiness(BrandBusiness brandBusiness);
	
	public int updateBusinessNumberAndUserName(String account, String businessNumber, String userName,String userPassword, long id,String salt, String roleId);
	
	public List<Province> getProvinceList();
	
	public List<City> getCitysByProvinceId(long parentId);
	
	public List<BrandLogo> getBrandList();
	
	public long checkBusinessName(String businessName);

	public long checkBrandId(long brandId);
	
	public int getStatusOfId(long id);

	public BrandBusiness get(BrandBusinessSO brandBusinessSO);

	public BrandBusiness getById(long id);

	public int updatePassword(long id, String randomSalt, String passwordMD5);

	public int resetPassword(long id, String randomSalt, String passwordMD5, int isOriginalPassword);

	public long checkLOWarehouseId(long lOWarehouseId);

	public List<Map<String, Object>> searchPhoneNumber(String phoneNumber);

	public List<BrandLogo> getBrandListWithClothNumberPrefix();
}
