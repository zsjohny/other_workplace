package com.jiuy.operator.modular.productManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.operator.BadgeMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.newentity.Badge;
import com.jiuyuan.service.common.IBadgeService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

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
 * 角标管理控制器
 *
 * @author fengshuonan
 * @Date 2018-01-23 09:45:02
 */
@Controller
@RequestMapping("/cornerMark")
@Login
public class CornerMarkController extends BaseController {

	@Autowired
	private BadgeMapper badgeMapper;
	@Autowired
	private IBadgeService badgeService;
	@Autowired
	private IProductNewService productNewService;
	private String PREFIX = "/productManage/cornerMark/";

	/**
	 * 跳转到角标管理首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "cornerMark.html";
	}

	/**
	 * 跳转到添加角标管理
	 */
	@RequestMapping("/cornerMark_add")
	public String cornerMarkAdd() {
		return PREFIX + "cornerMark_add.html";
	}

	/**
	   * 跳转到修改角标管理
	   */
	  @RequestMapping("/cornerMark_update")
	  public String cornerMarkUpdate() {
	    return PREFIX + "cornerMark_edit.html";
	  }
	/**
	 * 获取角标管理列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(@RequestParam(value = "badgeName", required = false, defaultValue = "") String badgeName) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
			Wrapper<Badge> wrapper = new EntityWrapper<Badge>();
			wrapper.eq("status", 0);
			if (StringUtils.isNotEmpty(badgeName)) {
				wrapper.like("badge_name", badgeName);
			}
			wrapper.orderBy("id", false);
			List<Map<String, Object>> selectMapsPage = badgeMapper.selectMapsPage(page, wrapper);
			for (Map<String, Object> map : selectMapsPage) {
				long badgeId = (long) map.get("id");
				// 根据角标id查询绑定该角标的商品数量
				int count = badgeService.getBindPruductCount(badgeId);
				map.put("productCount", count);
			}
			page.setRecords(selectMapsPage);
			return super.packForBT(page);
		} catch (Exception e) {
			return jsonResponse.setError("获取角标列表e：" + e.getMessage());
		}
	}

	/**
	 * 新增角标管理
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addBadge(@RequestParam(value = "badgeName", required = true) String badgeName,
			@RequestParam(value = "badgeImage", required = true) String badgeImage) {

		JsonResponse jsonResponse = new JsonResponse();
		try {
			Badge badge = new Badge();
			badge.setBadgeImage(badgeImage);
			badge.setBadgeName(badgeName);
			badge.setCreateTime(System.currentTimeMillis());
			badge.setUpdateTime(System.currentTimeMillis());
			badgeMapper.insert(badge);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("添加角标e:" + e.getMessage());
		}
	}

	/**
	 * 删除角标管理
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse deleteBadge(@RequestParam(value = "badgeId", required = true) long badgeId) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Badge badge = badgeMapper.selectById(badgeId);
			if (badge == null) {
				return jsonResponse.setError("角标不存在！");
			}
			// 判断角标id查询绑定该角标的商品数量
			if (badgeService.getBindPruductCount(badgeId) > 0) {
				return jsonResponse.setError("该角标下绑定了商品！");
			}
			Badge newBadge = new Badge();
			newBadge.setId(badgeId);
			newBadge.setStatus(-1);
			newBadge.setUpdateTime(System.currentTimeMillis());
			badgeMapper.updateById(newBadge);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("删除角标e:" + e.getMessage());
		}
	}

	/**
	 * 修改角标管理
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateBadge(@RequestParam(value = "badgeId", required = true) long badgeId) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Badge badge = badgeMapper.selectById(badgeId);
			if (badge == null) {
				return jsonResponse.setError("角标不存在！");
			}
			return jsonResponse.setSuccessful().setData(badge);
		} catch (Exception e) {
			return jsonResponse.setError("编辑角标e:" + e.getMessage());
		}
	}

	@RequestMapping("/saveUpdate")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse saveUpdateBadge(@RequestParam(value = "badgeId", required = true) long badgeId,
			@RequestParam(value = "badgeName", required = false, defaultValue = "") String badgeName,
			@RequestParam(value = "badgeImage", required = false, defaultValue = "") String badgeImage) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Badge badge = badgeMapper.selectById(badgeId);
			if (badge == null) {
				return jsonResponse.setError("角标不存在！");
			}
			Badge newBadge = new Badge();
			newBadge.setId(badgeId);
			if (StringUtils.isNotEmpty(badgeName)) {
				newBadge.setBadgeName(badgeName);
			}
			if (StringUtils.isNotEmpty(badgeImage)) {
				newBadge.setBadgeImage(badgeImage);
			}
			newBadge.setUpdateTime(System.currentTimeMillis());
			badgeMapper.updateById(newBadge);
			//修改绑定该角标的商品的角标图片和角标名称
			int count = badgeService.updateProductBadge(badgeId,badgeName,badgeImage);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("编辑角标保存e:" + e.getMessage());
		}
	}

	/**
	 * 角标管理详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail() {
		return null;
	}

	/**
	 * 删除指定商品角标
	 * 
	 * @param productIds
	 * @return
	 */
	@RequestMapping("/clearProductBadge")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse clearProductBadge(@RequestParam(value = "productIds", required = true) String productIds) {

		JsonResponse jsonResponse = new JsonResponse();
		String[] productIdsArr = productIds.split(",");
		try {
			// 清除角标并获取绑定了角标的商品数量(有效数量)
			int effectiveCount = badgeService.getBindBadgeProductCount(productIds);
			// 无效商品id数量
			int noEffectiveCount = productIdsArr.length - effectiveCount;
			Map<String, Object> map = new HashMap<>();
			map.put("effectiveCount", effectiveCount);
			map.put("noEffectiveCount", noEffectiveCount);
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError("清除指定商品id角标e:" + e.getMessage());
		}
	}
}
