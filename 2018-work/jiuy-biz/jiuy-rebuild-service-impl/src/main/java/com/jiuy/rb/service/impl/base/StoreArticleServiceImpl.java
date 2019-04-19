package com.jiuy.rb.service.impl.base;

import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.Declare;
import com.jiuy.rb.mapper.base.StoreArticleRbMapper;
import com.jiuy.rb.model.base.StoreArticleRb;
import com.jiuy.rb.service.base.IStoreArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/8 18:43
 * @Copyright 玖远网络
 */
@Service( "storeArticleServiceImpl" )
public class StoreArticleServiceImpl implements IStoreArticleService{
    @Autowired
    private StoreArticleRbMapper storeArticleRbMapper;


    /**
     * 创建一个文章
     *
     * @param article article
     * @return com.jiuy.rb.model.base.StoreArticleRb
     * @author Charlie
     * @date 2018/8/8 19:16
     */
    @Override
    public StoreArticleRb add(StoreArticleRb article) {
        //验证内容长度:需求文本内容3000字符以内,但是包含图片,字符长度校验放在前端
//        article.getArticleContext ()
        Declare.existResource (article);
        String title = article.getArticleTitle ();
        Declare.notNull (StringUtils.isNotBlank (title), GlobalsEnums.BASE_TITLE_NO_NULL);
        //无法校验...
//        Declare.state (title.length () > 50, GlobalsEnums.BASE_TITLE_TOO_LONG);

        Long current = System.currentTimeMillis ();
        article.setCreateTime (current);
        article.setUpdateTime (current);
        storeArticleRbMapper.insertSelective (article);
        return article;
    }


    /**
     * 更新文章
     *
     * @param article article
     * @author Charlie
     * @date 2018/8/8 19:47
     */
    @Override
    public void update(StoreArticleRb article) {
        Declare.noNullParams (article, article.getId ());
        StoreArticleRb updInfo = new StoreArticleRb ();
        //如果没填,默认不更改(历史兼容)
        //标题
        String title = article.getArticleTitle ();
        if (!StringUtils.isBlank (title)) {
//            Declare.notNull (StringUtils.isBlank (title), GlobalsEnums.BASE_TITLE_NO_NULL);
//            Declare.state (title.length () > 50, GlobalsEnums.BASE_TITLE_TOO_LONG);
            updInfo.setArticleTitle (title);
        }
        //文本
        if (!StringUtils.isBlank (article.getArticleContext ())) {
            updInfo.setArticleContext (article.getArticleContext ());
            updInfo.setUpdateTime (System.currentTimeMillis ());
        }
        //图片
        if (!StringUtils.isBlank (article.getHeadImage ())) {
            updInfo.setHeadImage (article.getHeadImage ());
            updInfo.setUpdateTime (System.currentTimeMillis ());
        }
        updInfo.setId (article.getId ());
        updInfo.setTop (article.getTop ());
        updInfo.setStatus (article.getStatus ());
        updInfo.setPublicState (article.getPublicState ());

        //是否存在
        StoreArticleRb query = new StoreArticleRb ();
        query.setId (article.getId ());
        query.setStoreId (article.getStoreId ());
        StoreArticleRb history = storeArticleRbMapper.selectOne (query);
        Declare.existResource (history);

        storeArticleRbMapper.updateByPrimaryKeySelective (updInfo);
    }
}
