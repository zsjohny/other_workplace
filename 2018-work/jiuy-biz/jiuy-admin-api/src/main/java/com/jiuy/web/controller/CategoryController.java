package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.core.business.assemble.CategoryAssemble;
import com.jiuy.core.business.facade.CategoryFacade;
import com.jiuy.core.business.facade.CategoryFiltersFacade;
import com.jiuy.core.dao.modelv2.CategoryMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.category.CategoryVO;
import com.jiuy.core.service.CategoryFilterService;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.tag.TagService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.product.CategoryFilter;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	private static Logger logger = Logger.getLogger(CategoryController.class);
	
	@Resource
    private CategoryService categoryService;
	
	@Resource
	private CategoryAssemble categoryAssemble;
	
	@Resource
	private CategoryFacade categoryFacade;
	
	@Resource
	private TagService tagService;
	
	@Resource
	private PropertyService propertyService;
	
	@Resource
	private CategoryFilterService categoryFilterService;
	
	@Resource
	private CategoryFiltersFacade categoryFiltersFacade;
	
	   @Autowired
		private ProductNewMapper productNewMapper;
		
	@Autowired
	private CategoryMapper categoryMapper;
	
	@RequestMapping("/classify")
	public String index() {
		return "page/backend/classifies";
	}
	
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public JsonResponse getCategoryById(@PathVariable("id") long id) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	CategoryVO caVo = null;
    	try {
    		caVo = categoryFacade.getCategoryById(id);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
    	
    	return jsonResponse.setSuccessful().setData(caVo);
    }
    
	@RequestMapping(value = "/remove")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse rmCategory(@RequestParam(value = "ids")Long[] ids) {
		JsonResponse jsonResponse = new JsonResponse();
		ResultCode code = categoryFacade.rmCategory(ids);
		
		return jsonResponse.setResultCode(code);
	}
	
	/**
     * 返回带父级名称的类别分类数据
     * 
     * @param page 0：加载所有数据，其它：页码
     * @param pageSize
     * @param categoryType 产品分类：0 ;品牌馆的商家分类：1
     * @return
     */
	@RequestMapping(value = "/getcategory", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getCategory(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
                                    @RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "category_type", required = false, defaultValue = "0") int categoryType,
                                    @RequestParam(value = "category_name", required = false, defaultValue = "") String categoryName) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery query = new PageQuery(page, pageSize);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
		List<Map<String, Object>> categories = new ArrayList<Map<String, Object>>();
        categories = categoryFacade.getCategory(query, categoryType, categoryName);
        //查询无下级分类的分类
        List<Map<String, Object>> Mapcategories = new ArrayList<Map<String, Object>>();
        List<Category> childCats = new ArrayList<Category>();
        for (Map<String, Object> map : categories) {
        	childCats = categoryService.getSubCat((Long)map.get("Id"));
        	if(childCats.size()>0){
        		map.put("noSon", 0);
        	}else{
        		map.put("noSon", 1);
        	}
        	Mapcategories.add(map);
		}
        data.put("categories", Mapcategories);
		int recordCount = categoryFacade.getCategoryListCount(categoryType, categoryName);
		queryResult.setRecordCount(recordCount);
		
