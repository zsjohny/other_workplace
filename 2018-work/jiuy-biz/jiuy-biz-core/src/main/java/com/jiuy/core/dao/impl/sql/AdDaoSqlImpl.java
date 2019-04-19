package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.AdDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.ad.Ad;

public class AdDaoSqlImpl extends DomainDaoSqlSupport<Ad, Long> implements AdDao {

	@Override
	public int insertAd(List<Ad> adList) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("collection", adList);
		int count = getSqlSession().insert("Ad.insertAd", map);
		return count;
	}

	@Override
	public int updateAd(Ad ad) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("adOrder", ad.getAdOrder());
		map.put("adType", ad.getAdType());
		map.put("imageUrl", ad.getImageUrl());
		map.put("linkUrl", ad.getLinkUrl());
		map.put("newPage", ad.getNewPage());
		map.put("id", ad.getId());
		map.put("adTitle", ad.getAdTitle());
		int count = getSqlSession().update("Ad.updateAd", map);
		return count;
	}

	@Override
	public int deleteAd(Ad ad) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ad.getId());
		int count = getSqlSession().delete("Ad.deleteAd", map);
		return count;
	}

	@Override
	public List<Ad> getAdListByType(Integer type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("adType", type);
		List<Ad> adList = getSqlSession().selectList("Ad.getAdListByType", map);
		return adList;
	}

	
}
