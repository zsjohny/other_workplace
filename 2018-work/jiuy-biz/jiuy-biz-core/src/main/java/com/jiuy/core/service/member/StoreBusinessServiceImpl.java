package com.jiuy.core.service.member;

import java.rmi.MarshalException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.jiuyuan.service.common.IYjjMemberServiceDeprecated;
import com.yujj.entity.product.YjjMember;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gexin.rp.sdk.base.uitls.MD5Util;
import com.jiuy.core.business.facade.ProductSkuFacade;
import com.jiuy.core.dao.AdminUserDao;
import com.jiuy.core.dao.MemberDao;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.StoreCommissionPercentageLogDao;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.member.StoreBusinessSearch;
import com.jiuy.core.service.actionlog.ActionLogService;
import com.jiuyuan.constant.Status;
import com.jiuyuan.constant.UserStatusLogType;
import com.jiuyuan.dao.mapper.supplier.SupplierCustomerMapper;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.StoreCommissionPercentageLog;
import com.jiuyuan.entity.StoreRegister;
import com.jiuyuan.entity.StroreRegisterVO;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.ISupplierCustomer;
import com.jiuyuan.service.common.YunXinSmsService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * @author WuWanjian
 * @version 创建时间: 2016年10月25日 下午3:37:13
 */
@Service("storeBusinessService")
public class StoreBusinessServiceImpl implements StoreBusinessService {
	
	 private static final Logger logger = LoggerFactory.getLogger(ProductSkuFacade.class);
	    Log log = LogFactory.get();
	    
	@Autowired
	private AdminUserDao adminUserDao;	
		
	@Autowired
	private IYjjMemberServiceDeprecated memberServiceDeprecated;

	@Autowired
	private ActionLogService actionLogService;

	private static final String BUSINESS_NUMBER_PREFIX = "800";

	private static final int DEFAULT_NUMBER_SIZE = 6;

	@Resource
	private StoreBusinessDao storeBusinessDao;

	@Resource
	private YunXinSmsService yunXinSmsService;

	@Resource
	private MemberDao memberDaoSqlImpl;

	@Resource
	private BrandBusinessService brandbusinessService;

	@Resource
	private StoreBusinessService storeBusinessService;
	
	@Resource
	private SupplierCustomerMapper supplierCustomerMapper;
	
	@Resource
	private StoreCommissionPercentageLogDao storeCommissionPercentageLogDao;
	@Override
	public List<StoreBusiness> search(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery) {
		return storeBusinessDao.search(storeBusinessSearch, pageQuery);
	}
	
	public List<StoreBusiness> getSynProductStoreList(){
		return storeBusinessDao.getSynProductStoreList();
	}

	@Override
	public int searchCount(StoreBusinessSearch storeBusinessSearch, long superStoreId) {

		return storeBusinessDao.searchCount(storeBusinessSearch, superStoreId);
	}

