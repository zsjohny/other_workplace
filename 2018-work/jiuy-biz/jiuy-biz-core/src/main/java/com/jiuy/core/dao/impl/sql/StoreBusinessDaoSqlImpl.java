package com.jiuy.core.dao.impl.sql;

import java.text.SimpleDateFormat;
import java.util.*;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.meta.member.StoreBusinessSearch;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.StoreRegister;
import com.jiuyuan.entity.StroreRegisterVO;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.withdraw.WithDrawApply;

/**
* @author WuWanjian
* @version 创建时间: 2016年10月25日 上午11:39:05
*/
@Repository
public class StoreBusinessDaoSqlImpl implements StoreBusinessDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	
	@Override
	public List<StoreBusiness> search(StoreBusinessSearch storeBusinessSearch,PageQuery pageQuery) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		
		params.put("storeBusinessSearch", storeBusinessSearch);
		params.put("pageQuery", pageQuery);
		params.put("superStoreId", -1);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.search",params);
	}
	
	


	@Override
	public int searchCount(StoreBusinessSearch storeBusinessSearch, long superStoreId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeBusinessSearch", storeBusinessSearch);
		params.put("superStoreId", superStoreId);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.searchCount", params);
	}
	
	@Override
	public int searchPersonCount(StoreBusinessSearch storeBusinessSearch, long superStoreId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeBusinessSearch", storeBusinessSearch);
		params.put("superStoreId", superStoreId);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.searchPersonCount", params);
	}

	@Override
	public long add(StoreBusiness storeBusiness) {
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.addStoreBusiness", storeBusiness);
	}

	@Override
	public int updateStoreBusiness(StoreBusiness storeBusiness) {
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateStoreBusiness", storeBusiness);
	}

	@Override
	public int updateBusinessNumberAndUserName(String businessNumber, String userName,String userPassword, long id) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("businessNumber", businessNumber);
		params.put("userName", userName);
		params.put("userPassword", userPassword);
		params.put("id", id);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateBusinessNumberAndUserName", params);
	}

	@Override
	public List<Province> getProvinceList() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getProvinces");
	}

	@Override
	public List<City> getCitysByProvinceId(long parentId) {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getCitysByProvinceId",parentId);
	}

	@Override
	public long getIdByBusinessNumber(long businessNumber) {
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getIdByBusinessNumber",businessNumber);
	}

	@Override
	public int decreaseMemberNumberById(long id) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.decreaseMemberNumberById",id);
	}

	@Override
	public List<Long> getRelationBrandIdOfStoreId(long storeId) {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getRelationBrandIdOfStoreId",storeId);
	}

	@Override
	public List<BrandLogo> getBrandsOfBrandIds(Collection<Long> brandIds) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("brandIds", brandIds);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getBrandsOfBrandIds",params);
	}

	@Override
	public int deleteBrandRelationOfStoreId(String storeIds) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("storeIds", storeIds);

		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.deleteBrandRelationOfStoreId",params);
	}
	
	@Override
	public int deleteAllBrandRelation() {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.deleteAllBrandRelation");
	}
	
	public int deleteBrandRelationOfStoreIds(Collection<Long> storeIds){
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("storeIds", storeIds);

		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.deleteBrandRelationOfStoreIds",params);
	}

	@Override
	public int addBrandRelationOfStoreId(String storeId,int status,long createTime,Collection<Long> brandIds) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("storeId", storeId);
		params.put("status", status);
		params.put("createTime", createTime);
		params.put("brandIds", brandIds);
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.addBrandRelationOfStoreId",params);
	}

	@Override
	public long checkBusinessName(String businessName) {
		Object result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.checkBusinessName", businessName);
		return result == null? 0 : (Long)result;
	}
	
	@Override
	public long checkPhoneNumber(String phoneNumber) {
		Object result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.checkPhoneNumber", phoneNumber);
		return result == null? 0 : (Long)result;
	}

	@Override
	public List<StoreBusiness> userOfIds(Set<Long> userIds) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("userIds", userIds);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.userOfIds", params);
	}

	@Override
	public StoreBusiness getByStoreId(long storeId) {
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getByStoreId", storeId);
	}

	@Override
	public int getStatusOfStoreId(long storeId) {
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStatusOfStoreId", storeId);
	}

	@Override
	public int getDistributionStatusOfStoreId(long storeId) {
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getDistributionStatusOfStoreId", storeId);
	}

	@Override
	public StoreBusiness searchBusinessById(long belongStoreId) {
	
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.searchBusinessById", belongStoreId);
	}
	
	@Override
	public StoreBusiness searchBusinessByBusinessNumber(long businessNumber) {
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.searchBusinessByBusinessNumber", businessNumber);
	}

	/* (non-Javadoc)
	 * @see com.jiuy.core.dao.StoreBusinessDao#userOfIdsMap(java.util.Set)
	 */
	@Override
	public Map<Long, StoreBusiness> userOfIdsMap(Set<Long> userIds) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("userIds", userIds);

        return sqlSessionTemplate.selectMap("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.userOfIdsMap", params, "id");
	}
	/* (non-Javadoc)
	 * @see com.jiuy.core.dao.StoreBusinessDao#userOfIdsMap(java.util.Set)
	 */
	@Override
	public Map<Long, StoreBusiness> userOfNumbersMap(List<Long> storeNumbers) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("storeNumbers", storeNumbers);
		
		return sqlSessionTemplate.selectMap("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.userOfNumbersMap", params, "businessNumber");
	}

	@Override
	public int increseAvaliableBalance(long id, double availableCommission) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", id);
        params.put("availableCommission", availableCommission);

        return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.increseAvaliableBalance", params);
	}

	@Override
	public int reduceIncome(long id, double commission) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", id);
        params.put("commission", commission);

        return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.reduceIncome", params);
	}

	@Override
	public StoreBusiness getByLOWarehouseId(long loWarehouseId) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("loWarehouseId", loWarehouseId);

        return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getByLOWarehouseId", params);
	}

	@Override
	public List<StoreBusiness> getBlurByBusinessNumber(Long businessNumber) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("businessNumber", businessNumber);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getBlurByBusinessNumber", params);
	}

	@Override
	public List<StoreRegister> getStoreRegister(PageQuery pageQuery, StroreRegisterVO stroreRegisterVO) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageQuery", pageQuery);
		params.put("params", stroreRegisterVO);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreRegister", params);
	}

	@Override
	public int getStoreRegisterCount(StroreRegisterVO stroreRegisterVO) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("params", stroreRegisterVO);	

        return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreRegisterCount", params);
	}

	@Override
	public Map<String, Object> getRegisterSummary(StroreRegisterVO stroreRegisterVO) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("params", stroreRegisterVO);	

        return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getRegisterSummary", params);
	}

	@Override
	public int updateStoreRegisterForStatus(int status, String applyIds,String applyMemo, String userName, long nowTime, long userId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("status", status);
        params.put("applyIds", applyIds);
        params.put("applyMemo", applyMemo);
        params.put("adminName", userName);
        params.put("adminId", userId);
        params.put("applyTime", nowTime);
        return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateStoreRegisterForStatus", params);
	}

	@Override
	public List<StoreRegister> getStoreregistersForIds(String applyIds) {
		 Map<String, Object> params = new HashMap<String, Object>();
		 
		 params.put("applyIds",applyIds);
		 return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreregistersForIds", params);
	}

	@Override
	public int checkPhoneNumbers(String phoneNumbers) {
		Map<String, Object> params = new HashMap<String, Object>();
		 
		params.put("phoneNumbers",phoneNumbers);
		Object result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.checkPhoneNumbers", params);
		return result == null? 0 : (Integer)result;
	}

	@Override
	public int upDateStoreRegister(StoreRegister storeRegister) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.upDateStoreRegister", storeRegister);
	}

	@Override
	public int setStatusForIds(int status, String ids) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("status", status);
        params.put("ids", ids);

        return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.setStatusForIds", params);
	}

	@Override
	public int setDistributionStatusForIds(int status, String ids) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("distributionStatus", status);
        params.put("ids", ids);

        return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.setDistributionStatusForIds", params);
	}

	public int searchStoreCount(int status){
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.searchStoreCount", status);
	}

	@Override
	public StoreRegister getStoreRegisterByPhoneNumber(String phoneNumber) {
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreRegisterByPhoneNumber", phoneNumber);
	}

	@Override
	public List<Long> getAllStoreBusinessIds() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getAllStoreBusinessIds");
	}

	@Override
	public int updateMoney(StoreBusiness storeBusiness) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateMoney", storeBusiness);
	}

	@Override
	public List<StoreBusiness> search(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery, long superStoreId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		
		params.put("storeBusinessSearch", storeBusinessSearch);
		params.put("pageQuery", pageQuery);
		params.put("superStoreId", superStoreId);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.search",params);
	}


	@Override
	public List<StoreBusinessSearch> searchStoreAndMember(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery, long superStoreId ,Integer membStatus) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		if (membStatus == null) {
			params.put("qMembStatus", -1);
		}
		else if (membStatus == 1) {
			//会员
			params.put("qMembStatus", 1);
		}
		else {
			//非会员
			params.put("qMembStatus", 0);
		}
		params.put("storeBusinessSearch", storeBusinessSearch);
		params.put("currentTime", new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format (new Date ()));
		params.put("pageQuery", pageQuery);
		params.put("superStoreId", superStoreId);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.searchStoreAndMember",params);
	}


	public int updateStoreActiveTime(long storeId, long activeTime) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		params.put("activeTime", activeTime);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateStoreActiveTime",params);
	}

	@Override
	public List<StoreBusiness> getStoreIncomeIds() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreIncomeIds",null);
	}

	@Override
	public List<WithDrawApply> getOnProcess(long id, int type) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
	
		params.put("id", id);
		params.put("type", type);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getOnProcess",params);
	}

	@Override
	public int setVIP(long storeId,int vip) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("id", storeId);
		params.put("vip", vip);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.setVIP",params);
	}

	
	
	@Override
	public int setOpenWxa(long storeId,int openWxaState,long currentTime,long afterOneYear) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		params.put("openWxaState", openWxaState);
		params.put("wxaOpenTime", currentTime);
		params.put("wxaCloseTime", afterOneYear);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.setOpenWxa",params);
	}
	@Override
	public int setWxaType(long storeId,int wxaType) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		params.put("wxaType", wxaType);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.setWxaType",params);
	}


	@Override
	public List<StoreBusiness> getByStoreIdVipKeyWord(Integer status, Integer isVip, String keyWord,
			PageQuery pageQuery, long registTimeStart, long registTimeEnd, String phoneNumber, String userName,
			long storeBusinessId, String storeBusinessName, String storeBusinessAddress,String referenceNumber) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("status", status);
		params.put("vip", isVip);
		params.put("keyWord", keyWord);
		params.put("pageQuery", pageQuery);
		params.put("registTimeStart", registTimeStart);
		params.put("registTimeEnd", registTimeEnd);
		params.put("phoneNumber", phoneNumber);
		params.put("userName", userName);
		params.put("storeBusinessId", storeBusinessId);
