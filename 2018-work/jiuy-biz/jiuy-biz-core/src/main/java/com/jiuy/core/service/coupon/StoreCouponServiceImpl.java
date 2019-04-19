package com.jiuy.core.service.coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuy.core.dao.GlobalSettingDao;
import com.jiuy.core.dao.StoreCouponDao;
import com.jiuy.core.dao.StoreCouponTemplateDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.constant.coupon.PublishObjectType;
import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class StoreCouponServiceImpl implements StoreCouponService {

	private static final Logger logger = Logger.getLogger(StoreCouponServiceImpl.class);
	
	private static final String PHONE_NUMBER_COLUMN = "PhoneNumber";
	
	private static final String BUSINESS_NUMBER_COLUMN = "BusinessNumber";
	
	private static final int NORMAL_STATUS = 0;
	
	@Autowired
	private StoreCouponDao storeCouponDao;
	
	@Autowired
	private UserManageService userManageService;
	
	@Autowired
	private StoreBusinessService storeBusinessService;
	
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private GlobalSettingDao globalSettingDao;

	@Autowired
	private StoreCouponTemplateDao storeCouponTemplateDao;
	
	@Autowired
	private StoreMapper storeMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
    public synchronized int batchAdd(Integer publishCount, StoreCouponTemplate storeCouponTemplate, Long adminId) {
		List<StoreCouponTemplate> storeCouponTemplates = new ArrayList<StoreCouponTemplate>();
		for(int i = 0; i < publishCount; i++) {
			storeCouponTemplates.add(storeCouponTemplate);
		}
		// 批量新增优惠券
		storeCouponDao.batchAdd(storeCouponTemplates, adminId, null);
		// 查询该模版下没有兑换码的优惠券
		List<StoreCoupon> storeCoupons = storeCouponDao.getNullCode(storeCouponTemplates);
		// 生成兑换码
        generateCode(storeCoupons);
		// 更新优惠券兑换码
		return storeCouponDao.batchUpdate(storeCoupons);
	}

    private String generateCode(List<StoreCoupon> storeCoupons) {
		for(StoreCoupon storeCoupon : storeCoupons) {
            String prefixCode = String.format("%04d", storeCoupon.getId());
            storeCoupon.setCode(prefixCode);
		}
		return null;
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
	public synchronized int batchGrant(Integer publishCount, StoreCouponTemplate storeCouponTemplate, List<Long> storeNumberList,
            Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId) {
    	Long templateId = storeCouponTemplate.getId();
    	List<StoreCoupon> storeCoupons = storeCouponDao.getAvaliable(templateId);
    	Integer grantCount = publishCount;
    	
    	if (storeCoupons.size() < grantCount) {
			throw new ParameterErrorException("当前已发行的可用优惠券不足！");
		}
    	
    	generalGrantCoupon(grantCount, storeCoupons, storeNumberList, pushStatus, pushTitle, pushDescription, pushUrl, pushImage, adminId);
		
        return storeCouponDao.batchUpdate(storeCoupons);
	}

    private void generalGrantCoupon(Integer count, List<StoreCoupon> storeCoupons, List<Long> storeNumberList, Integer pushStatus,
                                 String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId) {
		// 根据商家号查询门店
    	Map<Long, StoreBusiness> storeBusinessMap = storeBusinessService.storesMapOfNumbers(storeNumberList);
        for (int i = 0; i < count; i++) {
        	StoreCoupon storeCoupon = storeCoupons.get(i);
        	Long storeNumber = storeNumberList.get(i);
            storeCoupon.setBusinessNumber(storeNumber);
            StoreBusiness store = storeBusinessMap.get(storeNumber);
            if (store == null) {
            	logger.error("com.jiuy.core.service.coupon.CouponServiceImpl: " + storeNumber + "该门店号对应的用户为空");
            	throw new ParameterErrorException("com.jiuy.core.service.coupon.CouponServiceImpl: " + storeNumber + "该门店号对应的用户为空");
            }
            
            storeCoupon.setStoreId(store.getId());
            storeCoupon.setPushStatus(pushStatus);
            storeCoupon.setPushTitle(pushTitle);
            storeCoupon.setPushDescription(pushDescription);
            storeCoupon.setPushUrl(pushUrl);
            storeCoupon.setPushImage(pushImage);
            storeCoupon.setGrantAdminId(adminId);
            storeCoupon.setStatus(0);
        }
    }

    @Override
    public int searchCount(StoreCoupon storeCoupon, Double moneyMin, Double moneyMax , Double limitMoneyMin , Double limitMoneyMax) {
        return storeCouponDao.searchCount(storeCoupon, moneyMin, moneyMax, limitMoneyMin , limitMoneyMax);
    }

    @Override
    public List<StoreCoupon> search(PageQuery pageQuery, StoreCoupon storeCoupon, Double moneyMin, Double moneyMax, Double limitMoneyMin , Double limitMoneyMax) {
        return storeCouponDao.search(pageQuery, storeCoupon, moneyMin, moneyMax, limitMoneyMin , limitMoneyMax);
    }

    @Override
    public int update(Long id, Integer status) {
    	//作废加回可发行量
    	if (status == -1) {
    		
    		List<GlobalSetting> settings = globalSettingDao.getItems(CollectionUtil.createList(GlobalSettingName.STORE_COUPON.getStringValue()));
    		if (settings.size() < 1) {
    			throw new ParameterErrorException("作废失败,全局变量设置优惠券字段不存在！");
    		}
    		String couponJson = settings.get(0).getPropertyValue();

	        JSONObject jsonObject = JSONObject.parseObject(couponJson);
	        double publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());
	        StoreCoupon storeCoupon = storeCouponDao.search(id);
	        if (storeCoupon == null) {
	        	throw new ParameterErrorException("作废失败,优惠券id不存在！");
			}
	        publishedMoney -= storeCoupon.getMoney();
	        jsonObject.put("published_money", publishedMoney);
	        globalSettingDao.update(GlobalSettingName.STORE_COUPON.getStringValue(), jsonObject.toJSONString());
		}
        return storeCouponDao.update(id, status);
    }

    @Override
    public List<StoreCoupon> search(Integer pushStatus) {
        return storeCouponDao.search(pushStatus);
    }

	@Override
	public int update(Long id, Long userId, Long yjjNumber, Integer status, Integer pushStatus, String pushTitle, String pushDescription,
                      String pushUrl, String pushImage, Long adminId) {
        return storeCouponDao.update(id, userId, yjjNumber, status, pushStatus, pushTitle, pushDescription, pushUrl, pushImage, adminId);
	}

	@Override
	public int searchCount(Integer status, Long templateId) {
		return storeCouponDao.searchCount(status, templateId);
	}

	@Override
	public int searchAvailableCount(Long templateId) {
		return storeCouponDao.searchAvailableCount(templateId);
	}

	@Override
	public int searchExpiredCount(Long templateId) {
		return storeCouponDao.searchExpiredCount(templateId);
	}

	@Override
	public Map<Long, Integer> availableOfTemplateId(Collection<Long> templateIds) {
		if (templateIds.size() < 1) {
			return new HashMap<Long, Integer>();
		}
		List<Map<String, Object>> list = storeCouponDao.availableOfTemplateId(templateIds);
		
		Map<Long, Integer> avaliableMap = new HashMap<Long, Integer>();
		for (Map<String, Object> map : list) {
			avaliableMap.put(Long.parseLong(map.get("couponTemplateId").toString()), Integer.parseInt(map.get("count").toString()));
		}
		
		return avaliableMap;
	}

	@Override
	public Map<Long, List<StoreCoupon>> getCounponByOrderNo(Collection<Long> orderNos, String sortSql) {
		if (orderNos.size() < 1) {
			return new HashMap<Long, List<StoreCoupon>>();
		}
		
		List<StoreCoupon> storeCoupons = storeCouponDao.search(orderNos, sortSql);
		Map<Long, List<StoreCoupon>> map = new HashMap<Long, List<StoreCoupon>>();
		List<StoreCoupon> coList = null;
		Long lastOrderNo = -1L;
		for (StoreCoupon storeCoupon : storeCoupons) {
			Long orderNo = storeCoupon.getOrderNo();
			if (lastOrderNo != orderNo) {
				lastOrderNo = orderNo;
				coList = new ArrayList<StoreCoupon>();
				map.put(lastOrderNo, coList);
			}
			coList.add(storeCoupon);
		}
		
		return map;
	}

	@Override
	public int giveBack(Collection<Long> couponIds) {
		return storeCouponDao.update(0, 0L, couponIds, System.currentTimeMillis());
	}

	@Override
	public int updatePushStatus(Collection<Long> ids, Integer pushStatus, Long updateTime) {
		if (ids.size() < 1) {
			return 0;
		}
		return storeCouponDao.update(ids, pushStatus, updateTime);
	}

	@Override
	public StoreCoupon addTypeCoupon(Long templateId, int type, Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId) {
		StoreCouponTemplate ct = storeCouponTemplateDao.search(templateId);
		StoreCoupon storeCoupon = new StoreCoupon();
		storeCoupon.setCouponTemplateId(ct.getId());
		storeCoupon.setTemplateName(ct.getName());
		storeCoupon.setType(type);
		storeCoupon.setMoney(ct.getMoney());
		storeCoupon.setRangeContent(ct.getRangeContent());
		storeCoupon.setRangeType(ct.getRangeType());
		storeCoupon.setValidityEndTime(ct.getValidityEndTime());
		storeCoupon.setValidityStartTime(ct.getValidityStartTime());
		storeCoupon.setIsLimit(ct.getIsLimit());
		storeCoupon.setCoexist(ct.getCoexist());
		storeCoupon.setStatus(ct.getStatus());
		storeCoupon.setLimitMoney(ct.getLimitMoney());
		storeCoupon.setGetWay(CouponGetWay.FETCH.getValue());
		
		storeCoupon.setPushDescription(pushDescription);
		storeCoupon.setPushImage(pushImage);
		storeCoupon.setPushStatus(pushStatus);
		storeCoupon.setPushTitle(pushTitle);
		storeCoupon.setPushUrl(pushUrl);
		storeCoupon.setGrantAdminId(adminId);
		storeCoupon.setPublishAdminId(adminId);
		storeCoupon.setRangeTypeIds(ct.getRangeTypeIds());
		storeCoupon.setRangeTypeNames(ct.getRangeTypeNames());
		
		return storeCouponDao.add(storeCoupon);
	}
	
	@Transactional(noRollbackFor = ParameterErrorException.class, rollbackFor = Exception.class)
	public void getCoupon(Long templateId, Integer count, long storeId, CouponGetWay couponGetWay, boolean needLimit) {
		long time = System.currentTimeMillis();
		StoreCouponTemplate couponTemplate = storeCouponTemplateDao.searchValidity(templateId, time);
		
		if(couponTemplate == null) {
			throw new ParameterErrorException("找不到对应的优惠券模板" + templateId + ", 不存在或已模板过期！");
		}
		
		double money = couponTemplate.getMoney();
		double publishMoney = money * count;
        
//        if (needLimit) {
//        	double restMoney = getRestMoney();
//        	if (restMoney < publishMoney)
//        		throw new ParameterErrorException("发放" + money + "元优惠券" + count + "张，合计价值" + publishMoney 
//        				+ "元，当前发行可用余额为" + restMoney + "，超出发行上限，请联系系统管理员。");
//		}
        
//        synchronized (this) {
//        	JSONObject jsonObject = globalSettingService.getJsonObjectNoCache(GlobalSettingName.COUPON);
//        	Double publishedMoney = jsonObject.getDouble("published_money");
//        	publishedMoney += publishMoney;
//        	jsonObject.put("published_money", publishedMoney);
//        	globalSettingService.update(GlobalSettingName.COUPON, jsonObject.toJSONString());
//		}
        
		long validityEndTime = couponTemplate.getValidityEndTime();
		if (validityEndTime != 0 && validityEndTime < System.currentTimeMillis())
			throw new ParameterErrorException("优惠券模板已过期!");
		StoreBusiness storeBusiness =storeBusinessService.searchBusinessById(storeId);
	//	User user = userMapper.getUser(userId);
		
		if (storeBusiness == null) {
			throw new ParameterErrorException("用户不存在!");
		}
		
		StoreCoupon coupon = new StoreCoupon();
		coupon.setCouponTemplateId(couponTemplate.getId());
		coupon.setTemplateName(couponTemplate.getName());
		coupon.setType(couponTemplate.getType());
		coupon.setMoney(couponTemplate.getMoney());
		coupon.setRangeType(couponTemplate.getRangeType());
		coupon.setRangeContent(couponTemplate.getRangeContent());
		coupon.setValidityStartTime(couponTemplate.getValidityStartTime());
		coupon.setValidityEndTime(couponTemplate.getValidityEndTime());
		coupon.setIsLimit(couponTemplate.getIsLimit());
		coupon.setCoexist(couponTemplate.getCoexist());
		coupon.setGrantAdminId(-1L);
		coupon.setPublishAdminId(-1L);
		coupon.setPushStatus(-1);
		coupon.setStoreId(storeBusiness.getId());
		coupon.setBusinessNumber(storeBusiness.getBusinessNumber());
		coupon.setCreateTime(time);
		coupon.setUpdateTime(time);
		coupon.setGetWay(couponGetWay.getValue());
		coupon.setLimitMoney(couponTemplate.getLimitMoney());
		
		List<StoreCoupon> coupons = new ArrayList<StoreCoupon>();
		for (int i = 0; i < count; i++) {
			coupons.add(coupon);
		}
		storeCouponDao.batchAddByCoupons(coupons);
		storeCouponTemplateDao.updateCount(templateId, count);
		storeCouponTemplateDao.updateGrant(templateId, count);
	}
    /**
     * 获取检验结果，是否存在重复，不存在的号码
     */
	@Override
	public Map<String, Object> checkPublishObjectNumbers(List<String> publishObjectNumbersList,int type) {
		if(publishObjectNumbersList != null&& publishObjectNumbersList.size() <= 0){
			throw new RuntimeException("未输入对应的号码！");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		//检测发放对象是否存在
		if(type == PublishObjectType.GIVEN_STOREBUSINESS.getValue()){
			//获取检验结果，是否存在重复，不存在的号码
			map = getResult(publishObjectNumbersList,BUSINESS_NUMBER_COLUMN,type);
			map.put("type", type);
		}
		if(type == PublishObjectType.GIVEN_PHONE_NUMBER.getValue()){
			map = getResult(publishObjectNumbersList,PHONE_NUMBER_COLUMN,type);
			map.put("type", type);
		}
		return map;
	}

	private Map<String, Object> getResult(List<String> publishObjectNumbersList, String TypeColumn, int type) {
		Map<String,Object> map = new HashMap<String,Object>();
		Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>();
		Set<String> rightNumberSet = new TreeSet<String>();
		List<String> rightNumberList = new ArrayList<String>();
		Set<String> wrongNumberSet = new TreeSet<String>();
		for(int i=0 ;; i++){
			String number = publishObjectNumbersList.get(i);
			wrapper.eq(TypeColumn, number);
			if(i ==publishObjectNumbersList.size()-1){
				break;
			}
			wrapper.or();
		}
		wrapper.andNew("Status = "+NORMAL_STATUS);
		List<StoreBusiness> list = storeMapper.selectList(wrapper);
		if(publishObjectNumbersList.size() != list.size()){
			 
			map.put("equal", false);
		}else{
			map.put("equal", true);
		}
		for(StoreBusiness storeBusiness :list){
			if(type == PublishObjectType.GIVEN_STOREBUSINESS.getValue()){
				String businessNumber = storeBusiness.getBusinessNumber()+"";
				rightNumberSet.add(businessNumber);
				rightNumberList.add(businessNumber);
			}
			
			if(type == PublishObjectType.GIVEN_PHONE_NUMBER.getValue()){
				String phoneNumber = storeBusiness.getPhoneNumber();
				rightNumberSet.add(phoneNumber);
				rightNumberList.add(phoneNumber);
			}
			
		}
		publishObjectNumbersList.removeAll(rightNumberList);

		map.put("rightNumberSet", rightNumberSet);
		map.put("wrongNumberSet", publishObjectNumbersList);
		return map;
	}

	/**
	 * 给新注册并且第一次通过审核的用户发送优惠券
	 */
	@Override
	public boolean batchStoreCouponToNewStoreAudit(int couponCount, long storeCouponTemplateId, StoreBusiness storeBusiness) {
		int i = 0;
		long time = System.currentTimeMillis();
		StoreCouponTemplate storeCouponTemplate = storeCouponTemplateDao.search(storeCouponTemplateId);
		if(storeCouponTemplate.getValidityEndTime()>time || storeCouponTemplate.getValidityEndTime()==0){
			storeCouponTemplateDao.update(storeCouponTemplateId, null, couponCount, couponCount, couponCount);
			List<StoreCoupon> storeCouponList = new ArrayList<StoreCoupon>();
			while(i<couponCount){
				StoreCoupon storeCoupon = new StoreCoupon();
				storeCoupon.setCouponTemplateId(storeCouponTemplate.getId());
				storeCoupon.setTemplateName(storeCouponTemplate.getName());
				storeCoupon.setType(storeCouponTemplate.getType());
				storeCoupon.setMoney(storeCouponTemplate.getMoney());
				storeCoupon.setRangeType(storeCouponTemplate.getRangeType());
				storeCoupon.setRangeTypeIds(storeCouponTemplate.getRangeTypeIds());
				storeCoupon.setRangeTypeNames(storeCouponTemplate.getRangeTypeNames());
				storeCoupon.setRangeContent(storeCouponTemplate.getRangeContent());
				storeCoupon.setValidityStartTime(storeCouponTemplate.getValidityStartTime());
				storeCoupon.setValidityEndTime(storeCouponTemplate.getValidityEndTime());
				storeCoupon.setIsLimit(storeCouponTemplate.getIsLimit());
				storeCoupon.setCoexist(storeCouponTemplate.getCoexist());
				storeCoupon.setGrantAdminId(-1L);
				storeCoupon.setPublishAdminId(-1L);
				storeCoupon.setPushStatus(-1);
				storeCoupon.setStoreId(storeBusiness.getId());
				storeCoupon.setBusinessNumber(storeBusiness.getBusinessNumber());
				storeCoupon.setCreateTime(time);
				storeCoupon.setUpdateTime(time);
				storeCoupon.setGetWay(0);
				storeCoupon.setLimitMoney(storeCouponTemplate.getLimitMoney());
				storeCouponList.add(storeCoupon);
				i++;
			}
			storeCouponDao.batchAddByCoupons(storeCouponList);
			
			//更新优惠券可用发行余额，已发行余额
			updateGlobalSettingStoreCoupon(storeCouponTemplate.getMoney()*couponCount);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 更新优惠券可用发行余额，已发行余额
	 * @param
	 */
	private void updateGlobalSettingStoreCoupon(double money){
		//更新优惠券可用发行余额，已发行余额
		GlobalSetting globalSetting = globalSettingService.getItem(GlobalSettingName.STORE_COUPON);
		Double publishedMoney = 0.0;
		Double totalMoney = 0.0;
		if (globalSetting != null) {
			String propertyValue = globalSetting.getPropertyValue();
			if(!StringUtils.isEmpty(propertyValue)){
				JSONObject jsonObject = JSONObject.parseObject(propertyValue);
				if (jsonObject != null) {
					publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());
					totalMoney = Double.parseDouble(jsonObject.get("total_money").toString());
				}
			}
		}
		
		Map<String,String> property = new HashMap<String,String>();
		property.put("published_money", String.format("%.2f", money+publishedMoney));
		property.put("total_money", String.format("%.2f", money+totalMoney));
		String json = JSONObject.toJSON(property).toString();
		globalSetting.setPropertyValue(json);
		globalSetting.setUpdateTime(System.currentTimeMillis());
		
		globalSettingService.update(globalSetting);
	}
	
}
