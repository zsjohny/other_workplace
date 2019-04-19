package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.member.StoreBusinessSearch;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.StoreRegister;
import com.jiuyuan.entity.StroreRegisterVO;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.withdraw.WithDrawApply;
import org.apache.ibatis.annotations.Param;

/**
* @author WuWanjian
* @version 创建时间: 2016年10月25日 上午11:37:39
*/
public interface StoreBusinessDao {
	
	public List<StoreBusiness> search(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery);
	
	public int searchCount(StoreBusinessSearch storeBusinessSearch, long superStoreId);
	
	public long getIdByBusinessNumber(long businessNumber);
	
	public long add(StoreBusiness storeBusiness);
	
	public int updateStoreBusiness(StoreBusiness storeBusiness);
	
	public int updateBusinessNumberAndUserName(String businessNumber, String userName,String userPassword, long id);

	public List<Province> getProvinceList();
	
	public List<City> getCitysByProvinceId(long parentId);
	
	public int decreaseMemberNumberById(long id);
	
	public List<Long> getRelationBrandIdOfStoreId(long storeId);
	
	public List<BrandLogo> getBrandsOfBrandIds(Collection<Long> brandIds);
	
	public int deleteBrandRelationOfStoreId(String storeIds);
	
	public int addBrandRelationOfStoreId(String storeId,int status,long createTime,Collection<Long> brandIds);
	
	public long checkBusinessName(String businessName);
	
	public long checkPhoneNumber(String phoneNumber);

	public List<StoreBusiness> userOfIds(Set<Long> userIds);
	
	public Map<Long, StoreBusiness> userOfIdsMap(Set<Long> userIds);
	
	public Map<Long, StoreBusiness> userOfNumbersMap(List<Long> storeNumbers);
	
	public StoreBusiness getByStoreId(long storeId);
	
	public int getStatusOfStoreId(long storeId);
	
	public int getDistributionStatusOfStoreId(long storeId);

	public StoreBusiness searchBusinessById(long belongStoreId);
	
	public StoreBusiness searchBusinessByBusinessNumber(long businessNumber);

	public int increseAvaliableBalance(long id, double availableCommission);

	public int reduceIncome(long id, double commission);

	public StoreBusiness getByLOWarehouseId(long loWarehouseId);

	public List<StoreBusiness> getBlurByBusinessNumber(Long businessNumber);

	public List<StoreRegister> getStoreRegister(PageQuery pageQuery, StroreRegisterVO stroreRegisterVO);

	public int getStoreRegisterCount(StroreRegisterVO stroreRegisterVO);

	public Map<String, Object> getRegisterSummary(StroreRegisterVO stroreRegisterVO);

	public int updateStoreRegisterForStatus(int status, String applyIds, String applyMemo, String userName, long nowTime, long userId);

	public List<StoreRegister> getStoreregistersForIds(String applyIds);

	public int checkPhoneNumbers(String phoneNumbers);

	public int upDateStoreRegister(StoreRegister storeRegister);

	public int searchPersonCount(StoreBusinessSearch storeBusinessSearch, long superStoreId);

	public int setStatusForIds(int status, String ids);

	public int setDistributionStatusForIds(int status, String ids);
	
	public int searchStoreCount(int status);

	public StoreRegister getStoreRegisterByPhoneNumber(String phoneNumber);

	public int deleteAllBrandRelation();
	
	public List<Long> getAllStoreBusinessIds();
	
	public int deleteBrandRelationOfStoreIds(Collection<Long> storeIds);
	public int updateMoney(StoreBusiness storeBusiness);

	public List<StoreBusiness> search(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery, long superStoreId);

	public int updateStoreActiveTime(long storeId, long activeTime);

	public List<StoreBusiness> getStoreIncomeIds();

	public List<WithDrawApply> getOnProcess(long id, int i);

	public int setVIP(long storeId,int vip);


	public List<StoreBusiness> getByStoreIdVipKeyWord(Integer status, Integer isVip, String keyWord, PageQuery pageQuery,
			long startTime, long endTime, String phoneNumber, String userName,
			long storeBusinessId, String storeBusinessName, String storeBusinessAddress, String referenceNumber);
	/**
	 * 添加 开通小程序的开通时间和结束时间
	 * @param storeId
	 * @param openWxaState
	 * @param currentTime
	 * @param afterOneYear
	 */

	public int setOpenWxa(long storeId, int openWxaState,long currentTime,long afterOneYear);

//	public List<String> getStoreBusinessUserCIDByStoreId(List<Long> storeIdList);
	public int setWxaType(long storeId, int wxaType);

	public List<StoreBusiness> getSynProductStoreList();

	public List<String> getAllStoreBusinessUserCID();

	public List<String> getStoreBusinessUserCIDByStoreNumberList(List<Long> storeNumberList);

	public List<Long> getStoreIdListByStoreNumberList(List<Long> storeNumberList);

	public String getStoreBusinessUserCIDByStoreId(long storeId);

	public List<StoreBusiness> getReferrerPhoneNumberStoreList();

	public int updateBusinessQualificationProofImages(long storeId, String image);

	public int updateSupplierIdByPhone(String phoneNumber,long supplierId);

	public int updateFirstLoginStatus(int firstLogin, long storeId);

	public int updateStoreBusinessAuditStatusAndTime(long storeId, int status, long auditTime);

	public int setOnlineWxaVersion(long storeId, String onlineWxaVersion);

	/**
	 * 更新门店店铺审核信息 版本3.7.2
	 * @param storeId
	 * @param dataAuditStatus
	 * @param dataAuditTime
	 * @return: 
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/21 12:40
	 */
	int updateStoreAuditStatusAndTimeV372(long storeId, int dataAuditStatus, long dataAuditTime);

	/**
	 * 通过店铺审核id获取用户版本
	 * @param: auditId
	 * @return: com.jiuyuan.entity.newentity.StoreBusiness
	 * @auther: Charlie(唐静)
	 * @date: 2018/5/24 15:20
	 */
	String getAppIdByAuditId(Long auditId);

	List<StoreBusinessSearch> searchStoreAndMember(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery, long superStoreId, Integer membStatus);


}