//		params.put("storeType", storeType);
		params.put("storeBusinessName", storeBusinessName);
		params.put("storeBusinessAddress", storeBusinessAddress);
		params.put("referenceNumber", referenceNumber);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getByStoreIdVipKeyWord", params);
	}

//	@Override
//	public List<String> getStoreBusinessUserCIDByStoreId(List<Long> storeIdList) {
//		HashMap<String, Object> params = new HashMap<String,Object>(); 
//		params.put("storeIdList", storeIdList);
//		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreBusinessUserCIDByStoreId", params);
//	}
	
	@Override
	public List<StoreBusiness> getSynProductStoreList() {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
	
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getSynProductStoreList",params);
	}




	@Override
	public List<String> getAllStoreBusinessUserCID() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getAllStoreBusinessUserCID");
	}

	@Override
	public List<String> getStoreBusinessUserCIDByStoreNumberList(List<Long> storeNumberList) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("storeNumberList", storeNumberList);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreBusinessUserCIDByStoreNumberList",params);
	}

	@Override
	public List<Long> getStoreIdListByStoreNumberList(List<Long> storeNumberList) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("storeNumberList", storeNumberList);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreIdListByStoreNumberList",params);
	}

	@Override
	public String getStoreBusinessUserCIDByStoreId(long storeId) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("storeId", storeId);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getStoreBusinessUserCIDByStoreId", params);
	}

	@Override
	public List<StoreBusiness> getReferrerPhoneNumberStoreList() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getReferrerPhoneNumberStoreList");
	}
	@Override
	public int updateBusinessQualificationProofImages(long storeId, String image) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("storeId", storeId);
		params.put("image", image);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateBusinessQualificationProofImages", params);
	}


	@Override
	public int updateSupplierIdByPhone(String phoneNumber,long supplierId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("supplierId", supplierId);
		params.put("phoneNumber", phoneNumber);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateSupplierIdByPhone",params);
	}
	
	@Override
	public int setOnlineWxaVersion(long storeId, String onlineWxaVersion) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("storeId", storeId);
		params.put("onlineWxaVersion", onlineWxaVersion);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.setOnlineWxaVersion",params);
	}



	@Override
	public int updateFirstLoginStatus(int firstLogin, long storeId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("firstLogin", firstLogin);
		params.put("storeId", storeId);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateFirstLoginStatus",params);
	}




	@Override
	public int updateStoreBusinessAuditStatusAndTime(long storeId, int status, long auditTime) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("storeId", storeId);
		params.put("status", status);
		params.put("auditTime", auditTime);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateStoreBusinessAuditStatusAndTime",params);
	}



	/**
	 * 更新门店店铺审核信息 版本3.7.2
	 * @param storeId
	 * @param dataAuditStatus
	 * @param dataAuditTime
	 * @return:
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/21 12:40
	 */
	@Override
	public int updateStoreAuditStatusAndTimeV372(long storeId, int dataAuditStatus, long dataAuditTime) {
		Map<String,Object> params = new HashMap<>();
		params.put("storeId", storeId);
		params.put("dataAuditStatus", dataAuditStatus);
		params.put("dataAuditTime", dataAuditTime);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.updateStoreAuditStatusAndTimeV372",params);
	}



	/**
	 * 通过店铺审核id获取用户版本
	 * @param: auditId
	 * @return: com.jiuyuan.entity.newentity.StoreBusiness
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/24 15:20
	 */
	@Override
	public String getAppIdByAuditId(Long auditId){
		Map<String,Object> params = new HashMap<>();
		params.put("auditId", auditId);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.getAppIdByAuditId",params);
	}




}