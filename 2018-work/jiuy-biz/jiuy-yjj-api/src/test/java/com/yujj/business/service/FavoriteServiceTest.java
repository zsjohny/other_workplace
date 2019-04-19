package com.yujj.business.service;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyuan.constant.FavoriteType;
import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.test.util.YjjAbstractUnitilsTest;

public class FavoriteServiceTest extends YjjAbstractUnitilsTest {

	@Autowired
	private FavoriteService favoriteService;

	@Test
	@Ignore
	public void testAddFavorite() {
		int count = favoriteService.addFavorite(1L, 1L, FavoriteType.PRODUCT);
	}

	@Test
	@Ignore
	public void testDeleteFavorite() {
		int count = favoriteService
				.deleteFavorite(1L, 1L, FavoriteType.PRODUCT);
	}

	@Test
	@Ignore
	public void testGetFavorites() {

		List<UserFavorite> list = favoriteService.getFavorites(1L,
				FavoriteType.PRODUCT, new PageQuery());
	}

	@Test
	@Ignore
	public void testGetFavoritesCount() {
		int count = favoriteService.getFavoritesCount(1L, FavoriteType.PRODUCT);
	}
	

}
