package com.jiuy.supplier.modular.couponManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.enums.*;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.coupon.CouponTemplateNewQuery;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.dao.mapper.supplier.ShopExpressSupplierMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponTemplateNewMapper;
import com.jiuyuan.entity.newentity.StoreCouponNew;
import com.jiuyuan.entity.newentity.StoreCouponTemplateNew;
import com.jiuyuan.service.common.IStoreCouponNewService;
import com.jiuyuan.service.common.StoreCouponNewService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 我的优惠券控制器
 *
 * @author fengshuonan
 * @Date 2018-03-13 10:24:06
 */
@Controller
@RequestMapping("/myCoupon")
public class MyCouponController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(MyCouponController.class);

    private String PREFIX = "/couponManage/myCoupon/";
    
    @Autowired
    private IStoreCouponNewService storeCouponNewService;


    @Autowired
	private ICouponServerNew couponServerNew;

    /**
     * 跳转到我的优惠券首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "myCoupon.html";
    }

    /**
     * 跳转到添加我的优惠券
     */
    @RequestMapping("/myCoupon_add")
    public String myCouponAdd() {
        return PREFIX + "myCoupon_add.html";
    }

    /**
     * 跳转到修改我的优惠券
     */
    @RequestMapping("/myCoupon_edit")
    public String myCouponEdit() {
        return PREFIX + "myCoupon_edit.html";
    }




    /**
     * 停止领取我的优惠券
     */
    @RequestMapping("/stopDrawStoreCoupon")
    @ResponseBody
    @AdminOperationLog
    public Object stopDrawStoreCoupon(@RequestParam("storeCouponTemplateId") long storeTemplateId
    		                          ){
    	JsonResponse jsonResponse = new JsonResponse();
    	long supplierId = ShiroKit.getUser().getId();
    	try {
    		storeCouponNewService.stopDrawStoreCoupon(supplierId, storeTemplateId);
			return jsonResponse.setSuccessful();
    	} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    	
    }

    /**
     * 获取我的优惠券列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(
    		@RequestParam(value = "couponName",required = false) String  couponName,//优惠券模板名称
    		@RequestParam(value = "minMoney",required = false) Double minMoney,//优惠券面值下限
    		@RequestParam(value = "maxMoney",required = false) Double maxMoney,//优惠券面值上限
    		@RequestParam(value = "minValidStartTime",required = false) String minValidStartTime,//使用有效期下限
    		@RequestParam(value = "maxValidEndTime",required = false) String maxValidEndTime,//使用有效期上限
    		@RequestParam(value = "minValidTotalCount",required = false) Integer minValidTotalCount,//券数量下限
    		@RequestParam(value = "maxValidTotalCount",required = false) Integer maxValidTotalCount,//券数量上限
    		@RequestParam(value = "minValidTotalAmount",required = false) Double minValidTotalAmount,//券总额下限
    		@RequestParam(value = "maxValidTotalAmount",required = false) Double maxValidTotalAmount,//券总额上限
    		@RequestParam(value = "publishStatus",required = false,defaultValue = "-1") int publishStatus//发放状态 -1：全部 0：未发放 1：发放中 2：已停止 3：已作废 
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = ShiroKit.getUser().getId();
    	try {
			CouponTemplateNewQuery query = new CouponTemplateNewQuery();
			query.setPublishUserId(userId);
			if(Biz.isNotEmpty(couponName)) {
				query.setName(couponName);
			}
			return couponServerNew.tempPageSupplier(query);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }
    
    /**
	 * 获取当前可用优惠券总额，历史消费优惠券总额
	 */
    @RequestMapping("/getStatistics")
    @ResponseBody
    public Object getStatistics(){
    	JsonResponse jsonResponse = new JsonResponse();
    	long supplierId = ShiroKit.getUser().getId();
    	try {
    		Map<String,Object> map = new HashMap<>(2);

    		Map<String,Object> param = new HashMap<>(4);
    		param.put("publishUserId",supplierId);
    		param.put("userType",CouponSysEnum.APP.getCode());
    		param.put("status",CouponStateEnum.USED.getCode());
			param.put("hasOrderNo",1);
    		// 查询已使用的
    		BigDecimal historyUsedCouponAmount = couponServerNew.sumTempPrice(param);

    		// 查询未使用的
			param.put("status",CouponStateEnum.NOT_USE.getCode());
			param.remove("hasOrderNo");
			param.put("alive",1);
			BigDecimal currentTotalAmount = couponServerNew.sumTempPrice(param);

			map.put("currentTotalAmount", currentTotalAmount == null ? 0 :currentTotalAmount);
			map.put("historyUsedCouponAmount", historyUsedCouponAmount == null ? 0 :historyUsedCouponAmount);

    		return jsonResponse.setSuccessful().setData(map);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    	
    }


	/**
	 * 新增我的优惠券
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
	public Object addb(@RequestParam("couponName") String CouponName,//优惠券名称
						 @RequestParam("limitMoney") double limitMoney,//限定使用金额条件
						 @RequestParam("money") double money,//优惠金额
						 @RequestParam("validityStartTime") String validityStartTime,//使用开始时间
						 @RequestParam("validityEndTime") String validityEndTime,//使用结束时间
						 @RequestParam("validTotalCount") Long validTotalCount,//可用券总数
						 @RequestParam("limitDraw") int limitDraw,//限领
						 @RequestParam("drawStartTime") String drawStartTime,//领取开始时间
						 @RequestParam("drawEndTime") String drawEndTime,//领取结束时间
						 @RequestParam("rangeType") int RangeType//范围类型：目前商家优惠券模板0:通用，4:邮费
	) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser user = ShiroKit.getUser();
		long userId = ShiroKit.getUser().getId();
		String publisher = ShiroKit.getUser().getBusinessName();
		try {
			CouponTemplateNew templateNew = new CouponTemplateNew();
			templateNew.setCreateTime(new Date());
			templateNew.setStatus(0);
			templateNew.setGetRule("{}");
			templateNew.setDeadlineBegin(Biz.dateStr2Date(validityStartTime,"yyyy-MM-dd HH:mm:ss"));
			templateNew.setDeadlineEnd(Biz.dateStr2Date(validityEndTime,"yyyy-MM-dd HH:mm:ss"));
			templateNew.setDeadlineType(0);
			templateNew.setEachReceiveCount(limitDraw);
			templateNew.setIssueCount(validTotalCount);
			templateNew.setLimitMoney(new BigDecimal(limitMoney));
			templateNew.setPublishUser(publisher);
			templateNew.setPublishUserId(userId);
			templateNew.setPrice(new BigDecimal(money));
			templateNew.setName(CouponName);
			templateNew.setPlatformType(CouponPlatEnum.SUPPLIER.getCode());
			templateNew.setCouponType(CouponTpyeEnum.COUPON.getCode());
			templateNew.setSysType(CouponSysEnum.APP.getCode());
			templateNew.setSendType(CouponSendEnum.RECEIVE_SELF.getCode());
			templateNew.setUseRange(1);
			//templateNew.setRangeIds(user.getBrandId()+"");
			templateNew.setUpdateTime(new Date());
			templateNew.setDrawEndTime(Biz.dateStr2Date(drawEndTime,"yyyy-MM-dd HH:mm:ss"));
			templateNew.setDrawStartTime(Biz.dateStr2Date(drawStartTime,"yyyy-MM-dd HH:mm:ss"));
			couponServerNew.addCouponTemplate(templateNew);
			return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
	}



	/**
     * 新增我的优惠券
     */
    @RequestMapping(value = "/addbak")
    @ResponseBody
    @AdminOperationLog
    public Object addbak(@RequestParam("couponName") String CouponName,//优惠券名称
    		          @RequestParam("limitMoney") double limitMoney,//限定使用金额条件
    		          @RequestParam("money") double money,//优惠金额
    		          @RequestParam("validityStartTime") String validityStartTime,//使用开始时间
    		          @RequestParam("validityEndTime") String validityEndTime,//使用结束时间
    		          @RequestParam("validTotalCount") int validTotalCount,//可用券总数
    		          @RequestParam("limitDraw") int limitDraw,//限领
    		          @RequestParam("drawStartTime") String drawStartTime,//领取开始时间
    		          @RequestParam("drawEndTime") String drawEndTime,//领取结束时间
    		          @RequestParam("rangeType") int RangeType//范围类型：目前商家优惠券模板0:通用，4:邮费
    		          ) {
    	JsonResponse jsonResponse = new JsonResponse();
    	ShiroUser user = ShiroKit.getUser();
    	long userId = ShiroKit.getUser().getId();
    	String publisher = ShiroKit.getUser().getBusinessName();
    	try {
    		StoreCouponTemplateNew storeCouponTemplateNew = new StoreCouponTemplateNew();
    		storeCouponTemplateNew.setName(CouponName);
    		storeCouponTemplateNew.setLimitMoney(new BigDecimal(limitMoney));
    		storeCouponTemplateNew.setMoney(new BigDecimal(money));
    		long validStartTime = DateUtil.parseStrTime2Long(validityStartTime);
    		long validEndTime = DateUtil.parseStrTime2Long(validityEndTime);
    		long drawStart = DateUtil.parseStrTime2Long(drawStartTime);
    		long drawEnd = DateUtil.parseStrTime2Long(drawEndTime);
    		storeCouponTemplateNew.setValidityStartTime(validStartTime);
    		storeCouponTemplateNew.setValidityEndTime(validEndTime);
    		storeCouponTemplateNew.setValidTotalCount(validTotalCount);
    		storeCouponTemplateNew.setLimitDraw(limitDraw);
    		storeCouponTemplateNew.setDrawStartTime(drawStart);
    		storeCouponTemplateNew.setDrawEndTime(drawEnd);
    		storeCouponTemplateNew.setRangeType(RangeType);
    		
    		StringBuffer rangeTypeIds = new StringBuffer(",");
    		rangeTypeIds.append(user.getBrandId())
    		            .append(",");
    		storeCouponTemplateNew.setRangeTypeIds(rangeTypeIds.toString());
    		
    		StringBuffer rangeTypeNames = new StringBuffer(",");
    		rangeTypeNames.append(user.getBrandName())
    		              .append(",");
    		storeCouponTemplateNew.setRangeTypeNames(rangeTypeNames.toString());
    		
    		storeCouponTemplateNew.setSupplierId(userId);
    		storeCouponTemplateNew.setPublisher(publisher);
    		storeCouponNewService.add(storeCouponTemplateNew);
			return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }

    /**
     * 删除我的优惠券
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @AdminOperationLog
    public Object delete(@RequestParam("storeCouponTemplateId") long storeCouponTemplateId//优惠券模板ID
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long supplierId = ShiroKit.getUser().getId();
    	try {

			CouponTemplateNewQuery query = new CouponTemplateNewQuery();
			query.setId(storeCouponTemplateId);
			query.setPublishUserId(supplierId);
			CouponTemplateNew old = couponServerNew.getOneTemp(query);
			if(old == null) {
				return jsonResponse.setError("找不到模板信息");
			}

			CouponTemplateNew newTemp = new CouponTemplateNew();
			newTemp.setId(storeCouponTemplateId);
			newTemp.setStatus(-1);
			couponServerNew.updateTemp(newTemp);
			return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }


    /**
     * 修改我的优惠券
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @AdminOperationLog
    public Object update(@RequestParam("couponName") String CouponName,//优惠券名称
	                     @RequestParam("limitMoney") double limitMoney,//限定使用金额条件
	                     @RequestParam("money") double money,//优惠金额
	                     @RequestParam("validityStartTime") String validityStartTime,//使用开始时间
	                     @RequestParam("validityEndTime") String validityEndTime,//使用结束时间
	                     @RequestParam("validTotalCount") Long validTotalCount,//可用券总数
	                     @RequestParam("limitDraw") int limitDraw,//限领
	                     @RequestParam("drawStartTime") String drawStartTime,//领取开始时间
	                     @RequestParam("drawEndTime") String drawEndTime,//领取结束时间
	                     @RequestParam("rangeType") int RangeType,//范围类型：目前商家优惠券模板0:通用，4:邮费
	                     @RequestParam("storeCouponTemplateId") long storeCouponTemplateId//优惠券模板ID
    		) {
    	JsonResponse jsonResponse = new JsonResponse();

    	try {

    		Long pushId = ShiroKit.getUser().getId().longValue();
    		CouponTemplateNewQuery query = new CouponTemplateNewQuery();
    		query.setId(storeCouponTemplateId);
    		query.setPublishUserId(pushId);
    		CouponTemplateNew old = couponServerNew.getOneTemp(query);
    		if(old == null) {
				return jsonResponse.setError("找不到模板信息");
			}
    		CouponTemplateNew couponTemplate = new CouponTemplateNew();
    		couponTemplate.setDrawStartTime(Biz.dateStr2Date(drawStartTime,null));
    		couponTemplate.setDrawEndTime(Biz.dateStr2Date(drawEndTime,null));
			couponTemplate.setId(storeCouponTemplateId);
			couponTemplate.setName(CouponName);
			couponTemplate.setLimitMoney(new BigDecimal(limitMoney));
			couponTemplate.setPrice(new BigDecimal(money));
			couponTemplate.setDeadlineBegin(Biz.dateStr2Date(validityStartTime,null));
			couponTemplate.setDeadlineEnd(Biz.dateStr2Date(validityEndTime,null));
			couponTemplate.setIssueCount(validTotalCount);
			couponTemplate.setEachReceiveCount(limitDraw);
			couponServerNew.updateTemp(couponTemplate);
    		return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }

    /**
     * 我的优惠券详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail(@RequestParam("storeCouponTemplateId") long storeCouponTemplateId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = ShiroKit.getUser().getId();
    	try {

			CouponTemplateNewQuery query = new CouponTemplateNewQuery();
			query.setId(storeCouponTemplateId);
    		CouponTemplateNew template = couponServerNew.getOneTemp(query);

    		Map<String,Object> map = new HashMap<>(13);

    		map.put("ValidityEndTime",Biz.formatDate(template.getDeadlineEnd(),null));
			map.put("supplierId",template.getPublishUserId());
			map.put("validTotalAmount",template.getPrice().multiply(new BigDecimal(template.getIssueCount())));
			map.put("limitDraw",template.getEachReceiveCount());
			map.put("Name",template.getName());
			map.put("Money",template.getPrice());
			map.put("drawEndTime",Biz.formatDate(template.getDrawEndTime(),null));
			map.put("LimitMoney",template.getLimitMoney());
			map.put("ValidityStartTime",Biz.formatDate(template.getDeadlineBegin(),null));
			map.put("drawStartTime",Biz.formatDate(template.getDrawStartTime(),null));
			map.put("validTotalCount",template.getIssueCount());
			map.put("publisher",template.getPublishUser());
			map.put("Id",template.getId());

    		return jsonResponse.setSuccessful().setData(map);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }
}
