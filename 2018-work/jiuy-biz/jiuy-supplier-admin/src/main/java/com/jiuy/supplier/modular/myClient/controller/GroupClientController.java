package com.jiuy.supplier.modular.myClient.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.SupplierCustomerMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.SupplierCustomerGroup;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.ISupplierCustomer;
import com.jiuyuan.service.common.ISupplierCustomerGroupService;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 分组管理控制器
 *
 * @author fengshuonan
 * @Date 2018-03-13 10:17:15
 */
@Controller
@RequestMapping("/groupClient")
public class GroupClientController extends BaseController {
	private static final Log logger = LogFactory.get(GroupClientController.class);
	private String PREFIX = "/myClient/groupClient/";

	@Autowired
	private ISupplierCustomerGroupService supplierCustomerGroupService;
	@Autowired
	private UserNewMapper supplierUserMapper;
	@Autowired
	private SupplierCustomerMapper supplierCustomerMapper;
	@Autowired
	private ISupplierCustomer supplierCustomerService;

	/**
	 * 跳转到分组管理首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "groupClient.html";
	}

	/**
	 * 跳转到添加分组管理
	 */
	@RequestMapping("/groupClient_add")
	public String groupClientAdd() {
		return PREFIX + "groupClient_add.html";
	}

	/**
	 * 跳转到修改分组管理
	 */
	@RequestMapping("/groupClient_update/{groupClientId}")
	public String groupClientUpdate(@PathVariable Integer groupClientId, Model model) {
		return PREFIX + "groupClient_edit.html";
	}

	/**
	 * 获取分组管理列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(@RequestParam(value = "groupName", required = false, defaultValue = "") String groupName,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			List<Map<String, Object>> supplierCustomerGroupList = supplierCustomerGroupService.selectList(supplierId,
					groupName, remark, page);
			page.setRecords(supplierCustomerGroupList);
			return super.packForBT(page);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("获取分组列表失败！");
		}
	}

	/**
	 * 新增分组管理
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public JsonResponse add(@RequestParam(value = "groupName", required = true) String groupName,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			boolean result = supplierCustomerGroupService.validateGroupName(groupName,supplierId,0);
			if (result) {
				SupplierCustomerGroup customerGroup = new SupplierCustomerGroup();
				customerGroup.setCreateTime(System.currentTimeMillis());
				customerGroup.setUpdateTime(System.currentTimeMillis());
				customerGroup.setGroupName(groupName);
				customerGroup.setRemark(remark);
				customerGroup.setSupplierId(supplierId);
				supplierCustomerGroupService.insert(customerGroup);
				return jsonResponse.setSuccessful().setData("ok");
			}else{
				return jsonResponse.setError("创建失败！已有分组"+groupName+"，请换一个分组名称请重新输入");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("添加分组失败！");
		}
	}

	/**
	 * 删除分组管理
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public JsonResponse delete(@RequestParam(value = "groupId", required = true) long groupId) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			SupplierCustomerGroup customerGroup = new SupplierCustomerGroup();
			int count = supplierCustomerGroupService.selectCustomerCount(groupId, supplierId);
			if (count > 0) {
				return jsonResponse.setError("该分组下有用户！不能删除");
			}
			customerGroup.setId(groupId);
			customerGroup.setStatus(-1);
			customerGroup.setUpdateTime(System.currentTimeMillis());
			supplierCustomerGroupService.update(customerGroup);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("删除分组失败！");
		}
	}

	/**
	 * 修改分组管理
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(@RequestParam(value = "groupId", required = true) long groupId) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			SupplierCustomerGroup customerGroup = supplierCustomerGroupService.selectById(groupId);
			if (customerGroup == null) {
				return jsonResponse.setError("分组不存在！");
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("groupId", customerGroup.getId());
				map.put("groupName", customerGroup.getGroupName());
				map.put("remark", customerGroup.getRemark());
				return jsonResponse.setSuccessful().setData(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("删除分组失败！");
		}
	}

	/**
	 * 修改分组管理
	 */
	@RequestMapping(value = "/updateSave")
	@ResponseBody
	public Object updateSave(@RequestParam(value = "groupId", required = true) long groupId,
			@RequestParam(value = "groupName", required = false, defaultValue = "") String groupName,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			boolean result = supplierCustomerGroupService.validateGroupName(groupName,supplierId,groupId);
			if(result){
				SupplierCustomerGroup customerGroup = supplierCustomerGroupService.selectById(groupId);
				if (customerGroup.getSupplierId() != supplierId) {
					return jsonResponse.setError("无修改权限！");
				} else {
					SupplierCustomerGroup newCustomerGroup = new SupplierCustomerGroup();
					newCustomerGroup.setId(groupId);
					if (StringUtils.isNotEmpty(remark)) {
						newCustomerGroup.setRemark(remark);
					}
					if (StringUtils.isNotEmpty(groupName)) {
						newCustomerGroup.setGroupName(groupName);
					}
					supplierCustomerGroupService.update(newCustomerGroup);
					return jsonResponse.setSuccessful().setData("ok");
				}
			}else{
				return jsonResponse.setError("创建失败！已有分组"+groupName+"，请换一个分组名称请重新输入");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("删除分组失败！");
		}
	}

