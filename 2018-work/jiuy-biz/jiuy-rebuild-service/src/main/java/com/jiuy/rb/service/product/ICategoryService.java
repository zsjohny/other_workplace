package com.jiuy.rb.service.product;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.product.CategoryRb;
import com.jiuy.rb.model.product.CategoryRbQuery;

import java.util.List;

/**
 * 商品类目的业务
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/14 10:01
 * @Copyright 玖远网络
 */
public interface ICategoryService {

    /**
     * 添加类目
     *
     * @param categoryRb 类目
     * @param optUser 操作用户
     * @author Aison
     * @date 2018/6/14 10:03
     */
    MyLog<CategoryRb> addCategory(CategoryRb categoryRb, UserSession optUser);


    /**
     *  类目的tree
     *
     * @param categoryRbQuery 查询实体
     * @author Aison
     * @date 2018/6/14 11:24
     */
    List<CategoryRbQuery> categoryRbMyList(CategoryRbQuery categoryRbQuery);


    /**
     * 修改某个类目
     * 不允许修改所属父类目
     *
     * @param categoryRb 要修改的
     * @param optUser 操作员
     * @author Aison
     * @date 2018/6/14 15:18
     */
    MyLog<CategoryRb> updateCategory(CategoryRbQuery categoryRb,UserSession optUser);
}
