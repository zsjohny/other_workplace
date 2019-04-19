package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.article.ArticleVO;
import com.jiuyuan.entity.query.PageQuery;

@DBMaster
public interface ArticleMapper {

	Article getArticleById(@Param("id") long id);

	public List<Article> getQuestionList(@Param("categoryId") long categoryId, @Param("pageQuery") PageQuery pageQuery);
	
	List<Article> getUserFavoriteArticleList(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery);
	
	List<ArticleVO> getUserFavoriteArticleVOList(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery);

	int addPageView(@Param("id") long id);

	List<Article> getCommunityArticals(@Param("pageQuery") PageQuery pageQuery, @Param("arCategoryId") Long arCategoryId);

	int getCommunityArticalsCount( @Param("arCategoryId") Long arCategoryId);
	
	int getFavoriteArticleCount( @Param("userId") long userId);

}
