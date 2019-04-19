package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.ad.Ad;

public interface AdDao extends DomainDao<Ad, Long> {

	public int a = 0;
	
	public int insertAd(List<Ad> adList);

	public int updateAd(Ad ad);

	public int deleteAd(Ad ad);

	public List<Ad> getAdListByType(Integer type);
}
