package com.jiuy.core.service.coupon;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;

public interface StoreCouponService {

    int batchAdd(Integer publishCount, StoreCouponTemplate storeCouponTemplate, Long adminId);

    int searchCount(StoreCoupon storeCoupon, Double moneyMin, Double moneyMax ,Double limitMoneyMin , Double limitMoneyMax);

    List<StoreCoupon> search(PageQuery pageQuery, StoreCoupon storeCoupon, Double moneyMin, Double moneyMax, Double limitMoneyMin , Double limitMoneyMax);

    int update(Long id, Integer status);

    /**
     * @param pushStatus 0:待推送,-1:无需推送
     * @return
     */
    List<StoreCoupon> search(Integer pushStatus);

    int update(Long id, Long userId, Long yjjNumbers, Integer status, Integer pushStatus, String pushTitle, String pushDescription, String pushUrl,
               String pushImage, Long adminId);

	int searchCount(Integer status, Long templateId);

	int searchAvailableCount(Long templateId);

	int searchExpiredCount(Long templateId);

	int batchGrant(Integer publishCount, StoreCouponTemplate storeCouponTemplate, List<Long> yjjNumberList,
            Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId);

	Map<Long, Integer> availableOfTemplateId(Collection<Long> templateIds);

	Map<Long, List<StoreCoupon>> getCounponByOrderNo(Collection<Long> nos, String sql);

	/**
	 * 返回代金券
	 * @param status
	 * @param orderNo
	 * @param couponIds
	 * @return
	 */
	int giveBack(Collection<Long> couponIds);

	int updatePushStatus(Collection<Long> ids, Integer pushStatus , Long updateTime);
	
	void getCoupon(Long templateId, Integer count, long userId, CouponGetWay couponGetWay, boolean needLimit);

	StoreCoupon addTypeCoupon(Long templateId, int type, Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId);

	Map<String, Object> checkPublishObjectNumbers(List<String> publishObjectNumbersList,int type);

	/**
	 * 给新注册并且第一次通过审核的用户发送优惠券
	 * @param couponCount
	 * @param storeBusiness 
	 * @param storeCouponTemplateId 
	 * @return 
	 */
	boolean batchStoreCouponToNewStoreAudit(int couponCount, long storeCouponTemplateId, StoreBusiness storeBusiness);

}
