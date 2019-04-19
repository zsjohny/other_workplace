package com.jiuy.core.service.coupon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.CouponDao;
import com.jiuy.core.dao.CouponTemplateDao;
import com.jiuy.core.dao.GlobalSettingDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.coupon.Coupon;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuy.core.service.UserManageService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class CouponServiceImpl implements CouponService {

	private static final Logger logger = Logger.getLogger(CouponServiceImpl.class);
	
	@Autowired
	private CouponDao couponDao;
	
	@Autowired
	private UserManageService userManageService;
	
	@Autowired
	private GlobalSettingDao globalSettingDao;
	
	@Autowired
	private CouponTemplateDao couponTemplateDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
    public synchronized int batchAdd(Integer publishCount, CouponTemplate couponTemplate, Long adminId) {
		List<CouponTemplate> couponTemplates = new ArrayList<CouponTemplate>();
		for(int i = 0; i < publishCount; i++) {
			couponTemplates.add(couponTemplate);
		}
		
		couponDao.batchAdd(couponTemplates, adminId, null);
		List<Coupon> coupons = couponDao.getNullCode();
		
        generateCode(coupons);
		
		return couponDao.batchUpdate(coupons);
	}

    private String generateCode(List<Coupon> coupons) {
		for(Coupon coupon : coupons) {
            String prefixCode = String.format("%04d", coupon.getId());
			coupon.setCode(prefixCode);
		}
		return null;
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
	public synchronized int batchGrant(Integer publishCount, CouponTemplate couponTemplate, List<Long> yjjNumberList,
            Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId) {
    	Long templateId = couponTemplate.getId();
    	List<Coupon> coupons = couponDao.getAvaliable(templateId);
    	Integer grantCount = publishCount;
    	
    	if (coupons.size() < grantCount) {
			throw new ParameterErrorException("当前已发行的可用代金券不足！");
		}
    	
    	generalGrantCoupon(grantCount, coupons, yjjNumberList, pushStatus, pushTitle, pushDescription, pushUrl, pushImage, adminId);
		
        return couponDao.batchUpdate(coupons);
	}

    private void generalGrantCoupon(Integer count, List<Coupon> coupons, List<Long> yjjNumberList, Integer pushStatus,
                                 String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId) {
    	Map<Long, User> userMap = userManageService.usersMapOfYJJNumbers(yjjNumberList);
        for (int i = 0; i < count; i++) {
            Coupon coupon = coupons.get(i);
            Long yJJNumber = yjjNumberList.get(i);
            coupon.setyJJNumber(yJJNumber);
            User user = userMap.get(yJJNumber);
            if (user == null) {
            	logger.error("com.jiuy.core.service.coupon.CouponServiceImpl: " + yJJNumber + "该俞姐姐号对应的用户为空");
            	throw new ParameterErrorException("com.jiuy.core.service.coupon.CouponServiceImpl: " + yJJNumber + "该俞姐姐号对应的用户为空");
            }
            
            coupon.setUserId(user.getUserId());
            coupon.setPushStatus(pushStatus);
            coupon.setPushTitle(pushTitle);
            coupon.setPushDescription(pushDescription);
            coupon.setPushUrl(pushUrl);
            coupon.setPushImage(pushImage);
            coupon.setGrantAdminId(adminId);
            coupon.setStatus(0);
        }
    }

    @Override
    public int searchCount(Coupon coupon, Double moneyMin, Double moneyMax) {
        return couponDao.searchCount(coupon, moneyMin, moneyMax);
    }

    @Override
    public List<Coupon> search(PageQuery pageQuery, Coupon coupon, Double moneyMin, Double moneyMax) {
        return couponDao.search(pageQuery, coupon, moneyMin, moneyMax);
    }

    @Override
    public int update(Long id, Integer status) {
    	//作废加回可发行量
    	if (status == -1) {
    		
    		List<GlobalSetting> settings = globalSettingDao.getItems(CollectionUtil.createList(GlobalSettingName.COUPON.getStringValue()));
    		if (settings.size() < 1) {
    			throw new ParameterErrorException("作废失败,全局变量设置优惠券字段不存在！");
    		}
    		String couponJson = settings.get(0).getPropertyValue();

	        JSONObject jsonObject = JSONObject.parseObject(couponJson);
	        double publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());
	        Coupon coupon = couponDao.search(id);
	        if (coupon == null) {
	        	throw new ParameterErrorException("作废失败,优惠券id不存在！");
			}
	        publishedMoney -= coupon.getMoney();
	        jsonObject.put("published_money", publishedMoney);
	        globalSettingDao.update(GlobalSettingName.COUPON.getStringValue(), jsonObject.toJSONString());
		}
        return couponDao.update(id, status);
    }

    @Override
    public List<Coupon> search(Integer pushStatus) {
        return couponDao.search(pushStatus);
    }

	@Override
	public int update(Long id, Long userId, Long yjjNumber, Integer status, Integer pushStatus, String pushTitle, String pushDescription,
                      String pushUrl, String pushImage, Long adminId) {
        return couponDao.update(id, userId, yjjNumber, status, pushStatus, pushTitle, pushDescription, pushUrl, pushImage, adminId);
	}

	@Override
	public int searchCount(Integer status, Long templateId) {
		return couponDao.searchCount(status, templateId);
	}

	@Override
	public int searchAvailableCount(Long templateId) {
		return couponDao.searchAvailableCount(templateId);
	}

	@Override
	public int searchExpiredCount(Long templateId) {
		return couponDao.searchExpiredCount(templateId);
	}

	@Override
	public Map<Long, Integer> availableOfTemplateId(Collection<Long> templateIds) {
		if (templateIds.size() < 1) {
			return new HashMap<Long, Integer>();
		}
		List<Map<String, Object>> list = couponDao.availableOfTemplateId(templateIds);
		
		Map<Long, Integer> avaliableMap = new HashMap<Long, Integer>();
		for (Map<String, Object> map : list) {
			avaliableMap.put(Long.parseLong(map.get("couponTemplateId").toString()), Integer.parseInt(map.get("count").toString()));
		}
		
		return avaliableMap;
	}

	@Override
	public Map<Long, List<Coupon>> getCounponByOrderNo(Collection<Long> orderNos, String sortSql) {
		if (orderNos.size() < 1) {
			return new HashMap<Long, List<Coupon>>();
		}
		
		List<Coupon> coupons = couponDao.search(orderNos, sortSql);
		Map<Long, List<Coupon>> map = new HashMap<Long, List<Coupon>>();
		List<Coupon> coList = null;
		Long lastOrderNo = -1L;
		for (Coupon coupon : coupons) {
			Long orderNo = coupon.getOrderNo();
			if (lastOrderNo != orderNo) {
				lastOrderNo = orderNo;
				coList = new ArrayList<Coupon>();
				map.put(lastOrderNo, coList);
			}
			coList.add(coupon);
		}
		
		return map;
	}

	@Override
	public int giveBack(Collection<Long> couponIds) {
		return couponDao.update(0, 0L, couponIds, System.currentTimeMillis());
	}

	@Override
	public int updatePushStatus(Collection<Long> ids, Integer pushStatus, Long updateTime) {
		if (ids.size() < 1) {
			return 0;
		}
		return couponDao.update(ids, pushStatus, updateTime);
	}

	@Override
	public Coupon addTypeCoupon(Long templateId, int type, Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId) {
		CouponTemplate ct = couponTemplateDao.search(templateId);
		Coupon coupon = new Coupon();
		coupon.setCouponTemplateId(ct.getId());
		coupon.setTemplateName(ct.getName());
		coupon.setType(type);
		coupon.setMoney(ct.getMoney());
		coupon.setRangeContent(ct.getRangeContent());
		coupon.setRangeType(ct.getRangeType());
		coupon.setValidityEndTime(ct.getValidityEndTime());
		coupon.setValidityStartTime(ct.getValidityStartTime());
		coupon.setIsLimit(ct.getIsLimit());
		coupon.setCoexist(ct.getCoexist());
		coupon.setStatus(ct.getStatus());
		coupon.setLimitMoney(ct.getLimitMoney());
		coupon.setGetWay(CouponGetWay.FETCH.getValue());
		
		coupon.setPushDescription(pushDescription);
		coupon.setPushImage(pushImage);
		coupon.setPushStatus(pushStatus);
		coupon.setPushTitle(pushTitle);
		coupon.setPushUrl(pushUrl);
		coupon.setGrantAdminId(adminId);
		coupon.setPublishAdminId(adminId);
		
		return couponDao.add(coupon);
	}

	
}
