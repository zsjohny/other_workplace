package com.jiuy.supplier.modular.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.SupplierPlacard;
import com.jiuyuan.service.common.IMyFinanceSupplierService;
import com.jiuyuan.service.common.IOrderNewService;
import com.jiuyuan.service.common.ISupplierPlacardService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

	private static final Log logger = LogFactory.get("HomeController");

	@Autowired
	private IMyFinanceSupplierService myFinanceSupplierService;

	@Autowired
	private IOrderNewService supplierOrderService;
	
	@Autowired
	private ISupplierPlacardService supplierPlacardService;
	
	@Autowired
	private RefundOrderMapper refundOrderMapper;

	@RequestMapping("")
	@ResponseBody
	public JsonResponse home() {
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		double availableBalance = myFinanceSupplierService.getAvailableBalance(supplierId);
		int orderCount = myFinanceSupplierService.getCountOfDealingWDOrder(supplierId);
		Object unDealWithCount = supplierOrderService.getSupplierOrderCountUnDealWithCount(supplierId);
		Map<String,Object> totalOrderAmountMap = myFinanceSupplierService.getTotalOrderAmount(supplierId);
		Map<String,Object> settlingAmountMap = myFinanceSupplierService.getSettlingAmount(supplierId);
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("supplier_id", supplierId).eq("refund_status", 1);
		map.put("waitDealRefundOrderCount", refundOrderMapper.selectCount(wrapper ));//待受理售后订单数
		
		Wrapper<RefundOrder> wrapper1 = new EntityWrapper<RefundOrder>().eq("supplier_id", supplierId).eq("refund_status", 3);
		map.put("waitSureRefundOrderCount", refundOrderMapper.selectCount(wrapper1 ));//待确认售后订单数
		
		
		map.put("unDealWithCount", unDealWithCount);
		map.put("availableBalance", availableBalance);
		map.put("OrderCount", orderCount);
		int noReadPlacardCount = supplierPlacardService.getNoReadCount(supplierId);
		logger.info("noReadPlacardCount:"+noReadPlacardCount);
		map.put("noReadPlacardCount", noReadPlacardCount);//	
		map.put("homePageNoReadPlacardList", homePageNoReadPlacardList());//首页未读公告列表
		map.putAll(totalOrderAmountMap);
		map.putAll(settlingAmountMap);
		return jsonResponse.setSuccessful().setData(map);
	}

	
    public List<Map<String,String>> homePageNoReadPlacardList(){
			ShiroUser supplier = ShiroKit.getUser();//当前登录用户供应商ID
			long supplierId = supplier.getId();
			List<SupplierPlacard> supplierPlacardList  = supplierPlacardService.getHomePageNoReadPlacardListTop5(supplierId);
			List<Map<String,String>> supplierPlacardMapList = new ArrayList<Map<String,String>>();
	    	for(SupplierPlacard supplierPlacard: supplierPlacardList){
	    		Map<String,String> supplierPlacardMap = new HashMap<String,String>();
	    		supplierPlacardMap.put("placardId", String.valueOf(supplierPlacard.getId()));//公告ID
	    		supplierPlacardMap.put("placardTitle", supplierPlacard.getTitle());//公告标题
	    		supplierPlacardMap.put("placardContent", supplierPlacard.getContent());//公告内容
	    		supplierPlacardMap.put("publishTime", DateUtil.parseLongTime2Str3(supplierPlacard.getPublishTime()));//发布时间
	    		supplierPlacardMapList.add(supplierPlacardMap);
	    	}
//			logger.info("homePageNoReadPlacardList:",JSON.toJSONString(supplierPlacardMapList));
			return supplierPlacardMapList;
    }
}
