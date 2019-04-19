package com.jiuy.operator.modular.agentUserManagement.controller;

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
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.ProxyProduct;
import com.jiuyuan.entity.newentity.ProxyUser;
import com.jiuyuan.service.common.IProxyCustomerService;
import com.jiuyuan.service.common.IProxyProductService;
import com.jiuyuan.service.common.IProxyUserService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 代理商用户控制器
 *
 * @author fengshuonan
 * @Date 2018-04-03 11:18:43
 */
@Controller
@RequestMapping("/agencyUser")
public class AgencyUserController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(AgencyUserController.class);
	  
	   
    private String PREFIX = "/agentUserManagement/agencyUser/";
    
    @Autowired
    private IProxyProductService proxyProductService;

    @Autowired
    private IProxyUserService proxyUserService;
    

    @Autowired
    private IProxyCustomerService proxyCustomerService;
    
    
    /**
     * 查看代理商
     */
    @RequestMapping(value = "/showProxyUser")
    @ResponseBody 
    public JsonResponse showProxyUser(
    		@RequestParam(value = "proxyUserId",required = true) long proxyUserId//代理商ID
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			ProxyUser  proxyUser =proxyUserService.getProxyUser(proxyUserId);
			return jsonResponse.setSuccessful().setData(buildPproxyUserMap(proxyUser));
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("添加代理商品:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    private Map<String,String> buildPproxyUserMap(ProxyUser user) {
    	Map<String,String> map = new HashMap<String,String>();
    	long proxyUserId = user.getId();
		map.put("proxyUserId", String.valueOf(user.getId()));//代理商ID
		map.put("proxyUserNum", user.getProxyUserNum());//代理商编号
		map.put("proxyUserName", user.getProxyUserName());//代理商名称
		map.put("proxyUserFullName", user.getProxyUserFullName());//代理商姓名
		map.put("proxyUserPhone", user.getProxyUserPhone());//代理商手机号
		map.put("provinceCityCounty", user.getProvinceCityCounty());//所在省份城市县区
		map.put("province", user.getProvince());//所在省份
		map.put("city", user.getCity());//所在城市
		map.put("county", user.getCounty());//所在县区
		map.put("idCardNo", user.getIdCardNo());//身份证号码
		map.put("proxyState", String.valueOf(user.getStatus()));//代理商状态：1：启用 2：冻结 3：删除
		map.put("proxyStateName", user.buildProxyStateName());//代理商状态名称：0禁用、1启用
		int proxyCustomerCount = proxyCustomerService.getProxyCustomerCount(proxyUserId);
		map.put("proxyCustomerCount", String.valueOf(proxyCustomerCount));//客户数
		map.put("sellOutCount", String.valueOf(user.getSellOutCount()));//代理产品销售量
		map.put("historyTotalStockCount", String.valueOf(user.getHistoryTotalStockCount()));//历史总进货量
		
		long proxyProductId = user.getProxyProductId();//代理产品ID
		ProxyProduct proxyProduct = proxyProductService.getProxyProduct(proxyProductId);
		map.put("proxyProductId", String.valueOf(proxyProductId));//代理产品Id
		int stockCount = user.getStockCount();
		if(proxyProduct != null){
			map.put("proxyProductName", proxyProduct.getName());//代理产品名称
			Double price = proxyProduct.getPrice();
			map.put("totalAssets", String.valueOf(stockCount * price ));//账户总资产(产品库存量×产品市场单价)
		}else{
			map.put("proxyProductName", "");//代理产品名称
			map.put("totalAssets", "");//账户总资产(产品库存量×产品市场单价)
		}
		map.put("stockCount", String.valueOf(stockCount));//代理产品库存量
		
		
    	
		return map;
	}
    
    /**
     * 增加库存
     */
    @RequestMapping(value = "/incrStockCount")
    @ResponseBody 
    public JsonResponse incrStockCount(
    		@RequestParam(value = "proxyUserId",required = true) long proxyUserId,//代理商ID
    		@RequestParam(value = "incrStockCount",required = true) int incrStockCount//增加库存数
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			ShiroUser user = ShiroKit.getUser();
			long adminId = user.getId();
			proxyUserService.incrStockCount(proxyUserId,incrStockCount,adminId);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("增加库存:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }

	/**
     * 修改代理商
     */
    @RequestMapping(value = "/updProxyUser")
    @ResponseBody 
    public JsonResponse updProxyUser(
    		@RequestParam(value = "proxyUserId",required = true) long proxyUserId,//代理商ID
    		@RequestParam(value = "proxyUserName",required = true) String proxyUserName,//代理商名称
    		@RequestParam(value = "proxyUserFullName",required = true) String proxyUserFullName,//代理商姓名
    		@RequestParam(value = "proxyUserPhone",required = true) String proxyUserPhone,//代理商手机号
    		@RequestParam(value = "province",required = true) String province,//所在省份
    		@RequestParam(value = "city",required = true) String city,//所在城市
    		@RequestParam(value = "county",required = true) String county,//所在县区
    		@RequestParam(value = "idCardNo",required = true) String idCardNo,//身份证号码
    		@RequestParam(value = "proxyState",required = true) int proxyState//代理商状态：0禁用、1启用
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			proxyUserService.updProxyUser(proxyUserId,proxyUserName,proxyUserFullName,proxyUserPhone,province,city,county,idCardNo,proxyState);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("添加代理商品:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    /**
     * 添加代理商
     */
    @RequestMapping(value = "/addProxyUser")
    @ResponseBody 
    @AdminOperationLog
    public JsonResponse addProxyUser(
    		@RequestParam(value = "proxyUserName",required = true) String proxyUserName,//代理商名称
    		@RequestParam(value = "proxyUserFullName",required = true) String proxyUserFullName,//代理商姓名
    		@RequestParam(value = "proxyUserPhone",required = true) String proxyUserPhone,//代理商手机号
    		@RequestParam(value = "province",required = true) String province,//所在省份
    		@RequestParam(value = "city",required = true) String city,//所在城市
    		@RequestParam(value = "county",required = true) String county,//所在县区
    		@RequestParam(value = "idCardNo",required = true) String idCardNo,//身份证号码
    		@RequestParam(value = "proxyProductId",required = true) long proxyProductId,//代理产品ID
    		@RequestParam(value = "proxyState",required = true) int proxyState//代理商状态：0禁用、1启用
    		){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			proxyUserService.addProxyUser(proxyUserName,proxyUserFullName,proxyUserPhone,province,city,county,idCardNo,proxyProductId,proxyState);
			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("添加代理商品:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 获取代理商列表
     */
    @RequestMapping(value = "/getProxyUserList")
    @ResponseBody 
    public Object getProxyUserList(
    		@RequestParam(value = "proxyUserNum",required = false,defaultValue = "") String proxyUserNum,//代理商编号
    		@RequestParam(value = "proxyUserName",required = false,defaultValue = "") String proxyUserName,//代理商名称
    		@RequestParam(value = "proxyUserFullName",required = false,defaultValue = "") String proxyUserFullName,//代理商姓名
    		@RequestParam(value = "proxyUserPhone",required = false,defaultValue = "") String proxyUserPhone,//代理商手机号
    		@RequestParam(value = "idCardNo",required = false,defaultValue = "") String idCardNo,//身份证号码
    		@RequestParam(value = "proxyState",required = false,defaultValue = "-1") int proxyState//状态(1：启用 2：冻结 3：删除）
    		){
    	JsonResponse jsonResponse = new JsonResponse();
    	Page<Map<String,String>> page = new PageFactory<Map<String,String>>().defaultPage();
		try {
			
			List<ProxyUser> proxyUserList  = proxyUserService.getProxyUserList(proxyUserNum,proxyUserName,proxyUserFullName,proxyUserPhone,idCardNo,proxyState,page);
			page.setRecords(buildProxyUserListMapList(proxyUserList));
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取代理商品列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    

    private List<Map<String, String>> buildProxyUserListMapList(List<ProxyUser> proxyUserList) {
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	for(ProxyUser user: proxyUserList){
    		Map<String,String> map = new HashMap<String,String>();
    		long proxyUserId = user.getId();
    		map.put("proxyUserId", String.valueOf(user.getId()));//代理商ID
    		map.put("proxyUserNum", user.getProxyUserNum());//代理商编号
    		map.put("proxyUserName", user.getProxyUserName());//代理商名称
    		map.put("proxyUserFullName", user.getProxyUserFullName());//代理商姓名
    		map.put("proxyUserPhone", user.getProxyUserPhone());//代理商手机号
    		map.put("provinceCityCounty", user.getProvinceCityCounty());//所在省份城市县区
    		map.put("idCardNo", user.getIdCardNo());//身份证号码
    		map.put("proxyState", String.valueOf(user.getStatus()));//代理商状态：0禁用、1启用
    		map.put("proxyStateName", user.buildProxyStateName());//代理商状态名称：0禁用、1启用
    		int proxyCustomerCount = proxyCustomerService.getProxyCustomerCount(proxyUserId);
    		map.put("proxyCustomerCount", String.valueOf(proxyCustomerCount));//客户数
    		long proxyProductId = user.getProxyProductId();//代理产品ID
    		ProxyProduct proxyProduct = proxyProductService.getProxyProduct(proxyProductId);
    		if(proxyProduct != null){
    			map.put("proxyProductName", proxyProduct.getName());//代理产品名称
    		}else{
    			map.put("proxyProductName", "");//代理产品名称
    		}
    		map.put("stockCount", String.valueOf(user.getStockCount()));//代理产品库存量
    		map.put("sellOutCount", String.valueOf(user.getSellOutCount()));//代理产品销售量
    		list.add(map);
    	}
		return list;
	}

    /**
     * 获取代理用户统计
     */
    @RequestMapping("/getProxyUserStatistics")
    @ResponseBody
    public Object getProxyUserStatistics(
    		@RequestParam(value = "proxyUserNum",required = false,defaultValue = "") String proxyUserNum,//代理商编号
    		@RequestParam(value = "proxyUserName",required = false,defaultValue = "") String proxyUserName,//代理商名称
    		@RequestParam(value = "proxyUserFullName",required = false,defaultValue = "") String proxyUserFullName,//代理商姓名
    		@RequestParam(value = "proxyUserPhone",required = false,defaultValue = "") String proxyUserPhone,//代理商手机号
    		@RequestParam(value = "idCardNo",required = false,defaultValue = "") String idCardNo,//身份证号码
    		@RequestParam(value = "proxyState",required = false,defaultValue = "-1") int proxyState//状态(1：启用 2：冻结 3：删除）
    		){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		logger.info("代理商统计！");
			Map<String,Object> map = proxyUserService.getProxyUserStatistics(proxyUserNum, proxyUserName, proxyUserFullName, proxyUserPhone, idCardNo, proxyState);
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
     * 跳转到代理商用户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "agencyUser.html";
    }
    /**
     * 跳转到代理商用户详情
     */
    @RequestMapping("/agencyUser_detail")
    public String agencyUserDetail() {
        return PREFIX + "agencyUser_detail.html";
    }
    /**
     * 跳转到添加代理商用户
     */
    @RequestMapping("/agencyUser_add")
    public String agencyUserAdd() {
        return PREFIX + "agencyUser_add.html";
    }

    /**
     * 跳转到修改代理商用户
     */
    @RequestMapping("/agencyUser_edit")
    public String agencyUserEdit() {
        return PREFIX + "agencyUser_edit.html";
    }

    /**
     * 获取代理商用户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增代理商用户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除代理商用户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改代理商用户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 代理商用户详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
