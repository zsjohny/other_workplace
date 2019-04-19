package com.jiuy.wxaproxy.modular.orderManagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.wxaproxy.core.shiro.ShiroKit;
import com.jiuy.wxaproxy.core.shiro.ShiroUser;
import com.jiuyuan.entity.newentity.ProxyOrder;
import com.jiuyuan.service.common.IProxyOrderService;
import com.jiuyuan.service.common.IProxyProductService;
import com.jiuyuan.service.common.IProxyUserService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 我的订单控制器
 */
@Controller
@RequestMapping("/myOrder")
public class MyOrderController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(MyOrderController.class);
	
    private String PREFIX = "/orderManagement/myOrder/";
    
    @Autowired
    private IProxyProductService proxyProductService;
    
    @Autowired
    private IProxyUserService proxyUserService;
    
    @Autowired
    private IProxyOrderService proxyOrderService;
    /**
     * 代理商搜索代理订单列表
     */
    @RequestMapping(value = "/searchProxyOrderList")
    @ResponseBody 
    public Object searchProxyOrderList(
    		@RequestParam(value = "applyFullName",required = false,defaultValue = "") String applyFullName,//申请人姓名
    		@RequestParam(value = "applyPhone",required = false,defaultValue = "") String applyPhone,//申请人手机号
    		@RequestParam(value = "orderState",required = false,defaultValue = "-1") int orderState,//订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    		@RequestParam(value = "proxyProductName",required = false,defaultValue = "") String proxyProductName//申请开通服务产品名称
    		){
    	JsonResponse jsonResponse = new JsonResponse();
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			ShiroUser shiroUser = ShiroKit.getUser();
			long proxyUserId = shiroUser.getId();
			List<ProxyOrder> orderList  = proxyOrderService.searchProxyProductList(proxyUserId,applyFullName,applyPhone,orderState,proxyProductName,page);
			page.setRecords(buildOrderMapList(orderList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取代理商品列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    private List<Map<String, String>> buildOrderMapList(List<ProxyOrder> orderList) {
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	for(ProxyOrder order: orderList){
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
    		list.add(map);
    	}
		return list;
	}
    
    /**
     * 跳转到我的订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "myOrder.html";
    }

    /**
     * 跳转到添加我的订单
     */
    @RequestMapping("/myOrder_add")
    public String myOrderAdd() {
        return PREFIX + "myOrder_add.html";
    }

    /**
     * 跳转到修改我的订单
     */
    @RequestMapping("/myOrder_update/{myOrderId}")
    public String myOrderUpdate(@PathVariable Integer myOrderId, Model model) {
        return PREFIX + "myOrder_edit.html";
    }

    /**
     * 获取我的订单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增我的订单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除我的订单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改我的订单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 我的订单详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
