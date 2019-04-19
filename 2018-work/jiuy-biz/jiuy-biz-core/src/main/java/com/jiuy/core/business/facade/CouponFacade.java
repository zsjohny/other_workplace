package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.CouponTemplateDao;
import com.jiuy.core.dao.CouponUseLogDao;
import com.jiuy.core.dao.mapper.UserDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.coupon.Coupon;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuy.core.meta.coupon.CouponTemplateVO;
import com.jiuy.core.meta.coupon.CouponUseLog;
import com.jiuy.core.meta.coupon.CouponVO;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.coupon.CouponService;
import com.jiuy.core.service.coupon.CouponTemplateService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.CouponUseStatus;
import com.jiuyuan.constant.coupon.RangeType;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.HttpClientService;

@Service
public class CouponFacade {
	 private static final Logger log = LoggerFactory.getLogger(HttpClientService.class);
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private CouponTemplateService couponTemplateService;

	@Autowired
	private GlobalSettingService globalSettingService;
	
    @Autowired
    private UserManageService userManageService;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private BrandLogoServiceImpl brandLogoService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CouponUseLogDao couponUseLogDao;
    
    @Autowired
    private CouponTemplateDao couponTemplateDao;
    
    
	@Transactional(rollbackFor = Exception.class)
	public int updateTemplate(Long id, Double money, Integer publishCount, Long adminId) {
		updatePublishMoney(money, new Long(publishCount.intValue() + ""));
		
		CouponTemplate couponTemplate = couponTemplateService.search(id);
		checkTemplate(couponTemplate);
		
        couponService.batchAdd(publishCount, couponTemplate, adminId);
		return couponTemplateService.update(id, null, publishCount);
	}
	
	/**
	 * 验证模板的有效性
	 * @param couponTemplate
	 */
	private void checkTemplate(CouponTemplate couponTemplate) {
		if(couponTemplate == null) {
			throw new ParameterErrorException("找到对应id的代金券模板!");
		}
		long validityEndTime = couponTemplate.getValidityEndTime();
		if (validityEndTime != 0 && validityEndTime < System.currentTimeMillis())
			throw new ParameterErrorException("金券模板已过期!");
	}