//		data.put("categories", categories);
		data.put("total", queryResult);

		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 获取分类列表(新)
	 * @param page
	 * @param pageSize
	 * @param categoryId
	 * @param categoryName
	 * @param categoryLevel
	 * @param parentCategoryId
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/getCategoryList")
	@ResponseBody
	public JsonResponse getCategoryList(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
            						@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "categoryId", required = false, defaultValue = "0") long categoryId,
                                    @RequestParam(value = "categoryName", required = false, defaultValue = "") String categoryName,
                                    @RequestParam(value = "categoryLevel", required = false, defaultValue = "-1") int categoryLevel,
                                    @RequestParam(value = "parentCategoryId", required = false, defaultValue = "-1") long parentCategoryId,
                                    @RequestParam(value = "status", required = false, defaultValue = "-2") int status,//状态-1删除，0正常，1隐藏
                                    @RequestParam(value = "weightMin", required = false, defaultValue = "0") int weightMin,
                                    @RequestParam(value = "weightMax", required = false, defaultValue = "0") int weightMax) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Map<String, Object> data = categoryFacade.getCategoryList(page,pageSize,categoryId,categoryName,categoryLevel,parentCategoryId,status,weightMin
					,weightMax);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取分类列表有误:"+e.getMessage());
			return jsonResponse.setError("获取分类列表有误");
		}
	}
	
	/**
	 * @param categoryType
	 *            0:商品分类;1:品牌馆分类;2:虚拟分类
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse addcategory(@RequestParam(value = "categoryLevel") int categoryLevel,//分类等级:0:未知;1:一级;2:二级;3:三级;
            						@RequestParam(value = "parentId",required=false,defaultValue="0") long parentId,
    								@RequestParam(value = "categoryName") String categoryName,
                                    @RequestParam(value = "status", required = false, defaultValue = "0") int status,
                                    @RequestParam(value = "description", required = false, defaultValue = "") String description,
                                    @RequestParam(value = "weight") int weight) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			categoryFacade.addCategoryNew(categoryLevel, parentId,categoryName,status,description,weight);
		} catch (Exception e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
        
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse updateCategory(@RequestParam(value = "categoryId") long categoryId,
    								   @RequestParam(value = "parentId",required=false,defaultValue="0") long parentId,
                                       @RequestParam(value = "categoryName") String categoryName,
                                       @RequestParam(value = "status", required = false, defaultValue = "0") int status,
                                       @RequestParam(value = "description", required = false, defaultValue = "") String description,
                                       @RequestParam(value = "weight") int weight) {
		JsonResponse jsonResponse = new JsonResponse();
        try {
            categoryFacade.updateCategoryNew(categoryId, parentId,categoryName,status,description,weight);
        } catch (Exception e) {
        	e.printStackTrace();
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
        }
		
        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
//	/**
//	 * 获取分类信息
//	 * @param categoryId
//	 * @return
//	 */
//	@RequestMapping(value = "/getCategoryInfo")
//	@ResponseBody
//	@AdminOperationLog
//    public JsonResponse getCategoryInfo(@RequestParam(value = "categoryId") long categoryId) {
//		JsonResponse jsonResponse = new JsonResponse();
//        try {
//        	Map<String, Object> data = categoryFacade.getCategoryInfo(categoryId);
//        	return jsonResponse.setSuccessful().setData(data);
//        } catch (Exception e) {
//        	e.printStackTrace();
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
//        }
//	}
	
	/**
	 * 获取对应等级id和名称
	 * @param categoryLevel
	 * @return
	 */
	@RequestMapping(value = "/getParentCategoryId")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse getParentCategoryId(@RequestParam(value = "categoryLevel") int categoryLevel) {
		JsonResponse jsonResponse = new JsonResponse();
        try {
        	Map<String, Object> data = categoryFacade.getParentCategoryId(categoryLevel);
        	return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
        	e.printStackTrace();
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
        }
	}

	/**
	 * 控制分类是否显示
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/hideShowCategory", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse hideShowCategory(@RequestParam(value = "id") long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode resultCode = categoryFacade.hideShowCategory(id);
		
		return jsonResponse.setResultCode(resultCode);
	}
	
	@RequestMapping(value = "/gettopcat", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getTopCat(@RequestParam(value = "category_type", required = false, defaultValue = "0") int categoryType) {
		JsonResponse jsonResponse = new JsonResponse();
		List<Map<String, Object>> parentCats = new ArrayList<Map<String, Object>>();
		
		parentCats = categoryFacade.getTopCat(categoryType);
		
		
		return jsonResponse.setSuccessful().setData(parentCats);
	}
	
	@RequestMapping(value = "/getparentcat", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getParentCat(@RequestParam(value = "category_type", required = false, defaultValue = "0") int categoryType) {
		JsonResponse jsonResponse = new JsonResponse();
		List<Category> parentCats = new ArrayList<Category>();
		
		//parentCats = categoryService.getTopCategory(categoryType);
		parentCats = categoryService.getAllTopCategory();
		//如果是虚拟分类,在分类名称后面增加标识
		for(Category parentCat : parentCats){
			if(parentCat.getCategoryType()==2){
				parentCat.setCategoryName(parentCat.getCategoryName()+"(虚拟)");
			}
		}
		
		return jsonResponse.setSuccessful().setData(parentCats);
	}
	
	@RequestMapping("/getchildcat")
	@ResponseBody
	public JsonResponse getChildCat(@RequestParam(value = "parentId") long parentId) {
		JsonResponse jsonResponse = new JsonResponse();
		List<Category> childCats = new ArrayList<Category>();
		
		childCats = categoryService.getSubCat(parentId);
		
		return jsonResponse.setSuccessful().setData(childCats);
	}
	
	@RequestMapping(value = "/addvirtualpro", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addVirtualProduct(@RequestParam(value = "category_id")long id,
			@RequestParam(value = "virtual_product")String virtualProduct) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode resultCode = categoryFacade.addVirtualProduct(id, virtualProduct);
		
		return jsonResponse.setResultCode(resultCode);
	}
	
	@RequestMapping("/{id}/tag")
	@ResponseBody
	public JsonResponse tagInfo(@PathVariable("id") Long id,
			@RequestParam(value = "parent_name", required = false) String parentName) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		Category category = categoryService.getCategoryById(id);
		data.put("category_info", category);
		data.put("category_parent_name", parentName);
		List<Tag> tags = tagService.searchWithChild();
		data.put("tags_info", tags);
		Map<Long, ProductPropName> productPropNames = propertyService.getPropertyNames();
		data.put("property_names", productPropNames.values());
		
		List<Map<String, Object>> tagFilterInfo = categoryFilterService.getFilterInfo(id, 0);
		data.put("tag_filter_info", tagFilterInfo);
		List<Map<String, Object>> attributionFilterInfo = categoryFilterService.getFilterInfo(id, 1);
		data.put("attribution_filter_info", attributionFilterInfo);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 
	 * @param categoryId
	 * @param categoryFilterJson 对象组字符串,不传为"[]"
	 * @return
	 */
	@RequestMapping("/filter/add")
	@ResponseBody
	public JsonResponse addFilters(@RequestParam("category_id") Long categoryId,
			@RequestParam("category_filters") String categoryFilterJson) {
		JsonResponse jsonResponse = new JsonResponse();
		
		List<CategoryFilter> categoryFilters = JSON.parseArray(categoryFilterJson, CategoryFilter.class);
		categoryFiltersFacade.add(categoryId, categoryFilters);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/{id}/coupon/search")
	@ResponseBody
	public JsonResponse underCouponSearch(@PathVariable("id") long id) {
		JsonResponse jsonResponse = new JsonResponse();
    	
		Map<String, Object> map = categoryFacade.underCouponSearch(id);
    	
    	return jsonResponse.setSuccessful().setData(map);
	}
	
}
