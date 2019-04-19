package com.jiuy.core.business.facade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.StoreCouponTemplateDao;
import com.jiuy.core.dao.StoreCouponUseLogDao;
import com.jiuy.core.dao.mapper.UserDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.coupon.StoreCouponTemplateVO;
import com.jiuy.core.meta.coupon.StoreCouponVO;
import com.jiuy.core.meta.notification.Notification;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.coupon.StoreCouponService;
import com.jiuy.core.service.coupon.StoreCouponTemplateService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuy.core.service.notifacation.NotifacationService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.RangeType;
import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.dao.mapper.supplier.StoreCouponNewMapper;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreCouponNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.util.GetuiUtil;
import com.jiuyuan.util.IdsToStringUtil;

@Service
public class StoreCouponFacade {
	 private static final Logger log = LoggerFactory.getLogger(HttpClientService.class);
	@Autowired
	private StoreCouponService storeCouponService;
	
	@Autowired
	private StoreCouponTemplateService storeCouponTemplateService;

	@Autowired
	private GlobalSettingService globalSettingService;
	
    @Autowired
    private UserManageService userManageService;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private StoreBusinessService storeBusinessService;
    
    @Autowired
    private BrandLogoServiceImpl brandLogoService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private StoreCouponUseLogDao storeCouponUseLogDao;
    
    @Autowired
    private StoreCouponTemplateDao storeCouponTemplateDao;
    
    @Autowired
    private NotifacationService notifacationService;
    
    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;
    
    @Autowired
    private StoreCouponNewMapper storeCouponNewMapper;
    
    
	@Transactional(rollbackFor = Exception.class)
	public int updateTemplate(Long id, Double money, Integer publishCount, Long adminId) {
		// 更新发行的金额
		updatePublishMoney(money, new Long(publishCount.intValue() + ""));
		// 模版是否过期
		StoreCouponTemplate storeCouponTemplate = storeCouponTemplateService.search(id);
		checkTemplate(storeCouponTemplate);

		// 批量新增优惠券
		storeCouponService.batchAdd(publishCount, storeCouponTemplate, adminId);
		// 更新优惠券模版
		return storeCouponTemplateService.update(id, null, publishCount, null, publishCount);
	}
	
	/**
	 * 验证模板的有效性
	 * @param
	 */
	private void checkTemplate(StoreCouponTemplate storeCouponTemplate) {
		if(storeCouponTemplate == null) {
			throw new ParameterErrorException("找到对应id的优惠券模板!");
		}
		long validityEndTime = storeCouponTemplate.getValidityEndTime();
		if (validityEndTime != 0 && validityEndTime < System.currentTimeMillis())
			throw new ParameterErrorException("优惠券模板已过期!");
	}

