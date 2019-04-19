package com.jiuy.operator.modular.afterSaleManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ShopStoreAuthReasonMapper;
import com.jiuyuan.entity.newentity.ShopStoreAuthReason;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

import java.util.ArrayList;
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
 * 退款原因管理控制器
 *
 * @author fengshuonan
 * @Date 2017-12-15 14:12:50
 */
@Controller
@RequestMapping("/fadeReasonManage")
@Login
public class FadeReasonManageController extends BaseController {

	private String PREFIX = "/afterSaleManage/fadeReasonManage/";

	@Autowired
	private ShopStoreAuthReasonMapper shopStoreAuthReasonMapper;

	/**
	 * 跳转到原因管理首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "fadeReasonManage.html";
	}

	/**
	 * 跳转到添加原因管理
	 */
	@RequestMapping("/fadeReasonManage_add")
	public String fadeReasonManageAdd() {
		return PREFIX + "fadeReasonManage_add.html";
	}

	/**
	 * 跳转到修改原因管理
	 */
	@RequestMapping("/fadeReasonManage_update")
	public String fadeReasonManageUpdate() {
		return PREFIX + "fadeReasonManage_edit.html";
	}

	/**
	 * 获取原因管理列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(@RequestParam(value = "reason", required = false, defaultValue = "") String reason, // 原因关键字
			@RequestParam(value = "type", required = false, defaultValue = "-1") int type // 0：门店认证1：商品认证2：售后拒绝原因3:仅退款 4：退货退款
	) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
			Wrapper<ShopStoreAuthReason> wrapper = new EntityWrapper<ShopStoreAuthReason>();
			if (type != -1) {
				wrapper.eq("type", type);
			}
			if (reason != "") {
				wrapper.like("no_pass_reason", reason);
			}
			wrapper.eq("is_delete", 0).orderBy("type,id", false);// 0:未删除 1：已删除
			// .orderBy("id", true);
			List<ShopStoreAuthReason> shopStoreAuthReasonList = shopStoreAuthReasonMapper.selectList(wrapper);
			if (shopStoreAuthReasonList.size() < 1) {
				return jsonResponse.setError("无原因数据！");
			}
			List<Map<String, Object>> list = new ArrayList<>();
			for (ShopStoreAuthReason shopStoreAuthReason : shopStoreAuthReasonList) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", shopStoreAuthReason.getId());
				map.put("type", shopStoreAuthReason.getType());
				map.put("weight", shopStoreAuthReason.getWeight());
				map.put("reason", shopStoreAuthReason.getNoPassReason());
				list.add(map);
			}
			return jsonResponse.setSuccessful().setData(list);
		} catch (Exception e) {
			return jsonResponse.setError("退款原因管理列表e:" + e.getMessage());
		}
	}

	/**
	 * 新增原因管理
	 * 
	 * @param reason
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam(value = "reason", required = true) String reason,
			@RequestParam(value = "type", required = true) int type,
			@RequestParam(value = "weight", required = true) int weight) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			ShopStoreAuthReason authReason = new ShopStoreAuthReason();
			authReason.setCreateTime(System.currentTimeMillis());
			authReason.setNoPassReason(reason);
			authReason.setType(type);
			authReason.setWeight(weight);
			authReason.setUpdateTime(System.currentTimeMillis());
			shopStoreAuthReasonMapper.insert(authReason);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("添加退款原因e:" + e.getMessage());
		}
	}

	/**
	 * 删除原因管理
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse delete(@RequestParam(value = "reasonId", required = true) long id) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			ShopStoreAuthReason shopStoreAuthReason = shopStoreAuthReasonMapper.selectById(id);
			if (shopStoreAuthReason == null) {
				return jsonResponse.setError("该原因不存在");
			}
			ShopStoreAuthReason newShopStoreAuthReason = new ShopStoreAuthReason();
			newShopStoreAuthReason.setId(id);
			;
			newShopStoreAuthReason.setIsDelete(1);// 1:删除 0:未删除
			newShopStoreAuthReason.setUpdateTime(System.currentTimeMillis());
			shopStoreAuthReasonMapper.deleteById(id);
			shopStoreAuthReasonMapper.updateById(newShopStoreAuthReason);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("该原因不存在e:" + e.getMessage());
		}

	}

	/**
	 * 修改原因管理
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	@AdminOperationLog
	public Object update(@RequestParam(value = "reasonId", required = true) long id) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			ShopStoreAuthReason shopStoreAuthReason = shopStoreAuthReasonMapper.selectById(id);
			if (shopStoreAuthReason == null) {
				return jsonResponse.setError("该原因不存在");
			}
			Map<String, Object> map = new HashMap<>();
			map.put("id", shopStoreAuthReason.getId());
			map.put("type", shopStoreAuthReason.getType());
			map.put("weight", shopStoreAuthReason.getWeight());
			map.put("reason", shopStoreAuthReason.getNoPassReason());
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError("该原因不存在e:" + e.getMessage());
		}

	}

	/**
	 * 原因修改保存
	 */
	@RequestMapping(value = "/saveUpdate")
	@ResponseBody
	@AdminOperationLog
	public Object saveUpdate(@RequestParam(value = "reasonId", required = true) long reasonId, // 原因id
			@RequestParam(value = "weight", required = false, defaultValue = "-1") int weight, // 权重
			@RequestParam(value = "reason", required = false, defaultValue = "") String reason, // 原因内容
			@RequestParam(value = "type", required = false, defaultValue = "-1") int type // 类型
	) {

		JsonResponse jsonResponse = new JsonResponse();
		try {
			ShopStoreAuthReason shopStoreAuthReason = shopStoreAuthReasonMapper.selectById(reasonId);
			if (shopStoreAuthReason == null) {
				return jsonResponse.setError("该原因不存在");
			}
			ShopStoreAuthReason newShopStoreAuthReason = new ShopStoreAuthReason();
			newShopStoreAuthReason.setId(reasonId);
			if (weight != -1) {
				newShopStoreAuthReason.setWeight(weight);
			}
			if (StringUtils.isNotEmpty(reason)) {
				newShopStoreAuthReason.setNoPassReason(reason);
			}
			if (type != -1) {
				newShopStoreAuthReason.setType(type);
			}
			newShopStoreAuthReason.setUpdateTime(System.currentTimeMillis());
			shopStoreAuthReasonMapper.updateById(newShopStoreAuthReason);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("该原因不存在e:" + e.getMessage());
		}
	}

}
