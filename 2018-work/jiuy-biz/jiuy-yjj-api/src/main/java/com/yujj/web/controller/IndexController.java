package com.yujj.web.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.ProductService;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;

@Controller
@RequestMapping("/index")
public class IndexController {

    private static Long[] categoryIds = new Long[]{ 36L, 1L, 2L, 3L };

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @RequestMapping
    public String index(Map<String, Object> model) {
        PageQuery pageQuery = new PageQuery(1, 5);

        for (int i = 0; i < categoryIds.length; i++) {
            long categoryId = categoryIds[i];
            Category category = categoryService.getCategoryById(categoryId);
            model.put("category" + categoryId, category);

            Collection<Long> cateIds = category.getCategoryIds();
            List<Product> products = productService.getProductOfCategory(cateIds, SortType.CREATE_TIME_DESC, pageQuery);
            model.put("productList" + categoryId, products);
        }

        return "index.html";
    }
}
