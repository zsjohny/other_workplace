package com.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.StoreAudit;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.StoreAuditMapper;
import com.store.enumerate.StoreAuditStatusEnum;

@Service
public class StoreAuditServiceShop {
    private static final Logger logger = LoggerFactory.getLogger(StoreAuditServiceShop.class);

    @Autowired
    private StoreAuditMapper storeAuditMapper;
    
    @Autowired
    private StoreUserService storeUserService;
    
    @Autowired
    private IUserNewService userNewService;

    public JsonResponse getAuditList(StoreAuditStatusEnum statusEnum) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
//    	Boolean  sendFlag = true;
    	PageQuery pageQuery = new PageQuery();
//    	List<StoreAudit> storeAuditList = storeAuditMapper.getAuditList(pageQuery, statusEnum.getIntValue());
    	List<StoreBusiness> storeBusinesses = storeUserService.getStoreBusinessAuditList(pageQuery, statusEnum.getIntValue());
    	data.put("storeAuditList", storeBusinesses);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @Transactional(rollbackFor=Exception.class)
	public JsonResponse auditStoreBusiness(long storeId, int status, UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
//    	Boolean  sendFlag = true;
    	PageQuery pageQuery = new PageQuery();
//    	List<StoreAudit> storeAuditList = storeAuditMapper.getAuditList(pageQuery, statusEnum.getIntValue());
    	int num = 0;
    	if(status == StoreAuditStatusEnum.fail.getIntValue() || status == StoreAuditStatusEnum.pass.getIntValue()){
    		num = updateStoreAudit(storeId, status, userDetail.getId());
    	}
    	if(num > 0){
    		if(status == StoreAuditStatusEnum.pass.getIntValue()){
    			long sysTime = System.currentTimeMillis();
    			storeUserService.updateStoreActiveTime(storeId, sysTime);
    			//门店审核过后绑定供应商
    			String phoneNumber = userDetail.getUserDetail().getPhoneNumber();
    			//查找是否有该供应商
    			List<UserNew> list = userNewService.getSupplierUserInfoByPhoneNumber(phoneNumber);
    			if(list==null||list.size()==0){
    				logger.info("当前门店并没有对应供应商！不用绑定");
    			}
    			if(list.size()==1){
    				//绑定供应商
    				long supplierId = list.get(0).getId();
    				int i = storeUserService.updateSupplierId(supplierId,storeId);
    				if(i ==-1){
    					logger.error("供应商与门店绑定失败！请排查！");
    					throw new RuntimeException("供应商与门店绑定失败！请排查！");
    				}
    				logger.info("供应商与门店绑定成功！storeId:"+storeId+",supplierId:"+supplierId);
    			}
    			if(list.size()>1){
    				logger.error("当前门店的手机号码与多个供应商对应，请排查问题！");
    				throw new RuntimeException("当前门店的手机号码与多个供应商对应，请排查问题！");
    			}
    		}
    	}else{
    		logger.error("门店审核失败！");
    		throw new RuntimeException("门店审核失败！");
    	}

    	return jsonResponse.setSuccessful();
    }
    
	
	public int updateStoreAudit(long storeId,int status, long auditId) {
		return storeAuditMapper.updateStoreAudit(storeId, status, auditId);
	}
	
	public int getAuditCount(long storeId,int status) {
		return storeAuditMapper.getAuditCount(storeId, status);
	}
    
//    public JsonResponse getAuditList(StoreAuditStatusEnum statusEnum) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	Map<String, Object> data = new HashMap<String, Object>();
////    	Boolean  sendFlag = true;
//    	
//    	Wrapper<StoreAudit> wrapper = buildListWrapper(statusEnum, null);
//    	List<StoreAudit> storeAuditList = storeAuditMapper.selectList(wrapper);
//    	data.put("storeAuditList", storeAuditList);
//    	return jsonResponse.setSuccessful().setData(data);
//    }
    
	private Wrapper<StoreAudit> buildListWrapper(StoreAuditStatusEnum statusEnum, String searchWord) {
		Wrapper<StoreAudit> wrapper = new EntityWrapper<StoreAudit>();
	

//			
		wrapper.eq("status",statusEnum.getIntValue());
		
		
		//3、组装搜索词条件
		if(StringUtils.isNotEmpty(searchWord)){
			wrapper.like("user_nickname", searchWord);
		}
		
		wrapper.orderBy("CreateTime",false);
		
		return wrapper;
	}

	/**
	 * 判断门店资料是否完整
	 * @param storeBusiness
	 * @return
	 */
	public boolean chectStoreBusiness(StoreBusiness storeBusiness) {
		if(StringUtils.isEmpty(storeBusiness.getLegalPerson())){
			return true;
		}else if(StringUtils.isEmpty(storeBusiness.getPhoneNumber())){
			return true;
		}else if(StringUtils.isEmpty(storeBusiness.getBusinessName())){
			return true;
		}else if(StringUtils.isEmpty(storeBusiness.getProvince())){
			return true;
		}else if(StringUtils.isEmpty(storeBusiness.getCity())){
			return true;
		}else if(StringUtils.isEmpty(storeBusiness.getCounty())){
			return true;
		}else if(StringUtils.isEmpty(storeBusiness.getBusinessAddress())){
			return true;
		}else if(StringUtils.isEmpty(storeBusiness.getQualificationProofImages())){
			return true;
		}else{
			return false;
		}
	}
	
	public List<StoreAudit> getAuditByStoreId(long storeId) {
		return storeAuditMapper.getAuditByStoreId(storeId);
	}

}