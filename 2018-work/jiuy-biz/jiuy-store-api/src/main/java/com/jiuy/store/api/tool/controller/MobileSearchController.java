///**
// * 
// */
//package com.jiuy.store.api.tool.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.jiuyuan.constant.Constants;
//import com.jiuyuan.constant.GlobalSettingName;
//import com.jiuyuan.constant.product.SortType;
//import com.jiuyuan.entity.query.PageQuery;
//import com.jiuyuan.entity.query.PageQueryResult;
//import com.jiuyuan.entity.search.AppSearchKeyword;
//import com.store.entity.StoreSearchLog;
//import com.store.entity.UserDetail;
//import com.store.service.GlobalSettingService;
//import com.store.service.ProductService;
//import com.store.service.SearchFacade;
//import com.store.service.SearchKeywordService;
//import com.store.service.StoreSearchLogService;
//import com.yujj.util.uri.UriBuilder;
//import com.yujj.web.helper.JsonResponse;
//
//
///**
//* @author czy
//* @version 创建时间: 2017年7月19日 下午3:21:08
//*/
//@Controller
//@RequestMapping("/mobile/search")
//public class MobileSearchController {
//	@Autowired
//	SearchFacade searchFacade;
//	
//	@Autowired
//	StoreSearchLogService storeSearchLogService;
//	
//	@Autowired
//	SearchKeywordService searchKeywordService;
//	
//	@Autowired
//	GlobalSettingService globalSettingService;
//	
//	@Autowired
//	ProductService productService;
//	
//    @RequestMapping(value = "/search", method = {RequestMethod.GET })
//	@ResponseBody
//	public JsonResponse search(@RequestParam(value="keywords")String keywords,
//								@RequestParam(value="page", required=false, defaultValue="1")int page,
//								@RequestParam(value="pageSize", required=false, defaultValue="20")int pageSize,
//								@RequestParam(value="sort", required=false)SortType sort,
//								@RequestParam(value="guide_flag", required=false, defaultValue = "0") int guideFlag, 
//								UserDetail userDetail) {
//		JsonResponse jsonResponse = new JsonResponse();		
//		
////		long start = System.currentTimeMillis();
//		
//		
//		Map<String, Object> map = searchFacade.search(keywords, page-1, pageSize, userDetail.getId(), sort, guideFlag);
////		System.out.println("search time: " + (System.currentTimeMillis() - start));
//		Map<String, Object> data = new HashMap<String, Object>();
//		
//		int totalCount = 0;
//		if (map != null && map.containsKey("totalHits")) {
//			totalCount = (int)map.get("totalHits");
//		}
//
//		if (map != null && map.containsKey("resultList")) {
//			data.put("resultlist", map.get("resultList"));
//		}
//		
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(new PageQuery(page, pageSize), totalCount); 
//		
//        data.put("pageQuery", pageQueryResult);    	
//    	data.put("tipList", globalSettingService.getJsonObject(GlobalSettingName.SEARCH_TIP));
//    	data.put("ad", globalSettingService.getJsonObject(GlobalSettingName.SEARCH_AD));
//    	
//    	if (totalCount == 0)
//    		data.put("productSetting", searchFacade.getSearchRecommendProduct(userDetail));
//    	
//		return jsonResponse.setSuccessful().setData(data);
//	}
//        
//    @RequestMapping(value = "/lastsearch", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getLastSearch(PageQuery pageQuery, UserDetail userDetail) {
//    	JsonResponse jsonResponse = new JsonResponse();
//
//    	Map<String, Object> data = new HashMap<String, Object>();
//    	
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, 100);
//        data.put("pageQuery", pageQueryResult);
//        
//    	List<StoreSearchLog> userSearchLogList = 
//    			storeSearchLogService.getUserSearchLogs(userDetail.getId(), pageQueryResult);
//
//
//
//    	data.put("list", userSearchLogList);
//    	
//    	return jsonResponse.setSuccessful().setData(data);
//    }
//    
//    
//    @RequestMapping(value = "/hotsearch", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getHotSearch(@RequestParam(value="page", required=false, defaultValue="1")int page,
//									 @RequestParam(value="pageSize", required=false, defaultValue="7")int pageSize) {
//    	JsonResponse jsonResponse = new JsonResponse();
//
//    	Map<String, Object> data = new HashMap<String, Object>();
//    	
//    	int totalCount = searchKeywordService.getSearchKeywordCount();
//    	
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(new PageQuery(page, pageSize), totalCount);
//        data.put("pageQuery", pageQueryResult);
//        
//    	List<AppSearchKeyword> searchKeywordList = searchKeywordService.getSearchKeywords(new PageQuery(page, pageSize));
//
//    	data.put("list", searchKeywordList);
//    	
//        UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/search/hotsearch.json");
//        builder.set("page", pageQueryResult.getPage()%pageQueryResult.getPageCount()+1);
//        builder.set("pageSize", pageQueryResult.getPageSize());
//        data.put("nextDataUrl", builder.toUri());
//        
//    	return jsonResponse.setSuccessful().setData(data);
//    }
//
//}
