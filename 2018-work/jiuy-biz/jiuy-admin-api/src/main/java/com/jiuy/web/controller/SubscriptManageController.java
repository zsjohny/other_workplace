package com.jiuy.web.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.SubscriptFacade;
import com.jiuy.core.service.SubscriptServiceImpl;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.subscript.SubscriptVO;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@RequestMapping("/subscriptinner")
@Controller
@Login
public class SubscriptManageController {

	@Autowired
	private SubscriptServiceImpl subscriptService;
	
	@Autowired
	private SubscriptFacade subscriptFacade;

	/**
	 * 搜索角标
	 * 
	 * @param name
	 * @param pageSize
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getSubscriptList(@RequestParam(value = "name", defaultValue = "", required = false) String name,
			@RequestParam(value = "description", defaultValue = "", required = false) String description,
			@RequestParam(value = "productId", defaultValue = "", required = false)Long productId,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {

		JsonResponse jsonResponse = new JsonResponse();
		HashMap<String, Object> data = new HashMap<String, Object>();
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);

		//搜索返回角标的信息和相关商品的id、个数
		List<SubscriptVO> subscriptVOs = subscriptFacade.search(name, description, productId,pageQuery);
		
		int count = subscriptService.searchCount(name, description,productId);
		pageQueryResult.setRecordCount(count);

		data.put("list", subscriptVOs);
		data.put("total", pageQueryResult);

		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 单个插入Subscript
	 * 
	 * @param subscriptName
	 * @param logo
	 * @param description
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addSubscript(@RequestParam(value = "subscript_name") String subscriptName,
			@RequestParam(value = "logo") String logo,
			@RequestParam(value = "description") String description
			) {
		JsonResponse jsonResponse = new JsonResponse();

		Subscript subscript = new Subscript(0, subscriptName, logo, 0, description, 0);
		try {
			subscriptService.addSubscript(subscript);
		} catch (Exception e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

	/**
	 * 修改对应id的Subscript
	 * 
	 * @param id
	 * @param subscriptName
	 * @param logo
	 * @param description
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateSubscript(@RequestParam(value = "id") long id,
			@RequestParam(value = "subscript_name") String subscriptName, @RequestParam(value = "logo") String logo,
			@RequestParam(value = "description") String description
			) {
		JsonResponse jsonResponse = new JsonResponse();

		Subscript subscript = new Subscript(id, subscriptName, logo, 0, description, 0);
		try {
			subscriptService.updateSubscript(subscript);
		} catch (Exception e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

	/**
	 * 
	 * @param ids
	 *            id是数据库自然升序产生的id
	 * @return
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse removeArray(@RequestParam(value = "ids") Long[] ids) {
		JsonResponse jsonResponse = new JsonResponse();

		Collection<Long> idList = Arrays.asList(ids);
		subscriptService.remove(idList);

		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse deleteArray(@RequestParam(value = "ids")Long[] ids){
		JsonResponse jsonResponse = new JsonResponse();
		
		Collection<Long> idList = Arrays.asList(ids);
		subscriptService.deleteByIds(idList);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	
	}
	
	/**
	 * 批量添加到对应商品
	 * @param subscriptId
	 * @param productIdsString 商品idsString 例如 "110,220,223"
	 * @return
	 */
	@RequestMapping(value = "/products/batch/add", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse batchAddProducts(@RequestParam(value = "id")long subscriptId,
			@RequestParam(value = "product_ids_string")String productIdsString){
		JsonResponse jsonResponse = new JsonResponse();
		
		subscriptFacade.batchUpdateSubscriptId(subscriptId,productIdsString);
		
		return jsonResponse.setSuccessful();
	}
	
	
	@RequestMapping(value = "/apply/product", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse applyToProduct(@RequestParam(value = "id")long subscriptId,
			@RequestParam(value = "top_cat_id", required = false) Long topCatId,
			@RequestParam(value = "sub_cat_id", required = false) Long subCatId,
			@RequestParam(value = "brand_id", required = false) Long brandId,
			@RequestParam(value = "top_tag_id", required = false) Long topTagId,
			@RequestParam(value = "sub_tag_id", required = false) Long subTagId){
		JsonResponse jsonResponse = new JsonResponse();
		
		subscriptFacade.applyToProduct(subscriptId, topCatId, subCatId, brandId, topTagId, subTagId);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
}
