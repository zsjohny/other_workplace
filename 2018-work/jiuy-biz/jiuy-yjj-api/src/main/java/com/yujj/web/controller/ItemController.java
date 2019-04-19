package com.yujj.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.ProductService;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @RequestMapping
    public String index(@RequestParam("id") long categoryId,
                        @RequestParam(value = "cid", defaultValue = "0") long childId, Map<String, Object> model) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return Constants.ERROR_PAGE_NOT_FOUND;
        }
        model.put("category", category);

        return "item/index";
    }
    
    @RequestMapping("/filter")
    @ResponseBody
    public JsonResponse filterProduct(@RequestParam("id") long categoryId,
                                      @RequestParam(value = "cid", defaultValue = "0") long childId,
                                      @RequestParam(value = "sort", defaultValue = "1") SortType sortType,
                                      PageQuery pageQuery) {
        JsonResponse jsonResponse = new JsonResponse();

        Category category =  childId > 0 ? categoryService.getCategoryById(childId) : categoryService.getCategoryById(categoryId);
        if (category == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        Map<String, Object> data = new HashMap<String, Object>();

        Collection<Long> categoryIds = category.getCategoryIds();
        int totalCount = productService.getProductCountOfCategory(categoryIds);
        data.put("pageQuery", PageQueryResult.copyFrom(pageQuery, totalCount));

        List<Product> products = productService.getProductOfCategory(categoryIds, sortType, pageQuery);
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        for (Product product : products) {
            productList.add(product.toSimpleMap());
        }
        data.put("products", productList);

        return jsonResponse.setSuccessful().setData(data);
    }
}
