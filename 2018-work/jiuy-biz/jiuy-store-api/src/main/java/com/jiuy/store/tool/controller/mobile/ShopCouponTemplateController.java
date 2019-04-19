package com.jiuy.store.tool.controller.mobile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.jiuy.rb.enums.CouponPlatEnum;
import com.jiuy.rb.enums.CouponSendEnum;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.enums.CouponTpyeEnum;
import com.jiuy.rb.model.coupon.CouponRbNew;
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.coupon.CouponTemplateNewQuery;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.util.CouponAcceptVo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.coupon.ShopCouponTemplate;
import com.store.entity.coupon.ShopMemberCoupon;
import com.store.service.coupon.ShopCouponTemplateService;
import com.store.service.coupon.ShopMemberCouponFacade;
import com.store.service.coupon.ShopMemberCouponService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 商家优惠券模板开发
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/mobile/coupon")
public class ShopCouponTemplateController {
	 private static final Log logger = LogFactory.get();
    @Autowired
    ShopCouponTemplateService shopCouponTemplateService;
 
    
    @Autowired
    ShopMemberCouponService shopMemberCouponService;
    
    @Autowired
    private ShopMemberCouponFacade shopMemberCouponFacade;

    @Autowired
    private ICouponServerNew couponServerNew;
    
    /**
     * 获取商家模板列表
     * tag ：标签：可用优惠券（1可用优惠券，0失效优惠券）
     * @param current	当前是第几页
	 * @param size	每页显示条数
     */
    @RequestMapping("/getCouponTemplateList/auth")
    @Login
    @ResponseBody
    public JsonResponse getCouponTemplateList(@RequestParam(required = true) int tag,
    		@RequestParam(required=false, defaultValue = "1")int current, 
    		@RequestParam(required=false, defaultValue = "30") int size ,
    		UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) { 	
    	JsonResponse jsonResponse = new JsonResponse();

    	try {
     		long storeId = userDetail.getId();
     		checkStoreId(storeId);
     		logger.info("获取商家优惠券模板列表，tag:"+tag+",storeId:"+storeId);

			CouponTemplateNewQuery query = new CouponTemplateNewQuery();
			query.setPublishUserId(storeId);
			query.setLimit(size);
			query.setOffset((current - 1) * size);
			query.setSysType(CouponSysEnum.WXA.getCode());
			query.setNotSending(tag == 1 ? 0 : 1);
			if(tag==1) {
				query.setStatus(0);
			}
       	 	//获取数据
   	 		return jsonResponse.setSuccessful().setData(couponServerNew.tempPageApp(query));
    	} catch (Exception e) {
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }



	/**
	 * 添加商家模板列表
	 *
	 * @return
	 */
	@RequestMapping("/setCouponTemplate/auth")
	@Login
	@ResponseBody
	public JsonResponse setCouponTemplate(UserDetail<StoreBusiness> userDetail, ShopCouponTemplate shopTemp) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			checkStoreId(storeId);
			//获取数据
			CouponTemplateNew temp = new CouponTemplateNew();
			temp.setName(shopTemp.getName());
			temp.setEachReceiveCount(1);
			temp.setDeadlineBegin(new Date(shopTemp.getValidityStartTime()));
			temp.setDeadlineEnd(new Date(shopTemp.getValidityEndTime()));
			temp.setDeadlineType(0);
			temp.setGetRule("{}");
			temp.setLimitMoney(new BigDecimal(shopTemp.getLimitMoney()));
			temp.setPrice(new BigDecimal(shopTemp.getMoney()));
			temp.setIssueCount(Long.valueOf(shopTemp.getGrantCount()));
			temp.setPublishUserId(storeId);
			temp.setCouponType(CouponTpyeEnum.COUPON.getCode());
			temp.setPlatformType(CouponPlatEnum.APP.getCode());
			temp.setPublishUser(userDetail.getUserDetail().getCompanyName());
			temp.setUseRange(1);
			temp.setStatus(0);
			temp.setSysType(CouponSysEnum.WXA.getCode());
			temp.setCreateTime(new Date());
			temp.setSendType(CouponSendEnum.RECEIVE_SELF.getCode());
			couponServerNew.addCouponTemplate(temp);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}


	/**
	 * 停止发放优惠券模板
	 * @param id 优惠券模板id
	 * @return
	 */
	@RequestMapping("/updStopCouponTemplate/auth")
	@Login
	@ResponseBody
	public JsonResponse updStopCouponTemplate(
			@RequestParam("id") long id,
			UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			checkStoreId(storeId);
			if(id<=0){
				throw new RuntimeException("参数有错误id="+id);
			}
			logger.info("停止发放优惠券id:"+id+",storeId:"+storeId);
			couponServerNew.stopTempSend(id,storeId,1);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}


