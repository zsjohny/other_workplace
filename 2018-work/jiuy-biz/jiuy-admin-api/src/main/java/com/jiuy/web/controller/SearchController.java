package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.business.facade.GloableFacade;
import com.jiuy.core.dao.mapper.SearchDao;
import com.jiuy.core.meta.SearchKeyword;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.SearchMatchObject;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 搜索管理
 * @author Jeff.Zhan
 *
 */
@RequestMapping("/search")
@Controller
public class SearchController {
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private SearchDao searchDao;
	
	@Autowired
	private GloableFacade gloableFacade;
	
	/**
	 * 全局加载
	 * @return
	 */
	@RequestMapping("/global")
	@ResponseBody
	public JsonResponse getGlobalInfo() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();

		JSONObject searchTip = globalSettingService.getJsonObject(GlobalSettingName.SEARCH_TIP);
		JSONObject searchAd = globalSettingService.getJsonObject(GlobalSettingName.SEARCH_AD);
		JSONObject recommendedProduct = gloableFacade.getSearchRecommendedProduct(GlobalSettingName.SEARCH_RECOMMENDED_PRODUCT);
		
		data.put("search_tip", searchTip);
		data.put("search_ad", searchAd);
		data.put("search_recommended_product", recommendedProduct);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 关键词匹配对象下拉数据加载
	 * @return
	 */
	@RequestMapping("/match/objects")
	@ResponseBody
	public JsonResponse getMatchObjects() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();

		List<SearchMatchObject> searchMatchObjects = searchDao.loadMatchObject();
		data.put("searchMatchObjects", searchMatchObjects);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 权重配置
	 * @return
	 */
	@RequestMapping("/weight/setting")
	@ResponseBody
	public JsonResponse getWeightSetting() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();

		data.put("search_tip", globalSettingService.getJsonObject(GlobalSettingName.SEARCH_WEIGHT_SETTING));
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 关键字搜索 
	 * @param sort_type 1:降序 2:升序
	 * @return
	 */
	@RequestMapping(value = "/search" , method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse search(PageQuery pageQuery,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "weight_type", required = false) Integer weightType,
			@RequestParam(value = "min_count", required = false) Integer minCount,
			@RequestParam(value = "max_count", required = false) Integer maxCount,
			@RequestParam(value = "sort_type", required = false) Integer sortType){
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		int recordCount = searchDao.searchCount(keyword, weightType, minCount, maxCount);
		PageQueryResult queryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
		
		List<SearchKeyword> searchKeywords = searchDao.search(pageQuery, keyword, weightType, minCount, maxCount, sortType);
		
		data.put("list", searchKeywords);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 
	 * @param keyword
	 * @param weightType 权重分级 0：无, 1：1级，2:2级, 3:自定义
	 * @param weight 自定义权重分值
	 * @param type 0:自动,1:手动
	 */
	@RequestMapping(value = "/add/keywords" , method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse addKeywords(@RequestParam("keywords_json") String keywordsJson){
		JsonResponse jsonResponse = new JsonResponse();
		List<SearchKeyword> searchKeywords = new ArrayList<>();
		
		JSONArray jsonArray = JSONArray.parseArray(keywordsJson);
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			String keyword = jsonObject.getString("word");
			Integer weight = jsonObject.getInteger("sort");
			
			SearchKeyword searchKeyword = new SearchKeyword(keyword, 3, weight, 1);
			
			searchKeywords.add(searchKeyword);
		}
		searchDao.batchAddKeywords(searchKeywords);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 热搜关键词加载
	 * @return
	 */
	@RequestMapping("/hot/words")
	@ResponseBody
	public JsonResponse getHotWords() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();

		List<SearchKeyword> searchKeywords = searchDao.search(1);
		
		data.put("search_tip", searchKeywords);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/update/weight")
	@ResponseBody
	public JsonResponse search(@RequestParam("id") Long id,
			@RequestParam(value = "weight_type", required = false) Integer weightType,
			@RequestParam(value = "weight", required = false) Integer weight){
		JsonResponse jsonResponse = new JsonResponse();

		searchDao.update(id, weightType, weight);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
}
