package com.yujj.web.controller.mobile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ModuleType;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;
//import com.jiuyuan.web.interceptor.UriBuilder;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.ProductService;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;
import com.yujj.util.uri.UriBuilder;

@Controller
@RequestMapping("/mobile/index")
public class MobileIndexController {
    
    @Autowired
    private GlobalSettingService globalSettingService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    
    private static final Comparator<JSONObject> moduleCompare = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            Integer weight1 = o1.getInteger("weight");
            if (weight1 == null) {
                weight1 = 0;
            }
            Integer weight2 = o2.getInteger("weight");
            if (weight2 == null) {
                weight2 = 0;
            }
            return weight1.compareTo(weight2);
        }
    };

    @RequestMapping
    @ResponseBody
    public JsonResponse indexCategory() {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();

        List<JSONObject> modules = new ArrayList<JSONObject>();
        JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.INDEX_CATEGORY_MODULE);
        for (Iterator<Object> it = jsonArray.iterator(); it.hasNext();) {
            JSONObject jsonObject = (JSONObject) it.next();
            if (jsonObject.containsKey("show") && !jsonObject.getBooleanValue("show")) {
                it.remove();
                continue;
            }
            modules.add(jsonObject);
        }

        Collections.sort(modules, moduleCompare);

        for (JSONObject jsonObject : modules) {
            String moduleType = jsonObject.getString("moduleType");
            if (ModuleType.HOT.is(moduleType)) {
                assembleHotModule(jsonObject);
            } else if (ModuleType.SEASON.is(moduleType)) {
                assembleSeasonModule(jsonObject);
            } else if (ModuleType.CATEGORY.is(moduleType)) {
                assembleCategoryModule(jsonObject);
            }
        }

        addAllProductModule(modules);

        data.put("modules", modules);

        return jsonResponse.setSuccessful().setData(data);
    }

    // 增加全部商品模块
    private void addAllProductModule(List<JSONObject> modules) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "全部");
        jsonObject.put("weight", Integer.MAX_VALUE);
        jsonObject.put("detail", "");
        jsonObject.put("linkUrl", "");
        jsonObject.put("show", true);
        jsonObject.put("moduleType", ModuleType.ALL.getStringValue());

        PageQuery pageQuery = new PageQuery(1, 10);
        int totalCount = productService.getBestSellerProductCount();
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        List<Product> products = productService.getBestSellerProductList(pageQuery);
        jsonObject.put("productList", getSimpleProductList(products, false));
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/mainview/boutique.json");
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            jsonObject.put("nextDataUrl", builder.toUri());
        }

        modules.add(jsonObject);
    }

    // 人气单品
    private void assembleHotModule(JSONObject jsonObject) {
        Category hotCategory = categoryService.getCategoryById(-1L);
        Collection<Long> hotCategoryIds = hotCategory.getCategoryIds();
        List<Product> hotProducts =
            productService.getProductOfCategory(hotCategoryIds, SortType.WEIGHT_DESC, new PageQuery(1, 8));
        jsonObject.put("products", getSimpleProductList(hotProducts, true));
    }

    // 当季新品
    private void assembleSeasonModule(JSONObject jsonObject) {
        Category seasonCategory = categoryService.getCategoryById(-2L);
        List<Category> categoryList = seasonCategory.getChildCategories();

        Set<Long> seasonIds = new HashSet<Long>();
        String seasonSetting = globalSettingService.getSetting(GlobalSettingName.PROMOTION_SEASON);
        if (StringUtils.isNotBlank(seasonSetting)) {
            for (String season : StringUtils.split(seasonSetting, ",")) {
                seasonIds.add(Long.parseLong(season));
            }
        }

        jsonObject.put("categoryProducts", getCategoryProducts(categoryList, seasonIds, new PageQuery(1, 5)));
    }

    private List<Map<String, Object>> getCategoryProducts(List<Category> categoryList, Set<Long> seasonIds,
                                                          PageQuery pageQuery) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        SortType sortType = SortType.WEIGHT_DESC;
        for (Category category : categoryList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("categoryId", category.getId());
            map.put("title", category.getCategoryName());
            map.put("linkUrl", category.getCategoryUrl());
            List<Long> categoryIds = category.getCategoryIds();
            if (seasonIds.isEmpty()) {
                List<Product> products = productService.getProductOfCategory(categoryIds, sortType, pageQuery);
                map.put("products", getSimpleProductList(products, true));
            } else {
                List<Product> products =
                    productService.getProductOfCategoryProperty(categoryIds, seasonIds, sortType, pageQuery);
                map.put("products", getSimpleProductList(products, true));
            }
            result.add(map);
        }
        return result;
    }

    private void assembleCategoryModule(JSONObject jsonObject) {
        long categoryId = jsonObject.getLongValue("categoryId");
        Category category = categoryService.getCategoryById(categoryId);
        Collection<Long> categoryIds = category.getCategoryIds();
        List<Product> hotProducts =
            productService.getProductOfCategory(categoryIds, SortType.WEIGHT_DESC, new PageQuery(1, 4));
        jsonObject.put("products", getSimpleProductList(hotProducts, true));
    }

    private List<Map<String, Object>> getSimpleProductList(List<Product> products, boolean promotionImage) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Product product : products) {
        	//这里旧接口为toSimpleMap
            result.add(product.toSimpleMap15(promotionImage));
        }
        return result;
    }
    
    @RequestMapping("/recommend")
    @ResponseBody
    public JsonResponse recommend(@RequestParam("type") ModuleType moduleType, PageQuery pageQuery) {
        JsonResponse jsonResponse = new JsonResponse();
        if (moduleType == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        if (moduleType == ModuleType.HOT) {
            Category category = categoryService.getCategoryById(-1L);
            Collection<Long> categoryIds = category.getCategoryIds();
            List<Product> products = productService.getProductOfCategory(categoryIds, SortType.WEIGHT_DESC, pageQuery);
            data.put("products", getSimpleProductList(products, true));
        } else if (moduleType == ModuleType.SEASON) {

        }
        return jsonResponse.setSuccessful().setData(data);
    }
}
