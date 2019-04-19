package com.jiuy.operator.modular.agentSalesManagement.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuy.operator.core.shiro.ShiroUser;
import com.jiuy.operator.modular.agentProductManagement.controller.AllAgentProductController;
import com.jiuyuan.entity.newentity.ProxyOrder;
import com.jiuyuan.entity.newentity.ProxyProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.IProxyOrderService;
import com.jiuyuan.service.common.IProxyProductService;
import com.jiuyuan.service.common.IProxyUserService;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;

import java.util.ArrayList;
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
 * 所有代理销售订单控制器
 *
 * @author fengshuonan
 * @Date 2018-04-03 11:26:55
 */
@Controller
@RequestMapping("/allAgentSalesOrder")
public class AllAgentSalesOrderController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(AllAgentSalesOrderController.class);
	
    private String PREFIX = "/agentSalesManagement/allAgentSalesOrder/";
    
    @Autowired
    private IProxyProductService proxyProductService;
    
    @SuppressWarnings("unused")
	@Autowired
    private IProxyUserService proxyUserService;
    
    @Autowired
    private IProxyOrderService proxyOrderService;
    
    
    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;
    
    /**
     * 关闭订单
     */
    @RequestMapping(value = "/closeOrder")
    @ResponseBody 
    public JsonResponse closeOrder(
    		@RequestParam(value = "orderId",required = true) long orderId//代理订单ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			proxyOrderService.closeOrder(orderId);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("关闭订单:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    /**
     * 确认订单
     */
    @RequestMapping(value = "/confirmOrder")
    @ResponseBody 
    public JsonResponse confirmOrder(
    		@RequestParam(value = "orderId",required = true) long orderId,//代理订单ID
    		@RequestParam(value = "applyFullName",required = true) String applyFullName,//申请人姓名
    		@RequestParam(value = "proxyProductCount",required = true) int proxyProductCount//申请开通服务数量
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			proxyOrderService.confirmOrder(orderId,applyFullName,proxyProductCount);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("确认订单:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 完成订单
     */
    @RequestMapping(value = "/finishOrder")
    @ResponseBody
    public JsonResponse finishOrder(
    		@RequestParam(value = "orderId",required = true) long orderId//代理订单ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
    	logger.info("赵兴林开启完成订单任务！");
    	logger.debug("赵兴林开启完成订单任务！");
    	logger.error("赵兴林开启完成订单任务！");
		try {
			proxyOrderService.finishOrder(orderId);
			logger.info("赵兴林结束完成订单任务！");
	    	logger.debug("赵兴林结束完成订单任务！");
	    	logger.error("赵兴林结束完成订单任务！");
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("确认订单:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 查看代理订单
     */
    @RequestMapping(value = "/showProxyOrder")
    @ResponseBody 
    public JsonResponse showProxyProduct(
    		@RequestParam(value = "orderId",required = true) long orderId//代理订单ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			ProxyOrder order  = proxyOrderService.getProxyOrder(orderId);
			Map<String,String> map = new HashMap<String,String>();
    		map.put("orderId", String.valueOf(orderId));//代理订单ID
    		map.put("orderState",String.valueOf(order.getOrderState()));//订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    		map.put("orderStateName",order.buildOrderStateName());//订单状态名称：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
//    		map.put("proxyOrderNo",order.getProxyOrderNo());//订单编号
    		map.put("applyFullName",order.getApplyFullName());//申请人姓名
    		map.put("applyPhone",order.getApplyPhone());//申请人手机号
    		map.put("proxyProductId",String.valueOf(order.getProxyProductId()));//申请开通服务产品ID
    		map.put("proxyProductName",order.getProxyProductName());//申请开通服务产品名称
    		map.put("proxyProductCount",String.valueOf(order.getProxyProductCount()));//申请开通服务数量
    		map.put("proxyUserId",String.valueOf(order.getProxyUserId()));//代理商ID
    		map.put("proxyUserName",String.valueOf(order.getProxyUserName()));//代理商名称
    		map.put("proxyUserNo",order.getProxyUserNo());//代理商编号
    		map.put("updateTime",DateUtil.parseLongTime2Str(order.getUpdateTime()));//更新时间
    		map.put("createTime",DateUtil.parseLongTime2Str(order.getCreateTime()));//创建时间
    		
//    		long startTime = System.currentTimeMillis();
//    		long proxyProductId = order.getProxyProductId();
//    		ProxyProduct proxyProduct = proxyProductService.getProxyProduct(proxyProductId);
//    		long productCloseTime = proxyProduct.buildProductCloseTime(startTime,order.getProxyProductCount());
//    		map.put("productCloseTime",DateUtil.parseLongTime2Str(productCloseTime));//产品服务使用截止时间
			return jsonResponse.setSuccessful().setData(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取代理商品列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
   
    
  
	/**
     * 获取代理商品列表
     */
    @RequestMapping(value = "/getProxyOrderList")
    @ResponseBody 
    public Object getProxyProductList(
    		@RequestParam(value = "proxyUserNo",required = false,defaultValue = "") String proxyUserNo,//代理商编号
    		@RequestParam(value = "proxyUserName",required = false,defaultValue = "") String proxyUserName,//代理商名称
    		@RequestParam(value = "applyFullName",required = false,defaultValue = "") String applyFullName,//申请人姓名
    		@RequestParam(value = "applyPhone",required = false,defaultValue = "") String applyPhone,//申请人手机号
    		@RequestParam(value = "orderState",required = false,defaultValue = "-1") int orderState//订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    		){
    	JsonResponse jsonResponse = new JsonResponse();
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
    	logger.info("赵兴林获取代理商品列表,proxyUserNo:"+proxyUserNo+",proxyUserName:"+proxyUserName
    			+",applyFullName:"+applyFullName+",applyPhone:"+applyPhone+",orderState:"+orderState);
    	logger.info("赵兴林获取代理商品列表,proxyUserNo:"+proxyUserNo+",proxyUserName:"+proxyUserName
    			+",applyFullName:"+applyFullName+",applyPhone:"+applyPhone+",orderState:"+orderState);
    	logger.info("赵兴林获取代理商品列表,proxyUserNo:"+proxyUserNo+",proxyUserName:"+proxyUserName
    			+",applyFullName:"+applyFullName+",applyPhone:"+applyPhone+",orderState:"+orderState);
    	logger.info("赵兴林获取代理商品列表,proxyUserNo:"+proxyUserNo+",proxyUserName:"+proxyUserName
    			+",applyFullName:"+applyFullName+",applyPhone:"+applyPhone+",orderState:"+orderState);
		try {
			List<ProxyOrder> proxyOrderList  = proxyOrderService.getProxyOrderList(proxyUserNo,proxyUserName,applyFullName,applyPhone,orderState,page);
			page.setRecords(buildProxyOrderMapList(proxyOrderList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取代理商品列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    
   
    
    private List<Map<String, String>> buildProxyOrderMapList(List<ProxyOrder> proxyOrderList) {
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	for(ProxyOrder order: proxyOrderList){
    		Map<String,String> map = new HashMap<String,String>();
    		long orderId = order.getId();
    		map.put("orderId", String.valueOf(orderId));//代理订单ID
    		map.put("orderState",String.valueOf(order.getOrderState()));//订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    		map.put("orderStateName",order.buildOrderStateName());//订单状态名称：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    		map.put("applyFullName",order.getApplyFullName());//申请人姓名
    		map.put("applyPhone",order.getApplyPhone());//申请人手机号
    		map.put("proxyProductId",String.valueOf(order.getProxyProductId()));//申请开通服务产品ID
    		map.put("proxyProductName",order.getProxyProductName());//
    		map.put("proxyProductCount",String.valueOf(order.getProxyProductCount()));//申请开通服务数量
    		map.put("proxyUserId",String.valueOf(order.getProxyUserId()));//代理商ID
    		map.put("proxyUserName",String.valueOf(order.getProxyUserName()));//代理商名称
    		map.put("proxyUserNo",order.getProxyUserNo());//代理商编号
    		map.put("updateTime",DateUtil.parseLongTime2Str(order.getUpdateTime()));//更新时间
    		map.put("createTime",DateUtil.parseLongTime2Str(order.getCreateTime()));//创建时间
    		String phone = order.getApplyPhone();
    		
    		long proxyProductId = order.getProxyProductId();
    		ProxyProduct proxyProduct = proxyProductService.getProxyProduct(proxyProductId);
    		if(proxyProduct == null){
    			logger.info("未找到代理产品,proxyProductId:"+proxyProductId);
    			throw new RuntimeException("未找到代理产品");
    		}
    		
    		StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessByPhone(phone);
    		
    		
    		
    		if(storeBusiness == null){
    			long productCloseTime = proxyProduct.buildProductCloseTime(order.getProxyProductCount(),0,0);
        		map.put("productCloseTime",DateUtil.parseLongTime2Str(productCloseTime));//产品服务使用截止时间
    		}else{
    			long wxaCloseTime = storeBusiness.getWxaCloseTime();
        		long wxaOpenTime = storeBusiness.getWxaOpenTime();
    			long productCloseTime = proxyProduct.buildProductCloseTime(order.getProxyProductCount(),wxaCloseTime,wxaOpenTime);
        		map.put("productCloseTime",DateUtil.parseLongTime2Str(productCloseTime));//产品服务使用截止时间
        		
    		}
    		
    		
    		
			list.add(map);
    	}
		return list;
	}

    
    /**
     * 跳转到所有代理销售订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "allAgentSalesOrder.html";
    }

    /**
     * 跳转到添加所有代理销售订单
     */
    @RequestMapping("/allAgentSalesOrder_add")
    public String allAgentSalesOrderAdd() {
        return PREFIX + "allAgentSalesOrder_add.html";
    }

    /**
     * 跳转到销售订单详情
     */
    @RequestMapping("/allAgentSalesOrder_detail")
    public String allAgentSalesOrderDetail() {
        return PREFIX + "allAgentSalesOrder_detail.html";
    }

    /**
     * 获取所有代理销售订单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增所有代理销售订单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除所有代理销售订单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改所有代理销售订单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 所有代理销售订单详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
