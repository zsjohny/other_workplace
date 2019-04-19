package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.BrandBusinessDao;
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
* @version 创建时间: 2016年10月27日 下午4:45:24
*/
@Repository
public class BrandBusinessDaoSqlImpl implements BrandBusinessDao{
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<Map<String,Object>> search(BrandBusinessSearch brandBusinessSearch, PageQuery pageQuery) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("brandBusinessSearch", brandBusinessSearch);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.search",params);
	}

	@Override
	public int searchCount(BrandBusinessSearch brandBusinessSearch) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("brandBusinessSearch", brandBusinessSearch);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.searchCount",params);
	}

	@Override
	public long add(BrandBusiness brandBusiness) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.addBrandBusiness",brandBusiness);
	}

	@Override
	public int updateBrandBusiness(BrandBusiness brandBusiness) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.updateBrandBusiness",brandBusiness);
	}

	@Override
	public int updateBusinessNumberAndUserName(String account, String businessNumber, String userName, String userPassword, long id, String salt, String roleId) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		
		params.put("account", account);
		params.put("businessNumber", businessNumber);
		params.put("userName", userName);
		params.put("userPassword", userPassword);
		params.put("id", id);
		params.put("salt", salt);
		params.put("roleId", roleId);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.updateBusinessNumberAndUserName",params);
	}

	@Override
	public List<Province> getProvinceList() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.getProvinces");
	}

	@Override
	public List<City> getCitysByProvinceId(long parentId) {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.getCitysByProvinceId",parentId);
	}

	@Override
	public List<BrandLogo> getBrandList() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.getBrandList");
	}
	
	@Override
	public List<BrandLogo> getBrandListWithClothNumberPrefix() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.getBrandListWithClothNumberPrefix");
	}

	@Override
	public long checkBusinessName(String businessName) {
		Object result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.checkBusinessName",businessName);
		return result == null? 0 : (Long)result;
	}

	@Override
	public long checkBrandId(long brandId) {
		Object result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.checkBrandId",brandId);
		return result == null? 0 : (Long)result;
	}

	@Override
	public int getStatusOfId(long id) {
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.getStatusOfId",id);
	}

	@Override
	public BrandBusiness get(BrandBusinessSO brandBusinessSO) {
		Map<String, Object> params= new HashMap<>();
		
		params.put("so", brandBusinessSO);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.get", params);
	}

	@Override
	public BrandBusiness getById(long id) {
		Map<String, Object> params= new HashMap<>();
		
		params.put("id", id);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.getById", params);
	}

	@Override
	public int updatePassword(long id, String randomSalt, String passwordMD5) {
        Map<String, Object> params= new HashMap<>();
		
		params.put("id", id);
		params.put("salt", randomSalt);
		params.put("password", passwordMD5);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.updatePassword", params);
		
	}

	@Override
	public int resetPassword(long id, String randomSalt, String passwordMD5, int isOriginalPassword) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", id);
		params.put("salt", randomSalt);
		params.put("password", passwordMD5);
		params.put("isOriginalPassword", isOriginalPassword);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.resetPassword", params);
	}

	@Override
	public long checkLOWarehouseId(long lOWarehouseId) {
		Object result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.checkLOWarehouseId",lOWarehouseId);
		return result == null? 0 : (Long)result;
	}

	@Override
	public List<Map<String, Object>> searchPhoneNumber(String phoneNumber) {
		Map<String,Object> params = new HashMap<>();
		
		params.put("phoneNumber", phoneNumber);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.BrandBusinessDaoSqlImpl.searchPhoneNumber", params);
	}



}
