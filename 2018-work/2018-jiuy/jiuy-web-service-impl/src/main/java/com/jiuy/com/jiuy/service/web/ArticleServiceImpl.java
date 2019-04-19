package com.jiuy.com.jiuy.service.web;

import com.jiuy.base.model.MyPage;
import com.jiuy.mapper.web.article.OperatorArticleMapper;
import com.jiuy.mapper.web.article.OperatorSeoMapper;
import com.jiuy.model.web.article.OperatorArticle;
import com.jiuy.model.web.article.OperatorArticleQuery;
import com.jiuy.model.web.article.OperatorSeo;
import com.jiuy.model.web.article.OperatorSeoQuery;
import com.jiuy.service.web.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章services
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/24 15:47
 * @Copyright 玖远网络
 */
@Service("articleService")
public class ArticleServiceImpl implements IArticleService {


    private final OperatorArticleMapper operatorArticleMapper;

    private final OperatorSeoMapper operatorSeoMapper;

    @Autowired
    public ArticleServiceImpl(OperatorArticleMapper operatorArticleMapper, OperatorSeoMapper operatorSeoMapper) {
        this.operatorArticleMapper = operatorArticleMapper;
        this.operatorSeoMapper = operatorSeoMapper;
    }


    /**
     * 获取文章列表
     *
     * @param query
     * @author Aison
     * @date 2018/5/24 15:51
     */
    @Override
    public MyPage<OperatorArticle> getArticleList(OperatorArticleQuery query) {
        return new MyPage<>(operatorArticleMapper.selectList(query));
    }

    /**
     * 查询某一条新闻
     *
     * @param id id
     * @return com.jiuy.model.web.article.OperatorArticle
     * @author Aison
     * @date 2018/7/31 11:01
     */
    @Override
    public OperatorArticle getArticleById(Long id) {

        return operatorArticleMapper.selectByPrimaryKey(id);
    }


    /**
     *  获取seo的信息
     *
     * @param seoType seoType
     * @author Aison
     * @date 2018/7/31 11:57
     * @return com.jiuy.model.web.article.OperatorSeo
     */
    @Override
    public OperatorSeo getSeo(Integer seoType) {
        OperatorSeoQuery query = new OperatorSeoQuery();
        query.setStatus(0);
        query.setSeoType(seoType);
        return operatorSeoMapper.selectOne(query);
    }
}
