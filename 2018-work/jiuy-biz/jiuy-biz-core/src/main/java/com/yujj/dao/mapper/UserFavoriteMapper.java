package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.FavoriteType;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.entity.favorite.UserLike;
import com.jiuyuan.entity.query.PageQuery;

@DBMaster
public interface UserFavoriteMapper {

	int addFavorite(UserFavorite userFavorite);
	
	int addLike(UserLike userLike);

    int deleteFavorite(@Param("userId") long userId, @Param("relatedId") long relatedId,
                       @Param("type") FavoriteType type);

    List<UserFavorite> getFavorites(@Param("userId") long userId, @Param("type") FavoriteType type,
                                    @Param("pageQuery") PageQuery pageQuery);
    
    List<Article> getFavoriteArticleList(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery);

    int getFavoritesCount(@Param("userId") long userId, @Param("type") FavoriteType type);

    UserFavorite getFavorite(@Param("userId") long userId, @Param("relatedId") long relatedId);

	List<Map<String, Object>> countById(@Param("relatedIds") Collection<Long> relatedIds, @Param("type") int type);

	List<UserFavorite> getUserFavorite(@Param("relatedIds") Collection<Long> relatedIds, @Param("type") int type, @Param("userId") long userId);

	List<UserLike> getUserLikeMap(@Param("relatedIds") Collection<Long> relatedIds, @Param("userId") long userId);

	List<Map<Long, Object>> getLikeCountById(@Param("relatedIds") Collection<Long> relatedIds);
}
