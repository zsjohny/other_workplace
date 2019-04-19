/**
 *
 */
package com.jiuy.store.api.tool.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.FloorType;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.*;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.product.BrandFilter;
import com.jiuyuan.entity.product.CategoryFilter;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.IBrandNewService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.service.common.monitor.IProductMonitorService;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.ActivityPlaceMapper;
import com.store.dao.mapper.CategorySettingMapper;
import com.store.entity.Brand;
import com.store.entity.ProductVOShop;
import com.store.entity.ShopCategory;
import com.store.service.*;
import com.util.ConfigUtil;
import com.yujj.util.uri.UriBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static com.jiuyuan.entity.newentity.StoreBusiness.defaultStoreBusiness;
//import com.yujj.web.helper.JsonResponse;

/**
 * @author LWS
 *
 */
@Controller
@RequestMapping("/miniapp/mainview")
public class MobileMainController {
    private static final Logger logger = Logger.getLogger(MobileMainController.class);

    @Autowired
    private ProductServiceShop productService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private PropertyServiceShop propertyService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private VisitService visitService;


//    @Autowired
//    private BrandBusinessService brandBusinessService;

    @Autowired
    private CategorySettingMapper categorySettingMapper;

    //    @Autowired
//    private AdService adService;
//    
    @Autowired
    private ShopGlobalSettingService globalSettingService;

    @Autowired
    private HomeFacade homeFacade;

    @Autowired
    private ActivityPlaceMapper activityPlaceMapper;

    @Autowired
    private MobileMainFacade mobileMainFacade;

    @Autowired
    private IBrandNewService brandService;

    @Autowired
    private ShopProductMapper shopProductMapper;

    @Autowired
    private IProductMonitorService productMonitorService;


    // @Autowired
    // private BrandBusinessService brandBusinessService;


//    @Autowired
//    private CategorySettingMapper categorySettingMapper;

//    @RequestMapping(value = "/banner", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getBanners() {
//        JsonResponse jsonResponse = new JsonResponse();
//
//        Map<String, Object> data = new HashMap<String, Object>();
//        List<Map<String, Object>> banners = new ArrayList<Map<String, Object>>();
//        List<AdConfig> adConfigs = adService.getAdsByType(AdType.MAIN_BANNER);
//        for (AdConfig adConfig : adConfigs) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("imageUrl", adConfig.getImageUrl());
//            map.put("targetUrl", adConfig.getLinkUrl());
//            banners.add(map);
//        }
//        
//        data.put("banners", banners);
//
//        return jsonResponse.setSuccessful().setData(data);
//    }

//    /**
//     * 加载【活动换购】产品列表
//     * 
//     * @return
//     */
//    @RequestMapping(value = "/exchangeprod", method = RequestMethod.GET , produces = { "application/json;charset=UTF-8" })
//    @ResponseBody
//    public String loadExchangeProduct(PageQuery pageQuery) {
//        Category cate = categoryService.getCategoryById(36L);
//
//        Collection<Long> categoryIds = cate.getCategoryIds();
//        List<Product> productList =
//            productService.getProductOfCategory(categoryIds, SortType.CREATE_TIME_DESC, pageQuery);
//        String retString = JSON.toJSONString(productList, true);
//        logger.debug(logger);
//        return retString;
//    }


    /**
     * 2.2版本开始使用
     * App首页商品列表
     * @return
     */
    @RequestMapping(value = "/boutiqueNew/auth")
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse loadBoutiqueNew(
            @RequestParam(value = "guide_flag", required = false, defaultValue = "0") int guideFlag,
            @RequestParam(value = "startpage", defaultValue = "-1") int page,
            @RequestParam(value = "sort", defaultValue = "1") SortType sortType, PageQuery pageQuery,
            UserDetail userDetail) {
        if (page > 0) {// 兼容客户端传的是startpage参数
            pageQuery.setPage(page);
        }

        JsonResponse jsonResponse = new JsonResponse();
        List<ProductVOShop> prodcutList = productService.getUserBestSellerProductList186(userDetail, pageQuery, guideFlag);
        int totalCount = productService.getBestSellerProductCount();
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productList", getProductList15(prodcutList, userDetail));
        data.put("productPageQuery", pageQueryResult);
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/miniapp/mainview/boutique.json");
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            builder.set("sort", sortType.getIntValue());
            builder.set("guide_flag", guideFlag);
            data.put("nextDataUrl", builder.toUri());
        }

        return jsonResponse.setSuccessful().setData(data);
    }


