package com.jiuy.supplier.modular.myClient.controller;

import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.SupplierCustomerGroup;
import com.jiuyuan.service.common.IOrderNewService;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.service.common.ISupplierCustomer;
import com.jiuyuan.service.common.ISupplierCustomerGroupService;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 我的客户控制器
 *
 * @author fengshuonan
 * @Date 2017-10-19 14:39:47
 */
@Controller
@RequestMapping("/allClient")
public class AllClientController extends BaseController {

	private String PREFIX = "/myClient/allClient/";

	private static final Log logger = LogFactory.get("供应商客户Controller");

	@Autowired
	private IOrderNewService supplierOrderService;
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
	@Autowired
	private ISupplierCustomerGroupService supplierCustomerGroupService;
	@Autowired
	private ISupplierCustomer supplierCustomerService;

	/**
	 * 跳转到我的客户首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "allClient.html";
	}

	/**
	 * 跳转到客户详情
	 */
	@RequestMapping("/allClient_detail")
	public String allClientDetail() {
		return PREFIX + "allClient_detail.html";
	}
	/**
	 * 跳转到新建我的客户
	 */
	@RequestMapping("/allClient_add")
	public String allClientAdd() {
		return PREFIX + "allClient_add.html";
	}
	/**
	 * 跳转到修改我的客户
	 */
	@RequestMapping("/allClient_update/{allClientId}")
	public String allClientUpdate(@PathVariable Integer allClientId, Model model) {
		return PREFIX + "allClient_edit.html";
	}

