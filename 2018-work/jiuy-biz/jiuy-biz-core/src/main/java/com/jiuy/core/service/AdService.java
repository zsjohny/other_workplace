package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.AdDao;
import com.jiuyuan.entity.ad.Ad;
import com.jiuyuan.entity.ad.AdVo;

@Service("adService")
public class AdService {

	@Resource
	private AdDao adDao;

	public List<AdVo> getAdLsit(Integer adType) {
		List<Ad> adList=adDao.getAdListByType(adType);
		List<AdVo> voList=new ArrayList<AdVo>();
		
		for(Ad ad:adList){
			AdVo vo=new AdVo();
			BeanUtils.copyProperties(ad, vo);
			voList.add(vo);
		}

		return voList;
	}

	public int updateAd(AdVo adVo) {
		Ad ad = new Ad();
		BeanUtils.copyProperties(adVo, ad);
		return adDao.updateAd(ad);
	}

	public int deleteAd(Long id) {
		Ad ad = new Ad();
		ad.setId(id);

		return adDao.deleteAd(ad);
	}

	public int createAd(List<AdVo> voList) {
		List<Ad> adList = new ArrayList<Ad>();

		for (AdVo vo : voList) {
			Ad ad = new Ad();
			BeanUtils.copyProperties(vo, ad);
			adList.add(ad);
		}

		return adDao.insertAd(adList);
	}
}