	/**
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void add(StoreBusiness storeBusiness,StoreBusiness superStore) {
		long currentTimeMillis = System.currentTimeMillis();
		storeBusiness.setCreateTime(currentTimeMillis);
		storeBusiness.setUpdateTime(currentTimeMillis);
		
		if(superStore != null){ //有上级商家号
			long superBusinessId = superStore.getId();
			if(superBusinessId != 0){
				StoreBusiness superStoreBusiness =	storeBusinessDao.searchBusinessById(superBusinessId);
				String superIds = superStoreBusiness.getSuperBusinessIds();
				if(StringUtils.isNotEmpty(superIds)){//上级门店不是1级门店
					storeBusiness.setSuperBusinessIds(superStoreBusiness.getSuperBusinessIds()+superStoreBusiness.getId()+",");
					storeBusiness.setMemberCommissionPercentage(0);
				}else {//上级门店是1级门店
					storeBusiness.setSuperBusinessIds(","+superStoreBusiness.getId()+",");
					storeBusiness.setMemberCommissionPercentage(superStoreBusiness.getDefaultCommissionPercentage());
				}
				storeBusiness.setDeep(superStoreBusiness.getDeep() + 1);
			}else{
				storeBusiness.setDeep(1L);
			}
		}
				
		storeBusinessDao.add(storeBusiness);
		long businessId = storeBusiness.getId();
		// 生成商家号并写入数据库
		String businessNumber = generateBusinessNumber(String.valueOf(businessId));
		String password = generatePassword();
		String passwordMD5 = MD5Util.getMD5Format(password);
		storeBusinessDao.updateBusinessNumberAndUserName(businessNumber, businessNumber, passwordMD5, businessId);

		// 默认关联所有品牌
		List<BrandLogo> brandList = brandbusinessService.getBrandList();
		ArrayList<Long> brandIds = new ArrayList<Long>();
		for (BrandLogo brandLogo : brandList) {
			brandIds.add(brandLogo.getBrandId());
		}

		storeBusinessDao.addBrandRelationOfStoreId(String.valueOf(businessId), 0, System.currentTimeMillis(), brandIds);
		// 发送初始密码短信通知
		sendCode(storeBusiness.getPhoneNumber(), password);
	}

	// 生成商家号
	private String generateBusinessNumber(String id) {
		int count = DEFAULT_NUMBER_SIZE - id.length();
		StringBuffer stringBuffer = new StringBuffer();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				stringBuffer.append("0");
			}
		}

		return BUSINESS_NUMBER_PREFIX + stringBuffer.toString() + id;

	}

	// 生成6位随机密码
	private String generatePassword() {
		Random random = new Random();
		String password = "";
		for (int i = 0; i < 6; i++) {
			int nextInt = random.nextInt(10);
			password += nextInt;
		}
		return password;
	}

	private void sendCode(String phone, String password) {
		JSONArray params = new JSONArray();
		params.add(password);// 初始密码
		params.add("4001180099");// 400电话
		yunXinSmsService.send(phone, params, 6110);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateStoreBusiness(StoreBusiness storeBusiness,AdminUser userInfo) {
		long currentTime = System.currentTimeMillis();
		storeBusiness.setUpdateTime(currentTime);

		long id = storeBusiness.getId();

		int oldStatus = storeBusinessDao.getStatusOfStoreId(id);
		if (storeBusiness.getStatus() != oldStatus) {
			memberDaoSqlImpl.addUserStatusLog(id, UserStatusLogType.STORESTATUS.getIntValue(), oldStatus,
					storeBusiness.getStatus(), currentTime);
		}

		int oldDistributionStatus = storeBusinessDao.getDistributionStatusOfStoreId(id);
		if (storeBusiness.getDistributionStatus() != oldDistributionStatus) {
			memberDaoSqlImpl.addUserStatusLog(id, UserStatusLogType.STOREDISTRIBUTIONSTATUS.getIntValue(),
					oldDistributionStatus, storeBusiness.getDistributionStatus(), currentTime);
		}

		// 提款信息后有变化时需重新激活
		StoreBusiness oldStoreBusiness = storeBusinessDao.getByStoreId(id);
		if (oldStoreBusiness.getAlipayAccount() != null && oldStoreBusiness.getAlipayName() != null
				&& oldStoreBusiness.getWeixinAccount() != null && oldStoreBusiness.getWeixinName() != null
				&& oldStoreBusiness.getBankAccountName() != null && oldStoreBusiness.getBankAccountNo() != null
				&& oldStoreBusiness.getBankName() != null) {
			if (!oldStoreBusiness.getAlipayAccount().equals(oldStoreBusiness.getAlipayAccount())
					|| !oldStoreBusiness.getAlipayName().equals(oldStoreBusiness.getAlipayName())
					|| oldStoreBusiness.getAlipayFlag() != oldStoreBusiness.getAlipayFlag()
					|| !oldStoreBusiness.getWeixinAccount().equals(oldStoreBusiness.getWeixinAccount())
					|| !oldStoreBusiness.getWeixinName().equals(oldStoreBusiness.getWeixinName())
					|| oldStoreBusiness.getWeixinFlag() == oldStoreBusiness.getWeixinFlag()
					|| !oldStoreBusiness.getBankAccountName().equals(oldStoreBusiness.getBankAccountName())
					|| !oldStoreBusiness.getBankAccountNo().equals(oldStoreBusiness.getBankAccountNo())
					|| !oldStoreBusiness.getBankName().equals(oldStoreBusiness.getBankName())
					|| oldStoreBusiness.getBankCardFlag() == oldStoreBusiness.getBankCardFlag()) {
				storeBusiness.setActiveTime(0L);
			}
		}
		
		if(userInfo != null){
			//采购提成变化记录
			if(oldStoreBusiness.getCommissionPercentage() != storeBusiness.getCommissionPercentage()){
				StoreCommissionPercentageLog storeCommissionPercentageLog = new StoreCommissionPercentageLog(id,1,userInfo.getUserId(),oldStoreBusiness.getCommissionPercentage(),storeBusiness.getCommissionPercentage(),System.currentTimeMillis());
				 storeCommissionPercentageLogDao.insertLog(storeCommissionPercentageLog);
			}
			//分销提成变化记录
			if(oldStoreBusiness.getMemberCommissionPercentage() != storeBusiness.getMemberCommissionPercentage()){
				StoreCommissionPercentageLog storeCommissionPercentageLog = new StoreCommissionPercentageLog(id,2,userInfo.getUserId(),oldStoreBusiness.getMemberCommissionPercentage(),storeBusiness.getMemberCommissionPercentage(),System.currentTimeMillis());
				 storeCommissionPercentageLogDao.insertLog(storeCommissionPercentageLog);
			}
		}
	
		return storeBusinessDao.updateStoreBusiness(storeBusiness);
	}

	@Override
	public List<Province> getProvinceList() {
		return storeBusinessDao.getProvinceList();
	}

	@Override
	public List<City> getCitysByProvinceId(long parentId) {
		return storeBusinessDao.getCitysByProvinceId(parentId);
	}

	@Override
	public List<BrandLogo> getRelationBrandList(long storeId) {
		List<Long> brandIds = storeBusinessDao.getRelationBrandIdOfStoreId(storeId);
		List<BrandLogo> brandsOfBrandIds = new ArrayList<BrandLogo>();
		if (brandIds.size() > 0) {
			brandsOfBrandIds = storeBusinessDao.getBrandsOfBrandIds(brandIds);
		}

		return brandsOfBrandIds;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateBrandRelation(String storeIds, String brandIdsString) {
		int count = storeBusinessDao.deleteBrandRelationOfStoreId(storeIds);

		if (StringUtils.equals("", brandIdsString)) {
			return count;
		}
		Set<Long> brandIdList = transBrandIdsStrToList(brandIdsString);
		List<String> listIds = splitString(storeIds);
		int totalCount = 0;
		if (listIds != null && listIds.size() > 0) {
			for (String storeId : listIds) {
				int brandCount = storeBusinessDao.addBrandRelationOfStoreId(storeId, 0, System.currentTimeMillis(),
						brandIdList);
				totalCount += brandCount;
			}
			return totalCount;
		} else {
			return 0;
		}
	}

	@Override
	public int updateAllBrandRelation(String brandIdsString, List<Long> storeIds) {
		// 先删除所有的门店和品牌的对应关系
		storeBusinessDao.deleteBrandRelationOfStoreIds(storeIds);

		if (StringUtils.equals("", brandIdsString)) {
			return 0;
		}

		Set<Long> brandIdList = transBrandIdsStrToList(brandIdsString);
		int totalCount = 0;
		if (storeIds != null && storeIds.size() > 0) {
			for (Long storeId : storeIds) {
				int brandCount = storeBusinessDao.addBrandRelationOfStoreId(String.valueOf(storeId), 0,
						System.currentTimeMillis(), brandIdList);
				totalCount += brandCount;
			}
			return totalCount;
		} else {
			return 0;
		}

	}

	private List<String> splitString(String storeIds) {
		List<String> list = new ArrayList<>();
		if (!storeIds.isEmpty()) {
			String[] splits = storeIds.split(",");
			for (String string : splits) {
				list.add(string);
			}
			return list;
		} else {
			return list;
		}
	}

	private Set<Long> transBrandIdsStrToList(String brandIdsString) {
		String[] brandIdsStr = brandIdsString.split(",");

		Set<Long> brandIds = new HashSet<Long>();
		for (String brandId : brandIdsStr) {
			brandIds.add(Long.parseLong(brandId));
		}

		return brandIds;
	}

	@Override
	public Map<Long, StoreBusiness> storesMapOfIds(Set<Long> userIds) {
		if (userIds.isEmpty())
			return new HashMap<Long, StoreBusiness>();

		Map<Long, StoreBusiness> users = storeBusinessDao.userOfIdsMap(userIds);

		return users;
	}

	@Override
	public Map<Long, StoreBusiness> storesMapOfNumbers(List<Long> storeNumbers) {
		if (storeNumbers.isEmpty())
			return new HashMap<Long, StoreBusiness>();

		Map<Long, StoreBusiness> users = storeBusinessDao.userOfNumbersMap(storeNumbers);

		return users;
	}

	@Override
	public StoreBusiness searchBusinessById(long belongStoreId) {
		return storeBusinessDao.searchBusinessById(belongStoreId);
	}

	@Override
	public StoreBusiness searchBusinessByBusinessNumber(long businessNumber) {
		return storeBusinessDao.searchBusinessByBusinessNumber(businessNumber);
	}

	@Override
	public StoreBusiness getByLOWarehouseId(long loWarehouseId) {
		return storeBusinessDao.getByLOWarehouseId(loWarehouseId);
	}

	@Override
	public List<StoreRegister> getStoreRegister(PageQuery pageQuery, StroreRegisterVO stroreRegisterVO) {
		List<StoreRegister> storeRegisters = storeBusinessDao.getStoreRegister(pageQuery, stroreRegisterVO);
		if (storeRegisters != null && storeRegisters.size() > 0) {
			for (StoreRegister storeRegister : storeRegisters) {
				if (storeRegister.getSuperBusinessId() != 0) {// 有上级门店信息的
					StoreBusiness storeBusiness = storeBusinessDao.getByStoreId(storeRegister.getSuperBusinessId());
					if(storeBusiness!=null){
						storeRegister.setSuperStoreNameAndNumber(
								storeBusiness.getBusinessName() + "(" + storeBusiness.getBusinessNumber() + ")");
					}
				} else {
					storeRegister.setSuperStoreNameAndNumber("无");
				}
			}
		}
		return storeRegisters;
	}

	@Override
	public int getStoreRegisterCount(StroreRegisterVO stroreRegisterVO) {
		return storeBusinessDao.getStoreRegisterCount(stroreRegisterVO);
	}

	@Override
	public Map<String, Object> getRegisterSummary(StroreRegisterVO stroreRegisterVO) {
		return storeBusinessDao.getRegisterSummary(stroreRegisterVO);
	}

	@Override
	public int updateStoreRegisterForStatus(int status, String applyIds, String applyMemo, String userName, long userId,
			long nowTime) {
		return storeBusinessDao.updateStoreRegisterForStatus(status, applyIds, applyMemo, userName, nowTime, userId);
	}

	@Override
	public List<StoreRegister> getStoreregistersForIds(String applyIds) {
		return storeBusinessDao.getStoreregistersForIds(applyIds);
	}

	@Override
	public int upDateStoreRegister(StoreRegister storeRegister) {

		return storeBusinessDao.upDateStoreRegister(storeRegister);
	}

	@Override
	public int searchPersonCount(StoreBusinessSearch storeBusinessSearch, long superStoreId) {
		return storeBusinessDao.searchPersonCount(storeBusinessSearch, superStoreId);
	}

	@Override
	public int setStatusForIds(int status, String ids) {
		return storeBusinessDao.setStatusForIds(status, ids);
	}

	@Override
	public int setDistributionStatusForIds(int status, String ids) {
		return storeBusinessDao.setDistributionStatusForIds(status, ids);
	}

	@Override
	public int searchStoreCount(int status) {
		return storeBusinessDao.searchStoreCount(status);
	}

	@Override
	public StoreRegister getStoreRegisterByPhoneNumber(String phoneNumber) {
		return storeBusinessDao.getStoreRegisterByPhoneNumber(phoneNumber);
	}

	@Override
	public int updateMoney(StoreBusiness storeBusiness) {
		return storeBusinessDao.updateMoney(storeBusiness);
	}

	@Override
	public List<StoreBusinessSearch> searchStoreAndMember(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery, long superStoreId, Integer membStatus) {
        memberServiceDeprecated.clearHistoryDirtyData ();
		List<StoreBusinessSearch> result = storeBusinessDao.searchStoreAndMember (storeBusinessSearch, pageQuery, superStoreId, membStatus);
		for (StoreBusinessSearch action : result) {
            Long membEndTime = action.getMembEndTime ();
            Integer membLevel = action.getMembLevel ();
            Integer membDelState = action.getMembDelState ();
            if (membLevel == null || membLevel == 0) {
				//非会员
				action.setMembStatus (0);
			}
			else {
				if (membDelState !=0 ) {
					//会员关闭
					action.setMembStatus (0);
				}
				else if (membEndTime <= System.currentTimeMillis ()){
					//会员过期
					action.setMembStatus (0);
				}
				else {
					//正常
					action.setMembStatus (1);
				}
			}
		};
		return  result;
	}


	@Override
	public List<StoreBusiness> search(StoreBusinessSearch storeBusinessSearch, PageQuery pageQuery, long superStoreId) {
		List<StoreBusiness> storeBusinesses = storeBusinessDao.search(storeBusinessSearch, pageQuery, superStoreId);
		// 合并business时注释掉的，如果需要以下业务请用信息bean方式实现一下
//		if (storeBusinesses != null && storeBusinesses.size() > 0) {
//			for (StoreBusiness storeBusiness : storeBusinesses) {
//				String superIds = storeBusiness.getSuperBusinessIds();
//				if (StringUtils.isNotEmpty(superIds)) {
//					try {
//						String superId = superIds.split(",")[superIds.split(",").length - 1];
//						StoreBusiness sBusiness = storeBusinessDao.getByStoreId(Long.parseLong(superId));
//						if(sBusiness!=null){
//							storeBusiness.setSuperStoreNameAndNumber(
//									sBusiness.getBusinessName() + "(" + sBusiness.getBusinessNumber() + ")");
//							storeBusiness.setSuperBusinessNumber(sBusiness.getBusinessNumber());
//						}
//					} catch (NumberFormatException e) {
//						storeBusiness.setSuperStoreNameAndNumber("无");
//					}
//				} else {
//					storeBusiness.setFirstStore(true);
//					storeBusiness.setSuperStoreNameAndNumber("无");
//				}
//			}
//		}
		return storeBusinesses;
	}

	/**
	 * 设置门店为vip/非vip
	 */
	@Override
	public boolean setVIP(long storeId,int vip,long userId) {
		AdminUser adminUser = adminUserDao.getUser(userId);
		
		StoreBusiness storebusiness = storeBusinessDao.searchBusinessById(storeId);
		if(vip==1 && storebusiness.getVip()==1){
			throw new RuntimeException("该门店已经是vip门店");
		}else if(vip==0 && storebusiness.getVip()==0){
			throw new RuntimeException("该门店已经不是vip门店");
		}
		int record = storeBusinessDao.setVIP(storeId,vip);
		if(record!=1){
			return false;
		}else{
			//设置门店VIP操作日志
			int ret = actionLogService.setStoreAction(storebusiness,vip,adminUser);
			logger.info("设置VIP商品操作日志，ret："+ret);
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int toSaveUpdate(StoreBusiness storeBusiness) {
		Wrapper<SupplierCustomer> wrapper = new EntityWrapper<SupplierCustomer>();
		wrapper.eq("store_id", storeBusiness.getId())
		       .eq("status", Status.NORMAL.getIntValue());
		List<SupplierCustomer> list = supplierCustomerMapper.selectList(wrapper);
		String city = storeBusiness.getCity();
		String province = storeBusiness.getProvince();
		String businessAddress = storeBusiness.getBusinessAddress();
		String county = storeBusiness.getCounty();
		if(list.size() > 0 ){
				Set<Long> set = new HashSet<Long>();
				for(SupplierCustomer supplierCustomer : list){
					Long storeId = supplierCustomer.getStoreId();
					set.add(storeId);
				}
				supplierCustomerMapper.updateSupplierCustomerAddressInfoByStoreIds(set, city, province, businessAddress, county);
		}
		return storeBusinessDao.updateStoreBusiness(storeBusiness);
	}
	
	/**
	 * 获取所有门店的userCID
	 */
	@Override
	public List<String> getAllStoreBusinessUserCID() {
		return storeBusinessDao.getAllStoreBusinessUserCID();
	}

	/**
	 * 获取指定门店的storeId
	 */
	@Override
	public List<Long> getStoreIdListByStoreNumberList(List<Long> storeNumberList) {
		return storeBusinessDao.getStoreIdListByStoreNumberList(storeNumberList);
	}
	
	/**
	 * 获取指定门店的userCID
	 */
	@Override
	public List<String> getStoreBusinessUserCIDByStoreNumberList(List<Long> storeNumberList) {
		return storeBusinessDao.getStoreBusinessUserCIDByStoreNumberList(storeNumberList);
	}

	/**
	 * 获取指定门店的userCID
	 */
	@Override
	public String getStoreBusinessUserCIDByStoreId(long storeId) {
		return storeBusinessDao.getStoreBusinessUserCIDByStoreId(storeId);
	}

	@Override
	public int updateSupplierIdByPhone(String phoneNumber,long supplierId) {
		return storeBusinessDao.updateSupplierIdByPhone(phoneNumber,supplierId);
	}

	@Override
	public int setOnlineWxaVersion(long storeId, String onlineWxaVersion) {
		return storeBusinessDao.setOnlineWxaVersion(storeId,onlineWxaVersion);
	}

}