//    @RequestMapping(value = "/category", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getCategoryList() {
//        JsonResponse jsonResponse = new JsonResponse();
//        List<Category> categoryList = categoryService.getCategoryTree(true);
//        Map<String, Object> data = new HashMap<>();
//        JSONObject category_first_navigation = globalSettingService.getJsonObject(GlobalSettingName.CATEGORY_FIRST_NAVIGATION);
//        
//        data.put("categoryList", categoryList);
//        data.put("category_first_navigation", category_first_navigation);
//        return jsonResponse.setSuccessful().setData(data);
//    }


    @RequestMapping("/category")
    @ResponseBody
    public JsonResponse category(@RequestParam(value = "guide_flag", required = false, defaultValue = "0") int guideFlag) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<>();

        List<CategorySetting> categorySettings = categorySettingMapper.load();
        JSONObject category_first_navigation = globalSettingService.getJsonObject(GlobalSettingName.CATEGORY_FIRST_NAVIGATION);
        data.put("category_first_navigation", category_first_navigation);
        data.put("categorySettings", categorySettings);


        return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 分类列表
     * @return
     */
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

    private List<Map<String, Object>> getProductList(List<Product> products) {
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        for (Product product : products) {
            productList.add(product.toSimpleMap15());
        }
        return productList;
    }


    @RequestMapping(value = "/parentCategoryList", method = RequestMethod.GET)
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse getParentCategoryList() {
        JsonResponse jsonResponse = new JsonResponse();

        Map<String, Object> data = new HashMap<String, Object>();
        List<ShopCategory> parentCategoryList = shopCategoryService.getParentCategories();

        List<ShopCategory> categorieListAll = shopCategoryService.getCategories();

        //添加子分类到列表
        for (ShopCategory category : parentCategoryList) {
            for (ShopCategory categoryTemp : categorieListAll) {
                if (categoryTemp.getParentId() == category.getId()) {
                    category.getChildCategories().add(categoryTemp);
                }
            }
        }
        //添加三级子分类
        for (ShopCategory category : parentCategoryList) {
            if (category.getChildCategories().size() > 0) {
                for (ShopCategory childCategory : category.getChildCategories()) {
                    for (ShopCategory categoryTemp : categorieListAll) {
                        if (categoryTemp.getParentId() == childCategory.getId()) {
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
     * 进货商品列表
     * 选中品类     /miniapp/mainview/productbycategory.json
     * http://dev.yujiejie.com:32080/miniapp/mainview/productbycategory.json?categoryid=113
     *
     https://storelocal.yujiejie.com/miniapp/mainview/productbycategory.json?brandId=0&categoryid=411&childCtgrId=&deductFlag=1&pageSize=50&sort=6&startpage=1&vip=0

     https://storelocal.yujiejie.com/miniapp/mainview/productbycategory.json?brandId=0&categoryid=411&childCtgrId=&deductFlag=1&pageSize=50&sort=6&startpage=2&vip=0
     * @param guideFlag
     * @param sortType 排序方式：3.价格升序 4.价格降序 5.人气升序 6.人气降序 7.最新升序 8.最新降序 11.销量升序 12.销量降序 默认为6
     * @param page
     * @param minPrice
     * @param maxPrice
     * @param propGroup
     * @param tagGroup
     * @param inStock
     * @param onSale
     * @param childCtgrId
     * @param keyWords
     * @param pageQuery
     * @param clientPlatform
     * @param userDetail
     * @return
     */
    @RequestMapping({"/productbycategory/auth", "/productbycategory"})
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse loadProductByCategory(
            @RequestParam(value = "vip", defaultValue = "0") int vip, //是否是vip商品：0非VIP，1VIP
            @RequestParam(value = "guide_flag", required = false, defaultValue = "0") int guideFlag,
            @RequestParam("categoryid") String categoryid,   //分类ID，可传多个，逗号隔开 如categoryid=208,150,33
            @RequestParam(value = "sort", defaultValue = "1") SortType sortType,//排序方式：3.价格升序 4.价格降序 5.人气升序 6.人气降序 7.最新升序 8.最新降序 11.销量升序 12.销量降序 默认为6
            @RequestParam(value = "startpage", defaultValue = "-1") int page,
            @RequestParam(value = "minPrice", defaultValue = "0") double minPrice,    //最低价
            @RequestParam(value = "maxPrice", defaultValue = "0") double maxPrice,    //最高价
            @RequestParam(value = "propGroup", required = false) String propGroup,  //属性筛选 格式 9:430,431,432,433\\10:15,16
            @RequestParam(value = "tagGroup", required = false) String tagGroup,  //标签筛选 格式 9:430,431,432,433\\10:15,16
            @RequestParam(value = "inStock", required = false) Boolean inStock,  //是否有货
            @RequestParam(value = "onSale", required = false) Boolean onSale,  //是否促销
            @RequestParam(value = "childCtgrId", defaultValue = "0") long childCtgrId,  //子分类Id
            @RequestParam(value = "keyWords", required = false) String keyWords,  //关键词搜索，商品名称模糊匹配
            @RequestParam(value = "brandType", required = false, defaultValue = "-1") int brandType,  //品牌类型：1(高档)，2(中档)
            PageQuery pageQuery, ClientPlatform clientPlatform, UserDetail<StoreBusiness> userDetail) {
        if (userDetail == null) {
            userDetail = defaultStoreBusiness();
        }
//		DefaultStoreUserDetail defaultStoreUserDetail = new DefaultStoreUserDetail();
//		StoreBusiness storeBusiness1 = new StoreBusiness();
//		storeBusiness1.setId(1895L);
//		defaultStoreUserDetail.setStoreBusiness(storeBusiness1);
//		pageQuery.setPage(1);
//		pageQuery.setPageSize(10);

        JsonResponse jsonResponse = new JsonResponse();
        logger.info("productbycategory");
        //检测商家是否是VIP身份
    	/*if(vip == 1){
    		StoreBusiness storeBusiness = userDetail.getUserDetail();
    		int shopVip = storeBusiness.getVip();
    		if(shopVip != 1){
    			logger.info("获取进货商家列表时非VIP商家不能获取VIP列表，请联系APP人员进行排查问题。");
    			return jsonResponse.setResultCode(ResultCode.VIP_PRODYCT_SHOW_NO_AUTHORITY);
    		}
    	}*/


        if (keyWords == null) {
            keyWords = "";
        }


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
//    	List<ShopCategory> parentCategoryList = categoryService.getParentCategories();
//    	
//    	data.put("parentCategoryList", parentCategoryList);
        double commissionPercentage = 0;

        if (userDetail != null) {
            StoreBusiness storeBusiness = userDetail.getUserDetail();
            if (storeBusiness != null && storeBusiness.getCommissionPercentage() > 0) {
                commissionPercentage = storeBusiness.getCommissionPercentage();
            }
        }
        data.put("commissionPercentage", commissionPercentage / 100);

        String[] ctgrIdArr = categoryid.split(",");

        if (ctgrIdArr == null || ctgrIdArr.length == 0) {
            logger.info("ctgrIdArr不能为空");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }
        List<ShopCategory> categoryList = shopCategoryService.getCategoriesByIdsArr(ctgrIdArr);
        //为分类商品补充子分类
        List<ShopCategory> childCategoryListTemp = new ArrayList<ShopCategory>();
        for (ShopCategory category : categoryList) {
            //分类的子分类列表
            childCategoryListTemp.addAll(shopCategoryService.getChildCategoryByParentId(category.getId()));
        }
        categoryList.addAll(childCategoryListTemp);

        List<ShopCategory> categorieListAll = shopCategoryService.getCategories();

        //添加子分类到列表
        for (ShopCategory category : categoryList) {
            for (ShopCategory categoryTemp : categorieListAll) {
                if (categoryTemp.getParentId() == category.getId()) {
                    category.getChildCategories().add(categoryTemp);
                }
            }
        }
        if (categoryList == null || categoryList.size() == 0) {
            logger.info("productbycategory  categoryList不能为空");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }


        Set<Long> propertyIds = new HashSet<Long>();
        Set<Long> tagIds = new HashSet<Long>();
        Set<Long> catIds = new HashSet<Long>();

        for (String idTemp : ctgrIdArr) {
            catIds.add(Long.parseLong(idTemp));
        }

        List<CategoryFilter> categoryFilterList = new ArrayList<CategoryFilter>();
        ShopCategory category = categoryList.get(0);

        //	categoryFilterList = productService.getProductFilterByCatId(category.getId());
        categoryFilterList = productService.getProductFilterByCatIds(catIds);
        //若子类筛选项为空，则用父类筛选（单一品类时）
        if (ctgrIdArr.length == 1) {
            if (category.getParentId() > 0 && (categoryFilterList == null || categoryFilterList.size() == 0)) {
                categoryFilterList = productService.getProductFilterByCatId(category.getParentId());
            }
        }

        for (CategoryFilter categoryFilter : categoryFilterList) {
            //基本属性
            categoryFilter.setSelectNum(5);
            if (categoryFilter.getType() == 1) {
                propertyIds.add(categoryFilter.getRelatedId());
            } else if (categoryFilter.getType() == 0) {
                tagIds.add(categoryFilter.getRelatedId());
            }
        }

        //拼凑全部筛选项
        List<ProductPropName> propNameList = new ArrayList<ProductPropName>();
        List<ProductPropValue> propValueListTemp;
        List<ProductFilterVO> filterVOListTemp;
        ProductFilterVO productFilterVOTemp;
        if (propertyIds.size() > 0) {
            propNameList = propertyService.getPropertyNamesListByIds(propertyIds);
            List<ProductPropValue> productPropValueList = propertyService.getPropertyValuesByNameIds(propertyIds);

            for (ProductPropName productPropName : propNameList) {
                propValueListTemp = new ArrayList<ProductPropValue>();
                filterVOListTemp = new ArrayList<ProductFilterVO>();
                for (ProductPropValue productPropValue : productPropValueList) {
                    if (productPropValue.getPropertyNameId() == productPropName.getId()) {
                        propValueListTemp.add(productPropValue);
                        productFilterVOTemp = new ProductFilterVO();
                        productFilterVOTemp.setId(productPropValue.getId());
                        productFilterVOTemp.setPropertyValue(productPropValue.getPropertyValue());
                        filterVOListTemp.add(productFilterVOTemp);
                    }
                }
                productPropName.setPropertyValueList(propValueListTemp);
                //放入筛选列表中
                for (CategoryFilter categoryFilter : categoryFilterList) {
                    if (categoryFilter.getRelatedId() == productPropName.getId() && categoryFilter.getType() == 1) {
                        //categoryFilter.setPropValueList(propValueListTemp);  //del
                        categoryFilter.setProductFilterVOList(filterVOListTemp);
                        categoryFilter.setRelatedName(productPropName.getPropertyName());
                    }
                }
            }
        }

        //快捷设置
        List<CategoryFilter> shortcutList = new ArrayList<CategoryFilter>();
        int shortcutCount = 0; //快捷筛选项个数限定，暂时不限制
        //一级分类快捷筛选添加全部品类筛选
        if (categoryList.size() > 1 || (category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0)) {
            data.put("shortcutCategory", "YES");
            shortcutCount++;
            List<ProductFilterVO> productFilterVOList = new ArrayList<ProductFilterVO>();
            ProductFilterVO productFilterVO;
            CategoryFilter categoryFilter = new CategoryFilter();
            categoryFilter.setType(2);  //品类筛选
            if (categoryList.size() > 1) {
                categoryFilter.setRelatedName("全部品类");
                for (ShopCategory ctgr : categoryList) {
                    if (ctgr.getParentId() == 0 && ctgr.getChildCategories() != null && ctgr.getChildCategories().size() > 0) {
                        for (ShopCategory categoryTemp : ctgr.getChildCategories()) {
                            productFilterVO = new ProductFilterVO();
                            productFilterVO.setId(categoryTemp.getId());
                            productFilterVO.setPropertyValue(categoryTemp.getCategoryName());
                            productFilterVO.setIconUrl(categoryTemp.getIconUrl());
                            productFilterVO.setIconOnUrl(categoryTemp.getIconOnUrl());
                            productFilterVOList.add(productFilterVO);
                        }
                    } else {
                        productFilterVO = new ProductFilterVO();
                        productFilterVO.setId(ctgr.getId());
                        productFilterVO.setPropertyValue(ctgr.getCategoryName());
                        productFilterVO.setIconUrl(ctgr.getIconUrl());
                        productFilterVO.setIconOnUrl(ctgr.getIconOnUrl());
                        productFilterVOList.add(productFilterVO);
                    }
                }
            } else {
                categoryFilter.setRelatedName("全部" + category.getCategoryName());
                for (ShopCategory categoryTemp : category.getChildCategories()) {
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
        } else {
            data.put("shortcutCategory", "NO");
        }


        data.put("shortcutList", shortcutList);
        data.put("categoryFilterList", categoryFilterList);

        //标签筛选处理
        List<Tag> tagAllList = new ArrayList<Tag>();
        List<Tag> tagParentList = new ArrayList<Tag>();
        List<Tag> tagChildList = new ArrayList<Tag>();
        if (tagIds.size() > 0) {
            tagAllList = tagService.getPropertyNamesListByIds(tagIds);
            for (Tag tagTemp : tagAllList) {
                if (tagTemp.getGroupId() == -1) {
                    tagParentList.add(tagTemp);
                }
            }

            for (Tag tagParent : tagParentList) {
                tagChildList = new ArrayList<Tag>();
                filterVOListTemp = new ArrayList<ProductFilterVO>();
                for (Tag tagTemp : tagAllList) {

                    if (tagTemp.getGroupId() == tagParent.getId()) {
                        tagChildList.add(tagTemp);
                        productFilterVOTemp = new ProductFilterVO();
                        productFilterVOTemp.setId(tagTemp.getId());
                        productFilterVOTemp.setPropertyValue(tagTemp.getName());
                        filterVOListTemp.add(productFilterVOTemp);
                    }
                }
                tagParent.setChildTagList(tagChildList);
                //放入筛选列表中
                for (CategoryFilter categoryFilter : categoryFilterList) {
                    if (categoryFilter.getRelatedId() == tagParent.getId() && categoryFilter.getType() == 0) {

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
        for (CategoryFilter categoryFilter : categoryFilterList) {
            //shortcutCount < 4 &&
            if (categoryFilter.getQuickSetting() == 1 && categoryFilter.getProductFilterVOList() != null && categoryFilter.getProductFilterVOList().size() > 0) {
                shortcutList.add(categoryFilter);
                shortcutCount++;
            }
        }

        Collection<Long> categoryIds = new ArrayList<Long>();
        if (childCtgrId > 0) {
            List<Long> idListTemp = new ArrayList<Long>();
//    		 for(String  idTemp : ctgrIdArr){
//    			 idListTemp.add(Long.parseLong(idTemp));
//    	    	}	
            idListTemp.add(childCtgrId);
            categoryIds = idListTemp;
        } else {
            for (ShopCategory categoryTemp : categoryList) {
                categoryIds.addAll(categoryTemp.getCategoryIds());
            }
        }

        List<ProductVOShop> productList = new ArrayList<ProductVOShop>();
        Map<String, String[]> filterMap = new HashMap<String, String[]>();
        Map<String, String[]> colorSizeMap = new HashMap<String, String[]>();
        Map<String, String[]> tagFilterMap = new HashMap<String, String[]>();

        //解析筛选传值   格式 9:430,431,432,433\\10:15,16

        if (propGroup != null && propGroup.length() > 0) {
            String[] propGroupArr = propGroup.split("\\\\");
            if (propGroupArr != null && propGroupArr.length > 0) {
                for (String propArr : propGroupArr) {
                    String[] propGroupNatureArr = propArr.split(":");
                    if (propGroupNatureArr != null && propGroupNatureArr.length > 1) {
                        String[] propValueArr = propGroupNatureArr[1].split(",");
                        if (propGroupNatureArr[0] != null && propGroupNatureArr[0].length() > 0 && (propGroupNatureArr[0].trim().equals("7") || propGroupNatureArr[0].trim().equals("8"))) {
                            for (int i = 0; i < propValueArr.length; i++) {
                                propValueArr[i] = propGroupNatureArr[0] + ":" + propValueArr[i];
                            }
                            colorSizeMap.put(propGroupNatureArr[0], propValueArr);
                        } else {
                            filterMap.put(propGroupNatureArr[0], propValueArr);
                        }

                    }
                }
            }

        }
        //解析TAG筛选传值   格式 9:430,431,432,433\\10:15,16
        if (tagGroup != null && tagGroup.length() > 0) {
            String[] tagGroupArr = tagGroup.split("\\\\");
            if (tagGroupArr != null && tagGroupArr.length > 0) {
                for (String tagArr : tagGroupArr) {
                    String[] tagGroupNatureArr = tagArr.split(":");
                    if (tagGroupNatureArr != null && tagGroupNatureArr.length > 1) {
                        if (tagGroupNatureArr[1].length() > 0) {
                            String[] tagValueArr = tagGroupNatureArr[1].split(",");
                            tagFilterMap.put(tagGroupNatureArr[0], tagValueArr);
                        }
                    }
                }
            }
        }
        long storeId = 0;
        if (userDetail != null && userDetail.getId() > 0) {
            storeId = userDetail.getId();
        }
        productList = productService.getProductByFilter(vip, storeId, 0, categoryIds, filterMap, tagFilterMap, colorSizeMap, sortType, pageQuery, minPrice, maxPrice, inStock, onSale, guideFlag, keyWords, brandType);
        int totalCount = productService.getProductCountOfCategoryByFilter(vip, storeId, 0, categoryIds, filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, inStock, onSale, guideFlag, keyWords, brandType);
        //获取商品uploadNum
//    	if(productList != null && productList.size() > 0 ){
//    		Set<Long> productIdSet = new HashSet<Long>();
//    		for(ProductVOShop productVO : productList){
//    			productIdSet.add(productVO.getId());
//    		}
//    		List<ShopProduct> shopProductList = shopProductService.getShopProductListByProIds(storeId , productIdSet);
//    		
//    		
//    		logger.info("开始获得商品该店家上传数量");
//    		for(ProductVOShop productVO : productList){
//    			long productVOId = productVO.getId() ;
//    			for(ShopProduct shopProduct : shopProductList){
//    				Long productId = shopProduct.getProductId();
//    				if(productId != null && productId != 0 && productVOId == productId){
//    					productVO.setUploadNum(productVO.getUploadNum() + 1);
//    				}
//    			}
//    		}
//    		for(ProductVOShop productVO : productList){
//    			logger.info("productVO.getUploadNum():"+productVO.getUploadNum());
//    		}
//    		
//    		
//    	}

        data.put("productList", getProductList15(productList, userDetail));
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        data.put("categoryId", categoryid);
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
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbycategory.json");
            builder.set("categoryid", categoryid);
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
            data.put("nextDataUrl", builder.toUri());
        }


//    	logger.info("==========================");
//    	logger.info("==========================");
//    	logger.info("==========================");
//    	logger.info("==========================");
//    	logger.info("============进货获取商家列表接口返回数据，data："+data);
//    	logger.info("==========================");
//    	logger.info("==========================");
//    	logger.info("==========================");
//    	logger.info("==========================");
//    	logger.info("==========================");
//    	logger.info("==========================");


        return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 进货商品列表
     * 选中标签     /miniapp/mainview/productbyTag.json
     * http://dev.yujiejie.com:32080/miniapp/mainview/productbyTag.json?tagId=113
     * @param guideFlag
     * @param tagId
     * @param sortType 排序方式：3.价格升序 4.价格降序 5.人气升序 6.人气降序 7.最新升序 8.最新降序 11.销量升序 12.销量降序 默认为6
     * @param page
     * @param minPrice
     * @param maxPrice
     * @param propGroup
     * @param tagGroup
     * @param inStock
     * @param onSale
     * @param childCtgrId
     * @param keyWords
     * @param pageQuery
     * @param clientPlatform
     * @param userDetail
     * @return
     */
    @RequestMapping({"/productByTag", "/productByTag/auth"})
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse loadProductByTag(
            @RequestParam(value = "vip", defaultValue = "0") int vip, //是否是vip商品：0非VIP，1VIP
            @RequestParam(value = "guide_flag", required = false, defaultValue = "0") int guideFlag,
            @RequestParam("tagId") String tagId,   //分类ID，可传多个，逗号隔开 如categoryid=208,150,33
            @RequestParam(value = "sort", defaultValue = "1") SortType sortType,//排序方式：3.价格升序 4.价格降序 5.人气升序 6.人气降序 7.最新升序 8.最新降序 11.销量升序 12.销量降序 默认为6
            @RequestParam(value = "startpage", defaultValue = "-1") int page,
            @RequestParam(value = "minPrice", defaultValue = "0") double minPrice,    //最低价
            @RequestParam(value = "maxPrice", defaultValue = "0") double maxPrice,    //最高价
            @RequestParam(value = "propGroup", required = false) String propGroup,  //属性筛选 格式 9:430,431,432,433\\10:15,16
            @RequestParam(value = "tagGroup", required = false) String tagGroup,  //标签筛选 格式 9:430,431,432,433\\10:15,16
            @RequestParam(value = "inStock", required = false) Boolean inStock,  //是否有货
            @RequestParam(value = "onSale", required = false) Boolean onSale,  //是否促销
            @RequestParam(value = "childCtgrId", defaultValue = "0") long childCtgrId,  //子分类Id
            @RequestParam(value = "keyWords", required = false) String keyWords,  //关键词搜索，商品名称模糊匹配
            @RequestParam(value = "brandType", required = false, defaultValue = "-1") int brandType,  //品牌类型：1(高档)，2(中档)
            PageQuery pageQuery, ClientPlatform clientPlatform, UserDetail userDetail) {
        if (userDetail == null) {
            userDetail = defaultStoreBusiness();
        }
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();

        String[] tagIdArr = tagId.split(",");
        logger.info("productByTag");
        if (tagIdArr == null || tagIdArr.length == 0) {
            logger.info("tagIdArr不能为空");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        List<Long> tagIds = new ArrayList<Long>();
        for (String id : tagIdArr) {
            tagIds.add(Long.parseLong(id));
        }

        List<Tag> tagList = tagService.getPropertyNamesListByIds(tagIds);
        data.put("tagList", tagList);

        if (tagIds == null || tagIds.size() == 0) {
            logger.info("tagIds不能为空");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        if (keyWords == null) {
            keyWords = "";
        }

        Map<String, String[]> filterMap = new HashMap<String, String[]>();
        Map<String, String[]> colorSizeMap = new HashMap<String, String[]>();
        Map<String, String[]> tagFilterMap = new HashMap<String, String[]>();

        //解析筛选传值   格式 9:430,431,432,433\\10:15,16

        if (propGroup != null && propGroup.length() > 0) {
            String[] propGroupArr = propGroup.split("\\\\");
            if (propGroupArr != null && propGroupArr.length > 0) {
                for (String propArr : propGroupArr) {
                    String[] propGroupNatureArr = propArr.split(":");
                    if (propGroupNatureArr != null && propGroupNatureArr.length > 1) {
                        String[] propValueArr = propGroupNatureArr[1].split(",");
                        if (propGroupNatureArr[0] != null && propGroupNatureArr[0].length() > 0 && (propGroupNatureArr[0].trim().equals("7") || propGroupNatureArr[0].trim().equals("8"))) {
                            for (int i = 0; i < propValueArr.length; i++) {
                                propValueArr[i] = propGroupNatureArr[0] + ":" + propValueArr[i];
                            }
                            colorSizeMap.put(propGroupNatureArr[0], propValueArr);
                        } else {
                            filterMap.put(propGroupNatureArr[0], propValueArr);
                        }

                    }
                }
            }

        }
        //解析TAG筛选传值   格式 9:430,431,432,433\\10:15,16
        if (tagGroup != null && tagGroup.length() > 0) {
            String[] tagGroupArr = tagGroup.split("\\\\");
            if (tagGroupArr != null && tagGroupArr.length > 0) {
                for (String tagArr : tagGroupArr) {
                    String[] tagGroupNatureArr = tagArr.split(":");
                    if (tagGroupNatureArr != null && tagGroupNatureArr.length > 1) {
                        if (tagGroupNatureArr[1].length() > 0) {
                            String[] tagValueArr = tagGroupNatureArr[1].split(",");
                            tagFilterMap.put(tagGroupNatureArr[0], tagValueArr);
                        }
                    }
                }
            }
        }

        long storeId = 0;
        if (userDetail != null && userDetail.getId() > 0) {
            storeId = userDetail.getId();
        }
        List<ProductVOShop> productList = productService.getProductByFilterAndTagIds(vip, storeId, 0, tagIds, filterMap, tagFilterMap,
                colorSizeMap, sortType, pageQuery, minPrice, maxPrice, inStock, onSale, guideFlag, keyWords, brandType);
        int totalCount = productService.getProductCountOfCategoryByFilterAndTagIds(vip, storeId, 0, tagIds, filterMap, tagFilterMap,
                colorSizeMap, minPrice, maxPrice, inStock, onSale, guideFlag, keyWords, brandType);
        data.put("productList", getProductList15(productList, userDetail));
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 进货中商品列表
     * 进货  未选择品类 /miniapp/mainview/productbybrandfilter.json
     * @param guideFlag
     * @param brandId
     * @param sortType 排序方式：3.价格升序 4.价格降序 5.人气升序 6.人气降序 7.最新升序 8.最新降序 11.销量升序 12.销量降序 默认为6
     * @param page
     * @param minPrice
     * @param maxPrice
     * @param propGroup
     * @param tagGroup
     * @param inStock
     * @param onSale
     * @param childCtgrId
     * @param keyWords
     * @param pageQuery
     * @param clientPlatform
     * @param userDetail
     * @return
     */
    @RequestMapping({"/productbybrandfilter/auth", "/productbybrandfilter"})
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse loadProductFilterByBrand(
            @RequestParam(value = "vip", defaultValue = "0") int vip, //vip：0非VIP，1VIP
            @RequestParam(value = "guide_flag", required = false, defaultValue = "0") int guideFlag,
            @RequestParam("brandId") long brandId,   //品牌ID
            @RequestParam(value = "sort", defaultValue = "1") SortType sortType,//排序方式：3.价格升序 4.价格降序 5.人气升序 6.人气降序 7.最新升序 8.最新降序 11.销量升序 12.销量降序 默认为6
            @RequestParam(value = "startpage", defaultValue = "-1") int page,
            @RequestParam(value = "minPrice", defaultValue = "0") double minPrice,    //最低价
            @RequestParam(value = "maxPrice", defaultValue = "0") double maxPrice,    //最高价
            @RequestParam(value = "propGroup", required = false) String propGroup,  //属性筛选 格式 9:430,431,432,433\\10:15,16
            @RequestParam(value = "tagGroup", required = false) String tagGroup,  //标签筛选 格式 9:430,431,432,433\\10:15,16
            @RequestParam(value = "inStock", required = false) Boolean inStock,  //是否有货
            @RequestParam(value = "onSale", required = false) Boolean onSale,  //是否促销
            @RequestParam(value = "childCtgrId", defaultValue = "0") long childCtgrId,  //子分类Id
            @RequestParam(value = "categoryid", defaultValue = "") String categoryid,   //分类ID
            @RequestParam(value = "keyWords", required = false) String keyWords,  //关键词搜索，商品名称模糊匹配
            @RequestParam(value = "brandType", required = false, defaultValue = "-1") int brandType,  //品牌类型：1(高档)，2(中档)
            PageQuery pageQuery, ClientPlatform clientPlatform, UserDetail userDetail) {

        if (userDetail == null) {
            userDetail = defaultStoreBusiness();
        }
        JsonResponse jsonResponse = new JsonResponse();
        try {
            logger.info("productbybrandfilter");
            visitService.addVisitHistory(userDetail.getId(), 0, new Long[]{brandId}, 1);
            //    	onSale = false; //品牌筛选没有促销筛选项
            //    	inStock = true;
            if (keyWords == null) {
                keyWords = "";
            }
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
            //    	Brand brand = new Brand();
            Brand brand = null;
            if (brandId != 0) {
                brand = brandService.getBrand(brandId);
                if (brand == null) {
                    logger.info("获取品牌信息为空，请尽快排查问题brandId：" + brandId);
                    return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
                }
            }
            //    	BrandBusiness brandBusiness = brandBusinessService.getByBrandId(brandId);
            //    	if(parentCategoryId > 0){
            //    		Category category = getCategoryById
            //    	}
            //所有可用分类
            List<ShopCategory> categorieListAll = shopCategoryService.getCategories();
            List<ShopCategory> categoryList = new ArrayList<ShopCategory>();
            String[] ctgrIdArr;
            if (categoryid == null || categoryid.length() == 0 || categoryid.equals("0")) {
                categoryid = "";
                categoryList = shopCategoryService.getParentCategories();

                for (ShopCategory category : categoryList) {
                    categoryid += category.getId() + ",";
                }
                if (categoryid.length() > 0) {
                    categoryid = categoryid.substring(0, categoryid.length() - 1);
                }
                ctgrIdArr = categoryid.split(",");

                //为分类商品补充子分类
                List<ShopCategory> childCategoryListTemp = new ArrayList<ShopCategory>();
                for (ShopCategory category : categoryList) {
                    //分类的子分类列表
                    childCategoryListTemp.addAll(shopCategoryService.getChildCategoryByParentId(category.getId()));
                }
                categoryList.addAll(childCategoryListTemp);
            } else {
                ctgrIdArr = categoryid.split(",");
                categoryList = shopCategoryService.getCategoriesByIdsArr(ctgrIdArr);
                //为分类商品补充子分类
                List<ShopCategory> childCategoryListTemp = new ArrayList<ShopCategory>();
                for (ShopCategory category : categoryList) {
                    //分类的子分类列表
                    childCategoryListTemp.addAll(shopCategoryService.getChildCategoryByParentId(category.getId()));
                }
                categoryList.addAll(childCategoryListTemp);

            }
            if (ctgrIdArr == null || ctgrIdArr.length == 0) {
                logger.info("ctgrIdArr不能为空");
                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
            }
            List<ShopCategory> parentCategoryList = shopCategoryService.getParentCategories();
            //添加子分类到列表
            for (ShopCategory category : categoryList) {
                for (ShopCategory categoryTemp : categorieListAll) {
                    if (categoryTemp.getParentId() == category.getId()) {
                        category.getChildCategories().add(categoryTemp);
                    }
                }
            }
            if (categoryList == null || categoryList.size() == 0) {
                logger.info("productbybrandfilter  categoryList不能为空");
                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
            }
            Set<Long> propertyIds = new HashSet<Long>();
            Set<Long> tagIds = new HashSet<Long>();
            Set<Long> catIds = new HashSet<Long>();
            for (String idTemp : ctgrIdArr) {
                catIds.add(Long.parseLong(idTemp));
            }
            List<BrandFilter> categoryFilterList = new ArrayList<BrandFilter>();
            ShopCategory category = categoryList.get(0);
            //	categoryFilterList = productService.getProductFilterByCatId(category.getId());
            categoryFilterList = productService.getProductFilterByBrandIds(catIds);
            //    	categoryFilterList = productService.getProductFilterByCatIds(catIds);
            //    	//若子类筛选项为空，则用父类筛选（单一品类时）
            //    	if(ctgrIdArr.length == 1){
            //    		if(category.getParentId()> 0 && (categoryFilterList == null || categoryFilterList.size() == 0 )){
            //    			categoryFilterList = productService.getProductFilterByCatId(category.getParentId());
            //    		}
            //    	}
            for (BrandFilter categoryFilter : categoryFilterList) {
                //基本属性
                categoryFilter.setSelectNum(5);
                if (categoryFilter.getType() == 1) {
                    propertyIds.add(categoryFilter.getRelatedId());
                } else if (categoryFilter.getType() == 0) {
                    tagIds.add(categoryFilter.getRelatedId());
                }
            }
            //拼凑全部筛选项
            List<ProductPropName> propNameList = new ArrayList<ProductPropName>();
            List<ProductPropValue> propValueListTemp;
            List<ProductFilterVO> filterVOListTemp;
            ProductFilterVO productFilterVOTemp;
            if (propertyIds.size() > 0) {
                propNameList = propertyService.getPropertyNamesListByIds(propertyIds);
                List<ProductPropValue> productPropValueList = propertyService.getPropertyValuesByNameIds(propertyIds);

                for (ProductPropName productPropName : propNameList) {
                    propValueListTemp = new ArrayList<ProductPropValue>();
                    filterVOListTemp = new ArrayList<ProductFilterVO>();
                    for (ProductPropValue productPropValue : productPropValueList) {
                        if (productPropValue.getPropertyNameId() == productPropName.getId()) {
                            propValueListTemp.add(productPropValue);
                            productFilterVOTemp = new ProductFilterVO();
                            productFilterVOTemp.setId(productPropValue.getId());
                            productFilterVOTemp.setPropertyValue(productPropValue.getPropertyValue());
                            filterVOListTemp.add(productFilterVOTemp);
                        }
                    }
                    productPropName.setPropertyValueList(propValueListTemp);
                    //放入筛选列表中
                    for (BrandFilter categoryFilter : categoryFilterList) {
                        if (categoryFilter.getRelatedId() == productPropName.getId() && categoryFilter.getType() == 1) {
                            //categoryFilter.setPropValueList(propValueListTemp);  //del
                            categoryFilter.setProductFilterVOList(filterVOListTemp);
                            categoryFilter.setRelatedName(productPropName.getPropertyName());
                        }
                    }
                }
            }
            //快捷设置
            List<BrandFilter> shortcutList = new ArrayList<BrandFilter>();
            int shortcutCount = 0; //快捷筛选项个数限定，暂时不限制
            //一级分类快捷筛选添加全部品类筛选
            if (categoryList.size() > 1 || (category.getParentId() == 0 && category.getChildCategories() != null
                    && category.getChildCategories().size() > 0)) {
                data.put("shortcutCategory", "YES");
                shortcutCount++;
                List<ProductFilterVO> productFilterVOList = new ArrayList<ProductFilterVO>();
                ProductFilterVO productFilterVO;
                BrandFilter categoryFilter = new BrandFilter();
                categoryFilter.setType(2); //品类筛选
                if (categoryList.size() > 1) {
                    categoryFilter.setRelatedName("全部品类");
                    for (ShopCategory ctgr : parentCategoryList) {
                        if (ctgr.getParentId() == 0 && ctgr.getChildCategories() != null
                                && ctgr.getChildCategories().size() > 0) {
                            //    					for(Category categoryTemp : ctgr.getChildCategories()){
                            //    						productFilterVO = new ProductFilterVO();
                            //    						productFilterVO.setId(categoryTemp.getId());
                            //    						productFilterVO.setPropertyValue(categoryTemp.getCategoryName());
                            //    						productFilterVO.setIconUrl(categoryTemp.getIconUrl());
                            //    						productFilterVO.setIconOnUrl(categoryTemp.getIconOnUrl());
                            //    						productFilterVOList.add(productFilterVO);
                            //    					}
                        } else {
                            productFilterVO = new ProductFilterVO();
                            productFilterVO.setId(ctgr.getId());
                            productFilterVO.setPropertyValue(ctgr.getCategoryName());
                            productFilterVO.setIconUrl(ctgr.getIconUrl());
                            productFilterVO.setIconOnUrl(ctgr.getIconOnUrl());
                            productFilterVOList.add(productFilterVO);
                        }
                    }
                } else {
                    categoryFilter.setRelatedName("全部" + category.getCategoryName());
                    for (ShopCategory categoryTemp : category.getChildCategories()) {
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
            } else {
                data.put("shortcutCategory", "NO");
            }
            data.put("shortcutList", shortcutList);
            data.put("categoryFilterList", categoryFilterList);
            //标签筛选处理
            List<Tag> tagAllList = new ArrayList<Tag>();
            List<Tag> tagParentList = new ArrayList<Tag>();
            List<Tag> tagChildList = new ArrayList<Tag>();
            if (tagIds.size() > 0) {
                tagAllList = tagService.getPropertyNamesListByIds(tagIds);
                for (Tag tagTemp : tagAllList) {
                    if (tagTemp.getGroupId() == -1) {
                        tagParentList.add(tagTemp);
                    }
                }

                for (Tag tagParent : tagParentList) {
                    tagChildList = new ArrayList<Tag>();
                    filterVOListTemp = new ArrayList<ProductFilterVO>();
                    for (Tag tagTemp : tagAllList) {

                        if (tagTemp.getGroupId() == tagParent.getId()) {
                            tagChildList.add(tagTemp);
                            productFilterVOTemp = new ProductFilterVO();
                            productFilterVOTemp.setId(tagTemp.getId());
                            productFilterVOTemp.setPropertyValue(tagTemp.getName());
                            filterVOListTemp.add(productFilterVOTemp);
                        }
                    }
                    tagParent.setChildTagList(tagChildList);
                    //放入筛选列表中
                    for (BrandFilter categoryFilter : categoryFilterList) {
                        if (categoryFilter.getRelatedId() == tagParent.getId() && categoryFilter.getType() == 0) {

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
                if (categoryFilter.getProductFilterVOList() == null
                        || categoryFilter.getProductFilterVOList().size() == 0) {
                    stuIter.remove();//这里要使用Iterator的remove方法移除当前对象
                }
            }
            //加入快捷筛选
            for (BrandFilter categoryFilter : categoryFilterList) {
                //shortcutCount < 4 &&
                if (categoryFilter.getQuickSetting() == 1 && categoryFilter.getProductFilterVOList() != null
                        && categoryFilter.getProductFilterVOList().size() > 0) {
                    shortcutList.add(categoryFilter);
                    shortcutCount++;
                }
            }
            Collection<Long> categoryIds = new ArrayList<Long>();
            if (childCtgrId > 0) {
                List<Long> idListTemp = new ArrayList<Long>();
                //    		 for(String  idTemp : ctgrIdArr){
                //    			 idListTemp.add(Long.parseLong(idTemp));
                //    	    	}
                idListTemp.add(childCtgrId);
                categoryIds = idListTemp;
            } else {
                for (ShopCategory categoryTemp : categoryList) {
                    categoryIds.addAll(categoryTemp.getCategoryIds());
                }
            }
            List<ProductVOShop> productList = new ArrayList<ProductVOShop>();
            Map<String, String[]> filterMap = new HashMap<String, String[]>();
            Map<String, String[]> colorSizeMap = new HashMap<String, String[]>();
            Map<String, String[]> tagFilterMap = new HashMap<String, String[]>();
            if (propGroup != null && propGroup.length() > 0) {
                String[] propGroupArr = propGroup.split("\\\\");
                if (propGroupArr != null && propGroupArr.length > 0) {
                    for (String propArr : propGroupArr) {
                        String[] propGroupNatureArr = propArr.split(":");
                        if (propGroupNatureArr != null && propGroupNatureArr.length > 1) {
                            String[] propValueArr = propGroupNatureArr[1].split(",");
                            if (propGroupNatureArr[0] != null && propGroupNatureArr[0].length() > 0
                                    && (propGroupNatureArr[0].trim().equals("7")
                                    || propGroupNatureArr[0].trim().equals("8"))) {
                                for (int i = 0; i < propValueArr.length; i++) {
                                    propValueArr[i] = propGroupNatureArr[0] + ":" + propValueArr[i];
                                }
                                colorSizeMap.put(propGroupNatureArr[0], propValueArr);
                            } else {
                                filterMap.put(propGroupNatureArr[0], propValueArr);
                            }

                        }
                    }
                }

            }
            //解析TAG筛选传值   格式 9:430,431,432,433\\10:15,16
            if (tagGroup != null && tagGroup.length() > 0) {
                String[] tagGroupArr = tagGroup.split("\\\\");
                if (tagGroupArr != null && tagGroupArr.length > 0) {
                    for (String tagArr : tagGroupArr) {
                        String[] tagGroupNatureArr = tagArr.split(":");
                        if (tagGroupNatureArr != null && tagGroupNatureArr.length > 1) {
                            if (tagGroupNatureArr[1].length() > 0) {
                                String[] tagValueArr = tagGroupNatureArr[1].split(",");
                                tagFilterMap.put(tagGroupNatureArr[0], tagValueArr);
                            }
                        }
                    }
                }
            }
            long storeId = 0;
            if (userDetail != null && userDetail.getId() > 0) {
                storeId = userDetail.getId();
            }
            productList = productService.getProductByFilter(vip, storeId, brandId, categoryIds, filterMap, tagFilterMap,
                    colorSizeMap, sortType, pageQuery, minPrice, maxPrice, inStock, onSale, guideFlag, keyWords, brandType);
            int totalCount = productService.getProductCountOfCategoryByFilter(vip, storeId, brandId, categoryIds,
                    filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, inStock, onSale, guideFlag, keyWords, brandType);
            //获取商品uploadNum
            if (productList != null && productList.size() > 0) {
                Set<Long> productIdSet = new HashSet<Long>();
                for (ProductVOShop productVO : productList) {
                    productIdSet.add(productVO.getId());

                }
                List<ShopProduct> shopProductList = shopProductService.getShopProductListByProIds(storeId,
                        productIdSet);
                for (ProductVOShop productVO : productList) {
                    for (ShopProduct shopProduct : shopProductList) {
                        if (shopProduct.getProductId() != null && productVO.getId() == shopProduct.getProductId()) {
                            productVO.setUploadNum(productVO.getUploadNum() + 1);
                        }
                    }

                }

            }
            data.put("productList", getProductList15(productList, userDetail));
            PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
            data.put("pageQuery", pageQueryResult);
            data.put("categoryId", categoryid);
            data.put("category", category);
            data.put("categoryList", categoryList);
            data.put("filterMap", filterMap);
            if (brandId == 0) {
                data.put("categoryName", "");
            } else {
                data.put("categoryName", category.getCategoryName());
            }
            data.put("categoryFilterList", categoryFilterList);
            data.put("inStockTitle", "仅看有货");
            data.put("onSaleTitle", "促销");
            data.put("priceTitle", "价格区间");
            data.put("minPriceTitle", "最低价");
            data.put("maxPriceTitle", "最高价");
            data.put("subCategoryTitle", "品类");
            if (brand != null) {
                data.put("brandName", brand.getBrandName());
                data.put("brandLogo", brand.getLogo());
            } else {
                data.put("brandName", "");
                data.put("brandLogo", "");
            }
            if (pageQueryResult.isMore()) {
                UriBuilder builder = new UriBuilder(
                        Constants.SERVER_URL + "/mobile/mainview/productbybrandfilter.json");
                builder.set("categoryid", categoryid);
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
                data.put("nextDataUrl", builder.toUri());
            }
            return jsonResponse.setSuccessful().setData(data);

        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }

    }

//
//    @RequestMapping(value = "/productbybrand", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse loadProductByBrand(@RequestParam("brandId") Long brandId,
//    										@RequestParam("brandName") String brandName,
//                                              @RequestParam(value = "sort", defaultValue = "1") SortType sortType,
//                                              @RequestParam(value = "startpage", defaultValue = "-1") int page,
//                                              PageQuery pageQuery, ClientPlatform clientPlatform) {
//        JsonResponse jsonResponse = new JsonResponse();
//
//        if (page > 0) {// 兼容客户端传的是startpage参数
//            pageQuery.setPage(page);
//        }
//        
//        List<Product> prodcutList = productService.getProductOfBrand(brandId, sortType, pageQuery);
//        int totalCount = productService.getProductCountOfBrand(brandId);
//
//        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("productList", prodcutList);
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
//        data.put("pageQuery", pageQueryResult);
//        data.put("brandId", brandId);
//        data.put("brandName", brandName);
//        if (pageQueryResult.isMore()) {
//            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbybrand.json");
//            builder.set("brandId", brandId);
//            builder.set("sort", sortType.getIntValue());
//            builder.set("page", pageQueryResult.getPage() + 1);
//            builder.set("pageSize", pageQueryResult.getPageSize());
//            data.put("nextDataUrl", builder.toUri());
//        }
//
//        return jsonResponse.setSuccessful().setData(data);
//    }


    private List<Map<String, Object>> getProductList15(List<ProductVOShop> products, UserDetail userDetail) {
        long storeId = 0;
        if (userDetail != null && userDetail.getId() > 0) {
            storeId = userDetail.getId();
        }

//    	incomeAssembler.assemble(products, userDetail);
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        for (ProductVOShop product : products) {
            Map<String, Object> productMap = product.toSimpleMap15();
            //填充店家上传该商品数量
            long productId = (long) productMap.get("id");
            Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("store_id", storeId).eq("product_id", productId);
            List<ShopProduct> shopProductList = shopProductMapper.selectList(wrapper);
            productMap.put("uploadNum", shopProductList.size());
//	    	logger.info("uploadNum-----------shopProductList.size():"+shopProductList.size());
            productList.add(productMap);
        }

        productMonitorService.fillProductMonitor(productList, "id");

        return productList;
    }
//    
//    @RequestMapping(value = "/productbywarehouse", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse loadProductByWarehouse(@RequestParam("loWarehouseId") Long loWarehouseId,
//    											@RequestParam("warehouseName") String warehouseName,
//    											@RequestParam(value = "sort", defaultValue = "1") SortType sortType,
//    											@RequestParam(value = "startpage", defaultValue = "-1") int page,
//    											PageQuery pageQuery, ClientPlatform clientPlatform) {
//        JsonResponse jsonResponse = new JsonResponse();
//
//        if (page > 0) {// 兼容客户端传的是startpage参数
//            pageQuery.setPage(page);
//        }
//        
//        List<Product> prodcutList = productService.getProductOfWarehouse(loWarehouseId, sortType, pageQuery);
//        int totalCount = productService.getProductCountOfWarehouse(loWarehouseId);
//
//        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("productList", prodcutList);
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
//        data.put("pageQuery", pageQueryResult);
//        data.put("loWarehouseId", loWarehouseId);
//        data.put("warehouseName", warehouseName);
//        if (pageQueryResult.isMore()) {
//            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/productbywarehouse.json");
//            builder.set("loWarehouseId", loWarehouseId);
//            builder.set("sort", sortType.getIntValue());
//            builder.set("page", pageQueryResult.getPage() + 1);
//            builder.set("pageSize", pageQueryResult.getPageSize());
//            data.put("nextDataUrl", builder.toUri());
//        }
//
//        return jsonResponse.setSuccessful().setData(data);
//    }
//    

    /**
     * 活动专场
     * activity_place_id=20为首页专场
     * https://storelocal.yujiejie.com/miniapp/mainview/activity/place/show.json?activity_place_id=20&page=1&pageSize=20
     * @param activityPlaceId
     * @param pageQuery
     * @return
     */
    @RequestMapping({"/activity/place/show/auth", "/activity/place/show"})
    @ResponseBody
    public JsonResponse homeFloor(@RequestParam(value = "activity_place_id") Long activityPlaceId, UserDetail userDetail,
                                  PageQuery pageQuery) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        List<JSONObject> modules = new ArrayList<JSONObject>();

        // android 没有点击切档导致的activityPlaceId为0问题     20180326 by dongzhong
//		if (0 == activityPlaceId) {
//			return jsonResponse.setSuccessful().setData(data);
//		}

//		DefaultStoreUserDetail defaultStoreUserDetail = new DefaultStoreUserDetail();
//		StoreBusiness storeBusiness = new StoreBusiness();
//		storeBusiness.setId(192L);
//		defaultStoreUserDetail.setStoreBusiness(storeBusiness);
//		pageQuery.setPage(1);
//		pageQuery.setPageSize(10);
        int totalCount = homeFacade.getHomeFloorCount187(FloorType.ACTIVITY_PLACE, activityPlaceId);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);

        modules = homeFacade.getJsonList188(pageQuery, FloorType.ACTIVITY_PLACE, activityPlaceId, userDetail);
        data.put("modules", modules);
        boolean hasTag = homeFacade.getHasTag(FloorType.ACTIVITY_PLACE, activityPlaceId, userDetail);
        data.put("hasTag", hasTag);
        ActivityPlace activityPlace = activityPlaceMapper.getById(activityPlaceId);
        if (activityPlace == null) {
            return jsonResponse.setResultCode(ResultCode.ACTIVITY_PALCE_IS_DELETE);
        }

        data.put("activity_place_name", activityPlace != null ? activityPlace.getName() : "");
        return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 2.3版本开始使用
     * App首页标签列表和对应的商品列表
     * @return
     */
    @RequestMapping(value = "/getTagProduct/auth")
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse getTagProduct(@RequestParam(value = "tagId", required = false, defaultValue = "0") long tagId,
                                      PageQuery pageQuery, UserDetail userDetail) {
        logger.info("=================App首页标签列表和对应的商品列表tagId:" + tagId + ",userDetail:" + userDetail + ",pageQuery" + pageQuery);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            Map<String, Object> data = mobileMainFacade.getMobileMainTagsAndProducts(tagId, userDetail.getId(), pageQuery);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.getMessage();
            return jsonResponse.setError("App首页标签列表和对应的商品列表error" + e.getMessage());
        }
    }


    /**
     * 根据类目品牌标签获取商品列表
     * @param groupType 4：类目、 8：品牌 、14:标签
     * @param targetId  跳转目标ID：
     * @param current
     * @param size
     * @param brandIds 品牌ids
     * @param dynamicPotoId 动态属性ids 11_37
     * @param userDetail 用户信息
     * @date: 2018/5/10 18:26
     * @author: Aison
     */
    @RequestMapping({"/getProductListByGroupId", "/getProductListByGroupId/auth"})
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse getProductList(@RequestParam(value = "groupType", required = true) int groupType,
                                       @RequestParam(value = "targetId", required = true) int targetId,
                                       @RequestParam(value = "current", required = false, defaultValue = "1") int current,
                                       @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                                       String brandIds, String dynamicPotoId,
                                       UserDetail userDetail) {


        JsonResponse jsonResponse = new JsonResponse();
        try {
            SmallPage data = mobileMainFacade.getProductListByGroupId(groupType, targetId, current, size, brandIds, dynamicPotoId);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError("App首页标签列表和对应的商品列表error" + e.getMessage());
        }
    }


    /**
     * 热搜标签接口
     * /miniapp/mainview/getTagsByPriority.json
     * tagNums 默认为5
     */
    @RequestMapping("/getTagsByPriority/auth")
    @ResponseBody
    public JsonResponse getTagsByPriority(@RequestParam(value = "tagNums", required = false, defaultValue = "5") int tagNums,
                                          UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
        }
        List<Map<String, Object>> data = tagService.getTagsByPriority(tagNums);
        return jsonResponse.setSuccessful().setData(data);
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 2.1版本之前使用,2.2版本开始后不再使用
     * App首页买手推荐商品列表
     * @return
     */
    @RequestMapping(value = "/boutique")
    @ResponseBody
    public JsonResponse loadBoutique(@RequestParam(value = "guide_flag", required = false, defaultValue = "0") int guideFlag,
                                     @RequestParam(value = "startpage", defaultValue = "-1") int page, @RequestParam(value = "sort", defaultValue = "1") SortType sortType,
                                     PageQuery pageQuery, UserDetail userDetail) {
        if (page > 0) {// 兼容客户端传的是startpage参数
            pageQuery.setPage(page);
        }
//		pageQuery.setPage(1);
//		pageQuery.setPageSize(50);

        JsonResponse jsonResponse = new JsonResponse();
        List<ProductVOShop> prodcutList = productService.getUserBestSellerProductList186(userDetail, pageQuery, guideFlag);
        int totalCount = productService.getBestSellerProductCount();
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productList", getProductList15(prodcutList, userDetail));
        data.put("productPageQuery", pageQueryResult);
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/miniapp/mainview/boutique.json");
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            builder.set("sort", sortType.getIntValue());
            builder.set("guide_flag", guideFlag);
            data.put("nextDataUrl", builder.toUri());
        }

        return jsonResponse.setSuccessful().setData(data);
    }


    @RequestMapping("/test/test")
    public String testt() {

        System.out.println(ConfigUtil.getMemcachedServers("servers"));
        return null;
    }


}
