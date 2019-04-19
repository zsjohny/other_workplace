package com.jiuy.service.web;

import com.jiuy.base.model.MyPage;
import com.jiuy.model.web.article.OperatorArticle;
import com.jiuy.model.web.article.OperatorArticleQuery;
import com.jiuy.model.web.article.OperatorSeo;

import java.util.List;

/**
 * 文章service
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/24 15:46
 * @Copyright 玖远网络
 */
public interface IArticleService {

    /**
     * 获取文章列表
     * @param query query
     * @author Aison
     * @date 2018/5/24 15:51
     */
    MyPage<OperatorArticle> getArticleList(OperatorArticleQuery query);

    /**
     * 查询某一条新闻
     *
     * @param id id
     * @author Aison
     * @date 2018/7/31 11:01
     * @return com.jiuy.model.web.article.OperatorArticle
     */
    OperatorArticle getArticleById(Long id);

    /**
     *  获取seo的信息
     *
     * @param seoType seoType
     * @author Aison
     * @date 2018/7/31 11:57
     * @return com.jiuy.model.web.article.OperatorSeo
     */
    OperatorSeo getSeo(Integer seoType);



}
