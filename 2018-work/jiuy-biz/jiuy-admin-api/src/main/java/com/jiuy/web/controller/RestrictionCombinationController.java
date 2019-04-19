package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.ProductFacade;
import com.jiuy.core.business.facade.RestrictionCombinationFacade;
import com.jiuy.core.service.RestrictionCombinationService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.RestrictionCombinationVO;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;

import com.jiuyuan.web.help.JsonResponse;
/**
 * 限购组合
 * @author Jeff.Zhan
 *
 */
@RequestMapping("/restriction")
@Controller
@Login
public class RestrictionCombinationController {
	
	@Autowired
	private RestrictionCombinationFacade restrictionCombinationFacade;
	
	@Autowired
	private RestrictionCombinationService restrictionCombinationService;
	
	@Autowired
	private ProductFacade productFacade;
	
	@RequestMapping(value = "/search") 
	@ResponseBody
	public JsonResponse search(@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
		List<RestrictionCombinationVO> restrictionCombinationVOs = restrictionCombinationFacade.search(pageQuery, name);
		int count = restrictionCombinationFacade.searchCount(name);
		pageQueryResult.setRecordCount(count);
		
		data.put("list", restrictionCombinationVOs);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public JsonResponse add(@RequestParam(value = "name") String name, 
			@RequestParam(value = "description", required = false, defaultValue = "") String description,
			@RequestParam(value = "history_setting") int historySetting,
			@RequestParam(value = "history_buy", required = false) Integer historyBuy,
			@RequestParam(value = "history_cycle", required = false, defaultValue = "0") int historyCycle,
			@RequestParam(value = "history_start_time", required = false, defaultValue = "0") long historyStartTime,
			@RequestParam(value = "day_setting") int daySetting,
			@RequestParam(value = "day_buy", required = false) Integer dayBuy,
			@RequestParam(value = "day_start_time", required = false, defaultValue = "0") long dayStartTime) {

		JsonResponse jsonResponse = new JsonResponse();
		if(historySetting == 1 && historyBuy == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("历史限购有效下，限购数量不可以为空");
		}
		
		if(daySetting == 1 && dayBuy == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("当日限购有效下，限购数量不可以为空");
		}
		
		RestrictionCombination restrictionCombination = new RestrictionCombination(0, name, description, historySetting, historyBuy, historyCycle, historyStartTime, daySetting, dayBuy, dayStartTime);
		
		restrictionCombinationService.add(restrictionCombination);
		
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public JsonResponse update(@RequestParam(value = "id") long id,
			@RequestParam(value = "name") String name, 
			@RequestParam(value = "description", required = false, defaultValue = "") String description,
			@RequestParam(value = "history_setting") int historySetting,
			@RequestParam(value = "history_buy", required = false) Integer historyBuy,
			@RequestParam(value = "history_cycle", required = false, defaultValue = "0") int historyCycle,
			@RequestParam(value = "history_start_time", required = false, defaultValue = "0") long historyStartTime,
			@RequestParam(value = "day_setting") int daySetting,
			@RequestParam(value = "day_buy", required = false) Integer dayBuy,
			@RequestParam(value = "day_start_time", required = false, defaultValue = "0") long dayStartTime) {
		JsonResponse jsonResponse = new JsonResponse();
		if(historySetting == 1 && historyBuy == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("历史限购有效下，限购数量不可以为空");
		}
		
		if(daySetting == 1 && dayBuy == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("当日限购有效下，限购数量不可以为空");
		}
		
		RestrictionCombination restrictionCombination = new RestrictionCombination(id, name, description, historySetting, historyBuy, historyCycle, historyStartTime, daySetting, dayBuy, dayStartTime);
		
		restrictionCombinationService.update(restrictionCombination);
		
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 添加商品-加载
	 * @param id
	 * @return
	 */
    @RequestMapping(value = "/{id}/products", method=RequestMethod.GET)
    @ResponseBody
    public JsonResponse getCategoryById(@PathVariable("id") long id) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	String productsToString= restrictionCombinationFacade.clothesNosOfId(id);
    	
    	data.put("products_to_string", productsToString);
        data.put("total_count", StringUtils.equals("", productsToString) ? 0 : productsToString.split(",").length);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 添加商品-批量新增
     * @param restrictId
     * @param productIdsString 例:"220,221,222"
     * @return
     */
    @RequestMapping(value = "/products/batch/add")
    @ResponseBody
    public JsonResponse batchAddProducts(@RequestParam(value = "id") long restrictId, 
    		@RequestParam(value = "product_ids_string") String productIdsString) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	productFacade.batchUpdateRestrictId(restrictId, productIdsString);
    	
    	return jsonResponse.setSuccessful();
    }
}