	public void updatePublishMoney(Double money, Long publishCount) {
		// 计算总金额
		double publishMoney = money * publishCount;
		// 计算余额
        double restMoney = getRestMoney();

        // 余额不足
        if (restMoney < publishMoney)
			throw new ParameterErrorException("发放" + money + "元优惠券" + publishCount + "张，合计价值" + publishMoney 
					+ "元，当前发行可用余额为" + restMoney + "，超出发行上限，请联系系统管理员。");

        // 更新发布金额
        synchronized (this) {
        	JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.STORE_COUPON);
        	
        	Double publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());
        	publishedMoney += publishMoney;
        	jsonObject.put("published_money", publishedMoney);
        	globalSettingService.update(GlobalSettingName.STORE_COUPON.getStringValue(), jsonObject.toJSONString());
		}
	}

	@Transactional(rollbackFor = Exception.class)
    public Map<String, Object> grant(Long id, Long templateId, Double money, List<String> publishObjectNumbersList, int type, int count,
                      Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		StoreCouponTemplate storeCouponTemplate = storeCouponTemplateService.search(templateId);
        //核对模板有效性
		checkTemplate(storeCouponTemplate);
//		log.error("type:" + type);
		
        List<Long> storeNumberList = new ArrayList<>();
        //存放商家号
        List<Long> list = new ArrayList<>();
        //单个优惠券的ID，辨认是在优惠卷模板下发放还是优惠券中发放
        if (id == null ) {
        	//在优惠券模板下
        	//如果指定了用户的商家号
            if (type == 0) {
//            	log.error("type0:" + type);
                List<String> list2 = publishObjectNumbersList;
                for (String storeNum : list2) {
                    list.add(Long.parseLong(storeNum));
                }
            //如果指定了全部用户商家号
            } else if (type == 1 ||type == 2) {
//            	log.error("type1||2:" + type);
            	long endTime =System.currentTimeMillis();
            	int userCount = storeBusinessService.searchStoreCount(0);
            	int excludeCount = 0;
            	//每个用户都发放count数量的优惠券
            	for (int i = 0; i < count; i++) {
            		//添加发放量
            		updatePublishMoney(money, (long)userCount - excludeCount);
            		//添加优惠券
                	StoreCoupon storeCoupon = storeCouponService.addTypeCoupon(templateId, 1, pushStatus, pushTitle, pushDescription, pushUrl, pushImage, adminId);
                	//更新优惠券模板
                	storeCouponTemplateService.update(templateId, null, userCount - excludeCount, userCount - excludeCount, userCount - excludeCount);
                	//发送通知和推送
                	if(type==1){
                		List<String> userCIDList = new ArrayList<String>();
                		//发送通知和推送
                		String abstracts = "面额￥"+storeCouponTemplate.getMoney();
                		double limitMoney = storeCouponTemplate.getLimitMoney();
                		if(limitMoney>0){
                			abstracts = abstracts + ",满"+limitMoney+"可用";
                		}
                		
                		long validityEndTime = storeCouponTemplate.getValidityEndTime();
                		if(validityEndTime==0){
                			abstracts = abstracts + "无有效期限制";
                		}else{
                			long validityStartTime = storeCouponTemplate.getValidityStartTime();
                			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                			String start = simpleDateFormat.format(new Date(validityStartTime));
                			String end = simpleDateFormat.format(new Date(validityEndTime));
                			abstracts = abstracts + ",有效期:"+start+" - "+end;
                		}
                        sendNotificationAndGetui(userCIDList, "收到"+storeCouponTemplate.getName()+"优惠券", abstracts, "", "", "5", System.currentTimeMillis()+""
                        		,new ArrayList<Long>());
                	}
				}
            	map.put("success", userCount - excludeCount);
				map.put("fail", 0);
				map.put("fail_content", "");
				return map;
            }
            else if(type == 3){
            	//注册手机号,已经在前面核对过号码的有效性
            	List<String> phones = publishObjectNumbersList;
            	List<StoreBusiness> storeBusinessList = storeBusinessNewService.getStoreBusinessByPhones(phones);
            	for(StoreBusiness storeBusiness :storeBusinessList){
            		list.add(storeBusiness.getBusinessNumber());
            	}
            	map.put("success", phones.size());
				map.put("fail", 0);
				map.put("fail_content", "");
            	
            }

            for (int i = 0; i < count; i++) {
            	storeNumberList.addAll(list);
            }
            
            if (storeNumberList.size() < 1) {
				return map;
			}
            //获取发放数量
            Integer publishCount = storeNumberList.size();
//            updateTemplate(templateId, money, publishCount, adminId);
            //批量全发放
            storeCouponService.batchGrant(storeNumberList.size(), storeCouponTemplate, storeNumberList, pushStatus, pushTitle,
            		pushDescription, pushUrl, pushImage, adminId);
            //发放数目
            int grantCount = publishCount;
            // 更新优惠券模版: grantCount = historyGrantCount + newGrantCount
            storeCouponTemplateDao.update(templateId, null, null, grantCount, null);
            
            //发送通知和推送
			//获取发送对象信息 userCID,storeId
            List<String> userCIDList = storeBusinessService.getStoreBusinessUserCIDByStoreNumberList(storeNumberList);
            List<Long> storeIdList = storeBusinessService.getStoreIdListByStoreNumberList(storeNumberList);
    		userCIDList.removeAll(Collections.singleton(null));
            //组装推送消息
    		String abstracts = "面额￥"+storeCouponTemplate.getMoney();
    		double limitMoney = storeCouponTemplate.getLimitMoney();
    		if(limitMoney>0){
    			abstracts = abstracts + ",满"+limitMoney+"可用";
    		}
    		long validityEndTime = storeCouponTemplate.getValidityEndTime();
    		if(validityEndTime==0){
    			abstracts = abstracts + "无有效期限制";
    		}else{
    			long validityStartTime = storeCouponTemplate.getValidityStartTime();
    			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    			String start = simpleDateFormat.format(new Date(validityStartTime));
    			String end = simpleDateFormat.format(new Date(validityEndTime));
    			abstracts = abstracts + ",有效期:"+start+" - "+end;
    		}
    		// 推送
            sendNotificationAndGetui(userCIDList, "收到"+storeCouponTemplate.getName()+"优惠券", abstracts, "", "", "5", System.currentTimeMillis()+"",storeIdList);
            
        } else {
        	if (publishObjectNumbersList == null||publishObjectNumbersList.size() == 0) {
				throw new ParameterErrorException("用户输入不能为空");
			}
        	if(publishObjectNumbersList.size() != 1){
        		throw new RuntimeException("只能输入一个号码！");
        	}
        	
        	StoreCouponNew storeCouponNew = storeCouponNewMapper.selectById(id);
        	//搜索该优惠券是否已经发放过
        	if(storeCouponNew ==null ){
        		throw new RuntimeException("该优惠券查找不到相关详情");
        	}
        	if(storeCouponNew.getBusinessNumber() != null){
        		throw new RuntimeException("该订单已经发放过，请勿重复发放");
        	}
        	
        	StoreBusiness  storeBusiness = null;
        	Long storeNumber = null;
        	String phoneNumber = "";
        	if(type == 0){
        		storeNumber = Long.parseLong(publishObjectNumbersList.get(0));
        		storeBusiness = storeBusinessNewService.getStoreBusinessByBusinessNumber(storeNumber);
        		if (storeBusiness == null ) {
        			throw new ParameterErrorException(storeNumber + "该门店号不存在！");
        		}
        	}
        	if(type == 3){
        		phoneNumber = publishObjectNumbersList.get(0);
        		List<StoreBusiness> storeBusinessList = storeBusinessNewService.getStoreBusinessByPhones(publishObjectNumbersList);
        		if(storeBusinessList == null || storeBusinessList.size() == 0){
        			throw new ParameterErrorException(phoneNumber+"该手机号对应的门店号不存在！");
        		}
        		storeBusiness =storeBusinessList.get(0);
        		storeNumber = storeBusiness.getBusinessNumber();
        	}
            storeCouponService.update(id, storeBusiness.getId(), storeNumber, 0, pushStatus, pushTitle, pushDescription, pushUrl,
                pushImage, adminId);
            //storeCouponService.update(id, userId, publishObjectNumbersList, 0, pushStatus, pushTitle, pushDescription, pushUrl, pushImage, adminId);
            
            storeCouponTemplateDao.update(templateId, null, null, 1,null);
        }
        
        return map;
	}
	
	//发送系统通知和推送
	private void sendNotificationAndGetui(List<String> cids, String title,String abstracts,String linkUrl,String image,String type ,String pushTime
			,List<Long> storeIdList){

/*		System.out.println("=========================================== 发送系统通知和推送 ===========================================");
		System.out.println("cids: " + cids);
		System.out.println("title: " + title);
		System.out.println("abstracts: " + abstracts);
		System.out.println("linkUrl: " + linkUrl);
		System.out.println("image: " + image);
		System.out.println("type: " + type);
		System.out.println("pushTime: " + pushTime);
		System.out.println("storeIdList: " + storeIdList);*/



		try {
			if(cids.size()>0){
				boolean ret = GetuiUtil.pushGeTui(cids,title, abstracts, linkUrl, image, type , pushTime);
			}else{
				boolean ret = GetuiUtil.pushGeTui(title, abstracts, linkUrl, image, type , pushTime);
			}
//			if(ret){
				Notification notification = new Notification();
				notification.setTitle(title);
				notification.setAbstracts(abstracts);
				notification.setPushStatus(1);
				notification.setImage(image);
				notification.setType(type);
				notification.setStatus(0);
				notification.setLinkUrl(linkUrl);
				String storeIdArrays = "";
				if(storeIdList.size()>0){
					for (Long storeId : storeIdList) {
						if(storeId!=null){
							storeIdArrays = storeIdArrays + "," + storeId;
						}
					}
				}
				notification.setStoreIdArrays(storeIdArrays);
				notifacationService.addNotification(notification);
//			}
		} catch (Exception e) {
			throw new RuntimeException("发送优惠券通知和推送时发生异常:"+e.getMessage());
		}
	}
	
    private double getRestMoney() {
        JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.STORE_COUPON);
        
        double totalMoney = Double.parseDouble(jsonObject.get("total_money").toString());
        double publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());

        return totalMoney - publishedMoney;
    }

	public Map<String,Object> loadVOInfo(Long id) {
		StoreCouponTemplate storeCouponTemplate = storeCouponTemplateService.search(id);
		
		if(storeCouponTemplate == null) {
			throw new ParameterErrorException("优惠券模板id不存在!");
		}
		
		StoreCouponTemplateVO storeCouponTemplateVO = new StoreCouponTemplateVO();
		BeanUtils.copyProperties(storeCouponTemplate, storeCouponTemplateVO);
		//获取使用的数量
		int availableCount = storeCouponService.searchAvailableCount(id);
		int expiredCount = storeCouponService.searchExpiredCount(id);
		int deletedCount = storeCouponService.searchCount(-1, id);
		int usedCount = storeCouponService.searchCount(1, id);
		storeCouponTemplateVO.setAvailableCount(availableCount);
		storeCouponTemplateVO.setExpiredCount(expiredCount);
		storeCouponTemplateVO.setDeletedCount(deletedCount);
		storeCouponTemplateVO.setUsedCount(usedCount);
		
		Map<String,Object> map = new HashMap<String,Object>();
        map.put("storeCouponTemplateVO", storeCouponTemplateVO);
        StringBuffer stringBuffer = new StringBuffer("");
        if(checkEmpty(storeCouponTemplateVO.getRangeTypeIds()) &&  checkEmpty(storeCouponTemplateVO.getRangeTypeNames())){
        	List<String> rangeTypeIds = IdsToStringUtil.getIdsToListNoKomma(storeCouponTemplateVO.getRangeTypeIds());
        	List<String> rangeTypeNames = IdsToStringUtil.getIdsToListNoKomma(storeCouponTemplateVO.getRangeTypeNames());
        	for(int i=0;i<rangeTypeIds.size();i++){
        		String rangeTypeId = rangeTypeIds.get(i);
        		String rangeTypeName = rangeTypeNames.get(i);
        		stringBuffer.append(rangeTypeName).append("(ID:").append(rangeTypeId).append("),");
        	}
        }
        map.put("displayOfRangeType", stringBuffer);
		
//		//转换范围类型
//		String json = storeCouponTemplate.getRangeContent();
//		if(StringUtils.equals(null, json) || StringUtils.equals("", json)) {
//			return storeCouponTemplateVO;
//		}
//		
//		JSONObject jsonObject = (JSONObject) JSON.parse(json);
//		if(storeCouponTemplate.getRangeType() == RangeType.CATEGORY.getValue()) {
//			JSONArray jsonArray = (JSONArray) jsonObject.get("categoryIds");
//            if (jsonArray != null) {
//                List<Long> categoryIds = new ArrayList<Long>();
//                Object[] list = jsonArray.toArray();
//                for (Object object : list) {
//                	Long categoryId = Long.parseLong(object.toString());
//                    categoryIds.add(categoryId);
//                }
//
//                List<Category> categories = categoryService.search(categoryIds);
//                List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
//                for (Category category : categories) {
//                	JSONObject jsObject = new JSONObject();
//                	long parentId = category.getParentId();
//                	if (parentId > 0) {
//                		Category topCategory = categoryService.getCategoryById(parentId);
//                		if (topCategory == null) {
//							throw new ParameterErrorException("该分类的顶级分类未找到！");
//						}
//                		JSONObject jsonObject2 = new JSONObject();
//                		jsonObject2.put("id", category.getId());
//                		jsonObject2.put("name", category.getCategoryName());
//                		
//                		jsObject.put("id", topCategory.getId());
//                		jsObject.put("name", topCategory.getCategoryName());
//                		jsObject.put("is_top", "NO");
//                		jsObject.put("child", jsonObject2);
//                	} else {
//						jsObject.put("id", category.getId());
//						jsObject.put("name", category.getCategoryName());
//						jsObject.put("is_top", "YES");
//					}
//                    jsonObjects.add(jsObject);
//                }
//                storeCouponTemplateVO.setJsonObjects(jsonObjects);
//            }
//		} else if (storeCouponTemplate.getRangeType() == RangeType.BRAND.getValue()) {
//			JSONArray jsonArray = (JSONArray) jsonObject.get("brandIds");
//            if (jsonArray != null) {
//                List<Long> brandIds = new ArrayList<Long>();
//                Object[] list = jsonArray.toArray();
//                for (Object object : list) {
//                	Long brandId = Long.parseLong(object.toString());
//                    brandIds.add(brandId);
//                }
//
//                List<BrandLogo> brands = brandLogoService.search(brandIds);
//                List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
//                for (BrandLogo brand : brands) {
//                    JSONObject jsObject = new JSONObject();
//                    jsObject.put("id", brand.getBrandId());
//                    jsObject.put("name", brand.getBrandName());
//                    jsonObjects.add(jsObject);
//                }
//                storeCouponTemplateVO.setJsonObjects(jsonObjects);
//            }
//		}
		
		return map;
	}
	
	private boolean checkEmpty(String string) {
		if(string == null ||string.equals("")){
			return false;
		}
		return true;
	}

	public List<StoreCouponTemplateVO> search(PageQuery pageQuery, StoreCouponTemplate storeCouponTemplate, Integer publishCountMin,
			Integer publishCountMax, Double moneyMin, Double moneyMax,Double limitMoneyMin,Double limitMoneyMax, List<String> ids) {
		List<StoreCouponTemplate> storeCouponTemplates = storeCouponTemplateService.search(pageQuery, storeCouponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax,limitMoneyMin,limitMoneyMax,ids);
		
		Set<Long> templateIds = new HashSet<Long>();
		for (StoreCouponTemplate storeCouponTemplate2 : storeCouponTemplates) {
			templateIds.add(storeCouponTemplate2.getId());
		}
		
		//Map<Long, Integer> availableCountOfTemplateMap = storeCouponService.availableOfTemplateId(templateIds);
		List<StoreCouponTemplateVO> storeCouponTemplateVOs = new ArrayList<StoreCouponTemplateVO>();
		for (StoreCouponTemplate ct : storeCouponTemplates) {
			StoreCouponTemplateVO storeCouponTemplateVO = new StoreCouponTemplateVO();
			BeanUtils.copyProperties(ct, storeCouponTemplateVO);
			storeCouponTemplateVO.setAvailableCount(ct.getAvailableCount());
//			storeCouponTemplateVO.setAvailableCount(availableCountOfTemplateMap.get(ct.getId()) == null ? 0 : availableCountOfTemplateMap.get(ct.getId()));

			String rangeDescription = assembleRangeDescription(ct.getRangeType(), ct.getRangeContent());

			storeCouponTemplateVO.setRangeDescription(rangeDescription);
			
			storeCouponTemplateVOs.add(storeCouponTemplateVO);
		}
		
		return storeCouponTemplateVOs;
	}

	private String assembleRangeDescription(int rangeTypeValue, String rangContent) {
		RangeType rangeType = RangeType.getByValue(rangeTypeValue);
		
		String rangeDescription = null;
		switch (rangeType) {
		case GENERAL:
			rangeDescription = "全部";
			break;
		case CATEGORY:
			rangeDescription = getCategoryDescription(rangContent);
			break;
		case LIMIT_ORDER:
			rangeDescription = getOrderDescription(rangContent);
			break;
		case LIMIT_PRODUCT:
			rangeDescription = getProductDescription(rangContent);
			break;
		case BRAND:
			rangeDescription = getBrandDescription(rangContent);
			break;
		default:
			break;
		}
		
		return rangeDescription;
	}

	private String getBrandDescription(String rangContent) {
		StringBuilder builder = new StringBuilder("");
		List<Object> list = splitContent("brandIds", rangContent);
		for (Object object : list) {
			long brandId = Long.parseLong(object.toString());
			BrandLogo brandLogo = brandLogoService.getByBrandId(brandId);
			builder.append(brandLogo.getBrandName() + ",");
		}
		String rangeDescription = builder.toString();
		if (StringUtils.equals("", rangeDescription)) {
			return "";
		}
		return rangeDescription.substring(0, rangeDescription.length()-1);
	}

	private String getProductDescription(String rangContent) {
		StringBuilder builder = new StringBuilder("");
		List<Object> list = splitContent("productIds", rangContent);
		for (Object object : list) {
			long productId = Long.parseLong(object.toString());
			Product product = productService.getProductById(productId);
			builder.append(product.getName() + ",");
		}
		String rangeDescription = builder.toString();
		if (StringUtils.equals("", rangeDescription)) {
			return "";
		}
		return rangeDescription.substring(0, rangeDescription.length()-1);
	}

	private String getOrderDescription(String rangContent) {
		StringBuilder builder = new StringBuilder("");
		List<Object> list = splitContent("limitOrders", rangContent);
		for (Object object : list) {
			builder.append(object.toString());
		}
		return builder.toString();
	}

	private String getCategoryDescription(String rangContent) {
		StringBuilder builder = new StringBuilder("");
		List<Object> list = splitContent("categoryIds", rangContent);
		for (Object object : list) {
			long categoryId = Long.parseLong(object.toString());
			Category category = categoryService.getCategoryById(categoryId);
			builder.append(category.getCategoryName() + ",");
		}
		String rangeDescription = builder.toString();
		if (StringUtils.equals("", rangeDescription)) {
			return "";
		}
		return rangeDescription.substring(0, rangeDescription.length()-1);
	}

	private List<Object> splitContent(String key, String rangContent) {
		JSONObject jsonObject = JSONObject.parseObject(rangContent);
		if (jsonObject == null) {
			return new ArrayList<>();
		}
		JSONArray jsonArray = (JSONArray) jsonObject.get(key);
		
		if (jsonArray == null) {
			return new ArrayList<>();
		}
		
		List<Object> list = new ArrayList<>();
		for (Object object : jsonArray) {
			list.add(object);
		}
		return list;
	}

	public List<StoreCouponVO> search(PageQuery pageQuery, StoreCoupon storeCoupon, Double moneyMin, Double moneyMax, Double limitMoneyMin , Double limitMoneyMax) {
		List<StoreCouponVO> storeCouponVOs = new ArrayList<StoreCouponVO>();
		List<StoreCoupon> storeCoupons = storeCouponService.search(pageQuery, storeCoupon, moneyMin, moneyMax, limitMoneyMin , limitMoneyMax);
		for (StoreCoupon cp : storeCoupons) {
			StoreCouponVO storeCouponVO = new StoreCouponVO();
			BeanUtils.copyProperties(cp, storeCouponVO);
		
			String rangeDescription = assembleRangeDescription(cp.getRangeType(), cp.getRangeContent());
			storeCouponVO.setRangeDescription(rangeDescription);
			
			storeCouponVOs.add(storeCouponVO);
		}
		
		return storeCouponVOs;
	}

	public List<StoreCouponTemplateVO> search(PageQuery pageQuery, StoreCouponTemplate storeCouponTemplate,
			Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax) {
		// TODO Auto-generated method stub
		return null;
	}

}
