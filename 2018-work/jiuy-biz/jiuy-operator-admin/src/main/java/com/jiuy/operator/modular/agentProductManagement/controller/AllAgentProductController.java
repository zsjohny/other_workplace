package com.jiuy.operator.modular.agentProductManagement.controller;

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
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuy.operator.core.shiro.ShiroUser;
import com.jiuyuan.entity.newentity.ProxyProduct;
import com.jiuyuan.service.common.IProxyProductService;
import com.jiuyuan.service.common.IProxyUserService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 所有代理产品控制器
 * @author fengshuonan
 * @Date 2018-04-03 11:15:23
 */
@Controller
@RequestMapping("/AllAgentProduct")
public class AllAgentProductController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(AllAgentProductController.class);
	  
    private String PREFIX = "/agentProductManagement/AllAgentProduct/";

    
    @Autowired
    private IProxyProductService proxyProductService;
    
    @Autowired
    private IProxyUserService proxyUserService;
    
    
    
    
    
    
    /**
     * 查看代理商品
     */
    @RequestMapping(value = "/showProxyProduct")
    @ResponseBody 
    public JsonResponse showProxyProduct(
    		@RequestParam(value = "proxyProductId",required = true) long proxyProductId//代理产品ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			ProxyProduct proxyProduct  = proxyProductService.getProxyProduct(proxyProductId);
			Map<String,String> map = new HashMap<String,String>();
			map.put("proxyProductId", String.valueOf(proxyProduct.getId()));//代理产品ID
			map.put("name", proxyProduct.getName());//代理产品名称
			map.put("singleUseLimitDay", String.valueOf(proxyProduct.getSingleUseLimitDay()));//单位使用限制天数
			map.put("renewProtectDay", String.valueOf(proxyProduct.getRenewProtectDay()));//续约保护天数
			map.put("price", String.valueOf(proxyProduct.getPrice()));//市场单价
			map.put("note", proxyProduct.getNote());//产品备注
			return jsonResponse.setSuccessful().setData(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取代理商品列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    
    /**
     * 获取代理商品列表（由于编辑代理商选择代理商品）
     */
    @RequestMapping(value = "/getAllProxyProduct")
    @ResponseBody 
    public JsonResponse getAllProxyProduct(){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			List<ProxyProduct> proxyProductList  = proxyProductService.getProxyProductList();
			List<Map<String,String>> data = new ArrayList<Map<String,String>>();
			for(ProxyProduct proxyProduct : proxyProductList){
				Map<String,String> map = new HashMap<String,String>();
				map.put("proxyProductId", String.valueOf(proxyProduct.getId()));//代理产品ID
				map.put("proxyProductName", proxyProduct.getName());//代理产品名称
				data.add(map);
			}
			return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取代理商品列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    /**
     * 修改代理商品
     */
    @RequestMapping(value = "/updProxyProduct")
    @ResponseBody 
    public JsonResponse updProxyProduct(
    		@RequestParam(value = "proxyProductId",required = true) long proxyProductId,//代理产品ID
    		@RequestParam(value = "name",required = true) String name,//产品名称
    		@RequestParam(value = "singleUseLimitDay",required = true) int singleUseLimitDay,//单位使用限制天数
    		@RequestParam(value = "renewProtectDay",required = true) int renewProtectDay,//续约保护天数
    		@RequestParam(value = "price",required = true) double price,//市场单价
    		@RequestParam(value = "note",required = false,defaultValue = "") String note//产品备注
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			int receiveProxyUserCount = proxyUserService.getReceiveProxyUserCount(proxyProductId);
			if(receiveProxyUserCount > 0){
				throw new RuntimeException("有代理商代理了该商品不能修改");
			}
			proxyProductService.updProxyProduct(proxyProductId,name,singleUseLimitDay,renewProtectDay,price,note);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("修改代理商品:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    /**
     * 添加代理商品
     */
    @RequestMapping(value = "/addProxyProduct")
    @ResponseBody 
    public JsonResponse addProxyProduct(
    		@RequestParam(value = "name",required = true) String name,//产品名称
    		@RequestParam(value = "singleUseLimitDay",required = true) int singleUseLimitDay,//单位使用限制天数
    		@RequestParam(value = "renewProtectDay",required = true) int renewProtectDay,//续约保护天数
    		@RequestParam(value = "price",required = true) double price,//市场单价
    		@RequestParam(value = "note",required = false,defaultValue = "") String note//产品备注
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			proxyProductService.addProxyProduct(name,singleUseLimitDay,renewProtectDay,price,note);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("添加代理商品:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    
    /**
     * 获取代理商品列表
     */
    @RequestMapping(value = "/getProxyProductList")
    @ResponseBody 
    public Object getProxyProductList(
    		@RequestParam(value = "name",required = false,defaultValue = "") String name,//产品名称
    		@RequestParam(value = "note",required = false,defaultValue = "") String note//备注
    		){
    	JsonResponse jsonResponse = new JsonResponse();
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			List<ProxyProduct> proxyProductList  = proxyProductService.getProxyProductList(name,note,page);
			page.setRecords(buildProxyProductMapList(proxyProductList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取代理商品列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    
    

    private List<Map<String, String>> buildProxyProductMapList(List<ProxyProduct> proxyProductList) {
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	for(ProxyProduct proxyProduct: proxyProductList){
    		Map<String,String> map = new HashMap<String,String>();
    		long proxyProductId = proxyProduct.getId();
    		map.put("proxyProductId", String.valueOf(proxyProductId));//代理产品ID
    		map.put("name", proxyProduct.getName());//代理产品名称
    		int proxyProductTotalCount = proxyUserService.getProxyProductTotalCount(proxyProductId);
    		map.put("proxyProductTotalCount", String.valueOf(proxyProductTotalCount));//可售总库存量(代理总库存量（个）)
    		map.put("singleUseLimitDay", String.valueOf(proxyProduct.getSingleUseLimitDay()));//单位使用限制天数
    		map.put("renewProtectDay", String.valueOf(proxyProduct.getRenewProtectDay()));//续约保护天数
    		map.put("price", String.valueOf(proxyProduct.getPrice()));//市场单价
    		map.put("note", proxyProduct.getNote());//产品备注
    		map.put("createTime",DateUtil.parseLongTime2Str(proxyProduct.getCreateTime()));//创建时间
    		int receiveProxyUserCount = proxyUserService.getReceiveProxyUserCount(proxyProductId);
    		map.put("receiveProxyUserCount",String.valueOf(receiveProxyUserCount));//接受代理用户数量（用于显示修改按钮）大于0则不显示修改按钮
    		list.add(map);
    	}
		return list;
	}




    






	/**
     * 跳转到所有代理产品首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "AllAgentProduct.html";
    }

    /**
     * 跳转到添加所有代理产品
     */
    @RequestMapping("/AllAgentProduct_add")
    public String AllAgentProductAdd() {
        return PREFIX + "AllAgentProduct_add.html";
    }
    /**
     * 跳转到添加所有代理产品
     */
    @RequestMapping("/AllAgentProduct_edit")
    public String AllAgentProductEdit() {
        return PREFIX + "AllAgentProduct_edit.html";
    }
    /**
     * 跳转到添加所有代理产品
     */
    @RequestMapping("/AllAgentProduct_detail")
    public String AllAgentProductDetail() {
        return PREFIX + "AllAgentProduct_detail.html";
    }
    /**
     * 获取所有代理产品列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增所有代理产品
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除所有代理产品
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改所有代理产品
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 所有代理产品详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
