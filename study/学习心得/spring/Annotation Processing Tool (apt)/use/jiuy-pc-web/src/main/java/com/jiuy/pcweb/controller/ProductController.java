package com.jiuy.pcweb.controller;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.product.model.Category;
import com.jiuy.product.service.IProductService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品controller
 * @author Aison
 * @version V1.0
 * @date 2018/6/5 18:12
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/admin/")
public class ProductController {

    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * 添加类目
     * @param category
     * @author Aison
     * @date 2018/6/5 18:14
     */
    @RequestMapping("addCategory")
    public ResponseResult addCategory(Category category) {

       Long categoryId = productService.addCategory(category,UserSession.getUserSession()).getData();
       return ResponseResult.instance().success(categoryId);
    }

}
