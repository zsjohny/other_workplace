package com.jiuy.supplier.modular.authorityManage.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.ISupplierCustomerGroupService;
import com.jiuyuan.service.common.IUserNewService;
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
 * 商品权限控制器
 *
 * @author fengshuonan
 * @Date 2018-03-13 10:34:58
 */
@Controller
@RequestMapping("/productAuthority")
public class ProductAuthorityController extends BaseController {

	private String PREFIX = "/authorityManage/productAuthority/";
	private static final Log logger = LogFactory.get(ProductAuthorityController.class);
	@Autowired
	private UserNewMapper supplierUserMapper;

	@Autowired
	private ISupplierCustomerGroupService supplierCustomerGroupService;

	/**
	 * 跳转到商品权限首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "productAuthority.html";
	}

	/**
	 * 跳转到添加商品权限
	 */
	@RequestMapping("/productAuthority_add")
	public String productAuthorityAdd() {
		return PREFIX + "productAuthority_add.html";
	}

	/**
	 * 跳转到修改商品权限
	 */
	@RequestMapping("/productAuthority_update/{productAuthorityId}")
	public String productAuthorityUpdate(@PathVariable Integer productAuthorityId, Model model) {
		return PREFIX + "productAuthority_edit.html";
	}

	/**
	 * 获取商品权限列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(String condition) {
		return null;
	}

	/**
	 * 新增商品权限
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add() {
		return super.SUCCESS_TIP;
	}

	/**
	 * 删除商品权限
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() {
		return SUCCESS_TIP;
	}

	/**
	 * 修改商品权限
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public JsonResponse update() {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			UserNew supplier = supplierUserMapper.selectById(userId);
			// 商品权限
			String productPermission = supplier.getProductPermission();
			// 分组列表
			List<Map<String, Object>> groupList = supplierCustomerGroupService.getGroupList(userId);
			// 组装分组权限数据
			Map<String, Object> buildPermissionAndGroup = buildPermissionAndGroup(productPermission, groupList);
//			buildPermissionAndGroup.put("groupList", groupList);
			return jsonResponse.setSuccessful().setData(buildPermissionAndGroup);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("修改商品权限信息失败！");
		}
	}

	/**
	 * 修改商品权限保存
	 */
	@RequestMapping(value = "/updateSave")
	@ResponseBody
	public Object updateSave(@RequestParam(value = "permission", required = true) String permission) {
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			UserNew supplier = new UserNew();
			supplier.setId(userId);
			supplier.setProductPermission(permission);
			supplierUserMapper.updateById(supplier);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("修改商品权限信息失败！");
		}

	}

	/**
	 * 商品权限详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail() {
		return null;
	}

	/**
	 * 组装分组权限数据
	 * 
	 * @param productPermission
	 * @param groupList
	 * @return
	 */
	private Map<String, Object> buildPermissionAndGroup(String productPermission, List<Map<String, Object>> groupList) {
		Map<String, Object> map = new HashMap<>();
		if ("0".equals(productPermission) || "-1".equals(productPermission)) {// 供应商商品权限为公开
			map.put("productPermission", productPermission);
		} else {
			map.put("productPermission", 2);// 部分客户可见
			String[] productPermissionArr = productPermission.split(",");
			for (Map<String, Object> group : groupList) {
				group.put("choosed", 0);
				for (String string : productPermissionArr) {
					if (string.equals(group.get("id").toString())) {
						group.put("choosed", 1);
					}
				}
			}
		}
		map.put("groupList", groupList);
		return map;
	}
}
