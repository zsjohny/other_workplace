package com.jiuy.operator.controller.newController.product;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.operator.common.system.persistence.model.User;
import com.jiuy.rb.model.product.CategoryRb;
import com.jiuy.rb.model.product.CategoryRbQuery;
import com.jiuy.rb.service.product.ICategoryService;
import com.jiuyuan.entity.Category;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/12 19:21
 * @Copyright 玖远网络
 */
@Controller
@ResponseBody
@RequestMapping("/admin/")
public class CategoryController {

    @Resource(name = "categoryService")
    private  ICategoryService categoryService;

    /**
     * 添加类目
     * @param category
     * @author Aison
     * @date 2018/6/5 18:14
     */
    @RequestMapping("addCategory")
    public ResponseResult addCategory(CategoryRb category) {

        category = categoryService.addCategory(category,UserSession.getUserSession()).getData();
        return ResponseResult.instance().success(category);
    }


    /**
     *  类目的tree
     *
     * @param categoryRbQuery 查询实体
     * @author Aison
     * @date 2018/6/14 12:14
     */
    @RequestMapping("categoryTree")
    public ResponseResult categoryTree(CategoryRbQuery categoryRbQuery) {

        return ResponseResult.instance().success(categoryService.categoryRbMyList(categoryRbQuery));
    }

    /**
     *  修改某个类目的信息
     *
     * @param categoryRb categoryRb
     * @author Aison
     * @date 2018/6/14 15:34
     */
    @RequestMapping("updateCategory")
    public ResponseResult updateCategory(CategoryRbQuery categoryRb) {

        return ResponseResult.instance().success(categoryService.updateCategory(categoryRb,UserSession.getUserSession()));
    }
}
