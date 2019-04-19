package com.yujj.web.controller.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.JiuCoinExchangeLog;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.CouponTemplateService;
import com.yujj.business.service.OrderCouponService;
import com.yujj.business.service.UserCoinService;
import com.yujj.dao.mapper.CouponTemplateMapper;
import com.yujj.dao.mapper.JiuCoinExchangeLogMapper;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.CouponTemplate;
import com.yujj.exception.ParameterErrorException;

/**
 * @author jeff.zhan
 * @version 2016年12月14日 下午3:52:01
 * 
 */

@Controller
@RequestMapping("/mobile/point")
public class MobilePointController {
	
	private static final Logger logger = LoggerFactory.getLogger("POINT_PAYMENT");
	
	@Autowired
	private OrderCouponService orderCouponService;
	
	@Autowired
	private UserCoinService userCoinService;
	
	@Autowired
	private JiuCoinExchangeLogMapper jiuCoinExchangeLogMapper;
	
	@Autowired
	private CouponTemplateMapper couponTemplateMapper;
	
	@Autowired
	private CouponTemplateService couponTemplateService;
	
	/**
	 * 兑换代金券
	 * @param userDetail
	 * @param costPoint 单价
	 * @param count
	 * @param template_id
	 * @return
	 */
	@RequestMapping("/coupon/exchange")
	@ResponseBody
	@Login
	public synchronized JsonResponse exchange(UserDetail userDetail, @RequestParam("cost_point") int costPoint,
			@RequestParam("count") int count, @RequestParam("template_id") long templateId, @RequestParam("image") String image, ClientPlatform clientPlatform) {
		JsonResponse jsonResponse = new JsonResponse();

		long userId = userDetail.getUserId();
		UserCoin userCoin = userCoinService.getUserCoin(userId);
		
		if (userCoin == null) {
			logger.error("user coin can not null, userId:{}", userId);
			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
		} else if (userCoin.getUnavalCoins() < costPoint * count) {
			logger.error("user coin is less than total consume, user coins:{}, totalConsume:{}", userCoin.getAllCoins(), costPoint * count);
			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
		}
		
		CouponTemplate couponTemplate = couponTemplateService.search(templateId);
		if (couponTemplate.getExchangeJiuCoinSetting() == 0) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("不用于积分兑换");
		}
		if (couponTemplate.getExchangeCount() >= couponTemplate.getExchangeLimitTotalCount() && couponTemplate.getExchangeLimitTotalCount() != -1) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("兑换总量超限");
		}
		
		if (couponTemplate.getExchangeLimitSingleCount() != -1) {
			int exchange_count = jiuCoinExchangeLogMapper.getCount(userDetail.getUserId(), 1, couponTemplate.getId(), couponTemplate.getExchangeStartTime(), couponTemplate.getExchangeEndTime());
			if (exchange_count >= couponTemplate.getExchangeLimitSingleCount()) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("单人限购代金券张数超限");
			}
		}
		
		try {
			exchangeCounpon(userId, costPoint * count, templateId, count, image, clientPlatform);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

	@Transactional(rollbackFor = Exception.class)
	private void exchangeCounpon(long userId, int unavalCoinUsed, long templateId, int count, String image, ClientPlatform clientPlatform) {
		long current = System.currentTimeMillis();
		orderCouponService.getCoupon(templateId, count, userId, CouponGetWay.POINT_EXCHANGE, false);
		userCoinService.updateUserCoin(userId, 0, -unavalCoinUsed, templateId + "", current, UserCoinOperation.COUPON_EXCHANGE, image, clientPlatform.getVersion());
		
		JiuCoinExchangeLog jiuCoinExchangeLog = new JiuCoinExchangeLog();
		jiuCoinExchangeLog.setCount(1);
		jiuCoinExchangeLog.setCreateTime(current);
		jiuCoinExchangeLog.setRelatedId(templateId);
		jiuCoinExchangeLog.setType(1);
		jiuCoinExchangeLog.setUserId(userId);
		jiuCoinExchangeLog.setJiuCoin(unavalCoinUsed);
		jiuCoinExchangeLog.setContent(image);
		jiuCoinExchangeLogMapper.add(jiuCoinExchangeLog);
		
		couponTemplateMapper.updateExchangeCount(templateId);
	}
	
}
