package com.yujj.web.controller.mobile;

import java.util.ArrayList;
import java.util.Collections;
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
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.LinkType;
import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.article.ArticleVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ArticleFacade;
import com.yujj.business.service.ArticleService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.ChatDelegator;

@Controller
@RequestMapping("/mobile/article")
public class MobileArticleController {
	
	@Autowired
	private ArticleFacade articleFacade;
	
	@Autowired
	private ChatDelegator chatDelegator;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@RequestMapping("/search")
	@ResponseBody
	public JsonResponse getArticleById(@RequestParam(value = "id")long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		Article article = articleFacade.getArticleById(id);
		
		return jsonResponse.setSuccessful().setData(article);
	}
	
	@RequestMapping("/addPageView")
	@ResponseBody
	public JsonResponse addPageView(@RequestParam(value = "id")long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		articleFacade.addPageView(id);
		
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 问题列表
	 */
	@RequestMapping(value = "/questionlist", method = RequestMethod.GET)
	@ResponseBody
	@NoLogin
	public JsonResponse questionList(@RequestParam(value="category_id", defaultValue="0", required=false) long categoryId, PageQuery pageQuery) {
		
		List<Article> questionList = chatDelegator.getQuestionList(categoryId, pageQuery);

		JsonResponse response = new JsonResponse();

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", questionList);

		return response.setSuccessful().setData(data);
	}
	
	/**
	 * @author jeff.zhan
	 */
	@RequestMapping(value = "/community/load", method = RequestMethod.GET)
	@ResponseBody
	@NoLogin
	public JsonResponse loadArticals(PageQuery pageQuery, UserDetail userDetail) {
		JsonResponse response = new JsonResponse();
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.COMMUNITY_NAVIGATION);
		JSONArray jsonArray = jsonObject.getJSONArray("settings");
		List<JSONObject> navigation_list = new ArrayList<>();
		for (Object object : jsonArray) {
			navigation_list.add((JSONObject)(object));
		}
		sort(navigation_list);
		List<JSONObject> carousel_list = new ArrayList<>();
		for (Object object : globalSettingService.getJsonObject(GlobalSettingName.COMMUNITY_CAROUSEL).getJSONArray("carousel")) {
			carousel_list.add((JSONObject)(object));
		}
		sort(carousel_list);
		
		if (navigation_list.size() > 0) {
			JSONObject jObject = navigation_list.get(0);
			Integer type = jObject.getInteger("type");
			if (type == LinkType.ARTICAL_CATEGORY.getIntValue()) {
				Long arCategoryId = Long.parseLong(jObject.getString("linkUrl"));
				List<ArticleVO> articles = articleFacade.getCommunityArticals(pageQuery, userDetail, arCategoryId);
				int recordCount = articleService.getCommunityArticalsCount(arCategoryId);
				PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
				
				data.put("list", articles);
				data.put("pageQuery", pageQueryResult);
			}
		}
		
		data.put("community_navigation", navigation_list);
		data.put("community_carousel", carousel_list);
		
		return response.setSuccessful().setData(data);
	}
	
	private void sort(List<JSONObject> list) {
		Collections.sort(list, (JSONObject a, JSONObject b) -> {
			Integer sort_a = a.getInteger("order");
			Integer sort_b = b.getInteger("order");
			if(sort_a == null || sort_b == null){
				return 0;
			}
		    return sort_b.compareTo(sort_a);
		});

	}
	

	@RequestMapping(value = "/community/search", method = RequestMethod.GET)
	@ResponseBody
	@NoLogin
	public JsonResponse communitySearch(PageQuery pageQuery, UserDetail userDetail,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "artical_category_id", required = false) Long arCategoryId) {
		JsonResponse response = new JsonResponse();
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.COMMUNITY_NAVIGATION);
		JSONArray jsonArray = jsonObject.getJSONArray("settings");
		List<JSONObject> list = new ArrayList<>();
		for (Object object : jsonArray) {
			list.add((JSONObject)(object));
		}
		sort(list);
		List<JSONObject> carousel_list = new ArrayList<>();
		for (Object object : globalSettingService.getJsonObject(GlobalSettingName.COMMUNITY_CAROUSEL).getJSONArray("carousel")) {
			carousel_list.add((JSONObject)(object));
		}
		sort(carousel_list);
		
		if (id != null) {
			ArticleVO articleVO = articleFacade.getCommunityArticleById(userDetail, id);
			data.put("articleVO", articleVO);
		} else if (arCategoryId != null) {
			List<ArticleVO> articles = articleFacade.getCommunityArticals(pageQuery, userDetail, arCategoryId);
			int recordCount = articleService.getCommunityArticalsCount(arCategoryId);
			PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
			
			data.put("list", articles);
			data.put("pageQuery", pageQueryResult);
		}
		
		data.put("community_navigation", list);
		data.put("community_carousel", carousel_list);
		
		return response.setSuccessful().setData(data);
	}
	
}
