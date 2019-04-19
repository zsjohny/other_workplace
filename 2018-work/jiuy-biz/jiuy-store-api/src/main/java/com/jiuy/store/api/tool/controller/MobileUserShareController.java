package com.jiuy.store.api.tool.controller;

import java.util.HashMap;
import java.util.Map;

import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.service.common.IMyFinanceSupplierService;
import com.jiuyuan.service.common.IStoreOrderNewService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.StoreUserService;
import com.store.service.StoreWxaService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping("/mobile/user")
public class MobileUserShareController {
	
	private static final Logger logger = LoggerFactory.getLogger(MobileUserShareController.class);
    Log log = LogFactory.get();

	@Autowired
	private UserNewMapper userNewMapper;
    @Autowired
	private StoreUserService storeUserService;
    
	@Autowired
	private StoreWxaService storeWxaService;
	
	@Autowired
	private IMyFinanceSupplierService myFinanceSupplierService;
	
	@Autowired
	private IStoreOrderNewService storeOrderNewService;
	

	
	/**
	 * 获取供应商信息
	 */
	@RequestMapping("/getSupplierInfo/auth")
	@ResponseBody
	public JsonResponse getSupplierInfo(UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> data = new HashMap<String,Object>();
		long storeId = userDetail.getUserDetail().getId();
		if(storeId == 0){
			return jsonResponse.setError("该门店商家不存在！");
		}
		long supplierId = userDetail.getUserDetail().getSupplierId();
		if(supplierId <= 0){
			return jsonResponse.setError("该门店商家不存在关联的供应商ID！");
		}
		//总订单总额信息
		data.putAll(myFinanceSupplierService.getTotalOrderAmount(supplierId));
		data.putAll(myFinanceSupplierService.getSettlingAmount(supplierId));
		//可提现金额
		double availableBalance = myFinanceSupplierService.getAvailableBalance(supplierId);
		data.put("availableBalance", availableBalance);
		//未处理提现申请订单数
		int countOfDealingWDOrder = myFinanceSupplierService.getCountOfDealingWDOrder(supplierId);
		data.put("countOfDealingWDOrder", countOfDealingWDOrder);
		//销售订单管理
		//待发货订单数
		Object paidCount = storeOrderNewService.getSupplierOrderCountUnDealWithCount(supplierId);
		//待收货订单数
		Object deliverCount = storeOrderNewService.getSupplierOrderCountHasDelivered(supplierId);
		//已退单订单数
		Object refundedCount = storeOrderNewService.getSupplierOrderCountRefunded(supplierId);
		data.put("paidCount", paidCount);
		data.put("deliverCount", deliverCount);
		data.put("refundedCount", refundedCount);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 获取最低提现范围及可提现金额
	 */
	@RequestMapping("getMinWithdrawAndAvailableBalance")
	@ResponseBody
	public JsonResponse getMinWithdrawAndAvailableBalance(UserDetail userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		return jsonResponse.setSuccessful();
	}
    /**
     * 获取商家分享页面信息
     * @return
     */
    @RequestMapping("/getsharedinfo")
    @ResponseBody
    public JsonResponse getSharedInfo(@RequestParam("storeId")long storeId) {
    	logger.error("getSharedInfo获取商家分享页面信息storeId:"+storeId);
    	JsonResponse jsonResponse = new JsonResponse();
    	StoreBusiness storeBusiness = storeUserService.getStoreBusinessById(storeId);
    	if(storeBusiness==null){
    		return jsonResponse.setError("该商家不存在");
    	}else if(storeBusiness.getStatus()!=0){
    		return jsonResponse.setError("该商家状态异常");
    	}
   	    Map<String,String> data = new HashMap<String,String>();
   	    data.put("businessName", storeBusiness.getBusinessName());
   	    
   	    String province = storeBusiness.getProvince();
   	    if(province==null){
   	    	data.put("province", "");
   	    }else if("省".equals(province.substring(province.length()-1)) || "市".equals(province.substring(province.length()-1))){
   	    	data.put("province", province.substring(0,province.length()-1));
   	    }else if("自治区".equals(province.substring(province.length()-3))){
   	    	data.put("province", province.substring(0,province.length()-3));
   	    }else{
   	    	data.put("province", province);
   	    }
   	    
   	    String city = storeBusiness.getCity();
   	    if(city==null || "市".equals(province.substring(province.length()-1))){
   	    	data.put("city", "");
   	    }else if("市".equals(city.substring(city.length()-1))){
   	    	data.put("city", city.substring(0,city.length()-1));
   	    }else{
   	    	data.put("city", city);
   	    }


		Integer wxaShopTypoe = storeBusiness.getWxaBusinessType();
		if (wxaShopTypoe == 1) {
			//店中店
            logger.info("店中店storeId={}", storeId);
			String path;
			Map<String, Object> auditEntity = userNewMapper.getShareShopLoginQr(storeId);
			if (auditEntity == null) {
				logger.info("没有店铺资料");
				path = "";
			} else {
				path = (String) auditEntity.get("shareQcCodeUrl");
			}
			data.put("qrcodeUrl", path);
		}
		else {
			//专享店铺
            logger.info("专享店铺storeId={}", storeId);
			StoreWxa storeWxa = storeWxaService.getStoreWxaByStoreId(storeId+"");
			if(storeWxa==null){
				data.put("qrcodeUrl", "");
			}else{
				data.put("qrcodeUrl", storeWxa.getQrcodeUrl());
			}
		}

   	    data.put("text", "微信扫一扫,关注更多精品女装");
   	 	return jsonResponse.setSuccessful().setData(data);
    }
}
