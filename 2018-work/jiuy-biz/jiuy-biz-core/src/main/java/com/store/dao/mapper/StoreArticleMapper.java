package com.store.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.entity.newentity.StoreArticle;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 门店文章表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-02
 */
public interface StoreArticleMapper extends BaseMapper<StoreArticle> {

    @Deprecated
	long insertAndGetId(StoreArticle storeArticle);

    StoreArticle selectStoreArticle(Long articleId);

    /**
     * 查询文章列表(排序后)
     *
     * @param query query
     * @param pageQuery pageQuery
     * @param searchContext 是否包含文本字段, 开启后查询慢
     * @return java.util.List<com.jiuyuan.entity.newentity.StoreArticle>
     * @author Charlie
     * @date 2018/8/8 20:08
     */
    List<StoreArticle> listArticlesSort(
            @Param ("query") StoreArticle query,
            @Param ("pageQuery")PageQuery pageQuery,
            @Param ("searchContext")Boolean searchContext
    );


}