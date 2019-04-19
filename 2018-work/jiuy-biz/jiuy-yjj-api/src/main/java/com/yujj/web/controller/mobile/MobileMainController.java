/**
 * 
 */
package com.yujj.web.controller.mobile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.ad.AdEnum;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.AdConfig;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.CategorySetting;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.ProductFilterVO;
import com.jiuyuan.entity.product.BrandFilter;
import com.jiuyuan.entity.product.CategoryFilter;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.ClientUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.util.uri.UriBuilder;
import com.yujj.business.service.AdService;
import com.yujj.business.service.BrandBusinessService;
import com.yujj.business.service.BrandService;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.PropertyService;
import com.yujj.business.service.TagService;
import com.yujj.dao.mapper.CategorySettingMapper;
import com.yujj.entity.Brand;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductPropName;
import com.yujj.entity.product.ProductPropValue;
import com.yujj.entity.product.ProductVO;

/**
 * @author LWS
 *
 */
@Controller
@RequestMapping("/mobile/mainview")
public class MobileMainController {
    private static final Logger logger = Logger.getLogger(MobileMainController.class);

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private BrandService brandService;
    
    @Autowired
    private BrandBusinessService brandBusinessService;

    @Autowired
    private AdService adService;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private CategorySettingMapper categorySettingMapper;

