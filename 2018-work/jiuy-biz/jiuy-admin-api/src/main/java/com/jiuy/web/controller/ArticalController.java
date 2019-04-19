package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.ArticalFacade;
import com.jiuy.core.meta.artical.ARCategoryVO;
import com.jiuy.core.meta.artical.Artical;
import com.jiuy.core.meta.artical.ArticalVO;
import com.jiuy.core.service.artical.ARCategoryService;
import com.jiuy.core.service.artical.ArticalService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.article.ARCategory;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/artical")
public class ArticalController {
	
	private final int PAGE_SIZE = 4;
	
	@Resource
	private ARCategoryService arCategoryService;
	
	@Resource
	private ArticalService articalServiceImpl;
	
	@Resource
	private ArticalFacade articalFacade;
	
	/************************************文章管理**************************************/
    @AdminOperationLog
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "page/backend/articalindex"; 
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchArtical(@RequestParam(value = "page", defaultValue = "1", required = false)int page,
			@RequestParam(value = "content", defaultValue = "", required = false)String content,
			@RequestParam(value = "artical_cat", required = false, defaultValue = "-1")long aRCategory) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery query = new PageQuery(page, PAGE_SIZE);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
		List<ArticalVO> list = articalServiceImpl.searchArtical(query, content, aRCategory);
		int count = articalServiceImpl.searchArticalCount(content, aRCategory);
	    queryResult.setRecordCount(count);
	    
		data.put("articals", list);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}

	@RequestMapping("/remove")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse rmArtical(@RequestParam(value = "ids")long[] ids) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode code = articalServiceImpl.remove(ids);
		
		return jsonResponse.setResultCode(code);
	}
	
	/************************************文章管理：新增、编辑**************************************/
	/**
	 * 
	 * @param id
	 * 			文章id
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
    @AdminOperationLog
	public String edit(@RequestParam(value = "id", defaultValue = "0", required = false)long id, ModelMap modelMap) {
		
		if(id != 0) {
			ArticalVO artical = articalServiceImpl.loadCatById(id);
			modelMap.put("artical", artical);
		}
		
		return "page/backend/editartical";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse addArtical(@RequestBody Artical artical) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode code = articalServiceImpl.addArtical(artical);
		
		return jsonResponse.setResultCode(code);
	}
	
	/**
	 * 
	 * @param id
	 * 			文章id
	 * @return
	 */
	@RequestMapping(value = "/loadartical", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse loadCatById(@RequestParam(value = "id")long id) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(id != 0) {
			ArticalVO artical = articalServiceImpl.loadCatById(id);
			data.put("artical", artical);
		}
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/update")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse updateArtical(@RequestBody Artical artical) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode code = articalServiceImpl.updateArtical(artical);
		
		return jsonResponse.setResultCode(code);
	}
	
	/************************************文章分类**************************************/
    @AdminOperationLog
	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String classify() {
		return "page/backend/articalcat";
	}
	
	@RequestMapping(value = "/searchcat", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchCategory(@RequestParam(value = "page", defaultValue = "1", required = false)int page,
			@RequestParam(value = "name", defaultValue = "", required = false)String name) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery query = new PageQuery(page, PAGE_SIZE);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
		List<ARCategoryVO> list = arCategoryService.searchCat(query, name);
		statisticArticalNum(list);
		int count = arCategoryService.searchCatCount(name);
	    queryResult.setRecordCount(count);
	    
		data.put("categories", list);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	private void statisticArticalNum(List<ARCategoryVO> list) {
		for(ARCategoryVO vo : list) {
			int num = 0;
			List<Long> cats = new ArrayList<Long>();
			if(vo.getParentId() > 0) {
				cats.add(vo.getId());
			} else {
				cats = arCategoryService.getSubCats(vo.getId());
				cats.add(vo.getId());
			}
			num = articalServiceImpl.getCatARCount(cats);
			vo.setArticalNum(num);
		}
	}

	@RequestMapping(value = "/loadparentcat", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse loadParentCat() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<ARCategory> parentCat = arCategoryService.loadParentCat();
		
		data.put("parentCat", parentCat);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/loadcategorys", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse loadCategorys() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<ARCategoryVO> list = arCategoryService.searchCat(null, "");
		
		data.put("categorys", list);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/addcat")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse addCategory(@RequestBody ARCategory arCategory) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode code = arCategoryService.addcat(arCategory);
		
		return jsonResponse.setResultCode(code);
	}
	
	/**
	 * 
	 * @param id
	 * 			文章自定义分类的id
	 * @return
	 */
	@RequestMapping("/removecat")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse rmCategory(@RequestParam(value = "id")long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode code = articalFacade.removeCat(id);
		
		return jsonResponse.setResultCode(code);
	}
	
	@RequestMapping("/updatecat")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse updateCategory(@RequestBody ARCategory arCategory) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode code = arCategoryService.updateCategory(arCategory);
		
		return jsonResponse.setResultCode(code);
	}
	
	/************************************帮助中心**************************************/
    @AdminOperationLog
	@RequestMapping(value = "/helpcenter", method = RequestMethod.GET)
	public String helpCenter() {
		return "page/backend/helpcenter";
	}
	
	@RequestMapping(value = "/articalsbycat", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchArticalByCat(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "aRCategory_id", required = false, defaultValue = "0")long aRCategoryId) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		PageQuery pageQuery = new PageQuery(page, PAGE_SIZE);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
		List<ArticalVO> articalVOs = articalServiceImpl.searchArticalByCat(pageQuery, aRCategoryId);
		int count = articalServiceImpl.searchArticalCountByCat(aRCategoryId);
		pageQueryResult.setRecordCount(count);
		
		data.put("articals", articalVOs);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
}
