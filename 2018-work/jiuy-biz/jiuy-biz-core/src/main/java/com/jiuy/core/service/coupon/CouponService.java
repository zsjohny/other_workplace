package com.jiuy.core.service.coupon;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.coupon.Coupon;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public interface CouponService {

    int batchAdd(Integer publishCount, CouponTemplate couponTemplate, Long adminId);

    int searchCount(Coupon coupon, Double moneyMin, Double moneyMax);

    List<Coupon> search(PageQuery pageQuery, Coupon coupon, Double moneyMin, Double moneyMax);

    int update(Long id, Integer status);

    /**
     * @param pushStatus 0:待推送,-1:无需推送
     * @return
     */
    List<Coupon> search(Integer pushStatus);

    int update(Long id, Long userId, Long yjjNumbers, Integer status, Integer pushStatus, String pushTitle, String pushDescription, String pushUrl,
               String pushImage, Long adminId);

	int searchCount(Integer status, Long templateId);

	int searchAvailableCount(Long templateId);

	int searchExpiredCount(Long templateId);

	int batchGrant(Integer publishCount, CouponTemplate couponTemplate, List<Long> yjjNumberList,
            Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId);

	Map<Long, Integer> availableOfTemplateId(Collection<Long> templateIds);

	Map<Long, List<Coupon>> getCounponByOrderNo(Collection<Long> nos, String sql);

	/**
	 * 返回代金券
	 * @param status
	 * @param orderNo
	 * @param couponIds
	 * @return
	 */
	int giveBack(Collection<Long> couponIds);

	int updatePushStatus(Collection<Long> ids, Integer pushStatus , Long updateTime);

	Coupon addTypeCoupon(Long templateId, int type, Integer pushStatus, String pushTitle, String pushDescription, String pushUrl, String pushImage, Long adminId);

}
