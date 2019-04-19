package com.jiuy.web.controller.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.service.member.StoreAuthService;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.store.StoreAuth;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/shop")
// @Login
public class ShopBusinessController {

	private static final Logger logger = LoggerFactory.getLogger(ShopBusinessController.class);

	@Resource
	private StoreAuthService storeAuthService;

	/**
	 * 添加预设拒绝原因
	 * 
	 * @return type 拒绝原因类型：0：门店认证1：客服商品审核2：买手商品审核
	 */
	@RequestMapping(value = "/setPreinstallNoPassReason")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setPreinstallNoPassReason(@RequestParam("authReason") String authReason,
			@RequestParam("type") int type) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			int ret = storeAuthService.setPreinstallNoPassReason(authReason, type);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("失败");
		}
		return jsonResponse.setSuccessful();
	}

	/**
	 * 删除预设拒绝原因
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delPreinstallNoPassReason")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse delPreinstallNoPassReason(@RequestParam("authReasonId") long authReasonId) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			int ret = storeAuthService.delPreinstallNoPassReason(authReasonId);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("失败");
		}
		return jsonResponse.setSuccessful();
	}

	/**
	 * 获取预设拒绝原因列表
	 * 
	 * @return type 拒绝原因类型：0：门店认证1：客服商品审核2：买手商品审核 -1:全部
	 */
	@RequestMapping(value = "/getPreinstallNoPassReasonList")
	@ResponseBody
	public JsonResponse getPreinstallNoPassReasonList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "type", required = false, defaultValue = "-1") Integer type) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			PageQuery pageQuery = new PageQuery(page, pageSize);
			PageQueryResult pageQueryResult = new PageQueryResult();
			BeanUtils.copyProperties(pageQuery, pageQueryResult);

			List<Map<String, String>> preinstallNoPassReasonList = storeAuthService
					.getPreinstallNoPassReasonList(pageQuery, type);

			data.put("preinstallNoPassReasonList", preinstallNoPassReasonList);

			int count = storeAuthService.getPreinstallNoPassReasonListCount();
			pageQueryResult.setRecordCount(count);
			data.put("total", pageQueryResult);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("失败");
		}
		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 获取新门店审核列表 http://dev.yujiejie.com:32080/shop/getAuthList.json?authState=1
	 * 
	 * @param authState
	 *            认证审核状态：0未提交审核、1审核中、2审核通过、3审核不通过
	 * @param page
	 * @param pageSize
	 *            authType 认证类型：-1全部 0线下门店有营业执照、1线下门店无营业执照、2线上门店、3其他
	 * @return
	 */
	@RequestMapping(value = "/getAuthList")
	@ResponseBody
	public JsonResponse getAuthList(@RequestParam("authState") Integer authState,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(required = false, defaultValue = "-1") Integer authType,
			@RequestParam(required = false, defaultValue = "") String keyWord) {

		// int count =
		// storeAuthService.getAuthListCount(authState,authType,keyWord);

		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		List<StoreAuth> storeAuthList = storeAuthService.getAuthList(authState, pageQuery, authType, keyWord);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("storeAuthList", storeAuthList);

		int count = storeAuthService.getAuthListCount(authState, authType, keyWord);
		pageQueryResult.setRecordCount(count);
		data.put("total", pageQueryResult);

		// Map<String,String> pageInfo = new HashMap<String,String>();
		// pageInfo.put("current",String.valueOf(pageQuery.getPage()));
		// pageInfo.put("size",String.valueOf(pageQuery.getPageSize()));
		// data.put("page", pageInfo);
		// data.put("count", count+"");
		JsonResponse jsonResponse = new JsonResponse();
		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 审核通过
	 * 
	 * @return
	 */
	@RequestMapping(value = "/setAuthPass")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setAuthPass(@RequestParam("storeAuthId") long storeAuthId) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			storeAuthService.setAuthPass(storeAuthId);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("设置审核通过失败");
		}
		return jsonResponse.setSuccessful();
	}

	/**
	 * 审核不通过
	 * 
	 * @return
	 */
	@RequestMapping(value = "/setAuthNoPass")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setAuthNoPass(@RequestParam("storeAuthId") long storeAuthId, String noPassReason) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			storeAuthService.setAuthNoPass(storeAuthId, noPassReason);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("设置审核通过失败");
		}
		return jsonResponse.setSuccessful();
	}

}
