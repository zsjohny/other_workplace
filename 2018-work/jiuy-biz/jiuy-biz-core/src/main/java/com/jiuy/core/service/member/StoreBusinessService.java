package com.jiuy.core.service.member;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.member.StoreBusinessSearch;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.StoreRegister;
import com.jiuyuan.entity.StroreRegisterVO;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;


/**
* @author WuWanjian
* @version 创建时间: 2016年10月25日 下午3:35:59
*/
public interface StoreBusinessService {
	
	public List<StoreBusiness> search(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery);
	
	public int searchCount(StoreBusinessSearch storeBusinessSearch, long superStoreId);
	
	public void add(StoreBusiness storeBusiness,StoreBusiness superStore);
	
	public int updateStoreBusiness(StoreBusiness storeBusiness, AdminUser userinfo);
	
	public List<Province> getProvinceList();
	
	public List<City> getCitysByProvinceId(long parentId);
	
	public List<BrandLogo> getRelationBrandList(long businessNumber);
	
	public int updateBrandRelation(String storeIds,String brandIdString);

	public Map<Long, StoreBusiness> storesMapOfIds(Set<Long> userIds);
	
	public Map<Long, StoreBusiness> storesMapOfNumbers(List<Long> storeNumbers);

	public StoreBusiness searchBusinessById(long belongStoreId);
	
	public StoreBusiness searchBusinessByBusinessNumber(long businessNumber);

	public StoreBusiness getByLOWarehouseId(long getlOWarehouseId);

	public List<StoreRegister> getStoreRegister(PageQuery pageQuery, StroreRegisterVO stroreRegisterVO);

	public int getStoreRegisterCount(StroreRegisterVO stroreRegisterVO);

	public Map<String, Object> getRegisterSummary(StroreRegisterVO stroreRegisterVO);

	public int updateStoreRegisterForStatus(int i, String applyIds, String applyMemo, String userName, long userId, long nowTime);

	public List<StoreRegister> getStoreregistersForIds(String applyIds);

	public int upDateStoreRegister(StoreRegister storeRegister);

	public int searchPersonCount(StoreBusinessSearch storeBusinessSearch, long superStoreId);

	public int setStatusForIds(int status, String ids);

	public int setDistributionStatusForIds(int status, String ids);
	
	/**获取正常的门店总数量*/
	public int searchStoreCount(int status);

	public StoreRegister getStoreRegisterByPhoneNumber(String phoneNumber);
	
	public int updateAllBrandRelation(String brandIdString,List<Long> storeIds); 

	public int updateMoney(StoreBusiness storeBusiness);

	List<StoreBusinessSearch> searchStoreAndMember(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery, long superStoreId, Integer membStatus);

	public List<StoreBusiness> search(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery,
									  long superStoreId);

	public boolean setVIP(long storeId,int vip,long userId);

	public int toSaveUpdate(StoreBusiness storeBusiness);

	public List<StoreBusiness> getSynProductStoreList();

	public List<String> getAllStoreBusinessUserCID();

	public List<String> getStoreBusinessUserCIDByStoreNumberList(List<Long> storeNumberList);

	public List<Long> getStoreIdListByStoreNumberList(List<Long> storeNumberList);

	public String getStoreBusinessUserCIDByStoreId(long storeId);


	public int updateSupplierIdByPhone(String phoneNumber,long supplierId);

	public int setOnlineWxaVersion(long storeId, String onlineWxaVersion);
}