	/**
	 * 分组管理详情
	 */
	@RequestMapping(value = "/groupOptionList")
	@ResponseBody
	public JsonResponse groupOptionList() {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			// 获取分组列表
			List<Map<String, Object>> groupList = supplierCustomerGroupService.getGroupList(supplierId);
			return jsonResponse.setSuccessful().setData(groupList);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("获取分组下拉列表失败！");
		}
	}

	
	
	/**
	 * 分组管理详情
	 */
	@RequestMapping(value = "/getGroupAndCustomerCount")
	@ResponseBody
	public JsonResponse getGroupAndCustomerCount() {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long supplierId = shiroUser.getId();
		if (supplierId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			//分组数量
			int groupCount = supplierCustomerGroupService.selectCount(supplierId);
			//分组客户数量 不包含默认分组
			int groupCustomerCount = supplierCustomerGroupService.selectGroupCustomerCount(supplierId);
			Map<String,Object> map = new HashMap<>();
			map.put("groupCount", groupCount);
			map.put("groupCustomerCount", groupCustomerCount);
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("获取分组下拉列表失败！");
		}
	}
	
	/**
	 * 分组数据修复
	 */
	@RequestMapping(value = "/fix")
	@ResponseBody
	public JsonResponse fix() {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Wrapper<UserNew> wrapper = new EntityWrapper<>();
			wrapper.ne("status", 3);
			List<UserNew> userNews = supplierUserMapper.selectList(wrapper);
			for (UserNew userNew : userNews) {
				SupplierCustomerGroup customerGroup = new SupplierCustomerGroup();
				customerGroup.setCreateTime(System.currentTimeMillis());
				customerGroup.setUpdateTime(System.currentTimeMillis());
				customerGroup.setDefaultGroup(1);
				customerGroup.setGroupName("默认分组");
				customerGroup.setSupplierId(userNew.getId());
				supplierCustomerGroupService.insertAndGetId(customerGroup);
				Long id = customerGroup.getId();
				Wrapper<SupplierCustomer> wrapper1 = new EntityWrapper<>();
				wrapper1.eq("supplier_id", userNew.getId());
				List<SupplierCustomer> customers = supplierCustomerMapper.selectList(wrapper1);
				for (SupplierCustomer supplierCustomer : customers) {
					SupplierCustomer supplierCustomerNew = new SupplierCustomer();
					supplierCustomerNew.setGroupId(id);
					supplierCustomerNew.setId(supplierCustomer.getId());
					supplierCustomerService.update(supplierCustomerNew);
				}
			}

			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("添加分组失败！");
		}
	}

}
