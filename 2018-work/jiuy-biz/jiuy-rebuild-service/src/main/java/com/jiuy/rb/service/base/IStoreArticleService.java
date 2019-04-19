package com.jiuy.rb.service.base;

import com.jiuy.rb.model.base.StoreArticleRb;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/8 18:43
 * @Copyright 玖远网络
 */
public interface IStoreArticleService{


    /**
     * 创建一个文章
     *
     * @param article article
     * @return com.jiuy.rb.model.base.StoreArticleRb
     * @author Charlie
     * @date 2018/8/8 19:16
     */
    StoreArticleRb add(StoreArticleRb article);

    /**
     * 更新文章
     *
     * @param article article
     * @author Charlie
     * @date 2018/8/8 19:47
     */
    void update(StoreArticleRb article);
}
