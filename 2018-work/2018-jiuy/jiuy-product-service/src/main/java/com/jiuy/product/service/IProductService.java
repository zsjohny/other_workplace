package com.jiuy.product.service;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.product.model.Category;
import com.jiuy.product.model.CategoryQuery;
import com.jiuy.product.model.Product;
import com.jiuy.product.model.ProductSku;

import java.util.List;
import java.util.Map;

/**
 * 商品接口
 * @author Aison
 * @version V1.0
 * @date 2018/6/5 16:02
 * @Copyright 玖远网络
 */
public interface IProductService {

    /**
     * 添加一个商品
     * @param product 商品封装类
     * @param user 操作用户
     * @author Aison
     * @date 2018/6/5 16:03
     * @return MyLog 返回日志
     */
    MyLog addProduct(Product product, UserSession user);

    /**
     * 添加sku
     * @param productSku sku
     * @param product 商品
     * @param user 操作用户
     * @author Aison
     * @date 2018/6/5 16:04
     * @return MyLog 返回日志
     */
    MyLog addProductSku(ProductSku productSku,Product product,UserSession user);


    /**
     * 审核商品
     * @param product 商品对象
     * @param optUser  操作用户
     * @param supplierId 供应商id
     * @author Aison
     * @date 2018/6/5 16:10
     * @return MyLog 返回日志
     */
    MyLog auditProduct(Product product,UserSession optUser,Long supplierId);


    /**
     * 添加商品类目
     * @param category 类目实体
     * @param optUser 操作人
     * @author Aison
     * @date 2018/6/5 16:48
     */
    MyLog<Long> addCategory(Category category,UserSession optUser);

    /**
     * 修改类目
     * @param categoryQuery 类目查询实体
     * @param optUser   操作用户
     * @author Aison
     * @date 2018/6/5 16:52
     */
    MyLog modifyCategory(CategoryQuery categoryQuery,UserSession optUser);


    /**
     * 查询类目的树
     * @param optUser 操作用户
     * @param query 查询实体
     * @author Aison
     * @date 2018/6/5 16:50
     */
    List<CategoryQuery> categoryTree(CategoryQuery query,UserSession optUser);



    void moveTemplate();



}