	/**
	 * 获取我的客户列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(String condition) {
		return null;
	}

	/**
	 * 新增我的客户
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add() {
		return super.SUCCESS_TIP;
	}

	/**
	 * 删除我的客户
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() {
		return SUCCESS_TIP;
	}

	/**
	 * 修改我的客户
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update() {
		return super.SUCCESS_TIP;
	}

	/**
	 * 我的客户详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail(@RequestParam(value = "storeId", required = true) long storeId) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			StoreBusiness storeBusiness = storeBusinessNewService.getById(storeId);
			if (storeBusiness == null) {
				return jsonResponse.setError("客户不存在storeId:" + storeId);
			}
			Map<String, Object> map = storeBusinessNewService.getStoreBusinessDetail(storeId, supplierId);
			if (map == null) {
				return jsonResponse.setError("无客户详情storeId:" + storeId);
			}
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("供应商查看客户详情:" + e.getMessage());
		}
	}

	/**
	 * 获取供应商客户列表
	 * 
	 * @return
	 */
	@RequestMapping("/getSupplierCustomerList")
	@ResponseBody
	public Object getSupplierCustomerList(
			@RequestParam(value = "businessName", required = false, defaultValue = "") String businessName,
			@RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
			@RequestParam(value = "moneyMin", required = false, defaultValue = "0") double moneyMin,
			@RequestParam(value = "moneyMax", required = false, defaultValue = "0") double moneyMax,
			@RequestParam(value = "countMin", required = false, defaultValue = "0") int countMin,
			@RequestParam(value = "countMax", required = false, defaultValue = "0") int countMax,
			@RequestParam(value = "province", required = false, defaultValue = "") String province,
			@RequestParam(value = "city", required = false, defaultValue = "") String city) {
		// @RequestParam(value="current",required=false,defaultValue="1")int
		// current,
		// @RequestParam(value="size",required=false,defaultValue="14")int size)
		// {
		Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			// 获取所有客户
			List<Map<String, Object>> supplierCustomerList = supplierOrderService.getSupplierCustomerList(userId,
					businessName, phoneNumber, moneyMin, moneyMax, countMin, countMax, province, city, page);
			page.setRecords(supplierCustomerList);
			// data.put("supplierOrderList", supplierOrderList);
			// System.out.println(selectList);
			return super.packForBT(page);
		} catch (Exception e) {
			logger.error("获取供应商列表:" + e.getMessage());
			throw new RuntimeException("获取供应商列表:" + e.getMessage());
		}
	}

	/**
	 * 获取供应商客户数量
	 * 
	 * @return
	 */
	@RequestMapping("/getSupplierCustomerCount")
	@ResponseBody
	public JsonResponse getSupplierCustomerCount() {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			// 获取客户总数，今日新增个数
			int supplierCustomerAllCount = supplierOrderService.getSupplierCustomerAllCount(userId);
			int supplierCustomerTodayNewCount = supplierOrderService.getSupplierCustomerTodayNewCount(userId);
			data.put("supplierCustomerAllCount", supplierCustomerAllCount);
			data.put("supplierCustomerTodayNewCount", supplierCustomerTodayNewCount);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取供应商列表:" + e.getMessage());
			throw new RuntimeException("获取供应商列表:" + e.getMessage());
		}
	}

	// v3.6 供应商客户列表####################################################

	/**
	 * 新建客户--下一步
	 * 
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse saveCustomer(
			@RequestParam(value = "customerId", required = false, defaultValue = "0") long customerId,
			@RequestParam(value = "remarkName", required = false, defaultValue = "") String remarkName,
			@RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
			@RequestParam(value = "groupId", required = true) long groupId) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			SupplierCustomer supplierCustomer1 = supplierCustomerService.getCustomerByPhone(phoneNumber,supplierId);
			SupplierCustomer supplierCustomer = new SupplierCustomer();
			supplierCustomer.setRemarkName(remarkName);
			supplierCustomer.setUpdateTime(System.currentTimeMillis());
			supplierCustomer.setGroupId(groupId);
			if(supplierCustomer1 != null){
				//新客户第二部，更新新客户信息
				supplierCustomer.setId(supplierCustomer1.getId());
				supplierCustomerService.update(supplierCustomer);
			}
			else {
				if(StringUtils.isEmpty(phoneNumber)){
					//如果是老客户或者第二次新客户就通过customerId搜索
					if(customerId == 0){
						logger.info("该客户不存在！customerId："+customerId+",phoneNumber:"+phoneNumber);
						return jsonResponse.setError("该客户不存在！");
					}
					supplierCustomer.setId(customerId);
					supplierCustomerService.update(supplierCustomer);
				}else{
					//新客户第二部，创建新客户信息
					supplierCustomer.setSupplierId(supplierId);
					if (StringUtils.isEmpty(phoneNumber)) {
						return jsonResponse.setError("手机号为空！");
					}
					supplierCustomer.setPhoneNumber(phoneNumber);
					supplierCustomer.setCreateTime(System.currentTimeMillis());
					supplierCustomer.setSupplierId(supplierId);
					supplierCustomerService.insert(supplierCustomer);
				}
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			logger.error("添加或更新用户信息:" + e.getMessage());
			return jsonResponse.setError("添加或更新用户信息失败！");
		}
	}

	/**
	 * 新建客户第一步
	 * 
	 * @param phoneNumber
	 * @return
	 */
	@RequestMapping("/getByPhone")
	@ResponseBody
	public JsonResponse getByPhone(@RequestParam(value = "phoneNumber", required = true) String phoneNumber) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerByPhone(phoneNumber, userId);
			Map<String, Object> map = new HashMap<>();
			// 获取分组列表
			List<Map<String, Object>> groupList = supplierCustomerGroupService.getGroupList(userId);
			// 组装分组数据
			List<Map<String, Object>> makedGroupList = makeGroup(groupList, supplierCustomer);

			if (supplierCustomer != null) {
			    Integer customerType = supplierCustomer.getCustomerType();
				if (customerType == 0) {
					map.put("remarkName", supplierCustomer.getRemarkName());
					map.put("groupList", makedGroupList);
					map.put("customerType", 0);//已经存在的新客户
					map.put("customerId", supplierCustomer.getId());
					map.put("hasCustomerDetail", 1);//是否有客户详情 0 没有  1 有
				} else {
					map = supplierCustomerService.getCustomerDetailById(supplierCustomer.getId());
					map.put("groupList", makedGroupList);
					map.put("phoneNumber", phoneNumber);
					map.put("customerType", 1);//老客户
					map.put("customerId", supplierCustomer.getId());
					map.put("hasCustomerDetail", 1);//是否有客户详情 0 没有  1 有
				}
				return jsonResponse.setSuccessful().setData(map);
			} else {
				map.put("phoneNumber", phoneNumber);
				map.put("customerType", 0);//新客户
				map.put("groupList", makedGroupList);
				map.put("hasCustomerDetail", 0);//是否有客户详情 0 没有  1 有
				return jsonResponse.setSuccessful().setData(map);
			}
			
		} catch (Exception e) {
			logger.error(" 新建客户第一步:" + e.getMessage());
			return jsonResponse.setError(" 新建客户第一步失败！");
		}
	}
	
	/**
	 * 客户详情
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping("/customerDetail")
	@ResponseBody
	public JsonResponse customerDetail(@RequestParam(value = "customerId", required = false,defaultValue = "-1") long customerId,
			@RequestParam(value = "storeId", required = false,defaultValue = "-1") long storeId) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		
		try {
			SupplierCustomer supplierCustomer =new SupplierCustomer();
			if (customerId != -1) {
				 supplierCustomer = supplierCustomerService.getCustomerById(customerId);
			}else if (storeId != -1) {
				supplierCustomer = supplierCustomerService.getCustomerByStoreId(storeId, userId);
			}
			// 获取分组列表
			List<Map<String, Object>> groupList = supplierCustomerGroupService.getGroupList(userId);
			// 组装分组数据
			List<Map<String, Object>> makedGroupList = makeGroup(groupList, supplierCustomer);
			
			Map<String, Object> map = supplierCustomerService.getCustomerDetailById(supplierCustomer.getId());
			
//			Long storeId1 = supplierCustomer.getStoreId();
			
			map.put("customerType", supplierCustomer.getCustomerType());
			
			map.put("groupList", makedGroupList);
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			logger.error("客户详情:" + e.getMessage());
			return jsonResponse.setError("获取客户详情失败！");
		}
	}

	/**
	 * 删除新客户
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping("/customerDelete")
	@ResponseBody
	public JsonResponse customerDelete(@RequestParam(value = "customerId", required = true) long customerId) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
				SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerById(customerId);
				if (supplierCustomer.getSupplierId() != userId) {
					return  jsonResponse.setError("权限不足，不能删除！");
				}
				SupplierCustomer newSupplierCustomer = new SupplierCustomer();
				newSupplierCustomer.setId(customerId);
				newSupplierCustomer.setStatus(-1);//删除
				supplierCustomerService.update(newSupplierCustomer);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			logger.error("删除新客户:" + e.getMessage());
			return jsonResponse.setError("删除新客户失败！");
		}
	}

	/**
	 * 查询客户列表新 v3.6
	 * 
	 * @param customerName
	 * @param remarkName
	 * @param businessName
	 * @param phoneNumber
	 * @param moneyMin
	 * @param moneyMax
	 * @param countMin
	 * @param countMax
	 * @param groupId
	 * @param province
	 * @param city
	 * @param orderType
	 * @param customerType
	 * @return
	 */
	@RequestMapping("/listNew")
	@ResponseBody
	public Object listNew(
			@RequestParam(value = "customerName", required = false, defaultValue = "") String customerName,
			@RequestParam(value = "remarkName", required = false, defaultValue = "") String remarkName,
			@RequestParam(value = "businessName", required = false, defaultValue = "") String businessName,
			@RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
			@RequestParam(value = "moneyMin", required = false, defaultValue = "0") double moneyMin,
			@RequestParam(value = "moneyMax", required = false, defaultValue = "0") double moneyMax,
			@RequestParam(value = "countMin", required = false, defaultValue = "0") int countMin,
			@RequestParam(value = "countMax", required = false, defaultValue = "0") int countMax,
			@RequestParam(value = "groupId", required = false, defaultValue = "-1") long groupId,
			@RequestParam(value = "province", required = false, defaultValue = "") String province,
			@RequestParam(value = "city", required = false, defaultValue = "") String city,
			@RequestParam(value = "orderType", required = false, defaultValue = "0") int orderType,
			@RequestParam(value = "customerType", required = false, defaultValue = "-1") int customerType) {
		Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);	
		}
		try {
			List<Map<String, Object>> supplierCustomerList = supplierCustomerService.getCustomerList(userId,
					customerName, remarkName, businessName, phoneNumber, moneyMin, moneyMax, countMin, countMax,
					groupId, province, city, orderType, customerType, page);
			for (Map<String, Object> map : supplierCustomerList) {
				long id = (long) map.get("groupId");
				SupplierCustomerGroup supplierCustomerGroup = supplierCustomerGroupService.getCustomerGroupById(id);
				map.put("groupName", supplierCustomerGroup.getGroupName());
			}
			page.setRecords(supplierCustomerList);
			return super.packForBT(page);
		} catch (Exception e) {
			logger.error("查询客户列表:" + e.getMessage());
			return jsonResponse.setError("查询客户列表！");
		}
	}

	/**
	 * 获取客户数
	 * 
	 * @return
	 */
	@RequestMapping("/newAndOldCustomerCount")
	@ResponseBody
	public JsonResponse customerCount() {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			Map<String, Object> map = supplierCustomerService.getNewAndOldCustomerCount(userId);
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			logger.error("获取客户数:" + e.getMessage());
			return jsonResponse.setError("获取客户数失败！");
		}
	}
	/**
	 * 组装分组数据
	 * @param groupList
	 * @param supplierCustomer
	 * @return
	 */
	private List<Map<String, Object>> makeGroup(List<Map<String, Object>> groupList,
			SupplierCustomer supplierCustomer) {
		// 组装分组数据
		if (supplierCustomer != null) {
			for (Map<String, Object> map2 : groupList) {
				map2.put("choosed", 0);
				Long id = (Long) map2.get("id");
				if (id.equals(supplierCustomer.getGroupId())) {
					map2.put("choosed", 1);
				}
			}
		} else {
			for (Map<String, Object> map2 : groupList) {
				map2.put("choosed", 0);
			}
		}
		return groupList;
	}

}