	public void updatePublishMoney(Double money, Long publishCount) {
		double publishMoney = money * publishCount;
        double restMoney = getRestMoney();
        
        if (restMoney < publishMoney)
			throw new ParameterErrorException("发放" + money + "元代金券" + publishCount + "张，合计价值" + publishMoney 
					+ "元，当前发行可用余额为" + restMoney + "，超出发行上限，请联系系统管理员。");
        
        synchronized (this) {
        	JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.COUPON);
        	
        	Double publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());
        	publishedMoney += publishMoney;
        	jsonObject.put("published_money", publishedMoney);
        	globalSettingService.update(GlobalSettingName.COUPON.getStringValue(), jsonObject.toJSONString());
		}
	}

	@Transactional(rollbackFor = Exception.class)
    public Map<String, Object> grant(Long id, Long templateId, Double money, String yJJNumbers, int type, int count,
                      Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
        CouponTemplate couponTemplate = couponTemplateService.search(templateId);
        
		checkTemplate(couponTemplate);
//		log.error("type:" + type);
        List<Long> yjjNumberList = new ArrayList<>();
        List<Long> list = new ArrayList<>();
        if (id == null ) {
            if (type == 0) {
//            	log.error("type0:" + type);
                List<String> list2 = Arrays.asList(yJJNumbers.split(","));
                for (String yjjNum : list2) {
                    list.add(Long.parseLong(yjjNum));
                }

            } else if (type == 1 || type == 2) {
//            	log.error("type1||2:" + type);
            	long userCount = userManageService.getUserCount(null, null);
            	int excludeCount = 0;
            	List<Long> excludeYJJNumbers = new ArrayList<>();
            	if (type == 2 && yJJNumbers != null && !StringUtils.equals("", yJJNumbers)) {
            		String[] array = yJJNumbers.split(",");
            		for (String string : array) {
            			excludeYJJNumbers.add(Long.parseLong(string));
					}
            		excludeCount = array.length;
				}
            	
//            	updatePublishMoney(money, userCount - excludeCount);
            	Coupon coupon = couponService.addTypeCoupon(templateId, 1, pushStatus, pushTitle, pushDescription, pushUrl, pushImage, adminId);
            	if (type == 2) {
            		List<User> users = userManageService.excludeSearch(excludeYJJNumbers);

            		List<CouponUseLog> couponUseLogs = new ArrayList<>();
            		for (User user : users) {
            			CouponUseLog couponUseLog = new CouponUseLog();
            			couponUseLog.setOrderNo(-1L);
            			couponUseLog.setStatus(CouponUseStatus.USED);
            			couponUseLog.setActualDiscount(0.0);
            			couponUseLog.setCouponId(coupon.getId());
            			couponUseLog.setUserId(user.getUserId());

            			couponUseLogs.add(couponUseLog);
					}
            		couponUseLogDao.add(couponUseLogs);
				} 
            	map.put("success", userCount - excludeCount);
				map.put("fail", 0);
				map.put("fail_content", "");
				return map;
            	
            } else if (type == 3) {
            	List<String> phones = Arrays.asList(yJJNumbers.split(","));
            	List<User> users = userManageService.searchByPhone(phones);
            	List<String> userPhones = new ArrayList<String>();
            	for (User user : users) {
					list.add(user.getyJJNumber());
					userPhones.add(user.getUserRelatedName());
				}
            	if (users.size() == phones.size()) {
					map.put("success", users.size());
					map.put("fail", 0);
					map.put("fail_content", "");
				} else {
					StringBuilder builder = new StringBuilder("");
					for (String phone : phones) {
						if (!userPhones.contains(phone)) {
							builder.append(phone + ",");
						}
					}
					String failContent = builder.toString().substring(0, builder.length()-1);
					int failCount = failContent.split(",").length;
					map.put("success", phones.size() - failCount);
					map.put("fail", failCount);
					map.put("fail_content", failContent);
				}
            }

            for (int i = 0; i < count; i++) {
                yjjNumberList.addAll(list);
            }
            
            if (yjjNumberList.size() < 1) {
				return map;
			}
            
            Integer publishCount = yjjNumberList.size();
//            updateTemplate(templateId, money, publishCount, adminId);
            
            couponService.batchGrant(yjjNumberList.size(), couponTemplate, yjjNumberList, pushStatus, pushTitle,
            		pushDescription, pushUrl, pushImage, adminId);
            
            int grantCount = publishCount;
            couponTemplateDao.update(templateId, null, null, grantCount);
            
        } else {
        	if (yJJNumbers == null || StringUtils.equals("", yJJNumbers.trim())) {
				throw new ParameterErrorException("用户输入不能为空");
			}
            Long yJJNumber = Long.parseLong(yJJNumbers);
            User user = userDao.searchOne(yJJNumber);
            if (user == null) {
				throw new ParameterErrorException(yJJNumber + "该俞姐姐号不存在！");
			}
            couponService.update(id, user.getUserId(), yJJNumber, 0, pushStatus, pushTitle, pushDescription, pushUrl,
                pushImage, adminId);
            
            couponTemplateDao.update(templateId, null, null, 1);
        }
        
        return map;
	}
	
    private double getRestMoney() {
        JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.COUPON);
        
        double totalMoney = Double.parseDouble(jsonObject.get("total_money").toString());
        double publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());

        return totalMoney - publishedMoney;
    }

	public CouponTemplateVO loadVOInfo(Long id) {
		CouponTemplate couponTemplate = couponTemplateService.search(id);
		
		if(couponTemplate == null) {
			throw new ParameterErrorException("代金券模板id不存在!");
		}
		
		CouponTemplateVO couponTemplateVO = new CouponTemplateVO();
		BeanUtils.copyProperties(couponTemplate, couponTemplateVO);
		
		int availableCount = couponService.searchAvailableCount(id);
		int expiredCount = couponService.searchExpiredCount(id);
		int deletedCount = couponService.searchCount(-1, id);
		int usedCount = couponService.searchCount(1, id);
		couponTemplateVO.setAvailableCount(availableCount);
		couponTemplateVO.setExpiredCount(expiredCount);
		couponTemplateVO.setDeletedCount(deletedCount);
		couponTemplateVO.setUsedCount(usedCount);
		
		String json = couponTemplate.getRangeContent();
		if(StringUtils.equals(null, json) || StringUtils.equals("", json)) {
			return couponTemplateVO;
		}
		
		JSONObject jsonObject = (JSONObject) JSON.parse(json);
		if(couponTemplate.getRangeType() == RangeType.CATEGORY.getValue()) {
			JSONArray jsonArray = (JSONArray) jsonObject.get("categoryIds");
            if (jsonArray != null) {
                List<Long> categoryIds = new ArrayList<Long>();
                Object[] list = jsonArray.toArray();
                for (Object object : list) {
                	Long categoryId = Long.parseLong(object.toString());
                    categoryIds.add(categoryId);
                }

                List<Category> categories = categoryService.search(categoryIds);
                List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
                for (Category category : categories) {
                	JSONObject jsObject = new JSONObject();
                	long parentId = category.getParentId();
                	if (parentId > 0) {
                		Category topCategory = categoryService.getCategoryById(parentId);
                		if (topCategory == null) {
							throw new ParameterErrorException("该分类的顶级分类未找到！");
						}
                		JSONObject jsonObject2 = new JSONObject();
                		jsonObject2.put("id", category.getId());
                		jsonObject2.put("name", category.getCategoryName());
                		
                		jsObject.put("id", topCategory.getId());
                		jsObject.put("name", topCategory.getCategoryName());
                		jsObject.put("is_top", "NO");
                		jsObject.put("child", jsonObject2);
                	} else {
						jsObject.put("id", category.getId());
						jsObject.put("name", category.getCategoryName());
						jsObject.put("is_top", "YES");
					}
                    jsonObjects.add(jsObject);
                }
                couponTemplateVO.setJsonObjects(jsonObjects);
            }
		} else if (couponTemplate.getRangeType() == RangeType.BRAND.getValue()) {
			JSONArray jsonArray = (JSONArray) jsonObject.get("brandIds");
            if (jsonArray != null) {
                List<Long> brandIds = new ArrayList<Long>();
                Object[] list = jsonArray.toArray();
                for (Object object : list) {
                	Long brandId = Long.parseLong(object.toString());
                    brandIds.add(brandId);
                }

                List<BrandLogo> brands = brandLogoService.search(brandIds);
                List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
                for (BrandLogo brand : brands) {
                    JSONObject jsObject = new JSONObject();
                    jsObject.put("id", brand.getBrandId());
                    jsObject.put("name", brand.getBrandName());
                    jsonObjects.add(jsObject);
                }
                couponTemplateVO.setJsonObjects(jsonObjects);
            }
		}
		
		return couponTemplateVO;
	}

	public List<CouponTemplateVO> search(PageQuery pageQuery, CouponTemplate couponTemplate, Integer publishCountMin,
			Integer publishCountMax, Double moneyMin, Double moneyMax) {
		List<CouponTemplate> couponTemplates = couponTemplateService.search(pageQuery, couponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
		
		Set<Long> templateIds = new HashSet<Long>();
		for (CouponTemplate couponTemplate2 : couponTemplates) {
			templateIds.add(couponTemplate2.getId());
		}
		
		Map<Long, Integer> availableCountOfTemplateMap = couponService.availableOfTemplateId(templateIds);
		List<CouponTemplateVO> couponTemplateVOs = new ArrayList<CouponTemplateVO>();
		for (CouponTemplate ct : couponTemplates) {
			CouponTemplateVO couponTemplateVO = new CouponTemplateVO();
			BeanUtils.copyProperties(ct, couponTemplateVO);
			
			couponTemplateVO.setAvailableCount(availableCountOfTemplateMap.get(ct.getId()) == null ? 0 : availableCountOfTemplateMap.get(ct.getId()));
			
			String rangeDescription = assembleRangeDescription(ct.getRangeType(), ct.getRangeContent());

			couponTemplateVO.setRangeDescription(rangeDescription);
			
			couponTemplateVOs.add(couponTemplateVO);
		}
		
		return couponTemplateVOs;
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

	public List<CouponVO> search(PageQuery pageQuery, Coupon coupon, Double moneyMin, Double moneyMax) {
		List<CouponVO> couponVOs = new ArrayList<>();
		List<Coupon> coupons = couponService.search(pageQuery, coupon, moneyMin, moneyMax);
		for (Coupon cp : coupons) {
			CouponVO couponVO = new CouponVO();
			BeanUtils.copyProperties(cp, couponVO);
		
			String rangeDescription = assembleRangeDescription(cp.getRangeType(), cp.getRangeContent());
			couponVO.setRangeDescription(rangeDescription);
			
			couponVOs.add(couponVO);
		}
		
		return couponVOs;
	}
	
}
