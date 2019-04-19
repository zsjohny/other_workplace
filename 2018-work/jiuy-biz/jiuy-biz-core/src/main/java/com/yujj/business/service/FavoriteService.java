package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.FavoriteType;
import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.entity.favorite.UserLike;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.dao.mapper.UserFavoriteMapper;

@Service
public class FavoriteService {

    @Autowired
    private UserFavoriteMapper favoriteMapper;

    public int addFavorite(long userId, long relatedId, FavoriteType type) {
		UserFavorite favorite = new UserFavorite();
		favorite.setUserId(userId);
		favorite.setRelatedId(relatedId);
		favorite.setType(type.getIntValue());
		return favoriteMapper.addFavorite(favorite);
    }
    
    public int addLike(long userId, long relatedId) {
    	UserLike userLike = new UserLike();
    	userLike.setUserId(userId);
    	userLike.setRelatedId(relatedId);
    	long sysTime = System.currentTimeMillis();
    	userLike.setCreateTime(sysTime);
    	userLike.setUpdateTime(sysTime);
    	return favoriteMapper.addLike(userLike);
    }

	public int deleteFavorite(long userId, long relatedId, FavoriteType type) {

		return favoriteMapper.deleteFavorite(userId, relatedId, type);
	}
	
    public List<UserFavorite> getFavorites(long userId, FavoriteType type, PageQuery pageQuery) {
		return favoriteMapper.getFavorites(userId, type, pageQuery);
	}
    
//    public List<Article> getFavoriteArticleList(long userId, PageQuery pageQuery) {
//    	return favoriteMapper.getFavoriteArticleList(userId, pageQuery);
//    }

	public int getFavoritesCount(long userId, FavoriteType type) {
		return favoriteMapper.getFavoritesCount(userId, type);
	}
	
    public UserFavorite getFavorite(long userId, long relatedId) {
        return favoriteMapper.getFavorite(userId, relatedId);
    }

}
