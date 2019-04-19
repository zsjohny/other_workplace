package com.yujj.business.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.entity.favorite.UserLike;
import com.yujj.dao.mapper.UserFavoriteMapper;

@Service
public class UserFavoriteService {
	
	@Autowired
	private UserFavoriteMapper userFavoriteMapper;

	public Map<Long, Integer> countById(Collection<Long> relatedIds, int type) {
		if (relatedIds.size() < 1) {
			return new HashMap<>();
		}
		
		Map<Long, Integer> countById = new HashMap<>();
		List<Map<String, Object>> list = userFavoriteMapper.countById(relatedIds, type);
		for (Map<String, Object> map : list) {
			countById.put(Long.parseLong(map.get("RelatedId").toString()), Integer.parseInt(map.get("count").toString()));
		}
		
		return countById;
	}

	public Map<Long, UserFavorite> getUserFavoriteMap(Collection<Long> relatedIds, int type, long userId) {
		if (relatedIds.size() < 1) {
			return new HashMap<>();
		}
		
		Map<Long, UserFavorite> userFavoriteMap = new HashMap<>();
		List<UserFavorite> list = userFavoriteMapper.getUserFavorite(relatedIds, type, userId);
		for (UserFavorite userFavorite : list) {
			userFavoriteMap.put(userFavorite.getRelatedId(), userFavorite);
		}
		
		return userFavoriteMap;
	}

	public Map<Long, UserLike> getUserLikeMap(Collection<Long> relatedIds, long userId) {
		if (relatedIds.size() < 1) {
			return new HashMap<>();
		}
		
		Map<Long, UserLike> userLikeMap = new HashMap<>();
		List<UserLike> list = userFavoriteMapper.getUserLikeMap(relatedIds, userId);
		for (UserLike userLike : list) {
			userLikeMap.put(userLike.getRelatedId(), userLike);
		}
		
		return userLikeMap;
	}

	public Map<Long, Integer> getLikeCountById(Collection<Long> relatedIds) {
		if (relatedIds.size() < 1) {
			return new HashMap<>();
		}
		
		Map<Long, Integer> likeCountById = new HashMap<>();
		List<Map<Long, Object>> list = userFavoriteMapper.getLikeCountById(relatedIds);
		for (Map<Long, Object> map : list) {
			likeCountById.put(Long.parseLong(map.get("RelatedId").toString()), Integer.parseInt(map.get("count").toString()));
		}
		
		return likeCountById;
	}
}