    @RequestMapping(value = "/banner", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getBanners() {
        JsonResponse jsonResponse = new JsonResponse();

        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> banners = new ArrayList<Map<String, Object>>();
        List<AdConfig> adConfigs = adService.getAdsByType(AdEnum.MAIN_BANNER);
        for (AdConfig adConfig : adConfigs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("imageUrl", adConfig.getImageUrl());
            map.put("targetUrl", adConfig.getLinkUrl());
            banners.add(map);
        }
        
        data.put("banners", banners);

        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/parentCategoryList", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getParentCategoryList() {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	List<Category> parentCategoryList = categoryService.getParentCategories();
    	
    	List<Category> categorieListAll = categoryService.getCategories();

		//添加子分类到列表
		for(Category category : parentCategoryList){
			for(Category categoryTemp : categorieListAll){
				if(categoryTemp.getParentId() == category.getId()){
					category.getChildCategories().add(categoryTemp);
				}
			}
		}
		//添加三级子分类
		for(Category category : parentCategoryList){
			if(category.getChildCategories().size() > 0){
				for(Category childCategory : category.getChildCategories()){
					for(Category categoryTemp : categorieListAll){
						if(categoryTemp.getParentId() == childCategory.getId()){
							childCategory.getChildCategories().add(categoryTemp);
						}
					}
				}
			}
			
		}
    	
    	data.put("parentCategoryList", parentCategoryList);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 加载【活动换购】产品列表
     * 
     * @return
     */
    @RequestMapping(value = "/exchangeprod", method = RequestMethod.GET , produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String loadExchangeProduct(PageQuery pageQuery) {
        Category cate = categoryService.getCategoryById(36L);

        Collection<Long> categoryIds = cate.getCategoryIds();
        List<Product> productList =
            productService.getProductOfCategory(categoryIds, SortType.CREATE_TIME_DESC, pageQuery);
        String retString = JSON.toJSONString(productList, true);
        logger.debug(logger);
        return retString;
    }

    /**
     * @author LWS
     * @since 2015/07/31
     * 
     * @return
     */
    @RequestMapping(value = "/boutique", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadBoutique(@RequestParam(value = "startpage", defaultValue = "-1") int page,
                                     PageQuery pageQuery) {
        if (page > 0) {// 兼容客户端传的是startpage参数
            pageQuery.setPage(page);
        }
        
        JsonResponse jsonResponse = new JsonResponse();
        List<Product> prodcutList = productService.getBestSellerProductList(pageQuery);
        int totalCount = productService.getBestSellerProductCount();
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productList", getProductList(prodcutList));
        data.put("pageQuery", pageQueryResult);
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/boutique.json");
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            data.put("nextDataUrl", builder.toUri());
        }
        
        return jsonResponse.setSuccessful().setData(data);
    }
    

    /**
     * @author CZY
     * @since 2015/07/31
     * 
     * @return
     */
    @RequestMapping(value = "/boutique185", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadBoutique185(@RequestParam(value = "startpage", defaultValue = "-1") int page, @RequestParam(value = "sort", defaultValue = "1") SortType sortType,
                                     PageQuery pageQuery) {
        if (page > 0) {// 兼容客户端传的是startpage参数
            pageQuery.setPage(page);
        }
        
        JsonResponse jsonResponse = new JsonResponse();
        List<Product> prodcutList = productService.getBestSellerProductList185(pageQuery, sortType);
        int totalCount = productService.getBestSellerProductCount();
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productList", getProductList(prodcutList));
        data.put("pageQuery", pageQueryResult);
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/boutique185.json");
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            builder.set("sort", sortType.getIntValue());
            data.put("nextDataUrl", builder.toUri());
        }
        
        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/productbycategory", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadProductByCategory(@RequestParam("categoryid") long categoryId,
                                              @RequestParam(value = "sort", defaultValue = "1") SortType sortType,
                                              @RequestParam(value = "startpage", defaultValue = "-1") int page,
                                              PageQuery pageQuery, ClientPlatform clientPlatform) {
        if (clientPlatform != null && clientPlatform.isAndroid() &&
            ClientUtil.compareTo(clientPlatform.getVersion(), "1.2.0") == 0 && categoryId == 1) {
            return loadBoutique(page, pageQuery); // 兼容android 1.2.0首页bug
        }

        JsonResponse jsonResponse = new JsonResponse();

        if (page > 0) {// 兼容客户端传的是startpage参数
            pageQuery.setPage(page);
        }

        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        Collection<Long> categoryIds = category.getCategoryIds();
        List<Product> prodcutList = productService.getProductOfCategory(categoryIds, sortType, pageQuery);
        int totalCount = productService.getProductCountOfCategory(categoryIds);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productList", getProductList(prodcutList));
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        data.put("categoryId", categoryId);
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbycategory.json");
            builder.set("categoryid", categoryId);
            builder.set("sort", sortType.getIntValue());
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            data.put("nextDataUrl", builder.toUri());
        }

        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getCategoryList() {
        JsonResponse jsonResponse = new JsonResponse();
        List<Category> categoryList = categoryService.getCategoryTree(true);
        return jsonResponse.setSuccessful().setData(categoryList);
    }
    
    private List<Map<String, Object>> getProductList(List<Product> products) {
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        String deductStr = (String) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING).get("activityText");
        DecimalFormat    df   = new DecimalFormat("##.##");
    	for (Product product : products) {
    		if(product.getDeductPercent() > 0){
        		product.setDeductDesc(deductStr + df.format(product.getDeductPercent()) + "%");
    		}
    		productList.add(product.toSimpleMap15());
    	}
        return productList;
    }
    
    @RequestMapping(value = "/productbycategory15", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadProductByCategory15(@RequestParam("categoryid") long categoryId,
                                              @RequestParam(value = "sort", defaultValue = "1") SortType sortType,
                                              @RequestParam(value = "startpage", defaultValue = "-1") int page,
                                              PageQuery pageQuery, ClientPlatform clientPlatform) {
        if (clientPlatform != null && clientPlatform.isAndroid() &&
            ClientUtil.compareTo(clientPlatform.getVersion(), "1.2.0") == 0 && categoryId == 1) {
            return loadBoutique(page, pageQuery); // 兼容android 1.2.0首页bug
        }

        JsonResponse jsonResponse = new JsonResponse();

        if (page > 0) {// 兼容客户端传的是startpage参数
            pageQuery.setPage(page);
        }

        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        Collection<Long> categoryIds = category.getCategoryIds();
        List<Product> prodcutList = productService.getProductOfCategory(categoryIds, sortType, pageQuery);
        int totalCount = productService.getProductCountOfCategory(categoryIds);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productList", getProductList15(prodcutList));
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        data.put("categoryId", categoryId);
        data.put("categoryName", category.getCategoryName());
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbycategory15.json");
            builder.set("categoryid", categoryId);
            builder.set("sort", sortType.getIntValue());
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            data.put("nextDataUrl", builder.toUri());
        }

        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/productbycategory186", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadProductByCategory186(@RequestParam("categoryid") String categoryId,   //分类ID，可传多个，逗号隔开 如categoryid=208,150,33
    		@RequestParam(value = "sort", defaultValue = "1") SortType sortType,
    		@RequestParam(value = "startpage", defaultValue = "-1") int page,
    		@RequestParam(value = "minPrice", defaultValue = "0") double minPrice,	//最低价
    		@RequestParam(value = "maxPrice", defaultValue = "0") double maxPrice,	//最高价
    		@RequestParam(value = "propGroup", required = false) String propGroup,  //属性筛选 格式 9:430,431,432,433\\10:15,16
    		@RequestParam(value = "tagGroup", required = false) String tagGroup,  //标签筛选 格式 9:430,431,432,433\\10:15,16
    		@RequestParam(value = "inStock", required = false) Boolean inStock,  //是否有货
    		@RequestParam(value = "onSale", required = false) Boolean onSale,  //是否促销
    		@RequestParam(value = "deductFlag", required = false) Boolean deductFlag,  //是否玖币抵扣
    		@RequestParam(value = "childCtgrId", defaultValue = "0") long childCtgrId,  //子分类Id
    		PageQuery pageQuery, ClientPlatform clientPlatform) {
    	
    	if (clientPlatform != null && clientPlatform.isAndroid() &&
    			ClientUtil.compareTo(clientPlatform.getVersion(), "1.2.0") == 0 && categoryId.equals("1")) {
    		return loadBoutique(page, pageQuery); // 兼容android 1.2.0首页bug
    	}
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	if (page > 0) {// 兼容客户端传的是startpage参数
    		pageQuery.setPage(page);
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("sortType", sortType);
    	data.put("minPrice", minPrice);
    	data.put("maxPrice", maxPrice);
    	data.put("propGroup", propGroup);
    	data.put("inStock", inStock);
    	data.put("onSale", onSale);
    	data.put("deductFlag", deductFlag);
    	data.put("childCtgrId", childCtgrId);
    	data.put("multiSlctFlag", "NO");
    	
    	String[] ctgrIdArr = categoryId.split(",");
    	
    	if(ctgrIdArr == null || ctgrIdArr.length == 0){
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
    	}
    	List<Category> categoryList = categoryService.getCategoriesByIdsArr(ctgrIdArr);
    	
    	List<Category> categorieListAll = categoryService.getCategories();

		//添加子分类到列表
		for(Category category : categoryList){
			for(Category categoryTemp : categorieListAll){
				if(categoryTemp.getParentId() == category.getId()){
					category.getChildCategories().add(categoryTemp);
				}
			}
		}
    	if (categoryList == null || categoryList.size() == 0) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
    	}
    	
    	
    	Set<Long> propertyIds = new HashSet<Long>();
    	Set<Long> tagIds = new HashSet<Long>();
    	Set<Long> catIds = new HashSet<Long>();
    	
    	for(String  idTemp : ctgrIdArr){
    		catIds.add(Long.parseLong(idTemp));
    	}
    	
    	List<CategoryFilter> categoryFilterList = new ArrayList<CategoryFilter>();
    	Category category = categoryList.get(0);
    	
    //	categoryFilterList = productService.getProductFilterByCatId(category.getId());
    	categoryFilterList = productService.getProductFilterByCatIds(catIds);
    	//若子类筛选项为空，则用父类筛选（单一品类时）
    	if(ctgrIdArr.length == 1){
    		if(category.getParentId()> 0 && (categoryFilterList == null || categoryFilterList.size() == 0 )){
    			categoryFilterList = productService.getProductFilterByCatId(category.getParentId());
    		}  		
    	}	
    	
    	for(CategoryFilter categoryFilter : categoryFilterList){
    		//基本属性
    		categoryFilter.setSelectNum(5);
    		if( categoryFilter.getType() == 1){
    			propertyIds.add(categoryFilter.getRelatedId());
    		}else if( categoryFilter.getType() == 0){
    			tagIds.add(categoryFilter.getRelatedId());
    		}
    	}
    	
    	//拼凑全部筛选项
    	List<ProductPropName>  propNameList = new ArrayList<ProductPropName>();
    	List<ProductPropValue> propValueListTemp;
    	List<ProductFilterVO> filterVOListTemp;
    	ProductFilterVO productFilterVOTemp;
    	if(propertyIds.size() > 0 ){
    		propNameList = propertyService.getPropertyNamesListByIds(propertyIds);
    		List<ProductPropValue> productPropValueList = propertyService.getPropertyValuesByNameIds(propertyIds);
    		
    		for(ProductPropName productPropName : propNameList ){
    			propValueListTemp = new ArrayList<ProductPropValue>();
    			filterVOListTemp = new ArrayList<ProductFilterVO>();
    			for(ProductPropValue productPropValue : productPropValueList){
    				if(productPropValue.getPropertyNameId() == productPropName.getId()){
    					propValueListTemp.add(productPropValue);
    					productFilterVOTemp = new ProductFilterVO();
    					productFilterVOTemp.setId(productPropValue.getId());
    					productFilterVOTemp.setPropertyValue(productPropValue.getPropertyValue());
    					filterVOListTemp.add(productFilterVOTemp);
    				}	
    			}
    			productPropName.setPropertyValueList(propValueListTemp);
    			//放入筛选列表中
    			for(CategoryFilter categoryFilter : categoryFilterList){
    				if(categoryFilter.getRelatedId() == productPropName.getId() && categoryFilter.getType() == 1){
    					//categoryFilter.setPropValueList(propValueListTemp);  //del
    					categoryFilter.setProductFilterVOList(filterVOListTemp);
    					categoryFilter.setRelatedName(productPropName.getPropertyName());
    				}
    			}
    		}
    	}
     
    	//快捷设置
    	List<CategoryFilter>  shortcutList = new ArrayList<CategoryFilter>();
    	int shortcutCount = 0; //快捷筛选项个数限定，暂时不限制
    	//一级分类快捷筛选添加全部品类筛选
    	if(categoryList.size() > 1 || (category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0)){
    		data.put("shortcutCategory", "YES");
    		shortcutCount ++;
    		List<ProductFilterVO> productFilterVOList = new ArrayList<ProductFilterVO>();
    		ProductFilterVO productFilterVO;
    		CategoryFilter categoryFilter = new CategoryFilter();
    		categoryFilter.setType(2);  //品类筛选
    		if(categoryList.size() > 1){
    			categoryFilter.setRelatedName("全部品类");	
    			for(Category ctgr : categoryList){
    				if(ctgr.getParentId() == 0 && ctgr.getChildCategories() != null && ctgr.getChildCategories().size() > 0){
    					for(Category categoryTemp : ctgr.getChildCategories()){
    	    				productFilterVO = new ProductFilterVO();
    	    				productFilterVO.setId(categoryTemp.getId());
    	    				productFilterVO.setPropertyValue(categoryTemp.getCategoryName());
    	    				productFilterVO.setIconUrl(categoryTemp.getIconUrl());
    	    				productFilterVO.setIconOnUrl(categoryTemp.getIconOnUrl());
    	    				productFilterVOList.add(productFilterVO);
    	    			}	
    				}else{
    					productFilterVO = new ProductFilterVO();
	    				productFilterVO.setId(ctgr.getId());
	    				productFilterVO.setPropertyValue(ctgr.getCategoryName());
	    				productFilterVO.setIconUrl(ctgr.getIconUrl());
	    				productFilterVO.setIconOnUrl(ctgr.getIconOnUrl());
	    				productFilterVOList.add(productFilterVO);
    				}
    			}
    		}else{
    			categoryFilter.setRelatedName("全部" + category.getCategoryName());			
    			for(Category categoryTemp : category.getChildCategories()){
    				productFilterVO = new ProductFilterVO();
    				productFilterVO.setId(categoryTemp.getId());
    				productFilterVO.setPropertyValue(categoryTemp.getCategoryName());
    				productFilterVO.setIconUrl(categoryTemp.getIconUrl());
    				productFilterVO.setIconOnUrl(categoryTemp.getIconOnUrl());
    				productFilterVOList.add(productFilterVO);
    			}
    		}
    		categoryFilter.setProductFilterVOList(productFilterVOList);
    		categoryFilter.setSelectNum(1);
    		shortcutList.add(categoryFilter);
    		data.put("childCategoryFilter", categoryFilter);
    	}else{
    		data.put("shortcutCategory", "NO");
    	}
    	
//    	if(category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
//    		data.put("shortcutCategory", "YES");
//    		shortcutCount ++;
//    		List<ProductFilterVO> productFilterVOList = new ArrayList<ProductFilterVO>();
//    		ProductFilterVO productFilterVO;
//    		CategoryFilter categoryFilter = new CategoryFilter();
//    		categoryFilter.setRelatedName("全部" + category.getCategoryName());
//    		categoryFilter.setType(2);  //品类筛选
//    		for(Category categoryTemp : category.getChildCategories()){
//    			productFilterVO = new ProductFilterVO();
//    			productFilterVO.setId(categoryTemp.getId());
//    			productFilterVO.setPropertyValue(categoryTemp.getCategoryName());
//    			productFilterVO.setIconUrl(categoryTemp.getIconUrl());
//    			productFilterVO.setIconOnUrl(categoryTemp.getIconOnUrl());
//    			productFilterVOList.add(productFilterVO);
//    		}
//    		categoryFilter.setProductFilterVOList(productFilterVOList);
//    		categoryFilter.setSelectNum(1);
//    		shortcutList.add(categoryFilter);
//    		data.put("childCategoryFilter", categoryFilter);
//    	}else{
//    		data.put("shortcutCategory", "NO");
//    	}
    	
    	data.put("shortcutList", shortcutList);
    	data.put("categoryFilterList", categoryFilterList);
    	
    	//标签筛选处理 
    	List<Tag>  tagAllList = new ArrayList<Tag>();
    	List<Tag>  tagParentList = new ArrayList<Tag>();
    	List<Tag>  tagChildList = new ArrayList<Tag>();
    	if(tagIds.size() > 0 ){
    		tagAllList = tagService.getPropertyNamesListByIds(tagIds);
    		for(Tag tagTemp : tagAllList ){
    			if(tagTemp.getGroupId() == -1){
    				tagParentList.add(tagTemp);
    			}
    		}
    		
			for(Tag tagParent : tagParentList ){
				tagChildList = new ArrayList<Tag>();
				filterVOListTemp = new ArrayList<ProductFilterVO>();
				for(Tag tagTemp : tagAllList ){
				
					if(tagTemp.getGroupId() == tagParent.getId()){
						tagChildList.add(tagTemp);
						productFilterVOTemp = new ProductFilterVO();
    					productFilterVOTemp.setId(tagTemp.getId());
    					productFilterVOTemp.setPropertyValue(tagTemp.getName());
    					filterVOListTemp.add(productFilterVOTemp);
					}
				}
				tagParent.setChildTagList(tagChildList);
				//放入筛选列表中
    			for(CategoryFilter categoryFilter : categoryFilterList){
    				if(categoryFilter.getRelatedId() == tagParent.getId() && categoryFilter.getType() == 0){
    					
    					categoryFilter.setProductFilterVOList(filterVOListTemp);
    					categoryFilter.setRelatedName(tagParent.getName());
    					
    				}
    			}
			}	
    	}
    	
    	//删除没选项的筛选
    	Iterator<CategoryFilter> stuIter = categoryFilterList.iterator();  
	    while (stuIter.hasNext()) {  
	    	CategoryFilter categoryFilter = stuIter.next();  
	        if (categoryFilter.getProductFilterVOList() == null || categoryFilter.getProductFilterVOList().size() == 0) {
	        	stuIter.remove();//这里要使用Iterator的remove方法移除当前对象
	        }
	    }
		//加入快捷筛选
		for(CategoryFilter categoryFilter : categoryFilterList){
			//shortcutCount < 4 &&
    		if(  categoryFilter.getQuickSetting() == 1 && categoryFilter.getProductFilterVOList() != null && categoryFilter.getProductFilterVOList().size() > 0){
    			shortcutList.add(categoryFilter);
    			shortcutCount ++;
    		}
		}

    	Collection<Long> categoryIds = new ArrayList<Long>();
    	if(childCtgrId > 0 ){
    		 List<Long> idListTemp = new ArrayList<Long>();
//    		 for(String  idTemp : ctgrIdArr){
//    			 idListTemp.add(Long.parseLong(idTemp));
//    	    	}	
    		 idListTemp.add(childCtgrId);
    		 categoryIds = idListTemp;
    	}else {
    		for(Category categoryTemp: categoryList){
    			categoryIds.addAll(categoryTemp.getCategoryIds());
    		}
    	}
    			
    	List<Product> prodcutList = new ArrayList<Product>();
    	Map<String, String[]> filterMap = new HashMap<String ,String[]>();
    	Map<String, String[]> colorSizeMap = new HashMap<String ,String[]>();
    	Map<String, String[]> tagFilterMap = new HashMap<String ,String[]>();
    	
    	//解析筛选传值   格式 9:430,431,432,433\\10:15,16
    	
    	if(propGroup != null && propGroup.length() > 0){
	    	String[]  propGroupArr = propGroup.split("\\\\");
	    	if(propGroupArr != null && propGroupArr.length > 0 ){
	    		for(String propArr : propGroupArr){
	    			String[]  propGroupNatureArr = propArr.split(":");
	    			if(propGroupNatureArr != null && propGroupNatureArr.length > 1){
	    				String[] propValueArr = propGroupNatureArr[1].split(",");
	    				if(propGroupNatureArr[0] != null && propGroupNatureArr[0].length() > 0 && ( propGroupNatureArr[0].trim().equals("7") || propGroupNatureArr[0].trim().equals("8"))){
	    					for(int i = 0 ; i<propValueArr.length ; i++){
	    						propValueArr[i] = propGroupNatureArr[0] + ":" + propValueArr[i];
	    					}
	    					colorSizeMap.put(propGroupNatureArr[0],  propValueArr);
	    				}else{
	    					filterMap.put(propGroupNatureArr[0], propValueArr);
	    				}
	    				
	    			}
	    		}
	    	}
    	
    	}
    	//解析TAG筛选传值   格式 9:430,431,432,433\\10:15,16
    	if(tagGroup != null && tagGroup.length() > 0){
    		String[]  tagGroupArr = tagGroup.split("\\\\");
    		if(tagGroupArr != null && tagGroupArr.length > 0 ){
    			for(String tagArr : tagGroupArr){
    				String[]  tagGroupNatureArr = tagArr.split(":");
    				if(tagGroupNatureArr != null && tagGroupNatureArr.length > 1){
    					if(tagGroupNatureArr[1].length()>0){
    						String[]  tagValueArr = tagGroupNatureArr[1].split(",");
    						tagFilterMap.put(tagGroupNatureArr[0], tagValueArr);
    					}
    				}
    			}
    		}
    	}

    	prodcutList = productService.getProductByFilter(categoryIds, filterMap, tagFilterMap, colorSizeMap, sortType,  pageQuery ,minPrice, maxPrice, inStock, onSale, deductFlag);
	
    	int totalCount = productService.getProductCountOfCategoryByFilter(categoryIds, filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, inStock, onSale, deductFlag);	
    	String deductStr = (String) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING).get("activityText");
    	DecimalFormat    df   = new DecimalFormat("##.##");
    	for(Product product : prodcutList){
    		if(product.getDeductPercent() > 0){
    			product.setDeductDesc(deductStr + df.format(product.getDeductPercent()) + "%");    			
    		}
    	}
    	
    	data.put("productList", getProductList15(prodcutList));
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	data.put("pageQuery", pageQueryResult);
    	data.put("categoryId", categoryId);
    	data.put("category", category);
    	data.put("categoryList", categoryList);
    	data.put("filterMap", filterMap);
    	data.put("categoryName", category.getCategoryName());
    	data.put("categoryFilterList", categoryFilterList);
    	data.put("inStockTitle", "仅看有货");
    	data.put("onSaleTitle", "促销");
    	data.put("priceTitle", "价格区间");
    	data.put("minPriceTitle", "最低价");
    	data.put("maxPriceTitle", "最高价");
    	data.put("subCategoryTitle", "品类");
    	
    	if (pageQueryResult.isMore()) {
    		UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbycategory186.json");
    		builder.set("categoryid", categoryId);
    		builder.set("childCtgrId", childCtgrId);
    		builder.set("minPrice", minPrice);
    		builder.set("maxPrice", maxPrice);
    		builder.set("propGroup", propGroup);
    		builder.set("inStock", inStock);
    		builder.set("onSale", onSale);
    		builder.set("deductFlag", deductFlag);
    		builder.set("sort", sortType.getIntValue());
    		builder.set("page", pageQueryResult.getPage() + 1);
    		builder.set("pageSize", pageQueryResult.getPageSize());
    		data.put("nextDataUrl", builder.toUri());
    	}
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    
    @RequestMapping(value = "/productbybrandfilter", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadProductFilterByBrand(@RequestParam(value="guide_flag", required=false, defaultValue = "0") int guideFlag,
    		@RequestParam("brandId") long brandId,   //分类ID，可传多个，逗号隔开 如categoryid=208,150,33
    		@RequestParam(value = "sort", defaultValue = "1") SortType sortType,
    		@RequestParam(value = "startpage", defaultValue = "-1") int page,
    		@RequestParam(value = "minPrice", defaultValue = "0") double minPrice,	//最低价
    		@RequestParam(value = "maxPrice", defaultValue = "0") double maxPrice,	//最高价
    		@RequestParam(value = "propGroup", required = false) String propGroup,  //属性筛选 格式 9:430,431,432,433\\10:15,16
    		@RequestParam(value = "tagGroup", required = false) String tagGroup,  //标签筛选 格式 9:430,431,432,433\\10:15,16
    		@RequestParam(value = "inStock", required = false) Boolean inStock,  //是否有货
    		@RequestParam(value = "onSale", required = false) Boolean onSale,  //是否促销
    		@RequestParam(value = "childCtgrId", defaultValue = "0") long childCtgrId,  //子分类Id
    		@RequestParam(value = "categoryid" , defaultValue = "") String  categoryId,   //分类ID
    		@RequestParam(value = "deductFlag", required = false) Boolean deductFlag,  //是否玖币抵扣
    		PageQuery pageQuery, ClientPlatform clientPlatform, UserDetail userDetail) {
    	
//    	visitService.addVisitHistory(userDetail.getId(), 0, new Long[] {brandId}, 1);
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	if (page > 0) {// 兼容客户端传的是startpage参数
    		pageQuery.setPage(page);
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("sortType", sortType);
    	data.put("minPrice", minPrice);
    	data.put("maxPrice", maxPrice);
    	data.put("propGroup", propGroup);
    	data.put("inStock", inStock);
    	data.put("onSale", onSale);
    	data.put("childCtgrId", childCtgrId);
    	data.put("multiSlctFlag", "NO");
    	data.put("deductFlag", deductFlag);
    	
    	if(brandId > 0){
    		
    		Brand brand = brandService.getBrand(brandId);
    		if(brand == null){
    			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
    		}
    		BrandBusiness brandBusiness = brandBusinessService.getByBrandId(brandId);
    		data.put("brandName", brand.getBrandName());
    		data.put("brandLogo", brand.getLogo());
    		if(brandBusiness != null){
    			data.put("brandBusinessFlag", 1); 		
    		}else {
    			data.put("brandBusinessFlag", 0);
    		}
    	}
//    	if(brandId == 0){
//    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
//    	}
//    	Brand brand = brandService.getBrand(brandId);
//    	if(brand == null){
//    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
//    	}
//    	BrandBusiness brandBusiness = brandBusinessService.getByBrandId(brandId);

    	List<Category> categoryList = new ArrayList<Category>();
    	String[] ctgrIdArr ;
    	if( categoryId == null ||categoryId.length() == 0 || categoryId.equals("0") ){
    		categoryId = "";
    		categoryList = categoryService.getParentCategories();
    		
        	for(Category category : categoryList){
        		categoryId += category.getId()+",";
        	}
        	if(categoryId.length() > 0){
        		categoryId = categoryId.substring(0, categoryId.length()-1);
        	}
        	ctgrIdArr = categoryId.split(",");
    	}else{
    		ctgrIdArr = categoryId.split(",");
    		
    		categoryList = categoryService.getCategoriesByIdsArr(ctgrIdArr);
    		
    	}
    	if(ctgrIdArr == null || ctgrIdArr.length == 0){
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
    	}
    	
    	List<Category> parentCategoryList = categoryService.getParentCategories();
    	
    	List<Category> categorieListAll = categoryService.getCategories();
    	
    	//添加子分类到列表
    	for(Category category : categoryList){
    		for(Category categoryTemp : categorieListAll){
    			if(categoryTemp.getParentId() == category.getId()){
    				category.getChildCategories().add(categoryTemp);
    			}
    		}
    	}
    	if (categoryList == null || categoryList.size() == 0) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
    	}
    	
    	
    	Set<Long> propertyIds = new HashSet<Long>();
    	Set<Long> tagIds = new HashSet<Long>();
    	Set<Long> catIds = new HashSet<Long>();
    	
    	for(String  idTemp : ctgrIdArr){
    		catIds.add(Long.parseLong(idTemp));
    	}
    	
    	List<BrandFilter> categoryFilterList = new ArrayList<BrandFilter>();
    	Category category = categoryList.get(0);
    	
    	//	categoryFilterList = productService.getProductFilterByCatId(category.getId());
    	categoryFilterList = productService.getProductFilterByBrandIds(catIds);
//    	categoryFilterList = productService.getProductFilterByCatIds(catIds);
//    	//若子类筛选项为空，则用父类筛选（单一品类时）
//    	if(ctgrIdArr.length == 1){
//    		if(category.getParentId()> 0 && (categoryFilterList == null || categoryFilterList.size() == 0 )){
//    			categoryFilterList = productService.getProductFilterByCatId(category.getParentId());
//    		}  		
//    	}	
    	
    	for(BrandFilter categoryFilter : categoryFilterList){
    		//基本属性
    		categoryFilter.setSelectNum(5);
    		if( categoryFilter.getType() == 1){
    			propertyIds.add(categoryFilter.getRelatedId());
    		}else if( categoryFilter.getType() == 0){
    			tagIds.add(categoryFilter.getRelatedId());
    		}
    	}
    	
    	//拼凑全部筛选项
    	List<ProductPropName>  propNameList = new ArrayList<ProductPropName>();
    	List<ProductPropValue> propValueListTemp;
    	List<ProductFilterVO> filterVOListTemp;
    	ProductFilterVO productFilterVOTemp;
    	if(propertyIds.size() > 0 ){
    		propNameList = propertyService.getPropertyNamesListByIds(propertyIds);
    		List<ProductPropValue> productPropValueList = propertyService.getPropertyValuesByNameIds(propertyIds);
    		
    		for(ProductPropName productPropName : propNameList ){
    			propValueListTemp = new ArrayList<ProductPropValue>();
    			filterVOListTemp = new ArrayList<ProductFilterVO>();
    			for(ProductPropValue productPropValue : productPropValueList){
    				if(productPropValue.getPropertyNameId() == productPropName.getId()){
    					propValueListTemp.add(productPropValue);
    					productFilterVOTemp = new ProductFilterVO();
    					productFilterVOTemp.setId(productPropValue.getId());
    					productFilterVOTemp.setPropertyValue(productPropValue.getPropertyValue());
    					filterVOListTemp.add(productFilterVOTemp);
    				}	
    			}
    			productPropName.setPropertyValueList(propValueListTemp);
    			//放入筛选列表中
    			for(BrandFilter categoryFilter : categoryFilterList){
    				if(categoryFilter.getRelatedId() == productPropName.getId() && categoryFilter.getType() == 1){
    					//categoryFilter.setPropValueList(propValueListTemp);  //del
    					categoryFilter.setProductFilterVOList(filterVOListTemp);
    					categoryFilter.setRelatedName(productPropName.getPropertyName());
    				}
    			}
    		}
    	}
    	
    	//快捷设置
    	List<BrandFilter>  shortcutList = new ArrayList<BrandFilter>();
    	int shortcutCount = 0; //快捷筛选项个数限定，暂时不限制
    	//一级分类快捷筛选添加全部品类筛选
    	if(categoryList.size() > 1 || (category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0)){
    		data.put("shortcutCategory", "YES");
    		shortcutCount ++;
    		List<ProductFilterVO> productFilterVOList = new ArrayList<ProductFilterVO>();
    		ProductFilterVO productFilterVO;
    		BrandFilter categoryFilter = new BrandFilter();
    		categoryFilter.setType(2);  //品类筛选
    		if(categoryList.size() > 1){
    			categoryFilter.setRelatedName("全部品类");	
    			for(Category ctgr : parentCategoryList){
    				if(ctgr.getParentId() == 0 && ctgr.getChildCategories() != null && ctgr.getChildCategories().size() > 0){
//    					for(Category categoryTemp : ctgr.getChildCategories()){
//    						productFilterVO = new ProductFilterVO();
//    						productFilterVO.setId(categoryTemp.getId());
//    						productFilterVO.setPropertyValue(categoryTemp.getCategoryName());
//    						productFilterVO.setIconUrl(categoryTemp.getIconUrl());
//    						productFilterVO.setIconOnUrl(categoryTemp.getIconOnUrl());
//    						productFilterVOList.add(productFilterVO);
//    					}	
    				}else{
    					productFilterVO = new ProductFilterVO();
    					productFilterVO.setId(ctgr.getId());
    					productFilterVO.setPropertyValue(ctgr.getCategoryName());
    					productFilterVO.setIconUrl(ctgr.getIconUrl());
    					productFilterVO.setIconOnUrl(ctgr.getIconOnUrl());
    					productFilterVOList.add(productFilterVO);
    				}
    			}
    		}else{
    			categoryFilter.setRelatedName("全部" + category.getCategoryName());			
    			for(Category categoryTemp : category.getChildCategories()){
    				productFilterVO = new ProductFilterVO();
    				productFilterVO.setId(categoryTemp.getId());
    				productFilterVO.setPropertyValue(categoryTemp.getCategoryName());
    				productFilterVO.setIconUrl(categoryTemp.getIconUrl());
    				productFilterVO.setIconOnUrl(categoryTemp.getIconOnUrl());
    				productFilterVOList.add(productFilterVO);
    			}
    		}
    		categoryFilter.setProductFilterVOList(productFilterVOList);
    		categoryFilter.setSelectNum(1);
    		shortcutList.add(categoryFilter);
    		data.put("childCategoryFilter", categoryFilter);
    	}else{
    		data.put("shortcutCategory", "NO");
    	}
    	
    	
    	
    	data.put("shortcutList", shortcutList);
    	data.put("categoryFilterList", categoryFilterList);
    	
    	//标签筛选处理 
    	List<Tag>  tagAllList = new ArrayList<Tag>();
    	List<Tag>  tagParentList = new ArrayList<Tag>();
    	List<Tag>  tagChildList = new ArrayList<Tag>();
    	if(tagIds.size() > 0 ){
    		tagAllList = tagService.getPropertyNamesListByIds(tagIds);
    		for(Tag tagTemp : tagAllList ){
    			if(tagTemp.getGroupId() == -1){
    				tagParentList.add(tagTemp);
    			}
    		}
    		
    		for(Tag tagParent : tagParentList ){
    			tagChildList = new ArrayList<Tag>();
    			filterVOListTemp = new ArrayList<ProductFilterVO>();
    			for(Tag tagTemp : tagAllList ){
    				
    				if(tagTemp.getGroupId() == tagParent.getId()){
    					tagChildList.add(tagTemp);
    					productFilterVOTemp = new ProductFilterVO();
    					productFilterVOTemp.setId(tagTemp.getId());
    					productFilterVOTemp.setPropertyValue(tagTemp.getName());
    					filterVOListTemp.add(productFilterVOTemp);
    				}
    			}
    			tagParent.setChildTagList(tagChildList);
    			//放入筛选列表中
    			for(BrandFilter categoryFilter : categoryFilterList){
    				if(categoryFilter.getRelatedId() == tagParent.getId() && categoryFilter.getType() == 0){
    					
    					categoryFilter.setProductFilterVOList(filterVOListTemp);
    					categoryFilter.setRelatedName(tagParent.getName());
    					
    				}
    			}
    		}	
    	}
    	
    	//删除没选项的筛选
    	Iterator<BrandFilter> stuIter = categoryFilterList.iterator();  
    	while (stuIter.hasNext()) {  
    		BrandFilter categoryFilter = stuIter.next();  
    		if (categoryFilter.getProductFilterVOList() == null || categoryFilter.getProductFilterVOList().size() == 0) {
    			stuIter.remove();//这里要使用Iterator的remove方法移除当前对象
    		}
    	}
    	//加入快捷筛选
    	for(BrandFilter categoryFilter : categoryFilterList){
    		//shortcutCount < 4 &&
    		if(  categoryFilter.getQuickSetting() == 1 && categoryFilter.getProductFilterVOList() != null && categoryFilter.getProductFilterVOList().size() > 0){
    			shortcutList.add(categoryFilter);
    			shortcutCount ++;
    		}
    	}
    	
    	Collection<Long> categoryIds = new ArrayList<Long>();
    	if(childCtgrId > 0 ){
    		List<Long> idListTemp = new ArrayList<Long>();
//    		 for(String  idTemp : ctgrIdArr){
//    			 idListTemp.add(Long.parseLong(idTemp));
//    	    	}	
    		idListTemp.add(childCtgrId);
    		categoryIds = idListTemp;
    	}else {
    		for(Category categoryTemp: categoryList){
    			categoryIds.addAll(categoryTemp.getCategoryIds());
    		}
    	}
    	
    	List<ProductVO> productList = new ArrayList<ProductVO>();
    	Map<String, String[]> filterMap = new HashMap<String ,String[]>();
    	Map<String, String[]> colorSizeMap = new HashMap<String ,String[]>();
    	Map<String, String[]> tagFilterMap = new HashMap<String ,String[]>();
    	
    	//解析筛选传值   格式 9:430,431,432,433\\10:15,16
    	
    	if(propGroup != null && propGroup.length() > 0){
    		String[]  propGroupArr = propGroup.split("\\\\");
    		if(propGroupArr != null && propGroupArr.length > 0 ){
    			for(String propArr : propGroupArr){
    				String[]  propGroupNatureArr = propArr.split(":");
    				if(propGroupNatureArr != null && propGroupNatureArr.length > 1){
    					String[] propValueArr = propGroupNatureArr[1].split(",");
    					if(propGroupNatureArr[0] != null && propGroupNatureArr[0].length() > 0 && ( propGroupNatureArr[0].trim().equals("7") || propGroupNatureArr[0].trim().equals("8"))){
    						for(int i = 0 ; i<propValueArr.length ; i++){
    							propValueArr[i] = propGroupNatureArr[0] + ":" + propValueArr[i];
    						}
    						colorSizeMap.put(propGroupNatureArr[0],  propValueArr);
    					}else{
    						filterMap.put(propGroupNatureArr[0], propValueArr);
    					}
    					
    				}
    			}
    		}
    		
    	}
    	//解析TAG筛选传值   格式 9:430,431,432,433\\10:15,16
    	if(tagGroup != null && tagGroup.length() > 0){
    		String[]  tagGroupArr = tagGroup.split("\\\\");
    		if(tagGroupArr != null && tagGroupArr.length > 0 ){
    			for(String tagArr : tagGroupArr){
    				String[]  tagGroupNatureArr = tagArr.split(":");
    				if(tagGroupNatureArr != null && tagGroupNatureArr.length > 1){
    					if(tagGroupNatureArr[1].length()>0){
    						String[]  tagValueArr = tagGroupNatureArr[1].split(",");
    						tagFilterMap.put(tagGroupNatureArr[0], tagValueArr);
    					}
    				}
    			}
    		}
    	}
    	
    	long userId = 0;
    	if(userDetail != null && userDetail.getUserId() > 0 ){
    		userId = userDetail.getUserId();
    	}
    	
    	//品牌晒选
    	productList = productService.getProductByFilter(userId, brandId, categoryIds, filterMap, tagFilterMap, colorSizeMap, sortType,  pageQuery ,minPrice, maxPrice, inStock, onSale, deductFlag);
    	   	
    	int totalCount = productService.getProductCountOfCategoryByFilter(userId, brandId, categoryIds, filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, inStock, onSale, deductFlag);	
    	
    	data.put("productList", getProductList16(productList));
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	data.put("pageQuery", pageQueryResult);
    	data.put("categoryId", categoryId);
    	data.put("category", category);
    	data.put("categoryList", categoryList);
    	data.put("filterMap", filterMap);
    	data.put("categoryName", category.getCategoryName());
    	data.put("categoryFilterList", categoryFilterList);
    	data.put("inStockTitle", "仅看有货");
    	data.put("onSaleTitle", "促销");
    	data.put("priceTitle", "价格区间");
    	data.put("minPriceTitle", "最低价");
    	data.put("maxPriceTitle", "最高价");
    	data.put("subCategoryTitle", "品类");
    	
//    	if(brandId > 0){
//    		
//    		data.put("brandName", brand.getBrandName());
//    		data.put("brandLogo", brand.getLogo());
//    		if(brandBusiness != null){
//    			data.put("brandBusinessFlag", 1); 		
//    		}else {
//    			data.put("brandBusinessFlag", 0);
//    		}
//    	}
    	
    	
    	
    	if (pageQueryResult.isMore()) {
    		UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbybrandfilter.json");
    		builder.set("categoryid", categoryId);
    		builder.set("childCtgrId", childCtgrId);
    		builder.set("minPrice", minPrice);
    		builder.set("maxPrice", maxPrice);
    		builder.set("propGroup", propGroup);
    		builder.set("inStock", inStock);
    		builder.set("onSale", onSale);
    		builder.set("sort", sortType.getIntValue());
    		builder.set("page", pageQueryResult.getPage() + 1);
    		builder.set("pageSize", pageQueryResult.getPageSize());
    		builder.set("guide_flag", guideFlag);
    		builder.set("deductFlag", deductFlag);
    		data.put("nextDataUrl", builder.toUri());
    	}
    	return jsonResponse.setSuccessful().setData(data);
    }
    

    @RequestMapping(value = "/productbybrand", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadProductByBrand(@RequestParam("brandId") Long brandId,
    										@RequestParam("brandName") String brandName,
                                              @RequestParam(value = "sort", defaultValue = "1") SortType sortType,
                                              @RequestParam(value = "startpage", defaultValue = "-1") int page,
                                              PageQuery pageQuery, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();

        if (page > 0) {// 兼容客户端传的是startpage参数
            pageQuery.setPage(page);
        }
        
        List<Product> prodcutList = productService.getProductOfBrand(brandId, sortType, pageQuery);
        int totalCount = productService.getProductCountOfBrand(brandId);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productList", prodcutList);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        data.put("brandId", brandId);
        data.put("brandName", brandName);
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbybrand.json");
            builder.set("brandId", brandId);
            builder.set("sort", sortType.getIntValue());
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            data.put("nextDataUrl", builder.toUri());
        }

        return jsonResponse.setSuccessful().setData(data);
    }
    
    private List<Map<String, Object>> getProductList15(List<Product> products) {
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        
        String deductStr = (String) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING).get("activityText");
        DecimalFormat    df   = new DecimalFormat("##.##");
        for (Product product : products) {
        	if(product.getDeductPercent() > 0){
        		product.setDeductDesc(deductStr + df.format(product.getDeductPercent()) + "%");
        		//product.put("deductDesc", deductStr + product.getDeductPercent() + "%");
        	}
            productList.add(product.toSimpleMap15());
        }
        return productList;
    }
    
    private List<Map<String, Object>> getProductList16(List<ProductVO> products) {
    	List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
    	String deductStr = (String) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING).get("activityText");
    	DecimalFormat    df   = new DecimalFormat("##.##");
    	for (Product product : products) {
    		if(product.getDeductPercent() > 0){
        		product.setDeductDesc(deductStr + df.format(product.getDeductPercent()) + "%");
    		}
    		productList.add(product.toSimpleMap15());
    	}
    	return productList;
    }
    

    
    @RequestMapping(value = "/productbywarehouse", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse loadProductByWarehouse(@RequestParam("loWarehouseId") Long loWarehouseId,
    											@RequestParam("warehouseName") String warehouseName,
    											@RequestParam(value = "sort", defaultValue = "1") SortType sortType,
    											@RequestParam(value = "startpage", defaultValue = "-1") int page,
    											PageQuery pageQuery, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();

        if (page > 0) {// 兼容客户端传的是startpage参数
            pageQuery.setPage(page);
        }
        
        List<Product> prodcutList = productService.getProductOfWarehouse(loWarehouseId, sortType, pageQuery);
        int totalCount = productService.getProductCountOfWarehouse(loWarehouseId);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productList", prodcutList);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        data.put("loWarehouseId", loWarehouseId);
        data.put("warehouseName", warehouseName);
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbywarehouse.json");
            builder.set("loWarehouseId", loWarehouseId);
            builder.set("sort", sortType.getIntValue());
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            data.put("nextDataUrl", builder.toUri());
        }

        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/category18")
    @ResponseBody
    public JsonResponse category18() {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<>();
        
        JSONObject category_first_navigation = globalSettingService.getJsonObject(GlobalSettingName.CATEGORY_FIRST_NAVIGATION);
        List<CategorySetting> categorySettings = categorySettingMapper.load();
        
        data.put("category_first_navigation", category_first_navigation);
        data.put("categorySettings", categorySettings);
        
        return jsonResponse.setSuccessful().setData(data);
    }
}