    /**
     * 删除优惠券模板
     * @param id 优惠券模板id
     * @return
     */
    @RequestMapping("/delCouponTemplate/auth")
    @Login
    @ResponseBody
    public JsonResponse delCouponTemplate(
    		@RequestParam("id") long id,
    		UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) { 	
     	JsonResponse jsonResponse = new JsonResponse();
     	try {
     		long storeId = userDetail.getId();
     		checkStoreId(storeId);
     		if(id<=0){
     			throw new RuntimeException("参数有错误id="+id);
     		}
     		logger.info("删除优惠券模板id:"+id+",storeId:"+storeId);
			couponServerNew.stopTempSend(id,storeId,-1);
       	 	return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 核销优惠券
     * @param id 会员优惠券id
     * @return
     */
    @RequestMapping("/updMemberCoupon/auth")
    @Login
    @ResponseBody
    public JsonResponse updMemberCoupon(
    		@RequestParam("id") long id,
    		UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
     	JsonResponse jsonResponse = new JsonResponse();
     	try {
     		long storeId = userDetail.getId();
     		checkStoreId(storeId);
     		if(id<=0){
     			throw new RuntimeException("参数有错误id="+id);
     		}
     		logger.info("删除优惠券id:"+id+",storeId:"+storeId);
     		Map<String, String> result = couponServerNew.hx(id,storeId);
       	 	return jsonResponse.setSuccessful().setData(result);
    	} catch (Exception e) {
     		e.printStackTrace();
    		logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }


	/**
	 * 核销记录列表
	 * @return
	 */
	@RequestMapping("/getMemberCouponUsedList/auth")
	@Login
	@ResponseBody
	public JsonResponse getMemberCouponUsedList(@RequestParam(required=false, defaultValue = "1")int current,
												@RequestParam(required=false, defaultValue = "50") int size ,
												UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();

		try {
			long storeId = userDetail.getId();
			logger.info("核销记录列表storeId:"+storeId);
			checkStoreId(storeId);

			CouponRbNewQuery query = new CouponRbNewQuery();
			query.setStoreId(storeId);
			query.setStatus(1);
			query.setLimit(size);
			query.setOffset((current - 1) * size);
			query.setUserType(CouponSysEnum.WXA.getCode());
			Map<String, Object> data = couponServerNew.appHxList(query);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}

































	/**
	 * 删除优惠券模板
	 * @param id 优惠券模板id
	 * @return
	 */
	@RequestMapping("/delCouponTemplatebak/auth")
	@Login
	@ResponseBody
	public JsonResponse delCouponTemplatebak(
			@RequestParam("id") long id,
			UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			checkStoreId(storeId);
			if(id<=0){
				throw new RuntimeException("参数有错误id="+id);
			}
			logger.info("删除优惠券模板id:"+id+",storeId:"+storeId);
			shopCouponTemplateService.delCouponTemplate(id,storeId,ip,client);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}

	/**
	 * 获取商家模板列表
	 * tag ：标签：可用优惠券（1可用优惠券，0失效优惠券）
	 * @param current	当前是第几页
	 * @param size	每页显示条数
	 */
	@RequestMapping("/getCouponTemplateListbak/auth")
	@Login
	@ResponseBody
	public JsonResponse getCouponTemplateListbak(@RequestParam(required = true) int tag,
												 @RequestParam(required=false, defaultValue = "1")int current,
												 @RequestParam(required=false, defaultValue = "30") int size ,
												 UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			checkStoreId(storeId);
			logger.info("获取商家优惠券模板列表，tag:"+tag+",storeId:"+storeId);
			//获取数据
			Page<ShopCouponTemplate> page = shopCouponTemplateService.getCouponTemplateList(tag,new Page(current,size),storeId);
			//组装数据
			SmallPage smallPage = new SmallPage(page);
			logger.info("获取商家优惠券模板列表，smallPage:"+JSONObject.toJSONString(smallPage));
			return jsonResponse.setSuccessful().setData(smallPage);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}

	/**
	 * 核销优惠券
	 * @param id 会员优惠券id
	 * @return
	 */
	@RequestMapping("/updMemberCouponbak/auth")
	@Login
	@ResponseBody
	public JsonResponse updMemberCouponbak(
			@RequestParam("id") long id,
			UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			checkStoreId(storeId);
			if(id<=0){
				throw new RuntimeException("参数有错误id="+id);
			}
			logger.info("删除优惠券id:"+id+",storeId:"+storeId);
			Map<String, String> result = shopMemberCouponFacade.updMemberCoupon(id,storeId,ip,client);
			return jsonResponse.setSuccessful().setData(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}


    /**
     * 金额面值取证或者保留2位小数
     * @param money
     * @return
     */
    public String getStringMoney(Double money) {
		if(money.intValue()-money==0){//判断是否符合取整条件  
		    return money.intValue()+"";
		}else{  
			return String.format("%.2f", money);
		}
	}
    
    
	private void checkStoreId(long storeId) {
	 	if(storeId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
	 	}
	}


	/**
	 * 添加商家模板列表
	 * @param name 优惠券标题
	 * @param money  面值
	 * @param limitMoney 限额
	 * @param grantCount 发放张数
	 * @param validityStartTime 有效开始时间
	 * @param validityEndTime 有效结束时间
	 * @return
	 */
	@RequestMapping("/setCouponTemplatebak/auth")
	@Login
	@ResponseBody
	public JsonResponse setCouponTemplatebak(
			@RequestParam(required = false) String name,
			@RequestParam(required=true, defaultValue = "0") double money,
			@RequestParam(required=true, defaultValue = "0") double  limitMoney ,
			@RequestParam(required=true, defaultValue = "0") int  grantCount ,
			@RequestParam(required=true) long  validityStartTime ,
			@RequestParam(required=true) long  validityEndTime ,
			UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			checkStoreId(storeId);
			//获取数据
			shopCouponTemplateService.setCouponTemplate(name,money,limitMoney,grantCount,validityStartTime,validityEndTime,storeId,ip,client);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}



	/**
	 * 停止发放优惠券模板
	 * @param id 优惠券模板id
	 * @return
	 */
	@RequestMapping("/updStopCouponTemplatebak/auth")
	@Login
	@ResponseBody
	public JsonResponse updStopCouponTemplatebak(
			@RequestParam("id") long id,
			UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			checkStoreId(storeId);
			if(id<=0){
				throw new RuntimeException("参数有错误id="+id);
			}
			logger.info("停止发放优惠券id:"+id+",storeId:"+storeId);
			shopCouponTemplateService.updStopCouponTemplate(id,storeId,ip,client);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}


	/**
	 * 核销记录列表
	 * @return
	 */
	@RequestMapping("/getMemberCouponUsedListbak/auth")
	@Login
	@ResponseBody
	public JsonResponse getMemberCouponUsedListbak(@RequestParam(required=false, defaultValue = "1")int current,
												   @RequestParam(required=false, defaultValue = "50") int size ,
												   UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();

		CouponRbNewQuery query = new CouponRbNewQuery();
		couponServerNew.myCouponList(query);

		try {
			long storeId = userDetail.getId();
			logger.info("核销记录列表storeId:"+storeId);
			checkStoreId(storeId);
			Page<ShopMemberCoupon> shopMemberCouponPage = shopMemberCouponService.getMemberCouponUsedList(storeId,new Page<ShopMemberCoupon>(current,size));
			//组装数据
			List<ShopMemberCoupon> shopMemberCoupons = shopMemberCouponPage.getRecords();
			Map<String,Object> data = new HashMap<String,Object>();
			List<Map<String,String>> shopMemberCouponList = new ArrayList<Map<String,String>>();
			List<Long> memberIdList = new ArrayList<Long>();
			double allMoney = 0;
			int count = 0;
			for (ShopMemberCoupon shopMemberCoupon : shopMemberCoupons) {
				Map<String,String> shopMemberCouponMap = new HashMap<String,String>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
				shopMemberCouponMap.put("checkTime", sdf.format(new Date(shopMemberCoupon.getCheckTime()))+"");
				double money = shopMemberCoupon.getMoney();
				shopMemberCouponMap.put("money", shopMemberCoupon.getStringMoney());
				shopMemberCouponMap.put("memberNicheng", shopMemberCoupon.getMemberNicheng());
				long memberId = shopMemberCoupon.getMemberId();
				if(!(memberIdList.contains(memberId))){
					memberIdList.add(memberId);
				}
				allMoney+=money;
				count+=1;
				shopMemberCouponList.add(shopMemberCouponMap);
			}
			SmallPage smallPage = new SmallPage(shopMemberCouponPage);
			smallPage.setRecords(shopMemberCouponList);
			data.put("shopMemberCouponList", smallPage);
			data.put("allMoney", getStringMoney(allMoney));
			data.put("count", count);
			data.put("memberSize", memberIdList.size());
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}



}


